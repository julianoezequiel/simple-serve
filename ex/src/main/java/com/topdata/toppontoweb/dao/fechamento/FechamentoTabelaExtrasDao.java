package com.topdata.toppontoweb.dao.fechamento;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.topdata.toppontoweb.dao.Dao;
import com.topdata.toppontoweb.entity.fechamento.EmpresaFechamentoData;
import com.topdata.toppontoweb.entity.fechamento.FechamentoTabelaExtras;
import com.topdata.toppontoweb.services.ServiceException;

/**
 *
 * @author juliano.ezequiel
 */
@Repository
public class FechamentoTabelaExtrasDao extends Dao<FechamentoTabelaExtras, Integer> {

    @Transactional
    public long excluirPorEmpresaFechamentoData(EmpresaFechamentoData efd) throws ServiceException {
        try {
            Query query;
            query = this.getEntityManager().createQuery("DELETE FROM FechamentoTabelaExtrasDSR fta WHERE fta.idEmpresaFechamentoData =:efd");
            query.setParameter("efd", efd);
            return query.executeUpdate();
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

}
