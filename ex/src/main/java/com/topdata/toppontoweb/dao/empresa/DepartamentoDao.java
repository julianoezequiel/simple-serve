package com.topdata.toppontoweb.dao.empresa;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.topdata.toppontoweb.dao.Dao;
import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.entity.autenticacao.Grupo;
import com.topdata.toppontoweb.entity.empresa.Departamento;
import com.topdata.toppontoweb.entity.empresa.Empresa;
import com.topdata.toppontoweb.entity.funcionario.Funcionario;

/**
 *
 * @author juliano.ezequiel
 */
@Repository
public class DepartamentoDao extends Dao<Departamento, Integer> {

    public DepartamentoDao() {
        super(Departamento.class);
    }
    
    @Transactional(readOnly = true)
    public List<Departamento> buscarPorGrupo(Grupo grupo) {
        Query tq = this.getEntityManager()
                .createQuery("SELECT d FROM Departamento d INNER JOIN d.grupoList g WHERE g = :grupo");
        tq.setParameter("grupo", grupo);
        return tq.getResultList();
    }

    @Transactional(readOnly = true)
    public List<Departamento> buscarPorEmpresa(Empresa empresa) {
        Query tq = this.getEntityManager()
                .createQuery("SELECT d FROM Departamento d WHERE d.empresa = :empresa");
        tq.setParameter("empresa", empresa);
        return tq.getResultList();
    }

    @Transactional(readOnly = true)
    public List<Departamento> findByFuncioarios(List<Funcionario> funcionarioList) throws DaoException {
        try {
            String idFuncionarios = "";
            String virgula = "";
            for (int i = 0; i < funcionarioList.size(); i ++) {
                Funcionario func = funcionarioList.get(i);
                idFuncionarios += virgula + " "+ func.getIdFuncionario() +" ";
                virgula = ",";
            }
            String sql = "SELECT DISTINCT d FROM Departamento d INNER JOIN d.funcionarioList f WHERE f.idFuncionario IN ("+idFuncionarios+") ";
            
            Query query = this.getEntityManager().createQuery(sql);
            
            return query.getResultList();
            
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getSimpleName()).log(Level.SEVERE, "ERRO BUSCAR : {0}", "Departamento");
            throw new DaoException(ex);
        }
    }

    @Transactional(readOnly = true)
    public Departamento buscarPorFuncionario(Funcionario funcionario) throws DaoException  {
        try {
            String sql = "SELECT DISTINCT d FROM Departamento d INNER JOIN d.funcionarioList f WHERE f = :funcionario ";
            
            Query query = this.getEntityManager().createQuery(sql);
            
            query.setParameter("funcionario", funcionario);
            List<Departamento> resultList = query.getResultList();
            
            return resultList.isEmpty()? new Departamento() : resultList.get(0);
            
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getSimpleName()).log(Level.SEVERE, "ERRO BUSCAR : {0}", "Departamento");
            throw new DaoException(ex);
        }
    }
}
