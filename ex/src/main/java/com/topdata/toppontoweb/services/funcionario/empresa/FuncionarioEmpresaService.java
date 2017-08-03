package com.topdata.toppontoweb.services.funcionario.empresa;

//<editor-fold defaultstate="collapsed" desc="IMPORTS">
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.dao.funcionario.FuncionarioEmpresaDao;
import com.topdata.toppontoweb.entity.funcionario.FuncionarioEmpresa;
import com.topdata.toppontoweb.entity.funcionario.FuncionarioEmpresa_;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoService;
import com.topdata.toppontoweb.services.Validacao;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;
//</editor-fold>

/**
 * @version 1.0.0.0 data 18/01/2017
 * @since 1.0.0.0 data 23/08/2016
 *
 * @author juliano.ezequiel
 */
@Service
public class FuncionarioEmpresaService extends TopPontoService<FuncionarioEmpresa, Object>
        implements Validacao<FuncionarioEmpresa, Object> {

    @Autowired
    private FuncionarioEmpresaDao funcionarioEmpresaDao;

    private HashMap<String, Object> map;

    @Override
    public Response atualizar(FuncionarioEmpresa funcionarioEmpresa) throws ServiceException {
        try {
            if (this.validar(funcionarioEmpresa, null) != null) {
                funcionarioEmpresa = this.funcionarioEmpresaDao.save(funcionarioEmpresa);
                this.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_FUNCIONARIO, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.EDITAR, funcionarioEmpresa);
            }
            return this.getTopPontoResponse().sucessoAtualizar(funcionarioEmpresa.toString(), funcionarioEmpresa);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroBuscar(), ex);
        }
    }

    @Override
    public FuncionarioEmpresa validar(FuncionarioEmpresa funcionarioEmpresa, Object i) throws ServiceException {

        try {
            this.map = new HashMap<>();
            this.map.put(FuncionarioEmpresa_.empresa.getName(), funcionarioEmpresa.getEmpresa());
            this.map.put(FuncionarioEmpresa_.funcionario.getName(), funcionarioEmpresa.getFuncionario());
            List<FuncionarioEmpresa> list = funcionarioEmpresaDao.findbyAttributes(this.map, FuncionarioEmpresa.class);
            return list.size() > 0 ? null : funcionarioEmpresa;
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroValidar(funcionarioEmpresa.toString()), ex);
        }

    }

}
