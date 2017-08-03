package com.topdata.toppontoweb.services.fechamento;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.dao.fechamento.FechamentoTabelaExtrasDSRDao;
import com.topdata.toppontoweb.entity.fechamento.EmpresaFechamentoData;
import com.topdata.toppontoweb.entity.fechamento.FechamentoTabelaExtrasDSR;
import com.topdata.toppontoweb.entity.fechamento.FechamentoTabelaExtrasDSR_;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoService;

/**
 *
 * @author juliano.ezequiel
 */
@Service
public class FechamentoTabelaExtrasDSRServices extends TopPontoService<FechamentoTabelaExtrasDSR, Integer> {

    @Autowired
    private FechamentoTabelaExtrasDSRDao fechamentoTabelaExtrasDSRDao;
    private HashMap<String, Object> criterio;

    public List<FechamentoTabelaExtrasDSR> buscarPorEmpresaFechamentodata(EmpresaFechamentoData empresaFechamentoData) throws ServiceException {
        try {
            criterio = new HashMap<>();
            criterio.put(FechamentoTabelaExtrasDSR_.idEmpresaFechamentoData.getName(), empresaFechamentoData);
            return this.fechamentoTabelaExtrasDSRDao.findbyAttributes(criterio, FechamentoTabelaExtrasDSR.class);
        } catch (DaoException ex) {
            throw new ServiceException(ex);
        }
    }

    public long excluirPorEmpresaFechamentoData(EmpresaFechamentoData efd) throws ServiceException {
        return this.fechamentoTabelaExtrasDSRDao.excluirPorEmpresaFechamentoData(efd);
    }

    public FechamentoTabelaExtrasDSRDao getFechamentoTabelaExtrasDSRDao() {
        return fechamentoTabelaExtrasDSRDao;
    }

}
