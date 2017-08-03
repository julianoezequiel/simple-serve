package com.topdata.toppontoweb.rest.configuracoes;

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

import com.sun.jersey.api.spring.Autowire;
import com.sun.jersey.spi.resource.Singleton;
import com.topdata.toppontoweb.entity.Entidade;
import com.topdata.toppontoweb.entity.configuracoes.PercentuaisAcrescimo;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.configuracoes.PercentuaisAcrescimoServices;

/**
 * @version 1.0.5 data 19/07/2016
 * @since 1.0.5 data 19/07/2016
 *
 * @author juliano.ezequiel
 */
@Path("/percentuaisacrescimo")
@Singleton
@Autowire
public class PercentuaisAcrescimoRESTController {

    @Autowired
    private PercentuaisAcrescimoServices percentuaisAcrescimoServices;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(PercentuaisAcrescimo entity) {
        try {
            return percentuaisAcrescimoServices.salvar(entity);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/copiar")
    public Response copiar(List<PercentuaisAcrescimo> entityList) {
        try {
            return percentuaisAcrescimoServices.copiar(entityList);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response edit(PercentuaisAcrescimo entity) {
        try {
            return percentuaisAcrescimoServices.atualizar(entity);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @DELETE
    @Path("{id}")
    public Response remove(@PathParam("id") Integer id) {
        try {
            return percentuaisAcrescimoServices.excluir(PercentuaisAcrescimo.class, id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }

    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public PercentuaisAcrescimo find(@PathParam("id") Integer id) {
        try {
            return percentuaisAcrescimoServices.buscar(PercentuaisAcrescimo.class, id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Entidade> findAll() {
        try {
            return percentuaisAcrescimoServices.buscarTodos(PercentuaisAcrescimo.class);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

}
