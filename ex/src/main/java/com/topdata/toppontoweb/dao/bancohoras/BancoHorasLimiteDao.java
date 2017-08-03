package com.topdata.toppontoweb.dao.bancohoras;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.topdata.toppontoweb.dao.Dao;
import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.entity.bancohoras.BancoHorasLimite;

/**
 *
 * @author tharle.camargo
 */
@Repository
public class BancoHorasLimiteDao extends Dao<BancoHorasLimite, Integer> {
    
    public List<BancoHorasLimite> findAllByIdBancoHoras(Integer idBancoHoras) throws DaoException {
        try {
            String sql = "SELECT DISTINCT bhl "
                    + " FROM   BancoHorasLimite bhl  "
                    + " where bhl.bancoHoras.idBancoHoras = :idBancoHoras";

            Query query = this.getEntityManager().createQuery(sql);
            query.setParameter("idBancoHoras", idBancoHoras);

            return query.getResultList();

        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getSimpleName()).log(Level.SEVERE, "ERRO BUSCAR : {0}", "Calendario");
            throw new DaoException(ex);
        }
    }
}
