package com.topdata.toppontoweb.services.fechamento;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.dao.fechamento.EmpresaFechamentoDataDao;
import com.topdata.toppontoweb.entity.fechamento.EmpresaFechamentoData;
import com.topdata.toppontoweb.entity.fechamento.EmpresaFechamentoPeriodo;
import com.topdata.toppontoweb.entity.funcionario.Funcionario;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoService;

/**
 *
 * @author juliano.ezequiel
 */
@Service
public class EmpresaFechamentoDataServices extends TopPontoService<EmpresaFechamentoData, Integer> {

    @Autowired
    private EmpresaFechamentoDataDao empresaFechamentoDataDao;

    /**
     * Consulta as EmpresaFechamentoData pelo funcionário e pelo período
     * informado
     *
     * @param funcionario
     * @param dataInicio
     * @param dataFim
     * @return Lista com EmpresaFechamentoData
     * @throws ServiceException
     */
    public List<EmpresaFechamentoData> buscarPorPeriodoFuncionario(Funcionario funcionario, Date dataInicio, Date dataFim) throws ServiceException {
        try {
            return this.empresaFechamentoDataDao.buscarPorPeriodoFuncionario(funcionario, dataInicio, dataFim);
        } catch (DaoException ex) {
            throw new ServiceException(ex);
        }
    }

    /**
     * Consulta as EmpresaFechamentoData pelo empresaFechamentoPeriodo
     *
     * @param efp
     * @return Lista com EmpresaFechamentoData
     * @throws ServiceException
     */
    public List<EmpresaFechamentoData> buscarPorEmpresaFechamentoPeriodo(EmpresaFechamentoPeriodo efp) throws ServiceException {
        try {
            return this.empresaFechamentoDataDao.buscarPorEmpresaFechamentoPeriodo(efp);
        } catch (DaoException ex) {
            throw new ServiceException(ex);
        }
    }

    public long excluirPorId(Integer id) throws ServiceException {
        try {
            return this.empresaFechamentoDataDao.excluirPorId(id);
        } catch (DaoException ex) {
            throw new ServiceException(ex);
        }
    }

    public long excluirPorEmpresaFechamentoPeriodo(EmpresaFechamentoPeriodo empresaFechamentoPeriodo) throws ServiceException {
        try {
            return this.empresaFechamentoDataDao.excluirPorEmpresaFechamentoPeriodo(empresaFechamentoPeriodo);
        } catch (DaoException ex) {
            throw new ServiceException(ex);
        }
    }

    public EmpresaFechamentoDataDao getEmpresaFechamentoDataDao() {
        return empresaFechamentoDataDao;
    }

}
