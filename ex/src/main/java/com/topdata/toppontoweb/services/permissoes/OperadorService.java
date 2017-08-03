package com.topdata.toppontoweb.services.permissoes;

//<editor-fold defaultstate="collapsed" desc="IMPORTS">
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import net.sf.jasperreports.engine.JRException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.dao.permissoes.OperadorDao;
import com.topdata.toppontoweb.dto.OperadorTransfer;
import com.topdata.toppontoweb.entity.autenticacao.Grupo;
import com.topdata.toppontoweb.entity.autenticacao.Operador;
import com.topdata.toppontoweb.entity.autenticacao.Operador_;
import com.topdata.toppontoweb.entity.autenticacao.User;
import com.topdata.toppontoweb.entity.funcionario.Funcionario;
import com.topdata.toppontoweb.rest.autenticacao.TokenUtils;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoService;
import com.topdata.toppontoweb.services.funcionario.FuncionarioService;
import com.topdata.toppontoweb.services.permissoes.operador.validacoes.ValidacaoOperador;
import com.topdata.toppontoweb.services.permissoes.operador.validacoes.ValidarSenha;
import com.topdata.toppontoweb.utils.EmailConfirmacao;
import com.topdata.toppontoweb.utils.EmailRecuperacaoSenha;
import com.topdata.toppontoweb.utils.EmailValidator;
import com.topdata.toppontoweb.utils.GeradorCodigoConfirmacao;
import com.topdata.toppontoweb.utils.UploadFileService;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES.Enum_AUDITORIA;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES.Enum_FUNCIONALIDADE;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES.Enum_OPERACAO;
import com.topdata.toppontoweb.utils.constantes.MSG;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
//</editor-fold>

/**
 * @version 1.0.0.0 data 18/01/2017
 * @since 1.0.0.0 data 20/04/2016
 *
 * @author juliano.ezequiel
 */
@Service
public class OperadorService extends TopPontoService<Operador, Object> {

    //<editor-fold defaultstate="collapsed" desc="CDI">
    @Autowired
    private ValidarSenha validarSenha;
    @Autowired
    private EmailConfirmacao emailConfirmacao;
    @Autowired
    private ValidacaoOperador validacaoOperador;
    @Autowired
    private UploadFileService uploadFileService;
    @Autowired
    private FuncionarioService funcionarioService;
    @Autowired
    private EmailRecuperacaoSenha emailRecuperacaoSenha;
    @Autowired
    private GeradorCodigoConfirmacao geradorCodigoConfirmacao;
    @Autowired
    private TokenUtils tokenUtils;
    @Autowired
    private EmailValidator emailValidator;
    @Autowired
    private OperadorDao operadorDao;
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="VARIÁVEIS">
    private Operador operador;
    private List<OperadorTransfer> operadorTransfers;
    private HashMap<String, Object> mapParameter = new HashMap<>();
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="CONSULTAS">
    /**
     * Busca os operadores pelo grupo de acesso
     *
     * @param id
     * @return List OperadorTransfer
     * @throws ServiceException
     */
    public List<OperadorTransfer> buscarPorGrupo(Integer id) throws ServiceException {
        try {
            operadorTransfers = new ArrayList<>();
            mapParameter = new HashMap<>();
            mapParameter.put(Operador_.grupo.getName(), new Grupo(id));
            this.getDao().findbyAttributes(mapParameter, Operador.class).forEach(new toOperadorTransfer());
            return operadorTransfers;
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroBuscar(), ex);
        }
    }

    /**
     * Busca os operadores pelo grupo de acesso e que estejam ativos
     *
     * @param id
     * @return List OperadorTransfer
     * @throws ServiceException
     */
    public List<OperadorTransfer> buscarPorGrupoeAtivos(Integer id) throws ServiceException {
        try {
            operadorTransfers = new ArrayList<>();
            mapParameter = new HashMap<>();
            mapParameter.put(Operador_.grupo.getName(), new Grupo(id));
            mapParameter.put(Operador_.ativo.getName(), Boolean.TRUE);
            this.getDao().findbyAttributes(mapParameter, Operador.class).forEach(new toOperadorTransfer());
            return operadorTransfers;
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroBuscar(), ex);
        }
    }

    /**
     * Busca o User pelo nome de operador
     *
     * @param operador
     * @return Operador
     * @throws ServiceException
     */
    public User buscaUserPorNome(Operador operador) throws ServiceException {
        try {
            mapParameter = new HashMap<>();
            mapParameter.put(Operador_.usuario.getName(), operador.getUsuario());
            Operador o = (Operador) this.getDao().findOnebyAttributes(mapParameter, Operador.class);
            return o != null ? new User(o) : null;
        } catch (Exception e) {
            throw new ServiceException(this.getTopPontoResponse().erroBuscar(), e);
        }
    }

    /**
     * Utiliza o nome de usuário para realzar a consulta
     *
     * @param operador
     * @return Operador
     * @throws ServiceException
     */
    public Operador buscaOperadorPorNome(Operador operador) throws ServiceException {
        try {
            mapParameter = new HashMap<>();
            mapParameter.put(Operador_.usuario.getName(), operador.getUsuario());
            return (Operador) this.getDao().findOnebyAttributes(mapParameter, Operador.class);
        } catch (DaoException e) {
            throw new ServiceException(this.getTopPontoResponse().erroBuscar(), e);
        }
    }

    /**
     * Utiliza o email para realizar a consulta
     *
     * @param operador
     * @return Operador
     * @throws ServiceException
     */
    public Operador buscaOperadorPorEmail(Operador operador) throws ServiceException {
        try {
            mapParameter = new HashMap<>();
            mapParameter.put(Operador_.email.getName(), operador.getEmail());
            return (Operador) this.getDao().findOnebyAttributes(mapParameter, Operador.class);
        } catch (DaoException e) {
            throw new ServiceException(this.getTopPontoResponse().erroBuscar(), e);
        }
    }

    /**
     * Utiliza o código de confirmação para realizar a consulta
     *
     * @param operador
     * @return Operador
     * @throws ServiceException
     */
    public Operador buscaOperadorPorCodigoComfirmacao(Operador operador) throws ServiceException {
        try {
            mapParameter = new HashMap<>();
            mapParameter.put(Operador_.codigoConfirmacaoNovaSenha.getName(), operador.getCodigoConfirmacaoNovaSenha());
            return (Operador) this.getDao().findOnebyAttributes(mapParameter, Operador.class);
        } catch (DaoException e) {
            throw new ServiceException(this.getTopPontoResponse().erroBuscar(), e);
        }
    }

    /**
     * Lista todos os operadores
     *
     * @return List OperadorTransfer
     * @throws ServiceException
     */
    public List<OperadorTransfer> buscarTodosOperTranfer() throws ServiceException {
        try {
            this.operadorTransfers = new ArrayList<>();
            this.getDao().findAll(Operador.class).forEach(new toOperadorTransfer());
            return this.operadorTransfers;
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroBuscar(), ex);
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="CRUD">
    /**
     * Exclui o operador pelo nome de usuário
     *
     * @param nome
     * @return Response
     * @throws ServiceException
     */
    public Response excluirPorNome(String nome) throws ServiceException {
        try {
            this.operador = validacaoOperador.validarExcluir(new Operador(nome));
            this.getDao().delete(this.operador);
            this.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_PERMISSOES_OPERADORES, Enum_AUDITORIA.DEFAULT, Enum_OPERACAO.EXCLUIR, this.operador);
            return this.getTopPontoResponse().sucessoExcluir(Operador.class);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroExcluir(new Operador().toString()), ex);
        }
    }

    @Override
    public Operador buscar(Class<Operador> entidade, Object id) throws ServiceException {
        try {
            Operador o = (Operador) this.getDao().find(Operador.class, id);
            if (o == null) {
                throw new ServiceException(this.getTopPontoResponse().alertaNaoCad(new Operador().toString()));
            }
            return o;
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroBuscar(), ex);
        }

    }

    @Override
    public Response excluir(Class<Operador> c, Object id) throws ServiceException {
        try {
            this.operador = validacaoOperador.validarExcluir(new Operador((Integer) id));
            this.getAuditoriaServices().auditar(Enum_FUNCIONALIDADE.CAD_PERMISSOES_OPERADORES, Enum_AUDITORIA.DEFAULT, Enum_OPERACAO.EXCLUIR, operador);
            this.getDao().delete(operador);
            return this.getTopPontoResponse().sucessoExcluir(operador.toString());
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroExcluir(new Operador().toString()), ex);
        }
    }

    public Response salvar(HttpServletRequest request, Operador operador) throws ServiceException {
        try {
            Funcionario funcionario = null;
            if (operador.getFuncionario() != null) {
                funcionario = funcionarioService.buscar(Funcionario.class, operador.getFuncionario().getIdFuncionario());
            }
            operador.setTrocaSenhaProximoAcesso(Boolean.TRUE);
            operador.setAtivo(Boolean.TRUE);
            operador.setSenhaBloqueada(Boolean.FALSE);
            operador.setTrocaSenhaProximoAcesso(Boolean.FALSE);
            operador.setCodigoConfirmacaoNovaSenha(geradorCodigoConfirmacao.gerar(operador));
            operador = (Operador) this.getDao().save(validacaoOperador.validarSalvar(operador));

            //verifica o vínculo funcionário operador
            funcionarioService.atualizarFuncionarioOperador(funcionario, operador);
            this.getAuditoriaServices().auditar(Enum_FUNCIONALIDADE.CAD_PERMISSOES_OPERADORES, Enum_AUDITORIA.DEFAULT, Enum_OPERACAO.INCLUIR, operador);

            try {
                emailConfirmacao.enviarEmail(request, operador);
            } catch (ServiceException e) {
                return this.getTopPontoResponse().sucessoSalvarOperadorFalhaEnvioEmail(operador.toString(), new OperadorTransfer(operador));
            }
            return this.getTopPontoResponse().sucessoSalvar(operador.toString(), new OperadorTransfer(operador));
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroSalvar(new Operador().toString()), ex);
        }
    }

    public Response atualizar(HttpServletRequest request, Operador operador) throws ServiceException {
        try {
            Funcionario funcionario = null;
            if (operador.getFuncionario() != null) {
                funcionario = funcionarioService.buscar(Funcionario.class, operador.getFuncionario().getIdFuncionario());
            }
            Operador o = validacaoOperador.validarAtualizar(operador);
            this.getAuditoriaServices().auditar(Enum_FUNCIONALIDADE.CAD_PERMISSOES_OPERADORES, Enum_AUDITORIA.DEFAULT, Enum_OPERACAO.EDITAR, operador);
            operador = (Operador) this.getDao().save(o);
            funcionarioService.atualizarFuncionarioOperador(funcionario, operador);
            return this.getTopPontoResponse().sucessoAtualizar(operador.toString(), new OperadorTransfer(operador));
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroAtualizar(new Operador().toString()), ex);
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="FUNÇÕES AUXILIARES">
    /**
     * Realiza o upload da foto do operador
     *
     * @param request
     * @param uploadedInputStream
     * @param fileDetail
     * @param foto
     * @return Response
     * @throws ServiceException
     */
    public Response upload(HttpServletRequest request, InputStream uploadedInputStream, FormDataContentDisposition fileDetail, String foto) throws ServiceException {
        return uploadFileService.upload(request, uploadedInputStream, fileDetail, foto);
    }

    public Operador getOperadorDaSessao(HttpServletRequest request) throws ServiceException {
        String nomeOperador = TokenUtils.getUserNameFromToken(tokenUtils.extractAuthTokenFromRequest(request));
        return buscaOperadorPorNome(new Operador(nomeOperador));
    }

    /**
     * Consulta os operadores ativos ou todos
     *
     * @param ativo
     * @return
     * @throws ServiceException
     */
    public List<OperadorTransfer> buscarAtivos(Boolean ativo) throws ServiceException {
        try {
            this.operadorTransfers = new ArrayList<>();
            mapParameter = new HashMap<>();
            if (Objects.equals(ativo, Boolean.TRUE)) {
                mapParameter.put(Operador_.ativo.getName(), ativo);
            }
            this.getDao().findbyAttributes(mapParameter, Operador.class).stream().forEach(new toOperadorTransfer());
            return this.operadorTransfers;
        } catch (DaoException ex) {
            throw new ServiceException(ex);
        }

    }

    public void removerTokenAcesso(Grupo grupo) throws ServiceException {
        try {
            this.operadorDao.removerTokenAcesso(grupo);
        } catch (DaoException ex) {
            throw new ServiceException(ex);
        }
    }

    /**
     * Consummer da consulta buscar por grupo
     */
    private class toOperadorTransfer implements Consumer<Operador> {

        @Override
        public void accept(Operador operador) {
            if (!(operador.getUsuario().toUpperCase().equals(CONSTANTES.OPERADOR_ANONIMO)) && !(operador.getUsuario().toUpperCase().equals(CONSTANTES.OPERADOR_MASTER))) {
                operadorTransfers.add(new OperadorTransfer(operador));
            }
        }
    }

    /**
     * No filtro das requisições são realizadas alterações nos status dos
     * usuários
     *
     * @param operador
     * @return boolean
     */
    public Boolean filtroAutenticacaoAtualizarOperador(Operador operador) {
        try {
            return this.getDao().save(operador) != null;
        } catch (DaoException ex) {
            return Boolean.FALSE;
        }
    }

    /**
     * Altera somente a senha do operador
     *
     * @param operador
     * @return Response
     * @throws ServiceException
     */
    public Response mudarSenha(Operador operador) throws ServiceException {
        String senha = operador.getNovaSenha();
        operador = validarSenha.validar(operador, null);
        operador.setCodigoConfirmacaoNovaSenha("");
        operador.setTrocaSenhaProximoAcesso(Boolean.FALSE);
        operador.setSenha(senha);
        return this.atualizar(null, operador);
    }

    /**
     * envia o email de cadastro
     *
     * @param request
     * @param operador
     * @return Response
     * @throws ServiceException
     */
    public Response reenviarEmailConfirmacao(HttpServletRequest request, Operador operador) throws ServiceException {
        try {
            operador.setTrocaSenhaProximoAcesso(Boolean.FALSE);
            operador.setCodigoConfirmacaoNovaSenha(geradorCodigoConfirmacao.gerar(operador));
            Operador oper = buscar(Operador.class, operador.getIdOperador());
            operador.setEmail(oper.getEmail());
            operador.setFuncionarioList(oper.getFuncionarioList());
            operador = (Operador) this.getDao().save(validacaoOperador.validarAtualizar(operador));
            emailConfirmacao.enviarEmail(request, operador);
            return this.getTopPontoResponse().alertaSucesso(MSG.OPERADOR.ALERTA_EMAIL_ENVIADO.getResource());
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroValidar(operador.toString()), ex);
        }

    }

    /**
     * Confirma a criação do operador atravéz do código enviado pelo email
     *
     * @param request
     * @param operador
     * @return Response
     * @throws ServiceException
     */
    public Response confirmacao(HttpServletRequest request, Operador operador) throws ServiceException {
        try {
            if ((this.operador = buscaOperadorPorCodigoComfirmacao(operador)) != null) {
                this.operador.setCodigoConfirmacaoNovaSenha("");
                this.operador.setSenha(operador.getNovaSenha());
                this.operador.setTrocaSenhaProximoAcesso(Boolean.FALSE);
                this.operador.setSenhaBloqueada(Boolean.FALSE);
                operador = (Operador) this.getDao().save(validacaoOperador.validarAtualizar(this.operador));
                this.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_PERMISSOES_OPERADORES, Enum_AUDITORIA.DEFAULT, Enum_OPERACAO.EDITAR, operador);
            } else {
                throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.OPERADOR.ALERTA_CODIGO_INVALIDO.getResource()));
            }
            return this.getTopPontoResponse().sucessoAtualizar(operador.toString(), new OperadorTransfer(operador));
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroValidar(new Operador().toString()), ex);
        }
    }

    /**
     * Solicita o email para recuperação da senha
     *
     * @param request
     * @param operador
     * @return Response
     * @throws ServiceException
     */
    public Response novaSenha(HttpServletRequest request, Operador operador) throws ServiceException {
        try {

            if (emailValidator.validate(operador.getEmail()) == Boolean.TRUE) {
                if ((operador = buscaOperadorPorEmail(operador)) != null) {
                    operador.setTrocaSenhaProximoAcesso(Boolean.FALSE);
                    operador.setSenhaBloqueada(Boolean.FALSE);
                    operador.setCodigoConfirmacaoNovaSenha(geradorCodigoConfirmacao.gerar(operador));
                    operador = (Operador) this.getDao().save(validacaoOperador.validarAtualizar(operador));
                    emailRecuperacaoSenha.enviarEmail(request, operador);
                    return this.getTopPontoResponse().alertaSucesso(MSG.OPERADOR.ALERTA_EMAIL_ENVIADO.getResource());
                } else {
                    throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.OPERADOR.ALERTA_EMAIL_NAO_CADASTRADO.getResource()));
                }
            } else {
                operador.setUsuario(operador.getEmail());
                if ((operador = buscaOperadorPorNome(operador)) != null) {
                    operador.setTrocaSenhaProximoAcesso(Boolean.FALSE);
                    operador.setSenhaBloqueada(Boolean.FALSE);
                    operador.setCodigoConfirmacaoNovaSenha(geradorCodigoConfirmacao.gerar(operador));
                    operador = (Operador) this.getDao().save(validacaoOperador.validarAtualizar(operador));
                    emailRecuperacaoSenha.enviarEmail(request, operador);
                    return this.getTopPontoResponse().alertaSucesso(MSG.OPERADOR.ALERTA_EMAIL_ENVIADO.getResource());
                } else {
                    throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.OPERADOR.ALERTA_NAO_CADASTRADO.getResource()));
                }
            }
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroValidar(new Operador().toString()), ex);
        }
    }

    /**
     * Altera a senha do operador
     *
     * @param oper
     * @return Response
     * @throws ServiceException
     */
    public Response alterarSenha(Operador oper) throws ServiceException {
        Operador operador;
        if ((operador = buscaOperadorPorCodigoComfirmacao(oper)) != null
                || (operador = buscaOperadorPorNome(oper)) != null
                || (operador = buscaOperadorPorEmail(oper)) != null) {
            String senha = oper.getNovaSenha();
            validarSenha.validar(oper, null);
            operador.setCodigoConfirmacaoNovaSenha("");
            operador.setTrocaSenhaProximoAcesso(Boolean.FALSE);
            operador.setSenha(senha);
            operador.setHistoricoSenhasList(operador.getHistoricoSenhasList());
            return this.atualizar(null, operador);
        } else {
            throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.OPERADOR.ALERTA_CODIGO_INVALIDO.getResource()));
        }
    }

//</editor-fold>
}
