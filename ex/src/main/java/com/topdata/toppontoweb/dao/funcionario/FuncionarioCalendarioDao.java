package com.topdata.toppontoweb.dao.funcionario;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.topdata.toppontoweb.dao.Dao;
import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.entity.funcionario.Coletivo;
import com.topdata.toppontoweb.entity.funcionario.Funcionario;
import com.topdata.toppontoweb.entity.funcionario.FuncionarioCalendario;

/**
 * @version 1.0.0.0 data 18/01/2017
 * @since 1.0.0.0 data 29/09/2016
 *
 * @author juliano.ezequiel
 */
@Repository
public class FuncionarioCalendarioDao extends Dao<FuncionarioCalendario, Object> {

    @Transactional
    public void excluirPorColetivo(Coletivo c) throws DaoException {
        try {
            Query query = this.getEntityManager().createQuery("DELETE FROM FuncionarioCalendario AS fb WHERE fb.coletivo = :idColetivo");

            query.setParameter("idColetivo", c);

            query.executeUpdate();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Transactional
    public int excluirEntidade(FuncionarioCalendario entidade) {
        Query tq = this.getEntityManager()
                .createQuery("DELETE FROM FuncionarioCalendario AS af WHERE af.idFuncionarioCalendario = :id");
        tq.setParameter("id", entidade.getIdFuncionarioCalendario());

        return tq.executeUpdate();
    }

//    @Transactional(readOnly = true)
    public List<FuncionarioCalendario> buscarPorFuncionario(Funcionario funcionario) throws DaoException {
        try {
            Query query = this.getEntityManager().createQuery("SELECT DISTINCT fc FROM FuncionarioCalendario fc INNER JOIN fc.funcionario f WHERE fc.funcionario = :funcionario");
            query.setParameter("funcionario", funcionario);
            return query.getResultList();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Transactional(readOnly = true)
    public List<FuncionarioCalendario> buscarPorFuncionarioEDataInicio(FuncionarioCalendario funcionarioCalendario) throws DaoException {
        Query query = this.getEntityManager().createQuery("SELECT DISTINCT fc FROM FuncionarioCalendario fc INNER JOIN fc.funcionario f WHERE fc.funcionario = :funcionario AND fc.dataInicio = :dataInio");
        query.setParameter("funcionario", funcionarioCalendario.getFuncionario());
        query.setParameter("dataInio", funcionarioCalendario.getDataInicio());
        return query.getResultList();
    }
    
    @Transactional(readOnly = true)
    public FuncionarioCalendario buscarPorFuncionarioCalendario(Integer idFuncionarioCalendario) throws DaoException {
        try {
            Query query = this.getEntityManager().createQuery("SELECT DISTINCT fc FROM FuncionarioCalendario fc WHERE fc.idFuncionarioCalendario = :idFuncionarioCalendario");
            query.setParameter("idFuncionarioCalendario", idFuncionarioCalendario);
            List<FuncionarioCalendario> lista = query.getResultList();
            return lista.isEmpty()? null : lista.get(0);
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }
}
