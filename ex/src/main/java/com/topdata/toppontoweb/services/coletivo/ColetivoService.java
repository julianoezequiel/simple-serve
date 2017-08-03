package com.topdata.toppontoweb.services.coletivo;

//<editor-fold defaultstate="collapsed" desc="IMPORTS">
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.dao.coletivo.ColetivoDao;
import com.topdata.toppontoweb.dto.ColetivoTransfer;
import com.topdata.toppontoweb.entity.Entidade;
import com.topdata.toppontoweb.entity.autenticacao.Operador;
import com.topdata.toppontoweb.entity.empresa.Departamento;
import com.topdata.toppontoweb.entity.empresa.Empresa;
import com.topdata.toppontoweb.entity.funcionario.Coletivo;
import com.topdata.toppontoweb.entity.funcionario.Coletivo_;
import com.topdata.toppontoweb.entity.funcionario.Funcionario;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoResponse;
import com.topdata.toppontoweb.services.TopPontoService;
import com.topdata.toppontoweb.services.empresa.EmpresaService;
import com.topdata.toppontoweb.services.funcionario.FuncionarioService;
import com.topdata.toppontoweb.services.funcionario.bancohoras.fechamento.FechamentoAcerto;
import com.topdata.toppontoweb.services.permissoes.OperadorService;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;
//</editor-fold>

/**
 * Implementações genéricas para as regras de negócio dos lançamentos coletivo
 *
 * @author juliano.ezequiel
 * @param <T>
 */
@Service
public class ColetivoService<T> extends TopPontoService<Entidade, Object> {

    //<editor-fold defaultstate="collapsed" desc="CDI">
    @Autowired
    private ColetivoDao coletivoDao;
    @Autowired
    private FuncionarioService funcionarioService;
    @Autowired
    private EmpresaService empresaService;
    @Autowired
    private OperadorService operadorService;
    //</editor-fold>

    //<editor-fold defaultstate="" desc="CRUD">
    /**
     * Cria um novo coletivo
     *
     * @param coletivo
     * @param funcionalidade
     * @return
     * @throws ServiceException
     */
    public Coletivo criarColetivo(Coletivo coletivo, CONSTANTES.Enum_FUNCIONALIDADE funcionalidade) throws ServiceException {
        try {
            Coletivo c = new Coletivo();
            c.setDescricao(coletivo.getDescricao());
            validarCamposObrigatorios(c);
            coletivo = (Coletivo) getDao().save(c);
            getAuditoriaServices().auditar(funcionalidade, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.INCLUIR, coletivo);
            return coletivo;
        } catch (DaoException ex) {
            throw new ServiceException(getTopPontoResponse().erroSalvar(new Coletivo().toString()), ex);
        }
    }

    /**
     * Atualiza o coletivo
     *
     * @param coletivo
     * @param funcionalidade
     * @return
     * @throws ServiceException
     */
    public Coletivo atualizarColetivo(Coletivo coletivo, CONSTANTES.Enum_FUNCIONALIDADE funcionalidade) throws ServiceException {
        try {
            validarCamposObrigatorios(coletivo);
            coletivo = (Coletivo) getDao().save(coletivo);
            getAuditoriaServices().auditar(funcionalidade, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.EDITAR, coletivo);
            return coletivo;
        } catch (DaoException ex) {
            throw new ServiceException(getTopPontoResponse().erroSalvar(new Coletivo().toString()), ex);
        }
    }

    @Override
    public Entidade buscar(Class<Entidade> entidade, Object id) throws ServiceException {
        try {
            Coletivo c = (Coletivo) getDao().find(Coletivo.class, (Integer) id);
            if (Objects.nonNull(c)) {
                return c;
            } else {
                throw new ServiceException(this.getTopPontoResponse().alertaNaoCad(new Coletivo().toString()));
            }
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroBuscar(), ex);
        }
    }

    public Response excluirColetivo(Coletivo coletivo, CONSTANTES.Enum_FUNCIONALIDADE funcionalidade) throws ServiceException {
        this.coletivoDao.excluirEntidade(coletivo);

        getAuditoriaServices().auditar(funcionalidade, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.EXCLUIR, coletivo);

        return this.getTopPontoResponse().sucessoExcluir(coletivo.getIdColetivo().toString());
    }
    //</editor-fold>

    //<editor-fold defaultstate="" desc="VALIDAÇÕES">
    private void validarCamposObrigatorios(Coletivo coletivo) throws ServiceException {
        if (coletivo.getDescricao() == null) {
            throw new ServiceException(getTopPontoResponse().campoObrigatorio(Coletivo_.descricao.getName()));
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="" desc="FUNÇÕES AUXILIARES">
    /**
     * Valida se o coletivo não possui mais vínculos
     *
     * @param coletivo
     * @throws ServiceException
     * @return Coletivo não existe mais no banco?
     */
    public Boolean excluirColetivoSemVinculo(Coletivo coletivo) throws ServiceException {
        if (coletivo != null) {
            try {
                Coletivo c = this.getColetivoDao().find(Coletivo.class, coletivo.getIdColetivo());
                try {
                    if (this.getColetivoDao().contains(c)) {
                        if (coletivo.getFuncionarioBancoHorasList() != null && !coletivo.getFuncionarioBancoHorasList().isEmpty()) {
                            coletivo.getFuncionarioBancoHorasList().clear();
                        }
                        this.getColetivoDao().refresh(c);
                    }
                } catch (Exception e) {
                    LOGGER.debug("Não fez o refresh da entidade");
                }
                if ((c.getCompensacaoList() == null || c.getCompensacaoList().isEmpty())
                        && (c.getAfastamentoList() == null || c.getAfastamentoList().isEmpty())
                        && (c.getFuncionarioBancoHorasFechamentoList() == null || c.getFuncionarioBancoHorasFechamentoList().isEmpty())
                        && (c.getFuncionarioBancoHorasList() == null || c.getFuncionarioBancoHorasList().isEmpty())
                        && (c.getFuncionarioCalendarioList() == null || c.getFuncionarioCalendarioList().isEmpty())
                        && (c.getFuncionarioJornadaList() == null || c.getFuncionarioJornadaList().isEmpty())) {
                    this.coletivoDao.excluirEntidade(coletivo);
                    return true;
                }
                return false;
            } catch (DaoException ex) {
                LOGGER.error(ColetivoService.class.getName(), ex);
            }
        }

        return true;
    }

    /**
     * Consulta as empresa pelo departamento, e valida se o operador possui
     * permissão.
     *
     * @param depList Lista de departamentos do coletivo
     * @param operador Operador que tem as permissoes
     * @return
     */
    public List<Empresa> filtrarEmpresa(List<Departamento> depList, Operador operador) {

        List<Empresa> empresaList = depList.stream().map(f -> f.getEmpresa()).distinct().collect(Collectors.toList());
        List<Departamento> deptosValidos = operador.getGrupo()
                .getDepartamentoList().stream()
                .distinct().collect(Collectors.toList());
        if (deptosValidos.isEmpty()) {

            empresaList.stream().forEach((d1) -> {
                d1.setPermitido(true);
            });

        } else {
            //Para todas as empresas, verifica se existe um departamento n permitido
            empresaList.stream().forEach((empresa) -> {
                empresa.setPermitido(
                        !depList.stream().anyMatch(
                                dep -> !dep.getPermitido() && Objects.equals(dep.getEmpresa().getIdEmpresa(), empresa.getIdEmpresa())));
            });
        }

        return empresaList;
    }

    /**
     * Consulta os deparamentos pelo funcionário e valida se o operador possui
     * permissão
     *
     * @param list
     * @param operador
     * @return
     */
    public List<Departamento> filtrarDepartamento(List<Funcionario> list, Operador operador) {

        List<Departamento> departamentoList = list.stream().map(f -> f.getDepartamento()).distinct().collect(Collectors.toList());
        List<Departamento> deptosValidos = operador.getGrupo().getDepartamentoList().stream().distinct().collect(Collectors.toList());

        if (deptosValidos.isEmpty()) {
            departamentoList.stream().forEach((d1) -> {
                d1.setPermitido(Boolean.TRUE);
            });

        } else {
            deptosValidos.stream().forEach((Departamento d) -> {
                departamentoList.stream().forEach((d1) -> {
                    d1.setPermitido(deptosValidos.contains(d1));
                });
            });
        }
        return departamentoList;
    }

    /**
     * Valida se o coletivo poderá ser editado pelo operador
     *
     * @param deptFunionarios
     * @param operador
     * @return
     */
    public Boolean isColetivoPermitido(List<Departamento> deptFunionarios, Operador operador) {
        if (operador.getGrupo().getDepartamentoList().isEmpty()) {
            return Boolean.TRUE;
        } else {
            return !deptFunionarios.stream().anyMatch(funcDep -> funcDep.getPermitido().equals(Boolean.FALSE));
        }
    }

    /**
     * Cria o objeto de tranfer a partir da lista de coletivo
     *
     * @param coletivoList
     * @return
     */
    public List<ColetivoTransfer> listarColetivo(List<Coletivo> coletivoList) {
        List<ColetivoTransfer> coletivoTransferList = new ArrayList<>();
        coletivoList.stream().forEach(a ->coletivoTransferList.add(new ColetivoTransfer(a)));
        return coletivoTransferList;
    }

    /**
     * Retorna o operador autenticado no sistema
     *
     * @param request
     * @return
     * @throws ServiceException
     */
    public Operador getOperadorAtual(HttpServletRequest request) throws ServiceException {
        return operadorService.getOperadorDaSessao(request);
    }

    public ColetivoDao getColetivoDao() {
        return coletivoDao;
    }

    public EmpresaService getEmpresaService() {
        return empresaService;
    }

    public FuncionarioService getFuncionarioService() {
        return funcionarioService;
    }

//</editor-fold>
}
