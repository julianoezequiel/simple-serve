package com.topdata.toppontoweb.dao.empresa;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.topdata.toppontoweb.dao.Dao;
import com.topdata.toppontoweb.entity.empresa.Cei;
import com.topdata.toppontoweb.entity.empresa.Empresa;

/**
 *
 * @author juliano.ezequiel
 */
@Repository
public class CeiDao extends Dao<Cei, Integer> {

    public CeiDao() {
        super(Cei.class);
    }

    @Transactional(readOnly = true)
    public List<Cei> bucarPorEmpresa(Empresa empresa) {
        Query tq = this.getEntityManager()
                .createQuery("SELECT c FROM Cei c WHERE c.empresa = :empresa");
        tq.setParameter("empresa", empresa);
        return tq.getResultList();

    }
}
