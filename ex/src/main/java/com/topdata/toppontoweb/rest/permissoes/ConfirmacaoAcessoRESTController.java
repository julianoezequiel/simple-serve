package com.topdata.toppontoweb.rest.permissoes;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;

import com.sun.jersey.api.spring.Autowire;
import com.sun.jersey.spi.resource.Singleton;
import com.topdata.toppontoweb.entity.autenticacao.Operador;
import com.topdata.toppontoweb.services.permissoes.OperadorService;
import com.topdata.toppontoweb.services.ServiceException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

/**
 * @version 1.0.4 data 30/08/2016
 * @since 1.0.4 data 30/08/2016
 *
 * @author juliano.ezequiel
 */
@Path("/confirmacao")
@Singleton
@Autowire
public class ConfirmacaoAcessoRESTController {

    @Autowired
    private OperadorService operadorService;

    /**
     * Realiza a autenticação no primeiro acesso
     *
     * @param operador
     * @return
     */
    @POST
    @Path("primeiroAcesso")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response confirmacao(@Context HttpServletRequest request, Operador operador) {
        try {
            return operadorService.confirmacao(request, operador);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    /**
     * Para alteração da senha quando estiver utrapassado o prazo de renovação
     *
     * @param operador
     * @return
     */
    @POST
    @Path("alterarSenha")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response alterarSenha(Operador operador) {
        try {
            return operadorService.alterarSenha(operador);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    /**
     * Apos receber o Email de recuperação de senha
     *
     * @param request
     * @param operador
     * @return
     */
    @POST
    @Path("recuperacaoSenha")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response recuperacaoSenha(@Context HttpServletRequest request, Operador operador) {
        try {
            return operadorService.confirmacao(request, operador);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    /**
     * Solicita um Email para a nova senha
     *
     * @param request
     * @param operador
     * @return
     */
    @POST
    @Path("solicitarNovaSenha")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response novaSenha(@Context HttpServletRequest request, Operador operador) {
        try {
            return operadorService.novaSenha(request, operador);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

}
