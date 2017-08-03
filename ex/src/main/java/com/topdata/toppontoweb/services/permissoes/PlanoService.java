package com.topdata.toppontoweb.services.permissoes;

//<editor-fold defaultstate="collapsed" desc="IMPORTS">
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.entity.Entidade;
import com.topdata.toppontoweb.entity.autenticacao.Planos;
import com.topdata.toppontoweb.entity.autenticacao.Planos_;
import com.topdata.toppontoweb.entity.empresa.Empresa;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoService;
import com.topdata.toppontoweb.utils.constantes.MSG;
//</editor-fold>

/**
 * @version 1.0.0 data 18/01/2017
 * @since 1.0.0 data 09/05/2016
 *
 * @author juliano.ezequiel
 */
@Service
public class PlanoService extends TopPontoService<Planos, Object> {

    private HashMap<String, Object> map;

    public Planos getLiberacao() throws ServiceException {
        return null;
    }

    @Override
    public List<Entidade> buscarTodos(Class<Planos> entidade) throws ServiceException {
        try {
            return this.getDao().findAll(entidade);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroBuscar(), ex);
        }
    }

    public Planos buscarPlanoValido() throws ServiceException {
        try {
            List<Planos> list = this.getDao().findAll(Planos.class);
            if (list.isEmpty()) {
                throw new ServiceException(MSG.PLANOS.ALERTA_NAO_EXISTE_UM_PLANO_VALIDO.getResource());
            }
            return list.get(0);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroBuscar(), ex);
        }
    }

    public List<Planos> buscarPorEmpresa(Empresa e) throws ServiceException {
        try {
            this.map = new HashMap<>();
            this.map.put(Planos_.idEmpresa.getName(), e);
            return this.getDao().findbyAttributes(this.map, Planos.class);
        } catch (DaoException ex) {
            throw new ServiceException(ex);
        }
    }

}
