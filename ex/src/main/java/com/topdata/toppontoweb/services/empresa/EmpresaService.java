package com.topdata.toppontoweb.services.empresa;

//<editor-fold defaultstate="collapsed" desc="IMPORTS">
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.dao.empresa.EmpresaDao;
import com.topdata.toppontoweb.entity.Entidade;
import com.topdata.toppontoweb.entity.autenticacao.Grupo;
import com.topdata.toppontoweb.entity.empresa.Departamento;
import com.topdata.toppontoweb.entity.empresa.Empresa;
import com.topdata.toppontoweb.entity.empresa.Empresa_;
import com.topdata.toppontoweb.entity.funcionario.Funcionario;
import com.topdata.toppontoweb.entity.tipo.TipoDocumento;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoService;
import com.topdata.toppontoweb.services.Validacao;
import com.topdata.toppontoweb.services.permissoes.GrupoService;
import com.topdata.toppontoweb.services.ValidacoesCadastro;
import com.topdata.toppontoweb.utils.Generator;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES.Enum_FUNCIONALIDADE;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES.Enum_OPERACAO;
import com.topdata.toppontoweb.utils.constantes.MSG;
import java.util.logging.Level;
import java.util.logging.Logger;
//</editor-fold>

/**
 * Classe realiza as regras de negócio para a Empresa
 *
 * @version 1.0.0.0 data 18/01/2017
 * @since 1.0.0.0 data 17/05/2016
 *
 * @see Empresa
 *
 * @author juliano.ezequiel
 */
@Service
public class EmpresaService extends TopPontoService<Empresa, Object>
        implements ValidacoesCadastro<Empresa, Object> {

    //<editor-fold defaultstate="collapsed" desc="CDI">
    @Autowired
    EmpresaDao empresaDao;
    
    @Autowired
    private GrupoService grupoService;

    @Autowired
    private DepartamentoService departamentoService;

    @Autowired
    private CeiServices ceiServices;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="VARIÁVEIS">
    private List<Validacao> validacoes;
    private HashMap<String, Object> map;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="CONSULTAS">
    /**
     * Busca as empresa pelo grupo de acesso e verifica se não possui DSR
     *
     * @param id
     * @return
     * @throws ServiceException
     */
    public List<Empresa> buscaSemDsr(Integer id) throws ServiceException {
        List<Empresa> empresas = buscaPorGrupoPermissao(id).stream().filter(e -> e.getDsrList().isEmpty()).distinct().collect(Collectors.toList());
        return empresas;
    }

    public List<Empresa> buscaPorDrs(Integer id) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Realiza a consulta na base de dados de uma empresa pela sua descrição
     *
     * @param empresa com o CPF ou CNPJ
     * @return Empresa
     * @throws ServiceException
     */
    public Empresa buscaPorDocumento(Empresa empresa) throws ServiceException {
        return buscaPorDocumento(empresa.getDocumento());
    }
    
    
    /**
     * Realiza a consulta na base de dados de uma empresa pela sua descrição
     *
     * @param documento o CPF ou CNPJ
     * @return Empresa
     * @throws ServiceException
     */
    public Empresa buscaPorDocumento(String documento) throws ServiceException {
        try {
            map = new HashMap<>();
            map.put(Empresa_.documento.getName(), documento);
            List<Empresa> empresas = this.getDao().findbyAttributes(map, Empresa.class);
            return empresas.isEmpty() ? null : empresas.get(0);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erro(MSG.CADASTRO.ERRO_BUSCAR, Empresa.class), ex);
        }
    }

    /**
     * Busca as Empresa pelo grupo de acesso. O grupo de acesso controla os
     * departamentos. Caso a lista de departamento do grupo de acesso esteja
     * vazio, o grupo possui acesso a todas as empresas
     *
     * @param id do Grupo de acesso
     * @return Lista de Empresas
     * @throws ServiceException
     */
    public List<Empresa> buscaPorGrupoPermissao(Integer id) throws ServiceException {
        try {
            List<Departamento> departamentoList = this.departamentoService.buscarPorGrupo(new Grupo(id));
            if (departamentoList.size() <= 0) {
                //retorna todas as Empresas
                List<Empresa> empresaList = this.getDao().findAll(Empresa.class);
                empresaList.stream().forEach(e -> {
                    try {
                        e.setCeiList(this.ceiServices.buscarCeiEmpresa(e));
                        e.setDepartamentoList(this.departamentoService.buscarPorEmpresa(e));
                    } catch (ServiceException ex) {
                        Logger.getLogger(EmpresaService.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
                return empresaList;
            } else {
                //retorna somemente as empresas que o grupo possui acesso.
                List<Empresa> empresaList = departamentoList
                        .stream()
                        .map(departamento -> departamento.getEmpresa())
                        .distinct().collect(Collectors.toList());
                empresaList.stream().forEach(e -> {
                    try {
                        e.setCeiList(this.ceiServices.buscarCeiEmpresa(e));
                        e.setDepartamentoList(this.departamentoService.buscarPorEmpresa(e));
                    } catch (ServiceException ex) {
                        Logger.getLogger(EmpresaService.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
                return empresaList;
            }
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erro(MSG.CADASTRO.ERRO_BUSCAR, Empresa.class), ex);
        }
    }

    /**
     * Busca as Empresa pelo grupo de acesso. O grupo de acesso controla os
     * departamentos. Caso a lista de departamento do grupo de acesso esteja
     * vazio, o grupo possui acesso a todas as empresas
     *
     * @param id do Grupo de acesso
     * @return Lista de Empresas
     * @throws ServiceException
     */
    public List<Empresa> buscaPorGrupoPermissaoAtivas(Integer id) throws ServiceException {
        try {
            List<Departamento> departamentoList = this.departamentoService.buscarPorGrupo(new Grupo(id));
            if (departamentoList.size() <= 0) {
                //retorna todas as Empresas
                List<Empresa> empresaList = this.getDao().findAll(Empresa.class);
                empresaList.stream().forEach(e -> {
                    try {
                        e.setCeiList(this.ceiServices.buscarCeiEmpresa(e));
                        e.setDepartamentoList(this.departamentoService.buscarPorEmpresa(e));
                    } catch (ServiceException ex) {
                        Logger.getLogger(EmpresaService.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
                return empresaList
                        .stream()
                        .filter(e -> e.getAtivo().equals(Boolean.TRUE))
                        .collect(Collectors.toList());
            } else {
                //retorna somemente as empresas que o grupo possui acesso.
                List<Empresa> empresaList = departamentoList
                        .stream()
                        .map(departamento -> departamento.getEmpresa())
                        .filter(e -> e.getAtivo().equals(Boolean.TRUE))
                        .distinct().collect(Collectors.toList());
                empresaList.stream().forEach(e -> {
                    try {
                        e.setCeiList(this.ceiServices.buscarCeiEmpresa(e));
                        e.setDepartamentoList(this.departamentoService.buscarPorEmpresa(e));
                    } catch (ServiceException ex) {
                        Logger.getLogger(EmpresaService.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
                return empresaList;
            }
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erro(MSG.CADASTRO.ERRO_BUSCAR, Empresa.class), ex);
        }
    }
    
    public Empresa buscarPorFuncionario(Funcionario funcionario) throws ServiceException {
        try {
            return empresaDao.buscarPorFuncionario(funcionario);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erro(MSG.CADASTRO.ERRO_BUSCAR, Empresa.class), ex);
        }
    }

    public List<Entidade> buscarTodos(Class<Empresa> entidade, Boolean validar) throws ServiceException {
        List<Empresa> list = null;
        try {
            list = this.getDao().findAll(Empresa.class);
            list.stream().forEach(e -> {
                try {
                    this.validarExcluirVinculos(e);
                } catch (ServiceException ex) {
                    e.setPossuiVinculo(Boolean.TRUE);
                }
            });
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroBuscar(), ex);
        }
        return (List<Entidade>) (List) list;
    }

    /**
     * Busca a lista de funcioários da empresa
     *
     * @param empresa
     * @return
     * @throws ServiceException
     */
    private List<Funcionario> buscarPorEmpresa(Empresa empresa) throws ServiceException {
        try {

            if (empresa == null || empresa.getIdEmpresa() == null) {
                throw new ServiceException(this.getTopPontoResponse().alertaNaoCad(new Empresa().toString()));
            }

            List<Funcionario> funcionarios = new ArrayList<>();

            Empresa e = (Empresa) this.getDao().find(Empresa.class, empresa.getIdEmpresa());

            e.getDepartamentoList().stream().map(d
                    -> d.getFuncionarioList()).collect(Collectors.toList()).forEach(func -> {
                funcionarios.addAll(func.stream().distinct().collect(Collectors.toList()));
            });

            return funcionarios;

        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroBuscar(), ex);
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="" desc="CRUD">
    @Override
    public Response salvar(Empresa empresa) throws ServiceException {
        try {
            empresa = (Empresa) this.getDao().save(validarSalvar(empresa));
            this.getAuditoriaServices().auditar(Enum_FUNCIONALIDADE.CAD_EMPRESAS_EMPRESAS, CONSTANTES.Enum_AUDITORIA.DEFAULT, Enum_OPERACAO.INCLUIR, empresa);
            return this.getTopPontoResponse().sucessoSalvar(empresa.toString(), empresa);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroSalvar(Empresa.class), ex);
        }
    }

    @Override
    public Response atualizar(Empresa empresa) throws ServiceException {
        try {
            empresa = (Empresa) this.getDao().save(validarAtualizar(empresa));
            this.getAuditoriaServices().auditar(Enum_FUNCIONALIDADE.CAD_EMPRESAS_EMPRESAS, CONSTANTES.Enum_AUDITORIA.DEFAULT, Enum_OPERACAO.EDITAR, empresa);
            return this.getTopPontoResponse().sucessoAtualizar(empresa.toString(), empresa);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroAtualizar(Empresa.class), ex);
        }
    }

    @Override
    public Response excluir(Class<Empresa> c, Object id) throws ServiceException {
        try {
            Empresa empresa = validarExcluir(new Empresa((Integer) id));

            //Remove os departamentos
            empresa.getDepartamentoList().stream().forEach((dep) -> {
                try {
                    departamentoService.excluir(Departamento.class, dep.getIdDepartamento());
                } catch (ServiceException ex) {
                    Logger.getLogger(EmpresaService.class.getName()).log(Level.SEVERE, null, ex);
                }
            });

            this.getDao().delete(empresa);
            this.getAuditoriaServices().auditar(Enum_FUNCIONALIDADE.CAD_EMPRESAS_EMPRESAS, CONSTANTES.Enum_AUDITORIA.DEFAULT, Enum_OPERACAO.EXCLUIR, empresa);
            return this.getTopPontoResponse().sucessoExcluir(empresa.toString());
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().sucessoExcluir(Empresa.class), ex);
        }
    }

    @Override
    public Empresa buscar(Class<Empresa> entidade, Object id) throws ServiceException {
        try {
            Empresa e = (Empresa) this.getDao().find(entidade, id);
            e.setCeiList(this.ceiServices.buscarCeiEmpresa(e));
            e.setDepartamentoList(this.departamentoService.buscarPorEmpresa(e));
            if (e == null) {
                throw new ServiceException(this.getTopPontoResponse().alertaNaoCad(new Empresa().toString()));
            }
//            e.setPossuiHistorico(!e.getFuncionarioEmpresaList().isEmpty() || !e.getMarcacoesList().isEmpty());
//            e.setPossuiVinculo(!e.getEmpresaFechamentoHorasList().isEmpty() || !e.getFuncionarioEmpresaList().isEmpty() || !e.getRepList().isEmpty());
            e.carregarHistorico();
            return e;
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erro(MSG.CADASTRO.ERRO_BUSCAR, entidade), ex);
        }
    }

    public Empresa buscar(Integer id) throws ServiceException {
        try {
            Empresa e = (Empresa) this.getDao().find(Empresa.class, id);
            if (e == null) {
                throw new ServiceException(this.getTopPontoResponse().alertaNaoCad(new Empresa().toString()));
            }
            e.setCeiList(this.ceiServices.buscarCeiEmpresa(e));
            e.setDepartamentoList(this.departamentoService.buscarPorEmpresa(e));
            return e;
        } catch (DaoException ex) {
            throw new ServiceException();
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="VALIDAÇÕES OBRIGATÓRIAS">
    @Override
    public Empresa validarSalvar(Empresa empresa) throws ServiceException {
        if (empresa.getDocumento() == null) {
            throw new ServiceException(this.getTopPontoResponse().campoObrigatorio(Empresa_.documento.getName()));
        } else if (empresa.getRazaoSocial() == null || empresa.getRazaoSocial().equals("")) {
            throw new ServiceException(this.getTopPontoResponse().campoObrigatorio(Empresa_.razaoSocial.getName()));
        }
        empresa.setAtivo(empresa.getAtivo() != null ? empresa.getAtivo() : Boolean.TRUE);
        //Verifica se ja tem empresa com este decumento(CPF/CNPJ)
        return (Empresa) validarJaCadatrado(empresa);
    }

    @Override
    public Empresa validarExcluir(Empresa empresa) throws ServiceException {
        empresa = validarIdentificador(empresa);
        empresa = validarExcluirVinculos(empresa);
        return this.buscar(Empresa.class, empresa.getIdEmpresa());
    }

    @Override
    public Empresa validarAtualizar(Empresa empresa) throws ServiceException {
        this.validarIdentificador(empresa);
        this.validarJaCadatrado(empresa);
        this.validarAtualizarValoresEntreEntidades(empresa);
        return empresa;
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="FUNÇÕES AUXILIARES">
    public Response testar(Integer qtd) throws ServiceException {
        for (Integer i = 0; i < qtd; i++) {
            Empresa e = new Empresa();
            e.setAtivo(Boolean.TRUE);
            e.setDocumento(Generator.cnpj());
            e.setNomeFantasia("Nome fantasia " + i.toString());
            e.setRazaoSocial("Razão social " + i.toString());
            e.setTipoDocumento(new TipoDocumento(2));
            try {
                salvar(e);
            } catch (ServiceException se) {

            }
        }
        return this.getTopPontoResponse().sucessoValidar("foram criadas " + qtd + " de empresas para teste");
    }
//</editor-fold>

    //<editor-fold defaultstate="" desc="REGRAS DE VALIDAÇÕES">
    /**
     * Valida os vinculos existentes e retorna uma exceção caso possua algum
     * vinculo
     *
     * @param empresa
     * @return
     * @throws com.topdata.toppontoweb.services.ServiceException
     */
    private Empresa validarExcluirVinculos(Empresa empresa) throws ServiceException {
        Empresa e = buscar(Empresa.class, empresa.getIdEmpresa());
//        if (!e.getEmpresaFechamentoHorasList().isEmpty()) {
//            throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.EMPRESA.ALERTA_POSSUI_FECHAMENTO.getResource()));
//        }
        List<Funcionario> fucnionariosList = buscarPorEmpresa(empresa);
        if (!fucnionariosList.isEmpty() && fucnionariosList.size() == 1) {
            throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.EMPRESA.ALERTA_VINCULO_FUNCIONARIO.getResource()));
        } else if (!fucnionariosList.isEmpty() && fucnionariosList.size() > 1) {
            throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.EMPRESA.ALERTA_VINCULO_FUNCIONARIOS.getResource()));
        }
        if (!e.getPlanosList().isEmpty()) {
            throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.EMPRESA.ALERTA_VINCULO_PLANO.getResource()));
        }
        if (!e.getDsrList().isEmpty()) {
            throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.EMPRESA.ALERTA_POSSUI_DSR.getResource()));
        }
        if (!e.getRepList().isEmpty()) {
            throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.EMPRESA.ALERTA_VINCULO_REP.getResource()));
        }
        return empresa;
    }

    /**
     * Valida se ja existe uma empresa cadastrada com o CNPJ/CPF e CEI
     */
    private Empresa validarJaCadatrado(Empresa empresa) throws ServiceException {

        Empresa e1;
        //se encontrar um empresa pelo documento valida se é uma alteraçaõ ou uma inclusão
        if ((e1 = buscaPorDocumento(empresa)) != null) {
            if (empresa.getIdEmpresa() == null) {
                if (empresa.getTipoDocumento() != null) {
                    if (empresa.getTipoDocumento().getIdTipoDocumento().equals(CONSTANTES.Enum_DOCUMENTO.CNPJ.ordinal())) {
                        throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.EMPRESA.ALERTA_CNPJ_JA_CADASTRADO.getResource()));
                    } else {
                        throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.EMPRESA.ALERTA_CPF_JA_CADASTRADO.getResource()));
                    }
                }
            } else if (!empresa.getIdEmpresa().equals(e1.getIdEmpresa())) {
                if (empresa.getTipoDocumento() != null) {
                    if (empresa.getTipoDocumento().getIdTipoDocumento().equals(CONSTANTES.Enum_DOCUMENTO.CNPJ.ordinal())) {
                        throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.EMPRESA.ALERTA_CNPJ_JA_CADASTRADO.getResource()));
                    } else {
                        throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.EMPRESA.ALERTA_CPF_JA_CADASTRADO.getResource()));
                    }
                }
            }
        }
        return empresa;
    }

    /**
     * Busca na base de dados e mantem as entidades atualizadas
     */
    private Empresa validarAtualizarValoresEntreEntidades(Empresa empresa) throws ServiceException {

        Empresa empresaOriginal = buscar(Empresa.class, empresa.getIdEmpresa());

        empresa.setAtivo(empresa.getAtivo() != null ? empresa.getAtivo() : empresaOriginal.getAtivo());
        empresa.setBairro(empresa.getBairro() != null ? empresa.getBairro() : empresaOriginal.getBairro());
        empresa.setCeiList(empresaOriginal.getCeiList());
        empresa.setCep(empresa.getCep() != null ? empresa.getCep() : empresaOriginal.getCep());
        empresa.setCidade(empresa.getCidade() != null ? empresa.getCidade() : empresaOriginal.getCidade());
        empresa.setDepartamentoList(empresaOriginal.getDepartamentoList());
        empresa.setDocumento(empresa.getDocumento() != null ? empresa.getDocumento() : empresaOriginal.getDocumento());
        empresa.setEndereco(empresa.getEndereco() != null ? empresa.getEndereco() : empresaOriginal.getEndereco());
        empresa.setFax(empresa.getFax() != null ? empresa.getFax() : empresaOriginal.getFax());
        empresa.setFone(empresa.getFone() != null ? empresa.getFone() : empresaOriginal.getFone());
        empresa.setFuncionarioEmpresaList(empresaOriginal.getFuncionarioEmpresaList());
        empresa.setGrupoList(empresaOriginal.getGrupoList());
        empresa.setIdEmpresa(empresa.getIdEmpresa() != null ? empresa.getIdEmpresa() : empresaOriginal.getIdEmpresa());
        empresa.setNomeFantasia(empresa.getNomeFantasia() != null ? empresa.getNomeFantasia() : empresaOriginal.getNomeFantasia());
        empresa.setObservacao(empresa.getObservacao() != null ? empresa.getObservacao() : empresaOriginal.getObservacao());
        empresa.setPlanosList(empresa.getPlanosList());
        empresa.setRazaoSocial(empresa.getRazaoSocial() != null ? empresa.getRazaoSocial() : empresaOriginal.getRazaoSocial());
        empresa.setTipoDocumento(empresa.getTipoDocumento() != null ? empresa.getTipoDocumento() : empresaOriginal.getTipoDocumento());
        empresa.setUf(empresa.getUf() != null ? empresa.getUf() : empresaOriginal.getUf());
        return empresa;
    }

    /**
     * Valida se a empresa possui um id válido
     */
    private Empresa validarIdentificador(Empresa empresa) throws ServiceException {
        if (empresa == null || empresa.getIdEmpresa() == null) {
            throw new ServiceException(this.getTopPontoResponse().alertaNaoCad(Empresa.class));
        }
        return empresa;
    }
//</editor-fold>



}
