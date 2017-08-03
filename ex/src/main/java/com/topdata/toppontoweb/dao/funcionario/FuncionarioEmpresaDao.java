package com.topdata.toppontoweb.dao.funcionario;

import java.util.List;

import javax.persistence.TypedQuery;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.topdata.toppontoweb.dao.Dao;
import com.topdata.toppontoweb.entity.funcionario.Funcionario;
import com.topdata.toppontoweb.entity.funcionario.FuncionarioEmpresa;
import org.springframework.transaction.annotation.Transactional;

/**
 * @version 1.0.4 data 24/08/2016
 * @since 1.0.4 data 24/08/2016
 *
 * @author juliano.ezequiel
 */
@Repository
public class FuncionarioEmpresaDao extends Dao<FuncionarioEmpresa, Integer> {

    public FuncionarioEmpresaDao() {
        super(FuncionarioEmpresa.class);
    }
    
    @Transactional(readOnly = true)
    public FuncionarioEmpresa buscaUltimaEmpresa(Funcionario funcionario) {

        TypedQuery<FuncionarioEmpresa> tq
                = this.getEntityManager()
                .createQuery("SELECT f FROM FuncionarioEmpresa f "
                        + "WHERE f.funcionario.idFuncionario = :idFuncionario "
                        + "ORDER BY f.funcionarioEmpresaPK.dataEmpresa DESC",
                        FuncionarioEmpresa.class)
                .setParameter("idFuncionario", funcionario.getIdFuncionario())
                .setMaxResults(1);

        List<FuncionarioEmpresa> list = tq.getResultList();

        return CollectionUtils.isEmpty(list) ? null : list.get(0);
    }

    @Transactional(readOnly = true)
    public Long total(Funcionario funcionario) {

        TypedQuery<Long> tq
                = this.getEntityManager()
                .createQuery("SELECT COUNT(f) FROM FuncionarioEmpresa f "
                        + "WHERE f.funcionario.idFuncionario = :idFuncionario",
                        Long.class)
                .setParameter("idFuncionario", funcionario.getIdFuncionario());

        Long total = tq.getSingleResult();

        return total;
    }
}
