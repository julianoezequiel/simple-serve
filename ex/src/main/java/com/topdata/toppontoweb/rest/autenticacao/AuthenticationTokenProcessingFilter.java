package com.topdata.toppontoweb.rest.autenticacao;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.GenericFilterBean;
import com.topdata.toppontoweb.dao.AutenticacaoDao;
import com.topdata.toppontoweb.entity.autenticacao.User;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoResponse;
import com.topdata.toppontoweb.utils.constantes.MSG;

/**
 * Todas a requisições são interceptadas pelo filtro. Caso não possua um token
 * de autenticação válido a sessão não será iniciada
 *
 * @version 1.0.4 data 30/08/2016
 * @since 1.0.1 data 03/03/2016
 *
 * @author juliano.ezequiel
 */
public class AuthenticationTokenProcessingFilter extends GenericFilterBean {

    @Autowired
    private AutenticacaoDao userDao;

    @Autowired
    private TopPontoResponse topPontoResponse;

    @Autowired
    private TokenUtils tokenUtils;

    public AuthenticationTokenProcessingFilter() {
    }

    /**
     * Filtro responsável pela autenticação do sistema
     *
     * @param request
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {

            HttpServletRequest httpRequest = this.getAsHttpRequest(request);

            Boolean reqRest = httpRequest.getRequestURI().contains(".html") || httpRequest.getRequestURI().contains("rest");

            String authToken = tokenUtils.extractAuthTokenFromRequest(httpRequest);
            String userName = TokenUtils.getUserNameFromToken(authToken);
            String authTokenRefresh = tokenUtils.extractAuthTokenRefreshFromRequest(httpRequest);
            if (reqRest.equals(Boolean.TRUE)) {
                if (userName != null) {

                    User user = (User) userDao.loadUserByUsername(userName);

                    /* caso no header contenha um token, com um usu�rio inesistente */
                    if (user != null) {
                        /* Valida o token para realizar a autentica��o */
                        if (tokenUtils.validateToken(authToken, authTokenRefresh, user)) {
                            /* Verifica se existe algum bloqueio para este operador */
//                            if (vFilterLoginImpl.validar(user.getOperador(), null) != null) {
//                                Long tempoIniFitroAtualiza = Calendar.getInstance().getTimeInMillis();
                            /* atualiza o user = (User) seguranca.validar(user); ultimo acesso e qual o IP de origem */
//                                user.getOperador().setUltimoIp(request.getRemoteAddr());
//                                user.getOperador().setUltimoAcesso(Calendar.getInstance().getTime());
//                                user.getOperador().setUltimoToken(authToken);
                            /* realizar a atualiza��o do acesso na base de dados */
//                                isValido = operadorService.filtroAutenticacaoAtualizarOperador(user.getOperador());
//                            }
                            /* Autentica a sessão para o acesso a URI solicitada , após a autenticação da sessão 
                             o Spring Security ira validar a permissão e autorizaçao do acesso para a URI
                             solicitada */
//                            if (isValido == true || reqRest == false) {
                            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
                            SecurityContextHolder.getContext().setAuthentication(authentication);
//                                Logger.getLogger(this.getClass().getSimpleName()).log(Level.INFO, "REQUEST: {0}", authentication.getPrincipal());
//                            }
                        }
                    }
                }
            }
            chain.doFilter(request, response);
        } catch (IOException | ServletException ex) {
            System.err.printf("ERRO: "+ex.getCause());
            if (ex instanceof ServiceException) {
                throw new WebApplicationException(((ServiceException) ex).getResponse());
            }else if(ex.getCause() instanceof ServiceException ){
                throw new WebApplicationException(((ServiceException) ex.getCause()).getResponse());
            } else {
                throw new WebApplicationException(topPontoResponse.erro(MSG.ERRO, this.getClass()));
            }
        } catch (UsernameNotFoundException ex) {
            chain.doFilter(request, response);
//            throw new WebApplicationException(topPontoResponse.responseNaoAutorizado(ex.getMessage()));
        }
    }

    /**
     * Converte o Servlet Request para Http Request
     *
     * @param request
     * @return
     */
    private HttpServletRequest getAsHttpRequest(ServletRequest request) {
        if (!(request instanceof HttpServletRequest)) {
            throw new RuntimeException("Expecting an HTTP request");
        }
        return (HttpServletRequest) request;
    }

}
