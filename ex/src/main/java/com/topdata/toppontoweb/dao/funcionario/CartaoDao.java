package com.topdata.toppontoweb.dao.funcionario;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.topdata.toppontoweb.dao.Dao;
import com.topdata.toppontoweb.entity.empresa.Empresa;
import com.topdata.toppontoweb.entity.funcionario.Cartao;
import com.topdata.toppontoweb.entity.funcionario.Funcionario;

/**
 * @version 1.0.6 data 13/09/2016
 * @since 1.0.6 data 13/09/2016
 *
 * @author juliano.ezequiel
 */
@Repository
public class CartaoDao extends Dao<Cartao, Object> {

    public CartaoDao() {
        super(Cartao.class);
    }

    @Transactional(readOnly = true)
    public List<Cartao> buscarCartoesEntreDatas(Funcionario funcionario, Cartao cartao) {
        Query tq = this.getEntityManager()
                .createQuery("SELECT c FROM Cartao c WHERE c.idFuncionario = :idFuncionario "
                        + "AND ((:dataInicio BETWEEN c.dataInicio AND c.dataValidade) "
                        + "OR (:dataValidade BETWEEN c.dataInicio and c.dataValidade)) "
                        + "OR (c.dataInicio >= :dataInicio AND c.dataValidade <= :dataValidade) "
                        + "OR (c.dataInicio<= :dataInicio AND c.dataValidade >= :dataValidade)");

        tq.setParameter("idFuncionario", funcionario);
        tq.setParameter("dataInicio", cartao.getDataInicio());
        tq.setParameter("dataValidade", cartao.getDataValidade());

        return tq.getResultList();
    }

    @Transactional(readOnly = true)
    public List<Cartao> buscarCartoesEntreDatasInicio(Funcionario funcionario, Cartao cartao) {
        Query tq = this.getEntityManager()
                .createQuery("SELECT c FROM Cartao c WHERE c.idFuncionario = :idFuncionario and c.dataInicio = :dataInicio ");

        tq.setParameter("idFuncionario", funcionario);
        tq.setParameter("dataInicio", cartao.getDataInicio());

        return tq.getResultList();
    }

    @Transactional(readOnly = true)
    public List<Cartao> buscarCartoesUtilizadoMesmoPeriodo(Cartao cartao) {
        Query tq = this.getEntityManager()
                .createQuery("SELECT c FROM Cartao c WHERE c.numero = :numero and "
                        + "c.dataInicio <= :dataInicio and c.dataInicio >= :dataValidade");

        tq.setParameter("numero", cartao.getNumero());
        tq.setParameter("dataInicio", cartao.getDataInicio());
        tq.setParameter("dataValidade", cartao.getDataValidade());

        return tq.getResultList();
    }

    @Transactional(readOnly = true)
    public List<Cartao> buscarCartoesUtilizadoMesmoPeriodoInicio(Cartao cartao) {
        Query tq = this.getEntityManager()
                .createQuery("SELECT c FROM Cartao c WHERE c.numero = :numero and "
                        + "c.dataInicio <= :dataInicio and c.dataValidade >= :dataInicio");

        tq.setParameter("numero", cartao.getNumero());
        tq.setParameter("dataInicio", cartao.getDataInicio());

        return tq.getResultList();
    }

    @Transactional(readOnly = true)
    public List<Cartao> buscarPorNumeroDataEmpresa(String numeroCartao, Date dataMarcacao, Empresa empresa) {
        Query tq = this.getEntityManager()
                .createQuery("SELECT DISTINCT c FROM Funcionario f INNER JOIN f.departamento d INNER JOIN f.cartaoList c WHERE c.numero = :numeroCartao AND c.dataInicio <= :dataInicio AND d.empresa = :empresa");

        tq.setParameter("empresa", empresa);
        tq.setParameter("numeroCartao", numeroCartao);
        tq.setParameter("dataInicio", dataMarcacao, TemporalType.DATE);

        return tq.getResultList();
    }

    @Transactional(readOnly = true)
    public List<Cartao> buscarPorNumeroEmpresa(String numeroCartao, Empresa empresa) {
        Query tq = this.getEntityManager()
                .createQuery("SELECT DISTINCT c FROM Funcionario f INNER JOIN f.departamento d INNER JOIN f.cartaoList c WHERE c.numero = :numeroCartao AND d.empresa = :empresa");

        tq.setParameter("empresa", empresa);
        tq.setParameter("numeroCartao", numeroCartao);

        return tq.getResultList();
    }

    @Transactional(readOnly = true)
    public List<Cartao> buscarPorFuncinarioDataInicio(Funcionario funcionario, Date data) {
        Query tq = this.getEntityManager()
                .createQuery("SELECT DISTINCT c FROM Funcionario f INNER JOIN f.cartaoList c WHERE f = :funcionario AND c.dataInicio <= :dataInicio");

        tq.setParameter("funcionario", funcionario);
        tq.setParameter("dataInicio", data, TemporalType.DATE);

        return tq.getResultList();
    }

    @Transactional(readOnly = true)
    public List<Cartao> buscarPorFuncionario(Funcionario funcionario) {
        Query query = this.getEntityManager()
                .createQuery("SELECT c from Cartao c WHERE c.idFuncionario = :funcionario");
        query.setParameter("funcionario", funcionario);
        return query.getResultList();
    }

    @Transactional(readOnly = true)
    public List<Cartao> buscarCartaoDataFuncionario(Funcionario funcionario, Date dataInicio) {
        Query query = this.getEntityManager()
                .createQuery("SELECT c from Cartao c WHERE c.idFuncionario = :funcionario AND c.dataInicio >= :dataInicio");
        query.setParameter("funcionario", funcionario);
        query.setParameter("dataInicio", dataInicio, TemporalType.TIMESTAMP);
        return query.getResultList();
    }

}
