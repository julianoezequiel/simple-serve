package com.topdata.toppontoweb.services.funcionario.motivo;

//<editor-fold defaultstate="collapsed" desc="IMPORTS">
import java.util.HashMap;

import org.springframework.stereotype.Service;

import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.entity.tipo.TipoMotivo;
import com.topdata.toppontoweb.entity.tipo.TipoMotivo_;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoService;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;
//</editor-fold>

/**
 * @version 1.0.5 data 05/09/2016
 * @since 1.0.5 data 05/09/2016
 *
 * @author juliano.ezequiel
 */
@Service
public class TipoMotivoService extends TopPontoService<TipoMotivo, Object> {

    private HashMap<String, Object> map;

    public TipoMotivo buscaPorDescricao(String descricao) throws ServiceException {
        try {
            this.map = new HashMap<>();
            this.map.put(TipoMotivo_.descricao.getName(), descricao);
            return (TipoMotivo) this.getDao().findOnebyAttributes(this.map, TipoMotivo.class);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroBuscar(), ex);
        }
    }

    public TipoMotivo cartao() throws ServiceException {
        try {
            return (TipoMotivo) this.getDao().find(TipoMotivo.class, CONSTANTES.Enum_TIPO_MOTIVO.CARTAO.ordinal());
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroBuscar(), ex);
        }
    }

    public TipoMotivo afastamento() throws ServiceException {
        try {
            return (TipoMotivo) this.getDao().find(TipoMotivo.class, CONSTANTES.Enum_TIPO_MOTIVO.AFASTAMENTO.ordinal());
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroBuscar(), ex);
        }
    }

    public TipoMotivo compensacao() throws ServiceException {
        try {
            return (TipoMotivo) this.getDao().find(TipoMotivo.class, CONSTANTES.Enum_TIPO_MOTIVO.COMPENSACAO.ordinal());
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroBuscar(), ex);
        }
    }
}
