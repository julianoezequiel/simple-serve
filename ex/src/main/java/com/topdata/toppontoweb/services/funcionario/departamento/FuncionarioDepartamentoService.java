package com.topdata.toppontoweb.services.funcionario.departamento;

//<editor-fold defaultstate="collapsed" desc="IMPORTS">
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.dao.funcionario.FuncionarioDepartamentoDao;
import com.topdata.toppontoweb.entity.funcionario.Funcionario;
import com.topdata.toppontoweb.entity.funcionario.FuncionarioDepartamento;
import com.topdata.toppontoweb.entity.funcionario.FuncionarioDepartamento_;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoService;
import com.topdata.toppontoweb.services.Validacao;
import com.topdata.toppontoweb.services.ValidacoesCadastro;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;
import com.topdata.toppontoweb.utils.constantes.MSG;
//</editor-fold>

/**
 * @version 1.0.0.0 data 18/01/2017
 * @since 1.0.0.0 data 23/08/2016
 *
 * @author juliano.ezequiel
 */
@Service
public class FuncionarioDepartamentoService extends TopPontoService<FuncionarioDepartamento, Object>
        implements ValidacoesCadastro<FuncionarioDepartamento, Object> {

    @Autowired
    private FuncionarioDepartamentoDao funcionarioDepartamentoDao;

    private HashMap<String, Object> map;

    @Override
    public Response atualizar(FuncionarioDepartamento entidade) throws ServiceException {
        try {
            if (this.validarAtualizar(entidade) != null) {
                entidade = this.funcionarioDepartamentoDao.save(entidade);
                this.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_FUNCIONARIO, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.EDITAR, entidade);
            }
            return this.getTopPontoResponse().sucessoAtualizar(entidade.toString());
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erro(MSG.CADASTRO.ERRO_BUSCAR.getResource(), new FuncionarioDepartamento().toString()), ex);
        }
    }

    @Override
    public FuncionarioDepartamento validarSalvar(FuncionarioDepartamento t) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public FuncionarioDepartamento validarExcluir(FuncionarioDepartamento t) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public FuncionarioDepartamento validarAtualizar(FuncionarioDepartamento funcionarioDepartamento) throws ServiceException {
        try {
            this.map = new HashMap<>();
            this.map.put(FuncionarioDepartamento_.departamento.getName(), funcionarioDepartamento.getDepartamento());
            this.map.put(FuncionarioDepartamento_.funcionario.getName(), funcionarioDepartamento.getFuncionario());
            this.map.put(FuncionarioDepartamento_.descricao.getName(), funcionarioDepartamento.getDepartamento().getDescricao());
            List<FuncionarioDepartamento> list = funcionarioDepartamentoDao.findbyAttributes(this.map, FuncionarioDepartamento.class);
            return list.size() > 0 ? null : funcionarioDepartamento;
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroValidar(funcionarioDepartamento.toString()), ex);
        }
    }

    FuncionarioDepartamento buscaUltimoDepartamento(Funcionario funcionario) {
        return this.funcionarioDepartamentoDao.buscaUltimoDepartamento(funcionario);
    }

}
