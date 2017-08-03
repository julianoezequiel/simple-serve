package com.topdata.toppontoweb.dao.permissoes;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.topdata.toppontoweb.dao.Dao;
import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.entity.autenticacao.Grupo;

/**
 * @version 1.0.6 data 28/11/2016
 * @since 1.0.6 data 28/11/2016
 *
 * @author juliano.ezequiel
 */
@Repository
public class GrupoDao extends Dao {

    public GrupoDao() {
        super(Grupo.class);
    }

    @Transactional
    public void excluirGrupo(Grupo g) throws DaoException {
        try {
            //exclui todos os vinculos com a tabela funcionalidades
            Query q = this.getEntityManager().createQuery("DELETE FROM FuncionalidadesGrupoOperacao f where f.grupo = :grupo");
            q.setParameter("grupo", g);
            q.executeUpdate();
            
            //exclui o grupo
            q = this.getEntityManager().createQuery("DELETE FROM Grupo g WHERE g.idGrupo = :idGrupo");
            q.setParameter("idGrupo", g.getIdGrupo());
            q.executeUpdate();
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getSimpleName()).log(Level.SEVERE, "DELETAR ENTIDADE : {0}", e.getStackTrace());
            throw new DaoException(e);
        }

    }

}
