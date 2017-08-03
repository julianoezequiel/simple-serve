package com.topdata.toppontoweb.services.fechamento;

import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.dao.fechamento.FechamentoBHTabelaSomenteNoturnasDao;
import com.topdata.toppontoweb.entity.fechamento.EmpresaFechamentoData;
import com.topdata.toppontoweb.entity.fechamento.FechamentoBHTabelaSomenteNoturnas;
import com.topdata.toppontoweb.entity.fechamento.FechamentoBHTabelaSomenteNoturnas_;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoService;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author juliano.ezequiel
 */
@Service
public class FechamentoBHTabelaSomenteNoturnasServices extends TopPontoService<FechamentoBHTabelaSomenteNoturnas, Integer> {

    @Autowired
    private FechamentoBHTabelaSomenteNoturnasDao fechamentoBHTabelaSomenteNoturnasDao;
    private HashMap<String, Object> criterio;

    public List<FechamentoBHTabelaSomenteNoturnas> buscarPorEmpresaFechamentodata(EmpresaFechamentoData empresaFechamentoData) throws ServiceException {
        try {
            criterio = new HashMap<>();
            criterio.put(FechamentoBHTabelaSomenteNoturnas_.idEmpresaFechamentoData.getName(), empresaFechamentoData);
            return this.fechamentoBHTabelaSomenteNoturnasDao.findbyAttributes(criterio, FechamentoBHTabelaSomenteNoturnas.class);
        } catch (DaoException ex) {
            throw new ServiceException(ex);
        }
    }

    public long excluirPorEmpresaFechamentoData(EmpresaFechamentoData efd) throws ServiceException {
        return this.fechamentoBHTabelaSomenteNoturnasDao.excluirPorEmpresaFechamentoData(efd);
    }

    public FechamentoBHTabelaSomenteNoturnasDao getFechamentoBHTabelaSomenteNoturnasDao() {
        return fechamentoBHTabelaSomenteNoturnasDao;
    }

}
