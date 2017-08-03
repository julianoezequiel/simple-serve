package com.topdata.toppontoweb.services.permissoes;

//<editor-fold defaultstate="collapsed" desc="IMPORTS">
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.dao.permissoes.GrupoDao;
import com.topdata.toppontoweb.dto.GrupoExcluirResultado;
import com.topdata.toppontoweb.dto.GrupoTransfer;
import com.topdata.toppontoweb.dto.MsgRetorno;
import com.topdata.toppontoweb.entity.Entidade;
import com.topdata.toppontoweb.entity.autenticacao.Grupo;
import com.topdata.toppontoweb.entity.autenticacao.Grupo_;
import com.topdata.toppontoweb.entity.autenticacao.Permissoes;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoService;
import com.topdata.toppontoweb.services.ValidacoesCadastro;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES.Enum_AUDITORIA;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES.Enum_FUNCIONALIDADE;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES.Enum_OPERACAO;
import com.topdata.toppontoweb.utils.constantes.MSG;
import java.util.logging.Level;
import java.util.logging.Logger;
//</editor-fold>

/**
 * @version 1.0.0.0 data 18/01/2017
 * @since 1.0.0.0 data 02/05/2016
 * @author juliano.ezequiel
 */
@Service
public class GrupoService extends TopPontoService<Grupo, Object>
        implements ValidacoesCadastro<Grupo, Object> {

    //<editor-fold defaultstate="collapsed" desc="CDI">
    @Autowired
    private GrupoDao grupoDao;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private OperadorService operadorService;
//</editor-fold>

    private Response hasError;
    private List<Grupo> gruposValidos = new ArrayList<>();

    //<editor-fold defaultstate="collapsed" desc="CONSULTAS">
    /**
     * Realiza a consulta na base de dados , de um Grupo pela sua descrição
     *
     * @param grupo
     * @return Grupo
     * @throws ServiceException
     */
    public Grupo buscaPorDescricao(Grupo grupo) throws ServiceException {
        try {
            HashMap<String, String> criterios = new HashMap<>();
            criterios.put(Grupo_.descricao.getName(), grupo.getDescricao());
            List<Grupo> grupos = this.getDao().findbyAttributes(criterios, Grupo.class);
            return grupos.isEmpty() ? null : grupos.get(0);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erro(MSG.CADASTRO.ERRO_BUSCAR, Grupo.class), ex);
        }
    }

    /**
     * Busca o grupo pelo id, o retorno é convertido em JSON e utilizado o
     * GrupoTranfers por questões de segurança
     *
     * @param id
     * @return String JSON GrupoTransfer
     * @throws ServiceException
     */
    public String getGrupoTransfer(Integer id) throws ServiceException {
        try {
            return mapper.writeValueAsString(new GrupoTransfer(buscar(Grupo.class, id)));
        } catch (IOException ex) {
            throw new ServiceException(this.getTopPontoResponse().erro(MSG.CADASTRO.ERRO_BUSCAR, Grupo.class), ex);
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="CRUD">
    /**
     * realiza a exclusão do grupo já previamente validado
     *
     * @param grupo
     * @return
     * @throws ServiceException
     */
    public Response excluirGrupo(Grupo grupo) throws ServiceException {
        try {
            //verifica se o grupo possi vinculos e realiza a remoção dos vicnulos
            if (grupo.getDepartamentoList().size() > 1 || grupo.getPermissoesList().size() > 1) {
                grupo.getDepartamentoList().clear();
                grupo.getPermissoesList().clear();
                grupo.getFuncionalidadesGrupoOperacaoList().clear();
                this.getDao().save(grupo);
            }
            
            //exclui o grupo
            grupoDao.excluirGrupo(grupo);

            this.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_PERMISSOES_GRUPOS, Enum_AUDITORIA.DEFAULT, Enum_OPERACAO.EXCLUIR, grupo);
            return this.getTopPontoResponse().sucessoExcluir(new Grupo().toString());
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroExcluir(new Grupo().toString()), ex);
        }
    }

    @Override
    public Grupo buscar(Class<Grupo> entidade, Object id) throws ServiceException {
        try {
            Grupo g = (Grupo) this.getDao().find(Grupo.class, id);
            if (g == null) {
                throw new ServiceException(this.getTopPontoResponse().alertaNaoCad(new Grupo().toString()));
            }
            return g;
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroBuscar(), ex);
        }
    }

    private void removerTokenOperadores(Grupo grupo) {
        if (grupo != null && grupo.getOperadorList() != null) {
            grupo.getOperadorList().forEach(o -> {
                try {
                    this.operadorService.removerTokenAcesso(grupo);
                } catch (ServiceException ex) {
                    Logger.getLogger(GrupoService.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        }
    }

    @Override
    public Response atualizar(Grupo grupo) throws ServiceException {
        try {

            grupo = (Grupo) this.getDao().save(validarAtualizar(grupo));
            removerTokenOperadores(grupo);
            this.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_PERMISSOES_GRUPOS, null, Enum_OPERACAO.EDITAR, grupo);
            return this.getTopPontoResponse().sucessoAtualizar(Grupo.class);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroAtualizar(Grupo.class), ex);
        }
    }

    @Override
    public Response salvar(Grupo grupo) throws ServiceException {
        try {
            grupo = validarSalvar(grupo);
            List<Permissoes> listPermissoes = this.getDao().findAll(Permissoes.class);
            
            grupo.getPermissoesList().addAll(
                    listPermissoes.stream()
                    .filter(p -> p.getIdPermissoes() != 1)
                    .collect(Collectors.toList()));

            grupo = (Grupo) this.getDao().save(grupo);
            this.getAuditoriaServices().auditar(Enum_FUNCIONALIDADE.CAD_PERMISSOES_GRUPOS, Enum_AUDITORIA.DEFAULT, Enum_OPERACAO.INCLUIR, grupo);
            return hasError != null ? hasError : this.getTopPontoResponse().sucessoSalvar(Grupo.class);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroSalvar(Grupo.class), ex);
        }
    }

    @Override
    public List<Entidade> buscarTodos(Class<Grupo> entidade) throws ServiceException {
        try {
            List<Entidade> grupos = new ArrayList<>();
            this.getDao().findAll(entidade).forEach(grupo -> {
                if (!((Grupo) grupo).getDescricao().toUpperCase().equals(CONSTANTES.GRUPO_MASTER) && !((Grupo) grupo).getDescricao().toUpperCase().equals(CONSTANTES.GRUPO_ANONIMO)) {
                    grupos.add((Entidade) grupo);
                }
            });
            return grupos;
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroBuscar(), ex);
        }
    }

    /**
     * Exclui uma lista de grupos de acesso
     *
     * @param list
     * @return
     * @throws ServiceException
     */
    public Response excluirList(List<Grupo> list) throws ServiceException {
        gruposValidos = new ArrayList<>();
        List<Grupo> gruposInvalidos = new ArrayList<>();
        Response r = null;
        //realiza a validação individual de cada grupo/ grupos que possuem operadores , ou que não possua id ou o grupo administrador não serão excluidos
        list.forEach(g -> {
            try {
                Grupo grupo = buscar(Grupo.class, g.getIdGrupo());
                gruposValidos.add(validarExcluir(grupo));
            } catch (ServiceException ex) {
                MsgRetorno msg = (MsgRetorno) ex.getResponse().getEntity();
                g.setMensagem(msg.getBody());
                gruposInvalidos.add(g);
            }
        });

        List<Grupo> gruposValidosClone = (List<Grupo>) ((ArrayList<Grupo>) this.gruposValidos).clone();
        //exclui um grupo por vez
        for (Grupo g : gruposValidosClone) {
            try {
                excluirGrupo(g);
            } catch (ServiceException ex) {
                //Remove da lista de validos
                this.gruposValidos.remove(g);
                MsgRetorno msg = (MsgRetorno) ex.getResponse().getEntity();

                //Adiciona na lista de invalidos
                g.setMensagem(msg.getBody());
                gruposInvalidos.add(g);
            }
        }
//        if (gruposValidos.size() != list.size()) {
//            throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.GRUPO.ALERTA_ALGUNS_GRUPO_EXCLUIDO.getResource()));
//        }
//        if (gruposValidos.isEmpty() || Objects.isNull(r)) {
//            throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.GRUPO.ALERTA_NENHUM_GRUPO_EXCLUIDO.getResource()));
//        }

        return getTopPontoResponse().sucessoSalvar(new GrupoExcluirResultado(this.gruposValidos, gruposInvalidos));
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="VALIDAÇÕES OBRIGATÓRIAS">
    @Override
    public Grupo validarSalvar(Grupo t) throws ServiceException {
        ValidarGrupoDoSistema(t);
        ValidarGrupoDescricao(t);
        return t;
    }

    @Override
    public Grupo validarExcluir(Grupo grupo) throws ServiceException {

        ValidarNaoCadastrado(grupo);
        VExcluirAdministrador(grupo);
        VExcluirComOperador(grupo);

        return grupo;
    }

    @Override
    public Grupo validarAtualizar(Grupo grupo) throws ServiceException {

        ValidarIdentificador(grupo);
        ValidarGrupoDoSistema(grupo);
        ValidarAlteracaoDescricao(grupo);
        VAtualizaValoresEntreEntidades(grupo);

        return grupo;

    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="REGRAS DE VALIDAÇÕES">
    /**
     * Verifica se já existe um grupo cadastrado com a descrição do Grupo
     * passado como parâmetro. Somente deverá existir 1 grupo para cada
     * descrição
     */
    private Grupo ValidarGrupoDescricao(Grupo grupo) throws ServiceException {
        if (buscaPorDescricao(grupo) != null) {
            throw new ServiceException(this.getTopPontoResponse().alertaJaCad(Grupo.class));
        }
        return grupo;
    }

    /**
     * Verifica se já existe um grupo cadastrado com a descrição do Grupo
     * passado como parâmetro. Somente deverá existir 1 grupo para cada
     * descrição
     */
    private Grupo ValidarGrupoDoSistema(Grupo grupo) throws ServiceException {
        if (grupo.getDescricao().toUpperCase().equals(CONSTANTES.GRUPO_ADMIN)
                || grupo.getDescricao().toUpperCase().equals(CONSTANTES.GRUPO_ANONIMO)
                || grupo.getDescricao().toUpperCase().equals(CONSTANTES.GRUPO_MASTER)) {
            throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.GRUPO.ALERTA_GRUPO_DO_SISTEMA.getResource()));
        }
        return grupo;
    }

    /**
     * Verifica se o o Grupo possui um identificador válido
     */
    private Grupo ValidarIdentificador(Grupo grupo) throws ServiceException {
        if (grupo.getIdGrupo() == null) {
            throw new ServiceException(this.getTopPontoResponse().alertaNaoCad(Grupo.class));
        }
        return grupo;
    }

    /**
     * Valida a alteração na descrição do grupo
     */
    private Grupo ValidarAlteracaoDescricao(Grupo grupo) throws ServiceException {
        Grupo grupo1 = buscaPorDescricao(grupo);
        Grupo grupo2 = buscar(Grupo.class, grupo.getIdGrupo());

        if (grupo1 != null && !grupo1.getIdGrupo().equals(grupo2.getIdGrupo())) {
            throw new ServiceException(this.getTopPontoResponse().alertaJaCad(Grupo.class));
        }
        return grupo;

    }

    /**
     * Valida de o grupo informado não existe na base da dados
     */
    private Grupo ValidarNaoCadastrado(Grupo grupo) throws ServiceException {

        if (grupo == null) {
            throw new ServiceException(this.getTopPontoResponse().alertaNaoCad(Grupo.class));
        }
        return grupo;

    }

    /**
     * Busca na base de dados e antualiza os valores entre as entidades
     */
    private Grupo VAtualizaValoresEntreEntidades(Grupo grupo) throws ServiceException {

        Grupo grupoOriginal = buscar(Grupo.class, grupo.getIdGrupo());
        grupo.setPermissoesList(grupo.getPermissoesList().size() > 0 ? grupo.getPermissoesList() : grupoOriginal.getPermissoesList());
        grupo.setOperadorList(grupo.getOperadorList().size() > 0 ? grupo.getOperadorList() : grupoOriginal.getOperadorList());
        grupo.setDepartamentoList(grupo.getDepartamentoList().size() > 0 ? grupo.getDepartamentoList() : grupoOriginal.getDepartamentoList());
        return grupo;
    }

    /**
     * Verifica se o grupo é o grupo administrador
     */
    private Grupo VExcluirAdministrador(Grupo grupo) throws ServiceException {
        if (grupo.getDescricao() != null && grupo.getDescricao().toUpperCase().equals(CONSTANTES.GRUPO_ADMIN)) {
            throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.GRUPO.ALERTA_EXCLUIR_ADMIN.getResource()));
        }
        return grupo;
    }

    /**
     * Valida se o grupo possui operadores vinculados
     */
    private Grupo VExcluirComOperador(Grupo grupo) throws ServiceException {
        if (!grupo.getOperadorList().isEmpty()) {
            throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.GRUPO.ALERTA_EXCLUIR_COM_OPERADOR.getResource()));
        }
        return grupo;
    }
//</editor-fold>
}
