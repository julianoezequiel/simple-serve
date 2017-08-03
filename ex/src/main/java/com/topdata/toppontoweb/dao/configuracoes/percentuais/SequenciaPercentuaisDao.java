package com.topdata.toppontoweb.dao.configuracoes.percentuais;

import com.topdata.toppontoweb.dao.Dao;
import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.entity.configuracoes.SequenciaPercentuais;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author tharle.camargo
 */
@Repository
public class SequenciaPercentuaisDao extends Dao<SequenciaPercentuais, Integer> {

//    @Transactional(readOnly = true)
    public List<SequenciaPercentuais> findByPercentuaisAcrescimo(Integer idPercentuaisAcrescimo) throws DaoException {
        try {
            String sql = "SELECT DISTINCT seq "
                    + " FROM   SequenciaPercentuais seq  "
                    + " WHERE seq.percentuaisAcrescimo.idPercentuaisAcrescimo = :idPercentualAcrescimo ";

            Query query = this.getEntityManager().createQuery(sql);
            query.setParameter("idPercentualAcrescimo", idPercentuaisAcrescimo);

            return query.getResultList();

        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getSimpleName()).log(Level.SEVERE, "ERRO BUSCAR : {0}", "Calendario");
            throw new DaoException(ex);
        }
    }

}
