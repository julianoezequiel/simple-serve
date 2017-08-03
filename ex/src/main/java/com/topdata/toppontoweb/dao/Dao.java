/**
 * @Author: juliano.ezequiel <Juliano>
 * @Date: 02-09-2016
 * @Email: juliano.ezequiel@topdata.com.br
 * @Project: TopPontoWeb
 * @Last modified by: Juliano
 * @Last modified time: 28-10-2016
 */
package com.topdata.toppontoweb.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.topdata.toppontoweb.entity.Entidade;
import org.springframework.transaction.annotation.Propagation;

/**
 * Classe genérica para persistência dos dados
 *
 * @version 1.0.1 data 03/05/2016
 * @since 1.0.1 data 03/05/2016
 *
 * @author juliano.ezequiel
 * @param <T>
 * @param <I>
 */
@Repository
public class Dao<T extends Entidade, I> implements InterfaceDao<T, I> {

    public final static org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Dao.class.getName());

    /**
     * Dao genérico para persistência dos dados
     */
    public Dao() {
    }

    /**
     * Gerenciador de entidade do Spring
     */
    @PersistenceContext
    protected EntityManager entityManager;

    protected Class<T> entityClass;

    public Dao(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityClass(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public boolean contains(T entity) {
        return this.entityManager.contains(entity);
    }

    @Transactional(readOnly = true)
    public T merge(T entity) {
        return this.entityManager.merge(entity);
    }

    @Transactional(readOnly = true)
    public void refresh(T entity) {
        this.entityManager.refresh(entity);
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
    public T findRequiresNew(Class<T> entidade, Object id) throws DaoException {
        try {
            LOGGER.debug("find findRequiresNew" + entidade.getSimpleName() + " - " + id.getClass().getSimpleName());
            return this.getEntityManager().find(entidade, id);
        } catch (Exception e) {
            LOGGER.error(this.getClass().getSimpleName(), e);
            throw new DaoException(e);
        }
    }

    @Transactional(readOnly = true)
    public void flush() {
        this.entityManager.flush();
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> findAll(Class<T> entidade) throws DaoException {
        LOGGER.debug("findAll " + entidade.getSimpleName());
        try {
            final CriteriaBuilder builder = this.getEntityManager().getCriteriaBuilder();
            final CriteriaQuery<T> criteriaQuery = builder.createQuery(entidade);

            criteriaQuery.from(entidade);

            TypedQuery<T> typedQuery = this.getEntityManager().createQuery(criteriaQuery);
            return typedQuery.getResultList();
        } catch (Exception e) {
            LOGGER.error(this.getClass().getSimpleName(), e);
            throw new DaoException(e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public T find(Class<T> entidade, Object id) throws DaoException {
        try {
            LOGGER.debug("find " + entidade.getSimpleName() + " - " + id.getClass().getSimpleName());
            return this.getEntityManager().find(entidade, id);
        } catch (Exception e) {
            LOGGER.error(this.getClass().getSimpleName(), e);
            throw new DaoException(e);
        }
    }

    @Override
    @Transactional
    public T save(T entity) throws DaoException {
        try {
            LOGGER.debug("save " + entity.getClass().getSimpleName());
            return this.getEntityManager().merge(entity);
        } catch (Exception e) {
            LOGGER.error(this.getClass().getSimpleName(), e);
            throw new DaoException(e);
        }
    }

    @Transactional(readOnly = false)
    public T saveAndFlush(T entity) throws DaoException {
        try {
            LOGGER.debug("save " + entity.getClass().getSimpleName());
            return this.getEntityManager().merge(entity);
        } catch (Exception e) {
            LOGGER.error(this.getClass().getSimpleName(), e);
            throw new DaoException(e);
        }
    }

    @Override
    @Transactional
    public void delete(I id) throws DaoException {
        try {
            LOGGER.debug("delete " + id != null ? id.getClass().getSimpleName() : "");
            if (id != null) {
                this.getEntityManager().remove(id);
            }
        } catch (Exception e) {
            LOGGER.error(this.getClass().getSimpleName(), e);
            throw new DaoException(e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Long count(Class<T> entidade) throws DaoException {
        try {
            LOGGER.debug("count " + entidade.getSimpleName());
            final CriteriaBuilder cb = this.getEntityManager().getCriteriaBuilder();
            final CriteriaQuery<Long> cq = cb.createQuery(Long.class);

            Root<T> root = cq.from(entidade);
            cq.select(cb.count(root));

            TypedQuery<Long> typedQuery = this.getEntityManager().createQuery(cq);
            return typedQuery.getSingleResult();
        } catch (Exception e) {
            LOGGER.error(this.getClass().getSimpleName(), e);
            throw new DaoException(e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Entidade> find(int maxResults, int firstResult, Class<T> entidade) throws DaoException {
        try {
            LOGGER.debug("find " + entidade.getSimpleName());
            final CriteriaBuilder builder = this.getEntityManager().getCriteriaBuilder();
            final CriteriaQuery<T> criteriaQuery = builder.createQuery(entidade);

            criteriaQuery.from(entidade);

            TypedQuery typedQuery = this.getEntityManager().createQuery(criteriaQuery);
            typedQuery.setMaxResults(maxResults);
            typedQuery.setFirstResult(firstResult);

            return typedQuery.getResultList();
        } catch (Exception e) {
            LOGGER.error(this.getClass().getSimpleName(), e);
            throw new DaoException(e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> findbyAttributes(HashMap<String, Object> criterios, Class<T> entidade) throws DaoException {
        try {
            LOGGER.debug("findbyAttributes " + entidade.getSimpleName());
            final CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
            final CriteriaQuery<T> criteriaQuery = builder.createQuery(entidade);

            Root<T> root = criteriaQuery.from(entidade);
            List<Predicate> predicates = new ArrayList<>();

            for (Entry<String, Object> map : criterios.entrySet()) {
                if (root.get(map.getKey()) != null && map.getValue() != null) {
                    predicates.add(builder.equal(root.get(map.getKey()), map.getValue()));
                }
            }

            criteriaQuery.where(predicates.toArray(new Predicate[]{}));
            TypedQuery<T> typedQuery = getEntityManager().createQuery(criteriaQuery);

            return typedQuery.getResultList();
        } catch (Exception e) {
            LOGGER.error(this.getClass().getSimpleName(), e);
            throw new DaoException(e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public T findOnebyAttributes(HashMap<String, Object> criterios, Class<T> entidade) throws DaoException {
        try {
            LOGGER.debug("findOnebyAttributes " + entidade.getSimpleName());
            final CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
            final CriteriaQuery<T> criteriaQuery = builder.createQuery(entidade);

            Root<T> root = criteriaQuery.from(entidade);
            List<Predicate> predicates = new ArrayList<>();

            for (Entry<String, Object> map : criterios.entrySet()) {
                if (root.get(map.getKey()) != null && map.getValue() != null) {
                    predicates.add(builder.equal(root.get(map.getKey()), map.getValue()));
                }
            }

            criteriaQuery.where(predicates.toArray(new Predicate[]{}));
            TypedQuery<T> typedQuery = getEntityManager().createQuery(criteriaQuery);

            return typedQuery.getSingleResult();

        } catch (Exception e) {
            LOGGER.debug(this.getClass().getSimpleName(), e.getMessage());
            return null;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Long countByCriteria(HashMap<String, Object> criterios, Class<T> entidade) throws DaoException {
        try {
            LOGGER.debug("countByCriteria " + entidade.getSimpleName());
            final CriteriaBuilder cb = this.getEntityManager().getCriteriaBuilder();
            final CriteriaQuery<Long> cq = cb.createQuery(Long.class);

            Root<T> root = cq.from(entidade);
            cq.select(cb.count(root));

            List<Predicate> predicates = new ArrayList<>();

            for (Entry<String, Object> map : criterios.entrySet()) {
                if (root.get(map.getKey()) != null) {
                    predicates.add(cb.equal(root.get(map.getKey()), map.getValue()));
                }
            }

            cq.where(predicates.toArray(new Predicate[]{}));
            TypedQuery<Long> typedQuery = this.getEntityManager().createQuery(cq);

            return typedQuery.getSingleResult();
        } catch (Exception e) {
            LOGGER.error(this.getClass().getSimpleName(), e);
            throw new DaoException(e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public T deleteAll(Class<T> entidade) throws DaoException {
        final CriteriaBuilder cb = this.getEntityManager().getCriteriaBuilder();
        final CriteriaQuery<Long> cq = cb.createQuery(Long.class);

        return null;
    }

    @Override
    public T findReference(Class<T> entidade, Object id) throws DaoException {
        return this.getEntityManager().getReference(entidade, id);
    }

    public void detache(I e) throws DaoException {
        this.getEntityManager().detach(e);
    }

}
