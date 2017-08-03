package com.topdata.toppontoweb.dao.funcionario;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.topdata.toppontoweb.dao.Dao;
import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.entity.funcionario.Coletivo;
import com.topdata.toppontoweb.entity.funcionario.Compensacao;

/**
 *
 * @author tharle.camargo
 */
@Repository
public class CompensacaoDao extends Dao<Compensacao, Object> {

    public CompensacaoDao() {
        super(Compensacao.class);
    }

    @Transactional
    public int excluirEntidade(Compensacao af) {
        Query tq = this.getEntityManager()
                .createQuery("DELETE FROM Compensacao AS af WHERE af.idCompensacao = :id");
        tq.setParameter("id", af.getIdCompensacao());

        return tq.executeUpdate();
    }

    @Transactional(readOnly = true)
    public List<Compensacao> buscarPorPeriodoEntidade(Compensacao comp) {
        Query tq = this.getEntityManager()
                .createQuery("SELECT comp FROM Compensacao AS comp WHERE comp.funcionario.idFuncionario = :idFuncionario  AND ((:dataInicio BETWEEN comp.dataInicio AND comp.dataFim) OR (:dataFim BETWEEN comp.dataInicio AND comp.dataFim) OR (:dataInicio <= comp.dataInicio AND :dataFim >= comp.dataInicio) ) ");
        tq.setParameter("idFuncionario", comp.getFuncionario().getIdFuncionario());
        tq.setParameter("dataInicio", comp.getDataInicio());
        tq.setParameter("dataFim", comp.getDataFim());

        return tq.getResultList();
    }

    @Transactional(readOnly = true)
    public List<Compensacao> buscarPorPeriodoDataCompensadaEntidade(Compensacao comp) {
        Query tq = this.getEntityManager()
                .createQuery("SELECT comp FROM Compensacao AS comp WHERE comp.funcionario.idFuncionario = :idFuncionario  AND (:dataCompensada BETWEEN comp.dataInicio AND comp.dataFim) ");
        tq.setParameter("idFuncionario", comp.getFuncionario().getIdFuncionario());
        tq.setParameter("dataCompensada", comp.getDataCompensada());

        return tq.getResultList();
    }

    @Transactional(readOnly = true)
    public List<Compensacao> buscarPorDataCompensadaEmUsoEntidade(Compensacao comp) {
        Query tq = this.getEntityManager()
                .createQuery("SELECT comp FROM Compensacao AS comp WHERE comp.funcionario.idFuncionario = :idFuncionario  AND (:dataCompensada = comp.dataCompensada) ");
        tq.setParameter("idFuncionario", comp.getFuncionario().getIdFuncionario());
        tq.setParameter("dataCompensada", comp.getDataCompensada());

        return tq.getResultList();
    }

    @Transactional
    public void excluirPorColetivo(Coletivo c) throws DaoException {
        try {
            Query query = this.getEntityManager().createQuery("DELETE FROM Compensacao AS comp WHERE comp.coletivo.idColetivo = :idColetivo");

            query.setParameter("idColetivo", c.getIdColetivo());

            query.executeUpdate();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }
}
