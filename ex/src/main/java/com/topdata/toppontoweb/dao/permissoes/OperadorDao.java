package com.topdata.toppontoweb.dao.permissoes;


import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.topdata.toppontoweb.dao.Dao;
import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.entity.autenticacao.Grupo;
import com.topdata.toppontoweb.entity.autenticacao.Operador;

/**
 * @version 1.0.0 data 07/07/2017
 * @since 1.0.0 data 07/07/2017
 *
 * @author juliano.ezequiel
 */
@Repository
public class OperadorDao extends Dao<Operador, Object> {

    public OperadorDao() {
        super(Operador.class);
    }
    
    @Transactional
    public void removerTokenAcesso(Grupo g) throws DaoException {
        try {
            Query q = this.getEntityManager().createQuery("UPDATE Operador o SET o.ultimoToken = null WHERE o.grupo = :grupo");
            q.setParameter("grupo", g);
            q.executeUpdate();
            
        } catch (Exception e) {
            throw new DaoException(e);
        }

    }

}
