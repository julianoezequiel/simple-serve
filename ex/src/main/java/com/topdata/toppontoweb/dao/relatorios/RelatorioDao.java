package com.topdata.toppontoweb.dao.relatorios;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.topdata.toppontoweb.dao.Dao;
import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.dto.funcionario.FuncionarioPaginacaoTransfer;
import com.topdata.toppontoweb.dto.relatorios.RelatoriosGeraFrequenciaTransfer;
import com.topdata.toppontoweb.dto.relatorios.cadastros.RelatorioCadastroTransfer;
import com.topdata.toppontoweb.entity.Entidade;
import com.topdata.toppontoweb.entity.empresa.Departamento;
import com.topdata.toppontoweb.entity.empresa.Empresa;
import com.topdata.toppontoweb.entity.funcionario.Funcionario;

/**
 *
 * @author tharle.camargo
 */
@Repository
public class RelatorioDao extends Dao<Entidade, Object> {

    public RelatorioDao() {
        super(Entidade.class);
    }
    
    public List<Funcionario> buscarFuncinarioPorFiltroRelatorio(RelatoriosGeraFrequenciaTransfer relatorioTransfer) throws DaoException {
        return this.buscarFuncinarioPorFiltroRelatorio(relatorioTransfer.getEmpresa(), relatorioTransfer.getDepartamento(), relatorioTransfer.isEmpresaAtiva(), relatorioTransfer.isDepartamentoAtivo(), relatorioTransfer.isFuncionarioAtivo());
    }
    
    public List<Funcionario> buscarFuncinarioPorFiltroRelatorio(RelatorioCadastroTransfer relatorioTransfer) throws DaoException {
        return this.buscarFuncinarioPorFiltroRelatorio(relatorioTransfer.getEmpresa(), relatorioTransfer.getDepartamento(), relatorioTransfer.isEmpresaAtiva(), relatorioTransfer.isDepartamentoAtivo(), relatorioTransfer.isFuncionarioAtivo());
    }
    
    @Transactional(readOnly = true)
    public List<Funcionario> buscarFuncinarioPorFiltroRelatorio(Empresa empresa, Departamento departamento, boolean empresaAtiva, boolean departamentoAtivo, boolean funcionarioAtivo) throws DaoException {
        try {
            
            String strQuery = "SELECT f "
                            + "FROM Funcionario f INNER JOIN f.departamento d ";
            
            String separador = " WHERE";
            if(empresa != null){
                strQuery+= separador+" d.empresa = :empresa ";
                separador = " AND";
            }else if(empresaAtiva){
                strQuery+= separador+" d.empresa.ativo = TRUE ";
                separador = " AND";
            }
            
            if(departamento != null){
                strQuery+= separador+" d = :departamento ";
                separador = " AND";
            }else if(departamentoAtivo){
                strQuery+= separador+" d.ativo = TRUE ";
                separador = " AND";
            }
            
            if(funcionarioAtivo){
                strQuery+= separador+" f.ativo = TRUE ";
            }
            
            strQuery+= " ORDER BY f.nome";           
            
            Query query;
            query = this.getEntityManager().createQuery(strQuery);
            
            if(empresa != null){
                query.setParameter("empresa", empresa);
            }
            
            if(departamento != null){
                query.setParameter("departamento", departamento);
            }
            
            return query.getResultList();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Transactional(readOnly = true)
    private synchronized List<Funcionario> buscarLista(List<Funcionario> funcionarioList) throws DaoException {
        try {
            String sql = "SELECT DISTINCT f  FROM Funcionario f WHERE f IN (:funcionarioList)";
            Query query = this.getEntityManager().createQuery(sql);
            query.setParameter("funcionarioList", funcionarioList);
            return query.getResultList();
        } catch (Exception e) {
            LOGGER.error(this.getClass().getSimpleName(), e);
            throw new DaoException(e);
        }
    }

}
