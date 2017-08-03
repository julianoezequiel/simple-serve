package com.topdata.toppontoweb.services.calendario;

//<editor-fold defaultstate="collapsed" desc="IMPORTS">
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.ws.rs.core.Response;

import org.springframework.stereotype.Service;

import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.entity.calendario.Feriado;
import com.topdata.toppontoweb.entity.calendario.Feriado_;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoService;
import com.topdata.toppontoweb.services.ValidacoesCadastro;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;
import com.topdata.toppontoweb.utils.constantes.MSG;
//</editor-fold>

/**
 * Realiza as regras de negócio para o CRUD do Feriado.
 *
 * @version 1.0.0.0 data 18/01/2017
 * @since 1.0.0.0 data 30/06/2016
 *
 * @author juliano.ezequiel
 */
@Service
public class FeriadoServices extends TopPontoService<Feriado, Object>
        implements ValidacoesCadastro<Feriado, Object> {

    //<editor-fold defaultstate="collapsed" desc="VARIÁVEIS">
    private HashMap<String, Object> map;
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="CONSULTAS">
    /**
     * Busca o feriado pela descrição
     *
     * @param descricao do feriado
     * @return Feriado
     * @throws ServiceException
     */
    public Feriado buscarFeriadoPorDescricao(String descricao) throws ServiceException {
        try {
            this.map = new HashMap<>();
            this.map.put(Feriado_.descricao.getName(), descricao);
            List<Feriado> feriados = this.getDao().findbyAttributes(this.map, Feriado.class);
            if (feriados.isEmpty()) {
                throw new ServiceException(this.getTopPontoResponse().alertaNaoCad(Feriado.class));
            }
            return feriados.get(0);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erro(MSG.CADASTRO.ERRO_BUSCAR, Feriado.class), ex);
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="CRUD">
    @Override
    public Response salvar(Feriado feriado) throws ServiceException {
        try {
            feriado = (Feriado) this.getDao().save(validarSalvar(feriado));
            this.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.DEFAULT, CONSTANTES.Enum_AUDITORIA.CAD_FERIADO, CONSTANTES.Enum_OPERACAO.INCLUIR, feriado);
            return this.getTopPontoResponse().sucessoSalvar(feriado);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroSalvar(feriado), ex);
        }
    }

    @Override
    public Response atualizar(Feriado feriado) throws ServiceException {
        try {
            feriado = (Feriado) this.getDao().save(validarSalvar(feriado));
            this.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.DEFAULT, CONSTANTES.Enum_AUDITORIA.CAD_FERIADO, CONSTANTES.Enum_OPERACAO.EDITAR, feriado);
            return this.getTopPontoResponse().sucessoAtualizar(feriado);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroAtualizar(feriado), ex);
        }
    }

    @Override
    public Response excluir(Class<Feriado> c, Object id) throws ServiceException {
        try {
            Feriado feriado = validarExcluir(new Feriado((Integer) id));
            this.getDao().delete(feriado);
            this.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_CALENDARIO, CONSTANTES.Enum_AUDITORIA.CAD_FERIADO, CONSTANTES.Enum_OPERACAO.EXCLUIR, feriado);
            return this.getTopPontoResponse().sucessoExcluir(feriado.toString());
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroExcluir(Feriado.class), ex);
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="VALIDAÇÕES OBRIGATÓRIA">
    @Override
    public Feriado validarSalvar(Feriado feriado) throws ServiceException {
        VValidarJaCadastradoDescricao(feriado);
        return feriado;
    }

    @Override
    public Feriado validarExcluir(Feriado feriado) throws ServiceException {
        try {
            Feriado f = (Feriado) this.getDao().find(Feriado.class, feriado.getIdFeriado());
            if (f == null) {
                throw new ServiceException(this.getTopPontoResponse().alertaNaoCad(Feriado.class));
            }
            if (!f.getCalendarioFeriadoList().isEmpty()) {
                throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.FERIADO.ALERTA_EXCLUSAO_EM_USO.getResource()));
            }
            return f;
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroValidar(Feriado.class), ex);
        }

    }

    @Override
    public Feriado validarAtualizar(Feriado feriado) throws ServiceException {

        VValidarJaCadastradoDescricao(feriado);

        return feriado;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="CLASSES DE VALIDAÇÕES">
    /**
     * Valida se já existe um feriado com a descrição e em caso de atualização
     * verifica se está tentando alterar para uma descrição já existente.
     *
     * @param feriado
     * @return
     * @throws com.topdata.toppontoweb.services.ServiceException
     */
    public Feriado VValidarJaCadastradoDescricao(Feriado feriado) throws ServiceException {
        try {
            this.map = new HashMap<>();
            this.map.put(Feriado_.descricao.getName(), feriado.getDescricao());
            List<Feriado> feriados = this.getDao().findbyAttributes(this.map, Feriado.class);
            if (feriados.size() > 0 && (!Objects.equals(feriado.getIdFeriado(), feriados.get(0).getIdFeriado()))) {
                throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.FERIADO.ALERTA_DESCRICAO_JA_CAD.getResource()));
            }
            return feriado;
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroValidar(Feriado.class), ex);
        }
    }
//</editor-fold>

}
