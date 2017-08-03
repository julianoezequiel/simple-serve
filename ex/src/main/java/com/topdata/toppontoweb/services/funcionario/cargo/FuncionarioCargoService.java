package com.topdata.toppontoweb.services.funcionario.cargo;

//<editor-fold defaultstate="collapsed" desc="IMPORTS">
import com.topdata.toppontoweb.dao.Dao;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.dao.funcionario.FuncionarioCargoDao;
import com.topdata.toppontoweb.entity.funcionario.Funcionario;
import com.topdata.toppontoweb.entity.funcionario.HistoricoFuncionarioCargo;
import com.topdata.toppontoweb.entity.funcionario.HistoricoFuncionarioCargo_;
import com.topdata.toppontoweb.services.auditoria.AuditoriaServices;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoResponse;
import com.topdata.toppontoweb.services.TopPontoService;
import com.topdata.toppontoweb.services.Validacao;
import com.topdata.toppontoweb.services.ValidacoesCadastro;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES.Enum_FUNCIONALIDADE;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES.Enum_OPERACAO;
import com.topdata.toppontoweb.utils.constantes.MSG;
import javax.inject.Singleton;
//</editor-fold>

/**
 * Classe realiza as regras de negócio para o HistoricoFuncionarioCargo
 *
 * @version 1.0.0.0 data 18/01/2017
 * @since 1.0.0.0 data 04/05/2016
 * @author juliano.ezequiel
 */
@Service
public class FuncionarioCargoService extends TopPontoService<HistoricoFuncionarioCargo, Object>
        implements ValidacoesCadastro<HistoricoFuncionarioCargo, Object> {

    @Autowired
    private FuncionarioCargoDao funcionarioCargoDao;

    //<editor-fold defaultstate="collapsed" desc="VARIÁVEIS">
    private List<Validacao> validacoes;
    private HashMap<String, Object> map;
//</editor-fold>

    public List<HistoricoFuncionarioCargo> buscarPorFuncionario(Integer id) throws ServiceException {
        try {
            this.map = new HashMap<>();
            return funcionarioCargoDao.findbyAttributes(this.map, HistoricoFuncionarioCargo.class);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erro(MSG.CADASTRO.ERRO_LISTAR, HistoricoFuncionarioCargo.class), ex);
        }
    }

    public List<HistoricoFuncionarioCargo> buscarPorCargo(Integer id) throws ServiceException {
        try {
            this.map = new HashMap<>();
            return funcionarioCargoDao.findbyAttributes(this.map, HistoricoFuncionarioCargo.class);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erro(MSG.CADASTRO.ERRO_BUSCAR, HistoricoFuncionarioCargo.class), ex);
        }
    }

    @Override
    public HistoricoFuncionarioCargo buscar(Class<HistoricoFuncionarioCargo> entidade, Object id) throws ServiceException {
        try {
            HistoricoFuncionarioCargo hc = this.funcionarioCargoDao.find(HistoricoFuncionarioCargo.class, id);
            if (hc == null) {
                throw new ServiceException(this.getTopPontoResponse().alertaNaoCad(new HistoricoFuncionarioCargo().toString()));
            }
            return hc;
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroBuscar(), ex);
        }
    }

    @Override
    public Response atualizar(HistoricoFuncionarioCargo funcionarioCargo) throws ServiceException {
        try {
            if (this.validarAtualizar(funcionarioCargo) != null) {
                this.funcionarioCargoDao.save(funcionarioCargo);
                this.getAuditoriaServices().auditar(Enum_FUNCIONALIDADE.CAD_FUNCIONARIO, null, Enum_OPERACAO.EDITAR, funcionarioCargo);
            }
            return this.getTopPontoResponse().sucessoAtualizar(funcionarioCargo.toString());
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().sucessoAtualizar(new HistoricoFuncionarioCargo().toString()), ex);
        }
    }

    @Override
    public Response excluir(Class<HistoricoFuncionarioCargo> c, Object key) throws ServiceException {
        try {
            HistoricoFuncionarioCargo funcionarioCargo = this.validarExcluir(new HistoricoFuncionarioCargo((Integer) key));
            this.funcionarioCargoDao.delete(key);
            this.getAuditoriaServices().auditar(Enum_FUNCIONALIDADE.CAD_FUNCIONARIO, null, Enum_OPERACAO.EXCLUIR, funcionarioCargo);
            return this.getTopPontoResponse().sucessoExcluir(HistoricoFuncionarioCargo.class);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroExcluir(HistoricoFuncionarioCargo.class), ex);
        }
    }

    @Override
    public HistoricoFuncionarioCargo validarSalvar(HistoricoFuncionarioCargo t) throws ServiceException {
        return t;
    }

    @Override
    public HistoricoFuncionarioCargo validarExcluir(HistoricoFuncionarioCargo t) throws ServiceException {
        VNaoCadastrado(t);
        return t;
    }

    @Override
    public HistoricoFuncionarioCargo validarAtualizar(HistoricoFuncionarioCargo funcionarioCargo) throws ServiceException {
        try {

            this.map = new HashMap<>();
            this.map.put(HistoricoFuncionarioCargo_.cargo.getName(), funcionarioCargo.getCargo());
            this.map.put(HistoricoFuncionarioCargo_.funcionario.getName(), funcionarioCargo.getFuncionario());
            this.map.put(HistoricoFuncionarioCargo_.descricao.getName(), funcionarioCargo.getCargo().getDescricao());
            List<HistoricoFuncionarioCargo> list = this.funcionarioCargoDao.findbyAttributes(this.map, HistoricoFuncionarioCargo.class);
            return list.size() > 0 ? null : funcionarioCargo;

        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroValidar(funcionarioCargo.toString()), ex);
        }
    }

    HistoricoFuncionarioCargo buscaUltimoCargo(Funcionario funcionario) {
        return this.funcionarioCargoDao.buscaUltimoCargo(funcionario);
    }

    /**
     * Verifica não existe na base de dados
     */
    private HistoricoFuncionarioCargo VNaoCadastrado(HistoricoFuncionarioCargo fc) throws ServiceException {

        if (fc == null) {
            throw new ServiceException(this.getTopPontoResponse().alertaNaoCad(HistoricoFuncionarioCargo.class));
        }
        return fc;
    }

}
