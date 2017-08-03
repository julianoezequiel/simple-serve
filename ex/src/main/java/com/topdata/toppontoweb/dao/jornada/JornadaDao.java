package com.topdata.toppontoweb.dao.jornada;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.topdata.toppontoweb.dao.Dao;
import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.entity.funcionario.Funcionario;
import com.topdata.toppontoweb.entity.jornada.Jornada;

/**
 *
 * @author juliano.ezequiel
 */
@Repository
public class JornadaDao extends Dao<Jornada, Integer> {

    @Transactional(readOnly = true)
    public List<Jornada> buscarEntreDatasByFuncionario(Funcionario funcionario, Date dataInicio, Date dataFim) throws DaoException {
        try {

            Query query
                    = this.getEntityManager()
                    .createQuery("SELECT DISTINCT j FROM FuncionarioJornada fj INNER JOIN fj.jornada j WHERE fj.funcionario = :funcionario AND ((fj.dataInicio BETWEEN :dataInicio AND :dataFim) OR (fj.dataFim BETWEEN :dataInicio AND :dataFim) OR (fj.dataInicio >= :dataInicio AND (fj.dataFim <= :dataInicio OR fj.dataFim IS NULL ) OR (fj.dataInicio <= :dataInicio AND (fj.dataFim >= :dataInicio OR fj.dataFim IS NULL))))");
            query.setParameter("funcionario", funcionario);
            query.setParameter("dataInicio", dataInicio);
            query.setParameter("dataFim", dataFim);

            return query.getResultList();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Transactional(readOnly = true)
    public List<Jornada> buscarByFuncionario(Funcionario funcionario) throws DaoException {
        try {

            Query query = this.getEntityManager().createQuery("SELECT DISTINCT j FROM FuncionarioJornada fj INNER JOIN fj.jornada j WHERE fj.funcionario = :funcionario");
            query.setParameter("funcionario", funcionario);

            return query.getResultList();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    public Jornada buscarPorId(Integer idJornada) throws DaoException {
        try {

            Query query = this.getEntityManager().createQuery("SELECT DISTINCT j FROM Jornada j WHERE j.idJornada = :idJornada");
            query.setParameter("idJornada", idJornada);
            List<Jornada> jornadaList = query.getResultList();
            return jornadaList.size() > 0? jornadaList.get(0) : null;
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

}
