package com.topdata.toppontoweb.dao.funcionario;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.topdata.toppontoweb.dao.Dao;
import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.dto.funcionario.FuncionarioPaginacaoTransfer;
import com.topdata.toppontoweb.entity.bancohoras.BancoHoras;
import com.topdata.toppontoweb.entity.calendario.Calendario;
import com.topdata.toppontoweb.entity.empresa.Cei;
import com.topdata.toppontoweb.entity.empresa.Departamento;
import com.topdata.toppontoweb.entity.empresa.Empresa;
import com.topdata.toppontoweb.entity.funcionario.Cartao;
import com.topdata.toppontoweb.entity.funcionario.Funcionario;
import com.topdata.toppontoweb.entity.funcionario.HistoricoFuncionarioCargo;
import com.topdata.toppontoweb.utils.Utils;
import java.text.DateFormat;
import java.util.Date;
import javax.persistence.TemporalType;
import org.springframework.transaction.annotation.Transactional;

/**
 * @version 1.0.4 data 24/08/2016
 * @since 1.0.4 data 24/08/2016
 *
 * @author juliano.ezequiel
 */
@Repository
public class FuncionarioDao extends Dao<Funcionario, Object> {

    public FuncionarioDao() {
        super(Funcionario.class);
    }

    public List<Funcionario> buscarPorEmpresaPeriodoMarcacoesInvalidas(Empresa empresa, Date dataInicio, Date dataFim) {

        Query q = this.getEntityManager().createQuery("SELECT f FROM Funcionario f INNER JOIN f.departamento d INNER JOIN d.empresa e INNER JOIN e.marcacoesList m WHERE e = :empresa AND (m.dataHora BETWEEN :dataInicio AND :dataFim) AND m.invalido = :invalido AND f.pis = m.pis");
        q.setParameter("empresa", empresa);
        q.setParameter("dataInicio", dataInicio);
        q.setParameter("dataFim", dataFim);
        q.setParameter("invalido", true);

        return q.getResultList();
    }

    @Transactional(readOnly = true)
    public List<Funcionario> buscarSemOperador() {
        Query tq = this.getEntityManager()
                .createQuery("SELECT f FROM Funcionario f WHERE f.idOperador IS NULL and f.ativo = true");

        List<Funcionario> list = tq.getResultList();
        return list;
    }

    @Transactional(readOnly = true)
    public List<Funcionario> buscarPorCei(Cei cei) {
        Query tq = this.getEntityManager()
                .createQuery("SELECT f FROM Funcionario f WHERE f.cei.descricao = :numeroCei");
        tq.setParameter("numeroCei", cei.getDescricao());

        List<Funcionario> list = tq.getResultList();
        return list;
    }

    @Transactional(readOnly = true)
    public List<Funcionario> findByFuncionarioDepartamento(List<Funcionario> funcionarioList, Departamento departamento) throws DaoException {
        try {
            String sql = "SELECT f FROM Funcionario f WHERE f.departamento.idDepartamento = :idDepartamento AND f IN (:funcionarioList)";

            final List resultList = new ArrayList();
            int particionador = 1000;
            for (int i = 0; i < funcionarioList.size(); i += particionador) {
                int particionadorProximo = i + particionador;
                List<Funcionario> subList = funcionarioList.subList(i, (particionadorProximo < funcionarioList.size() ? particionadorProximo : funcionarioList.size()));
                Query query = this.getEntityManager().createQuery(sql);
                query.setParameter("funcionarioList", subList);
                query.setParameter("idDepartamento", departamento.getIdDepartamento());

                resultList.addAll(query.getResultList());
            }

            return resultList;
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getSimpleName()).log(Level.SEVERE, "ERRO BUSCAR : {0}", "Funcionario");
            throw new DaoException(ex);
        }
    }

    @Transactional(readOnly = true)
    public List<Funcionario> buscarPorEmpresa(Empresa empresa) throws DaoException {
        try {
            Query query;
            query = this.getEntityManager().createQuery("SELECT f FROM Funcionario f INNER JOIN f.departamento fd WHERE fd.empresa = :empresa");
            query.setParameter("empresa", empresa);
            return query.getResultList();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Transactional(readOnly = true)
    public List<Funcionario> buscarPorPaginacao(FuncionarioPaginacaoTransfer fpt) throws DaoException {
        try {

            String strQuery = "SELECT f "
                    + "FROM Funcionario f INNER JOIN f.departamento d "
                    + "WHERE (f.nome LIKE :busca OR f.matricula LIKE :busca) ";
            if (fpt.getEmpresa() != null) {
                strQuery += " AND d.empresa = :empresa ";
            } else if (fpt.isEmpresaAtiva()) {
                strQuery += " AND d.empresa.ativo = TRUE ";
            }

            if (fpt.getDepartamento() != null) {
                strQuery += " AND d = :departamento ";
            } else if (fpt.isDepartamentoAtivo()) {
                strQuery += " AND d.ativo = TRUE ";
            }

            if (fpt.isFuncionarioAtivo()) {
                strQuery += " AND f.ativo = TRUE ";
            }

            strQuery += " ORDER BY f.nome";

            Integer inicioRegistros = (fpt.getPagina() - 1) * fpt.getQntPorPagina();

            Query query;
            query = this.getEntityManager().createQuery(strQuery);
            query.setParameter("busca", "%" + fpt.getBusca() + "%");

            if (fpt.getEmpresa() != null) {
                query.setParameter("empresa", fpt.getEmpresa());
            }

            if (fpt.getDepartamento() != null) {
                query.setParameter("departamento", fpt.getDepartamento());
            }

            query.setFirstResult(inicioRegistros);
            query.setMaxResults(fpt.getQntPorPagina());
            return query.getResultList();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    /**
     * Utilizada pelo Data table.
     *
     * @param busca
     * @param inicioRegistros
     * @param qntPorPagina
     * @param coluna
     * @param direcao
     * @return
     * @throws com.topdata.toppontoweb.dao.DaoException
     */
    public List<Funcionario> buscarPorPaginacao(String busca, Integer inicioRegistros, Integer qntPorPagina, String coluna, String direcao) throws DaoException {
        return buscarPorPaginacao(busca, inicioRegistros, qntPorPagina, coluna, direcao, false);
    }

    /**
     * Utilizada pelo Data table.
     *
     * @param busca
     * @param inicioRegistros
     * @param qntPorPagina
     * @param coluna
     * @param direcao
     * @param buscarTotal
     * @return
     * @throws com.topdata.toppontoweb.dao.DaoException
     */
    @Transactional(readOnly = true)
    public List buscarPorPaginacao(String busca, Integer inicioRegistros, Integer qntPorPagina, String coluna, String direcao, boolean buscarTotal) throws DaoException {
        try {

            String strQuery = "SELECT ";
            strQuery += buscarTotal ? " COUNT(funcionario) " : " funcionario ";
            strQuery += "FROM Funcionario funcionario INNER JOIN funcionario.departamento departamento "
                    + "     INNER JOIN departamento.empresa empresa "
                    + "     LEFT JOIN funcionario.cargo cargo ";
            Date buscaData = null;
            if (busca != null && busca.length() > 0) {
                buscaData = Utils.convertStringToDate(busca);
                strQuery += "WHERE (funcionario.nome LIKE :busca "
                        + "OR funcionario.pis LIKE :busca ";

                if (buscaData != null) {
                    strQuery += "OR funcionario.dataAdmissao LIKE :buscaData ";
                }

                strQuery += "OR empresa.razaoSocial LIKE :busca "
                        + "OR departamento.descricao LIKE :busca "
                        + "OR (cargo.idCargo IS NOT NULL AND cargo.descricao LIKE :busca)) ";
            }

            if (!buscarTotal) {
                strQuery += " ORDER BY " + coluna + " " + direcao + " ";
            }

            Query query;
            query = this.getEntityManager().createQuery(strQuery);
            if (busca != null && busca.length() > 0) {
                query.setParameter("busca", "%" + busca + "%");
                //Converte uma possivel string de busca em uma string para dastas
                if (buscaData != null) {
                    query.setParameter("buscaData", buscaData);
                }

            }
            if (!buscarTotal) {
                query.setFirstResult(inicioRegistros);
                query.setMaxResults(qntPorPagina);
            }

            return query.getResultList();
        } catch (Exception e) {

            throw new DaoException(e);
        }
    }

    /**
     * Utilizada pelo Data table.
     *
     * @param busca
     * @return
     * @throws com.topdata.toppontoweb.dao.DaoException
     */
    public Long buscarPorPaginacaoTotal(String busca) throws DaoException {
        List lista = buscarPorPaginacao(busca, 0, 0, "", "", true);
        return lista.size() > 0 ? (Long) lista.get(0) : 0l;
    }

    /**
     * Utilizada pelo Data table.
     *
     * @return
     * @throws com.topdata.toppontoweb.dao.DaoException
     */
    public Long buscarPorPaginacaoTotal() throws DaoException {
        return buscarPorPaginacaoTotal(null);
    }

    @Transactional(readOnly = true)
    public Funcionario buscarPorCartao(Cartao cartao) throws DaoException {
        try {
            String queryStr = "SELECT f "
                    + "FROM Funcionario f INNER JOIN f.cartaoList c "
                    + "WHERE c.idCartao = :idCartao ";
            Query query;
            query = this.getEntityManager().createQuery(queryStr);
            query.setParameter("idCartao", cartao.getIdCartao());
            List<Funcionario> funcionarioList = query.getResultList();
            return !funcionarioList.isEmpty() ? funcionarioList.get(0) : null;
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Transactional(readOnly = true)
    public List buscarPorEmpresaPossuiCartao(Empresa empresa) throws DaoException {
        try {
            String queryStr = "SELECT DISTINCT f "
                    + "FROM Funcionario f INNER JOIN f.departamento d INNER JOIN d.empresa e INNER JOIN f.cartaoList c "
                    + "WHERE c IS NOT NULL AND e = :empresa ";
            Query query;
            query = this.getEntityManager().createQuery(queryStr);
            query.setParameter("empresa", empresa);
            return query.getResultList();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    /**
     * Lista os funcionário pelo calendário
     *
     * @param calendario
     * @return
     * @throws DaoException
     */
    @Transactional(readOnly = true)
    public List<Funcionario> buscarPorCalendario(Calendario calendario) throws DaoException {
        try {
            Query query = this.getEntityManager().createQuery("SELECT f from Funcionario f INNER JOIN f.funcionarioCalendarioList fc WHERE fc.calendario = :calendario");
            query.setParameter("calendario", calendario);
            return query.getResultList();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    /**
     * Lista os funcionário pelo bando de horas
     *
     * @param bancoHoras
     * @return
     * @throws DaoException
     */
    @Transactional(readOnly = true)
    public List<Funcionario> buscarPorBancoHoras(BancoHoras bancoHoras) throws DaoException {
        try {
            Query query = this.getEntityManager().createQuery("SELECT f from Funcionario f INNER JOIN f.funcionarioBancoHorasList fbh WHERE fbh.bancoHoras = :bancohoras");
            query.setParameter("bancohoras", bancoHoras);
            return query.getResultList();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Transactional(readOnly = true)
    public Funcionario findByPisEmpresa(String pis, Empresa empresa) throws DaoException {
        try {
            Query query = this.getEntityManager().createQuery("SELECT f from Funcionario f INNER JOIN f.departamento d INNER JOIN d.empresa e WHERE f.pis = :pis AND e = :empresa ");
            query.setParameter("pis", pis);
            query.setParameter("empresa", empresa);
            List<Funcionario> resultList = query.getResultList();
            return resultList.size() > 0 ? resultList.get(0) : null;
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    public HistoricoFuncionarioCargo buscarHistoricoFuncionarioCargoRetroativo(Funcionario funcionario, Date dataCargo) throws DaoException {
        try {
            Query query = this.getEntityManager().createQuery("SELECT fh from HistoricoFuncionarioCargo fh INNER JOIN fh.funcionario f WHERE f = :funcionario AND  fh.dataCargo <= :dataCargo ORDER BY fh DESC");
            query.setParameter("funcionario", funcionario);
            query.setParameter("dataCargo", dataCargo, TemporalType.TIMESTAMP);
            List<HistoricoFuncionarioCargo> resultList = query.getResultList();
            return resultList.size() > 0 ? resultList.get(0) : null;
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

}
