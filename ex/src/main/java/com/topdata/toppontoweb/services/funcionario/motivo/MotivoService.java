package com.topdata.toppontoweb.services.funcionario.motivo;

//<editor-fold defaultstate="collapsed" desc="IMPORTS">
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.dao.funcionario.MotivoDao;
import com.topdata.toppontoweb.entity.funcionario.Motivo;
import com.topdata.toppontoweb.entity.funcionario.Motivo_;
import com.topdata.toppontoweb.entity.tipo.TipoMotivo;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoService;
import com.topdata.toppontoweb.services.ValidacoesCadastro;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;
import com.topdata.toppontoweb.utils.constantes.MSG;
//</editor-fold>

/**
 * @version 1.0.5 data 05/09/2016
 * @since 1.0.5 data 05/09/2016
 *
 * @author juliano.ezequiel
 */
@Service
public class MotivoService extends TopPontoService<Motivo, Object> implements ValidacoesCadastro<Motivo, Object> {

    @Autowired
    private TipoMotivoService tipoMotivoService;
    @Autowired
    private MotivoDao motivoDao;

    private HashMap<String, Object> map;

    //<editor-fold defaultstate="collapsed" desc="CONSULTAS">
    public List<Motivo> buscarMotivoEventos() throws ServiceException {
        return this.motivoDao.buscarMotivoEventos();
    }

    /**
     * Busca o motivo pela sua descrição e tipo
     *
     * @param motivo
     * @return
     * @throws ServiceException
     */
    public Motivo buscaPorDescricao(Motivo motivo) throws ServiceException {
        return buscaPorDescricao(motivo.getDescricao(), motivo.getIdTipoMotivo().getIdTipoMotivo());
    }
    
    /**
     * Busca o motivo pela sua descrição e pelo seu tipo
     *
     * @param descricaoMotivo
     * @param idTipoMotivo
     * @return
     * @throws ServiceException
     */
    public Motivo buscaPorDescricao(String descricaoMotivo, Integer idTipoMotivo) throws ServiceException {
        try {
            this.map = new HashMap<>();
            this.map.put(Motivo_.descricao.getName(), descricaoMotivo.toUpperCase());
            this.map.put(Motivo_.idTipoMotivo.getName(), idTipoMotivo);
            List<Motivo> list = this.getDao().findbyAttributes(this.map, Motivo.class);
            if (!list.isEmpty()) {
                for (Motivo m : list) {
                    if (m.getIdTipoMotivo().getIdTipoMotivo().equals(idTipoMotivo)) {
                        return m;
                    }
                }
            }
            return null;
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroSalvar(descricaoMotivo), ex);
        }
    }

    /**
     * Lista os motivos utilizados para os cartões
     *
     * @return
     * @throws ServiceException
     */
    public List<Motivo> buscarMotivosCartao() throws ServiceException {
        try {
            this.map = new HashMap<>();
            this.map.put(Motivo_.idTipoMotivo.getName(), tipoMotivoService.cartao());
            return this.getDao().findbyAttributes(this.map, Motivo.class);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroBuscar(), ex);
        }
    }

    /**
     * Lista os motivos utilizados para os afastamentos
     *
     * @return
     * @throws ServiceException
     */
    public List<Motivo> buscarMotivosAfastamento() throws ServiceException {
        try {
            this.map = new HashMap<>();
            this.map.put(Motivo_.idTipoMotivo.getName(), tipoMotivoService.afastamento());
            return this.getDao().findbyAttributes(this.map, Motivo.class);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroBuscar(), ex);
        }
    }

    /**
     * Lista os motivos utilizados para as compensações
     *
     * @return
     * @throws ServiceException
     */
    public List<Motivo> buscarMotivosCompensacao() throws ServiceException {
        try {
            this.map = new HashMap<>();
            this.map.put(Motivo_.idTipoMotivo.getName(), tipoMotivoService.compensacao());
            return this.getDao().findbyAttributes(this.map, Motivo.class);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroBuscar(), ex);
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="CRUD">
    @Override
    public Response salvar(Motivo motivo) throws ServiceException {
        try {
            motivo = (Motivo) this.getDao().save(validarSalvar(motivo));
            if (motivo.getIdTipoMotivo().getIdTipoMotivo() == CONSTANTES.Enum_TIPO_MOTIVO.CARTAO.ordinal()) {
                this.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_FUNCIONARIO_CARTAO, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.INCLUIR, motivo);
            } else if (motivo.getIdTipoMotivo().getIdTipoMotivo() == CONSTANTES.Enum_TIPO_MOTIVO.AFASTAMENTO.ordinal()) {
                this.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_FUNCIONARIO_AFASTAMENTO, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.INCLUIR, motivo);
            } else if (motivo.getIdTipoMotivo().getIdTipoMotivo() == CONSTANTES.Enum_TIPO_MOTIVO.COMPENSACAO.ordinal()) {
                this.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_FUNCIONARIO_COMPENSASOES, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.INCLUIR, motivo);
            }
            return this.getTopPontoResponse().sucessoSalvar(motivo.toString(), motivo);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroSalvar(motivo.toString()), ex);
        }

    }

    @Override
    public Response atualizar(Motivo motivo) throws ServiceException {
        try {
            motivo = (Motivo) this.getDao().save(validarAtualizar(motivo));
            if (motivo.getIdTipoMotivo().getIdTipoMotivo() == CONSTANTES.Enum_TIPO_MOTIVO.CARTAO.ordinal()) {
                this.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_FUNCIONARIO_CARTAO, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.EDITAR, motivo);
            } else if (motivo.getIdTipoMotivo().getIdTipoMotivo() == CONSTANTES.Enum_TIPO_MOTIVO.AFASTAMENTO.ordinal()) {
                this.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_FUNCIONARIO_AFASTAMENTO, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.EDITAR, motivo);
            } else if (motivo.getIdTipoMotivo().getIdTipoMotivo() == CONSTANTES.Enum_TIPO_MOTIVO.COMPENSACAO.ordinal()) {
                this.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_FUNCIONARIO_COMPENSASOES, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.EDITAR, motivo);
            }
            return this.getTopPontoResponse().sucessoAtualizar(motivo.toString(), motivo);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroAtualizar(motivo.toString()), ex);
        }
    }

    @Override
    public Response excluir(Class<Motivo> c, Object id) throws ServiceException {
        try {
            Motivo motivo = validarExcluir(new Motivo((Integer) id));
            this.getDao().delete(motivo);
            this.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_FUNCIONARIO_CARTAO, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.EXCLUIR, motivo);
            return this.getTopPontoResponse().sucessoExcluir(motivo.toString());
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroExcluir(new Motivo().toString()), ex);
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="VALIDAÇÕES OBRIGATÓRIAS">
    @Override
    public Motivo validarSalvar(Motivo motivo) throws ServiceException {
        if (motivo.getDescricao() == null) {
            throw new ServiceException(this.getTopPontoResponse().campoObrigatorio(Motivo_.descricao.getName()));
        }
        if (motivo.getIdTipoMotivo() == null) {
            throw new ServiceException(this.getTopPontoResponse().campoObrigatorio(Motivo_.idTipoMotivo.getName()));
        }
        if (buscaPorDescricao(motivo) != null) {
            throw new ServiceException(this.getTopPontoResponse().alertaConfict(MSG.MOTIVOS.ALERTA_DESCRICAO_JA_CAD.getResource()));
        }
        return motivo;
    }

    @Override
    public Motivo buscar(Class<Motivo> entidade, Object id) throws ServiceException {
        try {
            Motivo fj = (Motivo) this.getDao().find(Motivo.class, id);
            if (fj == null) {
                throw new ServiceException(this.getTopPontoResponse().alertaNaoCad(new Motivo().toString()));
            }
            return fj;
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroBuscar(), ex);
        }
    }

    @Override
    public Motivo validarExcluir(Motivo motivo) throws ServiceException {

        if (motivo.getIdMotivo() == null) {
            throw new ServiceException(this.getTopPontoResponse().alertaNaoCad(motivo.toString()));
        }

        motivo = buscar(Motivo.class, motivo.getIdMotivo());

        if (!motivo.getAbonoList().isEmpty()) {
            throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.MOTIVOS.ALERTA_EXCLUSAO_NAO_PERMITIDA_ABONO.getResource()));
        }
        if (!motivo.getCartaoList().isEmpty()) {
            throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.MOTIVOS.ALERTA_EXCLUSAO_NAO_PERMITIDA_CARTAO.getResource()));
        }
        if (!motivo.getMarcacoesList().isEmpty()) {
            throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.MOTIVOS.ALERTA_EXCLUSAO_NAO_PERMITIDA_MARCACAO.getResource()));
        }
        if (!motivo.getAfastamentoList().isEmpty()) {
            throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.MOTIVOS.ALERTA_EXCLUSAO_NAO_PERMITIDA_AFASTAMENTO.getResource()));
        }
        if (!motivo.getCompensacaoList().isEmpty()) {
            throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.MOTIVOS.ALERTA_EXCLUSAO_NAO_PERMITIDA_COMPENSACAO.getResource()));
        }

        return motivo;
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="REGRAS DE VALIDAÇÕES">
    @Override
    public Motivo validarAtualizar(Motivo motivo) throws ServiceException {

        if (motivo.getIdMotivo() == null) {
            throw new ServiceException(this.getTopPontoResponse().alertaNaoCad(motivo.toString()));
        }
        Motivo m = null;
        if ((m = this.buscaPorDescricao(motivo)) != null) {
            if (!Objects.equals(m.getIdMotivo(), motivo.getIdMotivo())) {
                throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.MOTIVOS.ALERTA_DESCRICAO_JA_CAD.getResource()));
            }
            if (!m.getAbonoList().isEmpty()) {
                throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.MOTIVOS.ALERTA_ALTERAR_NAO_PERMITIDA_ABONO.getResource()));
            }
//        if (!motivo.getCartaoList().isEmpty()) {
//            throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.MOTIVOS.ALERTA_ALTERAR_NAO_PERMITIDA_CARTAO.getResource()));
//        }
        }
        return motivo;
    }

//</editor-fold>
}
