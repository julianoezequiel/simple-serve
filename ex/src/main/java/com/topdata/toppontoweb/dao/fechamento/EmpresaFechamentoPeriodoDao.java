package com.topdata.toppontoweb.dao.fechamento;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.topdata.toppontoweb.dao.Dao;
import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.entity.empresa.Empresa;
import com.topdata.toppontoweb.entity.fechamento.EmpresaFechamentoPeriodo;

/**
 *
 * @author juliano.ezequiel
 */
@Repository
public class EmpresaFechamentoPeriodoDao extends Dao<EmpresaFechamentoPeriodo, Object> {
    
    @Transactional(readOnly = true)
    public List<EmpresaFechamentoPeriodo> buscarPorPeriodoEmpresa(Empresa empresa, Date dataInicio, Date dataFim) throws DaoException {
        try {
            Query query;
            query = this.entityManager.createQuery("SELECT f FROM EmpresaFechamentoPeriodo f WHERE f.idEmpresa = :empresa and ( (f.inicio BETWEEN :dataInicio AND :dataFim OR f.termino BETWEEN :dataInicio AND :dataFim) OR (:dataInicio  BETWEEN f.inicio AND f.termino OR :dataFim  BETWEEN f.inicio AND f.termino) ) ");

            query.setParameter("empresa", empresa);
            query.setParameter("dataInicio", dataInicio);
            query.setParameter("dataFim", dataFim);

            return query.getResultList();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Transactional
    public long excluirPorId(Integer id) throws DaoException {
        try {
            Query query;
            query = this.getEntityManager().createQuery("DELETE FROM EmpresaFechamentoPeriodo f WHERE f.idEmpresaFechamentoPeriodo = :id");
            query.setParameter("id", id);
            return query.executeUpdate();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

}
