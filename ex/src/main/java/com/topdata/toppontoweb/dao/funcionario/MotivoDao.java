package com.topdata.toppontoweb.dao.funcionario;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.topdata.toppontoweb.dao.Dao;
import com.topdata.toppontoweb.entity.funcionario.Motivo;
import javax.persistence.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * @version 1.0.5 data 06/09/2016
 * @since 1.0.5 data 06/09/2016
 *
 * @author juliano.ezequiel
 */
@Repository
public class MotivoDao extends Dao<Motivo, Integer> {

    public MotivoDao() {
        super(Motivo.class);
    }

    /**
     * Lista o motivos que também são utilizados como eventos
     *
     * @return
     */
    @Transactional(readOnly = true)
    public List<Motivo> buscarMotivoEventos() {
        Query query = this.getEntityManager().createQuery("SELECT m FROM Motivo m WHERE m.idTipoMotivo.idTipoMotivo = 2 OR m.idTipoMotivo.idTipoMotivo = 3");
        return query.getResultList();
    }

//    afastamento e abono;
}
