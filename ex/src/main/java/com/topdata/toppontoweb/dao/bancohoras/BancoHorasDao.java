package com.topdata.toppontoweb.dao.bancohoras;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.topdata.toppontoweb.dao.Dao;
import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.entity.bancohoras.BancoHoras;
import com.topdata.toppontoweb.entity.funcionario.Funcionario;

/**
 *
 * @author tharle.camargo
 */
@Repository
public class BancoHorasDao extends Dao<BancoHoras, Integer> {

//    @Transactional(readOnly = true)
    public BancoHoras find(Integer idBancoHoras) throws DaoException {
        try {
            Query query = this.getEntityManager().createQuery("SELECT DISTINCT bh FROM BancoHoras bh WHERE bh.idBancoHoras =:idBancoHoras");
            query.setParameter("idBancoHoras", idBancoHoras);
            return (BancoHoras) query.getSingleResult();

        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getSimpleName()).log(Level.SEVERE, "ERRO BUSCAR : {0}", "Calendario");
            throw new DaoException(ex);
        }
    }

    /**
     * Consulta na base os banco de horas que o funcionario possui
     *
     * @param funcionario
     * @return
     * @throws DaoException
     */
    @Transactional(readOnly = true)
    public List<BancoHoras> buscarPorFuncionario(Funcionario funcionario) throws DaoException {
        try {
            Query query = this.getEntityManager().createQuery("SELECT B FROM BancoHoras B INNER JOIN B.funcionarioBancoHorasList FB WHERE FB.funcionario = :funcionario");
            query.setParameter("funcionario", funcionario);
            return query.getResultList();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

}
