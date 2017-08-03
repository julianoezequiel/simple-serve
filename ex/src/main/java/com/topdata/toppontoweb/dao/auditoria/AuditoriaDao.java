package com.topdata.toppontoweb.dao.auditoria;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.topdata.toppontoweb.dao.Dao;
import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.entity.Auditoria;
import com.topdata.toppontoweb.entity.Auditoria_;

/**
 *
 * @author juliano.ezequiel
 */
@Repository
public class AuditoriaDao extends Dao<Auditoria, Integer> {

    @Transactional(readOnly = true)
    public List<Auditoria> buscarTiposAuditoriaPeriodo(Date dataInicio, Date dataFim) throws DaoException {
        try {
            Query query = this.getEntityManager().createQuery("SELECT DISTINCT a FROM Auditoria a WHERE a.tipoAuditoria IS NOT NULL AND a.dataHora BETWEEN :dataInicio AND :dataFim");
            query.setParameter("dataInicio", dataInicio);
            query.setParameter("dataFim", dataFim);
            return query.getResultList();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Transactional(readOnly = true)
    public List<Auditoria> buscarTiposAuditoriaPeriodoOperador(Integer idOperador, Date dataInicio, Date dataFim) throws DaoException {
        try {
            Query query = this.getEntityManager().createQuery("SELECT DISTINCT a FROM Auditoria a WHERE a.tipoAuditoria IS NOT NULL AND a.operador.idOperador = :idOperador AND a.dataHora BETWEEN :dataInicio AND :dataFim");
            query.setParameter("idOperador", idOperador);
            query.setParameter("dataInicio", dataInicio);
            query.setParameter("dataFim", dataFim);
            return query.getResultList();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Transactional(readOnly = true)
    public List<Auditoria> buscarAuditoriaPorPeriodoIdModulo(Integer idModulo, Date dataInicio, Date dataFim) throws DaoException {
        try {
            Query query = this.getEntityManager().createQuery("SELECT DISTINCT a FROM Auditoria a WHERE a.funcionalidade.idModulo.id = :idModulo AND a.dataHora BETWEEN :dataInicio AND :dataFim");
            query.setParameter("idModulo", idModulo);
            query.setParameter("dataInicio", dataInicio);
            query.setParameter("dataFim", dataFim);
            return query.getResultList();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Transactional(readOnly = true)
    public List<Auditoria> buscarAuditoriaPorPeriodoIdModuloOperador(Integer idModulo, Integer idOperador, Date dataInicio, Date dataFim) throws DaoException {
        try {
            Query query = this.getEntityManager().createQuery("SELECT DISTINCT a FROM Auditoria a WHERE a.operador.idOperador = :idOperador AND a.funcionalidade.idModulo.id = :idModulo AND a.dataHora BETWEEN :dataInicio AND :dataFim");
            query.setParameter("idModulo", idModulo);
            query.setParameter("idOperador", idOperador);
            query.setParameter("dataInicio", dataInicio);
            query.setParameter("dataFim", dataFim);
            return query.getResultList();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Transactional(readOnly = true)
    public List<Auditoria> buscarCriterioPeriodo(HashMap<String, Object> criterios, Date dataInicio, Date dataFim) throws DaoException {
        try {
            final CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
            final CriteriaQuery<Auditoria> criteriaQuery = builder.createQuery(Auditoria.class);

            Root<Auditoria> root = criteriaQuery.from(Auditoria.class);
            List<Predicate> predicates = new ArrayList<>();

            criterios.entrySet().stream().filter((map) -> {
                return root.get(map.getKey()) != null && map.getValue() != null;
            }).forEach((map) -> {
                predicates.add(builder.equal(root.get(map.getKey()), map.getValue()));
            });
            predicates.add(builder.between(root.<Date>get(Auditoria_.dataHora.getName()), dataInicio, dataFim));
            criteriaQuery.where(predicates.toArray(new Predicate[]{}));
            TypedQuery<Auditoria> typedQuery = getEntityManager().createQuery(criteriaQuery);

            return typedQuery.getResultList();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

}
