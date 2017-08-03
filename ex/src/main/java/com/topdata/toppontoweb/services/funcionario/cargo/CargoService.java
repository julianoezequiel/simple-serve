package com.topdata.toppontoweb.services.funcionario.cargo;

//<editor-fold defaultstate="collapsed" desc="IMPORTS">
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.core.Response;

import org.springframework.stereotype.Service;

import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.entity.funcionario.Cargo;
import com.topdata.toppontoweb.entity.funcionario.Cargo_;
import com.topdata.toppontoweb.entity.funcionario.Funcionario;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoService;
import com.topdata.toppontoweb.services.Validacao;
import com.topdata.toppontoweb.services.ValidacoesCadastro;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES.Enum_AUDITORIA;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES.Enum_FUNCIONALIDADE;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES.Enum_OPERACAO;
import com.topdata.toppontoweb.utils.constantes.MSG;
import java.util.Date;
//</editor-fold>

/**
 * Classe realiza as regras de negócio para o Cargo
 *
 * @version 1.0.0.0 data 18/01/2017
 * @version 1.0.0.0 data 04/05/2016
 * @since 1.0.0.0 data 03/05/2016
 * @author juliano.ezequiel
 */
@Service
public class CargoService extends TopPontoService<Cargo, Object>
        implements ValidacoesCadastro<Cargo, Object> {

    //<editor-fold defaultstate="collapsed" desc="VARIÁVEIS">
    private List<Validacao> validacoes = new ArrayList<>();
    private HashMap<String, Object> map = new HashMap<>();
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="CONSULTAS">
    /**
     * Realiza a consulta na base de dados de um cargo pela sua descrição
     *
     * @param cargo com a descrição
     * @return Cargo
     * @throws ServiceException
     */
    public Cargo buscaPorDescricao(Cargo cargo) throws ServiceException {
        try {
            map = new HashMap<>();
            map.put(Cargo_.descricao.getName(), cargo.getDescricao());
            List<Cargo> cargos = this.getDao().findbyAttributes(map, Cargo.class);
            return cargos.isEmpty() ? null : cargos.get(0);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erro(MSG.CADASTRO.ERRO_BUSCAR, Cargo.class), ex);
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="CRUD">
    @Override
    public Cargo buscar(Class<Cargo> entidade, Object id) throws ServiceException {
        try {
            Cargo c = (Cargo) this.getDao().find(Cargo.class, id);
            if (c == null) {
                throw new ServiceException(this.getTopPontoResponse().alertaNaoCad(new Cargo().toString()));
            }
            return c;
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroBuscar(), ex);
        }
    }

    @Override
    public Response salvar(Cargo cargo) throws ServiceException {
        try {
            cargo = (Cargo) this.getDao().save(validarSalvar(cargo));
            this.getAuditoriaServices().auditar(Enum_FUNCIONALIDADE.CAD_FUNCIONARIO, Enum_AUDITORIA.CAD_CARGO, Enum_OPERACAO.INCLUIR, cargo);
            return this.getTopPontoResponse().sucessoSalvar(cargo.toString(), cargo);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroSalvar(new Cargo().toString()), ex);
        }
    }

    @Override
    public Response atualizar(Cargo cargo) throws ServiceException {
        try {
            cargo = (Cargo) this.getDao().save(validarAtualizar(cargo));
            this.getAuditoriaServices().auditar(Enum_FUNCIONALIDADE.CAD_FUNCIONARIO, Enum_AUDITORIA.CAD_CARGO, Enum_OPERACAO.EDITAR, cargo);
            return this.getTopPontoResponse().sucessoAtualizar(cargo.toString(), cargo);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroAtualizar(new Cargo().toString()), ex);
        }
    }

    @Override
    public Response excluir(Class<Cargo> c, Object id) throws ServiceException {
        try {
            Cargo cargo = validarExcluir(new Cargo((Integer) id));
            this.getDao().delete(cargo);
            this.getAuditoriaServices().auditar(Enum_FUNCIONALIDADE.CAD_FUNCIONARIO, Enum_AUDITORIA.CAD_CARGO, Enum_OPERACAO.EXCLUIR, cargo);
            return this.getTopPontoResponse().sucessoExcluir(cargo.toString());
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroExcluir(new Cargo().toString()), ex);
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="VALIDAÇÕES OBRIGATÓRIAS">
    @Override
    public Cargo validarSalvar(Cargo cargo) throws ServiceException {
        if (cargo.getDescricao() == null) {
            throw new ServiceException(this.getTopPontoResponse().campoObrigatorio(Cargo_.descricao.getName()));
        }
        validarJaCadastrado(cargo);
        return cargo;
    }

    @Override
    public Cargo validarExcluir(Cargo cargo) throws ServiceException {
        try {
            map = new HashMap<>();
            map.put(Cargo_.descricao.getName(), cargo.getDescricao());
            map.put(Cargo_.idCargo.getName(), cargo.getIdCargo());
            List<Cargo> cargos = this.getDao().findbyAttributes(map, Cargo.class);
            if (cargos.isEmpty()) {
                throw new ServiceException(this.getTopPontoResponse().alertaNaoCad(new Cargo().toString()));
            }
            cargo = cargos.iterator().next();
            if (!cargo.getFuncionarioList().isEmpty()) {
                throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.FUNCIONARIO.ALERTA_CARGO_EM_USO.getResource()));
            }
        } catch (DaoException ex) {
            Logger.getLogger(CargoService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cargo;
    }

    @Override
    public Cargo validarAtualizar(Cargo cargo) throws ServiceException {

        VIdentificador(cargo);
        VAlteracaoJaCadatrado(cargo);

        return cargo;
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="REGRAS DE VALIDAÇÕES">
    private Cargo VAlteracaoJaCadatrado(Cargo cargo) throws ServiceException {

        if (cargo.getDescricao() == null) {
            throw new ServiceException(this.getTopPontoResponse().campoObrigatorio(Cargo_.descricao.getName()));
        }
        Cargo c = buscar(Cargo.class, cargo.getIdCargo());
        Cargo c1 = buscaPorDescricao(cargo);
        if (c1 != null && !c.getIdCargo().equals(c1.getIdCargo())) {
            throw new ServiceException(this.getTopPontoResponse().alertaJaCad(cargo.toString()));
        }
        return cargo;
    }

    public Cargo VIdentificador(Cargo cargo) throws ServiceException {
        if (cargo == null || cargo.getIdCargo() == null) {
            throw new ServiceException(this.getTopPontoResponse().alertaNaoCad(cargo.toString()));
        }
        return cargo;
    }

    private void validarJaCadastrado(Cargo cargo) throws ServiceException {
        try {
            this.map = new HashMap<>();
            this.map.put(Cargo_.descricao.getName(), cargo.getDescricao());
            List<Cargo> cargoList = this.getDao().findbyAttributes(this.map, Cargo.class);
            if (!cargoList.isEmpty()) {
                throw new ServiceException(this.getTopPontoResponse().alertaJaCad(cargo.toString()));
            }
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroBuscar(), ex);
        }
    }
//</editor-fold>
}
