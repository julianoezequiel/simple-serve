package com.topdata.toppontoweb.dao.funcionario;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.topdata.toppontoweb.dao.Dao;
import com.topdata.toppontoweb.entity.funcionario.Abono;
import com.topdata.toppontoweb.entity.funcionario.Funcionario;

/**
 *
 * @author juliano.ezequiel
 */
@Repository
public class AbonoDao extends Dao<Abono, Integer> {

//    @Transactional(readOnly = true)
    public List<Abono> buscarPorFuncionarioPeriodo(Funcionario funcionario, Date dataInicio, Date dataFim) {

        Query query = this.getEntityManager().createQuery("SELECT DISTINCT a FROM Abono a INNER JOIN a.funcionario af WHERE af = :funcionario AND ((a.data BETWEEN :dataInicio AND :dataFim))");
        query.setParameter("funcionario", funcionario);
        query.setParameter("dataInicio", dataInicio);
        query.setParameter("dataFim", dataFim);

        return query.getResultList();
    }
}
