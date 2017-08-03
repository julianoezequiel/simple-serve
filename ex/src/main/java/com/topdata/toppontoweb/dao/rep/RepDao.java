package com.topdata.toppontoweb.dao.rep;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.topdata.toppontoweb.dao.Dao;
import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.entity.empresa.Empresa;
import com.topdata.toppontoweb.entity.rep.Rep;

/**
 *
 * @author tharle.camargo
 */
@Repository
public class RepDao extends Dao<Rep, Object> {

    public RepDao() {
        super(Rep.class);
    }

    @Transactional(readOnly = true)
    public Rep buscarPorNumeroRep(Integer numeroRep, Empresa empresa) throws DaoException {
        try {
            Query tq = this.getEntityManager()
                    .createQuery("SELECT DISTINCT r FROM Rep r WHERE r.numeroRep = :numeroRep AND r.empresa = :empresa");

            tq.setParameter("numeroRep", numeroRep);
            tq.setParameter("empresa", empresa);
            List<Rep> repList = tq.getResultList();
            return !repList.isEmpty() ? repList.get(0) : null;
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getSimpleName()).log(Level.SEVERE, "ERRO BUSCAR : {0}", "REP");
            throw new DaoException(ex);
        }
    }

    /**
     * Busca todos os reps diferentes do rep atual que possuam o mesmo numero
     * rep na mesma empresa
     *
     * @param rep
     * @return
     * @throws com.topdata.toppontoweb.dao.DaoException
     */
    @Transactional(readOnly = true)
    public List<Rep> buscarDiferentesPorNumeroRep(Rep rep) throws DaoException {
        try {
            String strQuery = "SELECT DISTINCT r FROM Rep r WHERE r.numeroRep = :numeroRep AND r.empresa = :empresa ";
            if (rep.getIdRep() != null) {
                strQuery += " AND r.idRep != :idRep";
            }

            Query tq = this.getEntityManager()
                    .createQuery(strQuery);

            tq.setParameter("numeroRep", rep.getNumeroRep());
            tq.setParameter("empresa", rep.getEmpresa());

            if (rep.getIdRep() != null) {
                tq.setParameter("idRep", rep.getIdRep());
            }

            return tq.getResultList();
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getSimpleName()).log(Level.SEVERE, "ERRO BUSCAR : {0}", "REP");
            throw new DaoException(ex);
        }
    }

    @Transactional(readOnly = true)
    public Rep buscarPorSerieCompletaEmpresa(Empresa empresa, String numeroFabricante, String numeroModelo, String numeroSerie) throws DaoException {
        try {

            Query tq = this.getEntityManager()
                    .createQuery("SELECT DISTINCT r FROM Rep r WHERE r.numeroFabricante = :numeroFabricante AND r.numeroModelo = :numeroModelo AND  r.numeroSerie = :numeroSerie AND r.empresa = :empresa ");

            tq.setParameter("numeroFabricante", numeroFabricante);
            tq.setParameter("numeroModelo", numeroModelo);
            tq.setParameter("numeroSerie", numeroSerie);
            tq.setParameter("empresa", empresa);

            List<Rep> repList = tq.getResultList();
            return !repList.isEmpty() ? repList.get(0) : null;
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getSimpleName()).log(Level.SEVERE, "ERRO BUSCAR : {0}", "REP");
            throw new DaoException(ex);
        }
    }

    @Transactional(readOnly = false)
    public int atualizarNSR(Integer idRep, String nsr) throws DaoException {
        try {
//            Query tq = this.getEntityManager().createQuery( "UPDATE Rep r SET r.nsr = :nsr WHERE r.idRep = :idRep");
            Query tq = this.getEntityManager().createNativeQuery("UPDATE Rep SET nsr = :nsr WHERE idRep = :idRep");

            tq.setParameter("nsr", nsr);
            tq.setParameter("idRep", idRep);

            return tq.executeUpdate();
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getSimpleName()).log(Level.SEVERE, "ERRO BUSCAR : {0}", "REP");
            throw new DaoException(ex);
        }
    }

}
