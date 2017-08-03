package com.topdata.toppontoweb.services.funcionario.afastamento;

//<editor-fold defaultstate="collapsed" desc="IMPORTS">
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.dao.funcionario.AfastamentoDao;
import com.topdata.toppontoweb.dto.ColetivoResultado;
import com.topdata.toppontoweb.dto.ProgressoTransfer;
import com.topdata.toppontoweb.entity.Entidade;
import com.topdata.toppontoweb.entity.empresa.Empresa;
import com.topdata.toppontoweb.entity.funcionario.Afastamento;
import com.topdata.toppontoweb.entity.funcionario.Afastamento_;
import com.topdata.toppontoweb.entity.funcionario.Coletivo;
import com.topdata.toppontoweb.entity.funcionario.Funcionario;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoService;
import com.topdata.toppontoweb.services.Validacao;
import com.topdata.toppontoweb.services.ValidacoesCadastro;
import com.topdata.toppontoweb.services.coletivo.ColetivoService;
import com.topdata.toppontoweb.services.empresa.EmpresaService;
import com.topdata.toppontoweb.services.fechamento.FechamentoException;
import com.topdata.toppontoweb.services.funcionario.FuncionarioService;
import com.topdata.toppontoweb.utils.Utils;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;
import com.topdata.toppontoweb.utils.constantes.MSG;
//</editor-fold>

/**
 * @version 1.0.0.0 data 18/01/2017
 * @since 1.0.0.0 data 04/10/2016
 *
 * @author juliano.ezequiel
 */
@Service
public class AfastamentoService extends TopPontoService<Afastamento, Object>
        implements ValidacoesCadastro<Afastamento, Object> {

    //<editor-fold defaultstate="collapsed" desc="CDI">
    @Autowired
    private FuncionarioService funcionarioService;
    @Autowired
    private ColetivoService coletivoService;
    @Autowired
    private AfastamentoDao afastamentoDao;
    @Autowired
    private EmpresaService empresaService;
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="VARIÁVEIS">
    private HashMap<String, Object> mapCriterios = new HashMap<>();
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="CONSULTAS">
    /**
     * Método para consulta de afastamento por coletivo
     *
     * @since 13/01/2017
     * @param coletivo
     * @return Lista com o afastamento do coletivo
     * @throws ServiceException
     */
    public List<Afastamento> buscarPorColetivo(Coletivo coletivo) throws ServiceException {
        try {
            this.mapCriterios = new HashMap<>();
            this.mapCriterios.put(Afastamento_.coletivo.getName(), coletivo);
            return this.afastamentoDao.findbyAttributes(this.mapCriterios, Afastamento.class);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroBuscar(), ex);
        }
    }

    /**
     * Busca os afastamentos pelo id do funcionário
     *
     * @param id
     * @return lista de afastamento do funcionario
     * @throws ServiceException
     */
    public List<Afastamento> buscarPorFuncionario(Integer id) throws ServiceException {
        try {
            mapCriterios = new HashMap<>();
            mapCriterios.put(Afastamento_.funcionario.getName(), new Funcionario(id));
            return this.afastamentoDao.findbyAttributes(mapCriterios, Afastamento.class);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroBuscar(), ex);
        }
    }

    /**
     * Lista os afastamentos do funcionário dentro do período
     *
     * @param idFuncionario
     * @param dataInicio
     * @param dataTermino
     * @return lista de afastamentos
     * @throws ServiceException
     */
    public List<Afastamento> buscarPorFuncionarioEPeriodo(Integer idFuncionario, Date dataInicio, Date dataTermino) throws ServiceException {
        try {
            return afastamentoDao.buscarPorFuncionarioEPeriodo(idFuncionario, dataInicio, dataTermino);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroBuscar(), ex);
        }
    }

    /**
     * Busca os afastamentos pela data de inicio
     *
     * @param afastamento
     * @return lista de afastamentos
     * @throws DaoException
     */
    private Afastamento buscarPorDataInicio(Afastamento afastamento) throws DaoException {
        this.mapCriterios = new HashMap<>();
        this.mapCriterios.put(Afastamento_.dataInicio.getName(), afastamento.getDataInicio());
        this.mapCriterios.put(Afastamento_.funcionario.getName(), afastamento.getFuncionario());
        return (Afastamento) this.afastamentoDao.findOnebyAttributes(this.mapCriterios, Afastamento.class);
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="CRUD">
    @Override
    public Afastamento buscar(Class<Afastamento> entidade, Object id) throws ServiceException {
        try {
            Afastamento c = (Afastamento) this.afastamentoDao.find(Afastamento.class, id);
            if (c == null) {
                throw new ServiceException(this.getTopPontoResponse().alertaNaoCad(new Afastamento().toString()));
            }
            return c;
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroBuscar(), ex);
        }
    }

    @Override
    public Response salvar(Afastamento afastamento) throws ServiceException {
        try {
            afastamento = (Afastamento) this.afastamentoDao.save(validarSalvar(afastamento));
            this.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_FUNCIONARIO_AFASTAMENTO, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.INCLUIR, afastamento);
            return this.getTopPontoResponse().sucessoSalvar(afastamento.toString(), afastamento);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroSalvar(afastamento.toString()), ex);
        }
    }

    public Response salvarLista(List<Afastamento> afastamentoList) throws ServiceException {

        try {
            for (Afastamento afastamento : afastamentoList) {
                validarSalvar(afastamento);
            }
        } catch (ServiceException ex) {
            throw new ServiceException(this.getTopPontoResponse().alertaValidacao(""));
        }

        for (Afastamento afastamento : afastamentoList) {
            try {
                this.afastamentoDao.save(afastamento);
                this.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_FUNCIONARIO_AFASTAMENTO, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.INCLUIR, afastamento);
            } catch (DaoException ex) {
                throw new ServiceException(this.getTopPontoResponse().erroSalvar(new Afastamento().toString()), ex);
            }
        }

        return this.getTopPontoResponse().sucessoSalvar(new Afastamento().toString());
    }

    @Override
    public Response atualizar(Afastamento afastamento) throws ServiceException {
        try {
            afastamento = (Afastamento) this.afastamentoDao.save(validarAtualizar(afastamento));
            this.coletivoService.excluirColetivoSemVinculo(afastamento.getColetivo());
            this.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_FUNCIONARIO_AFASTAMENTO, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.EDITAR, afastamento);
            return this.getTopPontoResponse().sucessoAtualizar(afastamento.toString(), afastamento);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroAtualizar(afastamento.toString()), ex);
        }
    }

    @Override
    public Response excluir(Class<Afastamento> c, Object id) throws ServiceException {
        Afastamento afastamento = this.validarExcluir(new Afastamento((Integer) id));
//            this.afastamentoDao.delete(afastamento);
        this.afastamentoDao.excluirEntidade(afastamento);
        this.coletivoService.excluirColetivoSemVinculo(afastamento.getColetivo());
        this.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_FUNCIONARIO_AFASTAMENTO, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.EXCLUIR, afastamento);
        return this.getTopPontoResponse().sucessoExcluir(afastamento.toString());
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="VALIDAÇÕES OBRIGATÓRIAS">
    @Override
    public Afastamento validarSalvar(Afastamento t) throws ServiceException {

        this.validarCamposObrigatorios(t);
        this.validarPossuiFechamento(t);
        this.validarDataInicialJaCadastrada(t);
        this.validarDataInicialPosteriorDataRetorno(t);
        this.validarPeriodoJaCadastrado(t);
        this.validarDataAnterioAdmissao(t);
        this.validarDataPosterioDemissao(t);

        return t;
    }

    @Override
    public Afastamento validarExcluir(Afastamento afastamento) throws ServiceException {
        this.validarIdentificador(afastamento);
        afastamento = this.buscar(Afastamento.class, afastamento.getIdAfastamento());
        this.validarPossuiFechamento(afastamento);
        return afastamento;
    }

    @Override
    public Afastamento validarAtualizar(Afastamento afastamento) throws ServiceException {
        this.validarIdentificador(afastamento);
        //valida se o afastamento estava cadastrado em periodo que possuia fechamento
        Afastamento afastamentoOriginal = this.buscar(Afastamento.class, afastamento.getIdAfastamento());
        validarPossuiFechamento(afastamentoOriginal);
        this.validarSalvar(afastamento);
        this.ValidarAlteracaoColetivo(afastamento);
        return afastamento;
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="REGRAS DE VALIDAÇÃO">
    private void validarPossuiFechamento(Afastamento afastamento) throws ServiceException {
        Funcionario funcionario = this.funcionarioService.buscar(Funcionario.class, afastamento.getFuncionario());
        Empresa empresa = this.empresaService.buscar(funcionario.getDepartamento().getEmpresa().getIdEmpresa());
        this.getFechamentoServices().isPossuiFechamentoPeriodo(empresa, afastamento.getDataInicio(), afastamento.getDataFim());
    }

    /**
     * Valida se o afastamento possúi um identificador válido
     */
    private void validarIdentificador(Afastamento afastamento) throws ServiceException {
        if (Objects.isNull(afastamento.getIdAfastamento()) == Boolean.TRUE) {
            throw new ServiceException(this.getTopPontoResponse().alertaNaoCad(afastamento.toString()));
        }
    }

    /**
     * Valida os campos obrigatórios do afastamento
     */
    private void validarCamposObrigatorios(Afastamento afastamento) throws ServiceException {
        if (Objects.isNull(afastamento.getDataInicio()) == Boolean.TRUE || Objects.isNull(afastamento.getDataFim()) == Boolean.TRUE) {
            throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.FUNCIONARIO_AFASTAMENTO.ALERTA_PERIODO_INVALIDO.getResource()));
        }
        if (Objects.isNull(afastamento.getFuncionario()) == Boolean.TRUE) {
            throw new ServiceException(this.getTopPontoResponse().campoObrigatorio(Afastamento_.funcionario.getName()));
        }
        if (Objects.isNull(afastamento.getMotivo()) == Boolean.TRUE) {
            throw new ServiceException(this.getTopPontoResponse().campoObrigatorio(Afastamento_.motivo.getName()));
        }
        afastamento.setFuncionario(funcionarioService.buscar(Funcionario.class, afastamento.getFuncionario().getIdFuncionario()));

    }

    /**
     * Valida se já existe um afastamento cadastrado com a data inicial
     */
    private void validarDataInicialJaCadastrada(Afastamento afastamento) throws ServiceException {
        try {
            Afastamento afastamentoBase = this.buscarPorDataInicio(afastamento);
            if (Objects.nonNull(afastamentoBase) && !Objects.equals(afastamentoBase.getIdAfastamento(), afastamento.getIdAfastamento())) {
                throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.FUNCIONARIO_AFASTAMENTO.ALERTA_MESMO_PERIODO.getResource()));
            }
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroValidar(afastamento.toString()), ex);
        }
    }

    /**
     * Valida se a data inicial do afastamento é posterior a data de retorno do
     * afastamento
     */
    private void validarDataInicialPosteriorDataRetorno(Afastamento afastamento) throws ServiceException {
        if (afastamento.getDataFim().before(afastamento.getDataInicio())) {
            throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.FUNCIONARIO_AFASTAMENTO.ALERTA_DATA_INICIAL_POSTERIOR_RETORNO.getResource()));
        }
    }

    /**
     * Valida se já existe um período cadastrado para o afastamento
     */
    private void validarPeriodoJaCadastrado(Afastamento afastamento) throws ServiceException {

        //Busca todos os afastamentos pelo funcionario, exceto o afastamento que será comparado
        List<Afastamento> dentroPeriodo = afastamentoDao.buscarPorPeriodoEntidade(afastamento);

        if (dentroPeriodo.iterator().hasNext()) {

            if (afastamento.getIdAfastamento() == null || dentroPeriodo
                    .stream()
                    .filter(dp -> !Objects.equals(dp.getIdAfastamento(), afastamento.getIdAfastamento()))
                    .findAny().orElse(null) != null) {
                throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.FUNCIONARIO_AFASTAMENTO.ALERTA_MESMO_PERIODO.getResource()));
            }

        }
    }

    /**
     * Valida se o afastamento está antes da data de admissão do funcionário
     */
    private void validarDataAnterioAdmissao(Afastamento afastamento) throws ServiceException {
        if (Objects.nonNull(afastamento.getFuncionario().getDataAdmissao())) {
            if (afastamento.getDataInicio().before(afastamento.getFuncionario().getDataAdmissao())) {
                throw new ServiceException(this.getTopPontoResponse().alertaValidacaoComGenero(MSG.FUNCIONARIO.ALERTA_DATA_INICIAL_ANTERIOR_ADMISSAO.getResource(), MSG.FUNCIONARIO_AFASTAMENTO.ALERTA_GENERO_ENTIDADE.getResource()));
            }
        }
    }

    /**
     * Valida se o afastamento está depois da data de demissão do funcionário
     */
    private void validarDataPosterioDemissao(Afastamento afastamento) throws ServiceException {
        if (Objects.nonNull(afastamento.getFuncionario().getDataDemissao())) {
            if (afastamento.getDataInicio().after(afastamento.getFuncionario().getDataDemissao())) {
                throw new ServiceException(this.getTopPontoResponse().alertaValidacaoComGenero(MSG.FUNCIONARIO.ALERTA_DATA_INICIAL_POSTERIOR_DEMISSAO.getResource(), MSG.FUNCIONARIO_AFASTAMENTO.ALERTA_GENERO_ENTIDADE.getResource()));
            }
        }
    }

    /**
     * Valida se o afastamento foi feita por coletivo, e caso tenha sido e
     * esteja diferente, remove o coletivo do registro
     */
    private void ValidarAlteracaoColetivo(Afastamento afastamento) throws ServiceException {
        Afastamento fc = this.buscar(Afastamento.class, afastamento.getIdAfastamento());

        if (afastamento.getIdAfastamento().equals(fc.getIdAfastamento())
                && (!afastamento.getDataFim().equals(fc.getDataFim()) || !afastamento.getDataInicio().equals(fc.getDataInicio())
                || !afastamento.getAbonado().equals(fc.getAbonado()) || !afastamento.getFuncionario().equals(fc.getFuncionario())
                || !afastamento.getMotivo().equals(fc.getMotivo()))
                && Objects.equals(afastamento.getManual(), Boolean.TRUE)) {
            afastamento.setColetivo(null);
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="COLETIVO">
    /**
     * Método para salvar uma lista de afastamento, este método é utilizado pelo
     * lançamento coletivo
     *
     * @param progresso Sinaliza o progresso na tela
     * @since 13/01/2017
     * @param afastamentoList
     * @return
     */
    public Collection<? extends ColetivoResultado> salvarAfastamentoColetivo(List<Afastamento> afastamentoList, ProgressoTransfer progresso) throws ServiceException {
        List<ColetivoResultado> resultadosColetivo = new ArrayList<>();
        List<Afastamento> listaRemover = new ArrayList<>();
        afastamentoList.stream().forEach((a) -> {
            try {
                Response r = a.getIdAfastamento() == null ? this.salvar(a) : this.atualizar(a);
                ColetivoResultado coletivoResultado = new ColetivoResultado((Entidade) a, Utils.extractMsgReturn(r), ColetivoResultado.Enum_ResultadoColetivo.LANCADO);
                resultadosColetivo.add(coletivoResultado);
            } catch (ServiceException se) {
                boolean fechamento = se instanceof FechamentoException;
                final ColetivoResultado coletivoResultado = new ColetivoResultado((Entidade) a, Utils.extractMsgReturn(se.getResponse()), ColetivoResultado.Enum_ResultadoColetivo.ERRO, fechamento);
                resultadosColetivo.add(coletivoResultado);
                if (!fechamento) {
                    listaRemover.add(a);
                }
            } finally {
                progresso.addProgresso(1);
            }
        });

        //Tenta remover os que deram erros, pois esse é o procedimento do coletivo
        if (!listaRemover.isEmpty()) {
            listaRemover.stream().forEach(entidade -> {
                try {
                    if (entidade.getIdAfastamento() != null) {
                        this.excluirEntidade(entidade);
                    }
                } catch (ServiceException ex) {
                    Logger.getLogger(AfastamentoService.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        }

        return resultadosColetivo;
    }

    public Response excluirEntidade(Afastamento afastamento) throws ServiceException {
        this.afastamentoDao.excluirEntidade(afastamento);
        this.coletivoService.excluirColetivoSemVinculo(afastamento.getColetivo());
        this.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_FUNCIONARIO_AFASTAMENTO, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.EXCLUIR, afastamento);

        return this.getTopPontoResponse().sucessoExcluir(afastamento.toString());
    }

    /**
     * Método para exclusão de uma lista de afastamento, este método é utilizado
     * pelo lançamento coletivo
     *
     * @since 13/01/2013
     * @param afastamentoList
     * @param coletivo
     * @return
     * @throws ServiceException
     */
    public List<ColetivoResultado> removerColetivo(List<Afastamento> afastamentoList, Coletivo coletivo) throws ServiceException {
        List<ColetivoResultado> resultadosColetivo = new ArrayList<>();

//        afastamentoList.stream().forEach(f -> {
        for (Afastamento f : afastamentoList) {
            try {
                if (f.getColetivo() != null) {
                    f.setColetivo(null);
                    f = (Afastamento) this.afastamentoDao.save(f);
                }

                Response r = excluir(Afastamento.class, f.getIdAfastamento());
                f.setColetivo(coletivo);
                resultadosColetivo.add(new ColetivoResultado((Entidade) f, Utils.extractMsgReturn(r), ColetivoResultado.Enum_ResultadoColetivo.EXCLUIDO));
            } catch (ServiceException ex) {
                f.setColetivo(coletivo);
                resultadosColetivo.add(new ColetivoResultado((Entidade) f, Utils.extractMsgReturn(ex.getResponse()), ColetivoResultado.Enum_ResultadoColetivo.ERRO));
            } catch (DaoException ex) {
                f.setColetivo(coletivo);
                Logger.getLogger(AfastamentoService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return resultadosColetivo;
    }

    /**
     * Método para remover o coletivo de uma lista de afastamento
     *
     * @since 13/01/2013
     * @param afastamentoList
     * @throws ServiceException
     */
    public void removerColetivo(List<Afastamento> afastamentoList) throws ServiceException {
        if (afastamentoList != null) {
            afastamentoList.stream().map(a -> a.getColetivo()).forEach(ac -> {
                try {
                    this.coletivoService.excluirColetivoSemVinculo(ac);
                } catch (ServiceException ex) {
                    Logger.getLogger(AfastamentoService.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        }
    }

    /**
     * Método para remover todos os afastamentos de um coletivo
     *
     * @return
     * @since 13/01/2013
     * @param c coletivo
     * @throws ServiceException
     */
    public Response excluirPorColetivo(Coletivo c) throws ServiceException {
        try {
            this.afastamentoDao.excluirPorColetivo(c);
            return this.getTopPontoResponse().sucessoExcluir(c.toString());
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroExcluir(c.toString()), ex);
        }
    }
//</editor-fold>

}
