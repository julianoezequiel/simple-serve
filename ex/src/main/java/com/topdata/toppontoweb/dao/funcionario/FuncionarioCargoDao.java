package com.topdata.toppontoweb.dao.funcionario;

import java.util.List;

import javax.persistence.TypedQuery;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.topdata.toppontoweb.dao.Dao;
import com.topdata.toppontoweb.entity.funcionario.Funcionario;
import com.topdata.toppontoweb.entity.funcionario.HistoricoFuncionarioCargo;
import org.springframework.transaction.annotation.Transactional;

/**
 * @version 1.0.4 data 24/08/2016
 * @since 1.0.4 data 24/08/2016
 *
 * @author juliano.ezequiel
 */
@Repository
public class FuncionarioCargoDao extends Dao<HistoricoFuncionarioCargo, Object> {

    public FuncionarioCargoDao() {
        super(HistoricoFuncionarioCargo.class);
    }

    @Transactional(readOnly = true)
    public HistoricoFuncionarioCargo buscaUltimoCargo(Funcionario funcionario) {

        TypedQuery<HistoricoFuncionarioCargo> tq
                = this.getEntityManager()
                .createQuery("SELECT f FROM FuncionarioCargo f WHERE "
                        + "f.funcionarioCargoPK.idFuncionario = :idFuncionario "
                        + "ORDER BY f.funcionarioCargoPK.dataCargo DESC",
                        HistoricoFuncionarioCargo.class)
                .setParameter("idFuncionario", funcionario.getIdFuncionario())
                .setMaxResults(1);

        List<HistoricoFuncionarioCargo> list = tq.getResultList();

        return CollectionUtils.isEmpty(list) ? null : list.get(0);
    }

    @Transactional(readOnly = true)
    public Long total(Funcionario funcionario) {

        TypedQuery<Long> tq
                = this.getEntityManager()
                .createQuery("SELECT COUNT(f) FROM FuncionarioCargo f WHERE "
                        + "f.funcionarioCargoPK.idFuncionario = :idFuncionario ",
                        Long.class)
                .setParameter("idFuncionario", funcionario.getIdFuncionario());

        Long total = tq.getSingleResult();

        return total;
    }
}
