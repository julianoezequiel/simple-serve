package com.topdata.toppontoweb.services.funcionario.jornada;

//<editor-fold defaultstate="collapsed" desc="IMPORTS">
import com.topdata.toppontoweb.services.funcionario.jornada.ValidacaoEntreJornadas;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.dao.funcionario.FuncionarioJornadaDao;
import com.topdata.toppontoweb.dto.ColetivoResultado;
import com.topdata.toppontoweb.dto.MsgRetorno;
import com.topdata.toppontoweb.dto.ProgressoTransfer;
import com.topdata.toppontoweb.entity.Entidade;
import com.topdata.toppontoweb.entity.configuracoes.SequenciaPercentuais;
import com.topdata.toppontoweb.entity.empresa.Empresa;
import com.topdata.toppontoweb.entity.funcionario.Coletivo;
import com.topdata.toppontoweb.entity.funcionario.Funcionario;
import com.topdata.toppontoweb.entity.funcionario.FuncionarioJornada;
import com.topdata.toppontoweb.entity.funcionario.FuncionarioJornada_;
import com.topdata.toppontoweb.entity.jornada.Jornada;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoService;
import com.topdata.toppontoweb.services.ValidacoesCadastro;
import com.topdata.toppontoweb.services.coletivo.ColetivoService;
import com.topdata.toppontoweb.services.fechamento.FechamentoException;
import com.topdata.toppontoweb.services.funcionario.afastamento.AfastamentoService;
import com.topdata.toppontoweb.services.funcionario.FuncionarioService;
import com.topdata.toppontoweb.services.jornada.HorarioMarcacaoService;
import com.topdata.toppontoweb.services.jornada.JornadaService;
import com.topdata.toppontoweb.utils.DateHelper;
import com.topdata.toppontoweb.utils.Utils;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;
import com.topdata.toppontoweb.utils.constantes.MSG;
//</editor-fold>

/**
 * @version 1.0.0.0 data 18/01/2017
 * @since 1.0.0.0 data 14/09/2016
 *
 * @author juliano.ezequiel
 */
@Service
public class FuncionarioJornadaService extends TopPontoService<FuncionarioJornada, Object>
        implements ValidacoesCadastro<FuncionarioJornada, Object> {

    //<editor-fold defaultstate="collapsed" desc="CDI">
    @Autowired
    private FuncionarioService funcionarioService;
    @Autowired
    private JornadaService jornadaService;
    @Autowired
    private FuncionarioJornadaDao funcionarioJornadaDao;
    @Autowired
    private ValidacaoEntreJornadas validacaoEntreJornadas;
    @Autowired
    private ColetivoService coletivoService;
    @Autowired
    private HorarioMarcacaoService horarioMarcacaoService;
    //</editor-fold>

    private HashMap<String, Object> map;

    //<editor-fold defaultstate="collapsed" desc="CONSULTAS">
    /**
     * Buscas as jornadas vinculadas ao funcionário
     *
     * @param funcionario
     * @return
     * @throws ServiceException
     */
    private List<FuncionarioJornada> buscarPorFuncionario(Funcionario funcionario, boolean carregarSubObjetos) throws ServiceException {
        try {
            List<FuncionarioJornada> funcionarioJornadaList = this.funcionarioJornadaDao.buscarFuncionario(funcionario);

            if (carregarSubObjetos) {
                funcionarioJornadaList = carregarSubObjetos(funcionarioJornadaList);
            }

            return funcionarioJornadaList;
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroBuscar(), ex);
        }
    }

    /**
     * Buscas as jornadas vinculadas ao funcionário
     *
     * @param funcionario
     * @return
     * @throws ServiceException
     */
    private List<FuncionarioJornada> buscarPorFuncionario(Funcionario funcionario) throws ServiceException {
        try {
            this.map = new HashMap<>();
            this.map.put(FuncionarioJornada_.funcionario.getName(), funcionario);
            return this.funcionarioJornadaDao.findbyAttributes(map, FuncionarioJornada.class);

        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroBuscar(), ex);
        }
    }

    /**
     * Busca o funcionario jornada pelo id do funcionário
     *
     * @param id
     * @param carregarSubObjetos
     * @return
     * @throws ServiceException
     */
    public List<FuncionarioJornada> buscarPorFuncionario(Integer id, boolean carregarSubObjetos) throws ServiceException {
        return buscarPorFuncionario(new Funcionario(id), carregarSubObjetos);
    }

    public List<FuncionarioJornada> buscarPorColetivo(Coletivo coletivo) throws ServiceException {
        try {
            this.map = new HashMap<>();
            this.map.put(FuncionarioJornada_.coletivo.getName(), coletivo);
            return this.funcionarioJornadaDao.findbyAttributes(this.map, FuncionarioJornada.class);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroBuscar(), ex);
        }
    }

    public List<FuncionarioJornada> buscarPorJornada(Jornada jornada) throws ServiceException {
        try {
            this.map = new HashMap<>();
            this.map.put(FuncionarioJornada_.jornada.getName(), jornada);
            return this.funcionarioJornadaDao.findbyAttributes(map, FuncionarioJornada.class);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroBuscar(), ex);
        }
    }

    public List<FuncionarioJornada> buscarEntreDatasFuncionario(Funcionario funcionario, Date dataInicio, Date dataFim) throws ServiceException {
        try {
            List<FuncionarioJornada> funcionarioJornadalist = this.funcionarioJornadaDao.buscarEntreDatasFuncionario(funcionario, dataInicio, dataFim);

            return carregarSubObjetos(funcionarioJornadalist);
        } catch (DaoException ex) {
            throw new ServiceException(ex);
        }
    }
    
    public Long buscarEntreDatasFuncionarioQuantidade(Funcionario funcionario, Date dataInicio, Date dataFim) throws ServiceException {
        try {
            return this.funcionarioJornadaDao.buscarEntreDatasFuncionarioQuantidade(funcionario, dataInicio, dataFim);
        } catch (DaoException ex) {
            throw new ServiceException(ex);
        }
    }

    public List<FuncionarioJornada> carregarSubObjetos(List<FuncionarioJornada> funcionarioJornadalist) {
        if (!funcionarioJornadalist.isEmpty()) {
            //para cada item carrega os horarios da lista
            funcionarioJornadalist
                    .stream()
                    .forEach(fj -> {
                        if (fj.getJornada() != null && fj.getJornada().getJornadaHorarioList() != null && !fj.getJornada().getJornadaHorarioList().isEmpty()) {
                            fj.getJornada().getJornadaHorarioList()
                                    .stream()
                                    .forEach(jh -> {
                                        try {
                                            jh.getHorario().setHorarioMarcacaoList(this.horarioMarcacaoService.buscarPorHorario(jh.getHorario().getIdHorario()));
                                        } catch (ServiceException ex) {
                                            Logger.getLogger(FuncionarioJornadaService.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    });
                            fj.getJornada().setJornadaHorarioList(fj.getJornada().getJornadaHorarioList()
                                    .stream().distinct().collect(Collectors.toList()));
                            if (fj.getJornada().getPercentuaisAcrescimo() != null) {
                                List<SequenciaPercentuais> sequenciaPercentuaisList = fj.getJornada().getPercentuaisAcrescimo().getSequenciaPercentuaisList();
                                List<SequenciaPercentuais> sequenciaPercentuaisList2 = new ArrayList<>();
                                sequenciaPercentuaisList
                                        .stream()
                                        .forEach(sp -> {
                                            if (!sequenciaPercentuaisList2.contains(sp)) {
                                                sequenciaPercentuaisList2.add(sp);
                                            }
                                        });
                                fj.getJornada().getPercentuaisAcrescimo().setSequenciaPercentuaisList(sequenciaPercentuaisList2);
                            }
                        }
                    });
        }
        return funcionarioJornadalist;
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="CRUD">
    @Override
    public FuncionarioJornada buscar(Class<FuncionarioJornada> entidade, Object id) throws ServiceException {
        try {
            FuncionarioJornada fj = (FuncionarioJornada) this.funcionarioJornadaDao.find(FuncionarioJornada.class, id);
            if (fj == null) {
                throw new ServiceException(this.getTopPontoResponse().alertaNaoCad(new FuncionarioJornada().toString()));
            }
            return fj;
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroBuscar(), ex);
        }
    }

    @Override
    public Response salvar(FuncionarioJornada funcionarioJornada) throws ServiceException {
        try {
            funcionarioJornada = this.validarSalvar(funcionarioJornada);
            funcionarioJornada = (FuncionarioJornada) this.funcionarioJornadaDao.save(funcionarioJornada);
            this.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_FUNCIONARIO_JORNADAS, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.INCLUIR, funcionarioJornada);
            return this.getTopPontoResponse().sucessoSalvar(funcionarioJornada.toString(), funcionarioJornada);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroSalvar(funcionarioJornada.toString()), ex);
        }

    }

    @Override
    public Response atualizar(FuncionarioJornada funcionarioJornada) throws ServiceException {
        try {
            funcionarioJornada = this.validarAtualizar(funcionarioJornada);
            funcionarioJornada = (FuncionarioJornada) this.funcionarioJornadaDao.save(funcionarioJornada);
            this.coletivoService.excluirColetivoSemVinculo(funcionarioJornada.getColetivo());
            this.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_FUNCIONARIO_JORNADAS, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.EDITAR, funcionarioJornada);
            return this.getTopPontoResponse().sucessoAtualizar(funcionarioJornada.toString(), funcionarioJornada);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroAtualizar(funcionarioJornada.toString()), ex);
        } catch (ServiceException se) {
            throw se;
        }
    }

    @Override
    public Response excluir(Class<FuncionarioJornada> c, Object id) throws ServiceException {
        return this.excluir(this.validarExcluir(new FuncionarioJornada((Integer) id)));
    }

    public Response excluir(FuncionarioJornada funcionarioJornada) throws ServiceException {
//        try {
//            this.funcionarioJornadaDao.delete(funcionarioJornada);
        this.excluirEntidade(funcionarioJornada);
        this.coletivoService.excluirColetivoSemVinculo(funcionarioJornada.getColetivo());
        this.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_FUNCIONARIO_JORNADAS, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.EXCLUIR, funcionarioJornada);
        return this.getTopPontoResponse().sucessoExcluir(funcionarioJornada.toString());
//        } catch (DaoException ex) {
//            throw new ServiceException(this.getTopPontoResponse().erroExcluir(new FuncionarioJornada().toString()), ex);
//        }
    }

    public Response excluirPorColetivo(Coletivo c) throws ServiceException {
        try {
            this.funcionarioJornadaDao.excluirPorColetivo(c);
            return this.getTopPontoResponse().sucessoExcluir(c.toString());
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroExcluir(c.toString()), ex);
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="VALIDAÇÕES OBRIGATÓRIAS">
    @Override
    public FuncionarioJornada validarSalvar(FuncionarioJornada funcionarioJornada) throws ServiceException {

        this.validarCamposObrigatorio(funcionarioJornada);
        validarPossuiFechamento(funcionarioJornada);
        this.validarDataInicioSalvar(funcionarioJornada);
        this.validarDataAdmissao(funcionarioJornada);
        this.validarDataDemissao(funcionarioJornada);
        this.validarDataInicioDataTermino(funcionarioJornada);
        this.validarJornadaDiaVariaveis(funcionarioJornada);
        this.validacaoEntreJornadas.validar(funcionarioJornada);

        if (Objects.equals(funcionarioJornada.getExcecaoJornada(), Boolean.TRUE)) {
            this.validarExisteJornada(funcionarioJornada);
            this.validarExcecoesJornadasAnterior(funcionarioJornada);
            this.validarExcecoesJornadasPosterior(funcionarioJornada);
            this.validarExcecoesJornadasDentroPeriodo(funcionarioJornada);
        } else {
            this.validarJornadaEmVigencia(funcionarioJornada);
        }

        return funcionarioJornada;
    }

    @Override
    public FuncionarioJornada validarAtualizar(FuncionarioJornada funcionarioJornada) throws ServiceException {
        this.validarIdentificador(funcionarioJornada);
        //valida se a jornada do funcionario estava em um periodo que possuia fechamento
        validarPossuiFechamento(funcionarioJornada);

        this.validarAlteracaoColetivo(funcionarioJornada);
        funcionarioJornada = this.validarSalvar(funcionarioJornada);
        this.validarUltimoRegistro(funcionarioJornada);
        return funcionarioJornada;
    }

    @Override
    public FuncionarioJornada validarExcluir(FuncionarioJornada funcionarioJornada) throws ServiceException {
        try {
            this.validarIdentificador(funcionarioJornada);
            //busca o registro na base
            FuncionarioJornada fj = this.buscar(FuncionarioJornada.class, funcionarioJornada.getIdJornadaFuncionario());

            validarPossuiFechamento(fj);

            //se não for uma exceção de jornada, é realizada validações específicas
            if (Objects.equals(fj.getExcecaoJornada(), Boolean.FALSE)) {
                //verifica qual é o registro anterior e o posterior
                List<FuncionarioJornada> anterior = this.funcionarioJornadaDao.buscarAnterior(fj);
//                //se não existir anterior, ESTA É A PRIMIRA JORNADA, deve-se verificar se existe alguma exceção de jornada neste periodo
//                if (anterior.isEmpty() && !funcionarioJornadaDao.buscarExcecaoDeJornada(fj).isEmpty()) {
//                    throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.FUNCIONARIO_JORNADA.ALERTA_EXCECAO_EXCLUIR_PRIMEIRA_JORNADA.getResource()));
//                }

                //busca o total de jornadas normal
                this.map = new HashMap<>();
                this.map.put(FuncionarioJornada_.funcionario.getName(), fj.getFuncionario());
                this.map.put(FuncionarioJornada_.excecaoJornada.getName(), false);
                Long totalJornadaNormal = this.funcionarioJornadaDao.countByCriteria(this.map, FuncionarioJornada.class);
                //busca o total de exceções de jornadas
                this.map.replace(FuncionarioJornada_.excecaoJornada.getName(), true);
                Long totalExcecoes = this.funcionarioJornadaDao.countByCriteria(this.map, FuncionarioJornada.class);

                //se não existir anterior, ESTA É A PRIMEIRA JORNADA, deve-se verificar se existe alguma exceção de jornada neste periodo
                if (totalJornadaNormal <= 1 && totalExcecoes >= 1) {
                    throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.FUNCIONARIO_JORNADA.ALERTA_EXCECAO_EXCLUIR_PRIMEIRA_JORNADA.getResource()));
                }

                //valida se existe 
                FuncionarioJornada primeiraJornada = funcionarioJornadaDao.buscarPrimeira(fj);
                if (primeiraJornada.getDataFim() != null && !funcionarioJornadaDao.buscarExcecaoDeJornadaEntreDatas(primeiraJornada).isEmpty()) {
                    throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.FUNCIONARIO_JORNADA.ALERTA_EXCECAO_EXCLUIR_PRIMEIRA_JORNADA.getResource()));
                }

                List<FuncionarioJornada> posterior = this.funcionarioJornadaDao.buscarPosterior(fj);
                //se existir uma jornada anterior, ajusta a data de fim para o inicio da próxima jornada
                //caso não exista uma próxima jornada, a data fim será null
                if (Objects.equals(funcionarioJornada.getValidarColetivo(), Boolean.FALSE)) {
                    if (!anterior.isEmpty()) {
                        //busca na base a referencia completa
                        FuncionarioJornada fj1 = this.buscar(FuncionarioJornada.class, anterior.iterator().next().getIdJornadaFuncionario());
                        if (posterior.isEmpty()) {
                            fj1.setDataFim(null);
                        } else {
                            FuncionarioJornada fj2 = this.buscar(FuncionarioJornada.class, posterior.iterator().next().getIdJornadaFuncionario());
                            fj1.setDataFim(Utils.addDias(fj2.getDataInicio(), -1));
                        }
                        this.funcionarioJornadaDao.save(fj1);
                    }
                }
            }
            return this.buscar(FuncionarioJornada.class, funcionarioJornada.getIdJornadaFuncionario());
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroExcluir(new FuncionarioJornada().toString()));
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="REGRAS DE VALIDAÇÕES">
    public void validarPossuiFechamento(FuncionarioJornada funcionarioJornada) throws ServiceException {
        LOGGER.debug("validando funcionário possui jornada com empresa fechamento");
        Funcionario funcionario = this.funcionarioService.buscar(Funcionario.class, funcionarioJornada.getFuncionario());
        Empresa empresa = funcionario.getDepartamento().getEmpresa();

        if (funcionarioJornada.getIdJornadaFuncionario() != null) {
            FuncionarioJornada funcionarioJornadaOriginal = this.buscar(FuncionarioJornada.class, funcionarioJornada.getIdJornadaFuncionario());
            Date dataFim = funcionarioJornadaOriginal.getDataFim() == null ? funcionarioJornada.getDataInicio() : funcionarioJornadaOriginal.getDataFim();
            LOGGER.debug("validando funcionario possui jornada com fechamento. EMPRESA : {} - FUNCIONÁRIO : {} - DATA INÍCIO {} - DATA FIM {}", empresa.getRazaoSocial(), funcionarioJornadaOriginal.getFuncionario().getNome(), funcionarioJornadaOriginal.getDataInicio(), funcionarioJornadaOriginal.getDataFim());
            this.getFechamentoServices().isPossuiFechamentoPeriodo(empresa, funcionarioJornadaOriginal.getDataInicio(), dataFim);
        } else {
            LOGGER.debug("validando funcionario possui jornada com fechamento. EMPRESA : {} - FUNCIONÁRIO : {} - DATA INÍCIO {} - DATA FIM {}", empresa.getRazaoSocial(), funcionarioJornada.getFuncionario().getNome(), funcionarioJornada.getDataInicio(), funcionarioJornada.getDataFim());
            this.getFechamentoServices().isPossuiFechamentoPeriodo(empresa, funcionarioJornada.getDataInicio(), funcionarioJornada.getDataInicio());
        }
    }

    /**
     * valida se o registro possui um id
     *
     * @param funcionarioJornada
     * @throws ServiceException
     */
    private void validarIdentificador(FuncionarioJornada funcionarioJornada) throws ServiceException {
        if (funcionarioJornada.getIdJornadaFuncionario() == null) {
            throw new ServiceException(this.getTopPontoResponse().alertaNaoCad(funcionarioJornada.toString()));
        }
    }

    /**
     * Validações dos campos obrigatórios
     *
     * @param funcionarioJornada
     * @throws ServiceException
     */
    private void validarCamposObrigatorio(FuncionarioJornada funcionarioJornada) throws ServiceException {
        if (funcionarioJornada.getFuncionario() == null) {
            throw new ServiceException(this.getTopPontoResponse().campoObrigatorio(FuncionarioJornada_.funcionario.getName()));
        }
        if (funcionarioJornada.getDataInicio() == null) {
            throw new ServiceException(this.getTopPontoResponse().campoObrigatorio(FuncionarioJornada_.dataInicio.getName()));
        }
        if (funcionarioJornada.getDataFim() == null) {
            funcionarioJornada.setDataInicio(funcionarioJornada.getDataInicio());

        }
        //busca o funcionario e vincula ao registro em validação
        funcionarioJornada.setFuncionario(funcionarioService.buscar(Funcionario.class, funcionarioJornada.getFuncionario().getIdFuncionario()));
        //busca a jornada e vincula ao funcionário

        if (funcionarioJornada.getJornada() != null) {
            funcionarioJornada.setJornada(jornadaService.buscar(Jornada.class, funcionarioJornada.getJornada().getIdJornada()));
        }
    }

    /**
     * Valida se a jornada vinvulada é variavél
     *
     * @param funcionarioJornada
     * @throws ServiceException
     */
    private void validarJornadaDiaVariaveis(FuncionarioJornada funcionarioJornada) throws ServiceException {
        if (funcionarioJornada.getJornada() != null && Objects.equals(funcionarioJornada.getJornada().getTipoJornada().getIdTipoJornada(), CONSTANTES.Enum_TIPO_JORNADA.VARIAVEL.ordinal()) && funcionarioJornada.getSequenciaInicial() == null) {
            throw new ServiceException(this.getTopPontoResponse().campoObrigatorio(MSG.FUNCIONARIO_JORNADA.ALERTA_CAMPO_SEQUENCIA_INICIAL.getResource()));
        }
    }

    /**
     * Valida se já existe uma jornada para na mesma data de inicio
     */
    private void validarDataInicioSalvar(FuncionarioJornada funcionarioJornada) throws ServiceException {

        List<FuncionarioJornada> jornadaLista = buscarPorFuncionario(funcionarioJornada.getFuncionario());
        if (jornadaLista.
                stream().filter(fj -> {
                    return !fj.getIdJornadaFuncionario().equals(funcionarioJornada.getIdJornadaFuncionario())
                            && fj.getDataInicio().compareTo(funcionarioJornada.getDataInicio()) == 0
                            && Objects.equals(fj.getExcecaoJornada(), funcionarioJornada.getExcecaoJornada());
                }).findAny().orElse(null) != null) {
            throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.FUNCIONARIO_JORNADA.ALERTA_DATA_INICIAL_JA_CADASTRADA.getResource()));
        }
    }

    /**
     * Valida se a data de inicio é inferior a data de fim
     */
    private void validarDataInicioDataTermino(FuncionarioJornada funcionarioJornada) throws ServiceException {
        if (funcionarioJornada.getDataFim() != null) {
            if (funcionarioJornada.getDataInicio().after(funcionarioJornada.getDataFim())) {
                throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.FUNCIONARIO_JORNADA.ALERTA_DATA_INICIAL_MAIOR_DATA_FIM.getResource()));
            }
        }
    }

    /**
     * verifica se a data de início é anterior a data de admissão do funcionário
     *
     * @param funcionarioJornada
     * @throws com.topdata.toppontoweb.services.ServiceException
     */
    public void validarDataAdmissao(FuncionarioJornada funcionarioJornada) throws ServiceException {
        if (Objects.nonNull(funcionarioJornada.getFuncionario().getDataAdmissao())) {
            if (funcionarioJornada.getDataInicio().before(funcionarioJornada.getFuncionario().getDataAdmissao())) {
                throw new ServiceException(this.getTopPontoResponse().alertaValidacaoComGenero(MSG.FUNCIONARIO.ALERTA_DATA_INICIAL_ANTERIOR_ADMISSAO.getResource(), MSG.FUNCIONARIO_JORNADA.ALERTA_GENERO_ENTIDADE.getResource()));
            }
        }
    }

    /**
     * verifica se a data de início é posterior a a data de demissão do
     * funcionário
     */
    private void validarDataDemissao(FuncionarioJornada funcionarioJornada) throws ServiceException {
        if (Objects.nonNull(funcionarioJornada.getFuncionario().getDataDemissao())) {
            if (funcionarioJornada.getDataInicio().after(funcionarioJornada.getFuncionario().getDataDemissao())) {
                throw new ServiceException(this.getTopPontoResponse().alertaValidacaoComGenero(MSG.FUNCIONARIO.ALERTA_DATA_INICIAL_POSTERIOR_DEMISSAO.getResource(), MSG.FUNCIONARIO_JORNADA.ALERTA_GENERO_ENTIDADE.getResource()));
            }
        }
    }

    private FuncionarioJornada validarUltimoRegistro(FuncionarioJornada funcionarioJornada) throws ServiceException {

        try {
            List<FuncionarioJornada> jornadaLista = buscarPorFuncionario(funcionarioJornada.getFuncionario());
            FuncionarioJornada fj = jornadaLista.stream()
                    .filter(f -> f.getExcecaoJornada() == null || f.getExcecaoJornada() == false)
                    .sorted(Comparator.comparing(FuncionarioJornada::getDataInicio).reversed()).findFirst().get();

            if (fj != null && fj.getDataFim() != null) {
                fj.setDataFim(null);
                this.funcionarioJornadaDao.save(fj);
                if (fj.getIdJornadaFuncionario().equals(funcionarioJornada.getIdJornadaFuncionario())) {
                    funcionarioJornada.setDataFim(null);
                }

            } else if (fj != null && fj.getIdJornadaFuncionario().equals(funcionarioJornada.getIdJornadaFuncionario())) {

                FuncionarioJornada fj2 = jornadaLista.stream()
                        .filter(r -> r.getDataFim() != null)
                        .filter(f -> f.getExcecaoJornada() == null || f.getExcecaoJornada() == false)
                        .sorted(Comparator.comparing(FuncionarioJornada::getDataInicio).reversed())
                        .findFirst().get();
                fj2.setDataFim(null);
                this.funcionarioJornadaDao.save(fj2);

            }
        } catch (NoSuchElementException e) {
        } catch (DaoException ex) {
            Logger.getLogger(FuncionarioJornadaService.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return funcionarioJornada;
    }

    /**
     * Valida as jornadas dentro do periodo da atual
     *
     * @param funcionarioJornada
     * @throws ServiceException
     */
    private void validarJornadaEmVigencia(FuncionarioJornada funcionarioJornada) throws ServiceException {

        try {
            //lista todas as que iniciam antes , ordena pela data de inicio e inverte a ordem
            List<FuncionarioJornada> jornadasAnterior = funcionarioJornadaDao.buscarAnterior(funcionarioJornada);
            jornadasAnterior = jornadasAnterior.stream().filter(f -> !Objects.equals(f.getIdJornadaFuncionario(), funcionarioJornada.getIdJornadaFuncionario())).collect(Collectors.toList());

            if (jornadasAnterior.iterator().hasNext()) {

                FuncionarioJornada jornadaAnterior = jornadasAnterior.iterator().next();
                //salva a joranafuncionario com a data de fim com um dia a menos que a proxima jornada
                jornadaAnterior.setDataFim(Utils.addDias(funcionarioJornada.getDataInicio(), -1));
                Jornada jornada = jornadaService.buscar(Jornada.class, jornadaAnterior.getJornada().getIdJornada());
                jornadaAnterior.setJornada(jornada);
                this.funcionarioJornadaDao.save(jornadaAnterior);

            }
            //lista todas as que iniciam depois e ordena pela data de inicio
            List<FuncionarioJornada> jornadasPosterior = funcionarioJornadaDao.buscarPosterior(funcionarioJornada);
            jornadasPosterior = jornadasPosterior.stream().filter(f -> !Objects.equals(f.getIdJornadaFuncionario(), funcionarioJornada.getIdJornadaFuncionario())).collect(Collectors.toList());

            if (jornadasPosterior.iterator().hasNext()) {
                FuncionarioJornada jornadaPosterior = jornadasPosterior.iterator().next();
                //salva a joranafuncionario com a data de fim com um dia a menos que a proxima jornada
                funcionarioJornada.setDataFim(Utils.addDias(jornadaPosterior.getDataInicio(), -1));
            }
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroSalvar(new FuncionarioJornada().toString()), ex);
        } catch (NoSuchElementException e) {
        }
    }

    /**
     * Valida as exceções de jornadas que são anterior a data da atual
     *
     * @param funcionarioJornada
     * @throws ServiceException
     */
    private void validarExcecoesJornadasAnterior(FuncionarioJornada funcionarioJornada) throws ServiceException {

        //lista todas as que iniciam antes , ordena pela data de inicio e inverte a ordem
        List<FuncionarioJornada> jornadaLista = buscarPorFuncionario(funcionarioJornada.getFuncionario());
        List<FuncionarioJornada> anterior = jornadaLista
                .stream()
                .filter((FuncionarioJornada fj1)
                        -> Objects.nonNull(fj1.getDataFim())
                        && fj1.getDataInicio().before(funcionarioJornada.getDataInicio())
                        && Objects.equals(fj1.getExcecaoJornada(), Boolean.TRUE))
                .sorted(Comparator.comparing(FuncionarioJornada::getDataInicio).reversed())
                .collect(Collectors.toList());

        //filtra somente a anterior que terminar depois do inicio da que esta em validação
        List<FuncionarioJornada> atr = anterior
                .stream()
                .filter(at1 -> at1.getDataFim().after(funcionarioJornada.getDataInicio()))
                .sorted(Comparator.comparing(FuncionarioJornada::getDataInicio).reversed())
                .collect(Collectors.toList());
        //caso exista um exceção para este periodo
        if (atr.iterator().hasNext()) {
            throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.FUNCIONARIO_JORNADA.ALERTA_EXCECAO_JA_CADASTRADA.getResource()));
        }
    }

    /**
     * valida as exceções de jornadas que estão dentro do mesmo período
     *
     * @param funcionarioJornada
     * @throws ServiceException
     */
    private void validarExcecoesJornadasDentroPeriodo(FuncionarioJornada funcionarioJornada) throws ServiceException {
        List<FuncionarioJornada> jornadaLista = buscarPorFuncionario(funcionarioJornada.getFuncionario());

        //Lista todas as jornadas que estão dentro do periodo da jornada em validacao
        List<FuncionarioJornada> dentroPeriodo = jornadaLista
                .stream()
                .filter((FuncionarioJornada fj1)
                        -> Objects.nonNull(fj1.getDataFim())
                        && fj1.getDataInicio().before(funcionarioJornada.getDataFim())
                        && fj1.getDataFim().after(funcionarioJornada.getDataInicio())
                        && Objects.equals(fj1.getExcecaoJornada(), Boolean.TRUE))
                .sorted(Comparator.comparing(FuncionarioJornada::getDataInicio))
                .collect(Collectors.toList());

        if (dentroPeriodo.size() > 1) {
            throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.FUNCIONARIO_JORNADA.ALERTA_EXCECAO_JA_CADASTRADA.getResource()));
        }
    }

    /**
     * Valida se já existe uma jornada para a exceção
     */
    private void validarExisteJornada(FuncionarioJornada funcionarioJornada) throws ServiceException {
        try {
            List<FuncionarioJornada> jornadaLista = buscarPorFuncionario(funcionarioJornada.getFuncionario());
            jornadaLista
                    .stream()
                    .filter(fj -> {
                        return (fj.getDataInicio().before(funcionarioJornada.getDataInicio())
                                && funcionarioJornada.getDataFim() != null && funcionarioJornada.getDataFim().after(fj.getDataInicio()))
                                || fj.getDataInicio().compareTo(funcionarioJornada.getDataInicio()) == 0
                                && !fj.getIdJornadaFuncionario().equals(fj.getIdJornadaFuncionario());
                    }).findAny().get();

        } catch (NoSuchElementException e) {
            throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.FUNCIONARIO_JORNADA.ALERTA_EXCECAO_JORNADA_NAO_CADASTRADA.getResource()));
        }
    }

    /**
     * Validas as exceções de jornadas
     *
     * @param funcionarioJornada
     * @throws ServiceException
     */
    private void validarExcecoesJornadasPosterior(FuncionarioJornada funcionarioJornada) throws ServiceException {

        List<FuncionarioJornada> jornadaLista = buscarPorFuncionario(funcionarioJornada.getFuncionario());
        //Lista todas as posterior a data de fim da que esta em validação
        List<FuncionarioJornada> posterior = jornadaLista
                .stream()
                .filter((FuncionarioJornada fj1)
                        -> Objects.nonNull(fj1.getDataFim())
                        && fj1.getDataInicio().after(funcionarioJornada.getDataFim())
                        && Objects.equals(fj1.getExcecaoJornada(), Boolean.TRUE))
                .sorted(Comparator.comparing(FuncionarioJornada::getDataInicio))
                .collect(Collectors.toList());
        //filtra somente a que inicia antes do data de fim da que esta em validação
        List<FuncionarioJornada> dpr = posterior
                .stream()
                .filter(dpr1 -> dpr1.getDataInicio().before(funcionarioJornada.getDataFim()))
                .collect(Collectors.toList());

        if (dpr.iterator().hasNext()) {
            throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.FUNCIONARIO_JORNADA.ALERTA_EXCECAO_JA_CADASTRADA.getResource()));

        }
    }

    /**
     * Valida se está sendo modificado em relação ao momento em que foi
     * cadastrado como coletivo
     *
     * @param funcionarioJornada
     * @throws ServiceException
     */
    private void validarAlteracaoColetivo(FuncionarioJornada funcionarioJornada) throws ServiceException {
        FuncionarioJornada fc = buscar(FuncionarioJornada.class, funcionarioJornada.getIdJornadaFuncionario());
        if (Objects.equals(funcionarioJornada.getIdJornadaFuncionario(), fc.getIdJornadaFuncionario())
                && (!Objects.equals(funcionarioJornada.getDataFim(), fc.getDataFim())
                || !Objects.equals(funcionarioJornada.getDataInicio(), fc.getDataInicio())
                || !Objects.equals(funcionarioJornada.getExcecaoJornada(), fc.getExcecaoJornada())
                || !Objects.equals(funcionarioJornada.getJornada(), fc.getJornada())
                || !Objects.equals(funcionarioJornada.getSequenciaInicial(), fc.getSequenciaInicial()))
                && Objects.equals(funcionarioJornada.getManual(), Boolean.TRUE)) {
            funcionarioJornada.setColetivo(null);
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="COLETIVO">
    /**
     * Método para exclusão de uma lista de afastamento, este método é utilizado
     * pelo lançamento coletivo
     *
     * @since 13/01/2013
     * @param funcionarioJornadaList
     * @param coletivo
     * @return
     * @throws ServiceException
     */
    public List<ColetivoResultado> removerColetivo(List<FuncionarioJornada> funcionarioJornadaList, Coletivo coletivo) throws ServiceException {
        List<ColetivoResultado> resultadosColetivo = new ArrayList<>();
        funcionarioJornadaList.stream().forEach(f -> {
            try {
                if (f.getColetivo() != null) {
                    f.setColetivo(null);
                    f = (FuncionarioJornada) this.funcionarioJornadaDao.save(f);

                }
                Response r = excluir(FuncionarioJornada.class, f.getIdJornadaFuncionario());

                //Preciso do valor de volta
                f.setColetivo(coletivo);
                resultadosColetivo.add(new ColetivoResultado((Entidade) f, Utils.extractMsgReturn(r), ColetivoResultado.Enum_ResultadoColetivo.EXCLUIDO));
            } catch (ServiceException ex) {
                f.setColetivo(coletivo);
                resultadosColetivo.add(new ColetivoResultado((Entidade) f, Utils.extractMsgReturn(ex.getResponse()), ColetivoResultado.Enum_ResultadoColetivo.ERRO));
            } catch (DaoException ex) {
                f.setColetivo(coletivo);
                resultadosColetivo.add(new ColetivoResultado((Entidade) f, new MsgRetorno(ex.getMessage(), MSG.TIPOMSG.ALERTA), ColetivoResultado.Enum_ResultadoColetivo.ERRO));
            }
        });
        return resultadosColetivo;
    }

    /**
     * Método para remover o coletivo de uma lista de calendario
     *
     * @since 13/01/2013
     * @param funcionarioJornadaList
     * @throws ServiceException
     */
    public void removerColetivo(List<FuncionarioJornada> funcionarioJornadaList) throws ServiceException {
        if (funcionarioJornadaList != null) {
            funcionarioJornadaList.stream().map(a -> a.getColetivo()).forEach(ac -> {
                try {
                    this.coletivoService.excluirColetivoSemVinculo(ac);

                } catch (ServiceException ex) {
                    Logger.getLogger(AfastamentoService.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            });
        }
    }

    public Collection<? extends ColetivoResultado> salvarFuncionarioJornadaColetivo(List<FuncionarioJornada> funcionarioJornadaList, ProgressoTransfer progresso) throws ServiceException {
        List<ColetivoResultado> resultadosColetivo = new ArrayList<>();
        List<FuncionarioJornada> listaRemover = new ArrayList<>();
        funcionarioJornadaList.stream().forEach((a) -> {
            try {
                Response r = a.getIdJornadaFuncionario() == null ? this.salvar(a) : this.atualizar(a);
                resultadosColetivo.add(new ColetivoResultado((Entidade) a, Utils.extractMsgReturn(r), ColetivoResultado.Enum_ResultadoColetivo.LANCADO));
            } catch (ServiceException se) {
                boolean fechamento = se instanceof FechamentoException;
                resultadosColetivo.add(new ColetivoResultado((Entidade) a, Utils.extractMsgReturn(se.getResponse()), ColetivoResultado.Enum_ResultadoColetivo.ERRO, fechamento));
                try {
                    if (!fechamento) {
                        listaRemover.add((FuncionarioJornada) a.clone());

                    }
                } catch (CloneNotSupportedException ex) {
                    Logger.getLogger(FuncionarioJornadaService.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            } finally {
                progresso.addProgresso(1);
            }
        });

        //Tenta remover os que deram erros, pois esse é o procedimento do coletivo
        if (!listaRemover.isEmpty()) {
            listaRemover.stream().forEach(entidade -> {
                try {
                    if (entidade.getIdJornadaFuncionario() != null) {
                        this.excluirEntidade(entidade);

                    }
                } catch (ServiceException ex) {
                    Logger.getLogger(AfastamentoService.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            });
        }

        return resultadosColetivo;
    }

    public Response excluirEntidade(FuncionarioJornada entidade) throws ServiceException {
        this.funcionarioJornadaDao.excluirEntidade(entidade);
        this.coletivoService.excluirColetivoSemVinculo(entidade.getColetivo());
        this.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_FUNCIONARIO_JORNADAS, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.EXCLUIR, entidade);

        return this.getTopPontoResponse().sucessoExcluir(entidade.toString());
    }
//</editor-fold>

}
