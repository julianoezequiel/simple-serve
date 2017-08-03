package com.topdata.toppontoweb.dao.configuracoes.percentuais;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.topdata.toppontoweb.dao.Dao;
import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.entity.configuracoes.PercentuaisAcrescimo;

/**
 *
 * @author tharle.camargo
 */
@Repository
public class PercentuaisAcrescimoDao  extends Dao<PercentuaisAcrescimo, Integer> {
    
//    @Transactional(readOnly = true)
    public PercentuaisAcrescimo findByBancoHoras(Integer idBancoHoras) throws DaoException {
        try {
            String sql = "SELECT DISTINCT perc "
                    + " FROM   PercentuaisAcrescimo perc INNER JOIN perc.bancoHorasList bh  "
                    + " WHERE bh.idBancoHoras = :idBancoHoras ";

            Query query = this.getEntityManager().createQuery(sql);
            query.setParameter("idBancoHoras", idBancoHoras);
            List<PercentuaisAcrescimo> resultList = query.getResultList();
            return resultList.size() > 0? resultList.get(0) : null;

        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getSimpleName()).log(Level.SEVERE, "ERRO BUSCAR : {0}", "Calendario");
            throw new DaoException(ex);
        }
    }
    
}
