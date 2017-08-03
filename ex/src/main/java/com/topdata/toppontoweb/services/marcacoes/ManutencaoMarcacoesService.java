package com.topdata.toppontoweb.services.marcacoes;

import com.topdata.toppontoweb.dto.exportar.ExportarStatusTransfer;
import com.topdata.toppontoweb.dto.marcacoes.ManutencaoMarcacoesFuncionarioTransfer;
import com.topdata.toppontoweb.dto.funcionario.FuncionarioTransfer;
import com.topdata.toppontoweb.dto.gerafrequencia.GeraFrequenciaStatusTransfer;
import com.topdata.toppontoweb.dto.marcacoes.ManutencaoMarcacoesStatusTransfer;
import com.topdata.toppontoweb.dto.marcacoes.ManutencaoSaidaDiaTransfer;
import com.topdata.toppontoweb.dto.marcacoes.MarcacoesFuncionarioTransfer;
import com.topdata.toppontoweb.dto.relatorios.RelatoriosGeraFrequenciaTransfer;
import com.topdata.toppontoweb.entity.Entidade;
import com.topdata.toppontoweb.entity.autenticacao.Operador;
import com.topdata.toppontoweb.entity.funcionario.Funcionario;
import com.topdata.toppontoweb.entity.funcionario.FuncionarioDiaNaoDescontaDSR;
import com.topdata.toppontoweb.entity.marcacoes.Marcacoes;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoService;
import static com.topdata.toppontoweb.services.TopPontoService.LOGGER;
import com.topdata.toppontoweb.services.empresa.EmpresaService;
import com.topdata.toppontoweb.services.funcionario.FuncionarioService;
import com.topdata.toppontoweb.services.funcionario.dsr.FuncionarioDiaNaoDescontaDSRService;
import com.topdata.toppontoweb.services.gerafrequencia.entity.calculo.EntradaApi;
import com.topdata.toppontoweb.services.gerafrequencia.entity.calculo.SaidaDia;
import com.topdata.toppontoweb.services.gerafrequencia.entity.marcacoes.MarcacoesDia;
import com.topdata.toppontoweb.services.gerafrequencia.integracao.EntradaPadrao;
import com.topdata.toppontoweb.services.gerafrequencia.integracao.GeraFrequencia;
import com.topdata.toppontoweb.services.gerafrequencia.integracao.GeraFrequenciaServices;
import com.topdata.toppontoweb.services.gerafrequencia.integracao.SaidaPadrao;
import com.topdata.toppontoweb.services.gerafrequencia.services.calculo.CalculoPeriodoService;
import com.topdata.toppontoweb.services.permissoes.OperadorService;
import com.topdata.toppontoweb.services.relatorios.RelatorioHandler;
import com.topdata.toppontoweb.utils.Utils;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author tharle.camargo
 */

@Service
public class ManutencaoMarcacoesService extends TopPontoService<Entidade, Object> {
    
    @Autowired
    FuncionarioService funcionarioService;
    
    @Autowired
    EmpresaService empresaService;
    
    @Autowired
    private GeraFrequencia geraFrequencia;
    
    @Autowired
    private OperadorService operadorService;
    
    @Autowired
    private EntradaPadrao entradaPadrao;
    
    @Autowired
    private FuncionarioDiaNaoDescontaDSRService funcionarioDiaNaoDescontaDSRService;
    
    @Autowired
    private MarcacoesService marcacoesService;
    
    private final static HashMap<String, ManutencaoMarcacoesStatusTransfer> PROGRESSO_MAP = new HashMap<>();
    
    private static final long DELAY = 4000;
    private static final long PERIOD = 4000;

    public Response iniciarProcessoManutecaoMarcacoes(HttpServletRequest request, RelatoriosGeraFrequenciaTransfer transfer) throws ServiceException {
        Operador operador = operadorService.getOperadorDaSessao(request);
        
        //Objeto de uso para o gera frequencia
        transfer.setTipo_relatorio(RelatorioHandler.TIPO_RELATORIO.MANUTENCAO_MARCACOES);
        transfer.setMarcacoesInvalidas(true);
        transfer.setOperador(operador);

        ManutencaoMarcacoesStatusTransfer status = criarStatus();
        status.setProgresso(0);

        Thread thread = new Thread(() -> {
           processarManutencaoMarcacoes(transfer, status);
        }, "processo-manutencao-marcacoes-id-"+status.getId());
        thread.start();

        status.setThread(thread);

        //Cria o monitor de timeout
        criarTimerMonitor(status);



        return getTopPontoResponse().sucessoSalvar(status);
    }
    
    public void processarManutencaoMarcacoes(RelatoriosGeraFrequenciaTransfer transfer, ManutencaoMarcacoesStatusTransfer status){
        
        try {
            
            //inicio de calculo do gera frequencia
            GeraFrequenciaStatusTransfer statusGeraFrequencia = this.geraFrequencia.iniciar(null, transfer, CONSTANTES.Enum_TIPO_PROCESSO.MANUTENCAO_MARCACOES);
            status.setGeraFrequenciaStatusTransfer(statusGeraFrequencia);
            
            status.setStatus(CONSTANTES.PROCESSO_STATUS.CARREGANDO_DADOS_GERA_FREQUENCIA);
            
            //GeraFrequenciaStatusTransfer leva 60% do processo
            while(!status.isCancelar()){
                
                //Espera até o "gera frequencia" terminar e (se existir marcações invalidas) a resposta de processar-las 
                try{
                    if(statusGeraFrequencia != null && statusGeraFrequencia.getEstado_processo().equals(RelatorioHandler.ESTADO_PROCESSO.CALCULO_CONCLUIDO)){//
                        break;
                    }else if(statusGeraFrequencia != null && statusGeraFrequencia.getEstado_processo().equals(RelatorioHandler.ESTADO_PROCESSO.ERRO)){
                        //Ocorreu um erro, sai da função e finaliza tudo
                        status.setStatus(CONSTANTES.PROCESSO_STATUS.ERRO_CARREGAR_DADOS);
                        return;
                    }
                }catch(NullPointerException ex){
                    LOGGER.debug("Manutencação Marcações - Nullpointer - " + ex.getMessage());
                }
            }
            
            if(status.isCancelar()){
                LOGGER.debug("Não incia 'Manutencação Marcações', pois, foi cancelado.");
                status.setStatus(CONSTANTES.PROCESSO_STATUS.CANCELADO);
                return;
            }
            
            status.setProgresso(60);
            status.setStatus(CONSTANTES.PROCESSO_STATUS.CONFIGURANDO_MARCACOES);
            
            //35%
            List<SaidaPadrao> saidaPadraoList = statusGeraFrequencia != null? statusGeraFrequencia.getSaidaPadraoList(): new ArrayList<>();
            List<ManutencaoMarcacoesFuncionarioTransfer> manutencaoMarcacoesFuncionarioTransferList = processarSaidaPadrao(saidaPadraoList, status);
            status.setManutencaoMarcacaoFuncionarioList(manutencaoMarcacoesFuncionarioTransferList);
            
            //5%
            status.setProgresso(100);
            status.setStatus(CONSTANTES.PROCESSO_STATUS.CONCLUIDO);
        } catch (ServiceException ex) {
            Logger.getLogger(ManutencaoMarcacoesService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public List<ManutencaoMarcacoesFuncionarioTransfer> processarSaidaPadrao(List<SaidaPadrao> saidaPadraoList, ManutencaoMarcacoesStatusTransfer status){
        List<ManutencaoMarcacoesFuncionarioTransfer> manutencaoMarcacoesFuncionarioList = new ArrayList<>();
        double percentual = 35d/(saidaPadraoList.size() > 0? saidaPadraoList.size() : 1);
        saidaPadraoList.stream().forEach(saidaPadrao->{
            status.addProgresso(percentual);
            Funcionario funcionario = saidaPadrao.getFuncionario();
            boolean possuiInconsistencias = saidaPadrao.getSaidaDiaList().stream().anyMatch(r -> r.getRegras().isInconsistencia());
            
            FuncionarioTransfer funcionarioTransfer = new FuncionarioTransfer(funcionario, false);
            ManutencaoMarcacoesFuncionarioTransfer transfer = new ManutencaoMarcacoesFuncionarioTransfer(funcionarioTransfer, possuiInconsistencias);
            manutencaoMarcacoesFuncionarioList.add(transfer);
        });
        
        return manutencaoMarcacoesFuncionarioList;
    }
    
    public Response cancelarProgresso(String id){
        ManutencaoMarcacoesStatusTransfer status = this.buscarStatus(id);
        if(status != null){
            status.setCancelar(true);
            status.setStatus(CONSTANTES.PROCESSO_STATUS.CANCELADO);
            return getTopPontoResponse().sucessoAtualizar(status); 
        }else{
            //Não achou, deve ter sido finalizado já, então retorna um status vazio
            return getTopPontoResponse().sucessoAtualizar(new ExportarStatusTransfer(id)); 
        }
        
        
    }
    
    public Response buscarProgresso(String id){
        ManutencaoMarcacoesStatusTransfer status = this.buscarStatus(id);
        
        //Atualiza data
        status.atualizarDataAtualizacao();
        
        GeraFrequenciaStatusTransfer geraFrequenciaStatus = status.getGeraFrequenciaStatusTransfer();
        
        
        if(geraFrequenciaStatus!=null) {
            
            LOGGER.debug("PERCENTUAL DO GERA FREQUENCIA (REFERENCIA): " +geraFrequenciaStatus.getPercentual());
            GeraFrequenciaStatusTransfer g2 = GeraFrequenciaServices.STATUS_MAP.get(geraFrequenciaStatus.getId());
            LOGGER.debug("PERCENTUAL DO GERA FREQUENCIA (MATRIZ): " + (g2 != null? g2.getPercentual(): "<<NÃO EXISTE NO MAPA!>>"));

            LOGGER.debug("STATUS DO PROCESSO DE EXPORTACAO" + status.getStatus());
            
            geraFrequenciaStatus.setData(new Date());
            
            if(status.getStatus().equals(CONSTANTES.PROCESSO_STATUS.CARREGANDO_DADOS_GERA_FREQUENCIA)){
                status.setProgresso(geraFrequenciaStatus.getPercentual());
            }
        }
        
        //Existe resultado? Então é a ultima vez que está sendo chamado
        if(status.getStatus() != null && status.getStatus().equals(CONSTANTES.PROCESSO_STATUS.CONCLUIDO)){
            this.removerStatus(id);
        }
        
        return this.getTopPontoResponse().sucessoSalvar(status);
    }
    
    public ManutencaoMarcacoesStatusTransfer buscarStatus(String id) {
        
        return ManutencaoMarcacoesService.PROGRESSO_MAP.get(id);
    }
    
    public ManutencaoMarcacoesStatusTransfer criarStatus() {
        removerStatusObsoletos();
        
        ManutencaoMarcacoesStatusTransfer status = new ManutencaoMarcacoesStatusTransfer();
        ManutencaoMarcacoesService.PROGRESSO_MAP.put(status.getId(), status);
        
        return status;
    }

    public void removerStatusObsoletos(){
        HashMap<String, ManutencaoMarcacoesStatusTransfer> progressoMapClone =  (HashMap<String, ManutencaoMarcacoesStatusTransfer>) ManutencaoMarcacoesService.PROGRESSO_MAP.clone();
        progressoMapClone.forEach((id, progresso)->{
            //Calcula a diferença em minutos do tempo da ultima atualizacao até agora
            long emMili = new Date().getTime() - progresso.getDataAtualizacao().getTime();
            long emMinutos = emMili / (1000 * 60);
            
            //Se passou mais que 5 minutos, então remove o progresso
            if(emMinutos > 5 ){
                removerStatus(progresso.getId());
            }
        });
    }
    
    public void removerStatus(String id) {
        ManutencaoMarcacoesService.PROGRESSO_MAP.remove(id);
    }
    
        /**
     * Cria o Timer que servirá como monitor da Thread principal. Este timer tem
     * como objetivo cancelar a exportacao caso ela não seja verificada pelo front em 10s. 
     * O tempo de Reset é determinado pelas contantes DELAY e PERIOD.
     *
     * @param statusTransfer
     */
    private void criarTimerMonitor(ManutencaoMarcacoesStatusTransfer status) {
        Timer timer = new Timer("Manutencao Marcações - Monitor processo - " + status.getId() + " - " + status.getStatus() );
        
        TimerTask task = new TimerTask() {
            
            @Override
            public void run() {
                try{
                    if(status.getThread() == null || !status.getThread().isAlive() || status.isCancelar() || status.getProgresso() >= 100 ){
                        LOGGER.debug("Manutencao Marcações - Monitor finalizado - Processo - " + status.getId());
                        this.cancel();

                        //Se o processo ainda nao foi concluido, entao cancela-o
                        GeraFrequenciaStatusTransfer statusGeraFrequencia = status.getGeraFrequenciaStatusTransfer();
                        statusGeraFrequencia.getCalculoPrincipal().setParar(Boolean.TRUE);
                    }else{
                        Calendar c = Calendar.getInstance();
                        c.add(Calendar.SECOND, -10);//espera 10 segundos

                        if (status.getDataAtualizacao().before(c.getTime())) {
                            LOGGER.debug("Manutencao Marcações - Processo cancelado por TimeOut : Processo - " + status.getId());
                            status.setCancelar(true);
                        }
                    }
                }catch(NullPointerException nullpointer){
                    LOGGER.debug("NULLPOINTER >> ");
                    nullpointer.printStackTrace();
                }
                
                
            }
        };
        
        timer.schedule(task, DELAY, PERIOD);
    }

    public Response buscarMarcacoesPorFuncionarioPeriodo(Integer idFuncionario, Date periodoInicio, Date periodoFim) throws ServiceException {
        Funcionario funcionario = this.funcionarioService.buscar(Funcionario.class, idFuncionario);
        //menor data para corregar os dados da base
        Date dataInicio = Utils.configuraHorarioData(periodoInicio, 0, 0, 0);
        Date dataTermino = Utils.configuraHorarioData(periodoFim, 23, 59, 59);
        
        //monta os dados da base na entrada da api
        EntradaApi entradaApi = this.entradaPadrao.getEntrada(funcionario, dataInicio, dataTermino);

        //configurações específicas da API
        entradaApi.setDataInicioPeriodo(Utils.configuraHorarioData(periodoInicio, 0, 0, 0));
        entradaApi.setDataFimPeriodo(Utils.configuraHorarioData(periodoFim, 23, 59, 59));

        entradaApi.setProcessaCalculo(true);
        entradaApi.setProcessaACJEF(false);
        entradaApi.setProcessaAFDT(false);
        entradaApi.setProcessaAbsenteismo(false);
        entradaApi.setProcessaDSR(false);

        //instancia a classe principal da API
        CalculoPeriodoService calculoPeriodoService = new CalculoPeriodoService(entradaApi);

        //solicita o calculo e 
        List<SaidaDia> saidaDiaList = calculoPeriodoService.getSaidaAPI();//.stream().sorted(Comparator.comparing((SaidaDia s)
        
        List<ManutencaoSaidaDiaTransfer> manutencaoSaidaDiaList = new ArrayList<>();
        for (SaidaDia saidaDia : saidaDiaList) {
            List<FuncionarioDiaNaoDescontaDSR> funcionarioDiaNaoDescontaDSRList  = funcionarioDiaNaoDescontaDSRService.buscarPorFuncionarioData(funcionario, saidaDia.getData());
            
            boolean descontarDSR = funcionarioDiaNaoDescontaDSRList.size() <= 0;
            
            List<Marcacoes> marcacoesList = new ArrayList<>();
            for (MarcacoesDia marcacoesDia : saidaDia.getHorariosTrabalhadosList()) {
                 Marcacoes marcacaoEntrada = marcacoesService.buscar(Marcacoes.class, marcacoesDia.getIdMarcacaoEntrada());
                 marcacoesList.add(marcacaoEntrada);
                 Marcacoes marcacaoSaida = marcacoesService.buscar(Marcacoes.class, marcacoesDia.getIdMarcacaoSaida());
                 marcacoesList.add(marcacaoSaida);
            }
            
            manutencaoSaidaDiaList.add(new ManutencaoSaidaDiaTransfer(saidaDia.getData(), saidaDia.getHorariosTrabalhadosOriginaisEquipamentoList(), marcacoesList, descontarDSR) );
        }
        
        FuncionarioTransfer transfer = new FuncionarioTransfer(funcionario, false);
        MarcacoesFuncionarioTransfer marcacoesFuncionarioTransfer = new MarcacoesFuncionarioTransfer(transfer, manutencaoSaidaDiaList);
        return this.getTopPontoResponse().sucessoSalvar(marcacoesFuncionarioTransfer);
    }
    
}
