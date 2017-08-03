package com.topdata.toppontoweb.services.permissoes;

//<editor-fold defaultstate="collapsed" desc="IMPORTS">
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.dto.TreeViewData;
import com.topdata.toppontoweb.dto.TreeViewData.State;
import com.topdata.toppontoweb.entity.Entidade;
import com.topdata.toppontoweb.entity.autenticacao.Funcionalidades;
import com.topdata.toppontoweb.entity.autenticacao.FuncionalidadesGrupoOperacao;
import com.topdata.toppontoweb.entity.autenticacao.FuncionalidadesGrupoOperacao_;
import com.topdata.toppontoweb.entity.autenticacao.FuncionalidadesPlanoOperacao;
import com.topdata.toppontoweb.entity.autenticacao.Grupo;
import com.topdata.toppontoweb.entity.autenticacao.Modulos;
import com.topdata.toppontoweb.entity.autenticacao.Planos;
import com.topdata.toppontoweb.entity.tipo.Operacao;
import com.topdata.toppontoweb.entity.tipo.TipoAuditoria;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoService;
import com.topdata.toppontoweb.services.ValidacoesCadastro;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;
import com.topdata.toppontoweb.utils.constantes.MSG;
//</editor-fold>

/**
 * Classe realiza as regras de negocio para as funcionalidades,
 * FuncionalidadesGrupoOperaÃ§Ã£o e FuncionalidadesPlanoOperacao.
 *
 * @version 1.0.0.0 data 18/01/2017
 * @since 1.0.0.0 data 07/06/2016
 *
 * @see FuncionalidadesGrupoOperacao
 * @see FuncionalidadesPlanoOperacao
 *
 * @author juliano.ezequiel
 */
@Service
public class FuncionalidadesServices extends TopPontoService<Object, Object>
        implements ValidacoesCadastro<FuncionalidadesGrupoOperacao, Object> {

    //<editor-fold defaultstate="collapsed" desc="CDI">
    @Autowired
    private ObjectMapper mapper;
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="VARIÁVEIS">
    private Grupo grupo;
    private Planos planos;
    private Response hasError = null;
    private Boolean planoBloqueado;
    private Boolean selecionadoOper = true;
    private final List<TreeViewData> treeViewDataList = new ArrayList<>();
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="VALIDAÇÕES OBRIGATÓRIAS">
    @Override
    public FuncionalidadesGrupoOperacao validarSalvar(FuncionalidadesGrupoOperacao t) throws ServiceException {
        if (t.getFuncionalidadesGrupoOperacaoPK().getIdFuncionalidade() <= 0 || t.getFuncionalidadesGrupoOperacaoPK().getIdFuncionalidade() >= 80
                || t.getFuncionalidadesGrupoOperacaoPK().getIdGrupo() <= 0 || t.getFuncionalidadesGrupoOperacaoPK().getIdOperacao() < 2
                || t.getFuncionalidadesGrupoOperacaoPK().getIdOperacao() > 7) {
            throw new ServiceException();
        }
        return t;
    }

    @Override
    public FuncionalidadesGrupoOperacao validarExcluir(FuncionalidadesGrupoOperacao t) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public FuncionalidadesGrupoOperacao validarAtualizar(FuncionalidadesGrupoOperacao t) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="FUNÇÕES PARA MONTAGEM DO TREEVIEW">
    /**
     * Busca as funcionalidades e módulos e operações para criar o JSON para o
     * TreeView
     *
     * @param id do grupo
     * @return String em JSON
     * @throws ServiceException
     */
    public String getFuncionalidades(Integer id) throws ServiceException {
        try {
            grupo = id == null ? new Grupo() : (Grupo) this.getDao().find(Grupo.class, id);

            planos = (Planos) this.getDao().findAll(Planos.class).get(0);
            planoBloqueado = planos.getPrazoExpira().before(Calendar.getInstance().getTime());

            treeViewDataList.clear();
            TreeViewData treeViewData = new TreeViewData(CONSTANTES.Enum_FUNCIONALIDADE.DEFAULT);
            treeViewData.setState(treeViewData.new State(grupo.getDescricao().toUpperCase().equals(CONSTANTES.GRUPO_ADMIN), Boolean.TRUE, (planoBloqueado || grupo.getDescricao().toUpperCase().equals(CONSTANTES.GRUPO_ADMIN))));
            treeViewDataList.add(treeViewData);

            this.getDao().findAll(Modulos.class).forEach(new ConsummerModulos());

            return mapper.writeValueAsString(treeViewDataList);
        } catch (IOException | DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().alerta(MSG.CADASTRO.ERRO_BUSCAR.getResource(), "Erro!"), ex);
        }
    }

    public List<Modulos> buscarModulosFuncionalidades() throws ServiceException {
        try {
            return this.getDao().findAll(Modulos.class);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().alerta(MSG.CADASTRO.ERRO_BUSCAR.getResource(), "Erro!"), ex);
        }
    }

    public Modulos buscarModulosPorId(Integer id) throws ServiceException {
        try {
            Modulos modulo = (Modulos) this.getDao().find(Modulos.class, id);
            modulo.setFuncionalidadesList(modulo.getFuncionalidadesList().stream().distinct().collect(Collectors.toList()));
            return modulo;
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().alerta(MSG.CADASTRO.ERRO_BUSCAR.getResource(), "Erro!"), ex);
        }
    }

    public List<TipoAuditoria> buscarTiposAuditoria() throws ServiceException {
        try {
            List<?> tipoAuditoria = this.getDao().findAll(TipoAuditoria.class);
            return (List<TipoAuditoria>) tipoAuditoria;
        } catch (DaoException ex) {
            throw new ServiceException(ex);
        }
    }

    /**
     * Adiciona um ítem a Lista de TreeView utilizando como base os Módulos
     */
    private class ConsummerModulos implements Consumer<Modulos> {

        @Override
        public void accept(Modulos modulo) {

            TreeViewData treeViewData = new TreeViewData(modulo.getModulo());

            //verifica se o plano de acesso permite a liberação deste modulo
            treeViewData.getState().setDisabled(planoBloqueado.equals(Boolean.TRUE) || grupo.getDescricao().toUpperCase().equals(CONSTANTES.GRUPO_ADMIN)
                    || planos.getFuncionalidadesPlanoOperacaoList().stream().filter(
                            f -> f.getFuncionalidades().getIdModulo().getModulo().ordinal()
                            == modulo.getModulo().ordinal()).findAny().orElse(null) == null);
            //caso
            if (!grupo.getDescricao().toUpperCase().equals(CONSTANTES.GRUPO_ADMIN)) {
                treeViewData.getState().setIsAtivoPlano(treeViewData.getState().getDisabled());
            } else {
                treeViewData.getState().setIsAtivoPlano(planos.getFuncionalidadesPlanoOperacaoList().stream().filter(
                        f -> f.getFuncionalidades().getIdModulo().getModulo().ordinal()
                        == modulo.getModulo().ordinal()).findAny().orElse(null) == null);
            }

            treeViewDataList.add(treeViewData);

            modulo.getFuncionalidadesList().forEach(new ConsummerFuncionalidade());

        }
    }

    /**
     * Adiciona um ítem a Lista de FuncionalidadesTranfer utilizando como base
     * as Funcionalidades
     */
    private class ConsummerFuncionalidade implements Consumer<Funcionalidades> {

        @Override
        public void accept(Funcionalidades funcionalidades) {
            TreeViewData treeViewData = new TreeViewData(funcionalidades.getFuncionalidade());

            treeViewData.getState().setDisabled(planoBloqueado.equals(Boolean.TRUE) || grupo.getDescricao().toUpperCase().equals(CONSTANTES.GRUPO_ADMIN)
                    || planos.getFuncionalidadesPlanoOperacaoList().stream().filter(
                            f -> f.getFuncionalidades().getFuncionalidade().ordinal()
                            == funcionalidades.getFuncionalidade().ordinal()).findAny().orElse(null) == null);

            if (!grupo.getDescricao().toUpperCase().equals(CONSTANTES.GRUPO_ADMIN)) {
                treeViewData.getState().setIsAtivoPlano(treeViewData.getState().getDisabled());
            } else {
                treeViewData.getState().setIsAtivoPlano(planos.getFuncionalidadesPlanoOperacaoList().stream().filter(
                        f -> f.getFuncionalidades().getFuncionalidade().ordinal()
                        == funcionalidades.getFuncionalidade().ordinal()).findAny().orElse(null) == null);
            }

            treeViewDataList.add(treeViewData);

            funcionalidades.getOperacaoList().forEach(new ConsummerOperacao(funcionalidades));

        }
    }

    /**
     * Adiciona um ítem a Lista de FuncionalidadesTranfer utilizando como base
     * as Operações
     */
    private class ConsummerOperacao implements Consumer<Operacao> {

        private final Funcionalidades funcionalidades;

        public ConsummerOperacao(Funcionalidades funcionalidades) {
            this.funcionalidades = funcionalidades;
        }

        @Override
        public void accept(Operacao operacao) {

            TreeViewData treeViewData = new TreeViewData(operacao.getOperacao(), funcionalidades.getFuncionalidade());

            //verifica se este grupo possui esta operacao
            treeViewData.getState().setSelected(grupo.getDescricao().toUpperCase().equals(CONSTANTES.GRUPO_ADMIN)
                    || grupo.getFuncionalidadesGrupoOperacaoList().stream().filter(
                            f -> f.getFuncionalidades().getFuncionalidade().ordinal() == funcionalidades.getFuncionalidade().ordinal()
                            && f.getOperacao().getOperacao().ordinal() == operacao.getOperacao().ordinal()).findAny().orElse(null) != null);

            selecionadoOper = Objects.equals(selecionadoOper, treeViewData.getState().getSelected());
            //verifica se o plano de acesso permite a liberação desta operacao
            treeViewData.getState().setDisabled(planoBloqueado.equals(Boolean.TRUE) || grupo.getDescricao().toUpperCase().equals(CONSTANTES.GRUPO_ADMIN)
                    || planos.getFuncionalidadesPlanoOperacaoList().stream().filter(
                            f -> f.getFuncionalidades().getFuncionalidade().ordinal() == funcionalidades.getFuncionalidade().ordinal()
                            && f.getOperacao().getOperacao().ordinal() == operacao.getOperacao().ordinal()).findAny().orElse(null) == null);

            if (!grupo.getDescricao().toUpperCase().equals(CONSTANTES.GRUPO_ADMIN)) {
                treeViewData.getState().setIsAtivoPlano(treeViewData.getState().getDisabled());
            } else {
                treeViewData.getState().setIsAtivoPlano(planos.getFuncionalidadesPlanoOperacaoList().stream().filter(
                        f -> f.getFuncionalidades().getFuncionalidade().ordinal() == funcionalidades.getFuncionalidade().ordinal()
                        && f.getOperacao().getOperacao().ordinal() == operacao.getOperacao().ordinal()).findAny().orElse(null) == null);
            }

            treeViewDataList.add(treeViewData);
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="ADICIONA FUNCIONALIDADES AO GRUPO DE ACESSO">
    public Response adicionarFuncionalidadeGrupo(List<FuncionalidadesGrupoOperacao> funcionalidadesGrupoOperacao)
            throws ServiceException {
        hasError = null;
        funcionalidadesGrupoOperacao.forEach(fgo -> {
            try {
                this.getDao().save(validarSalvar(fgo));
            } catch (DaoException | ServiceException ex) {
                Logger.getLogger(FuncionalidadesServices.class.getName()).log(Level.SEVERE, null, ex);
                hasError = this.getTopPontoResponse().erroSalvar(FuncionalidadesGrupoOperacao.class);
            }
        });
        return hasError != null ? hasError : this.getTopPontoResponse().sucessoSalvar(FuncionalidadesGrupoOperacao.class);
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="ATUALIZA FUNCIONALIDADES AO GRUPO DE ACESSO">
    /**
     * Atualiza as funcionalidades do Grupo de acesso. Recebe como parâmetro uma
     * lista contendo todas as funcionalidades para o Grupo de acesso.
     *
     * @param funcionalidadesGrupoOperacao
     * @return
     * @throws ServiceException
     */
    public Response atualizarFuncionalidadeGrupo(List<FuncionalidadesGrupoOperacao> funcionalidadesGrupoOperacao)
            throws ServiceException {
        hasError = null;
        try {
            HashMap<String, Object> map = new HashMap<>();
            map.put(FuncionalidadesGrupoOperacao_.grupo.getName(), new Grupo(funcionalidadesGrupoOperacao.get(0).getFuncionalidadesGrupoOperacaoPK().getIdGrupo()));
            List<FuncionalidadesGrupoOperacao> funcionalidadesGrupoOperacaoBase = this.getDao().findbyAttributes(map, FuncionalidadesGrupoOperacao.class);
            //exclui todos as funcionalidades do grupo
            if (!funcionalidadesGrupoOperacaoBase.isEmpty()) {
                funcionalidadesGrupoOperacaoBase.forEach(fgo -> {
                    try {
                        this.getDao().delete(fgo);
                    } catch (DaoException ex) {
                        Logger.getLogger(FuncionalidadesServices.class.getName()).log(Level.SEVERE, null, ex);
                        hasError = this.getTopPontoResponse().erroExcluir(FuncionalidadesGrupoOperacao.class);
                    }
                });
            }
            //insere as novas funcionalidades
            funcionalidadesGrupoOperacao.forEach(fgo -> {
                try {
                    this.getDao().save((Entidade) validarSalvar(fgo));
                } catch (DaoException | ServiceException ex) {
                    Logger.getLogger(FuncionalidadesServices.class.getName()).log(Level.SEVERE, null, ex);
                    hasError = this.getTopPontoResponse().erroSalvar(FuncionalidadesGrupoOperacao.class);
                }
            });
            return hasError != null ? hasError : this.getTopPontoResponse().sucessoSalvar(Grupo.class);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().alerta(MSG.CADASTRO.ERRO_SALVAR.getResource(), Grupo.class.getSimpleName()), ex);
        }
    }
//</editor-fold>
}
