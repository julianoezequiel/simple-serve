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

/**
 *
 * @author tharle.camargo
 */
@Repository
public class CalendarioDao extends Dao<Calendario, Integer> {

    @Transactional(readOnly = true)
    public List<Calendario> findAll() throws DaoException {
        try {
            String sql = "SELECT DISTINCT cal "
                    + "FROM   Calendario cal  "
                    + "";

            Query query = this.getEntityManager().createQuery(sql);

            return query.getResultList();

        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getSimpleName()).log(Level.SEVERE, "ERRO BUSCAR : {0}", "Calendario");
            throw new DaoException(ex);
        }
    }

    @Transactional(readOnly = true)
    public Calendario find(Integer idCalendario) throws DaoException {
        try {
            String sql = "SELECT DISTINCT cal "
                    + "FROM   Calendario cal  "
                    + "WHERE cal.idCalendario = :idCalendario";

            Query query = this.getEntityManager().createQuery(sql);
            query.setParameter("idCalendario", idCalendario);

            List list = query.getResultList();

            return list.size() > 0 ? (Calendario) list.get(0) : null;

        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getSimpleName()).log(Level.SEVERE, "ERRO BUSCAR : {0}", "Calendario");
            throw new DaoException(ex);
        }
    }

}
