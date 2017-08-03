package com.topdata.toppontoweb.rest.autenticacao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.sun.jersey.api.spring.Autowire;
import com.sun.jersey.spi.resource.Singleton;
import com.topdata.toppontoweb.dao.AutenticacaoDao;
import com.topdata.toppontoweb.dao.Dao;
import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.dto.TokenTransfer;
import com.topdata.toppontoweb.dto.UserTransfer;
import com.topdata.toppontoweb.entity.autenticacao.Operador;
import com.topdata.toppontoweb.entity.autenticacao.User;
import com.topdata.toppontoweb.services.auditoria.AuditoriaServices;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoResponse;
import com.topdata.toppontoweb.services.Validacao;
import com.topdata.toppontoweb.services.autenticacao.ValidarContaDesativada;
import com.topdata.toppontoweb.services.autenticacao.ValidarDataTrocarSenha;
import com.topdata.toppontoweb.services.autenticacao.ValidarSenhaBloqueada;
import com.topdata.toppontoweb.services.autenticacao.ValidarTentativasLogin;
import com.topdata.toppontoweb.services.autenticacao.ValidarTrocarSenhaProximoAcesso;
import com.topdata.toppontoweb.services.permissoes.OperadorService;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;
import com.topdata.toppontoweb.utils.constantes.MSG;
import com.topdata.toppontoweb.utils.constantes.ROTA;
import java.util.Objects;

/**
 * Classe realiza a integração do Rest com o Spring Security, criando o objeto
 * User de autenticação que é passado para o Spring Security
 *
 * @version 1.0.3 DATA 03/05/2016
 * @since 1.0.1 DATA 20/04/2016
 * @see ValidarContaDesativada
 * @see ValidarSenhaBloqueada
 * @see ValidarTrocarSenhaProximoAcesso
 * @see ValidarDataTrocarSenha
 * @see ValidarTentativasLogin
 * @see User
 * @author juliano.ezequiel
 */
@Path(ROTA.USER)
@Singleton
@Autowire
public class UserAuthentication {

    //<editor-fold defaultstate="collapsed" desc="CDI">
    @Autowired
    @Qualifier("authenticationManager")
    private AuthenticationManager authManager;

    @Autowired
    private UserDetailsService userService;

    @Autowired
    private OperadorService operadorService;

    @Autowired
    private Dao dao;

    @Autowired
    private AuditoriaServices auditarDados;

    @Autowired
    private ValidarContaDesativada validarContaDesativada;

    @Autowired
    private ValidarSenhaBloqueada validarSenhaBloqueada;

    @Autowired
    private ValidarTrocarSenhaProximoAcesso validarTrocarSenhaProximoAcesso;

    @Autowired
    private ValidarDataTrocarSenha validarDataTrocarSenha;

    @Autowired
    private ValidarTentativasLogin validarTentativasLogin;

    @Autowired
    private TopPontoResponse topPontoResponse;

    @Autowired
    private AutenticacaoDao autenticacaoDao;
    //</editor-fold>

    private final UserAuthentication.ValidarAutenticacao validarAutenticacao = new ValidarAutenticacao();

    private final UserAuthentication.ValidarFalhaAutenticacao validarFalhaAutenticacao = new ValidarFalhaAutenticacao();

    private User user;
    private String username;
    private String password;
    private final Map<String, Object> map = new HashMap<>();

    private final List<Validacao> validacoes = new ArrayList<>();

    /**
     * Retorna o usuário autenticado.
     *
     * @return O transfer contendo os dados do usuário.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public UserTransfer getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        if (principal instanceof String && ((String) principal).equals("anonymousUser")) {
            throw new WebApplicationException(401);
        }
        this.user = (User) principal;
        return new UserTransfer(user.getUsername(), this.createRoleMap(user), user.getOperador());
    }

    /**
     * Realiza a autenticação do usuário e cria o token de acesso.
     *
     * @param request
     * @param usersMap
     * @return O transfer contendo o token para o acesso.
     * @throws com.topdata.toppontoweb.dao.DaoException
     * @throws com.topdata.toppontoweb.services.ServiceException
     */
    @Path("/auth")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public TokenTransfer authenticate(@Context HttpServletRequest request, Map<String, String> usersMap) throws DaoException, ServiceException {
        try {
            this.username = usersMap.containsKey("username") ? usersMap.get("username") : null;
            this.password = usersMap.containsKey("password") ? usersMap.get("password") : null;
            this.map.put("usuario", this.username != null ? this.username : "");
            this.map.put("senha", this.password != null ? this.password : "");

            /* Autentica o contexto da aplicação para a requisição solicitada */
            UserDetails u = this.autenticacaoDao.loadUserByUsername(username);
            if (u.getPassword() == null) {
                throw new ServiceException(this.topPontoResponse.alertaValidacao(MSG.SEGURANCA.FALHA_AUTENTICACAO.getResource()));
            }
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(this.username, this.password);
            Authentication authentication = this.authManager.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            this.user = (User) authentication.getPrincipal();

            /* Realiza as validações de acesso */
            this.user = this.validarAutenticacao.validar(this.user, null);

            TokenTransfer tokenTransfer = new TokenTransfer(TokenUtils.createToken(this.user));

            this.user.getOperador().setUltimoIp(request.getRemoteAddr());
            this.user.getOperador().setTentativasLogin(0);
            this.user.getOperador().setUltimoAcesso(Calendar.getInstance().getTime());
            this.user.getOperador().setUltimoToken(tokenTransfer.getToken());
            this.dao.save(this.user.getOperador());

            /* Seta os valores de acesso */
            this.auditarDados.auditar(CONSTANTES.Enum_AUDITORIA.SIS_LOGIN, CONSTANTES.Enum_OPERACAO.SISTEMA, user.getOperador());
            return tokenTransfer;

        } catch (AuthenticationException e) {
            if ((this.user = this.operadorService.buscaUserPorNome(new Operador(this.username))) != null) {
                this.user.setIP(request.getRemoteAddr());
                try {
                    this.user = validarFalhaAutenticacao.validar(this.user, null);
                    this.auditarDados.auditar(CONSTANTES.Enum_AUDITORIA.SIS_TENATIVA_LOGIN, CONSTANTES.Enum_OPERACAO.SISTEMA, this.map, this.user);
                } catch (ServiceException ex) {
                    throw new WebApplicationException(ex.getResponse());
                }
                throw new WebApplicationException(this.topPontoResponse.responseNaoAutorizado(MSG.OPERADOR.ALERTA_SENHA_INVALIDA.getResource()));
            }
            Operador o = new Operador();
            o.setEmail(this.username);
            if ((o = this.operadorService.buscaOperadorPorEmail(o)) != null) {
                if (Objects.equals(o.getTrocaSenhaProximoAcesso(), Boolean.TRUE)) {
                    throw new WebApplicationException(this.topPontoResponse.alertaTrocaSenha(MSG.OPERADOR.ALERTA_TROCAR_SENHA.getResource(), o));
                }
            }
            this.auditarDados.auditar(CONSTANTES.Enum_AUDITORIA.SIS_TENATIVA_LOGIN, CONSTANTES.Enum_OPERACAO.SISTEMA, map, request.getRemoteAddr());
            throw new WebApplicationException(this.topPontoResponse.responseNaoAutorizado(MSG.SEGURANCA.FALHA_AUTENTICACAO.getResource()));
        } catch (ServiceException e) {
            throw new WebApplicationException(e.getResponse());
        }
    }

    /**
     * Verifica as permissões dos operadores
     *
     * @param userDetails
     * @return
     */
    private Map<String, Boolean> createRoleMap(UserDetails userDetails) {
        Map<String, Boolean> roles = new HashMap<>();
        for (GrantedAuthority authority : userDetails.getAuthorities()) {
            roles.put(authority.getAuthority(), Boolean.TRUE);
        }
        return roles;
    }

    /**
     * Realiza as validações referente a autenticação do Usuário no sistema.
     *
     */
    private class ValidarAutenticacao implements Validacao<User, Object> {

        public ValidarAutenticacao() {
        }

        @Override
        public User validar(User user, Object i) throws ServiceException {
            validacoes.clear();
            validacoes.add(validarContaDesativada);
            validacoes.add(validarSenhaBloqueada);
            validacoes.add(validarTrocarSenhaProximoAcesso);
            validacoes.add(validarDataTrocarSenha);

            for (Validacao validacao : validacoes) {
                user.setOperador((Operador) validacao.validar(user.getOperador(), null));
            }
            return user;
        }
    }

    /**
     * Em caso de falha no login , são realizadas validações das falhas
     */
    private class ValidarFalhaAutenticacao implements Validacao<User, Object> {

        public ValidarFalhaAutenticacao() {
        }

        @Override
        public User validar(User user, Object i) throws ServiceException {
            validacoes.clear();
            validacoes.add(validarSenhaBloqueada);
            validacoes.add(validarTentativasLogin);
            Long tempoInicial = Calendar.getInstance().getTimeInMillis();
            for (Validacao validacao : validacoes) {
                user.setOperador((Operador) validacao.validar(user.getOperador(), null));
            }
            Logger.getLogger(this.toString()).log(Level.INFO, "TEMPO ValidarFalhaAutenticacao {0}", Calendar.getInstance().getTimeInMillis() - tempoInicial);
            return user;
        }
    }

}
