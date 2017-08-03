package com.topdata.toppontoweb.services.funcionario.compensacao;

//<editor-fold defaultstate="collapsed" desc="IMPORTS">
import com.topdata.toppontoweb.services.funcionario.afastamento.AfastamentoService;
import com.topdata.toppontoweb.services.funcionario.jornada.FuncionarioJornadaService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.dao.funcionario.CompensacaoDao;
import com.topdata.toppontoweb.dto.ColetivoResultado;
import com.topdata.toppontoweb.dto.ProgressoTransfer;
import com.topdata.toppontoweb.entity.Entidade;
import com.topdata.toppontoweb.entity.empresa.Empresa;
import com.topdata.toppontoweb.entity.funcionario.Coletivo;
import com.topdata.toppontoweb.entity.funcionario.Compensacao;
import com.topdata.toppontoweb.entity.funcionario.Compensacao_;
import com.topdata.toppontoweb.entity.funcionario.Funcionario;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoService;
import com.topdata.toppontoweb.services.ValidacoesCadastro;
import com.topdata.toppontoweb.services.coletivo.ColetivoService;
import com.topdata.toppontoweb.services.fechamento.FechamentoException;
import com.topdata.toppontoweb.services.funcionario.FuncionarioService;
import com.topdata.toppontoweb.utils.Utils;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;
import com.topdata.toppontoweb.utils.constantes.MSG;
//</editor-fold>

/**
 * @version 1.0.0.0 data 18/01/2017
 * @since 1.0.0.0 data 10/10/2016
 *
 * @author juliano.ezequiel
 */
@Service
public class CompensacaoService extends TopPontoService<Compensacao, Object>
        implements ValidacoesCadastro<Compensacao, Object> {

    //<editor-fold defaultstate="collapsed" desc="CDI">
    @Autowired
    private FuncionarioService funcionarioService;

    @Autowired
    private ColetivoService coletivoService;

    @Autowired
    private CompensacaoDao compensacaoDao;
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="VARIÁVEIS">
    private HashMap<String, Object> map = new HashMap<>();
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="CONSULTAS">
    /**
     * Busca as compensações pelo id do funcionário
     *
     * @param id
     * @return
     * @throws ServiceException
     */
    public List<Compensacao> buscarPorFuncionario(Integer id) throws ServiceException {
        try {
            this.map = new HashMap<>();
            this.map.put(Compensacao_.funcionario.getName(), new Funcionario(id));
            return this.compensacaoDao.findbyAttributes(this.map, Compensacao.class);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroBuscar(), ex);
        }
    }

    /**
     * Busca as compnesações pela de compensacao
     *
     * @param compensacao
     * @return
     * @throws DaoException
     */
    private Compensacao buscarPorDataCompesada(Compensacao compensacao) throws DaoException {
        this.map = new HashMap<>();
        this.map.put(Compensacao_.dataCompensada.getName(), compensacao.getDataCompensada());
        this.map.put(Compensacao_.funcionario.getName(), compensacao.getFuncionario());
        return this.compensacaoDao.findOnebyAttributes(this.map, Compensacao.class);
    }

    /**
     * Método para consulta de compensação por coletivo
     *
     * @since 13/01/2017
     * @param coletivo
     * @return
     * @throws ServiceException
     */
    public List<Compensacao> buscarPorColetivo(Coletivo coletivo) throws ServiceException {
        try {
            this.map = new HashMap<>();
            this.map.put(Compensacao_.coletivo.getName(), coletivo);
            return this.compensacaoDao.findbyAttributes(this.map, Compensacao.class);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroBuscar(), ex);
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="CRUD">
    @Override
    public Compensacao buscar(Class<Compensacao> entidade, Object id) throws ServiceException {
        try {
            Compensacao c = (Compensacao) this.compensacaoDao.find(Compensacao.class, id);
            if (c == null) {
                throw new ServiceException(this.getTopPontoResponse().alertaNaoCad(new Compensacao().toString()));
            }
            return c;
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroBuscar(), ex);
        }
    }

    @Override
    public Response salvar(Compensacao compensacao) throws ServiceException {
        try {
            compensacao = (Compensacao) this.compensacaoDao.save(validarSalvar(compensacao));
            this.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_FUNCIONARIO_AFASTAMENTO, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.INCLUIR, compensacao);
            return this.getTopPontoResponse().sucessoSalvar(compensacao.toString(), compensacao);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroSalvar(compensacao.toString()), ex);
        }
    }

    @Override
    public Response atualizar(Compensacao compensacao) throws ServiceException {
        try {
            compensacao = (Compensacao) this.compensacaoDao.save(validarAtualizar(compensacao));
            compensacao = this.buscar(Compensacao.class, compensacao.getIdCompensacao());
            this.coletivoService.excluirColetivoSemVinculo(compensacao.getColetivo());
            this.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_FUNCIONARIO_COMPENSASOES, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.EDITAR, compensacao);
            return this.getTopPontoResponse().sucessoAtualizar(compensacao.toString(), compensacao);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroAtualizar(compensacao.toString()), ex);
        }
    }

    @Override
    public Response excluir(Class<Compensacao> c, Object id) throws ServiceException {
        Compensacao compensacao = validarExcluir(new Compensacao((Integer) id));
//            this.compensacaoDao.delete(compensacao);
        excluirEntidade(compensacao);
        this.coletivoService.excluirColetivoSemVinculo(compensacao.getColetivo());
        this.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_FUNCIONARIO_COMPENSASOES, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.EXCLUIR, compensacao);
        return this.getTopPontoResponse().sucessoExcluir(compensacao.toString());
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="VALIDAÇÕES OBRIGATÓRIAS">
    @Override
    public Compensacao validarSalvar(Compensacao t) throws ServiceException {

        validarCamposObrigatorios(t);
        validarPossuiFechamento(t);
        validarDataAnterioAdmissao(t);
        validarDataPosterioDemissao(t);
        validarDataCompensadaJaCadastrada(t);
        validarPeriodoJaCadastrado(t);
        validarDataDentroDoPeriodo(t);
        validarExisteDataSerCompesadaDentroDoPeriodo(t);
        validarDataInicialPosteriorDataFinal(t);
        validarPeriodoEmUsoPorOutraDataDeCompensacao(t);

        return t;
    }

    @Override
    public Compensacao validarAtualizar(Compensacao t) throws ServiceException {
        try {
            validarIdentificador(t);
            Compensacao compensacaoOriginal = this.compensacaoDao.find(Compensacao.class, t.getIdCompensacao());
            validarPossuiFechamento(compensacaoOriginal);
            validarSalvar(t);
            validarAlteracaoColetivo(t);
            return t;
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroAtualizar(t), ex);
        }
    }

    @Override
    public Compensacao validarExcluir(Compensacao t) throws ServiceException {
        try {
            validarIdentificador(t);
            t = this.compensacaoDao.find(Compensacao.class, t.getIdCompensacao());
            validarPossuiFechamento(t);
            return t;
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroBuscar(), ex);
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="REGRAS DE VALIDAÇÕES">
    private void validarPossuiFechamento(Compensacao compensacao) throws ServiceException {
        Funcionario funcionario = this.funcionarioService.buscar(Funcionario.class, compensacao.getFuncionario());
        Empresa empresa = funcionario.getDepartamento().getEmpresa();
        this.getFechamentoServices().isPossuiFechamentoPeriodo(empresa, compensacao.getDataInicio(), compensacao.getDataFim());
        this.getFechamentoServices().isPossuiFechamentoPeriodo(empresa, compensacao.getDataCompensada(), null);
    }

    /**
     * Valida se a compensação possúi um identificador válido
     *
     * @param o
     * @return
     * @throws com.topdata.toppontoweb.services.ServiceException
     */
    private void validarIdentificador(Compensacao o) throws ServiceException {
        if (Objects.isNull(o.getIdCompensacao()) == Boolean.TRUE) {
            throw new ServiceException(this.getTopPontoResponse().alertaNaoCad(o.toString()));
        }
    }

    /**
     * Valida os campos obrigatório
     *
     * @param o
     * @return
     * @throws com.topdata.toppontoweb.services.ServiceException
     */
    private void validarCamposObrigatorios(Compensacao o) throws ServiceException {
        if (Objects.isNull(o.getDataInicio()) == Boolean.TRUE || Objects.isNull(o.getDataFim()) == Boolean.TRUE) {
            throw new ServiceException(this.getTopPontoResponse().campoObrigatorio(Compensacao_.dataInicio.getName()));
        }
        if (Objects.isNull(o.getFuncionario()) == Boolean.TRUE) {
            throw new ServiceException(this.getTopPontoResponse().campoObrigatorio(Compensacao_.funcionario.getName()));
        }
        if (Objects.isNull(o.getMotivo()) == Boolean.TRUE) {
            throw new ServiceException(this.getTopPontoResponse().campoObrigatorio(Compensacao_.motivo.getName()));
        }
        if (Objects.isNull(o.getDataCompensada()) == Boolean.TRUE) {
            throw new ServiceException(this.getTopPontoResponse().campoObrigatorio(Compensacao_.dataCompensada.getName()));
        }
        //atribui a instacia completa do funcionário
        o.setFuncionario(funcionarioService.buscar(Funcionario.class, o.getFuncionario().getIdFuncionario()));

    }

    /**
     * Valida se já existe uma compesação com a data a ser compensada
     *
     * @param o
     * @return
     * @throws com.topdata.toppontoweb.services.ServiceException
     */
    private void validarDataCompensadaJaCadastrada(Compensacao o) throws ServiceException {
        try {
            Compensacao compensacao = buscarPorDataCompesada(o);
            if (Objects.nonNull(compensacao) && !Objects.equals(compensacao.getIdCompensacao(), o.getIdCompensacao())) {
                throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.FUNCIONARIO_COMPENSACAO.ALERTA_DATA_COMPESACAO_JA_CADASTRADA.getResource()));
            }
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroValidar(o.toString()), ex);
        }
    }

    /**
     * Valida se já existe algum periodo cadastrado dentro do período informado
     * nesta compensação
     */
    private void validarPeriodoJaCadastrado(Compensacao compensacao) throws ServiceException {

        List<Compensacao> dentroPeriodo = compensacaoDao.buscarPorPeriodoEntidade(compensacao);

        if (dentroPeriodo.iterator().hasNext()) {

            if (compensacao.getIdCompensacao() == null || dentroPeriodo
                    .stream()
                    .filter(dp
                            -> !Objects.equals(
                                    dp.getIdCompensacao(), compensacao.getIdCompensacao())).findAny().orElse(null) != null) {
                throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.FUNCIONARIO_COMPENSACAO.ALERTA_PERIODO_JA_CADASTRADO.getResource()));
            }

        }
    }

    /**
     * Valida se já existe uma data a ser compensada dentro do periodo informado
     * na compensação
     */
    private void validarExisteDataSerCompesadaDentroDoPeriodo(Compensacao compensacao) throws ServiceException {

        List<Compensacao> dentroPeriodo = compensacaoDao.buscarPorPeriodoDataCompensadaEntidade(compensacao);

        if (dentroPeriodo.iterator().hasNext()) {

            if (compensacao.getIdCompensacao() == null || dentroPeriodo
                    .stream()
                    .filter(dp
                            -> !Objects.equals(dp.getIdCompensacao(), compensacao.getIdCompensacao())).findAny().orElse(null) != null) {
                throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.FUNCIONARIO_COMPENSACAO.ALERTA_JA_EXISTE_UMA_DATA_DENTRO_DO_PERIODO.getResource()));
            }

        }
    }

    /**
     * Valida se a data compensada esta dentro de outro periodo já cadastrado
     */
    private void validarPeriodoEmUsoPorOutraDataDeCompensacao(Compensacao compensacao) throws ServiceException {

        List<Compensacao> dentroPeriodo = compensacaoDao.buscarPorDataCompensadaEmUsoEntidade(compensacao);

        if (dentroPeriodo.iterator().hasNext()) {

            if (compensacao.getIdCompensacao() == null || dentroPeriodo
                    .stream()
                    .filter(dp
                            -> !Objects.equals(dp.getIdCompensacao(), compensacao.getIdCompensacao())).findAny().orElse(null) != null) {
                throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.FUNCIONARIO_COMPENSACAO.ALERTA_JA_EXISTE_UMA_DATA_DENTRO_DO_PERIODO.getResource()));
            }

        }
    }

    /**
     * Validar se a data de compensação esta dentro do periodo de compensação
     */
    private void validarDataDentroDoPeriodo(Compensacao o) throws ServiceException {

        if (o.getDataCompensada().after(o.getDataInicio()) && o.getDataCompensada().before(o.getDataFim())
                || o.getDataCompensada().compareTo(o.getDataInicio()) == 0
                || o.getDataCompensada().compareTo(o.getDataFim()) == 0) {
            throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.FUNCIONARIO_COMPENSACAO.ALERTA_DATA_COMPENSADA_DENTRO_PERIODO_COMPENSACAO.getResource()));
        }
    }

    /**
     * Validar se a data a ser compensada ou o periodo de compensação são
     * anterior a data de adimissão
     */
    private void validarDataAnterioAdmissao(Compensacao compensacao) throws ServiceException {
        if (Objects.nonNull(compensacao.getFuncionario().getDataAdmissao())) {
            if (compensacao.getDataCompensada().before(compensacao.getFuncionario().getDataAdmissao())) {
                throw new ServiceException(this.getTopPontoResponse().alertaValidacaoComGenero(MSG.FUNCIONARIO.ALERTA_DATA_COMPENSADA_ANTERIOR_ADMISSAO.getResource(), MSG.FUNCIONARIO_COMPENSACAO.ALERTA_GENERO_ENTIDADE.getResource()));
            }
            if (compensacao.getDataInicio().before(compensacao.getFuncionario().getDataAdmissao())) {
                throw new ServiceException(this.getTopPontoResponse().alertaValidacaoComGenero(MSG.FUNCIONARIO.ALERTA_DATA_INICIAL_ANTERIOR_ADMISSAO.getResource(), MSG.FUNCIONARIO_COMPENSACAO.ALERTA_GENERO_ENTIDADE.getResource()));
            }
            if (compensacao.getDataFim().before(compensacao.getFuncionario().getDataAdmissao())) {
                throw new ServiceException(this.getTopPontoResponse().alertaValidacaoComGenero(MSG.FUNCIONARIO.ALERTA_DATA_FINAL_ANTERIOR_ADMISSAO.getResource(), MSG.FUNCIONARIO_COMPENSACAO.ALERTA_GENERO_ENTIDADE.getResource()));
            }
        }
    }

    /**
     * validar se a data a ser compensada ou o periodo de compensação são
     * posterior a data de demissão
     */
    private void validarDataPosterioDemissao(Compensacao compensacao) throws ServiceException {
        if (Objects.nonNull(compensacao.getFuncionario().getDataDemissao())) {
            if (compensacao.getDataInicio().after(compensacao.getFuncionario().getDataDemissao())) {
                throw new ServiceException(this.getTopPontoResponse().alertaValidacaoComGenero(MSG.FUNCIONARIO.ALERTA_DATA_INICIAL_POSTERIOR_DEMISSAO.getResource(), MSG.FUNCIONARIO_COMPENSACAO.ALERTA_GENERO_ENTIDADE.getResource()));
            }
            if (compensacao.getDataCompensada().after(compensacao.getFuncionario().getDataDemissao())) {
                throw new ServiceException(this.getTopPontoResponse().alertaValidacaoComGenero(MSG.FUNCIONARIO.ALERTA_DATA_COMPENSADA_POSTERIOR_DEMISSAO.getResource(), MSG.FUNCIONARIO_COMPENSACAO.ALERTA_GENERO_ENTIDADE.getResource()));
            }
            if (compensacao.getDataFim().after(compensacao.getFuncionario().getDataDemissao())) {
                throw new ServiceException(this.getTopPontoResponse().alertaValidacaoComGenero(MSG.FUNCIONARIO.ALERTA_DATA_FINAL_POSTERIOR_DEMISSAO.getResource(), MSG.FUNCIONARIO_COMPENSACAO.ALERTA_GENERO_ENTIDADE.getResource()));
            }
        }
    }

    /**
     * validar se a data a ser compensada ou o periodo de compensação são
     * posterior a data de demissão
     */
    private void validarDataInicialPosteriorDataFinal(Compensacao compensacao) throws ServiceException {
        if (compensacao.getDataInicio().after(compensacao.getDataFim())) {
            throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.FUNCIONARIO_COMPENSACAO.ALERTA_DATA_INICIAL_POSTERIOR_DATA_FINAL.getResource()));
        }
    }

    /**
     * Valida se a compensação foi feita por coletivo, e caso tenha sido e
     * esteja diferente, remove o coletivo do registro
     */
    private void validarAlteracaoColetivo(Compensacao compensacao) throws ServiceException {
        Compensacao fc = buscar(Compensacao.class, compensacao.getIdCompensacao());
        if (compensacao.getIdCompensacao().equals(fc.getIdCompensacao())
                && (!Objects.equals(compensacao.getConsideraDiasSemJornada(), fc.getConsideraDiasSemJornada()))
                || !Objects.equals(compensacao.getDataInicio(), fc.getDataInicio())
                || !Objects.equals(compensacao.getDataFim(), fc.getDataFim())
                || !Objects.equals(compensacao.getDataCompensada(), fc.getDataCompensada())
                || !Objects.equals(compensacao.getMotivo(), fc.getMotivo())
                || !Objects.equals(Utils.horasInt(compensacao.getLimiteDiario()), Utils.horasInt(fc.getLimiteDiario()))
                && Objects.equals(compensacao.getManual(), Boolean.TRUE)) {
            compensacao.setColetivo(null);
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="COLETIVO">
    /**
     * Método para salvar uma lista de afastamento, este método é utilizado pelo
     * lançamento coletivo
     *
     * @since 13/01/2017
     * @param compensacaoList
     * @return
     */
    public Collection<? extends ColetivoResultado> salvarCompensacaoColetivo(List<Compensacao> compensacaoList, ProgressoTransfer progresso) throws ServiceException {
        List<ColetivoResultado> resultadosColetivo = new ArrayList<>();
        List<Compensacao> listaRemover = new ArrayList<>();
        compensacaoList.stream().forEach((a) -> {
            try {
                Response r = a.getIdCompensacao() == null ? this.salvar(a) : this.atualizar(a);
                resultadosColetivo.add(new ColetivoResultado((Entidade) a, Utils.extractMsgReturn(r), ColetivoResultado.Enum_ResultadoColetivo.LANCADO));
            } catch (ServiceException se) {
                boolean fechamento = se instanceof FechamentoException;
                resultadosColetivo.add(new ColetivoResultado((Entidade) a, Utils.extractMsgReturn(se.getResponse()), ColetivoResultado.Enum_ResultadoColetivo.ERRO,fechamento));
                try {
                    if (!fechamento) {
                        listaRemover.add((Compensacao) a.clone());
                    }
                } catch (CloneNotSupportedException ex) {
                    Logger.getLogger(FuncionarioJornadaService.class.getName()).log(Level.SEVERE, null, ex);
                }
            } finally {
                progresso.addProgresso(1);
            }
        });

        //Tenta remover os que deram erros, pois esse é o procedimento do coletivo
        if (!listaRemover.isEmpty()) {
            listaRemover.stream().forEach(entidade -> {
                try {
                    if (entidade.getIdCompensacao() != null) {
                        this.excluirEntidade(entidade);
                    }
                } catch (ServiceException ex) {
                    Logger.getLogger(AfastamentoService.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        }

        return resultadosColetivo;
    }

    public Response excluirEntidade(Compensacao entidade) throws ServiceException {
        this.compensacaoDao.excluirEntidade(entidade);
        this.coletivoService.excluirColetivoSemVinculo(entidade.getColetivo());
        this.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_FUNCIONARIO_COMPENSASOES, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.EXCLUIR, entidade);

        return this.getTopPontoResponse().sucessoExcluir(entidade.toString());
    }

    /**
     * Método para exclusão de uma lista de afastamento, este método é utilizado
     * pelo lançamento coletivo
     *
     * @since 13/01/2013
     * @param compensacaoList
     * @param coletivo
     * @return
     * @throws ServiceException
     */
    public List<ColetivoResultado> removerColetivo(List<Compensacao> compensacaoList, Coletivo coletivo) throws ServiceException {
        List<ColetivoResultado> resultadosColetivo = new ArrayList<>();
        compensacaoList.stream().forEach((Compensacao f) -> {
            try {
                if (f.getColetivo() != null) {
                    f.setColetivo(null);
                    f = (Compensacao) this.compensacaoDao.save(f);
                }

                Response r = excluir(Compensacao.class, f.getIdCompensacao());
                f.setColetivo(coletivo);
                resultadosColetivo.add(new ColetivoResultado((Entidade) f, Utils.extractMsgReturn(r), ColetivoResultado.Enum_ResultadoColetivo.EXCLUIDO));
            } catch (ServiceException ex) {
                f.setColetivo(coletivo);
                resultadosColetivo.add(new ColetivoResultado((Entidade) f, Utils.extractMsgReturn(ex.getResponse()), ColetivoResultado.Enum_ResultadoColetivo.ERRO));
            } catch (DaoException ex) {
                f.setColetivo(coletivo);
                Logger.getLogger(AfastamentoService.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        return resultadosColetivo;
    }

    /**
     * Método para remover o coletivo de uma lista de afastamento
     *
     * @since 13/01/2013
     * @param compensacaoList
     * @throws ServiceException
     */
    public void removerColetivo(List<Compensacao> compensacaoList) throws ServiceException {
        compensacaoList.stream().map(a -> a.getColetivo()).forEach(ac -> {
            try {
                this.coletivoService.excluirColetivoSemVinculo(ac);
            } catch (ServiceException ex) {
                Logger.getLogger(AfastamentoService.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
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
            this.compensacaoDao.excluirPorColetivo(c);
            return this.getTopPontoResponse().sucessoExcluir(c.toString());
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroExcluir(c.toString()), ex);
        }
    }
//</editor-fold>

}
