package com.topdata.toppontoweb.services.configuracoes;

//<editor-fold defaultstate="collapsed" desc="IMPORTS">
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.entity.configuracoes.Dsr;
import com.topdata.toppontoweb.entity.configuracoes.Dsr_;
import com.topdata.toppontoweb.entity.empresa.Empresa;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoService;
import com.topdata.toppontoweb.services.ValidacoesCadastro;
import com.topdata.toppontoweb.services.empresa.EmpresaService;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;
import com.topdata.toppontoweb.utils.constantes.MSG;
//</editor-fold>

/**
 * Classe de regras de Negócio para o DSR
 *
 * @version 1.0.0 data 19/07/2016
 * @since 1.0.0 data 19/07/2016
 *
 * @author juliano.ezequiel
 */
@Service
public class DsrServices extends TopPontoService<Dsr, Object>
        implements ValidacoesCadastro<Dsr, Object> {

    @Autowired
    private EmpresaService empresaService;

    private HashMap<String, Object> map;

    //<editor-fold defaultstate="" desc="CONSULTAS">
    public Dsr buscarDsrEmpresa(Integer id) throws ServiceException {
        try {
            this.map = new HashMap<>();
            this.map.put(Dsr_.empresa.getName(), this.empresaService.buscar(Empresa.class, id));
            List<Dsr> dsrList = this.getDao().findbyAttributes(this.map, Dsr.class);
            return dsrList.size() > 0 ? dsrList.get(0) : null;
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erro(MSG.REST.ERRO_BUSCAR.getResource(), Dsr.class), ex);
        }
    }

    public Dsr buscarDsrEmpresa(Empresa e) throws ServiceException {
        try {
            this.map = new HashMap<>();
            this.map.put(Dsr_.empresa.getName(), e);
            List<Dsr> dsrList = this.getDao().findbyAttributes(this.map, Dsr.class);
            return dsrList.size() > 0 ? dsrList.get(0) : null;
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erro(MSG.REST.ERRO_BUSCAR.getResource(), Dsr.class), ex);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="CRUD">
    @Override
    public Dsr buscar(Class<Dsr> entidade, Object id) throws ServiceException {
        try {
            Dsr d = (Dsr) this.getDao().find(Dsr.class, id);
            if (d == null) {
                throw new ServiceException(this.getTopPontoResponse().alertaNaoCad(new Dsr().toString()));
            }
            return d;
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroBuscar(), ex);
        }
    }

    @Override
    public Response salvar(Dsr dsr) throws ServiceException {
        try {
            dsr = (Dsr) this.getDao().save(validarSalvar(dsr));
            this.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_CONFIGURACOES_DSR, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.INCLUIR, dsr);
            return this.getTopPontoResponse().sucessoSalvar(dsr.toString(), dsr);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroSalvar(Dsr.class), ex);
        }
    }

    @Override
    public Response atualizar(Dsr dsr) throws ServiceException {
        try {
            dsr = (Dsr) this.getDao().save(validarAtualizar(dsr));
            this.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_CONFIGURACOES_DSR, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.EDITAR, dsr);
            return this.getTopPontoResponse().sucessoAtualizar(dsr.toString(), dsr);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroAtualizar(Dsr.class), ex);
        }
    }

    @Override
    public Response excluir(Class<Dsr> c, Object id) throws ServiceException {
        try {
            Dsr dsr = validarExcluir(new Dsr((Integer) id));
            this.getDao().delete(dsr);
            this.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_CONFIGURACOES_DSR, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.EXCLUIR, dsr);
            return this.getTopPontoResponse().sucessoExcluir(dsr.toString());
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroExcluir(new Dsr().toString()), ex);
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="VALIDAÇÕES OBRIGATÓRIAS">
    @Override
    public Dsr validarSalvar(Dsr t) throws ServiceException {
        ValidarExisteEmpresa(t);
        return t;
    }

    @Override
    public Dsr validarExcluir(Dsr t) throws ServiceException {
        if (t == null || t.getIdDsr() == null) {
            throw new ServiceException(this.getTopPontoResponse().alertaNaoCad(new Dsr().toString()));
        }
        return buscar(Dsr.class, t.getIdDsr());
    }

    @Override
    public Dsr validarAtualizar(Dsr t) throws ServiceException {
        if (t == null || t.getIdDsr() == null) {
            throw new ServiceException(this.getTopPontoResponse().alertaNaoCad(t.toString()));
        }
//        ValidarExisteEmpresa(t);
        buscar(Dsr.class, t.getIdDsr());
        return t;
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="REGRAS DE VALIDAÇÕES">
    private void ValidarExisteEmpresa(Dsr t) throws ServiceException {
        if (t.getEmpresa() != null && t.getEmpresa().getIdEmpresa() != null) {
            empresaService.buscar(Empresa.class, t.getEmpresa().getIdEmpresa());
        } else {
            throw new ServiceException(this.getTopPontoResponse().campoObrigatorio("Empresa"));
        }
    }
//</editor-fold>
}
