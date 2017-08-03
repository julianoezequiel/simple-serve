package com.topdata.toppontoweb.dao.funcionario;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.topdata.toppontoweb.dao.Dao;
import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.entity.funcionario.Afastamento;
import com.topdata.toppontoweb.entity.funcionario.Coletivo;

/**
 *
 * @author tharle.camargo
 */
@Repository
public class AfastamentoDao extends Dao<Afastamento, Object> {

    public AfastamentoDao() {
        super(Afastamento.class);
    }

    @Transactional
    public int excluirEntidade(Afastamento af) {
        Query tq = this.getEntityManager()
                .createQuery("DELETE FROM Afastamento AS af WHERE af.idAfastamento = :idAfastamento");
        tq.setParameter("idAfastamento", af.getIdAfastamento());

        return tq.executeUpdate();
    }

    @Transactional(readOnly = true)
    public List<Afastamento> buscarPorPeriodoEntidade(Afastamento af) {
        Query tq = this.getEntityManager()
                .createQuery("SELECT af FROM Afastamento AS af WHERE af.funcionario.idFuncionario = :idFuncionario  AND ((:dataInicio BETWEEN af.dataInicio AND af.dataFim) OR (:dataFim BETWEEN af.dataInicio AND af.dataFim) OR (:dataInicio <= af.dataInicio AND :dataFim >= af.dataInicio) ) ");
        tq.setParameter("idFuncionario", af.getFuncionario().getIdFuncionario());
        tq.setParameter("dataInicio", af.getDataInicio());
        tq.setParameter("dataFim", af.getDataFim());

        return tq.getResultList();
    }

    @Transactional
    public void excluirPorColetivo(Coletivo c) throws DaoException {
        try {
            Query query = this.getEntityManager().createQuery("DELETE FROM Afastamento AS af WHERE af.coletivo.idColetivo = :idColetivo");

            query.setParameter("idColetivo", c.getIdColetivo());

            query.executeUpdate();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

//    @Transactional(readOnly = true)
    public List<Afastamento> buscarPorFuncionarioEPeriodo(Integer idFuncionario, Date dataInicio, Date dataTermino) throws DaoException {

        String sql = "SELECT DISTINCT af "
                + "FROM   Afastamento af INNER JOIN af.funcionario f  "
                + " WHERE f.idFuncionario IN (:idFuncionario) "
                + " AND (af.dataInicio BETWEEN  :periodoInicio AND :periodoFim) ";

        Query query = this.getEntityManager().createQuery(sql);
        query.setParameter("idFuncionario", idFuncionario);
        query.setParameter("periodoInicio", dataInicio);
        query.setParameter("periodoFim", dataTermino);

        return query.getResultList();

    }
}
