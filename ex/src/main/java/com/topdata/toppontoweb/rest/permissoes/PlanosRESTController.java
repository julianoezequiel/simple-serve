package com.topdata.toppontoweb.rest.permissoes;

import java.util.List;

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
import com.topdata.toppontoweb.entity.Entidade;
import com.topdata.toppontoweb.entity.autenticacao.Planos;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.permissoes.PlanoService;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;
import com.topdata.toppontoweb.utils.constantes.ROTA;

/**
 * Classe de acesso aos métodos REST para o Plano
 *
 * @version 1.0.3 data 09/05/2016
 * @since 1.0.3 data 09/05/2016
 * @author juliano.ezequiel
 */
@Path("/planoAcesso")
@Singleton
@Autowire
public class PlanosRESTController {

    @Autowired
    private PlanoService planoService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    @PreAuthorize(CONSTANTES.PRE_AUTH_MASTER)
    public Response create(Planos entity) {
        try {
            return planoService.salvar(entity);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    @PreAuthorize(CONSTANTES.PRE_AUTH_MASTER)
    public Response edit(Planos entity) {
        try {
            return planoService.atualizar(entity);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @DELETE
    @Path(ROTA.ID)
    @Transactional
    @PreAuthorize(CONSTANTES.PRE_AUTH_MASTER)
    public Response remove(@PathParam(ROTA.PARAM_01) Integer id) {
        try {
            return planoService.excluir(Planos.class, id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Path(ROTA.ID)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Planos find(@PathParam(ROTA.PARAM_01) Integer id) {
        try {
            return (Planos) planoService.buscar(Planos.class, id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @PreAuthorize(CONSTANTES.PRE_AUTH_MASTER)
    public List<Entidade> findAll() {
        try {
            return planoService.buscarTodos(Planos.class);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Path(ROTA.PLANO_LIBERACAO)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Planos getLiberacao() {
        try {
            return planoService.getLiberacao();
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

}
