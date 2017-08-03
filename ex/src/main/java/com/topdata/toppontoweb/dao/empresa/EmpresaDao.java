package com.topdata.toppontoweb.dao.empresa;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.topdata.toppontoweb.dao.Dao;
import static com.topdata.toppontoweb.dao.Dao.LOGGER;
import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.entity.empresa.Empresa;
import com.topdata.toppontoweb.entity.funcionario.Funcionario;

/**
 *
 * @author tharle.camargo
 */
@Repository
public class EmpresaDao extends Dao<Empresa, Object> {
    
    @Transactional(readOnly = true)
    public Empresa buscarPorFuncionario(Funcionario funcionario) throws DaoException {
        try {
            Query query = this.getEntityManager().createQuery("SELECT DISTINCT emp  FROM Empresa emp INNER JOIN emp.departamentoList dep INNER JOIN dep.funcionarioList f WHERE f  = :funcionario");
            query.setParameter("funcionario", funcionario);
            List<Empresa> empresaList = query.getResultList();
            return empresaList.isEmpty() ? null : empresaList.get(0);
        } catch (Exception e) {
            LOGGER.error(this.getClass().getSimpleName(), e);
            throw new DaoException(e);
        }
    }
    
}
