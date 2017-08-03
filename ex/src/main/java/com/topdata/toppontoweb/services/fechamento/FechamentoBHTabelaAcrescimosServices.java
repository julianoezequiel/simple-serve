package com.topdata.toppontoweb.services.fechamento;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.dao.fechamento.FechamentoBHTabelaAcrescimosDao;
import com.topdata.toppontoweb.entity.fechamento.EmpresaFechamentoData;
import com.topdata.toppontoweb.entity.fechamento.FechamentoBHTabelaAcrescimos;
import com.topdata.toppontoweb.entity.fechamento.FechamentoBHTabelaAcrescimos_;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoService;

/**
 *
 * @author juliano.ezequiel
 */
@Service
public class FechamentoBHTabelaAcrescimosServices extends TopPontoService<FechamentoBHTabelaAcrescimos, Integer> {

    @Autowired
    private FechamentoBHTabelaAcrescimosDao fechamentoBHTabelaAcrescimosDao;
    private HashMap<String, Object> criterio;

    public List<FechamentoBHTabelaAcrescimos> buscarPorEmpresaFechamentodata(EmpresaFechamentoData empresaFechamentoData) throws ServiceException {
        try {
            criterio = new HashMap<>();
            criterio.put(FechamentoBHTabelaAcrescimos_.idEmpresaFechamentoData.getName(), empresaFechamentoData);
            return this.fechamentoBHTabelaAcrescimosDao.findbyAttributes(criterio, FechamentoBHTabelaAcrescimos.class);
        } catch (DaoException ex) {
            throw new ServiceException(ex);
        }
    }

    public long excluirPorEmpresaFechamentoData(EmpresaFechamentoData efd) throws ServiceException {
        return this.fechamentoBHTabelaAcrescimosDao.excluirPorEmpresaFechamentoData(efd);
    }

    public FechamentoBHTabelaAcrescimosDao getFechamentoBHTabelaAcrescimosDao() {
        return fechamentoBHTabelaAcrescimosDao;
    }

}
