package com.topdata.toppontoweb.services.permissoes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topdata.toppontoweb.dao.Dao;
import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.entity.empresa.Departamento;
import com.topdata.toppontoweb.entity.empresa.Empresa;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoResponse;
import com.topdata.toppontoweb.services.TopPontoService;
import com.topdata.toppontoweb.dto.EmpresaDepartamentoTransfer;
import com.topdata.toppontoweb.utils.constantes.MSG;

/**
 * @version 1.0.4 data 01/06/2016
 * @since 1.0.4 data 01/06/2016
 *
 * @author juliano.ezequiel
 */
@Service
public class DepartamentoEmpresaGrupoService extends TopPontoService<Departamento, Object> {

    //<editor-fold defaultstate="collapsed" desc="CDI">
    @Autowired
    private Dao dao;

    @Autowired
    private TopPontoResponse topPontoResponse;

    @Autowired
    private ObjectMapper mapper;
    //</editor-fold>

    public String buscarEmpresaDeptTransfer() throws ServiceException {
        try {
            List<EmpresaDepartamentoTransfer> empresaDepartamentoTransfers = new ArrayList<>();
            dao.findAll(Empresa.class).forEach(empresa -> {
                empresaDepartamentoTransfers.add(new EmpresaDepartamentoTransfer((Empresa) empresa));
            });
            return mapper.writeValueAsString(empresaDepartamentoTransfers);
        } catch (DaoException | IOException ex) {
            throw new ServiceException(topPontoResponse.erro(MSG.CADASTRO.ERRO_BUSCAR, Empresa.class), ex);
        }
    }
}
