package com.topdata.toppontoweb.dao.funcionario;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.topdata.toppontoweb.dao.Dao;
import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.entity.funcionario.Coletivo;
import com.topdata.toppontoweb.entity.funcionario.Funcionario;
import com.topdata.toppontoweb.entity.funcionario.FuncionarioJornada;

/**
 * @version 1.0.6 data 14/09/2016
 * @since 1.0.6 data 14/09/2016
 *
 * @author juliano.ezequiel
 */
@Repository
public class FuncionarioJornadaDao extends Dao<FuncionarioJornada, Object> {

    public FuncionarioJornadaDao() {
        super(FuncionarioJornada.class);
    }

    /**
     * Lista as jornadas anteriores a jornada passada como referência, não
     * considera a data igual. Não valida o id . A lista é retornada de forma
     * decrescente
     *
     * @param funcionarioJornada
     * @return
     * @throws DaoException
     */
    @Transactional(readOnly = true)
    public List<FuncionarioJornada> buscarAnterior(FuncionarioJornada funcionarioJornada) throws DaoException {
        try {
            Query query = this.entityManager.createQuery("SELECT f FROM FuncionarioJornada f WHERE f.funcionario = :idFuncionario and f.dataInicio < :dataInicio AND f.excecaoJornada = false ORDER BY f.dataInicio desc");

            query.setParameter("idFuncionario", funcionarioJornada.getFuncionario());
            query.setParameter("dataInicio", funcionarioJornada.getDataInicio());

            return query.getResultList();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    /**
     * Lista as jornadas posterior a jornada passada como referencia, não
     * considera a data igual. Não valida o id . A lista é retornada de forma
     * crescente
     *
     * @param funcionarioJornada
     * @return
     * @throws DaoException
     */
    @Transactional(readOnly = true)
    public List<FuncionarioJornada> buscarPosterior(FuncionarioJornada funcionarioJornada) throws DaoException {
        try {
            Query query = this.entityManager.createQuery("SELECT f FROM FuncionarioJornada f WHERE f.funcionario = :idFuncionario and f.dataInicio > :dataInicio AND f.excecaoJornada = false ORDER BY f.dataInicio asc");

            query.setParameter("idFuncionario", funcionarioJornada.getFuncionario());
            query.setParameter("dataInicio", funcionarioJornada.getDataInicio());

            return query.getResultList();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    /**
     * Lista as jornadas com a mesma data da jornada passada como referencia.
     * Valida somente os id diferentes .
     *
     * @param funcionarioJornada
     * @return
     * @throws DaoException
     */
    @Transactional(readOnly = true)
    public List<FuncionarioJornada> buscarDataInicio(FuncionarioJornada funcionarioJornada) throws DaoException {
        try {
            Query query = this.entityManager.createQuery("SELECT f FROM FuncionarioJornada f WHERE f.funcionario = :idFuncionario and f.dataInicio = :dataInicio AND f.idJornadaFuncionario != :id AND f.excecaoJornada = true");

            query.setParameter("idFuncionario", funcionarioJornada.getFuncionario());
            query.setParameter("dataInicio", funcionarioJornada.getDataInicio());
            query.setParameter("id", funcionarioJornada.getIdJornadaFuncionario());

            return query.getResultList();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    /**
     * Busca as jornadas que estão dentro do mesmo período da jornada passada
     * como referencia
     *
     * @param funcionarioJornada
     * @return
     * @throws DaoException
     */
    @Transactional(readOnly = true)
    public List<FuncionarioJornada> buscarSobrepostas(FuncionarioJornada funcionarioJornada) throws DaoException {
        try {
            Query query = this.entityManager.createQuery("SELECT f FROM FuncionarioJornada f WHERE f.funcionario = :idFuncionario and ((f.dataInicio BETWEEN :dataInicio and :dataFim) OR( f.dataFim BETWEEN :dataInicio AND :dataFim ) OR (f.dataInicio >= :dataInicio AND f.dataInicio <= :dataFim) OR (f.dataInicio <= :dataInicio AND f.dataFim >= :dataFim))");

            query.setParameter("idFuncionario", funcionarioJornada.getFuncionario());
            query.setParameter("dataInicio", funcionarioJornada.getDataInicio());
            query.setParameter("dataFim", funcionarioJornada.getDataFim());

            return query.getResultList();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    /**
     * Lista as exceções de jornadas, que estão dentro do mesmo período da
     * jornada passada na referencia
     *
     * @param funcionarioJornada
     * @return
     * @throws DaoException
     */
    @Transactional(readOnly = true)
    public List<FuncionarioJornada> buscarExcecaoDeJornadaEntreDatas(FuncionarioJornada funcionarioJornada) throws DaoException {
        try {
            Query query = this.entityManager.createQuery("SELECT f FROM FuncionarioJornada f WHERE f.funcionario = :idFuncionario and ((f.dataInicio BETWEEN :dataInicio and :dataFim) OR( f.dataFim BETWEEN :dataInicio AND :dataFim ) OR (f.dataInicio >= :dataInicio AND f.dataInicio <= :dataFim) OR (f.dataInicio <= :dataInicio AND f.dataFim >= :dataFim)) AND f.excecaoJornada = true");

            query.setParameter("idFuncionario", funcionarioJornada.getFuncionario());
            query.setParameter("dataInicio", funcionarioJornada.getDataInicio());
            query.setParameter("dataFim", funcionarioJornada.getDataFim());

            return query.getResultList();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Transactional(readOnly = true)
    public FuncionarioJornada buscarUltima(FuncionarioJornada funcionarioJornada) throws DaoException {
        try {
            Query query = this.entityManager.createQuery("SELECT  f FROM  FuncionarioJornada f WHERE f.funcionario = :idFuncionario and f.dataInicio >= :dataInicio AND f.excecaoJornada = false");
            query.setMaxResults(1);
            query.setParameter("idFuncionario", funcionarioJornada.getFuncionario());
            query.setParameter("dataInicio", funcionarioJornada.getDataInicio());

            return (FuncionarioJornada) query.getSingleResult();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Transactional(readOnly = true)
    public FuncionarioJornada buscarPrimeira(FuncionarioJornada funcionarioJornada) throws DaoException {
        try {
            Query query = this.entityManager.createQuery("SELECT f FROM FuncionarioJornada f WHERE f.funcionario = :idFuncionario and f.dataInicio >= :dataInicio AND f.excecaoJornada = false ORDER BY f.dataInicio ASC");
            query.setMaxResults(1);
            query.setParameter("idFuncionario", funcionarioJornada.getFuncionario());
            query.setParameter("dataInicio", funcionarioJornada.getDataInicio());

            return (FuncionarioJornada) query.getSingleResult();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Transactional
    public void excluirPorColetivo(Coletivo c) throws DaoException {
        try {
            Query query = this.getEntityManager().createQuery("DELETE FROM FuncionarioJornada AS fb WHERE fb.coletivo = :idColetivo");

            query.setParameter("idColetivo", c);

            query.executeUpdate();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Transactional
    public int excluirEntidade(FuncionarioJornada entidade) {
        Query tq = this.getEntityManager()
                .createQuery("DELETE FROM FuncionarioJornada AS af WHERE af.idJornadaFuncionario = :id");
        tq.setParameter("id", entidade.getIdJornadaFuncionario());

        return tq.executeUpdate();
    }

//    @Transactional(readOnly = true)
    public List<FuncionarioJornada> buscarEntreDatasFuncionario(Funcionario funcionario, Date dataInicio, Date dataFim) throws DaoException {
        try {

            Query query = this.getEntityManager().createQuery("SELECT DISTINCT fj FROM FuncionarioJornada fj WHERE fj.funcionario = :funcionario AND ((fj.dataInicio BETWEEN :dataInicio AND :dataFim) OR (fj.dataFim BETWEEN :dataInicio AND :dataFim) OR (fj.dataInicio >= :dataInicio AND (fj.dataFim <= :dataInicio OR fj.dataFim IS NULL ) OR (fj.dataInicio <= :dataInicio AND (fj.dataFim >= :dataInicio OR fj.dataFim IS NULL))))");
            query.setParameter("funcionario", funcionario);
            query.setParameter("dataInicio", dataInicio);
            query.setParameter("dataFim", dataFim);

            return query.getResultList();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }
    
    public Long buscarEntreDatasFuncionarioQuantidade(Funcionario funcionario, Date dataInicio, Date dataFim) throws DaoException {
        try {

            Query query = this.getEntityManager().createQuery("SELECT DISTINCT COUNT(fj) FROM FuncionarioJornada fj WHERE fj.funcionario = :funcionario AND ((fj.dataInicio BETWEEN :dataInicio AND :dataFim) OR (fj.dataFim BETWEEN :dataInicio AND :dataFim) OR (fj.dataInicio >= :dataInicio AND (fj.dataFim <= :dataInicio OR fj.dataFim IS NULL ) OR (fj.dataInicio <= :dataInicio AND (fj.dataFim >= :dataInicio OR fj.dataFim IS NULL))))");
            query.setParameter("funcionario", funcionario);
            query.setParameter("dataInicio", dataInicio);
            query.setParameter("dataFim", dataFim);
            
            List<Long> result = query.getResultList();

            return result.size() > 0? result.get(0) : 0l;
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Transactional(readOnly = true)
    public List<FuncionarioJornada> buscarFuncionario(Funcionario funcionario) throws DaoException {
        try {

            Query query = this.getEntityManager().createQuery("SELECT DISTINCT fj FROM FuncionarioJornada fj WHERE fj.funcionario = :funcionario ");
            query.setParameter("funcionario", funcionario);

            return query.getResultList();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

}
