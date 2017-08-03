package com.topdata.toppontoweb.services.funcionario;

//<editor-fold defaultstate="collapsed" desc="IMPORTS">
import com.topdata.toppontoweb.services.funcionario.calendario.FuncionarioCalendarioService;
import com.topdata.toppontoweb.services.funcionario.departamento.FuncionarioDepartamentoService;
import com.topdata.toppontoweb.services.funcionario.cargo.FuncionarioCargoService;
import com.topdata.toppontoweb.services.funcionario.compensacao.CompensacaoService;
import com.topdata.toppontoweb.services.funcionario.cartao.CartaoService;
import com.topdata.toppontoweb.services.funcionario.afastamento.AfastamentoService;
import com.topdata.toppontoweb.services.funcionario.empresa.FuncionarioEmpresaService;
import com.topdata.toppontoweb.services.funcionario.jornada.FuncionarioJornadaService;
import com.topdata.toppontoweb.services.funcionario.bancohoras.FuncionarioBancoHorasService;
import com.sun.jersey.core.header.FormDataContentDisposition;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.dao.funcionario.FuncionarioDao;
import com.topdata.toppontoweb.dto.datatable.PaginacaoDataTableRetornoTransfer;
import com.topdata.toppontoweb.dto.PaginacaoTransfer;
import com.topdata.toppontoweb.dto.datatable.Column;
import com.topdata.toppontoweb.dto.datatable.Order;
import com.topdata.toppontoweb.dto.datatable.PaginacaoDataTableTransfer;
import com.topdata.toppontoweb.dto.funcionario.FuncionarioPaginacaoTransfer;
import com.topdata.toppontoweb.dto.funcionario.FuncionarioTransfer;
import com.topdata.toppontoweb.entity.autenticacao.Operador;
import com.topdata.toppontoweb.entity.autenticacao.Planos;
import com.topdata.toppontoweb.entity.bancohoras.BancoHoras;
import com.topdata.toppontoweb.entity.calendario.Calendario;
import com.topdata.toppontoweb.entity.empresa.Cei;
import com.topdata.toppontoweb.entity.empresa.Departamento;
import com.topdata.toppontoweb.entity.empresa.Empresa;
import com.topdata.toppontoweb.entity.fechamento.EmpresaFechamentoPeriodo;
import com.topdata.toppontoweb.entity.funcionario.Afastamento;
import com.topdata.toppontoweb.entity.funcionario.Cargo;
import com.topdata.toppontoweb.entity.funcionario.Cartao;
import com.topdata.toppontoweb.entity.funcionario.Compensacao;
import com.topdata.toppontoweb.entity.funcionario.Funcionario;
import com.topdata.toppontoweb.entity.funcionario.FuncionarioBancoHoras;
import com.topdata.toppontoweb.entity.funcionario.FuncionarioCalendario;
import com.topdata.toppontoweb.entity.funcionario.FuncionarioDepartamento;
import com.topdata.toppontoweb.entity.funcionario.FuncionarioEmpresa;
import com.topdata.toppontoweb.entity.funcionario.FuncionarioJornada;
import com.topdata.toppontoweb.entity.funcionario.Funcionario_;
import com.topdata.toppontoweb.entity.funcionario.HistoricoFuncionarioCargo;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoService;
import com.topdata.toppontoweb.services.empresa.DepartamentoService;
import com.topdata.toppontoweb.services.empresa.EmpresaService;
import com.topdata.toppontoweb.services.permissoes.PlanoService;
import com.topdata.toppontoweb.services.ValidacoesCadastro;
import com.topdata.toppontoweb.services.funcionario.cargo.CargoService;
import com.topdata.toppontoweb.services.jornada.JornadaService;
import com.topdata.toppontoweb.services.marcacoes.MarcacoesService;
import com.topdata.toppontoweb.utils.DateHelper;
import com.topdata.toppontoweb.utils.PISValidator;
import com.topdata.toppontoweb.utils.UploadFileService;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES.Enum_FUNCIONALIDADE;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES.Enum_OPERACAO;
import com.topdata.toppontoweb.utils.constantes.MSG;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.NoSuchElementException;
//</editor-fold>

/**
 * @version 1.0.0.0 data 18/01/2017
 * @since 1.0.0.0 data 03/05/2016
 *
 * @author juliano.ezequiel
 */
@Service
public class FuncionarioService extends TopPontoService<Funcionario, Object> implements ValidacoesCadastro<Funcionario, Object> {

    //<editor-fold defaultstate="collapsed" desc="CDI">
    @Autowired
    private FuncionarioDao funcionarioDao;

    @Autowired
    private DepartamentoService departamentoService;

    @Autowired
    private FuncionarioCargoService funcionarioCargoService;

    @Autowired
    private FuncionarioDepartamentoService funcionarioDepartamentoService;

    @Autowired
    private FuncionarioEmpresaService funcionarioEmpresaService;

    @Autowired
    private PlanoService planoService;

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private UploadFileService uploadFileService;

    @Autowired
    private PISValidator pISValidator;

    @Autowired
    private AfastamentoService afastamentoService;

    @Autowired
    private FuncionarioCalendarioService funcionarioCalendarioService;

    @Autowired
    private FuncionarioBancoHorasService funcionarioBancoHorasService;

    @Autowired
    private CompensacaoService compensacaoService;

    @Autowired
    private CartaoService cartaoService;

    @Autowired
    private JornadaService jornadaService;

    @Autowired
    private FuncionarioJornadaService funcionarioJornadaService;

    @Autowired
    private MarcacoesService marcacoesService;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="VARIÁVEIS">
    private HashMap<String, Object> map;
    private Response hasError = null;
    private Funcionario funcionario = null;
    private Funcionario funcionarioBase = null;
//</editor-fold>

    //<editor-fold defaultstate="" desc="CONSULTAS">
    public List buscarPorEmpresaPossuiCartao(Empresa empresa) throws ServiceException {
        try {
            return this.funcionarioDao.buscarPorEmpresaPossuiCartao(empresa);
        } catch (DaoException ex) {
            throw new ServiceException(ex);
        }
    }

    public List<Funcionario> buscarPorEmpresaPeriodoMarcacoesInvalidas(Empresa empresa, Date dataInicio, Date dataFim) {
        return this.funcionarioDao.buscarPorEmpresaPeriodoMarcacoesInvalidas(empresa, dataInicio, dataFim);
    }

    public synchronized List<Funcionario> buscarPorEmpresa(Empresa empresa) throws ServiceException {
        try {
            return this.funcionarioDao.buscarPorEmpresa(empresa);
        } catch (DaoException ex) {
            throw new ServiceException(ex);
        }
    }

    /**
     * Lista todos os funcionarios e adiciona a entidade funcionário completa a
     * lista para realizar o retorno via REST JSON
     *
     * @return
     * @throws ServiceException
     */
    public synchronized List<Funcionario> buscarTodos() throws ServiceException {
        try {
            List<Funcionario> funcionariosList = funcionarioDao.findAll(Funcionario.class);
            Arrays.toString(funcionariosList.toArray());
            funcionariosList.forEach(func -> {
                try {
                    func = buscar(null, func);
                } catch (ServiceException ex) {
                    hasError = ex.getResponse();
                }
            });

            if (hasError != null) {
                throw new ServiceException(hasError);
            }
            return funcionariosList;
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erro(MSG.CADASTRO.ERRO_BUSCAR.getResource(), new Funcionario().toString()), ex);
        }
    }

    public synchronized List<Funcionario> buscarTodosAtivos() throws ServiceException {
        return this.buscarTodos().stream()
                .filter((Funcionario func) -> Objects.equals(func.getAtivo(), Boolean.TRUE))
                .collect(Collectors.toList());
    }

    /**
     * Busca o funcionário pelo seu PIS
     *
     * @param funcionario
     * @return
     * @throws ServiceException
     */
    public synchronized List<Funcionario> buscaPorPisEmpresa(Funcionario funcionario) throws ServiceException {
        try {
            map = new HashMap<>();
            map.put(Funcionario_.pis.getName(), funcionario.getPis());
            if (funcionario.getDepartamento() != null) {
                return funcionarioDao.findbyAttributes(map, Funcionario.class)
                        .stream()
                        .filter(f -> Objects.nonNull(f.getDepartamento())
                                && f.getDepartamento().getEmpresa().equals(funcionario.getDepartamento().getEmpresa()))
                        .collect(Collectors.toList());
            } else {
                return new ArrayList<>();
            }

        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erro(MSG.CADASTRO.ERRO_BUSCAR, Funcionario.class), ex);
        }

    }

    public synchronized Funcionario buscarPorPisEmpresa(String pis, Empresa empresa) throws ServiceException {
        try {
            return funcionarioDao.findByPisEmpresa(pis, empresa);

        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erro(MSG.CADASTRO.ERRO_BUSCAR, Funcionario.class), ex);
        }
    }

    /**
     * Busca o funcionário por sua matrícula
     *
     * @param matricula
     * @return
     * @throws ServiceException
     */
    public synchronized List<Funcionario> buscaPorMatricula(String matricula) throws ServiceException {
        try {
            map = new HashMap<>();
            map.put(Funcionario_.matricula.getName(), matricula);
            return funcionarioDao.findbyAttributes(map, Funcionario.class);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erro(MSG.CADASTRO.ERRO_BUSCAR, Funcionario.class), ex);
        }
    }

    /**
     * Lista todos os funcionário que não possuem operador vinculado
     *
     * @return
     * @throws ServiceException
     */
    public synchronized List<Funcionario> buscaSemOperador() throws ServiceException {
        return funcionarioDao.buscarSemOperador();
    }

    /**
     * Bussc ao funcionário pelo id do operador
     *
     * @param id
     * @return
     * @throws ServiceException
     */
    public synchronized Funcionario buscarPorOperador(Integer id) throws ServiceException {
        try {
            map = new HashMap<>();
            map.put(Funcionario_.idOperador.getName(), new Operador(id));
            List<Funcionario> list = funcionarioDao.findbyAttributes(map, Funcionario.class);
            return list.isEmpty() ? null : list.get(0);
        } catch (DaoException ex) {
            return null;
        }
    }

    /**
     * Busca os funcionario pelo grupo de permissão
     *
     * @param id
     * @return
     * @throws ServiceException
     */
    public synchronized List<Funcionario> buscarPorPermissao(Integer id) throws ServiceException {
        //lista as empresa que o grupo de acesso possui permissao
        List<Departamento> departamentoList = departamentoService.buscarPorPermissao(id);

        List<Funcionario> funcionarioList = new ArrayList<>();
        //se existir permissoes por departamento
        if (departamentoList.size() > 0) {

            //lista todos os funcionario vinculados as empresa
            departamentoList.stream().forEach(d -> {
                d.getFuncionarioDepartamentoList().stream().forEach(f -> {
                    funcionarioList.add(f.getFuncionario());
                });
            });
            //adiciona os funcionarios a lista
            funcionarioList.stream().forEach(new FuncionarioConsummer());

        } else {

            List<Empresa> empresaList = empresaService.buscaPorGrupoPermissaoAtivas(id);
            //filtra os funcionario 
            empresaList.stream().forEach(e -> {
                e.getFuncionarioEmpresaList().stream().forEach(fe -> {
                    funcionarioList.add(fe.getFuncionario());
                });
            });
            //adiciona os funcionarios a lista
            funcionarioList.stream().forEach(new FuncionarioConsummer());
        }
        //veriica se ocorreu algum erro
        if (hasError != null) {
            throw new ServiceException(hasError);
        }

        return funcionarioList.stream().distinct().collect(Collectors.toList());
    }

    /**
     * Consulta os funcionário existente no departamento
     *
     * @param request
     * @param id
     * @param somenteAtivos
     * @return
     * @throws ServiceException
     */
    public synchronized List<Funcionario> buscarPorDepartamento(HttpServletRequest request, Integer id, Boolean somenteAtivos) throws ServiceException {
//        Operador o = operadorService.getOperadorDaSessao(request);
        Departamento dept = departamentoService.buscar(Departamento.class, id);
        if (dept != null) {
            return dept.getFuncionarioList().stream()
                    .filter(func -> (!somenteAtivos || Objects.equals(func.getAtivo(), Boolean.TRUE)) && func.getDataDemissao() == null)
                    .collect(Collectors.toList());
        } else {
            throw new ServiceException(this.getTopPontoResponse().alertaNaoCad());
        }
    }

    /**
     * Consulta os funcionário existente em uma lista filtrando pelo
     * departamento
     *
     * @param funcionarioList
     * @param departamento
     * @return
     * @throws ServiceException
     */
    public synchronized List<Funcionario> buscarFuncionarioDepartamento(List<Funcionario> funcionarioList, Departamento departamento) throws ServiceException {
        try {
            List<Funcionario> funcionarioResult = funcionarioDao.findByFuncionarioDepartamento(funcionarioList, departamento);

            funcionarioResult.forEach((f) -> {
                try {
                    f.setFuncionarioJornadaList(this.funcionarioJornadaService.buscarPorFuncionario(f.getIdFuncionario(), true));
                } catch (ServiceException ex) {
                    Logger.getLogger(FuncionarioService.class.getName()).log(Level.SEVERE, null, ex);
                }
            });

            return funcionarioResult;
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroBuscar(), ex);
        }
    }

    /**
     * Consulta os funcionário que possuam vinculo com o Cei
     *
     * @param cei
     * @return
     * @throws ServiceException
     */
    public synchronized List<Funcionario> buscarPorCei(Cei cei) throws ServiceException {
        if (cei != null) {

            return funcionarioDao.buscarPorCei(cei);

        } else {
            throw new ServiceException(this.getTopPontoResponse().alertaNaoCad());
        }
    }

    public synchronized List<Funcionario> buscarPorAtributos(HashMap<String, Object> map) throws ServiceException {
        try {
            return this.getDao().findbyAttributes(map, Funcionario.class);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroBuscar(), ex);
        }
    }

    public List<Funcionario> buscarPorCalendario(Calendario calendario) throws ServiceException {
        try {
            return this.funcionarioDao.buscarPorCalendario(calendario);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public List<Funcionario> buscarPorBancoHoras(BancoHoras bancoHoras) throws ServiceException {
        try {
            return this.funcionarioDao.buscarPorBancoHoras(bancoHoras);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="CRUD">
    @Override
    public Funcionario buscar(Class<Funcionario> entidade, Object id) throws ServiceException {
        try {
            if (id instanceof Funcionario) {
                funcionario = (Funcionario) id;
            } else {
                funcionario = funcionarioDao.find(entidade, id);
            }

            if (funcionario == null) {
                throw new ServiceException(this.getTopPontoResponse().alertaNaoCad(new Funcionario().toString()));
            }

            return funcionario;

        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erro(MSG.CADASTRO.ERRO_BUSCAR.getResource(), new Funcionario().toString()), ex);
        }
    }

    @Override
    public Response salvar(Funcionario funcionario) throws ServiceException {
        try {

            //salva na base o funcionario e recebe o id gerado pela base
            funcionario.setIdFuncionario(funcionarioDao.save(validarSalvar(funcionario)).getIdFuncionario());

            //somente se existe um cargo para salvar
            if (funcionario.getCargo() != null) {
                funcionarioCargoService.salvar(new HistoricoFuncionarioCargo(funcionario));
            }
            //salva na base o departamento para este funcionario
            funcionarioDepartamentoService.salvar(new FuncionarioDepartamento(funcionario));

            //salva na base a empresa para este funcionario
            funcionarioEmpresaService.salvar(new FuncionarioEmpresa(funcionario));

            this.getAuditoriaServices().auditar(Enum_FUNCIONALIDADE.CAD_FUNCIONARIO_FUNCIONARIO, CONSTANTES.Enum_AUDITORIA.DEFAULT, Enum_OPERACAO.INCLUIR, funcionario);

            this.marcacoesService.atualizarMarcacoesInvalidasPorPis(funcionario);

            return this.getTopPontoResponse().sucessoSalvar(funcionario.toString(), funcionario);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroSalvar(new Funcionario().toString()), ex);
        }
    }

    @Override
    public Response atualizar(Funcionario funcionario) throws ServiceException {
        try {

            //salva na base o funcionario e recebe o id gerado pela base
            Funcionario funcionarioAtualizado = funcionarioDao.save(validarAtualizar(funcionario));

            //somente se existe um cargo para salvar o histórico
            if (funcionario.getCargo() != null) {
                funcionarioCargoService.atualizar(new HistoricoFuncionarioCargo(funcionario));
            }
            if (funcionario.getDepartamento() != null) {
                //salva na base o departamento para este funcionario
                funcionarioDepartamentoService.atualizar(new FuncionarioDepartamento(funcionario));
                //salva na base a empresa para este funcionario
                funcionarioEmpresaService.atualizar(new FuncionarioEmpresa(funcionario));
            }

            this.getAuditoriaServices().auditar(Enum_FUNCIONALIDADE.CAD_FUNCIONARIO_FUNCIONARIO, CONSTANTES.Enum_AUDITORIA.DEFAULT, Enum_OPERACAO.EDITAR, funcionarioAtualizado);
            
            this.marcacoesService.atualizarMarcacoesInvalidasPorPis(funcionario);
            
            return this.getTopPontoResponse().sucessoAtualizar(funcionario.toString(), funcionarioAtualizado);

        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroAtualizar(new Funcionario().toString()), ex);
        }
    }

    @Override
    public Response excluir(Class<Funcionario> c, Object id) throws ServiceException {
        try {
            validarExcluir(new Funcionario((Integer) id));
            Funcionario f = buscar(Funcionario.class, (Integer) id);
            this.funcionarioDao.delete(f);
            //método adicionado para remover um lançamento coletivo que não esta mais sendo utilizado
            this.validarExcluxaoColetivos(funcionario);
            this.getAuditoriaServices().auditar(Enum_FUNCIONALIDADE.CAD_FUNCIONARIO, CONSTANTES.Enum_AUDITORIA.DEFAULT, Enum_OPERACAO.EXCLUIR, f);
            return this.getTopPontoResponse().sucessoExcluir(this.funcionario.toString());
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroExcluir(new Funcionario().toString()), ex);
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="VALIDAÇÕES OBRIGATÓRIAS">
    @Override
    public Funcionario validarSalvar(Funcionario funcionario) throws ServiceException {
        ValidarCamposObrigatorio(funcionario);
        pISValidator.validar(null, funcionario.getPis());
        validarJaCadastrado(funcionario);
        validarLimiteFuncionarioPlano(funcionario);
        return funcionario;
    }

    @Override
    public Funcionario validarExcluir(Funcionario funcionario) throws ServiceException {
        Funcionario f = buscar(Funcionario.class, funcionario.getIdFuncionario());
        if (f == null) {
            throw new ServiceException(this.getTopPontoResponse().alertaNaoCad(new Funcionario().toString()));
        }
        validarPossuiFechamento(f);
        //verifica se o funcionário possúi um operador vinculado que pode ser excluido
        if (f.getIdOperador() != null) {
            throw new ServiceException(this.getTopPontoResponse().alerta(MSG.FUNCIONARIO.ALERTA_FUNCIONARIO_VINCULO_OPERADOR.getResource(), f.toString()));
        }
        return funcionario;
    }

    @Override
    public Funcionario validarAtualizar(Funcionario funcionario) throws ServiceException {

        validarIdentificador(funcionario);
        ValidarCamposObrigatorio(funcionario);
        validarJaCadastrado(funcionario);
        validarAtualizarValoresEntreEntidades(funcionario);
        validarPossuiFechamento(funcionario);
        validarAlteracaoAdmissaoDemissaoAfastamento(funcionario);
        validarAlteracaoAdmissaoDemissaoBancoHorasManutencao(funcionario);
        validarAlteracaoAdmissaoDemissaoCalendario(funcionario);
        validarAlteracaoAdmissaoDemissaoCartao(funcionario);
        validarAlteracaoAdmissaoDemissaoCompensacao(funcionario);
        validarAlteracaoAdmissaoDemissaoJornada(funcionario);

        pISValidator.validar(null, funcionario.getPis());

        return funcionario;
    }
//</editor-fold>
    private ServiceException erro = null;

    //<editor-fold defaultstate="collapsed" desc="REGRAS DE VALIDAÇÕES">
    public synchronized void validarPossuiFechamento(Funcionario funcionario) throws ServiceException {

        LOGGER.debug("validando funcionário possui empresa com fechamento");
        Empresa empresa = this.empresaService.buscarPorFuncionario(funcionario);
        Funcionario funcionarioOriginal = this.buscar(Funcionario.class, funcionario.getIdFuncionario());
        this.erro = null;
        //fechamento da empresa
        List<EmpresaFechamentoPeriodo> fechamentoDaEmpresa = empresa.getEmpresaFechamentoPeriodoList();
        if (fechamentoDaEmpresa != null && !fechamentoDaEmpresa.isEmpty()) {
            //se esta alterando a data de admissao
            //a nova data de admissao deve ser menor que a data de fechamento
            if (funcionario.getDataAdmissao() != null && funcionarioOriginal.getDataAdmissao().compareTo(funcionario.getDataAdmissao()) != 0) {
                //valida se existe fechamento no periodo da data de adissão
                try {
                    this.getFechamentoServices().isPossuiFechamentoPeriodo(empresa, funcionarioOriginal.getDataAdmissao(), funcionarioOriginal.getDataAdmissao());
                    this.getFechamentoServices().isPossuiFechamentoPeriodo(empresa, funcionario.getDataAdmissao(), funcionario.getDataAdmissao());
                } catch (ServiceException ex) {
                    this.erro = ex;
                }
                //valida se existe um fechamento anterior a data de admissão
                fechamentoDaEmpresa.stream().forEach(fc -> {
                    if (this.erro == null) {
                        if (fc.getInicio().compareTo(funcionarioOriginal.getDataAdmissao()) <= 0 || fc.getTermino().compareTo(funcionario.getDataAdmissao()) <= 0) {
                            this.erro = new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.FECHAMENTO.ALERTA_EXISTE_FECHAMENTO_PERIODO.getResource()));
                        }
                    }
                });
            }
            //se esta alterando a data de demissao verificar se existe fechamento na data 
            if (funcionario.getDataDemissao() != null && funcionarioOriginal.getDataDemissao() == null
                    || funcionarioOriginal.getDataDemissao() != null && funcionario.getDataDemissao() != null && funcionarioOriginal.getDataDemissao().compareTo(funcionario.getDataDemissao()) != 0) {
                try {
                    this.getFechamentoServices().isPossuiFechamentoPeriodo(empresa, funcionario.getDataDemissao(), funcionario.getDataDemissao());
                    if (funcionarioOriginal.getDataDemissao() != null) {
                        this.getFechamentoServices().isPossuiFechamentoPeriodo(empresa, funcionarioOriginal.getDataDemissao(), funcionarioOriginal.getDataDemissao());
                    }
                } catch (ServiceException ex) {
                    this.erro = ex;
                }

                //valida se existe um fechamento anterior a data de demissão
                fechamentoDaEmpresa.stream().forEach(fc -> {
                    if (this.erro == null) {
                        if ((funcionarioOriginal.getDataDemissao() != null && fc.getInicio().compareTo(funcionarioOriginal.getDataDemissao()) <= 0)
                                || (funcionario.getDataDemissao() != null && fc.getTermino().compareTo(funcionario.getDataDemissao()) <= 0)) {
                            this.erro = new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.FECHAMENTO.ALERTA_EXISTE_FECHAMENTO_PERIODO.getResource()));
                        }
                    }
                });
            }
            //se estiver alterando a empresa, verificar se existe fechamento na empresa antiga
            if (!Objects.equals(funcionario.getDepartamento().getIdDepartamento(), funcionarioOriginal.getDepartamento().getIdDepartamento())) {
                this.erro = new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.FECHAMENTO.ALERTA_EXISTE_FECHAMENTO_PERIODO.getResource()));
            }
        }
        //se existir erro, gera a exceção
        if (this.erro != null) {
            throw this.erro;
        }
    }

    /**
     * valida se o funcionário possui um identificar válido
     *
     * @param funcionario
     * @throws com.topdata.toppontoweb.services.ServiceException
     */
    public void validarIdentificador(Funcionario funcionario) throws ServiceException {
        try {
            if ((funcionario == null || funcionario.getIdFuncionario() == null) && funcionario.getIdOperador() == null) {
                throw new ServiceException(this.getTopPontoResponse().alertaNaoCad(funcionario.toString()));
            }
            //busca pelo operador ou pelo id do funcionário
            if (funcionario.getIdFuncionario() == null) {
                map = new HashMap<>();
                map.put(Funcionario_.idOperador.getName(), funcionario.getIdOperador());
                funcionarioBase = funcionarioDao.findOnebyAttributes(map, Funcionario.class);
            } else {
                funcionarioBase = buscar(Funcionario.class, funcionario.getIdFuncionario());
            }
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroValidar(new Funcionario().toString()), ex);
        }
    }

    /**
     * Valida se o funcionário já está cadastrado, usa como referencia o PIS e o
     * id do funcionário
     *
     * @param funcionario
     * @throws com.topdata.toppontoweb.services.ServiceException
     */
    public void validarJaCadastrado(Funcionario funcionario) throws ServiceException {

        List<Funcionario> funcionarioCompar2 = buscaPorPisEmpresa(funcionario);
        if (funcionario.getIdFuncionario() == null && !funcionarioCompar2.isEmpty()) {
            throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.FUNCIONARIO.ALERTA_FUNCIONARIO_PIS_JA_CAD.getResource()));
        }

        if (!funcionarioCompar2.isEmpty()) {
            if (Objects.nonNull(funcionario.getIdFuncionario()) && !funcionario.getIdFuncionario().equals(funcionarioCompar2.get(0).getIdFuncionario())) {
                throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.FUNCIONARIO.ALERTA_FUNCIONARIO_PIS_JA_CAD.getResource()));
            }
        }
    }

    /**
     * Validar a quantidade máxima de fucionário permitida pelo plano
     *
     * @param funcionario
     * @throws com.topdata.toppontoweb.services.ServiceException
     */
    public void validarLimiteFuncionarioPlano(Funcionario funcionario) throws ServiceException {

        Planos planos = planoService.buscarPlanoValido();
        //validar por limites funcionario plano
        if (Integer.parseInt(quantidade(Funcionario.class)) > planos.getLimiteFuncionarios()) {

        }
        //validar por limite empresa funcionario plano
        if (totalFuncionarioEmpresa(funcionario.getDepartamento().getEmpresa()) + 1 >= planos.getLimiteFuncionarios()) {

        }
    }

    /**
     * Realiza a atulziação entre as entidade, para manter somente uma
     * referencia no Hibernate
     *
     * @param funcionario
     * @throws com.topdata.toppontoweb.services.ServiceException
     */
    public void validarAtualizarValoresEntreEntidades(Funcionario funcionario) throws ServiceException {

        funcionario.setAtivo(funcionario.getAtivo() != null ? funcionario.getAtivo() : funcionarioBase.getAtivo());
        funcionario.setCtps(funcionario.getCtps() != null ? funcionario.getCtps() : funcionarioBase.getCtps());
        funcionario.setDataAdmissao(funcionario.getDataAdmissao() != null ? funcionario.getDataAdmissao() : funcionarioBase.getDataAdmissao());
//            funcionario.setDataDemissao(funcionario.getDataDemissao() != null ? funcionario.getDataDemissao() : funcionarioBase.getDataDemissao());
        funcionario.setFoto(funcionario.getFoto() != null ? funcionario.getFoto() : funcionarioBase.getFoto());
        funcionario.setIdOperador(funcionario.getIdOperador() != null ? funcionario.getIdOperador() : funcionarioBase.getIdOperador());
        funcionario.setIdentificacaoExportacao(funcionario.getIdentificacaoExportacao() != null ? funcionario.getIdentificacaoExportacao() : funcionarioBase.getIdentificacaoExportacao());
        funcionario.setMatricula(funcionario.getMatricula() != null ? funcionario.getMatricula() : funcionarioBase.getMatricula());
        funcionario.setNome(funcionario.getNome() != null ? funcionario.getNome() : funcionarioBase.getNome());
        funcionario.setPis(funcionario.getPis() != null ? funcionario.getPis() : funcionarioBase.getPis());
        funcionario.setDepartamento(funcionario.getDepartamento() != null ? funcionario.getDepartamento() : funcionarioBase.getDepartamento());

        funcionario.setAfastamentoList(this.funcionarioBase.getAfastamentoList());
        funcionario.setCompensacaoList(this.funcionarioBase.getCompensacaoList());
        funcionario.setFuncionarioBancoHorasList(this.funcionarioBase.getFuncionarioBancoHorasList());
        funcionario.setFuncionarioCalendarioList(this.funcionarioBase.getFuncionarioCalendarioList());
        funcionario.setFuncionarioJornadaList(this.funcionarioBase.getFuncionarioJornadaList());
        funcionario.setCartaoList(this.funcionarioBase.getCartaoList());

    }

    private void ValidarCamposObrigatorio(Funcionario funcionario) throws ServiceException {

        if (funcionario.getNome() == null) {
            throw new ServiceException(this.getTopPontoResponse().campoObrigatorio(Funcionario_.nome.getName()));
        }
        if (funcionario.getDepartamento() == null) {
            throw new ServiceException(this.getTopPontoResponse().campoObrigatorio(Funcionario_.departamento.getName()));
        }
        if (funcionario.getPis() == null) {
            throw new ServiceException(this.getTopPontoResponse().campoObrigatorio(Funcionario_.pis.getName()));
        }
        if (funcionario.getDataAdmissao() == null) {
            throw new ServiceException(this.getTopPontoResponse().campoObrigatorio("Data de admissão"));
        }
    }

    private void validarExcluxaoColetivos(Funcionario funcionario) throws ServiceException {
        this.afastamentoService.removerColetivo(funcionario.getAfastamentoList());
        this.compensacaoService.removerColetivo(funcionario.getCompensacaoList());
        this.funcionarioBancoHorasService.removerColetivo(funcionario.getFuncionarioBancoHorasList());
        this.funcionarioCalendarioService.removerColetivo(funcionario.getFuncionarioCalendarioList());
    }

    private void validarAlteracaoAdmissaoDemissaoCartao(Funcionario funcionarioBase) throws ServiceException {
        try {
            if (funcionarioBase.getCartaoList() != null
                    && funcionarioBase.getCartaoList().stream()
                    .sorted(Comparator.comparing(Cartao::getDataInicio))
                    .reduce((a, b) -> a)
                    .get().getDataInicio().before(funcionarioBase.getDataAdmissao())) {
                throw new ServiceException(this.getTopPontoResponse().alertaValidacao(
                        MSG.FUNCIONARIO.ALERTA_DATA_CARTAO_ANTERIOR_ADMISSAO.getResource()));
            }
        } catch (NoSuchElementException e) {
        }
    }

    private void validarAlteracaoAdmissaoDemissaoJornada(Funcionario funcionario) throws ServiceException {
        try {
            if (funcionario.getFuncionarioJornadaList() != null
                    && funcionario.getFuncionarioJornadaList().stream()
                    .sorted(Comparator.comparing(FuncionarioJornada::getDataInicio))
                    .reduce((a, b) -> a)
                    .get().getDataInicio().before(funcionario.getDataAdmissao())) {
                throw new ServiceException(this.getTopPontoResponse().alertaValidacao(
                        MSG.FUNCIONARIO.ALERTA_DATA_JORNADA_ANTERIOR_ADMISSAO.getResource()));
            }
        } catch (NoSuchElementException e) {
        }
    }

    private void validarAlteracaoAdmissaoDemissaoCalendario(Funcionario funcionario) throws ServiceException {
        try {
            if (funcionario.getFuncionarioJornadaList() != null
                    && funcionario.getFuncionarioCalendarioList().stream()
                    .sorted(Comparator.comparing(FuncionarioCalendario::getDataInicio))
                    .reduce((a, b) -> a)
                    .get().getDataInicio().before(funcionario.getDataAdmissao())) {
                throw new ServiceException(this.getTopPontoResponse().alertaValidacao(
                        MSG.FUNCIONARIO.ALERTA_DATA_CALENDARIO_ANTERIOR_ADMISSAO.getResource()));
            }
        } catch (NoSuchElementException e) {
        }
    }

    private void validarAlteracaoAdmissaoDemissaoAfastamento(Funcionario funcionario) throws ServiceException {
        try {
            if (funcionario.getAfastamentoList() != null
                    && funcionario.getAfastamentoList().stream()
                    .sorted(Comparator.comparing(Afastamento::getDataInicio))
                    .reduce((a, b) -> a)
                    .get().getDataInicio().before(funcionario.getDataAdmissao())) {
                throw new ServiceException(this.getTopPontoResponse().alertaValidacao(
                        MSG.FUNCIONARIO.ALERTA_DATA_AFASTAMENTO_ANTERIOR_ADMISSAO.getResource()));
            }
        } catch (NoSuchElementException e) {
        }
    }

    private void validarAlteracaoAdmissaoDemissaoCompensacao(Funcionario funcionario) throws ServiceException {
        try {
            if (funcionario.getCompensacaoList() != null && funcionario.getCompensacaoList().stream()
                    .sorted(Comparator.comparing(Compensacao::getDataInicio))
                    .reduce((a, b) -> a)
                    .get().getDataInicio().before(funcionario.getDataAdmissao())
                    || funcionario.getCompensacaoList().stream()
                    .sorted(Comparator.comparing(Compensacao::getDataCompensada))
                    .reduce((a, b) -> a)
                    .get().getDataCompensada().before(funcionario.getDataAdmissao())) {
                throw new ServiceException(this.getTopPontoResponse().alertaValidacao(
                        MSG.FUNCIONARIO.ALERTA_DATA_COMPENSACAO_ANTERIOR_ADMISSAO.getResource()));
            }
        } catch (NoSuchElementException e) {
        }
    }

    private void validarAlteracaoAdmissaoDemissaoBancoHorasManutencao(Funcionario funcionario) throws ServiceException {
        try {
            if (funcionario.getFuncionarioBancoHorasList() != null && funcionario.getFuncionarioBancoHorasList().stream()
                    .sorted(Comparator.comparing(FuncionarioBancoHoras::getDataInicio))
                    .reduce((a, b) -> a)
                    .get().getDataInicio().before(funcionario.getDataAdmissao())) {
                throw new ServiceException(this.getTopPontoResponse().alertaValidacao(
                        MSG.FUNCIONARIO.ALERTA_DATA_BHMANUTENCAO_ANTERIOR_ADMISSAO.getResource()));
            }
        } catch (NoSuchElementException e) {
        }
    }

    private void validarAlteracaoAdmissaoDemissaoBancoHorasFechamento(Funcionario funcionario) throws ServiceException {
        try {
            if (funcionario.getFuncionarioBancoHorasList() != null
                    && funcionario.getFuncionarioBancoHorasList().stream()
                    .sorted(Comparator.comparing(FuncionarioBancoHoras::getDataInicio))
                    .reduce((a, b) -> a)
                    .get().getDataInicio().before(funcionario.getDataAdmissao())) {
                throw new ServiceException(this.getTopPontoResponse().alertaValidacao(
                        MSG.FUNCIONARIO.ALERTA_DATA_BHMANUTENCAO_ANTERIOR_ADMISSAO.getResource()));
            }

        } catch (NoSuchElementException e) {
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="FUNÇÕES AUXILIARES">
    /**
     * Realiza o upload da foto do funcionário
     *
     * @param request
     * @param uploadedInputStream
     * @param fileDetail
     * @param foto
     * @return
     * @throws ServiceException
     */
    public Response upload(HttpServletRequest request, InputStream uploadedInputStream, FormDataContentDisposition fileDetail, String foto) throws ServiceException {
        return uploadFileService.upload(request, uploadedInputStream, fileDetail, foto);
    }

    /**
     * Atribui o operador ao funcionário
     *
     * @param funcionario
     * @param operador
     * @throws ServiceException
     */
    public void atualizarFuncionarioOperador(Funcionario funcionario, Operador operador) throws ServiceException {
        if (Objects.nonNull(funcionario)) {
            removeOperadorFuncionario(operador);
            funcionario.setIdOperador(operador);
            atualizar(funcionario);
        } else {
            removeOperadorFuncionario(operador);
        }
    }

    /**
     * Remove o vínculo do funcionário com o operado
     *
     * @param o
     * @throws ServiceException
     */
    public void removeOperadorFuncionario(Operador o) throws ServiceException {
        try {
            map = new HashMap<>();
            map.put(Funcionario_.idOperador.getName(), o.getIdOperador());
            List<Funcionario> list = funcionarioDao.findbyAttributes(map, Funcionario.class);
            list.stream().forEach(f -> {
                try {
                    f.setIdOperador(null);
                    funcionarioDao.save(f);

                } catch (DaoException ex) {
                    Logger.getLogger(FuncionarioService.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            });

        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroExcluir(new Funcionario().toString()), ex);

        }
    }

    public Response buscarPorPaginacao(FuncionarioPaginacaoTransfer fpt) throws ServiceException {
        try {
            List<Funcionario> funcionarioList = funcionarioDao.buscarPorPaginacao(fpt);
            List<FuncionarioTransfer> funcionarioTransferList = new ArrayList<>();
            funcionarioList.stream().forEach(f -> {
                funcionarioTransferList.add(new FuncionarioTransfer(f, false));
            });

            return this.getTopPontoResponse().sucessoSalvar(funcionarioTransferList);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroExcluir(new Funcionario().toString()), ex);
        }
    }

    public PaginacaoDataTableRetornoTransfer buscarPorPaginacaoDataTable(PaginacaoDataTableTransfer transfer) throws ServiceException {
        try {
            String busca = transfer.getSearch() != null ? transfer.getSearch().getValue() : "";
            Order order = transfer.getPrimeiraOrdem();

            //Busca a coluna correta
            Column column = transfer.getColumnPorId(order.getColumn());

            List<Funcionario> funcionarioList = funcionarioDao.buscarPorPaginacao(busca, transfer.getStart(), transfer.getLength(), column.getName(), order.getDir());

            Long maxTotalFiltrados = funcionarioDao.buscarPorPaginacaoTotal(busca);
            Long maxTotal = funcionarioDao.buscarPorPaginacaoTotal();
            List<FuncionarioTransfer> funcionarioTransferList = new ArrayList<>();
            funcionarioList.stream().forEach(f -> {
                funcionarioTransferList.add(new FuncionarioTransfer(f, true));
            });
            PaginacaoDataTableRetornoTransfer retorno = new PaginacaoDataTableRetornoTransfer(funcionarioTransferList, transfer.getDraw(), maxTotalFiltrados, maxTotal);

            return retorno;
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroExcluir(new Funcionario().toString()), ex);
        }
    }

    /**
     * Validação e busca utilizada na importação de marcações de acordo com a
     * seguinte regra: ou não encontrou o cartão do funcionário na data ou se
     * não é o ultimo cartão do funcionario ou funcionário foi demitido
     *
     * @param numeroCartao
     * @param empresa
     * @param dataMarcacao
     * @return Funcionario ou null (caso não exista funcionario valido nessas
     * condições)
     * @throws com.topdata.toppontoweb.services.ServiceException
     */
    public Funcionario validarBuscarPorCartao(String numeroCartao, Empresa empresa, Date dataMarcacao) throws ServiceException {
        List<Cartao> cartaoList = cartaoService.buscarPorNumeroDataFuncionario(numeroCartao, dataMarcacao, empresa);

        if (!cartaoList.isEmpty()) {
            Cartao cartaoMaiorData = new Cartao();

            for (Cartao cartao : cartaoList) {
                if (cartaoMaiorData.getDataInicio() == null
                        || cartaoMaiorData.getDataInicio().getTime() < cartao.getDataInicio().getTime()) {
                    cartaoMaiorData = cartao;
                }
            }
            try {

                //Busca cartao
                Funcionario f = funcionarioDao.buscarPorCartao(cartaoMaiorData);

                //valida se o funcionário foi demitido
                if (f.getDataDemissao() != null && f.getDataDemissao().getTime() <= dataMarcacao.getTime()) {
                    throw new ServiceException(this.getTopPontoResponse().erro(MSG.FERRAMENTAS.FUNCIONARIO_DEMITIDO.getResource()));
                }

                //Busca o ultimo cartao do funcionario
                Cartao ultimoCartao = cartaoService.buscarAtualPorFuncinarioDataInicio(f, dataMarcacao);

                //Compara com outro cartao para ver se são iguais
                if (!Objects.equals(ultimoCartao.getIdCartao(), cartaoMaiorData.getIdCartao())) {
                    return null;//valida se não é o ultimo cartão do funcionario 
                }

                return f;
            } catch (DaoException ex) {
                throw new ServiceException(this.getTopPontoResponse().erro(MSG.CARTOES.ALERTA_PROBLEMA_AO_BUSCAR.getResource()));
            }
        } else {
            //valida não encontrou o cartão na data 
            return null;
        }
    }

    public void alterarParaDadosRetroativos(Funcionario f, Date periodoFim) throws ServiceException {
        try {
            HistoricoFuncionarioCargo historicoFuncionarioCargo = funcionarioDao.buscarHistoricoFuncionarioCargoRetroativo(f, periodoFim);
            if (historicoFuncionarioCargo != null && historicoFuncionarioCargo.getCargo() != null) {
                f.setCargo(new Cargo(historicoFuncionarioCargo.getDescricao()));
            }
        } catch (DaoException ex) {
            throw new ServiceException(ex);
        }
    }

    public boolean contains(Funcionario funcionario) {
        return this.funcionarioDao.contains(funcionario);
    }

    public void refresh(Funcionario funcionario) {
        this.funcionarioDao.refresh(funcionario);
    }

    /**
     * Implementação funcional para a busca de funcionário
     */
    private class FuncionarioConsummer implements Consumer<Funcionario> {

        @Override
        public void accept(Funcionario t) {
            try {
                t = buscar(null, t);
            } catch (ServiceException ex) {
                hasError = ex.getResponse();
            }
        }

    }

    /**
     * Valida a quantidade de funcionário existente na empresa
     *
     * @param e
     * @return
     * @throws ServiceException
     */
    private int totalFuncionarioEmpresa(Empresa e) throws ServiceException {

        List<Funcionario> l = new ArrayList<>();

        departamentoService.buscarPorEmpresa(e.getIdEmpresa(), null).forEach(d -> {
            l.addAll(d.getFuncionarioList());
        });

        return l.size();
    }
//</editor-fold>
}
