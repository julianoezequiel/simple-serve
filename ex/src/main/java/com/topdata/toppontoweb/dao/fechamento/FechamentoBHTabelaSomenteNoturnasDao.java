package com.topdata.toppontoweb.dao.fechamento;

import com.topdata.toppontoweb.dao.Dao;
import com.topdata.toppontoweb.entity.fechamento.EmpresaFechamentoData;
import com.topdata.toppontoweb.entity.fechamento.FechamentoBHTabelaSomenteNoturnas;
import com.topdata.toppontoweb.services.ServiceException;
import javax.persistence.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author juliano.ezequiel
 */
@Repository
public class FechamentoBHTabelaSomenteNoturnasDao extends Dao<FechamentoBHTabelaSomenteNoturnas, Integer> {

    @Transactional
    public long excluirPorEmpresaFechamentoData(EmpresaFechamentoData efd) throws ServiceException {
        try {
            Query query;
            query = this.getEntityManager().createQuery("DELETE FROM FechamentoBHTabelaSomenteNoturnas fta WHERE fta.idEmpresaFechamentoData =:efd");
            query.setParameter("efd", efd);
            return query.executeUpdate();
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

}
