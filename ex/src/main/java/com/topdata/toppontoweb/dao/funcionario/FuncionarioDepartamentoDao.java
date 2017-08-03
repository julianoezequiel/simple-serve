package com.topdata.toppontoweb.dao.funcionario;

import java.util.List;

import javax.persistence.TypedQuery;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.topdata.toppontoweb.dao.Dao;
import com.topdata.toppontoweb.entity.funcionario.Funcionario;
import com.topdata.toppontoweb.entity.funcionario.FuncionarioDepartamento;
import org.springframework.transaction.annotation.Transactional;

/**
 * @version 1.0.4 data 24/08/2016
 * @since 1.0.4 data 24/08/2016
 *
 * @author juliano.ezequiel
 */
@Repository
public class FuncionarioDepartamentoDao extends Dao<FuncionarioDepartamento, Integer> {

    public FuncionarioDepartamentoDao() {
        super(FuncionarioDepartamento.class);
    }

    @Transactional(readOnly = true)
    public FuncionarioDepartamento buscaUltimoDepartamento(Funcionario funcionario) {

        TypedQuery<FuncionarioDepartamento> tq
                = this.getEntityManager()
                .createQuery("SELECT f FROM FuncionarioDepartamento f WHERE "
                        + "f.funcionarioDepartamentoPK.idFuncionario = :idFuncionario "
                        + "ORDER BY f.funcionarioDepartamentoPK.dataDepartamento DESC",
                        FuncionarioDepartamento.class)
                .setParameter("idFuncionario", funcionario.getIdFuncionario())
                .setMaxResults(1);

        List<FuncionarioDepartamento> list = tq.getResultList();

        return CollectionUtils.isEmpty(list) ? null : list.get(0);
    }

    @Transactional(readOnly = true)
    public Long total(Funcionario funcionario) {

        TypedQuery<Long> tq
                = this.getEntityManager()
                .createQuery("SELECT COUNT(f) FROM FuncionarioDepartamento f WHERE "
                        + "f.funcionarioDepartamentoPK.idFuncionario = :idFuncionario",
                        Long.class)
                .setParameter("idFuncionario", funcionario.getIdFuncionario());

        Long total = tq.getSingleResult();

        return total;
    }
}
