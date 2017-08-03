package com.topdata.toppontoweb.dao.funcionario;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.topdata.toppontoweb.dao.Dao;
import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.entity.bancohoras.BancoHoras;
import com.topdata.toppontoweb.entity.funcionario.Coletivo;
import com.topdata.toppontoweb.entity.funcionario.Funcionario;
import com.topdata.toppontoweb.entity.funcionario.FuncionarioBancoHoras;

/**
 * @version 1.0.6 data 02/12/2016
 * @since 1.0.6 data 02/12/2016
 *
 * @author juliano.ezequiel
 */
@Repository
public class FuncionarioBancoHorasDao extends Dao<FuncionarioBancoHoras, Object> {

    public FuncionarioBancoHorasDao() {
        super(FuncionarioBancoHoras.class);
    }

    /**
     * busca os bancos que estão sobrepondo as datas do banco a ser cadastrado
     *
     * @param fb
     * @return
     * @throws DaoException
     */
    @Transactional(readOnly = true)
    public List<FuncionarioBancoHoras> buscarEntreDatas(FuncionarioBancoHoras fb) throws DaoException {
        try {

            Query query = this.getEntityManager().createQuery("SELECT fb FROM FuncionarioBancoHoras fb WHERE fb.funcionario = :funcionario AND((fb.dataInicio BETWEEN :dataInicio AND :dataFim) OR (fb.dataFim BETWEEN :dataInicio AND :dataFim) OR (fb.dataInicio >= :dataInicio AND (fb.dataFim <= :dataInicio) OR (fb.dataInicio <= :dataInicio AND (fb.dataFim >= :dataInicio))))");
            query.setParameter("funcionario", fb.getFuncionario());
            query.setParameter("dataInicio", fb.getDataInicio());
            query.setParameter("dataFim", fb.getDataFim());

            return query.getResultList();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    /**
     * busca os bancos que estão em um periodo por um funcionario
     *
     * @param funcionario
     * @param dataInicio
     * @param dataFim
     * @return
     * @throws DaoException
     */
    @Transactional(readOnly = true)
    public List<FuncionarioBancoHoras> buscarFuncionarioPeriodo(Funcionario funcionario, Date dataInicio, Date dataFim) throws DaoException {
        try {
            String strQuery = "SELECT DISTINCT fb FROM FuncionarioBancoHoras fb INNER JOIN fb.funcionario f "
                    + "WHERE f = :funcionario "
                    + "AND ((fb.dataInicio BETWEEN :dataInicio AND :dataFim) "
                    + "OR (fb.dataFim BETWEEN :dataInicio AND :dataFim) "
                    + "OR (fb.dataInicio >= :dataInicio AND (fb.dataFim <= :dataFim))"
                    + "OR (fb.dataInicio <= :dataInicio AND (fb.dataFim >= :dataFim))"
                    + "OR (fb.dataFim IS NULL and fb.dataInicio <= :dataInicio))";

            Query query = this.getEntityManager().createQuery(strQuery);

            query.setParameter("funcionario", funcionario);
            query.setParameter("dataInicio", dataInicio);
            query.setParameter("dataFim", dataFim);

            return query.getResultList();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Transactional(readOnly = true)
    public List<FuncionarioBancoHoras> buscarFuncionarioBancoHorasAbertoPeriodo(Funcionario funcionario, Date dataInicio, Date dataFim) throws DaoException {
        try {

            Query query = this.getEntityManager().createQuery("SELECT fb FROM FuncionarioBancoHoras fb WHERE fb.funcionario = :funcionario AND (fb.dataFim IS NULL AND fb.dataInicio <= :dataInicio OR fb.dataFim IS NULL AND fb.dataInicio <= :dataFim)");
            query.setParameter("funcionario", funcionario);
            query.setParameter("dataInicio", dataInicio);
            query.setParameter("dataFim", dataFim);

            return query.getResultList();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }
    
    @Transactional(readOnly = true)
    public List<FuncionarioBancoHoras> buscarFuncionarioBancoHorasAbertoEPeriodo(Funcionario funcionario, Date dataInicio, Date dataFim) throws DaoException {
        try {

            Query query = this.getEntityManager().createQuery("SELECT fb FROM FuncionarioBancoHoras fb "
                    + "WHERE fb.funcionario = :funcionario "
                    + "AND (fb.dataFim IS NULL AND fb.dataInicio BETWEEN :dataInicio AND :dataFim)");
            
            query.setParameter("funcionario", funcionario);
            query.setParameter("dataInicio", dataInicio);
            query.setParameter("dataFim", dataFim);

            return query.getResultList();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }
    
    /**
     * busca todos os bancos do funcionário que esteja abertos
     *
     * @param fb
     * @return
     * @throws DaoException
     */
    @Transactional(readOnly = true)
    public List<FuncionarioBancoHoras> buscarbancosAberto(FuncionarioBancoHoras fb) throws DaoException {
        try {

            Query query = this.getEntityManager().createQuery("SELECT fb FROM FuncionarioBancoHoras fb WHERE fb.funcionario = :funcionario AND fb.dataFim IS NULL");

            query.setParameter("funcionario", fb.getFuncionario());

            return query.getResultList();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    /**
     * busca os banco posteriores que estejam abertos
     *
     * @param fb
     * @return
     * @throws DaoException
     */
    @Transactional(readOnly = true)
    public List<FuncionarioBancoHoras> buscarbancosAbertoPosterior(FuncionarioBancoHoras fb) throws DaoException {
        try {

            Query query = this.getEntityManager().createQuery("SELECT fb FROM FuncionarioBancoHoras fb WHERE fb.funcionario = :funcionario AND (fb.dataInicio >= :dataInicio OR fb.dataFim >= :dataInicio)");

            query.setParameter("funcionario", fb.getFuncionario());
            query.setParameter("dataInicio", fb.getDataInicio());

            return query.getResultList();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    /**
     * busca os banco anterior a um sem data fim
     *
     * @param fb
     * @return
     * @throws DaoException
     */
    @Transactional(readOnly = true)
    public List<FuncionarioBancoHoras> buscarbancosAnteriorAberto(FuncionarioBancoHoras fb) throws DaoException {
        try {

            Query query = this.getEntityManager().createQuery("SELECT fb FROM FuncionarioBancoHoras fb WHERE fb.funcionario = :funcionario AND (fb.dataFim IS NULL AND  fb.dataInicio <= :dataFim)");

            query.setParameter("funcionario", fb.getFuncionario());
            query.setParameter("dataFim", fb.getDataFim());

            return query.getResultList();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Transactional
    public void excluirPorColetivo(Coletivo c) throws DaoException {
        try {
            Query query = this.getEntityManager().createQuery("DELETE FROM FuncionarioBancoHoras AS fb WHERE fb.coletivo = :idColetivo");

            query.setParameter("idColetivo", c);

            query.executeUpdate();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Transactional
    public int excluirEntidade(FuncionarioBancoHoras entidade) {
        Query tq = this.getEntityManager()
                .createQuery("DELETE FROM FuncionarioBancoHoras AS af WHERE af.idFuncionarioBancoHoras = :id");
        tq.setParameter("id", entidade.getIdFuncionarioBancoHoras());

        return tq.executeUpdate();
    }

    @Transactional(readOnly = true)
    public List<FuncionarioBancoHoras> buscarPorFuncionario(Integer id) throws DaoException {
        try {
            Query query = this.getEntityManager().createQuery("SELECT fb FROM FuncionarioBancoHoras fb WHERE fb.funcionario.idFuncionario = :id");
            query.setParameter("id", id);

            return query.getResultList();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Transactional(readOnly = true)
    public List<FuncionarioBancoHoras> buscarPorBancoHoras(BancoHoras bancoHoras) throws DaoException {
        try {

            Query query = this.getEntityManager().createQuery("SELECT DISTINCT fb FROM FuncionarioBancoHoras fb WHERE fb.bancoHoras = :bancohoras");
            query.setParameter("bancohoras", bancoHoras);

            return query.getResultList();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

}
