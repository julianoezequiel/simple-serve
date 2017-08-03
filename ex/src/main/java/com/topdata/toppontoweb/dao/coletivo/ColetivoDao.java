package com.topdata.toppontoweb.dao.coletivo;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.topdata.toppontoweb.dao.Dao;
import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.entity.funcionario.Afastamento;
import com.topdata.toppontoweb.entity.funcionario.Coletivo;

/**
 * @version 1.0.6 data 28/11/2016
 * @since 1.0.6 data 28/11/2016
 *
 * @author juliano.ezequiel
 */
@Repository
public class ColetivoDao extends Dao<Coletivo, Object> {

    public ColetivoDao() {
        super(Coletivo.class);
    }

    /**
     * lista todos os coletivos dos afastamentos
     *
     * @return lista de Coletivo
     * @throws DaoException
     */
    @Transactional(readOnly = true)
    public List<Coletivo> buscarColetivoAfastamentos() throws DaoException {
        try {
            Query query = this.getEntityManager().createQuery("SELECT DISTINCT c FROM Coletivo c,Afastamento a WHERE c.idColetivo = a.coletivo.idColetivo");

            return query.getResultList();

        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getSimpleName()).log(Level.SEVERE, "ERRO BUSCAR : {0}", "Coletivo");
            throw new DaoException(ex);
        }
    }

    /**
     * lista todas os coletivos dos FuncionarioCalendario que possui coletivo
     *
     * @return lista de Coletivo
     * @throws DaoException
     */
    @Transactional(readOnly = true)
    public List<Coletivo> buscarColetivoFuncionarioCalendario() throws DaoException {
        try {

            Query query = this.getEntityManager().createQuery("SELECT DISTINCT c FROM Coletivo c,FuncionarioCalendario a WHERE c.idColetivo = a.coletivo.idColetivo");

            return query.getResultList();

        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getSimpleName()).log(Level.SEVERE, "ERRO BUSCAR : {0}", "Coletivo");
            throw new DaoException(ex);
        }
    }

    /**
     * lista todos os coletivos dos FuncionarioJornada
     *
     * @return lista de Coletivo
     * @throws DaoException
     */
    @Transactional(readOnly = true)
    public List<Coletivo> buscarColetivoFuncionarioJornada() throws DaoException {
        try {

            Query query = this.getEntityManager().createQuery("SELECT DISTINCT c FROM Coletivo c,FuncionarioJornada a WHERE c.idColetivo = a.coletivo.idColetivo");

            return query.getResultList();

        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getSimpleName()).log(Level.SEVERE, "ERRO BUSCAR : {0}", "Coletivo");
            throw new DaoException(ex);
        }
    }

    /**
     * lista todos os coletivos das Compencacaoes
     *
     * @return lista de Coletivo
     * @throws DaoException
     */
    @Transactional(readOnly = true)
    public List<Coletivo> buscarColetivoCompencacaoes() throws DaoException {
        try {

            Query query = this.getEntityManager().createQuery("SELECT DISTINCT c FROM Coletivo c,Compensacao a WHERE c.idColetivo = a.coletivo.idColetivo");

            return query.getResultList();

        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getSimpleName()).log(Level.SEVERE, "ERRO BUSCAR : {0}", "Coletivo");
            throw new DaoException(ex);
        }
    }

    /**
     * lista todos os coletivos dos FuncionarioBancoHoras
     *
     * @return lista de Coletivo
     * @throws DaoException
     */
    @Transactional(readOnly = true)
    public List<Coletivo> buscarColetivoBHManutencao() throws DaoException {
        try {

            Query query = this.getEntityManager().createQuery("SELECT DISTINCT c FROM Coletivo c,FuncionarioBancoHoras a WHERE c.idColetivo = a.coletivo.idColetivo");

            return query.getResultList();

        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getSimpleName()).log(Level.SEVERE, "ERRO BUSCAR : {0}", "Coletivo");
            throw new DaoException(ex);
        }
    }

    /**
     * lista todos os coletivos dos FuncionarioBancoHorasFechamento
     *
     * @return lista de Coletivo
     * @throws DaoException
     */
    @Transactional(readOnly = true)
    public List<Coletivo> buscarColetivoBHFechamento() throws DaoException {
        try {

            Query query = this.getEntityManager().createQuery("SELECT DISTINCT c FROM Coletivo c, FuncionarioBancoHorasFechamento a WHERE c.idColetivo = a.coletivo.idColetivo");

            return query.getResultList();

        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getSimpleName()).log(Level.SEVERE, "ERRO BUSCAR : {0}", "Coletivo");
            throw new DaoException(ex);
        }
    }

    @Transactional(readOnly = true)
    public List<Coletivo> buscarAPI() throws DaoException {
        try {
            final CriteriaBuilder builder = this.getEntityManager().getCriteriaBuilder();
            final CriteriaQuery<Coletivo> criteriaQuery = builder.createQuery(Coletivo.class);

            Root<Coletivo> fromColetivo = criteriaQuery.from(Coletivo.class);

            Join<Afastamento, Coletivo> join = fromColetivo.join("");

            TypedQuery<Coletivo> typedQuery = this.getEntityManager().createQuery(
                    criteriaQuery.select(fromColetivo)
                    .where(builder.equal(fromColetivo, join)).distinct(true));
            return typedQuery.getResultList();
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getSimpleName()).log(Level.SEVERE, "ERRO BUSCAR : {0}", "Coletivo");
            throw new DaoException(ex);
        }

    }

    /**
     * Remove (via query) um coletivo pelo "id" contido na entidade.
     *
     * @param coletivo Entidade que será removida
     * @return lista de Coletivo
     */
    @Transactional
    public int excluirEntidade(Coletivo coletivo) {
        Query tq = this.getEntityManager()
                .createQuery("DELETE FROM Coletivo AS col WHERE col.idColetivo = :id");
        tq.setParameter("id", coletivo.getIdColetivo());

        return tq.executeUpdate();
    }
}
