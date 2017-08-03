package com.topdata.toppontoweb.dao.funcionario;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.topdata.toppontoweb.dao.Dao;
import com.topdata.toppontoweb.entity.funcionario.Funcionario;
import com.topdata.toppontoweb.entity.funcionario.FuncionarioDiaNaoDescontaDSR;
import java.util.Date;
import javax.persistence.TemporalType;

/**
 *
 * @author juliano.ezequiel
 */
@Repository
public class FuncionarioDiaNaoDescontaDSRDao extends Dao<FuncionarioDiaNaoDescontaDSR, Integer> {

//    @Transactional(readOnly = true)
    public List<FuncionarioDiaNaoDescontaDSR> buscarPorFuncionario(Funcionario funcionario) {
        Query query = this.getEntityManager().createQuery("SELECT DISTINCT fd FROM FuncionarioDiaNaoDescontaDSR fd WHERE fd.idFuncionario = :funcionario");
        query.setParameter("funcionario", funcionario);

        return query.getResultList();
    }

    public List<FuncionarioDiaNaoDescontaDSR> buscarPorFuncionarioData(Funcionario funcionario, Date data) {
        Query query = this.getEntityManager().createQuery("SELECT DISTINCT fd FROM FuncionarioDiaNaoDescontaDSR fd WHERE fd.idFuncionario = :funcionario AND fd.data = :data");
        query.setParameter("funcionario", funcionario);
        query.setParameter("data", data, TemporalType.DATE);

        return query.getResultList();
    }

}
