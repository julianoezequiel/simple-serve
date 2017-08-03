package com.topdata.toppontoweb.services.empresa;

//<editor-fold defaultstate="collapsed" desc="CDI">
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.dao.empresa.CeiDao;
import com.topdata.toppontoweb.entity.empresa.Cei;
import com.topdata.toppontoweb.entity.empresa.Cei_;
import com.topdata.toppontoweb.entity.empresa.Empresa;
import com.topdata.toppontoweb.entity.funcionario.Funcionario;
import com.topdata.toppontoweb.entity.rep.Rep;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoService;
import com.topdata.toppontoweb.services.Validacao;
import com.topdata.toppontoweb.services.funcionario.FuncionarioService;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;
import com.topdata.toppontoweb.utils.constantes.MSG;
//</editor-fold>

/**
 * @version 1.0.0.0 data 19/01/2017
 * @since 1.0.0.0 data 19/07/2016
 *
 * @author juliano.ezequiel
 */
@Service
public class CeiServices extends TopPontoService<Cei, Object> implements Validacao<Cei, Object> {

    //<editor-fold defaultstate="collapsed" desc="CDI">
    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private FuncionarioService funcionarioService;

    @Autowired
    private CeiDao ceiDao;
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="VARIÁVEIS">
    private HashMap<String, Object> map;
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="CONSULTAS">
    /**
     * Função para consultar os Cei da empresa
     *
     * @param id da Empresa a ser consultada
     * @return lista de Cei da empresa
     * @throws ServiceException
     */
    public List<Cei> buscarCeiEmpresa(Integer id) throws ServiceException {
        try {
            this.map = new HashMap<>();
            this.map.put(Cei_.empresa.getName(), empresaService.buscar(Empresa.class, id));
            return this.getDao().findbyAttributes(this.map, Cei.class);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erro(MSG.REST.ERRO_BUSCAR.getResource(), Cei.class), ex);
        }
    }

    public List<Cei> buscarCeiEmpresa(Empresa empresa) throws ServiceException {
        return this.ceiDao.bucarPorEmpresa(empresa);
    }

//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="CRUD">
    @Override
    public Response salvar(Cei cei) throws ServiceException {
        try {
            cei = validar(cei, null);
            cei = (Cei) this.getDao().save(cei);
            this.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_EMPRESA_CEI, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.INCLUIR, cei);
            return this.getTopPontoResponse().sucessoSalvar(new Cei().toString());
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroSalvar(Cei.class), ex);
        }
    }

    @Override
    public Response atualizar(Cei cei) throws ServiceException {
        try {
            cei = this.validar(cei, null);
            cei = (Cei) this.getDao().save(cei);
            this.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_EMPRESA_CEI, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.EDITAR, cei);
            return this.getTopPontoResponse().sucessoAtualizar(new Cei().toString());
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroSalvar(new Cei().toString()), ex);
        }
    }

    @Override
    public Response excluir(Class<Cei> c, Object id) throws ServiceException {
        try {
            //busca o cei
            Cei cei = this.buscar(Cei.class, id);
            if (cei != null) {
                for (Rep r : cei.getEmpresa().getRepList()) {
                    if (r.getCei() != null && r.getCei().equals(cei)) {
                        throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.CEI.ALERTA_REP_VINCULADO.getResource()));
                    }
                }

                List<Funcionario> funcionarioLista = funcionarioService.buscarPorCei(cei);
                if (!funcionarioLista.isEmpty()) {
                    throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.CEI.ALERTA_FUNCIONARIO_VINCULADO.getResource()));
                }
            }
            //realiza a exclusão após a validação
            this.getDao().delete(cei);
            this.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_EMPRESA_CEI, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.EXCLUIR, cei);
            return this.getTopPontoResponse().sucessoExcluir(new Cei().toString());
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroExcluir(new Cei().toString()), ex);
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="VALIDAÇÕES">
    @Override
    public Cei validar(Cei cei, Object i) throws ServiceException {
        try {
            //remove todos os caracteres que não forem números
            cei.setDescricao(cei.getDescricao().replaceAll("[^0-9]", ""));
            // o tamanho deverá ser exatamente 12
            if (cei.getDescricao().length() != 12) {
                throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.CEI.ALERTA_CEI_INVALIDO.getResource()));
            }

            if (cei.getEmpresa() == null || cei.getEmpresa().getIdEmpresa() == null) {
                throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.CEI.ALERTA_CEI_EMPRESA_OBRIGATORIA.getResource()));
            }

            this.empresaService.buscar(Empresa.class, cei.getEmpresa().getIdEmpresa());

            //busca por descrição e por empresa
            this.map = new HashMap<>();
            this.map.put(Cei_.descricao.getName(), cei.getDescricao());
//            this.map.put(Cei_.empresa.getName(), cei.getEmpresa());
            List<Cei> ceis = this.getDao().findbyAttributes(this.map, Cei.class);
            //valida se já esta cadastrado
            if (!ceis.isEmpty()) {
                if (cei.getIdCei() == null) {
                    throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.CEI.ALERTA_CEI_JA_CADASTRADO.getResource()));
                }
                //valida se está alterando por uma descrição já existente
                if (!Objects.equals(ceis.get(0).getIdCei(), cei.getIdCei())) {
                    throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.CEI.ALERTA_CEI_JA_CADASTRADO.getResource()));
                }
            }

            //retorna o objeto validado
            return cei;
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroValidar(Cei.class), ex);
        }
    }
//</editor-fold>
}
