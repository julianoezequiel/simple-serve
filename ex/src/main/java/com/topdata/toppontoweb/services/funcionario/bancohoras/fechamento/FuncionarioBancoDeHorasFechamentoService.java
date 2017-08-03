package com.topdata.toppontoweb.services.funcionario.bancohoras.fechamento;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.dao.funcionario.FuncionarioBancoDeHorasFechamentoDao;
import com.topdata.toppontoweb.dto.ColetivoResultado;
import com.topdata.toppontoweb.dto.ProgressoTransfer;
import com.topdata.toppontoweb.dto.funcionario.bancohoras.Gatilho;
import com.topdata.toppontoweb.dto.funcionario.bancohoras.SaldoBH;
import com.topdata.toppontoweb.entity.Entidade;
import com.topdata.toppontoweb.entity.empresa.Empresa;
import com.topdata.toppontoweb.entity.funcionario.Coletivo;
import com.topdata.toppontoweb.entity.funcionario.Funcionario;
import com.topdata.toppontoweb.entity.funcionario.FuncionarioBancoHoras;
import com.topdata.toppontoweb.entity.funcionario.FuncionarioBancoHorasFechamento;
import com.topdata.toppontoweb.entity.funcionario.FuncionarioBancoHorasFechamento_;
import com.topdata.toppontoweb.entity.tipo.TipoAcerto;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoService;
import static com.topdata.toppontoweb.services.TopPontoService.LOGGER;
import com.topdata.toppontoweb.services.coletivo.ColetivoService;
import com.topdata.toppontoweb.services.empresa.EmpresaService;
import com.topdata.toppontoweb.services.fechamento.FechamentoException;
import com.topdata.toppontoweb.services.funcionario.FuncionarioService;
import com.topdata.toppontoweb.services.funcionario.bancohoras.FuncionarioBancoHorasService;
import com.topdata.toppontoweb.services.gerafrequencia.integracao.VerificarSaldoBH;
import com.topdata.toppontoweb.utils.DateHelper;
import com.topdata.toppontoweb.utils.Utils;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;
import com.topdata.toppontoweb.utils.constantes.MSG;

/**
 * Classe de regras para o cadastro de fechamentos de banco de horas para o
 * funcionário
 *
 * @version 1.0.0 data 28/06/2017
 * @since 1.0.0 data 28/06/2017
 *
 * @author juliano.ezequiel
 */
@Service
public class FuncionarioBancoDeHorasFechamentoService extends TopPontoService<FuncionarioBancoHorasFechamento, Integer> {

    //<editor-fold defaultstate="collapsed" desc="CDI">
    @Autowired
    private FuncionarioBancoDeHorasFechamentoDao funcionarioBancoDeHorasFechamentoDao;
    @Autowired
    private FuncionarioBancoHorasService funcionarioBancoHorasService;
    @Autowired
    private VerificarSaldoBH verificarSaldoBH;
    @Autowired
    private FechamentoSubTotal fechamentoSubTotal;
    @Autowired
    private FechamentoAcerto fechamentoAcerto;
    @Autowired
    private FechamentoEdicaoSaldo fechamentoEdicaoSaldo;
    @Autowired
    private ColetivoService coletivoService;
    @Autowired
    private FuncionarioService funcionarioService;
    @Autowired
    private EmpresaService empresaService;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="CONSULTAS">
    /**
     * Busca a lista de banco de horas do funcionário dentro do periodo
     * informado
     *
     * @param funcionario
     * @param dataInicio
     * @param dataFim
     * @return
     * @throws ServiceException
     */
    public List<FuncionarioBancoHorasFechamento> buscarPorFuncionarioPeriodo(Funcionario funcionario, Date dataInicio, Date dataFim) throws ServiceException {
        //TODO:fazer o metodo de consulta por periodo
        return buscarPorFuncionario(funcionario.getIdFuncionario());
    }

    /**
     * Busca o banco que esta em vigor para o funcionário para a data
     *
     * @param funcionario
     * @param dataInicio
     * @return
     * @throws ServiceException
     */
    public FuncionarioBancoHoras getBancoValido(Funcionario funcionario, Date dataInicio) throws ServiceException {
        //busca o banco de horas que esta valido neste periodo para este funcionário
        List<FuncionarioBancoHoras> bancoDeHorasPeriodo
                = this.funcionarioBancoHorasService.buscarPorFuncionarioEPeriodo(funcionario,
                        dataInicio, dataInicio);
        Optional<FuncionarioBancoHoras> bancoDeHorasAtual = bancoDeHorasPeriodo.stream().sorted(Comparator.comparing(FuncionarioBancoHoras::getDataInicio)).findFirst();

        //se encontrou o banco de horas gera o fechamento de subtotal
        if (bancoDeHorasAtual.isPresent()) {
            return bancoDeHorasAtual.get();
        } else {
            throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.FUNCIONARIO_BANCO_DE_HORAS.ALERTA_INCLUIR_BANCO_NAO_ENCONTRADO.getResource()));
        }
    }

    /**
     * Os banco fechamento pelo banco de horas
     *
     * @param funcionarioBancoHoras
     * @return
     * @throws ServiceException
     */
    public List<FuncionarioBancoHorasFechamento> buscarPorFuncionarioBancoHoras(FuncionarioBancoHoras funcionarioBancoHoras) throws ServiceException {

        try {
            List<FuncionarioBancoHorasFechamento> list = this.funcionarioBancoDeHorasFechamentoDao.buscarPorFuncionarioBancoHoras(funcionarioBancoHoras.getIdFuncionarioBancoHoras());
            return list;
        } catch (DaoException ex) {
            throw new ServiceException(ex);
        }
    }

    /**
     * Busca o fechamento de banco de horas pelo id do funcionario
     *
     * @param idFuncionario
     * @return
     * @throws ServiceException
     */
    public List<FuncionarioBancoHorasFechamento> buscarPorFuncionario(Integer idFuncionario) throws ServiceException {
        try {
            return this.funcionarioBancoDeHorasFechamentoDao.buscarPorFuncionario(idFuncionario);
        } catch (DaoException de) {
            throw new ServiceException(de);
        }
    }
    //</editor-fold>

    public void recalcularSubTotaisPosteriores(Funcionario funcionario, Date data) throws ServiceException {
        List<FuncionarioBancoHorasFechamento> listaFechamento = this.buscarPorFuncionarioPosterior(funcionario, data);
        listaFechamento
                .stream()
                .filter(f -> !f.getTipoFechamento().getIdTipoFechamento().equals(CONSTANTES.Enum_TIPO_FECHAMENTO.ACERTO.ordinal()))
                .sorted(Comparator.comparing(FuncionarioBancoHorasFechamento::getDataFechamento).reversed())
                .forEach(new RecalcularSubTotais());
    }

    private class RecalcularSubTotais implements Consumer<FuncionarioBancoHorasFechamento> {

        @Override
        public void accept(FuncionarioBancoHorasFechamento t) {
            try {
                LOGGER.debug("Recalculando Fechamento {} - data : {}", t.getTipoFechamento().getDescricao(), t.getDataFechamento());
                t.setRecalcularSaldos(Boolean.FALSE);
                atualizar(t);
            } catch (ServiceException ex) {
                LOGGER.debug("Erro ao recalcular subtotais {}");
            }

        }

    }

    /**
     * Verifica o saldo do banco de horas via HTTP
     *
     * @param saldoBH
     * @return
     * @throws ServiceException
     */
    public Response verificarSaldo(SaldoBH saldoBH) throws ServiceException {
        LOGGER.debug("Inicio da verificação de saldo via HTTP");
        SaldoBH saldoBH1 = verificarSaldo(saldoBH.getDataConsulta(), saldoBH.getFuncionario());
        return this.getTopPontoResponse().sucesso(saldoBH1);
    }

    /**
     * Função para verificação do saldo de banco de horas. Esta funcção realiza
     * o uso do gera frequencia
     *
     * @param dataConsulta
     * @param func
     * @return
     * @throws ServiceException
     */
    public SaldoBH verificarSaldo(Date dataConsulta, Funcionario func) throws ServiceException {
        return this.verificarSaldoBH.calcular(func, dataConsulta);
    }

    /**
     * Valida se existe um fechamento de banco de horas cadastrado para o banco
     * de horas. O tipo de fechamento sub total é ignorado na validação
     *
     * @param funcionarioBancoHoras
     * @throws
     * com.topdata.toppontoweb.services.funcionario.bancohoras.fechamento.FechamentoBHException
     * @throws ServiceException
     */
    public void validarPossuiFechamentoBancoHoras(FuncionarioBancoHoras funcionarioBancoHoras) throws FechamentoBHException, ServiceException {
        if (funcionarioBancoHoras.getIdFuncionarioBancoHoras() != null) {

            try {
                funcionarioBancoHoras.setFuncionarioBancoHorasFechamentoList(this.funcionarioBancoDeHorasFechamentoDao.buscarPorFuncionarioBancoHorasDataInicio(funcionarioBancoHoras));

                if (!funcionarioBancoHoras.getFuncionarioBancoHorasFechamentoList().isEmpty()) {
                    throw new FechamentoBHException(this.getTopPontoResponse().alertaValidacao(MSG.FUNCIONARIO_BANCO_DE_HORAS.ALERTA_FECHAMENTO_BH.getResource()));
                }
            } catch (DaoException ex) {
                throw new ServiceException(ex);
            }
        }
    }

    /**
     * Valida se existe um fechamento de banco de horas cadastrado para o banco
     * de horas. O tipo de fechamento sub total é ignorado na validação
     *
     * @param funcionarioBancoHoras
     * @param dataInicioAntigo
     * @throws
     * com.topdata.toppontoweb.services.funcionario.bancohoras.fechamento.FechamentoBHException
     * @throws ServiceException
     */
    public void validarPossuiFechamentoBancoHorasNovo(FuncionarioBancoHoras funcionarioBancoHoras, Date dataInicioAntigo) throws FechamentoBHException, ServiceException {
        try {
            Date dataFim = funcionarioBancoHoras.getDataFim() != null ? funcionarioBancoHoras.getDataFim() : DateHelper.Max();
            List<FuncionarioBancoHorasFechamento> list
                    = this.funcionarioBancoDeHorasFechamentoDao.buscarPorFuncionarioPeriodo(
                            funcionarioBancoHoras.getFuncionario(), funcionarioBancoHoras.getDataInicio(), dataFim);
            LOGGER.debug("Total de fechamentos no periodo : {} ", list.size());
            list = list.stream().filter(f -> f.getDataFechamento().compareTo(dataInicioAntigo) != 0).collect(Collectors.toList());
            LOGGER.debug("Total de fechamentos no periodo filtrado: {} ", list.size());
            if (!list.isEmpty()) {
                throw new FechamentoBHException(this.getTopPontoResponse().alertaValidacao(MSG.FUNCIONARIO_BANCO_DE_HORAS.ALERTA_FECHAMENTO_BH.getResource()));
            }
        } catch (DaoException ex) {
            Logger.getLogger(FuncionarioBancoDeHorasFechamentoService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Valida se o fechamento de banco de horas esta dentro de um periodo de
     * fechamento de empresaF
     *
     * @param funcionarioBancoHorasFechamento
     * @throws ServiceException
     */
    public void validarPossuiFechamento(FuncionarioBancoHorasFechamento funcionarioBancoHorasFechamento) throws ServiceException {
        Funcionario funcionario = this.funcionarioService.buscar(Funcionario.class, funcionarioBancoHorasFechamento.getFuncionarioBancoHoras().getFuncionario().getIdFuncionario());
        Empresa empresa = this.empresaService.buscar(funcionario.getDepartamento().getEmpresa().getIdEmpresa());
        this.getFechamentoServices().isPossuiFechamentoPeriodo(empresa, Utils.configuraHorarioData(funcionarioBancoHorasFechamento.getDataFechamento(), 0, 0, 0), Utils.configuraHorarioData(funcionarioBancoHorasFechamento.getDataFechamento(), 23, 59, 59));
    }

    public void validarJaexisteFechamentoDeBancoNaData(FuncionarioBancoHorasFechamento bancoHorasFechamento, Funcionario funcionario) throws ServiceException {
        //se for uma inclusao verificar se existe um fechamento de anco para a data
        try {
            Date dataInicio = Utils.configuraHorarioData(bancoHorasFechamento.getDataFechamento(), 0, 0, 0);
            Date dataTermino = Utils.configuraHorarioData(bancoHorasFechamento.getDataFechamento(), 23, 59, 59);
            List<FuncionarioBancoHorasFechamento> fbhfs = this.funcionarioBancoDeHorasFechamentoDao.buscarPorFuncionarioPeriodo(funcionario, dataInicio, dataTermino);

            if (bancoHorasFechamento.getIdFuncionarioBancoHorasFechamento() == null && !fbhfs.isEmpty()) {

            }
        } catch (DaoException ex) {
            Logger.getLogger(FuncionarioBancoDeHorasFechamentoService.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Valida se o fechamento foi criado a partir de um coletivo e se esta sendo
     * alterado. Se o fechamento foi modificado, o vínculo do coletivo é
     * removido
     *
     * @param fechamento
     * @param somenteDataFechamento
     * @return
     * @throws ServiceException
     */
    public Coletivo validarAlteracaoColetivo(FuncionarioBancoHorasFechamento fechamento, Boolean somenteDataFechamento) throws ServiceException {
        Coletivo coletivo = null;

        if (fechamento.getIdFuncionarioBancoHorasFechamento() != null) {
            try {
                FuncionarioBancoHorasFechamento fechamentoOriginal = this.funcionarioBancoDeHorasFechamentoDao.find(FuncionarioBancoHorasFechamento.class, fechamento.getIdFuncionarioBancoHorasFechamento());
                if (somenteDataFechamento == true && fechamentoOriginal.getColetivo() != null
                        && fechamento.getDataFechamento() != null && fechamentoOriginal.getDataFechamento() != null && fechamento.getDataFechamento().getTime() != fechamentoOriginal.getDataFechamento().getTime()) {
                    coletivo = fechamentoOriginal.getColetivo();
                    fechamento.setColetivo(null);
                } else if (somenteDataFechamento == false && (fechamentoOriginal.getColetivo() != null && fechamento.getCredito() != null && fechamentoOriginal.getCredito() != null && fechamento.getCredito().getTime() != fechamentoOriginal.getCredito().getTime()
                        || fechamento.getDebito() != null && fechamentoOriginal.getDebito() != null && fechamento.getDebito().getTime() != fechamentoOriginal.getDebito().getTime()
                        || fechamento.getDataFechamento() != null && fechamentoOriginal.getDataFechamento() != null && fechamento.getDataFechamento().getTime() != fechamentoOriginal.getDataFechamento().getTime()) && fechamento.isManual()) {

                    coletivo = fechamentoOriginal.getColetivo();
                    fechamento.setColetivo(null);
                }
            } catch (DaoException ex) {
                LOGGER.error(this.getClass().getName(), ex);
            }
        }
        return coletivo;
    }

    /**
     * Valida se o fechamento foi criado a partir de um coletivo e se esta sendo
     * alterado. Se o fechamento foi modificado, o vínculo do coletivo é
     * removido
     *
     * @param fechamento
     * @return
     * @throws ServiceException
     */
    public Coletivo validarAlteracaoColetivo(FuncionarioBancoHorasFechamento fechamento) throws ServiceException {
        return this.validarAlteracaoColetivo(fechamento, false);
    }

    /**
     * Realiza a verificação de qual tipos de exclusão sera realizada. Direciona
     * para a classe de exclusão específica.
     *
     * @see FechamentoAcerto
     * @see FechamentoSubTotal
     * @see FechamentoEdicaoSaldo
     * @param id
     * @param validar
     * @return
     * @throws ServiceException
     */
    public FuncionarioBancoHorasFechamento
            excluir(Object id, boolean validar) throws ServiceException {
        FuncionarioBancoHorasFechamento funcionarioBancoHorasFechamento = this.buscar(FuncionarioBancoHorasFechamento.class, (Integer) id);
        if (funcionarioBancoHorasFechamento.getTipoFechamento().getIdTipoFechamento().equals(CONSTANTES.Enum_TIPO_FECHAMENTO.SUBTOTAL.ordinal())) {
            return this.fechamentoSubTotal.excluir(id, validar);
        } else if (funcionarioBancoHorasFechamento.getTipoFechamento().getIdTipoFechamento().equals(CONSTANTES.Enum_TIPO_FECHAMENTO.ACERTO.ordinal())) {
            return this.fechamentoAcerto.excluir(id);
        } else if (funcionarioBancoHorasFechamento.getTipoFechamento().getIdTipoFechamento().equals(CONSTANTES.Enum_TIPO_FECHAMENTO.EDICAO_DE_SALDO.ordinal())) {
            return this.fechamentoEdicaoSaldo.excluir(id);
        } else {
            return null;
        }

    }

    /**
     * Método utlizado pelo serviço de banco de horas do funcionário para
     * remover os vinculos entre funcionario banco de horas e seus fechamentos
     *
     * @param funcionarioBancoHoras
     * @throws ServiceException
     */
    public void removerVinculos(FuncionarioBancoHoras funcionarioBancoHoras) throws ServiceException {
        try {
            LOGGER.debug("removendo vínculos funcionário banco de horas com banco de horas fechamento");
            this.funcionarioBancoDeHorasFechamentoDao.excluirPorIdFuncionarioBanco(funcionarioBancoHoras.getIdFuncionarioBancoHoras());
        } catch (DaoException ex) {
            throw new ServiceException(ex);
        }
    }

    @Override
    public FuncionarioBancoHorasFechamento
            buscar(Class<FuncionarioBancoHorasFechamento> entidade, Object id) throws ServiceException {
        try {
            FuncionarioBancoHorasFechamento fbhf = (FuncionarioBancoHorasFechamento) this.funcionarioBancoDeHorasFechamentoDao.find(FuncionarioBancoHorasFechamento.class, id);

            if (fbhf == null) {
                throw new ServiceException(this.getTopPontoResponse().alertaNaoCad(new FuncionarioBancoHoras().toString()));
            }
            return fbhf;
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroBuscar(), ex);
        }
    }

    @Override
    public Response salvar(FuncionarioBancoHorasFechamento funcionarioBancoHorasFechamento) throws ServiceException {

        if (funcionarioBancoHorasFechamento.getTipoFechamento().getIdTipoFechamento().equals(CONSTANTES.Enum_TIPO_FECHAMENTO.SUBTOTAL.ordinal())) {
            this.fechamentoSubTotal.salvarFechamento(funcionarioBancoHorasFechamento);
        } else if (funcionarioBancoHorasFechamento.getTipoFechamento().getIdTipoFechamento().equals(CONSTANTES.Enum_TIPO_FECHAMENTO.ACERTO.ordinal())) {
            this.fechamentoAcerto.salvar(funcionarioBancoHorasFechamento);
        } else if (funcionarioBancoHorasFechamento.getTipoFechamento().getIdTipoFechamento().equals(CONSTANTES.Enum_TIPO_FECHAMENTO.EDICAO_DE_SALDO.ordinal())) {
            this.fechamentoEdicaoSaldo.salvar(funcionarioBancoHorasFechamento);
        } else {
            throw new ServiceException(this.getTopPontoResponse().campoObrigatorio("Tipo de fechamento"));
        }

        return this.getTopPontoResponse().sucessoSalvar(funcionarioBancoHorasFechamento.toString());
    }

    @Override
    public Response atualizar(FuncionarioBancoHorasFechamento funcionarioBancoHorasFechamento) throws ServiceException {
        if (funcionarioBancoHorasFechamento.getTipoFechamento().getIdTipoFechamento().equals(CONSTANTES.Enum_TIPO_FECHAMENTO.SUBTOTAL.ordinal())) {
            return this.getTopPontoResponse().sucessoAtualizar(this.fechamentoSubTotal.atualizarFechamentoSubTotal(funcionarioBancoHorasFechamento));
        } else if (funcionarioBancoHorasFechamento.getTipoFechamento().getIdTipoFechamento().equals(CONSTANTES.Enum_TIPO_FECHAMENTO.ACERTO.ordinal())) {
            return this.getTopPontoResponse().sucessoAtualizar(this.fechamentoAcerto.atualizar(funcionarioBancoHorasFechamento));
        } else if (funcionarioBancoHorasFechamento.getTipoFechamento().getIdTipoFechamento().equals(CONSTANTES.Enum_TIPO_FECHAMENTO.EDICAO_DE_SALDO.ordinal())) {
            return this.getTopPontoResponse().sucessoAtualizar(this.fechamentoEdicaoSaldo.atualizar(funcionarioBancoHorasFechamento));
        } else {
            throw new ServiceException(this.getTopPontoResponse().campoObrigatorio("Tipo de fechamento"));
        }
    }

    @Override
    public Response excluir(Class<FuncionarioBancoHorasFechamento> c, Object id) throws ServiceException {
        return this.getTopPontoResponse().sucessoExcluir(excluir(id, true).toString());
    }

    /**
     * Lista as opções de fechamento disponiveis para a manipulação da tela de
     * fechamento de banco de horas
     *
     * @return @throws ServiceException
     */
    public List<TipoAcerto> buscarTiposAcerto() throws ServiceException {
        try {
            List<TipoAcerto> tipoAcertoList = this.getDao().findAll(TipoAcerto.class
            );
            return tipoAcertoList;
        } catch (DaoException ex) {
            throw new ServiceException(ex);
        }
    }

    /**
     * Consulta os valores de gatilhos para o banco de horas validos para a data
     * informada
     *
     * @param gatilho
     * @return
     * @throws ServiceException
     */
    public Response consultaGatilho(Gatilho gatilho) throws ServiceException {
        //busca o banco de horas que esta valido neste periodo para este funcionário
        FuncionarioBancoHoras fbh = getBancoValido(gatilho.getFuncionario(), gatilho.getDataConsulta());
        Gatilho g = new Gatilho(Boolean.FALSE, fbh.getBancoHoras().getGatilhoPositivo(), fbh.getBancoHoras().getGatilhoNegativo());
        return this.getTopPontoResponse().sucesso(g);

    }

    /**
     * Salva os fechamentos pelo coletivo
     *
     * @param funcionarioBancoHorasFechamentoList
     * @param progresso
     * @return
     */
    public Collection<? extends ColetivoResultado> salvarFuncionarioBancoHorasFechamentoColetivo(List<FuncionarioBancoHorasFechamento> funcionarioBancoHorasFechamentoList, ProgressoTransfer progresso) {

        List<ColetivoResultado> resultadosColetivo = new ArrayList<>();
        List<FuncionarioBancoHorasFechamento> listaRemover = new ArrayList<>();

        funcionarioBancoHorasFechamentoList.stream().forEach((a) -> {
            try {
                progresso.addProgresso(1);
                Response r = a.getIdFuncionarioBancoHorasFechamento() == null ? this.salvar(a) : this.atualizar(a);
                resultadosColetivo.add(new ColetivoResultado((Entidade) a, Utils.extractMsgReturn(r), ColetivoResultado.Enum_ResultadoColetivo.LANCADO));
            } catch (ServiceException se) {
                //recupera as exeções e trata os resultados
                boolean fechamento = se instanceof FechamentoException;
                resultadosColetivo.add(new ColetivoResultado((Entidade) a, Utils.extractMsgReturn(se.getResponse()), ColetivoResultado.Enum_ResultadoColetivo.ERRO, fechamento));
                try {
                    if (!fechamento) {
                        listaRemover.add((FuncionarioBancoHorasFechamento) a.clone());
                    }
                } catch (CloneNotSupportedException ex) {
                    LOGGER.debug(this.getClass().getSimpleName(), ex);
                }
            }
        });

        //Tenta remover os que deram erros, pois esse é o procedimento do coletivo
        if (!listaRemover.isEmpty()) {
            listaRemover.stream().forEach(fechamento -> {
                try {
                    if (fechamento.getIdFuncionarioBancoHorasFechamento() != null) {
                        this.excluirEntidade(fechamento);
                    }
                } catch (ServiceException ex) {
                    LOGGER.debug(this.getClass().getSimpleName(), ex);
                }
            });
        }

        return resultadosColetivo;

    }

    /**
     * Excluir o fechamento entidade que está vicnulado au coletivo
     *
     * @param funcionarioBancoHorasFechamento
     * @return
     * @throws ServiceException
     */
    public Response excluirEntidade(FuncionarioBancoHorasFechamento funcionarioBancoHorasFechamento) throws ServiceException {
        try {
            this.funcionarioBancoDeHorasFechamentoDao.excluirEntidade(funcionarioBancoHorasFechamento);
            this.coletivoService.excluirColetivoSemVinculo(funcionarioBancoHorasFechamento.getColetivo());
            this.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_FUNCIONARIO_BANCO_DE_HORAS, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.EXCLUIR, funcionarioBancoHorasFechamento);

            return this.getTopPontoResponse().sucessoExcluir(funcionarioBancoHorasFechamento.toString());
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroExcluir(funcionarioBancoHorasFechamento.toString()), ex);
        }
    }

    /**
     * Método para consulta de fechamento do banco de horas do funcionário por
     * coletivo
     *
     * @author juliano.ezequiel
     * @since 04/07/2017
     * @param coletivo
     * @return
     * @throws ServiceException
     */
    public List<FuncionarioBancoHorasFechamento> buscarPorColetivo(Coletivo coletivo) throws ServiceException {
        try {
            HashMap<String, Object> map = new HashMap<>();
            map.put(FuncionarioBancoHorasFechamento_.coletivo.getName(), coletivo);

            return this.funcionarioBancoDeHorasFechamentoDao.findbyAttributes(map, FuncionarioBancoHorasFechamento.class
            );
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroBuscar(), ex);
        }
    }

    /**
     * Valida a exclusão
     *
     * @param a
     * @throws ServiceException
     */
    public void validarExcluir(FuncionarioBancoHorasFechamento a) throws ServiceException {
        FuncionarioBancoHorasFechamento funcionarioBancoHorasFechamento = this.buscar(FuncionarioBancoHorasFechamento.class, a.getIdFuncionarioBancoHorasFechamento());
        if (funcionarioBancoHorasFechamento.getTipoFechamento().getIdTipoFechamento().equals(CONSTANTES.Enum_TIPO_FECHAMENTO.SUBTOTAL.ordinal())) {
            this.fechamentoSubTotal.validarExlusao(a);
        } else if (funcionarioBancoHorasFechamento.getTipoFechamento().getIdTipoFechamento().equals(CONSTANTES.Enum_TIPO_FECHAMENTO.ACERTO.ordinal())) {
            this.fechamentoAcerto.validarExlusao(a);
        } else if (funcionarioBancoHorasFechamento.getTipoFechamento().getIdTipoFechamento().equals(CONSTANTES.Enum_TIPO_FECHAMENTO.EDICAO_DE_SALDO.ordinal())) {
            this.fechamentoEdicaoSaldo.validarExlusao(a);
        }
    }

    /**
     * Exclui os fechamentos pelo coletivo
     *
     * @param c
     * @throws ServiceException
     */
    public void excluirPorColetivo(Coletivo c) throws ServiceException {
        try {
            this.funcionarioBancoDeHorasFechamentoDao.excluirPorColetivo(c);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroExcluir(c.toString()), ex);
        }
    }

    public List<ColetivoResultado> removerColetivo(List<FuncionarioBancoHorasFechamento> funcionarioBancoHorasFechamentoList, Coletivo coletivo) {
        List<ColetivoResultado> resultadosColetivo = new ArrayList<>();

        funcionarioBancoHorasFechamentoList.stream().forEach(f -> {
            try {
                if (f.getColetivo() != null) {
                    f.setColetivo(null);
                    f = (FuncionarioBancoHorasFechamento) this.funcionarioBancoDeHorasFechamentoDao.save(f);

                }

                Response r = this.excluir(FuncionarioBancoHorasFechamento.class, f.getIdFuncionarioBancoHorasFechamento());
                f.setColetivo(coletivo);
                resultadosColetivo.add(new ColetivoResultado((Entidade) f, Utils.extractMsgReturn(r), ColetivoResultado.Enum_ResultadoColetivo.EXCLUIDO));
            } catch (ServiceException ex) {
                f.setColetivo(coletivo);
                resultadosColetivo.add(new ColetivoResultado((Entidade) f, Utils.extractMsgReturn(ex.getResponse()), ColetivoResultado.Enum_ResultadoColetivo.ERRO));
            } catch (DaoException ex) {
                f.setColetivo(coletivo);
                LOGGER.debug(this.getClass().getSimpleName(), ex);
            }
        });
        return resultadosColetivo;
    }

    List<FuncionarioBancoHorasFechamento> buscarPorFuncionarioPosterior(Funcionario funcionario, Date dataFechamento) throws ServiceException {
        try {
            return this.funcionarioBancoDeHorasFechamentoDao.buscarPorFuncionarioPosterior(funcionario, dataFechamento);
        } catch (DaoException ex) {
            throw new ServiceException(ex);
        }
    }

}
