package com.topdata.toppontoweb.rest.permissoes;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import com.sun.jersey.api.spring.Autowire;
import com.sun.jersey.spi.resource.Singleton;
import com.topdata.toppontoweb.entity.autenticacao.Seguranca;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.permissoes.SegurancaService;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;
import com.topdata.toppontoweb.utils.constantes.ROTA;

/**
 * Classe de acesso aos métodos REST para a Segurança
 *
 * @version 1.0.3 data 05/09/2016
 * @since 1.0.1
 * @author juliano.ezequiel
 */
@Path(ROTA.SEGURANCA)
@Singleton
@Autowire
public class SegurancaRESTController {

    @Autowired
    private SegurancaService segurancaService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    @PreAuthorize(CONSTANTES.PRE_AUTH_MASTER)
    public Response create(Seguranca entity) {
        try {
            return segurancaService.salvar(entity);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response edit(Seguranca entity) {
        try {
            return segurancaService.atualizar(entity);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @DELETE
    @Path("{id}")
    @Transactional
    @PreAuthorize(CONSTANTES.PRE_AUTH_MASTER)
    public Response remove(@PathParam("id") Integer id) {
        try {
            return segurancaService.excluir(Seguranca.class, id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Seguranca buscar() {
        try {
            return (Seguranca) segurancaService.buscarTodos(Seguranca.class).get(0);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }
}
