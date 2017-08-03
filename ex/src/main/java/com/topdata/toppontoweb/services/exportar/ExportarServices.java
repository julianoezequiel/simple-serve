/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.topdata.toppontoweb.services.exportar;

import com.topdata.toppontoweb.dto.exportar.ExportarHandler;
import com.topdata.toppontoweb.dto.exportar.ExportarStatusTransfer;
import com.topdata.toppontoweb.dto.exportar.ExportarTransfer;
import com.topdata.toppontoweb.dto.gerafrequencia.GeraFrequenciaStatusTransfer;
import com.topdata.toppontoweb.dto.relatorios.RelatoriosGeraFrequenciaTransfer;
import com.topdata.toppontoweb.entity.Entidade;
import com.topdata.toppontoweb.entity.autenticacao.Operador;
import com.topdata.toppontoweb.entity.empresa.Empresa;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoService;
import static com.topdata.toppontoweb.services.TopPontoService.LOGGER;
import com.topdata.toppontoweb.services.empresa.EmpresaService;
import com.topdata.toppontoweb.services.funcionario.FuncionarioService;
import com.topdata.toppontoweb.services.gerafrequencia.integracao.GeraFrequencia;
import com.topdata.toppontoweb.services.gerafrequencia.integracao.GeraFrequenciaServices;
import com.topdata.toppontoweb.services.gerafrequencia.integracao.SaidaPadrao;
import com.topdata.toppontoweb.services.permissoes.OperadorService;
import com.topdata.toppontoweb.services.relatorios.RelatorioHandler;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author tharle.camargo
 */
@Service
public class ExportarServices extends TopPontoService<Entidade, Object> {
    
    @Autowired
    private EmpresaService empresaService;
    
    @Autowired
    private GeraFrequencia geraFrequencia;
    
    @Autowired
    private FuncionarioService funcionarioService;
    
    @Autowired
    private OperadorService operadorService;
    
    private final static HashMap<String, ExportarStatusTransfer> PROGRESSO_MAP = new HashMap<>();
    
    private Operador operador;
    
    private static final long DELAY = 4000;
    private static final long PERIOD = 4000;
    
    public Response exportarArquivo(ExportarTransfer transfer, ExportarHandler handler, HttpServletRequest request ) throws ServiceException {
        
        Empresa empresa = empresaService.buscar(transfer.getEmpresa().getIdEmpresa());
        transfer.setEmpresa(empresa);
            
        ExportarStatusTransfer status = criarStatus();
        status.setProgresso(0);
        
        this.operador = operadorService.getOperadorDaSessao(request);

        Thread thread = new Thread(() -> {
           gerarArquivo(transfer, handler, status);
        }, "processo-exportar-"+handler.name()+"-id-"+status.getId());
        thread.start();

        status.setThread(thread);

        //Cria o monitor de timeout
        criarTimerMonitor(status);



        return getTopPontoResponse().sucessoSalvar(status);
    }
    
    private void gerarArquivo(ExportarTransfer transfer, ExportarHandler handler, ExportarStatusTransfer status){
        try{
            //Carregar dados
            GeraFrequenciaStatusTransfer statusGeraFrequencia = processarDadosGeraFrequencia(transfer, status, handler.getTipoRelatorio());
            
            status.setMensagem(CONSTANTES.PROCESSO_STATUS.CARREGANDO_DADOS_GERA_FREQUENCIA);
            
            //Inicia em 5%
            //GeraFrequenciaStatusTransfer leva 60% do processo
            while(!status.isCancelar()){
                
                //Espera até o "gera frequencia" terminar e (se existir marcações invalidas) a resposta de processar-las 
                try{
                    if(statusGeraFrequencia != null && statusGeraFrequencia.getEstado_processo().equals(RelatorioHandler.ESTADO_PROCESSO.CALCULO_CONCLUIDO)){//
                        if(!statusGeraFrequencia.isPossuiInconsistencias() || status.isProcessarMarcacoesInvalidas() ){
                            break;
                        }
                    }else if(statusGeraFrequencia != null && statusGeraFrequencia.getEstado_processo().equals(RelatorioHandler.ESTADO_PROCESSO.ERRO)){
                        //Ocorreu um erro, sai da função e finaliza tudo
                        status.setMensagem(CONSTANTES.PROCESSO_STATUS.ERRO_CARREGAR_DADOS);
                        return;
                    }
                }catch(NullPointerException ex){
                    LOGGER.debug("Exportação - Nullpointer - " + ex.getMessage());
                }
            }
            
            if(status.isCancelar()){
                LOGGER.debug("Não incia exportar arquivo, pois, foi cancelado.");
                return;
            }
            
            //Verifica se o objeto n está vazio
            List<SaidaPadrao> saidaPadraoList = statusGeraFrequencia != null? statusGeraFrequencia.getSaidaPadraoList(): new ArrayList<>();
            
            
            status.setMensagem(CONSTANTES.PROCESSO_STATUS.GERANDO_ARQUIVO);
            
            BufferedWriter buffer = null;
            try {
                final String nomeCompletoArquivo = System.getProperty("java.io.tmpdir") + File.separator + status.getId()+".txt";
                File arquivo = new File(nomeCompletoArquivo);
                arquivo.createNewFile();
                
                FileWriter fileW = new FileWriter (arquivo);//arquivo para escrita
                buffer = new BufferedWriter (fileW);
                
                //Sobre 40% para gerar o relatorio
                //Gerar arquivo
                handler.gerarArquivo(transfer, status, saidaPadraoList, buffer, 40);
                status.setNomeArquivo(nomeCompletoArquivo);
                
                status.setMensagem(CONSTANTES.PROCESSO_STATUS.CONCLUIDO);
            } catch (FileNotFoundException ex) {
                LOGGER.debug(ex.getMessage());
                status.setMensagem(CONSTANTES.PROCESSO_STATUS.ERRO_GERAR_ARQUIVO);
            } catch (IOException ex) {
                LOGGER.debug(ex.getMessage());
                status.setMensagem(CONSTANTES.PROCESSO_STATUS.ERRO_GERAR_ARQUIVO);
            }finally{
                if(buffer != null){
                    try {
                        buffer.close();
                    } catch (IOException ex) {
                        LOGGER.debug(ex.getMessage());
                    }
                }
            }
            
            status.setProgresso(100);
        }catch(ServiceException ex){
            LOGGER.debug("Exportação - Erro - " + ex.getMessage());
        }
    }
    
    /**
     * Inicia o processo de fechamento.
     *
     * @param transfer
     * @param status
     * @param tipoRelatorio
     * @return GeraFrequenciaStatusTransfer
     * @throws ServiceException
     */
    public synchronized GeraFrequenciaStatusTransfer processarDadosGeraFrequencia(ExportarTransfer transfer, ExportarStatusTransfer status, RelatorioHandler.TIPO_RELATORIO tipoRelatorio) throws ServiceException {

        LOGGER.debug("Processando API para exportação");

        Empresa empresa = this.empresaService.buscar(transfer.getEmpresa().getIdEmpresa());

        //Objeto de uso para o gera frequencia
        RelatoriosGeraFrequenciaTransfer relatoriosGeraFrequencia = new RelatoriosGeraFrequenciaTransfer();
        relatoriosGeraFrequencia.setPeriodoInicio(transfer.getPeriodoInicio());
        relatoriosGeraFrequencia.setPeriodoFim(transfer.getPeriodoFim());
        relatoriosGeraFrequencia.setEmpresa(empresa);
        relatoriosGeraFrequencia.setEmpresaAtiva(Boolean.TRUE);
        relatoriosGeraFrequencia.setDepartamentoAtivo(Boolean.TRUE);
        relatoriosGeraFrequencia.setFuncionarioAtivo(Boolean.TRUE);
        relatoriosGeraFrequencia.setTipo_relatorio(tipoRelatorio);
        relatoriosGeraFrequencia.setMarcacoesInvalidas(true);
        relatoriosGeraFrequencia.setOperador(this.operador);

        //inicio de calculo do gera frequencia
        GeraFrequenciaStatusTransfer geraFrequenciaStatus 
                = this.geraFrequencia.iniciar(null, relatoriosGeraFrequencia, CONSTANTES.Enum_TIPO_PROCESSO.EXPORTAR_ARQUIVO);
        
        status.setGeraFrequenciaStatusTransfer(geraFrequenciaStatus);
//        geraFrequenciaStatus.setOperador(this.operadorService.getOperadorDaSessao(request));

//        if (efp.getIdEmpresaFechamentoPeriodo() != null) {
//            geraFrequenciaStatus.mapAux.put(ID_EMPRESA_FECHAMENTO_PERIODO, efp.getIdEmpresaFechamentoPeriodo());
//        }
//        geraFrequenciaStatus.mapAux.put(ID_EMPRESA, empresa.getIdEmpresa());
//        geraFrequenciaStatus.mapAux.put(ID_PROCESSO, idProcesso);

        return geraFrequenciaStatus;

    }

    
    public Response cancelarExportacao(String id){
        ExportarStatusTransfer status = this.buscarStatus(id);
        if(status != null){
            status.setCancelar(true);
            status.setMensagem(CONSTANTES.PROCESSO_STATUS.CANCELADO);
            return getTopPontoResponse().sucessoAtualizar(status); 
        }else{
            //Não achou, deve ter sido finalizado já, então retorna um status vazio
            return getTopPontoResponse().sucessoAtualizar(new ExportarStatusTransfer(id)); 
        }
        
        
    }
    
    public Response buscarExportacao(String id){
        ExportarStatusTransfer status = this.buscarStatus(id);
        
        //Atualiza data
        status.atualizarDataAtualizacao();
        
        GeraFrequenciaStatusTransfer geraFrequenciaStatus = status.getGeraFrequenciaStatusTransfer();
        
        
        if(geraFrequenciaStatus!=null) {
            
            LOGGER.debug("PERCENTUAL DO GERA FREQUENCIA (REFERENCIA): " +geraFrequenciaStatus.getPercentual());
            GeraFrequenciaStatusTransfer g2 = GeraFrequenciaServices.STATUS_MAP.get(geraFrequenciaStatus.getId());
            LOGGER.debug("PERCENTUAL DO GERA FREQUENCIA (MATRIZ): " + (g2 != null? g2.getPercentual(): "<<NÃO EXISTE NO MAPA!>>"));

            LOGGER.debug("STATUS DO PROCESSO DE EXPORTACAO" + status.getMensagem());
            
            geraFrequenciaStatus.setData(new Date());
            
            if(status.getMensagem().equals(CONSTANTES.PROCESSO_STATUS.CARREGANDO_DADOS_GERA_FREQUENCIA)){
                status.setProgresso(geraFrequenciaStatus.getPercentual());
            }
        }
        
        //Existe resultado? Então é a ultima vez que está sendo chamado
        if(status.getMensagem().equals(CONSTANTES.PROCESSO_STATUS.CONCLUIDO)
            || status.getMensagem().equals(CONSTANTES.PROCESSO_STATUS.ERRO_ABRIR_ARQUIVO)
            || status.getMensagem().equals(CONSTANTES.PROCESSO_STATUS.ERRO_AUDITAR) ){
            this.removerStatus(id);
        }
        
        return this.getTopPontoResponse().sucessoSalvar(status);
    }
    
    public ExportarStatusTransfer buscarStatus(String id) {
        
        return ExportarServices.PROGRESSO_MAP.get(id);
    }
    
    public ExportarStatusTransfer criarStatus() {
        removerStatusObsoletos();
        
        ExportarStatusTransfer progresso = new ExportarStatusTransfer();
        ExportarServices.PROGRESSO_MAP.put(progresso.getId(), progresso);
        
        return progresso;
    }
    
    public void removerStatusObsoletos(){
        HashMap<String, ExportarStatusTransfer> progressoMapClone =  (HashMap<String, ExportarStatusTransfer>) ExportarServices.PROGRESSO_MAP.clone();
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
        ExportarServices.PROGRESSO_MAP.remove(id);
    }
    
    /**
     * Cria o Timer que servirá como monitor da Thread principal. Este timer tem
     * como objetivo cancelar a exportacao caso ela não seja verificada pelo front em 10s. 
     * O tempo de Reset é determinado pelas contantes DELAY e PERIOD.
     *
     * @param statusTransfer
     */
    private void criarTimerMonitor(ExportarStatusTransfer status) {
        Timer timer = new Timer("Exportação - Monitor processo - " + status.getId() + " - " + status.getMensagem() );
        
        TimerTask task = new TimerTask() {
            
            @Override
            public void run() {
                try{
                    if(status.getThread() == null || !status.getThread().isAlive() || status.isCancelar() || status.getProgresso() >= 100 ){
                        LOGGER.debug("Exportação - Monitor finalizado - Processo - " + status.getId());
                        this.cancel();

                        //Se o processo ainda nao foi concluido, entao cancela-o
                        GeraFrequenciaStatusTransfer statusGeraFrequencia = status.getGeraFrequenciaStatusTransfer();
                        statusGeraFrequencia.getCalculoPrincipal().setParar(Boolean.TRUE);
                    }else{
                        Calendar c = Calendar.getInstance();
                        c.add(Calendar.SECOND, -10);//espera 10 segundos

                        if (status.getDataAtualizacao().before(c.getTime())) {
                            LOGGER.debug("Exportação - Processo cancelado por TimeOut : Processo - " + status.getId());
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

    /**
     * Retorna o arquivo exportado/gerado
     *
     * @param idStatus
     * @return Object
     * @throws ServiceException
     */
    public synchronized Object baixarArquivo(String  idStatus) throws ServiceException {
        LOGGER.debug("Baixar relatório id : " + idStatus);
        File arquivo = null;
        try {
            String fullRelatorioNome = System.getProperty("java.io.tmpdir") + File.separator + idStatus + ".txt";
            arquivo = new File(fullRelatorioNome);
            InputStream in = new FileInputStream(fullRelatorioNome);
            return (Object) in;
        } catch (IOException e) {
            throw new ServiceException(this.getTopPontoResponse().erro("ERRO2"));
        } finally {
            if (arquivo != null) {
                arquivo.deleteOnExit();
            }
        }
    }

    public Response processarMarcacoesInvalidas(String id) {
        ExportarStatusTransfer status = this.buscarStatus(id);
        
        //Atualiza data
        status.setProcessarMarcacoesInvalidas(true);
        
        return this.getTopPontoResponse().sucessoSalvar(status);
    }
}

