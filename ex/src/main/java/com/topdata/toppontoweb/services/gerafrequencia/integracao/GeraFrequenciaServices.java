package com.topdata.toppontoweb.services.gerafrequencia.integracao;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.dao.relatorios.RelatorioDao;
import com.topdata.toppontoweb.dto.FormatoTransfer;
import com.topdata.toppontoweb.dto.fechamento.FechamentoStatusTransfer;
import com.topdata.toppontoweb.dto.gerafrequencia.GeraFrequenciaStatusTransfer;
import com.topdata.toppontoweb.dto.gerafrequencia.IGeraFrequenciaTransfer;
import com.topdata.toppontoweb.dto.relatorios.cadastros.RelatorioCadastroTransfer;
import com.topdata.toppontoweb.entity.empresa.Departamento;
import com.topdata.toppontoweb.entity.funcionario.Funcionario;
import com.topdata.toppontoweb.entity.funcionario.FuncionarioBancoHorasFechamento;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoResponse;
import com.topdata.toppontoweb.services.TopPontoService;
import com.topdata.toppontoweb.services.auditoria.AuditoriaServices;
import com.topdata.toppontoweb.services.empresa.DepartamentoService;
import com.topdata.toppontoweb.services.fechamento.EmpresaFechamentoPeriodoService;
import static com.topdata.toppontoweb.services.fechamento.EmpresaFechamentoPeriodoService.ID_PROCESSO;
import com.topdata.toppontoweb.services.funcionario.FuncionarioService;
import com.topdata.toppontoweb.services.permissoes.OperadorService;
import com.topdata.toppontoweb.services.relatorios.ProcessarAquivosRelatorio;
import com.topdata.toppontoweb.services.relatorios.RelatorioHandler;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;

/**
 * Realiza o gerenciamento dos cálculos realizados pelo gera frequencia
 *
 * @author juliano.ezequiel
 */
@Service
public class GeraFrequenciaServices {

    public final static Logger LOGGER = LoggerFactory.getLogger(GeraFrequenciaServices.class.getName());

    @Autowired
    private RelatorioDao relatorioDao;
    @Autowired
    private TopPontoResponse topPontoResponse;
    @Autowired
    private ProcessarAquivosRelatorio processarRelatorio;
    @Autowired
    private MenorData menorData;
    @Autowired
    private EntradaPadrao entradaApiPadrao;
    @Autowired
    private TopPontoService topPontoService;
    @Autowired
    private EmpresaFechamentoPeriodoService fechamentoPeriodoService;
    @Autowired
    private FuncionarioService funcionarioService;
    
    @Autowired
    private DepartamentoService departamentoService;

    @Autowired
    private AuditoriaServices auditoriaServices;
    @Autowired
    private OperadorService operadorService;

    public List<?> dsrList;
    public List<?> tipoJornadaList;
    public List<?> tipoDiaList;
    public IGeraFrequenciaTransfer geraFrequenciaTransfer;

    /**
     * Mapa statico de controle de processos de criação de relatórios
     */
    public static final Map<String, GeraFrequenciaStatusTransfer> STATUS_MAP = new HashMap<>();

    /**
     * Realiza o processamento dos dados do relatório
     *
     * @param statusTransfer
     * @throws ServiceException
     */
    public synchronized void processarRelatorio(GeraFrequenciaStatusTransfer statusTransfer) throws ServiceException {
        LOGGER.debug("processar relatório : " + statusTransfer.getId());
        this.processarRelatorio.processar(statusTransfer);
    }

    /**
     * Inicia o processo para gerar o relatório.<br>
     * Instancia um objeto GeraFrequenciaStatusTransfer e armazena no HashMap
     * para controles futuros
     *
     * @param tIPO_PROCESSO
     * @return GeraFrequenciaStatusTransfer
     */
    public synchronized GeraFrequenciaStatusTransfer iniciarProcesso(CONSTANTES.Enum_TIPO_PROCESSO tIPO_PROCESSO) {
        GeraFrequenciaStatusTransfer statusTransfer = new GeraFrequenciaStatusTransfer(tIPO_PROCESSO);
        STATUS_MAP.put(statusTransfer.getId(), statusTransfer);
        return statusTransfer;
    }

    /**
     * Retorna o arquivo do relatório gerado
     *
     * @param id
     * @param idFormato
     * @return Object
     * @throws ServiceException
     */
    public synchronized Object baixarRelatorio(String id, String idFormato) throws ServiceException {
        LOGGER.debug("Baixar relatório id : " + id);
        File relatorio = null;
        try {
            FormatoTransfer formato = CONSTANTES.Enum_FORMATO.getFormatoTransfer(idFormato);
            this.processarRelatorio.exportar(id, formato, id);
            String fullRelatorioNome = System.getProperty("java.io.tmpdir") + File.separator + id + formato.getPrefixo();
            relatorio = new File(fullRelatorioNome);
            InputStream in = new FileInputStream(fullRelatorioNome);
            return (Object) in;
        } catch (IOException e) {
            if (GeraFrequenciaServices.STATUS_MAP.containsKey(id)) {
                GeraFrequenciaServices.STATUS_MAP.get(id).setEstado_processo(RelatorioHandler.ESTADO_PROCESSO.ERRO);
            }
            throw new ServiceException(this.getTopPontoResponse().erro("ERRO2"));
        } finally {
            if (relatorio != null) {
                relatorio.deleteOnExit();
            }
        }
    }

    /**
     * Cancela um processo de criação de relatório em andamento
     *
     * @param id
     * @return
     */
    public synchronized Response cancelar(String id) {
        LOGGER.debug("Processo cancelado id : " + id);
        finalizarProcesso(id);
        return this.topPontoResponse.sucesso();
    }

    private void finalizarProcesso(String id) {
        if (GeraFrequenciaServices.STATUS_MAP.containsKey(id)) {
            if (GeraFrequenciaServices.STATUS_MAP.get(id).mapAux.containsKey(ID_PROCESSO)) {
                String idProcesso = (String) GeraFrequenciaServices.STATUS_MAP.get(id).mapAux.get(ID_PROCESSO);
                if (EmpresaFechamentoPeriodoService.CONTROLE_FECHAMENTOS.containsKey(idProcesso)) {
                    EmpresaFechamentoPeriodoService.CONTROLE_FECHAMENTOS.remove(idProcesso);
                }
            }
            if (GeraFrequenciaServices.STATUS_MAP.get(id).getCalculoPrincipal() != null) {
                GeraFrequenciaServices.STATUS_MAP.get(id).getCalculoPrincipal().setParar(Boolean.TRUE);
            }
            if (GeraFrequenciaServices.STATUS_MAP.get(id).getTimerMonitor() != null) {
                GeraFrequenciaServices.STATUS_MAP.get(id).getTimerMonitor().cancel();
            }
            GeraFrequenciaServices.STATUS_MAP.remove(id);

        }
    }

    /**
     * Retorna o status do andamento do processo que esta gerando o relatório
     *
     * @param id
     * @return
     * @throws ServiceException
     */
    public synchronized GeraFrequenciaStatusTransfer status(String id) throws ServiceException {
        LOGGER.debug("Verificação de status id : " + id);
        if (GeraFrequenciaServices.STATUS_MAP.containsKey(id)) {
            GeraFrequenciaServices.STATUS_MAP.get(id).setData(new Date());
            return GeraFrequenciaServices.STATUS_MAP.get(id);
        }else{
            LOGGER.debug("Não foi possivel encontrar o processo de ID = "+id);
            LOGGER.debug("Está sendo retornado um status concluído.");
            GeraFrequenciaStatusTransfer status = new GeraFrequenciaStatusTransfer(CONSTANTES.Enum_TIPO_PROCESSO.RELATORIO);
            status.setPercentual(100d);
            status.setId(id);
            return status;
        }
//        throw new ServiceException();
    }

    /**
     * Monitora as threads, em caso do cliente não utilizar mais as threads
     * serão eliminadas
     *
     * @param id
     * @param timerMonitor
     */
    public synchronized void keepAlive(String id, TimerMonitor timerMonitor) {
        try {
            LOGGER.debug("KeepAlive id " + id);
            if (GeraFrequenciaServices.STATUS_MAP.containsKey(id)) {
                Calendar c = Calendar.getInstance();
                c.add(Calendar.SECOND, -20);
                if (GeraFrequenciaServices.STATUS_MAP.get(id).getPercentual() >= 100l && !GeraFrequenciaServices.STATUS_MAP.get(id).getCalculoPrincipal().isAlive()) {
                    LOGGER.debug("Processo Finalizado com sucesso : id Processo - " + GeraFrequenciaServices.STATUS_MAP.get(id).getId());
                    finalizarProcesso(id);
                    //se passou 5 segundo sem o front atualizar a data , a lista de thread sera suspenca
                } else if (GeraFrequenciaServices.STATUS_MAP.get(id).getData().before(c.getTime())) {
                    LOGGER.debug("Processo cancelado por TimeOut : Processo - " + GeraFrequenciaServices.STATUS_MAP.get(id).getId());
                    cancelar(id);
                }

            } else {
                boolean c = timerMonitor.cancel();
                if (c == true) {
                    timerMonitor.run();
                }
            }
        } catch (Exception e) {
            LOGGER.error(this.getClass().getSimpleName(), e);
        }
    }

    public List<Funcionario> buscarFuncionariosPorRelatorioCadastro(RelatorioCadastroTransfer relatorioTransfer) throws ServiceException {
        List<Funcionario> funcionarioList = new ArrayList<>();
        try {
            if (relatorioTransfer.getFuncionario() == null) {
                funcionarioList = this.getRelatorioDao().buscarFuncinarioPorFiltroRelatorio(relatorioTransfer);

            } else {
                Funcionario funcionario = this.getFuncionarioService().buscar(
                        Funcionario.class, relatorioTransfer.getFuncionario().getIdFuncionario());
                funcionarioList.add(funcionario);
            }

            return funcionarioList;
        } catch (DaoException ex) {
            throw new ServiceException(ex);
        }
    }

    public List<Departamento> buscarDepartamentosPorRelatorioCadastro(RelatorioCadastroTransfer relatorioTransfer,
            GeraFrequenciaStatusTransfer statusTransfer, Double max) throws ServiceException {
        List<Departamento> departamentoList = new ArrayList<>();

        List<Funcionario> funcionarioList = buscarFuncionariosPorRelatorioCadastro(relatorioTransfer);

        //Agrupa os funcionarios por departamento
        if (!funcionarioList.isEmpty()) {
            departamentoList = this.departamentoService.buscarPorFuncionarios(funcionarioList, statusTransfer, max);
        }

        return departamentoList;
    }

    public TopPontoResponse getTopPontoResponse() {
        return topPontoResponse;
    }

    public RelatorioDao getRelatorioDao() {
        return relatorioDao;
    }

    public MenorData getMenorData() {
        return menorData;
    }

    public EntradaPadrao getEntradaApiPadrao() {
        return entradaApiPadrao;
    }

    public TopPontoService getTopPontoService() {
        return topPontoService;
    }

    public EmpresaFechamentoPeriodoService getFechamentoPeriodoService() {
        return fechamentoPeriodoService;
    }

    /**
     * @return the funcionarioService
     */
    public FuncionarioService getFuncionarioService() {
        return funcionarioService;
    }

    public DepartamentoService getDepartamentoService() {
        return departamentoService;
    }

    public AuditoriaServices getAuditoriaServices() {
        return auditoriaServices;
    }

    /**
     * @return the operadorService
     */
    public OperadorService getOperadorService() {
        return operadorService;
    }

}
