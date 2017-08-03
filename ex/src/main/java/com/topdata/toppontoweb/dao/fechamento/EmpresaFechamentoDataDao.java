package com.topdata.toppontoweb.dao.fechamento;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.topdata.toppontoweb.dao.Dao;
import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.entity.fechamento.EmpresaFechamentoData;
import com.topdata.toppontoweb.entity.fechamento.EmpresaFechamentoPeriodo;
import com.topdata.toppontoweb.entity.funcionario.Funcionario;

/**
 *
 * @author juliano.ezequiel
 */
@Repository
public class EmpresaFechamentoDataDao extends Dao<EmpresaFechamentoData, Integer> {

    public EmpresaFechamentoDataDao() {
        super(EmpresaFechamentoData.class);
    }

    /**
     * Consulta as EmpresaFechamentoData pelo funcionário e pelo período
     * informado
     *
     * @param funcionario
     * @param dataInicio
     * @param dataFim
     * @return Lista com EmpresaFechamentoData
     * @throws DaoException
     */
    @Transactional(readOnly = true)
    public List<EmpresaFechamentoData> buscarPorPeriodoFuncionario(Funcionario funcionario, Date dataInicio, Date dataFim) throws DaoException {
        try {
            Query query;
            query = this.entityManager.createQuery("SELECT f FROM EmpresaFechamentoData f INNER JOIN f.idEmpresaFechamentoPeriodo fp WHERE f.idFuncionario = :funcionario and (fp.inicio BETWEEN :dataInicio AND :dataFim OR fp.termino BETWEEN :dataInicio AND :dataFim)");

            query.setParameter("funcionario", funcionario);
            query.setParameter("dataInicio", dataInicio);
            query.setParameter("dataFim", dataFim);

            return query.getResultList();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    /**
     * Exclui uma EmpresaFechamentoData pelo seu ID, Exclui diretamente via
     * query
     *
     * @param id
     * @return long com o total de registros excluidos
     * @throws DaoException
     */
    @Transactional
    public long excluirPorId(Integer id) throws DaoException {
        try {
            Query query;
            query = this.getEntityManager().createQuery("DELETE FROM EmpresaFechamentoData f WHERE f.idEmpresaFechamentoData = :id");
            query.setParameter("id", id);
            return query.executeUpdate();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    /**
     * Exclui EmpresaFechamentoData pela EmpresaFechamentoPeriodo
     *
     * @param empresaFechamentoPeriodo
     * @return
     * @throws DaoException
     */
    @Transactional
    public long excluirPorEmpresaFechamentoPeriodo(EmpresaFechamentoPeriodo empresaFechamentoPeriodo) throws DaoException {
        try {
            Query query;
            query = this.getEntityManager().createQuery("DELETE FROM EmpresaFechamentoData f WHERE f.idEmpresaFechamentoPeriodo = :empresaFechamentoPeriodo");
            query.setParameter("empresaFechamentoPeriodo", empresaFechamentoPeriodo);
            return query.executeUpdate();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    /**
     * Consulta as EmpresaFechamentoData pelo EmpresaFechamentoPeriodo
     *
     * @param efp
     * @return Lista com EmpresaFechamentoData
     * @throws DaoException
     */
    @Transactional(readOnly = true)
    public List<EmpresaFechamentoData> buscarPorEmpresaFechamentoPeriodo(EmpresaFechamentoPeriodo efp) throws DaoException {
        try {
            Query query;
            query = this.entityManager.createQuery("SELECT f FROM EmpresaFechamentoData f INNER JOIN f.idEmpresaFechamentoPeriodo fp WHERE f.idEmpresaFechamentoPeriodo = :empresaFechamentoPeriodo");

            query.setParameter("empresaFechamentoPeriodo", efp);

            return query.getResultList();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

}
