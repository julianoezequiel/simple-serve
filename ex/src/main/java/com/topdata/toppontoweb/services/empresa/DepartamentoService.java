package com.topdata.toppontoweb.services.empresa;

//<editor-fold defaultstate="collapsed" desc="IMPORTS">
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.dao.empresa.DepartamentoDao;
import com.topdata.toppontoweb.dto.EmpresaDepartamentoTransfer;
import com.topdata.toppontoweb.dto.gerafrequencia.GeraFrequenciaStatusTransfer;
import com.topdata.toppontoweb.entity.Entidade;
import com.topdata.toppontoweb.entity.autenticacao.Grupo;
import com.topdata.toppontoweb.entity.autenticacao.Operador;
import com.topdata.toppontoweb.entity.empresa.Departamento;
import com.topdata.toppontoweb.entity.empresa.Departamento_;
import com.topdata.toppontoweb.entity.empresa.Empresa;
import com.topdata.toppontoweb.entity.funcionario.Funcionario;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoService;
import com.topdata.toppontoweb.services.ValidacoesCadastro;
import com.topdata.toppontoweb.services.funcionario.FuncionarioService;
import com.topdata.toppontoweb.services.permissoes.GrupoService;
import com.topdata.toppontoweb.services.permissoes.OperadorService;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;
import com.topdata.toppontoweb.utils.constantes.MSG;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
//</editor-fold>

/**
 * Classe realiza as validações e as regra de negócio para o Departamento
 *
 * @version 1.0.0.0 data 18/01/2017
 * @since 1.0.0.0 data 18/05/2016
 * @author juliano.ezequiel
 */
@Service
public class DepartamentoService extends TopPontoService<Departamento, Object>
        implements ValidacoesCadastro<Departamento, Object> {

    //<editor-fold defaultstate="collapsed" desc="CDI">
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private EmpresaService empresaService;
    @Autowired
    private DepartamentoDao departamentoDao;
    @Autowired
    private FuncionarioService funcionarioService;
    @Autowired
    private GrupoService grupoService;
    @Autowired
    private OperadorService operadorService;
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="VARIÁVEIS">
    private HashMap<String, Object> map;
    private Departamento departamento;
//</editor-fold>
    
    public List<Departamento> buscarTodosAtivos() throws ServiceException {
        try {
            HashMap<String, Object> criterios = new HashMap<>();
            criterios.put(Departamento_.ativo.getName(), true);
            
            return departamentoDao.findbyAttributes(criterios, Departamento.class);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erro(MSG.CADASTRO.ERRO_BUSCAR, Departamento.class), ex);
        }
    }

    //<editor-fold defaultstate="collapsed" desc="CONSULTAS">
    /**
     * Realiza a busca dos Departamentos pela empresa e pelo grupo de permissão
     *
     * @param idEmpresa da Empresa
     * @param idGrupo
     * @return Lista de Departamentos
     * @throws ServiceException
     */
    public List<Departamento> buscarPorEmpresa(Integer idEmpresa, Integer idGrupo) throws ServiceException {
        if (idGrupo == null) {
            return buscarPorEmpresa(new Empresa(idEmpresa));
        }
        List<Departamento> departamentoList = buscarPorPermissao(idGrupo);
        if (departamentoList.size() <= 0) {
            return buscarPorEmpresa(new Empresa(idEmpresa));
        } else {
            return departamentoList.stream().filter(dep -> Objects.equals(dep.getEmpresa().getIdEmpresa(), idEmpresa)).distinct().collect(Collectors.toList());
        }
    }
    
    /**
     * Realiza a busca dos Departamentos (Ativos) pela empresa e pelo grupo de permissão
     *
     * @param idEmpresa da Empresa
     * @param idGrupo
     * @return Lista de Departamentos
     * @throws ServiceException
     */
    public List<Departamento> buscarPorEmpresaAtivos(Integer idEmpresa, Integer idGrupo) throws ServiceException {
        List<Departamento> departamentos = buscarPorEmpresa(idEmpresa, idGrupo);
        
        return departamentos.stream().filter(dep -> dep.getAtivo()).collect(Collectors.toList());
    }

    /**
     * Busca os departamentos pela Empresa
     *
     * @param e
     * @return
     * @throws ServiceException
     */
    public List<Departamento> buscarPorEmpresa(Empresa e) throws ServiceException {
        return this.departamentoDao.buscarPorEmpresa(e);
    }

    /**
     * Realiza a busca dos Departamentos pelo Id da empresa e pela Descrição do
     * departamento
     *
     * @param id da Empresa
     * @param descricao do departamento
     * @return Lista de Departamentos
     * @throws ServiceException
     */
    public List<Departamento> buscarPorDescricaoDepartamentoEmpresa(Integer id, String descricao)
            throws ServiceException {
        try {
            map = new HashMap<>();
            map.put(Departamento_.empresa.getName(), new Empresa(id));
            map.put(Departamento_.descricao.getName(), descricao);
            return this.getDao().findbyAttributes(map, Departamento.class);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erro(MSG.CADASTRO.ERRO_BUSCAR, Departamento.class), ex);
        }
    }

    /**
     * Lista as empresas e os departamentos que o grupo de acesso possui
     * permissão
     *
     * @param id do Grupo
     * @return String em formato JSON
     * @throws ServiceException
     */
    public String buscarEmpresaDeptTransfer(Integer id) throws ServiceException {
        try {
            List<EmpresaDepartamentoTransfer> empresaDepartamentoTransfers = new ArrayList<>();

            Grupo grupo = id != null ? ((Grupo) this.getDao().find(Grupo.class, id)) : new Grupo();

            //caso o grupo de acesso não possua departamentos vinculados, o grupo possui acesso a todas as empresas
            if (grupo.getDepartamentoList().size() <= 0) {
                this.getDao().findAll(Empresa.class).forEach(empresa -> {
                    if (((Empresa) empresa).getDepartamentoList().size() > 0) {
                        empresaDepartamentoTransfers.add(new EmpresaDepartamentoTransfer((Empresa) empresa));
                    }
                });
            } else {
                //Lista as empresas que o grupo de acesso tem permissão
                List<Empresa> empresas = grupo.getDepartamentoList().stream().map(d -> d.getEmpresa())
                        .distinct().collect(Collectors.toList());
                //Filtra as empresa ativas
                empresas.forEach(empresa -> {
//                    if (Objects.equals(empresa.getAtivo(), Boolean.TRUE)) {
                    empresaDepartamentoTransfers.add(new EmpresaDepartamentoTransfer(empresa));
//                    }
                });
            }
            //converte a classe Transfer em JSON
            return mapper.writeValueAsString(empresaDepartamentoTransfers);
        } catch (DaoException | IOException ex) {
            throw new ServiceException(this.getTopPontoResponse().erro(MSG.CADASTRO.ERRO_BUSCAR, Empresa.class), ex);
        }
    }

    public List<Departamento> buscarPorPermissao(Integer idGrupo) throws ServiceException {
        try {
            Grupo grupo = ((Grupo) this.getDao().find(Grupo.class, idGrupo));

            //retorna os departamentos que este grupo de acesso possui permissao.
            return grupo.getDepartamentoList()
                    .stream()
                    .distinct().collect(Collectors.toList());

        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroBuscar(), ex);
        }
    }

    public List<Departamento> buscarPorAtributos(HashMap<String, Object> map) throws ServiceException {
        try {
            return this.getDao().findbyAttributes(map, Departamento.class);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroBuscar(), ex);
        }
    }

    public List<Departamento> buscarPorGrupo(Grupo grupo) {
        return this.departamentoDao.buscarPorGrupo(grupo);
    }

    public List<Departamento> buscarPorFuncionarios(List<Funcionario> funcionarioList, GeraFrequenciaStatusTransfer status, Double max) throws ServiceException {
        final double percIndividual = status != null && funcionarioList.size() > 0 ? max / funcionarioList.size() : 0;
        List<Departamento> departamentoList = new ArrayList<>();
        funcionarioList.forEach((funcionario) -> {
            if (status != null) {
                status.addPercentual(percIndividual);
            }
            
            boolean achou = false;
            for (Departamento dep : departamentoList) {
                if(Objects.equals(dep.getIdDepartamento(), funcionario.getDepartamento().getIdDepartamento())){
                    achou = true;
                    dep.getFuncionarioList().add(funcionario);
                }
            }
            
            if(!achou){
                Departamento dep;
                try {
                    dep = departamentoDao.buscarPorFuncionario(funcionario);
                } catch (DaoException ex) {
                    Logger.getLogger(DepartamentoService.class.getName()).log(Level.SEVERE, null, ex);
                    dep = new Departamento();
                }
                
                dep.setFuncionarioList(new ArrayList<>());
                dep.getFuncionarioList().add(funcionario);
                departamentoList.add(dep);
            }
//                try {
////                    d.setFuncionarioList(funcionarioService.buscarFuncionarioDepartamento(funcionarioList, d));
//                    d.setFuncionarioList(new ArrayList<>());
//                    
//                    funcionarioList.stream().forEach(funcionario->{
//                        d.getFuncionarioList().add(funcionario);
//                    });
//                } catch (ServiceException ex) {
//                    Logger.getLogger(DepartamentoService.class.getName()).log(Level.SEVERE, null, ex);
//                }
        });
        return departamentoList;
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="CRUD">
    public Response salvar(HttpServletRequest request, Departamento departamento) throws ServiceException {
        try {
            Operador operador = this.operadorService.getOperadorDaSessao(request);
            departamento = validarSalvar(departamento);
            departamento = (Departamento) this.getDao().save(departamento);
            this.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_EMPRESAS_DEPARTAMENTO, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.INCLUIR, departamento);

            if (operador.getGrupo().getDepartamentoList() != null
                    && (!operador.getGrupo().getDescricao().toUpperCase().equals(CONSTANTES.GRUPO_ADMIN)
                    && !operador.getGrupo().getDescricao().toUpperCase().equals(CONSTANTES.GRUPO_ANONIMO)
                    && !operador.getGrupo().getDescricao().toUpperCase().equals(CONSTANTES.GRUPO_MASTER))) {
                operador.getGrupo().getDepartamentoList().add(departamento);
                List<Departamento> departamentoList = operador.getGrupo().getDepartamentoList()
                        .stream().distinct().collect(Collectors.toList());
                operador.getGrupo().setDepartamentoList(departamentoList);
                this.grupoService.atualizar(operador.getGrupo());
            }
            return this.getTopPontoResponse().sucessoSalvar(departamento.toString(), departamento);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroSalvar(new Departamento().toString()), ex);
        }
    }

    public Response atualizar(HttpServletRequest request, Departamento departamento) throws ServiceException {
        try {
            Operador operador = this.operadorService.getOperadorDaSessao(request);
            departamento = validarAtualizar(departamento);
            departamento = (Departamento) this.getDao().save(departamento);
            this.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_EMPRESAS_DEPARTAMENTO, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.EDITAR, departamento);
            return this.getTopPontoResponse().sucessoAtualizar(departamento.toString(), departamento);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroAtualizar(new Departamento().toString()), ex);
        }

    }

    /**
     * Exclui o departamento, é nescessário o id da empresa e a descrição do
     * departamento
     *
     * @param id
     * @param desc
     * @return
     * @throws ServiceException
     */
    public Response excluir(Integer id, String desc) throws ServiceException {
        try {
            this.buscarPorEmpresa(new Empresa(id)).stream().forEach(d -> {
                if (d.getDescricao().equals(desc)) {
                    this.departamento = d;
                }
            });
            if (this.departamento != null) {
                if (!this.departamento.getFuncionarioList().isEmpty()) {
                    throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.DEPARTAMENTO.ALERTA_EXISTE_FUNCIONARIO.getResource()));
                }
                this.getDao().delete(this.departamento);
                this.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.getByClass(Departamento.class.getSimpleName()), CONSTANTES.Enum_AUDITORIA.getPorDescricao(Departamento.class.getSimpleName()), CONSTANTES.Enum_OPERACAO.EXCLUIR, this.departamento);
                return this.getTopPontoResponse().sucessoExcluir(this.departamento.toString());
            } else {
                throw new ServiceException(this.getTopPontoResponse().alertaNaoCad(new Departamento().toString()));
            }
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroExcluir(new Departamento().toString()), ex);
        }
    }

    @Override
    public Response excluir(Class<Departamento> c, Object id) throws ServiceException {
        try {
            this.departamento = null;
            this.departamento = validarExcluir(new Departamento((Integer) id));
            
            if (!this.departamento.getGrupoList().isEmpty()) {
                this.departamento.getGrupoList().stream().forEach(g -> {
                    List<Departamento> list = g.getDepartamentoList().stream()
                            .filter(d -> d != this.departamento).collect(Collectors.toList());
                    g.setDepartamentoList(list);
                    try {
                        this.grupoService.atualizar(g);
                    } catch (ServiceException ex) {
                        Logger.getLogger(DepartamentoService.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });

            }
            
            this.departamento.setFuncionarioDepartamentoList(null);
            this.departamento.setGrupoList(null);
            this.departamento.setFuncionarioList(null);
            
            this.departamento = (Departamento) this.getDao().save(this.departamento);

            this.getDao().delete(this.departamento);
            
            this.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_EMPRESAS_DEPARTAMENTO, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.EXCLUIR, this.departamento);
            return this.getTopPontoResponse().sucessoExcluir(this.departamento.toString());
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroExcluir(new Departamento().toString()), ex);
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="VALIDAÇÕES OBRIGATÓRIAS">
    @Override
    public Departamento validarSalvar(Departamento departamento) throws ServiceException {
        if (departamento.getDescricao().equals("") || departamento.getDescricao() == null) {
            throw new ServiceException(this.getTopPontoResponse().campoObrigatorio(Departamento_.descricao.getName()));
        }
        if (departamento.getEmpresa() == null || departamento.getEmpresa().getIdEmpresa() == null) {
            throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.DEPARTAMENTO.ALERTA_EMPRESA_OBRIGATORIA.getResource()));
        }
        Empresa e = this.empresaService.buscar(Empresa.class, departamento.getEmpresa().getIdEmpresa());
        departamento.setEmpresa(e);
        departamento = validatAlteracaoJaCadatrado(departamento);
        return departamento;
    }

    @Override
    public Departamento validarExcluir(Departamento t) throws ServiceException {
        Departamento departamento = validarIdentificador(t);
        departamento = this.buscar(Departamento.class, t.getIdDepartamento());
        departamento = ValidarDepartamentoFuncionario(t);
        return departamento;
    }

    @Override
    public Departamento validarAtualizar(Departamento departamento) throws ServiceException {
        this.validarIdentificador(departamento);
        this.validarAtualizarValoresEntreEntidades(departamento);
        this.validatAlteracaoJaCadatrado(departamento);
        this.empresaService.buscar(Empresa.class, departamento.getEmpresa().getIdEmpresa());
        return departamento;
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="REGRAS DE VALIDAÇÕES">
    /**
     * Valida se o departamento possui funcionários
     */
    private Departamento ValidarDepartamentoFuncionario(Departamento departamento) throws ServiceException {
        departamento = this.buscar(Departamento.class, departamento.getIdDepartamento());
        if (departamento.getFuncionarioList().size() > 0) {
            throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.DEPARTAMENTO.ALERTA_EXISTE_FUNCIONARIO.getResource()));
        }
        return departamento;
    }

    /**
     * Valida se o depart5amento possui um id
     */
    private Departamento validarIdentificador(Departamento departamento) throws ServiceException {
        if (departamento == null || departamento.getIdDepartamento() == null) {
            throw new ServiceException(this.getTopPontoResponse().alertaNaoCad(Departamento.class));
        }
        return departamento;
    }

    /**
     * Atualiza valores entre entidades departamento
     */
    private Departamento validarAtualizarValoresEntreEntidades(Departamento departamento) throws ServiceException {
        try {
            Departamento d = (Departamento) this.getDao().find(Departamento.class, departamento.getIdDepartamento());

            departamento.setDescricao(departamento.getDescricao() == null ? d.getDescricao() : departamento.getDescricao());
            departamento.setEmpresa(departamento.getEmpresa() == null ? d.getEmpresa() : departamento.getEmpresa());
            departamento.setAtivo(departamento.getAtivo() == null ? d.getAtivo() : departamento.getAtivo());

            return departamento;
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroValidar(Departamento.class));
        }
    }

    /**
     * Valida se já existe um departamento cadastrado
     */
    private Departamento validatAlteracaoJaCadatrado(Departamento departamento) throws ServiceException {

        try {
            // busca o departamento pela descrição
            this.map = new HashMap<>();
            this.map.put(Departamento_.descricao.getName(), departamento.getDescricao());
            this.map.put(Departamento_.empresa.getName(), departamento.getEmpresa());
            List<Departamento> dpts;
            if (!(dpts = this.getDao().findbyAttributes(map, Departamento.class)).isEmpty()) {
                if (departamento.getIdDepartamento() == null || !dpts.get(0).getIdDepartamento().equals(departamento.getIdDepartamento())) {
                    throw new ServiceException(this.getTopPontoResponse().alertaJaCad(Departamento.class));
                }
            }

        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroValidar(Departamento.class), ex);
        }
        return departamento;
    }
//</editor-fold>

}
