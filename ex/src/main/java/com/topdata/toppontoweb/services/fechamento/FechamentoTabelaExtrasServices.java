package com.topdata.toppontoweb.services.fechamento;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.dao.fechamento.FechamentoTabelaExtrasDao;
import com.topdata.toppontoweb.entity.fechamento.EmpresaFechamentoData;
import com.topdata.toppontoweb.entity.fechamento.FechamentoTabelaExtras;
import com.topdata.toppontoweb.entity.fechamento.FechamentoTabelaExtras_;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoService;

/**
 *
 * @author juliano.ezequiel
 */
@Service
public class FechamentoTabelaExtrasServices extends TopPontoService<FechamentoTabelaExtras, Integer> {

    @Autowired
    private FechamentoTabelaExtrasDao fechamentoTabelaExtrasDao;
    private HashMap<String, Object> criterio;

    public List<FechamentoTabelaExtras> buscarPorEmpresaFechamentodata(EmpresaFechamentoData empresaFechamentoData) throws ServiceException {
        try {
            criterio = new HashMap<>();
            criterio.put(FechamentoTabelaExtras_.idEmpresaFechamentoData.getName(), empresaFechamentoData);
            return this.fechamentoTabelaExtrasDao.findbyAttributes(criterio, FechamentoTabelaExtras.class);
        } catch (DaoException ex) {
            throw new ServiceException(ex);
        }
    }

    public long excluirPorEmpresaFechamentoData(EmpresaFechamentoData efd) throws ServiceException {
        return this.fechamentoTabelaExtrasDao.excluirPorEmpresaFechamentoData(efd);
    }

    public FechamentoTabelaExtrasDao getFechamentoTabelaExtrasDao() {
        return fechamentoTabelaExtrasDao;
    }

}
