package com.topdata.toppontoweb.dao.calendario;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.topdata.toppontoweb.dao.Dao;
import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.entity.calendario.Calendario;
import com.topdata.toppontoweb.entity.calendario.CalendarioFeriado;

/**
 *
 * @author tharle.camargo
 */
@Repository
public class CalendarioFeriadoDao extends Dao<CalendarioFeriado, Integer> {

    @Transactional(readOnly = true)
    public List<CalendarioFeriado> findAllByCalendario(Calendario calendario) throws DaoException {
        try {
            String sql = "SELECT DISTINCT cf "
                    + " FROM   CalendarioFeriado cf "
                    + " WHERE cf.calendario.idCalendario = :idCalendario";

            Query query = this.getEntityManager().createQuery(sql);
            query.setParameter("idCalendario", calendario.getIdCalendario());

            return query.getResultList();

        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getSimpleName()).log(Level.SEVERE, "ERRO BUSCAR : {0}", "CalendarioFeriado");
            throw new DaoException(ex);
        }
    }

}
