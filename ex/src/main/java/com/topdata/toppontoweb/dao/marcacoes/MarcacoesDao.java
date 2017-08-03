package com.topdata.toppontoweb.dao.marcacoes;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.topdata.toppontoweb.dao.Dao;
import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.entity.empresa.Empresa;
import com.topdata.toppontoweb.entity.funcionario.Funcionario;
import com.topdata.toppontoweb.entity.marcacoes.Importacao;
import com.topdata.toppontoweb.entity.marcacoes.Marcacoes;
import com.topdata.toppontoweb.entity.rep.Rep;

/**
 *
 * @author juliano.ezequiel
 */
@Repository
public class MarcacoesDao extends Dao<Marcacoes, Integer> {

    @Transactional(readOnly = true)
    public List<Marcacoes> buscarPorPeriodoEmpresa(Date dataInicio, Date dataFim, Empresa empresa, boolean invalidas) throws DaoException {
        try {
            Query tq = this.getEntityManager().createQuery("SELECT m FROM Marcacoes m WHERE m.empresa = :empresa AND (m.dataHora BETWEEN :dataInicio AND :dataFim) AND m.invalido = :invalidas");
            tq.setParameter("empresa", empresa);
            tq.setParameter("dataInicio", dataInicio, TemporalType.TIMESTAMP);
            tq.setParameter("dataFim", dataFim, TemporalType.TIMESTAMP);
            tq.setParameter("invalidas", invalidas);

            return tq.getResultList();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }
    
    @Transactional(readOnly = true)
    public List<Marcacoes> buscarPorPeriodo(Date dataInicio, Date dataFim, Funcionario funcionario, boolean invalidas) throws DaoException {
        try {
            Query tq = this.getEntityManager().createQuery("SELECT m FROM Marcacoes m WHERE m.idFuncionario = :funcionario AND (m.dataHora BETWEEN :dataInicio AND :dataFim) AND m.invalido = :invalidas");
            tq.setParameter("funcionario", funcionario);
            tq.setParameter("dataInicio", dataInicio, TemporalType.TIMESTAMP);
            tq.setParameter("dataFim", dataFim, TemporalType.TIMESTAMP);
            tq.setParameter("invalidas", invalidas);

            return tq.getResultList();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Transactional(readOnly = true)
    public List<Marcacoes> buscarPorDataHoraECartaoOriginalEmpresa(String cartaoOriginal, Rep rep, Empresa empresa, Date dataHora) throws DaoException {
        try {
            Query tq = this.getEntityManager().createQuery("SELECT m FROM Marcacoes m WHERE m.cartaoOriginal = :cartaoOriginal AND m.dataHora = :dataHora AND m.rep = :rep AND m.empresa = :empresa");
            tq.setParameter("cartaoOriginal", cartaoOriginal);
            tq.setParameter("dataHora", dataHora, TemporalType.TIMESTAMP);
            tq.setParameter("rep", rep);
            tq.setParameter("empresa", empresa);

            return tq.getResultList();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Transactional(readOnly = true)
    public List<Marcacoes> buscarPorDataHoraECartaoOriginalFuncionario(String cartaoOriginal, Rep rep, Funcionario funcionario, Date dataHora) throws DaoException {
        try {
            Query tq = this.getEntityManager().createQuery("SELECT m FROM Marcacoes m WHERE m.cartaoOriginal = :cartaoOriginal AND m.dataHora = :dataHora AND m.rep = :rep AND m.idFuncionario = :funcionario");
            tq.setParameter("cartaoOriginal", cartaoOriginal);
            tq.setParameter("dataHora", dataHora, TemporalType.TIMESTAMP);
            tq.setParameter("rep", rep);
            tq.setParameter("funcionario", funcionario);

            return tq.getResultList();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Transactional(readOnly = true)
    public List<Marcacoes> buscarPorDataHoraNSRPIS(String nsr, String pis, Rep rep, Empresa empresa, Date dataHora) throws DaoException {
        try {
            Query tq = this.getEntityManager().createQuery("SELECT m FROM Marcacoes m WHERE m.dataHora = :dataHora AND m.pis = :pis AND m.empresa = :empresa AND (m.nsr = :nsr OR m.rep = :rep)");
            tq.setParameter("nsr", nsr);
            tq.setParameter("dataHora", dataHora, TemporalType.TIMESTAMP);
            tq.setParameter("rep", rep);
            tq.setParameter("pis", pis);
            tq.setParameter("empresa", empresa);

            return tq.getResultList();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Transactional
    public int deletarAllPelaImportacao(Importacao importacao) throws DaoException {
        try {
            Query tq = this.getEntityManager().createQuery("DELETE FROM Marcacoes m WHERE m.importacao = :importacao");
            tq.setParameter("importacao", importacao);

            return tq.executeUpdate();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    //Transformar isso em um update
    @Transactional
    public List<Marcacoes> atualizarMarcacoesPorPeriodoFuncionarioCartao(Date dataInicio, Date dataFim, String numeroCartao, boolean invalido) throws DaoException {
        try {
            Query tq = this.getEntityManager().createQuery("SELECT m FROM Marcacoes m WHERE m.cartao = :numeroCartao AND (m.dataHora BETWEEN :dataInicio AND :dataFim) AND m.invalido = :invalido");
            tq.setParameter("numeroCartao", numeroCartao);
            tq.setParameter("dataInicio", dataInicio, TemporalType.TIMESTAMP);
            tq.setParameter("dataFim", dataFim, TemporalType.TIMESTAMP);
            tq.setParameter("invalido", invalido);

            return tq.getResultList();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    /**
     * Tranforma as marcações invalidas 1 em validas 0, vinculando o funcionário
     *
     * @param f
     * @return
     * @throws DaoException
     */
    @Transactional
    public int atualizarMarcacoesInvalidosPorPis(Funcionario f) throws DaoException {
        try {
            String sql = "UPDATE Marcacoes "
                    + "SET idFuncionario = " + f.getIdFuncionario() + ", "
                    + "invalido = 0 "
                    + "WHERE invalido = 1 AND pis = " + f.getPis();

            Query query = this.getEntityManager().createNativeQuery(sql);

            return query.executeUpdate();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    /**
     * Tranforma as marcações invalidas 1 em validas 0, vinculando o funcionário
     * dentro do periodo
     *
     * @param f
     * @param inicio
     * @param fim
     * @param empresa
     * @return
     * @throws DaoException
     */
    @Transactional
    public int atualizarMarcacoesInvalidosPorPisPeriodo(Funcionario f, Date inicio, Date fim, Empresa empresa) throws DaoException {
        try {
            String sql = "UPDATE Marcacoes "
                    + "SET idFuncionario = " + f.getIdFuncionario() + ", "
                    + "invalido = 0 "
                    + "WHERE invalido = 1 AND pis = " + f.getPis()
                    + " AND idEmpresa = " + empresa.getIdEmpresa()
                    + " AND DataHora >= :dataInicio AND DataHora <= :dataFim";

            Query query = this.getEntityManager().createNativeQuery(sql);
            query.setParameter("dataInicio", inicio, TemporalType.TIMESTAMP);
            query.setParameter("dataFim", fim, TemporalType.TIMESTAMP);

            return query.executeUpdate();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Transactional
    public int atualizarMarcacoesInvalidosCartao(Funcionario f, String numeroCartao, Date dataInicio, Date dataFim) throws DaoException {
        try {
            String sql = "UPDATE Marcacoes "
                    + "SET idFuncionario = " + f.getIdFuncionario() + ", "
                    + "invalido = 0 "
                    + "WHERE Invalido = 1 "
                    + "AND cartao = :numeroCartao "
                    + "AND DataHora >= :dataInicio AND DataHora <= :dataFim ";

            Query query = this.getEntityManager().createNativeQuery(sql);
            query.setParameter("numeroCartao", numeroCartao);
            query.setParameter("dataInicio", dataInicio, TemporalType.TIMESTAMP);
            query.setParameter("dataFim", dataFim, TemporalType.TIMESTAMP);

            return query.executeUpdate();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Transactional(readOnly = true)
    public List<Marcacoes> buscarMarcacoesPorPisEmpresa(String pis, Empresa empresa, boolean invalido, Date dataInicio, Date dataFim) throws DaoException {
        try {
            Query tq = this.getEntityManager().createQuery("SELECT m FROM Marcacoes m WHERE m.pis = :pis AND m.empresa = :empresa AND (m.dataHora BETWEEN :dataInicio AND :dataFim) AND m.invalido = :invalido");
            tq.setParameter("pis", pis);
            tq.setParameter("empresa", empresa);
            tq.setParameter("dataInicio", dataInicio, TemporalType.TIMESTAMP);
            tq.setParameter("dataFim", dataFim, TemporalType.TIMESTAMP);
            tq.setParameter("invalido", invalido);

            return tq.getResultList();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Transactional(readOnly = true)
    public List<Marcacoes> buscarMarcacoesPorCartaoEmpresa(String cartao, Empresa empresa, boolean invalido, Date dataInicio, Date dataFim) throws DaoException {
        try {
            Query tq = this.getEntityManager().createQuery("SELECT m FROM Marcacoes m WHERE m.cartao = :cartao AND m.empresa = :empresa AND (m.dataHora BETWEEN :dataInicio AND :dataFim) AND m.invalido = :invalido");
            tq.setParameter("cartao", cartao);
            tq.setParameter("empresa", empresa);
            tq.setParameter("dataInicio", dataInicio, TemporalType.TIMESTAMP);
            tq.setParameter("dataFim", dataFim, TemporalType.TIMESTAMP);
            tq.setParameter("invalido", invalido);

            return tq.getResultList();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    public Long buscarQuantidadePorRep(Rep rep) throws DaoException {
        try {
            Query tq = this.getEntityManager().createQuery("SELECT COUNT(m) FROM Marcacoes m WHERE m.rep = :rep");
            tq.setParameter("rep", rep);
            List<Long> result = tq.getResultList();
            return result != null && result.size() > 0? result.get(0) : 0l;
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

}
