package com.topdata.toppontoweb.services.fechamento;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.dao.fechamento.EmpresaFechamentoPeriodoDao;
import com.topdata.toppontoweb.dto.gerafrequencia.GeraFrequenciaStatusTransfer;
import com.topdata.toppontoweb.dto.relatorios.RelatoriosGeraFrequenciaTransfer;
import com.topdata.toppontoweb.entity.empresa.Empresa;
import com.topdata.toppontoweb.entity.fechamento.EmpresaFechamentoPeriodo;
import com.topdata.toppontoweb.entity.fechamento.EmpresaFechamentoPeriodo_;
import com.topdata.toppontoweb.entity.funcionario.Funcionario;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoService;
import com.topdata.toppontoweb.services.empresa.EmpresaService;
import com.topdata.toppontoweb.services.funcionario.bancohoras.FuncionarioBancoHorasService;
import com.topdata.toppontoweb.services.funcionario.FuncionarioService;
import com.topdata.toppontoweb.services.gerafrequencia.integracao.GeraFrequencia;
import com.topdata.toppontoweb.services.gerafrequencia.integracao.GeraFrequenciaServices;
import com.topdata.toppontoweb.services.gerafrequencia.integracao.SaidaPadrao;
import com.topdata.toppontoweb.services.marcacoes.MarcacoesService;
import com.topdata.toppontoweb.services.permissoes.OperadorService;
import com.topdata.toppontoweb.services.relatorios.RelatorioHandler;
import com.topdata.toppontoweb.utils.DateHelper;
import com.topdata.toppontoweb.utils.Utils;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES.Enum_TIPO_PROCESSO;
import com.topdata.toppontoweb.utils.constantes.MSG;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.transaction.annotation.Transactional;

/**
 * Classe de Serviço para a Tabela EmpresaFechamentoPeriodo
 *
 * @since 1.0.0.0 data 27/04/2017
 * @version 1.0.0.0 data 27/04/2017
 *
 * @author juliano.ezequiel
 */
@Service
public class EmpresaFechamentoPeriodoService extends TopPontoService<EmpresaFechamentoPeriodo, Integer> {

    //<editor-fold defaultstate="collapsed" desc="CDI">
    @Autowired
    public EmpresaFechamentoPeriodoDao empresaFechamentoPeriodoDao;
    @Autowired
    private FuncionarioBancoHorasService funcionarioBancoHorasService;
    @Autowired
    private FechamentoServices fechamentoServices;
    @Autowired
    private GeraFrequencia geraFrequencia;
    @Autowired
    private EmpresaService empresaService;
    @Autowired
    private OperadorService operadorService;
    @Autowired
    private FuncionarioService funcionarioService;
    @Autowired
    private MarcacoesService marcacoesService;
    //</editor-fold>

    public static final String ID_EMPRESA_FECHAMENTO_PERIODO = "ID_EMPRESA_FECHAMENTO_PERIODO";
    public static final String ID_EMPRESA = "ID_EMPRESA";
    public static final String ID_PROCESSO = "ID_PROCESSO";

    public static final HashMap<String, EmpresaFechamentoPeriodo> CONTROLE_FECHAMENTOS = new HashMap<>();

    /**
     * Inicia o processo de fechamento.
     *
     * @param request HttpServletRequest
     * @param efp EmpresaFechamentoPeriodo
     * @param operacao CONSTANTES.Enum_OPERACAO
     * @return GeraFrequenciaStatusTransfer
     * @throws ServiceException
     */
    public synchronized GeraFrequenciaStatusTransfer iniciarFechamento(HttpServletRequest request,
            EmpresaFechamentoPeriodo efp, CONSTANTES.Enum_OPERACAO operacao) throws ServiceException {

        LOGGER.debug("Inicio processo fechamento");

        //validações
        validarCamposObrigatorios(efp);
        Empresa empresa = this.empresaService.buscar(efp.getIdEmpresa().getIdEmpresa());
        List<Funcionario> funcionarioList = this.funcionarioService.buscarPorEmpresa(empresa);
        validarExisteFechamento(efp.getIdEmpresa(), efp.getInicio(), efp.getTermino(), efp);
        validarPossuiFuncionario(funcionarioList);
        validarBancoHorasFuncionarioEmAberto(funcionarioList, efp.getInicio(), efp.getTermino());

        //Objeto de uso para o gera frequencia
        RelatoriosGeraFrequenciaTransfer relatoriosGeraFrequencia = new RelatoriosGeraFrequenciaTransfer();
        relatoriosGeraFrequencia.setPeriodoInicio(efp.getInicio());
        relatoriosGeraFrequencia.setPeriodoFim(efp.getTermino());
        relatoriosGeraFrequencia.setEmpresa(empresa);
        relatoriosGeraFrequencia.setTipo_relatorio(RelatorioHandler.TIPO_RELATORIO.FECHAMENTO);

        String idProcesso = UUID.randomUUID().toString();
        CONTROLE_FECHAMENTOS.put(idProcesso, efp);

        //inicio de calculo do gera frequencia
        GeraFrequenciaStatusTransfer geraFrequenciaStatus
                = this.geraFrequencia.iniciar(null, relatoriosGeraFrequencia, Enum_TIPO_PROCESSO.FECHAMENTO_EMPRESA);
        geraFrequenciaStatus.setOperacao(operacao);
        geraFrequenciaStatus.setOperador(this.operadorService.getOperadorDaSessao(request));

        if (efp.getIdEmpresaFechamentoPeriodo() != null) {
            geraFrequenciaStatus.mapAux.put(ID_EMPRESA_FECHAMENTO_PERIODO, efp.getIdEmpresaFechamentoPeriodo());
        }
        geraFrequenciaStatus.mapAux.put(ID_EMPRESA, empresa.getIdEmpresa());
        geraFrequenciaStatus.mapAux.put(ID_PROCESSO, idProcesso);

        return geraFrequenciaStatus;

    }

    /**
     * Inicia o processo de Salvar Dados do Fechamento depois que os dados forem
     * processados pelas threads
     *
     * @param gfst GeraFrequenciaStatusTransfer
     * @throws ServiceException
     */
    public synchronized void iniciarProcessoSalvarDadosFechamento(GeraFrequenciaStatusTransfer gfst) throws ServiceException {
        //espera se existri inconsistencias a decisão do operador
        while (aguardandoOperador(gfst.getId())) {
            LOGGER.debug("Aguardando usuário");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(EmpresaFechamentoPeriodoService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        try {
            GeraFrequenciaStatusTransfer statusTransfer = null;
            if (GeraFrequenciaServices.STATUS_MAP.containsKey(gfst.getId())) {
                statusTransfer = GeraFrequenciaServices.STATUS_MAP.get(gfst.getId());
            }
            if (statusTransfer != null && (statusTransfer.isProcessarMarcacoesInvalidas()
                    || (!statusTransfer.isProcessarMarcacoesInvalidas() && !statusTransfer.isPossuiInconsistencias()))) {

                LOGGER.debug("Início processo de salvar EmpresaFechamentoPeriodo");
                GeraFrequenciaServices.STATUS_MAP.get(gfst.getId()).setEstado_processo(RelatorioHandler.ESTADO_PROCESSO.PERSISTINDO_DADOS);

                //verifica se já existe um id para o fechamento ou cria um novo
                EmpresaFechamentoPeriodo efp = null;
                if (gfst.mapAux.containsKey(ID_EMPRESA_FECHAMENTO_PERIODO)) {
                    efp = this.buscar(EmpresaFechamentoPeriodo.class,
                            (Integer) gfst.mapAux.get(ID_EMPRESA_FECHAMENTO_PERIODO));
                    //em caso de edição exclui os registros anteriores
                    this.fechamentoServices.excluirEmpresaFechamentoDataPorEmpresaFechamentoPeriodo(efp);
                }
                if (Objects.isNull(efp)) {
                    efp = new EmpresaFechamentoPeriodo();
                }
                //busca a empresa que esta sendo realizado o fechamento
                Empresa empresa = (Empresa) this.getDao().find(Empresa.class, (Integer) gfst.mapAux.get(ID_EMPRESA));
                if (Objects.isNull(empresa)) {
                    throw new ServiceException(this.getTopPontoResponse().erroBuscar());
                }
                if (efp != null) {
                    efp.setIdEmpresa(empresa);
                    efp.setInicio(gfst.getInicio());
                    efp.setTermino(gfst.getTermino());
                    efp.setEmpresaFechamentoDataList(new ArrayList<>());
                    efp = this.empresaFechamentoPeriodoDao.save(efp);

                    //remove da lista de processo em andamento
                    if (gfst.mapAux.containsKey(ID_PROCESSO)) {
                        removerProcesso((String) gfst.mapAux.get(ID_PROCESSO));
                    }

                    //salva o resultado do fechamento
                    gfst.getSaidaPadraoList()
                            .parallelStream()
                            .forEach(new salvarResultado(efp, this.fechamentoServices, gfst));

                    boolean possuiInconsistencia = gfst.getSaidaPadraoList()
                            .parallelStream()
                            .anyMatch(e
                                    -> e.getSaidaDiaList()
                                    .parallelStream()
                                    .anyMatch(r
                                            -> r.getRegras().isInconsistencia()));
                    if (possuiInconsistencia == true) {
                        efp.setPossuiInconsistencia(possuiInconsistencia);
                        efp = this.empresaFechamentoPeriodoDao.save(efp);
                    }

                    //audita os dados do fechamento realizado
                    this.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_EMPRESAS_FECHAMENTOS, CONSTANTES.Enum_AUDITORIA.DEFAULT, gfst.getOperacao(), efp, gfst.getOperador());
                    if (GeraFrequenciaServices.STATUS_MAP.containsKey(gfst.getId())) {
                        GeraFrequenciaServices.STATUS_MAP.get(gfst.getId()).setPercentual(CONSTANTES.PERC_PADRAO_FIM);
                    }
                    LOGGER.debug("Término processo de salvar EmpresaFechamentoPeriodo");
                } else {
                    LOGGER.debug("Não foi possível instanciar o objeto EmpresaFechamentoPeriodo");
                    throw new ServiceException(this.getTopPontoResponse().erroSalvar(new EmpresaFechamentoPeriodo().toString()));
                }
            }

        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroSalvar(new EmpresaFechamentoPeriodo().toString()), ex);
        }
    }

    private boolean aguardandoOperador(String id) {
        if (GeraFrequenciaServices.STATUS_MAP.containsKey(id)) {
            LOGGER.debug("Possui inconsistencias {}", GeraFrequenciaServices.STATUS_MAP.get(id).isPossuiInconsistencias());
            LOGGER.debug("Processar marcacoes inválidas {}", GeraFrequenciaServices.STATUS_MAP.get(id).isProcessarMarcacoesInvalidas());
            return GeraFrequenciaServices.STATUS_MAP.get(id).isPossuiInconsistencias()
                    && !GeraFrequenciaServices.STATUS_MAP.get(id).isProcessarMarcacoesInvalidas();
        }
        return false;
    }

    /**
     *
     * @param id
     */
    private void removerProcesso(String id) {
        if (CONTROLE_FECHAMENTOS.containsKey(id)) {
            CONTROLE_FECHAMENTOS.remove(id);
        }
    }

    public void processarMarcacoesInvalidas(String id) {
        GeraFrequencia.STATUS_MAP.get(id).setProcessarMarcacoesInvalidas(true);
    }

    /**
     * Salva os dados do resultado do gera frequência
     */
    private class salvarResultado implements Consumer<SaidaPadrao> {

        private final EmpresaFechamentoPeriodo empresaFechamentoPeriodo;
        private final FechamentoServices fechamentoServices;
        private final GeraFrequenciaStatusTransfer geraFrequenciaStatusTransfer;

        public salvarResultado(EmpresaFechamentoPeriodo efp, FechamentoServices fechamentoServices, GeraFrequenciaStatusTransfer gfst) {
            this.empresaFechamentoPeriodo = efp;
            this.fechamentoServices = fechamentoServices;
            this.geraFrequenciaStatusTransfer = gfst;
        }

        @Override
        public synchronized void accept(SaidaPadrao saidaPadrao) {
            LOGGER.debug("Salvar resultado gera frequência - funcionpario : {} - Total de saidas para salvar : {} ", saidaPadrao.getFuncionario().getNome(), saidaPadrao.getSaidaDiaList().size());
            Double perc = CONSTANTES.PERC_PADRAO_PERSISTENCIA_FECHAMENTO / this.geraFrequenciaStatusTransfer.getSaidaPadraoList().size();
            if (GeraFrequenciaServices.STATUS_MAP.containsKey(this.geraFrequenciaStatusTransfer.getId())) {
                GeraFrequenciaServices.STATUS_MAP.get(this.geraFrequenciaStatusTransfer.getId()).addPercentual(perc);
                this.fechamentoServices.salvarResultadoCalculo(saidaPadrao.getSaidaDiaList(), this.empresaFechamentoPeriodo, this.geraFrequenciaStatusTransfer);
            }
        }

    }

    /**
     * Consulta os fechamentos pelo id da Empresa
     *
     * @param id
     * @return
     * @throws ServiceException
     */
    public List<EmpresaFechamentoPeriodo> buscarPorEmpresa(Integer id) throws ServiceException {
        try {
            HashMap<String, Object> criterios = new HashMap<>();
            criterios.put(EmpresaFechamentoPeriodo_.idEmpresa.getName(), new Empresa(id));
            return this.empresaFechamentoPeriodoDao.findbyAttributes(criterios, EmpresaFechamentoPeriodo.class);
        } catch (DaoException ex) {
            throw new ServiceException(ex);
        }
    }

    /**
     * Lista os fechamento que a empresa possui dentro do período informado
     *
     * @param empresa
     * @param dataInicio
     * @param dataFim
     * @return
     * @throws ServiceException
     */
    @Transactional(readOnly = true)
    public List<EmpresaFechamentoPeriodo> buscarPorPeriodoEmpresa(Empresa empresa, Date dataInicio, Date dataFim) throws ServiceException {
        try {
            return this.empresaFechamentoPeriodoDao.buscarPorPeriodoEmpresa(empresa, dataInicio, dataFim);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroBuscar());
        }
    }

    @Override
    public EmpresaFechamentoPeriodo buscar(Class<EmpresaFechamentoPeriodo> entidade, Object id) throws ServiceException {
        try {
            EmpresaFechamentoPeriodo efp = this.empresaFechamentoPeriodoDao.find(EmpresaFechamentoPeriodo.class, id);

            if (efp == null) {
                throw new ServiceException(this.getTopPontoResponse().alertaNaoCad(new EmpresaFechamentoPeriodo().toString()));
            }
            return efp;
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroBuscar(), ex);
        }
    }

    /**
     * Cancela o processo de fechamento
     *
     * @param id
     * @return
     * @throws ServiceException
     */
    public Response cancelarFechamento(String id) throws ServiceException {
        return this.geraFrequencia.cancelar(id);
    }

    @Override
    public Response excluir(Class<EmpresaFechamentoPeriodo> c, Object id) throws ServiceException {
        try {
            LOGGER.debug("Início exclusão " + c.getSimpleName() + " id : " + id);
            EmpresaFechamentoPeriodo efp = this.empresaFechamentoPeriodoDao.find(c, id);
            if (efp == null) {
                throw new ServiceException(this.getTopPontoResponse().alertaNaoCad(new EmpresaFechamentoPeriodo().toString()));
            }
            this.marcacoesService.atualizarMarcacoesInvalidas(efp.getIdEmpresa(), efp.getInicio(), efp.getTermino());

            this.empresaFechamentoPeriodoDao.delete(efp);
            this.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_EMPRESAS_FECHAMENTOS,
                    CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.EXCLUIR, efp);
            LOGGER.debug("Término exclusão " + c.getSimpleName());
            return this.getTopPontoResponse().sucessoExcluir(efp.toString());
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroExcluir(new EmpresaFechamentoPeriodo().toString()), ex);
        }
    }

    //<editor-fold defaultstate="collapsed" desc="VALIDAÇÕES">
    /**
     * Valida se a empresa possúi algum funcionario com banco de horas em aberto
     *
     * @param funcionarioListIn
     * @param dataInicio
     * @param dataTermino
     * @throws ServiceException
     */
    private void validarBancoHorasFuncionarioEmAberto(List<Funcionario> funcionarioListIn, Date dataInicio, Date dataTermino) throws ServiceException {
        LOGGER.debug("Validação banco de horas em aberto");
        List<Funcionario> funcionarioListTest = this.funcionarioBancoHorasService.validarBancoHorasAbertoPeriodo(funcionarioListIn, dataInicio, dataTermino);
        if (!funcionarioListTest.isEmpty()) {
            funcionarioListTest.stream().forEach(f -> {
                LOGGER.debug("Funcionário com Banco de horas em aberto : " + f.getNome());
            });
            throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.FECHAMENTO.ALERTA_FUNCIONARIO_BANCO_HORAS_ABERTO.getResource()));
        }
    }

    /**
     * Valida se a empresa possúi funcionários
     *
     * @param empresa
     * @throws ServiceException
     */
    private void validarPossuiFuncionario(List<Funcionario> funcionarioList) throws ServiceException {
        boolean possui = funcionarioList == null || !funcionarioList.isEmpty();
        LOGGER.debug("Validação possui funcionário = " + possui);
        if (possui == false) {
            throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.FECHAMENTO.ALERTA_SEM_FUNCIOANRIO.getResource()));
        }
    }

    /**
     * Valida os campos obrigatórios
     *
     * @param efp
     * @throws ServiceException
     */
    private void validarCamposObrigatorios(EmpresaFechamentoPeriodo efp) throws ServiceException {
        if (efp.getIdEmpresa() == null) {
            throw new ServiceException(this.getTopPontoResponse().campoObrigatorio(EmpresaFechamentoPeriodo_.idEmpresa.getName()));
        }
        if (efp.getInicio() == null) {
            throw new ServiceException(this.getTopPontoResponse().campoObrigatorio(EmpresaFechamentoPeriodo_.inicio.getName()));
        }
        if (efp.getTermino() == null) {
            throw new ServiceException(this.getTopPontoResponse().campoObrigatorio(EmpresaFechamentoPeriodo_.termino.getName()));
        }
    }

    /**
     * Método para validação de fechamento
     *
     * @param empresa
     * @param dataInicio
     * @param dataFim
     * @param empresaFechamentoPeriodo
     * @throws ServiceException
     */
    private void validarExisteFechamento(Empresa empresa, Date dataInicio, Date dataFim, EmpresaFechamentoPeriodo empresaFechamentoPeriodo) throws ServiceException {

        boolean existe = isExisteFechamentoEmExecucao(empresaFechamentoPeriodo);
        LOGGER.debug("Validação existe fechamento em execucao = " + existe);
        if (existe == true) {
            throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.EMPRESA.ALERTA_POSSUI_FECHAMENTO_EM_ANDAMENTO.getResource()));
        }
        existe = isExisteFechamento(empresa, dataInicio, dataFim, empresaFechamentoPeriodo);
        LOGGER.debug("Validação existe fechamento = " + existe);
        if (existe == true) {
            throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.EMPRESA.ALERTA_POSSUI_FECHAMENTO.getResource()));
        }

    }

    /**
     * Método para validação de fechamento
     *
     * @param empresa
     * @param dataInicio
     * @param dataFim
     * @throws ServiceException
     */
    public void validarEmpresaPossuiFechamento(Empresa empresa, Date dataInicio, Date dataFim) throws ServiceException {

        boolean existe = isExisteFechamento(empresa, dataInicio, dataFim, null);
        LOGGER.debug("Validação existe fechamento = " + existe);
        if (existe == true) {
            throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.EMPRESA.ALERTA_POSSUI_FECHAMENTO.getResource()));
        }
    }

    /**
     * Verifica se existe um fechamento para a empresa no período informado<br>
     * O parametro empresaFechamentoPeriodo é opcional, serve somente para
     * atualização de um registro.
     *
     * @param empresa Empresa que será consultada
     * @param dataInicio data de inicio da consulta
     * @param dataFim data de fim da consulta
     * @param empresaFechamentoPeriodo Opcional
     * @param excluir verdadeiro se houver necessidade de excluir o registro
     * @return verdadeiro de existe fechamento para o período informado
     * @throws ServiceException
     */
    private Boolean isExisteFechamento(Empresa empresa, Date dataInicio, Date dataFim, EmpresaFechamentoPeriodo empresaFechamentoPeriodo) throws ServiceException {

        List<EmpresaFechamentoPeriodo> empresaFechamentoPeriodosList = new ArrayList<>();
        //busca os fechamentos dentro do periodo
        empresaFechamentoPeriodosList.addAll(this.buscarPorPeriodoEmpresa(empresa, dataInicio, dataFim));

        AtomicBoolean existe = new AtomicBoolean(!empresaFechamentoPeriodosList.isEmpty());
        //se for uma atualização
        if (existe.get() == Boolean.TRUE && empresaFechamentoPeriodo != null && empresaFechamentoPeriodo.getIdEmpresaFechamentoPeriodo() != null) {

            empresaFechamentoPeriodosList = empresaFechamentoPeriodosList
                    .stream()
                    .filter(efp -> !Objects.equals(efp.getIdEmpresaFechamentoPeriodo(), empresaFechamentoPeriodo.getIdEmpresaFechamentoPeriodo()))
                    .collect(Collectors.toList());
            if (!empresaFechamentoPeriodosList.isEmpty()) {
                empresaFechamentoPeriodosList = empresaFechamentoPeriodosList
                        .stream()
                        .filter(new FilterExisteFechamentoPeriodo(empresaFechamentoPeriodo))
                        .filter(e -> !Objects.equals(e.getIdEmpresaFechamentoPeriodo(), empresaFechamentoPeriodo.getIdEmpresaFechamentoPeriodo()))
                        .collect(Collectors.toList());
            }
            existe.set(!empresaFechamentoPeriodosList.isEmpty());
        }

        return existe.get();
    }

    /**
     * Verifica se existe um fechamento em andamento para a empresa no período
     * informado<br>
     * O parametro empresaFechamentoPeriodo é opcional, serve somente para
     * atualização de um registro.
     *
     * @param empresa Empresa que será consultada
     * @param dataInicio data de inicio da consulta
     * @param dataFim data de fim da consulta
     * @param empresaFechamentoPeriodo Opcional
     * @param excluir verdadeiro se houver necessidade de excluir o registro
     * @return verdadeiro de existe fechamento para o período informado
     * @throws ServiceException
     */
    private Boolean isExisteFechamentoEmExecucao(EmpresaFechamentoPeriodo empresaFechamentoPeriodo) throws ServiceException {

        List<EmpresaFechamentoPeriodo> empresaFechamentoPeriodosList = new ArrayList<>();
        //valida se existe algum processo agendado
        empresaFechamentoPeriodosList.addAll(
                CONTROLE_FECHAMENTOS.values()
                .stream()
                .filter(new FilterExisteFechamentoPeriodo(empresaFechamentoPeriodo))
                .collect(Collectors.toList()));

        return !empresaFechamentoPeriodosList.isEmpty();

    }

    private static class FilterExisteFechamentoPeriodo implements Predicate<EmpresaFechamentoPeriodo> {

        private final EmpresaFechamentoPeriodo empresaFechamentoPeriodo;

        public FilterExisteFechamentoPeriodo(EmpresaFechamentoPeriodo empresaFechamentoPeriodo) {
            this.empresaFechamentoPeriodo = empresaFechamentoPeriodo;
        }

        @Override
        public boolean test(EmpresaFechamentoPeriodo efp) {

            Date dataAInicio = Utils.configuraHorarioData(empresaFechamentoPeriodo.getInicio(), 0, 0, 0);
            Date dataATermino = Utils.configuraHorarioData(empresaFechamentoPeriodo.getTermino(), 23, 59, 59);
            Date dataBInicio = Utils.configuraHorarioData(efp.getInicio(), 0, 0, 0);
            Date dataBTermino = Utils.configuraHorarioData(efp.getTermino(), 23, 59, 59);

            return efp.getIdEmpresa().equals(empresaFechamentoPeriodo.getIdEmpresa())
                    && DateHelper.ValidarDatasPeriodo(dataAInicio, dataATermino, dataBInicio, dataBTermino);
        }
    }

//</editor-fold>
    /**
     * Retorna o status do andamento do processamento dos dados
     *
     * @param id
     * @return
     * @throws ServiceException
     */
    public GeraFrequenciaStatusTransfer statusFechamentoProcessamentoCalculos(String id) throws ServiceException {
        if (GeraFrequencia.STATUS_MAP.containsKey(id)) {
            GeraFrequencia.STATUS_MAP.get(id).setData(new Date());
            return GeraFrequencia.STATUS_MAP.get(id);
        }
        GeraFrequenciaStatusTransfer gfst = new GeraFrequenciaStatusTransfer(Enum_TIPO_PROCESSO.FECHAMENTO_EMPRESA);
        gfst.setPercentual(CONSTANTES.PERC_PADRAO_FIM);
        gfst.setEstado_processo(RelatorioHandler.ESTADO_PROCESSO.AGENDANDO_PROCESSO);
        return gfst;
    }

}
