package com.topdata.toppontoweb.services.funcionario.bancohoras;

//<editor-fold defaultstate="collapsed" desc="IMPORTS">
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.dao.funcionario.FuncionarioBancoHorasDao;
import com.topdata.toppontoweb.dto.ColetivoResultado;
import com.topdata.toppontoweb.dto.ProgressoTransfer;
import com.topdata.toppontoweb.dto.funcionario.bancohoras.SaldoBH;
import com.topdata.toppontoweb.entity.Entidade;
import com.topdata.toppontoweb.entity.bancohoras.BancoHoras;
import com.topdata.toppontoweb.entity.empresa.Empresa;
import com.topdata.toppontoweb.entity.funcionario.Coletivo;
import com.topdata.toppontoweb.entity.funcionario.Funcionario;
import com.topdata.toppontoweb.entity.funcionario.FuncionarioBancoHoras;
import com.topdata.toppontoweb.entity.funcionario.FuncionarioBancoHoras_;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoService;
import com.topdata.toppontoweb.services.ValidacoesCadastro;
import com.topdata.toppontoweb.services.bancoHoras.BancoHorasServices;
import com.topdata.toppontoweb.services.coletivo.ColetivoService;
import com.topdata.toppontoweb.services.empresa.EmpresaService;
import com.topdata.toppontoweb.services.fechamento.FechamentoException;
import com.topdata.toppontoweb.services.funcionario.FuncionarioService;
import com.topdata.toppontoweb.services.funcionario.bancohoras.fechamento.FechamentoBHException;
import com.topdata.toppontoweb.services.funcionario.bancohoras.fechamento.FechamentoSubTotal;
import com.topdata.toppontoweb.services.funcionario.bancohoras.fechamento.FuncionarioBancoDeHorasFechamentoService;
import com.topdata.toppontoweb.utils.Utils;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;
import com.topdata.toppontoweb.utils.constantes.MSG;
//</editor-fold>

/**
 * Classe de regras para o banco de horas do funcionário
 *
 * @version 1.0.0 data 18/10/2016
 * @since 1.0.0 data 11/10/2016
 *
 * @author juliano.ezequiel
 */
@Service
public class FuncionarioBancoHorasService extends TopPontoService<FuncionarioBancoHoras, Object>
        implements ValidacoesCadastro<FuncionarioBancoHoras, Object> {

    //<editor-fold defaultstate="" desc="CDI">
    @Autowired
    private FuncionarioService funcionarioService;
    @Autowired
    private FuncionarioBancoHorasDao funcionarioBancoHorasDao;
    @Autowired
    private ColetivoService coletivoService;
    @Autowired
    private BancoHorasServices bancoHorasServices;
    @Autowired
    private FuncionarioBancoDeHorasFechamentoService funcionarioBancoDeHorasFechamentoService;
    @Autowired
    private EmpresaService empresaService;
    @Autowired
    private FechamentoSubTotal fechamentoSubTotal;
//</editor-fold>

    //<editor-fold defaultstate="" desc="CONSULTAS">
    public boolean contains(FuncionarioBancoHoras entidade) {
        return this.funcionarioBancoHorasDao.contains(entidade);
    }

    public FuncionarioBancoHoras merge(FuncionarioBancoHoras funcionarioBancoHoras) {
        return this.funcionarioBancoHorasDao.merge(funcionarioBancoHoras);
    }

    /**
     * Busca os bancos pelo id do funcionario
     *
     * @param id
     * @return
     * @throws ServiceException
     */
    public List<FuncionarioBancoHoras> buscarPorFuncionario(Integer id) throws ServiceException {
        try {
            return this.funcionarioBancoHorasDao.buscarPorFuncionario(id);
        } catch (DaoException ex) {
            throw new ServiceException(ex);
        }
    }

    public List<FuncionarioBancoHoras> buscarPorBancoHoras(BancoHoras bancoHoras) throws ServiceException {
        try {
            return this.funcionarioBancoHorasDao.buscarPorBancoHoras(bancoHoras);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * Método para consulta de funcionarioBancoHoras por coletivo
     *
     * @since 13/01/2017
     * @param coletivo
     * @return
     * @throws ServiceException
     */
    public List<FuncionarioBancoHoras> buscarPorColetivo(Coletivo coletivo) throws ServiceException {
        try {
            LOGGER.debug("Consulta funcionario banco de horas por coletivo");
            HashMap<String, Object> map = new HashMap<>();
            map.put(FuncionarioBancoHoras_.coletivo.getName(), coletivo);
            return this.funcionarioBancoHorasDao.findbyAttributes(map, FuncionarioBancoHoras.class);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroBuscar(), ex);
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="" desc="CRUD">
    @Override
    public FuncionarioBancoHoras buscar(Class<FuncionarioBancoHoras> entidade, Object id) throws ServiceException {
        try {
            FuncionarioBancoHoras fbh = (FuncionarioBancoHoras) this.funcionarioBancoHorasDao.find(FuncionarioBancoHoras.class, id);
            if (fbh == null) {
                throw new ServiceException(this.getTopPontoResponse().alertaNaoCad(new FuncionarioBancoHoras().toString()));
            }
            return fbh;
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroBuscar(), ex);
        }
    }

    public List<FuncionarioBancoHoras> buscarPorFuncionarioEPeriodo(Funcionario func, Date inicio, Date fim) throws ServiceException {
        try {
            inicio = Utils.configuraHorarioData(inicio, 0, 0, 0);
            fim = Utils.configuraHorarioData(fim, 23, 59, 59);

            List<FuncionarioBancoHoras> fbhs = this.funcionarioBancoHorasDao.buscarFuncionarioPeriodo(func, inicio, fim);

            fbhs.forEach(fbh -> {
                try {
                    BancoHoras bh = this.bancoHorasServices.buscar(fbh.getBancoHoras().getIdBancoHoras());
                    fbh.setBancoHoras(bh);
                    fbh.setFuncionarioBancoHorasFechamentoList(
                            this.funcionarioBancoDeHorasFechamentoService.buscarPorFuncionarioBancoHoras(fbh));
                } catch (ServiceException ex) {
                    LOGGER.debug(this.getClass().getSimpleName(), ex);
                }
            });
            return fbhs;
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroBuscar(), ex);
        }
    }

//    public List<FuncionarioBancoHoras> buscarPorFuncionarioEPeriodoSemDataFim(Funcionario func, Date inicio, Date fim) throws ServiceException {
//        try {
//            inicio = Utils.configuraHorarioData(inicio, 0, 0, 0);
//            fim = Utils.configuraHorarioData(fim, 23, 59, 59);
//            
//            List<FuncionarioBancoHoras> fbhs = this.funcionarioBancoHorasDao.buscarFuncionarioPeriodo(func, inicio, fim);
//
//            fbhs.forEach(fbh -> {
//                try {
//                    BancoHoras bh = this.bancoHorasServices.buscar(fbh.getBancoHoras().getIdBancoHoras());
//                    fbh.setBancoHoras(bh);
//                    fbh.setFuncionarioBancoHorasFechamentoList(
//                            this.funcionarioBancoDeHorasFechamentoService.buscarPorFuncionarioBancoHoras(fbh));
//                } catch (ServiceException ex) {
//                    LOGGER.debug(this.getClass().getSimpleName(), ex);
//                }
//            });
//            return fbhs;
//        } catch (DaoException ex) {
//            throw new ServiceException(this.getTopPontoResponse().erroBuscar(), ex);
//        }
//    }
    public List<FuncionarioBancoHoras> buscarBancoHorasAbertoPeriodo(Funcionario funcionario, Date dataInicio, Date dataTermino) throws ServiceException {
        try {
            dataInicio = Utils.configuraHorarioData(dataInicio, 0, 0, 0);
            dataTermino = Utils.configuraHorarioData(dataTermino, 23, 59, 59);

            List<FuncionarioBancoHoras> fbhList = this.funcionarioBancoHorasDao.buscarFuncionarioBancoHorasAbertoEPeriodo(funcionario, dataInicio, dataTermino);

            fbhList.forEach(fbh -> {
                try {
                    BancoHoras bh = this.bancoHorasServices.buscar(fbh.getBancoHoras().getIdBancoHoras());
                    fbh.setBancoHoras(bh);
                    fbh.setFuncionarioBancoHorasFechamentoList(
                            this.funcionarioBancoDeHorasFechamentoService.buscarPorFuncionarioBancoHoras(fbh));
                } catch (ServiceException ex) {
                    LOGGER.debug(this.getClass().getSimpleName(), ex);
                }
            });
            return fbhList;
        } catch (DaoException ex) {
            throw new ServiceException(ex);
        }
    }

    /**
     * Valida se o banco de horas do funcionário possui fechamento de banco de
     * horas e fechamento de empresa.
     *
     * @param funcionarioBancoHoras
     * @throws ServiceException
     */
    private void validarPossuiFechamento(FuncionarioBancoHoras funcionarioBancoHoras) throws ServiceException {
        Funcionario funcionario = this.funcionarioService.buscar(Funcionario.class, funcionarioBancoHoras.getFuncionario());
        Empresa empresa = this.empresaService.buscar(funcionario.getDepartamento().getEmpresa().getIdEmpresa());
        this.getFechamentoServices().isPossuiFechamentoPeriodo(empresa, funcionarioBancoHoras.getDataInicio(), funcionarioBancoHoras.getDataFim());
//        this.funcionarioBancoDeHorasFechamentoService.validarPossuiFechamentoBancoHoras(funcionarioBancoHoras);
    }

    /**
     * Retorna os funcionarios que possuem banco de horas em aberto dentro do
     * periodo
     *
     * @param funcionarioListIn
     * @param dataInicio
     * @param dataFim
     * @return lista com os funcionarios
     */
    public List<Funcionario> validarBancoHorasAbertoPeriodo(List<Funcionario> funcionarioListIn, Date dataInicio, Date dataFim) {
        List<Funcionario> funcionarioListOut = new ArrayList<>();
        funcionarioListIn.parallelStream().forEach(funcionario -> {
            try {
                List<FuncionarioBancoHoras> listBancoHoras = funcionarioBancoHorasDao.buscarFuncionarioBancoHorasAbertoPeriodo(funcionario, dataInicio, dataFim);
                if (listBancoHoras != null && !listBancoHoras.isEmpty()) {
                    funcionarioListOut.add(funcionario);
                }
            } catch (DaoException ex) {
                LOGGER.debug(this.getClass().getSimpleName(), ex);
            }
        });
        return funcionarioListOut;
    }

    @Override
    public Response salvar(FuncionarioBancoHoras funcionarioBancoHoras) throws ServiceException {
        return this.getTopPontoResponse().sucessoSalvar(funcionarioBancoHoras.toString(), salvarFuncionarioBanco(funcionarioBancoHoras));
    }

    public FuncionarioBancoHoras salvarFuncionarioBanco(FuncionarioBancoHoras funcionarioBancoHoras) throws ServiceException {
        try {
            LOGGER.debug("Início do processo salvar banco de horas");
            funcionarioBancoHoras = validarSalvar(funcionarioBancoHoras);
            this.getDao().refresh(funcionarioBancoHoras.getFuncionario());
            funcionarioBancoHoras = (FuncionarioBancoHoras) this.funcionarioBancoHorasDao.save(funcionarioBancoHoras);
            LOGGER.debug("Término do processo salvar banco de horas");

            this.funcionarioBancoDeHorasFechamentoService.recalcularSubTotaisPosteriores(funcionarioBancoHoras.getFuncionario(), funcionarioBancoHoras.getDataInicio());
            this.getDao().refresh(funcionarioBancoHoras);
            this.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_FUNCIONARIO_BANCO_DE_HORAS, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.INCLUIR, funcionarioBancoHoras);

            //SaldoBH saldoBH = this.fechamentoSubTotal.consultaSaldoUltimoBanco(funcionarioBancoHoras.getFuncionario(), funcionarioBancoHoras.getDataInicio(), funcionarioBancoHoras);
            SaldoBH saldoBH = this.fechamentoSubTotal.getVerificarSaldoBH().calcular(funcionarioBancoHoras.getFuncionario(), funcionarioBancoHoras.getDataInicio());
            
            //cria o fechamento de acerto inicial zerado
            this.fechamentoSubTotal.criarFechamentoSubtotal(funcionarioBancoHoras, saldoBH);

            return funcionarioBancoHoras;
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroSalvar(funcionarioBancoHoras.toString()), ex);
        }
    }

    @Override
    public Response atualizar(FuncionarioBancoHoras funcionarioBancoHoras) throws ServiceException {
        return this.getTopPontoResponse().sucessoAtualizar(funcionarioBancoHoras.toString(), atualizarFuncionarioBancoHoras(funcionarioBancoHoras));
    }

    public FuncionarioBancoHoras atualizarFuncionarioBancoHoras(FuncionarioBancoHoras funcionarioBancoHorasNovo) throws ServiceException {
        try {
            funcionarioBancoHorasNovo = validarAtualizar(funcionarioBancoHorasNovo);
            FuncionarioBancoHoras funcionarioBancoHorasAntigo = (FuncionarioBancoHoras) this.buscar(FuncionarioBancoHoras.class, funcionarioBancoHorasNovo.getIdFuncionarioBancoHoras()).clone();
            Coletivo coletivo = funcionarioBancoHorasAntigo.getColetivo();

            Date dataInicioNovo = funcionarioBancoHorasNovo.getDataInicio();
            Date dataTerminoNovo = funcionarioBancoHorasNovo.getDataFim();
            Date dataInicioAntigo = funcionarioBancoHorasAntigo.getDataInicio();
            Date dataTerminoAntigo = funcionarioBancoHorasAntigo.getDataFim();

            //se houve alteração na data de inicio
            if (dataInicioAntigo.compareTo(dataInicioNovo) != 0) {
                this.funcionarioBancoDeHorasFechamentoService.validarPossuiFechamentoBancoHoras(funcionarioBancoHorasAntigo);
                this.funcionarioBancoDeHorasFechamentoService.validarPossuiFechamentoBancoHorasNovo(funcionarioBancoHorasNovo, funcionarioBancoHorasAntigo.getDataInicio());
            }

//            //se houve alteração na data de termino
//            if ((dataTerminoAntigo != null && dataTerminoNovo == null
//                    || dataTerminoAntigo == null 
//                    || !(dataTerminoAntigo != null && dataTerminoNovo != null
//                    && dataTerminoAntigo.compareTo(dataTerminoNovo) != 0)) {
//                this.funcionarioBancoDeHorasFechamentoService.validarPossuiFechamentoBancoHoras(funcionarioBancoHorasAntigo);
//                this.funcionarioBancoDeHorasFechamentoService.validarPossuiFechamentoBancoHorasNovo(funcionarioBancoHorasNovo, funcionarioBancoHorasAntigo.getDataInicio());
//            }

            funcionarioBancoHorasNovo = (FuncionarioBancoHoras) this.funcionarioBancoHorasDao.save(funcionarioBancoHorasNovo);

            this.getDao().refresh(funcionarioBancoHorasNovo);
            this.fechamentoSubTotal.atualizarFechamentoSubTotalInicial(funcionarioBancoHorasAntigo, funcionarioBancoHorasNovo);
            this.coletivoService.excluirColetivoSemVinculo(coletivo);
            this.funcionarioBancoDeHorasFechamentoService.recalcularSubTotaisPosteriores(funcionarioBancoHorasNovo.getFuncionario(), funcionarioBancoHorasNovo.getDataInicio());
            this.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_FUNCIONARIO_BANCO_DE_HORAS, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.EDITAR, funcionarioBancoHorasNovo);
            return funcionarioBancoHorasNovo;
        } catch (DaoException | CloneNotSupportedException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroAtualizar(funcionarioBancoHorasNovo.toString()), ex);
        }
    }

    @Override
    public Response excluir(Class<FuncionarioBancoHoras> c, Object id) throws ServiceException {
        FuncionarioBancoHoras funcionarioBancoHoras = buscar(FuncionarioBancoHoras.class, id);
        validarPossuiFechamento(funcionarioBancoHoras);
        
        validarExcluir(funcionarioBancoHoras);
        
        Coletivo coletivo = funcionarioBancoHoras.getColetivo();
        if (funcionarioBancoHoras.getColetivo() != null) {
            try {
                funcionarioBancoHoras.setColetivo(null);
                this.funcionarioBancoHorasDao.save(funcionarioBancoHoras);
            } catch (DaoException ex) {

            }
        }
        this.funcionarioBancoDeHorasFechamentoService.removerVinculos(funcionarioBancoHoras);
        this.excluirEntidade(funcionarioBancoHoras);
//        this.getDao().refresh(funcionarioBancoHoras);
        this.funcionarioBancoDeHorasFechamentoService.recalcularSubTotaisPosteriores(funcionarioBancoHoras.getFuncionario(), funcionarioBancoHoras.getDataInicio());
        this.coletivoService.excluirColetivoSemVinculo(coletivo);
        this.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_FUNCIONARIO_BANCO_DE_HORAS, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.EXCLUIR, funcionarioBancoHoras);
        return this.getTopPontoResponse().sucessoExcluir(funcionarioBancoHoras.toString());
    }
//</editor-fold>

    //<editor-fold defaultstate="" desc="VALIDAÇÕES OBRIGATÓRIAS">
    @Override
    public FuncionarioBancoHoras validarSalvar(FuncionarioBancoHoras t) throws ServiceException {
        this.validarCamposObrigatorios(t);
        this.validarPossuiFechamento(t);
        this.validarDataInicialPosteriorDataFinal(t);
        this.validarDemissaoAdmissao(t);
        this.validarPeriodo(t);
        this.validarBancoAberto(t);
        this.validarPosterioBancoAberto(t);
        this.validarAnteriorBancoAberto(t);
        return t;
    }

    @Override
    public FuncionarioBancoHoras validarAtualizar(FuncionarioBancoHoras t) throws ServiceException {
        this.validarIdentificador(t);
        //valida o registro original , se estava cadastrado  em um periodo que possuia fechamnetos
        FuncionarioBancoHoras funcionarioBancoHorasOriginal = this.buscar(FuncionarioBancoHoras.class, t.getIdFuncionarioBancoHoras());

        validarPossuiFechamento(funcionarioBancoHorasOriginal);

        t = this.validarSalvar(t);
        this.validarAlteracaoColetivo(t);
        return t;
    }

    /**
     * Valida se é possível excluir o vínculo entre funcionário e banco de
     * horas. Somente se for o último banco e não possuir fechamentos , o
     * vínculo poderá ser removido
     *
     * @param t
     * @return
     * @throws ServiceException
     */
    @Override
    public FuncionarioBancoHoras validarExcluir(FuncionarioBancoHoras t) throws ServiceException {
        LOGGER.debug("validação excluir funcionário banco de horas");
        this.validarIdentificador(t);
        FuncionarioBancoHoras fbh = this.buscar(FuncionarioBancoHoras.class, t.getIdFuncionarioBancoHoras());
        //carrega os objetos que estão em lazy
        fbh.getFuncionario().setFuncionarioBancoHorasList(this.buscarPorFuncionario(fbh.getFuncionario().getIdFuncionario()));
        this.validarPossuiFechamento(fbh);
        if (fbh.getFuncionario().getFuncionarioBancoHorasList()
                .stream()
                .filter(fj1
                        -> fj1.getDataInicio().after(fbh.getDataInicio())
                        || (fj1.getDataInicio().compareTo(fbh.getDataInicio()) == 0)
                        && !Objects.equals(fj1.getIdFuncionarioBancoHoras(), fbh.getIdFuncionarioBancoHoras()))
                .sorted(Comparator.comparing(FuncionarioBancoHoras::getDataInicio)).count() > 0) {
            throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.FUNCIONARIO_BANCO_DE_HORAS.ALERTA_EXCLUSAO_NAO_PERMITIDA.getResource()));
        }
        return t;
    }
//</editor-fold>

    //<editor-fold defaultstate="" desc="FUNÇÕES AUX">
    public Response validarExclusao(Integer id) throws ServiceException {
        return this.getTopPontoResponse().sucessoValidar(this.validarExcluir(new FuncionarioBancoHoras(id)).toString());
    }
//</editor-fold>

    //<editor-fold defaultstate="" desc="REGRAS DE VALIDAÇÕES">
    /**
     * Valida se o banco de horas possúi um identificador válido
     *
     * @param o
     * @throws ServiceException
     */
    private void validarIdentificador(FuncionarioBancoHoras o) throws ServiceException {
        if (Objects.isNull(o.getIdFuncionarioBancoHoras()) == Boolean.TRUE) {
            throw new ServiceException(this.getTopPontoResponse().alertaNaoCad(o.toString()));
        }
    }

    /**
     * Valida os campos obrigatórios
     *
     * @param o
     * @throws ServiceException
     */
    private void validarCamposObrigatorios(FuncionarioBancoHoras o) throws ServiceException {
        if (Objects.isNull(o.getFuncionario())) {
            throw new ServiceException(this.getTopPontoResponse().campoObrigatorio(FuncionarioBancoHoras_.funcionario.getName()));
        }
        if (Objects.isNull(o.getBancoHoras())) {
            throw new ServiceException(this.getTopPontoResponse().campoObrigatorio(FuncionarioBancoHoras_.bancoHoras.getName()));
        }
        if (Objects.isNull(o.getDataInicio())) {
            throw new ServiceException(this.getTopPontoResponse().campoObrigatorio(FuncionarioBancoHoras_.dataInicio.getName()));
        }
        //Adiciona a entidade funcionario completa para a entidade FuncionarioBancoHoras
        o.setFuncionario(this.funcionarioService.buscar(Funcionario.class, o.getFuncionario().getIdFuncionario()));
    }

    /**
     * Valida se a data de inicio do banco de horas é maior que a data final
     *
     * @param o
     * @throws ServiceException
     */
    private void validarDataInicialPosteriorDataFinal(FuncionarioBancoHoras o) throws ServiceException {
        if (Objects.nonNull(o.getDataFim()) && o.getDataInicio().after(o.getDataFim())) {
            throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.FUNCIONARIO.ALERTA_DATA_INICIAL_MAIOR_DATA_FIM.getResource()));
        }
    }

    /**
     * Valida se o banco de horas está depois da data de demissão do funcionário
     * ou antes da data de admissão
     *
     * @param o
     * @throws ServiceException
     */
    private void validarDemissaoAdmissao(FuncionarioBancoHoras o) throws ServiceException {
        if (Objects.nonNull(o.getFuncionario().getDataDemissao())
                && o.getDataInicio().after(o.getFuncionario().getDataDemissao())) {
            throw new ServiceException(this.getTopPontoResponse().alertaValidacaoComGenero(MSG.FUNCIONARIO.ALERTA_DATA_INICIAL_POSTERIOR_DEMISSAO.getResource(), MSG.FUNCIONARIO_BANCO_DE_HORAS.ALERTA_GENERO_ENTIDADE.getResource()));
        }
        if (Objects.nonNull(o.getFuncionario().getDataAdmissao())
                && o.getDataInicio().before(o.getFuncionario().getDataAdmissao())) {
            throw new ServiceException(this.getTopPontoResponse().alertaValidacaoComGenero(MSG.FUNCIONARIO.ALERTA_DATA_INICIAL_ANTERIOR_ADMISSAO.getResource(), MSG.FUNCIONARIO_BANCO_DE_HORAS.ALERTA_GENERO_ENTIDADE.getResource()));
        }
    }

    /**
     * Valida se o banco de horas foi feito por coletivo, e caso tenha sido e
     * esteja diferente, remove o coletivo do registro
     *
     * @param funcionarioBancoHoras
     * @throws ServiceException
     */
    private void validarAlteracaoColetivo(FuncionarioBancoHoras funcionarioBancoHoras) throws ServiceException {
        FuncionarioBancoHoras fc = this.buscar(FuncionarioBancoHoras.class, funcionarioBancoHoras.getIdFuncionarioBancoHoras());
        if (funcionarioBancoHoras.getIdFuncionarioBancoHoras().equals(fc.getIdFuncionarioBancoHoras())
                && (!Objects.equals(funcionarioBancoHoras.getBancoHoras(), fc.getBancoHoras()))
                || (fc.getDataFim() != null && funcionarioBancoHoras.getDataFim() == null || fc.getDataFim() == null && funcionarioBancoHoras.getDataFim() != null)
                || (fc.getDataFim() != null && funcionarioBancoHoras.getDataFim() != null && funcionarioBancoHoras.getDataFim().compareTo(fc.getDataFim()) != 0)
                || funcionarioBancoHoras.getDataInicio().compareTo(fc.getDataInicio()) != 0) {
            funcionarioBancoHoras.setColetivo(null);
        }
    }

    /**
     * Valida se as datas estão sobrepostas.
     *
     * @param fbh
     * @throws ServiceException
     */
    private void validarPeriodo(FuncionarioBancoHoras fbh) throws ServiceException {

        try {
            List<FuncionarioBancoHoras> listaFuncionarioBancoHoras = this.funcionarioBancoHorasDao.buscarEntreDatas(fbh);

            for (FuncionarioBancoHoras f : listaFuncionarioBancoHoras) {
                if (!Objects.equals(fbh.getIdFuncionarioBancoHoras(), f.getIdFuncionarioBancoHoras())) {
                    throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.FUNCIONARIO_BANCO_DE_HORAS.ALERTA_PERIODO_JA_CADASTRADO.getResource()));
                }
            }

        } catch (DaoException ex) {
            LOGGER.debug(this.getClass().getSimpleName(), ex);
        }

    }

    /**
     * Valida se ja existe um banco de horas em aberto, somente se o registro
     * atual não possuir data final deverá verificar se já existe um registro
     * sem data final
     *
     * @param fbh
     * @throws ServiceException
     */
    private void validarBancoAberto(FuncionarioBancoHoras fbh) throws ServiceException {
        try {
            if (Objects.isNull(fbh.getDataFim())) {

                List<FuncionarioBancoHoras> listaFuncionarioBancoHoras = this.funcionarioBancoHorasDao.buscarbancosAberto(fbh);

                for (FuncionarioBancoHoras f : listaFuncionarioBancoHoras) {
                    if (!Objects.equals(fbh.getIdFuncionarioBancoHoras(), f.getIdFuncionarioBancoHoras())) {
                        throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.FUNCIONARIO_BANCO_DE_HORAS.ALERTA_BANCO_ABERTO.getResource()));
                    }
                }
            }

        } catch (DaoException ex) {
            LOGGER.debug(this.getClass().getSimpleName(), ex);
        }

    }

    /**
     * Valida se existe um banco de horas posterio em aberto, somente se o
     * registro atual não possuir data final deverá verificar se já existe um
     * registro sem data final
     *
     * @param fbh
     * @throws ServiceException
     */
    private void validarPosterioBancoAberto(FuncionarioBancoHoras fbh) throws ServiceException {
        try {
            if (Objects.isNull(fbh.getDataFim())) {
                List<FuncionarioBancoHoras> listaFuncionarioBancoHoras = this.funcionarioBancoHorasDao.buscarbancosAbertoPosterior(fbh);

                for (FuncionarioBancoHoras f : listaFuncionarioBancoHoras) {
                    if (!Objects.equals(fbh.getIdFuncionarioBancoHoras(), f.getIdFuncionarioBancoHoras())) {
                        throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.FUNCIONARIO_BANCO_DE_HORAS.ALERTA_DATA_INICIAL_POSTERIOR_ESTA_DATA.getResource()));
                    }
                }
            }
        } catch (DaoException ex) {
            LOGGER.debug(this.getClass().getSimpleName(), ex);
        }
    }

    /**
     * Valida se existe um banco de horas anterio em aberto, somente se o
     * registro atual possuir data final deverá verificar se já existe um
     * registro sem data final antes do periodo a ser cadastrador
     *
     * @param fbh
     * @throws ServiceException
     */
    private void validarAnteriorBancoAberto(FuncionarioBancoHoras fbh) throws ServiceException {
        try {
            if (Objects.nonNull(fbh.getDataFim())) {
                List<FuncionarioBancoHoras> listaFuncionarioBancoHoras = this.funcionarioBancoHorasDao.buscarbancosAnteriorAberto(fbh);

                for (FuncionarioBancoHoras f : listaFuncionarioBancoHoras) {
                    if (!Objects.equals(fbh.getIdFuncionarioBancoHoras(), f.getIdFuncionarioBancoHoras())) {
                        throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.FUNCIONARIO_BANCO_DE_HORAS.ALERTA_BANCO_ABERTO.getResource()));
                    }
                }
            }
        } catch (DaoException ex) {
            LOGGER.debug(this.getClass().getSimpleName(), ex);
        }
    }

    public FuncionarioBancoHorasDao getFuncionarioBancoHorasDao() {
        return funcionarioBancoHorasDao;
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="COLETIVO">
    /**
     * Método para salvar uma lista de funcionario banco de horas, este método é
     * utilizado pelo lançamento coletivo
     *
     * @param progresso
     * @throws com.topdata.toppontoweb.services.ServiceException
     * @since 13/01/2017
     * @param funcionarioBancoHorasList
     * @return
     */
    public Collection<? extends ColetivoResultado> salvarFuncionarioBancoHorasColetivo(List<FuncionarioBancoHoras> funcionarioBancoHorasList, final ProgressoTransfer progresso) throws ServiceException {
        List<ColetivoResultado> resultadosColetivo = new ArrayList<>();
        List<FuncionarioBancoHoras> listaRemover = new ArrayList<>();
        funcionarioBancoHorasList.stream().forEach((a) -> {
            progresso.addProgresso(1);
            try {
                Response r = a.getIdFuncionarioBancoHoras() == null ? this.salvar(a) : this.atualizar(a);
                resultadosColetivo.add(new ColetivoResultado((Entidade) a, Utils.extractMsgReturn(r), ColetivoResultado.Enum_ResultadoColetivo.LANCADO));
            } catch (ServiceException se) {
                boolean fechamento = se instanceof FechamentoException || se instanceof FechamentoBHException;
                resultadosColetivo.add(new ColetivoResultado((Entidade) a, Utils.extractMsgReturn(se.getResponse()), ColetivoResultado.Enum_ResultadoColetivo.ERRO, fechamento));
                try {
                    if (!fechamento) {
                        listaRemover.add((FuncionarioBancoHoras) a.clone());
                    }
                } catch (CloneNotSupportedException ex) {
                    LOGGER.debug(this.getClass().getSimpleName(), ex);
                }
            }
        });

        //Tenta remover os que deram erros, pois esse é o procedimento do coletivo
        if (!listaRemover.isEmpty()) {
            listaRemover.stream().forEach(entidade -> {
                try {
                    if (entidade.getIdFuncionarioBancoHoras() != null) {
                        this.excluirEntidade(entidade);
                    }
                } catch (ServiceException ex) {
                    LOGGER.debug(this.getClass().getSimpleName(), ex);
                }
            });
        }

        return resultadosColetivo;
    }

    public Response excluirEntidade(FuncionarioBancoHoras funcionarioBancoHoras) throws ServiceException {
        this.funcionarioBancoHorasDao.excluirEntidade(funcionarioBancoHoras);
        this.coletivoService.excluirColetivoSemVinculo(funcionarioBancoHoras.getColetivo());
        this.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_FUNCIONARIO_BANCO_DE_HORAS, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.EXCLUIR, funcionarioBancoHoras);

        return this.getTopPontoResponse().sucessoExcluir(funcionarioBancoHoras.toString());
    }

    /**
     * Método para exclusão de uma lista de afastamento, este método é utilizado
     * pelo lançamento coletivo
     *
     * @since 13/01/2013
     * @param funcionarioBancoHorasList
     * @param coletivo
     * @return
     * @throws ServiceException
     */
    public List<ColetivoResultado> removerColetivo(List<FuncionarioBancoHoras> funcionarioBancoHorasList, Coletivo coletivo) throws ServiceException {

        List<ColetivoResultado> resultadosColetivo = new ArrayList<>();

        funcionarioBancoHorasList.stream().forEach(f -> {
            try {
                if (f.getColetivo() != null) {
                    f.setColetivo(null);
                    f = (FuncionarioBancoHoras) this.funcionarioBancoHorasDao.save(f);
                }

                Response r = this.excluir(FuncionarioBancoHoras.class, f.getIdFuncionarioBancoHoras());
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

    public Response excluirPorColetivo(Coletivo c) throws ServiceException {
        try {
            this.funcionarioBancoHorasDao.excluirPorColetivo(c);
            return this.getTopPontoResponse().sucessoExcluir(c.toString());
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroExcluir(c.toString()), ex);
        }
    }

    /**
     * Método para remover o coletivo de uma lista de afastamento
     *
     * @since 13/01/2013
     * @param funcionarioBancoHorasList
     * @throws ServiceException
     */
    public void removerColetivo(List<FuncionarioBancoHoras> funcionarioBancoHorasList) throws ServiceException {
        if (funcionarioBancoHorasList != null) {
            funcionarioBancoHorasList.stream().map(a -> a.getColetivo()).forEach(ac -> {
                try {
                    this.coletivoService.excluirColetivoSemVinculo(ac);
                } catch (ServiceException ex) {
                    LOGGER.debug(this.getClass().getSimpleName(), ex);
                }
            });
        }
    }
//</editor-fold>
}
