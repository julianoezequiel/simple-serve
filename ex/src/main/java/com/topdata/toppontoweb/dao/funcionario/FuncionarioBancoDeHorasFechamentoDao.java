package com.topdata.toppontoweb.dao.funcionario;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.topdata.toppontoweb.dao.Dao;
import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.entity.funcionario.Coletivo;
import com.topdata.toppontoweb.entity.funcionario.Funcionario;
import com.topdata.toppontoweb.entity.funcionario.FuncionarioBancoHoras;
import com.topdata.toppontoweb.entity.funcionario.FuncionarioBancoHorasFechamento;

/**
 *
 * @author juliano.ezequiel
 */
@Repository
public class FuncionarioBancoDeHorasFechamentoDao extends Dao<FuncionarioBancoHorasFechamento, Object> {

    public List<FuncionarioBancoHorasFechamento> buscarPorFuncionarioPeriodo(Funcionario funcionario, Date dataInicio, Date dataFim) throws DaoException {
        try {
            //TODO:regra para carregar no periodo
            Query query
                    = this.getEntityManager()
                    .createQuery("SELECT DISTINCT fbf FROM FuncionarioBancoHorasFechamento fbf INNER JOIN fbf.funcionarioBancoHoras fbh INNER JOIN fbh.funcionario f  WHERE f = :funcionario AND ((fbh.dataInicio BETWEEN :dataInicio AND :dataFim) OR (fbh.dataFim BETWEEN :dataInicio AND :dataFim) OR (fbh.dataInicio >= :dataInicio AND (fbh.dataFim <= :dataInicio) OR (fbh.dataInicio <= :dataInicio AND (fbh.dataFim >= :dataInicio) ) OR (fbh.dataFim IS NULL and fbh.dataInicio <= :dataInicio)))");
            query.setParameter("funcionario", funcionario);
            query.setParameter("dataInicio", dataInicio);
            query.setParameter("dataFim", dataFim);
//
            return query.getResultList();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Transactional(readOnly = true)
    public List<FuncionarioBancoHorasFechamento> buscarPorFuncionario(Integer id) throws DaoException {
        try {
            Query query = this.getEntityManager().createQuery("SELECT fbf FROM FuncionarioBancoHorasFechamento fbf INNER JOIN fbf.funcionarioBancoHoras fbh INNER JOIN fbh.funcionario f WHERE f.idFuncionario = :id");
            query.setParameter("id", id);
            return query.getResultList();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    /**
     * Consulta os fechamentos existentes para o banco de horas do funcionário
     *
     * @param id
     * @return
     * @throws DaoException
     */
    @Transactional(readOnly = true)
    public List<FuncionarioBancoHorasFechamento> buscarPorFuncionarioBancoHoras(Integer id) throws DaoException {
        try {
            Query query = this.getEntityManager().createQuery("SELECT fbf FROM FuncionarioBancoHorasFechamento fbf INNER JOIN fbf.funcionarioBancoHoras fbh INNER JOIN fbh.bancoHoras b WHERE  fbh.idFuncionarioBancoHoras = :id");
            query.setParameter("id", id);
            return query.getResultList();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Transactional(readOnly = true)
    public List<FuncionarioBancoHorasFechamento> buscarPorFuncionarioBancoHorasDataInicio(FuncionarioBancoHoras funcionarioBancoHoras) throws DaoException {
        try {
            Query query = this.getEntityManager().createQuery("SELECT fbf FROM FuncionarioBancoHorasFechamento fbf INNER JOIN fbf.funcionarioBancoHoras fbh INNER JOIN fbh.bancoHoras b WHERE  fbh = :funcionarioBancoHoras and fbf.dataFechamento <> :data");
            query.setParameter("funcionarioBancoHoras", funcionarioBancoHoras);
            query.setParameter("data", funcionarioBancoHoras.getDataInicio(), TemporalType.DATE);
            return query.getResultList();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Transactional(readOnly = true)
    public List<FuncionarioBancoHorasFechamento> buscarPorFuncionarioPeriodo(FuncionarioBancoHoras funcionarioBancoHoras, Date inicio, Date fim) throws DaoException {
        try {
            Query query = this.getEntityManager().createQuery("SELECT fbf FROM FuncionarioBancoHorasFechamento fbf INNER JOIN fbf.funcionarioBancoHoras fbh INNER JOIN fbh.bancoHoras b WHERE  fbh = :funcionarioBancoHoras and fbf.dataFechamento BETWEEN :inicio AND :fim");
            query.setParameter("funcionarioBancoHoras", funcionarioBancoHoras);
            query.setParameter("inicio", inicio);
            query.setParameter("fim", fim);
            return query.getResultList();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Transactional
    public int excluirPorIdFuncionarioBanco(Integer id) throws DaoException {
        try {
            Query query = this.getEntityManager().createQuery("DELETE FROM FuncionarioBancoHorasFechamento f WHERE f.funcionarioBancoHoras.idFuncionarioBancoHoras = :id");
            query.setParameter("id", id);
            return query.executeUpdate();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Transactional
    public void atualizar(FuncionarioBancoHorasFechamento fbhf) throws DaoException {
        try {

            Query query = this.getEntityManager()
                    .createNativeQuery("UPDATE FuncionarioBancoHorasFechamento set DataFechamento = :dataFechamento,Credito=:credito,Debito=:debito where idFuncionarioBancoHoras = :idFuncionarioBancoHoras");

            query.setParameter("idFuncionarioBancoHoras", fbhf.getFuncionarioBancoHoras().getIdFuncionarioBancoHoras());
            query.setParameter("dataFechamento", fbhf.getDataFechamento(), TemporalType.TIMESTAMP);
            query.setParameter("credito", fbhf.getCredito(), TemporalType.TIMESTAMP);
            query.setParameter("debito", fbhf.getDebito(), TemporalType.TIMESTAMP);

            query.executeUpdate();

        } catch (Exception ex) {
            throw new DaoException(ex);
        }

    }

    @Transactional
    public void excluirEntidade(FuncionarioBancoHorasFechamento funcionarioBancoHorasFechamento) throws DaoException {
        try {
            Query query = this.getEntityManager().createQuery("DELETE FROM FuncionarioBancoHorasFechamento f WHERE f = :funcionarioBancoHorasFechamento");
            query.setParameter("funcionarioBancoHorasFechamento", funcionarioBancoHorasFechamento);
            query.executeUpdate();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Transactional
    public void excluirPorColetivo(Coletivo c) throws DaoException {
        try {
            Query query = this.getEntityManager().createQuery("DELETE FROM FuncionarioBancoHorasFechamento AS fb WHERE fb.coletivo = :idColetivo");

            query.setParameter("idColetivo", c);

            query.executeUpdate();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    public List<FuncionarioBancoHorasFechamento> buscarPorFuncionarioPosterior(Funcionario funcionario, Date dataFechamento) throws DaoException {
        try {
            Query query = this.getEntityManager().createQuery("SELECT fbf FROM FuncionarioBancoHorasFechamento fbf INNER JOIN fbf.funcionarioBancoHoras fbh INNER JOIN fbh.funcionario f WHERE  f = :funcionario and fbf.dataFechamento >= :data");
            query.setParameter("funcionario", funcionario);
            query.setParameter("data", dataFechamento);
            return query.getResultList();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }
}
