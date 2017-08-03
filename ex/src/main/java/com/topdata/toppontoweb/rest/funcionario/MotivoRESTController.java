package com.topdata.toppontoweb.rest.funcionario;

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
import com.topdata.toppontoweb.entity.funcionario.Motivo;
import com.topdata.toppontoweb.services.funcionario.motivo.MotivoService;
import com.topdata.toppontoweb.services.ServiceException;

/**
 * @version 1.0.5 data 05/09/2016
 * @since 1.0.5 data 05/09/2016
 *
 * @author juliano.ezequiel
 */
@Path("/motivo")
@Singleton
@Autowire
public class MotivoRESTController {

    @Autowired
    private MotivoService motivoService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(Motivo motivo) {
        try {
            return motivoService.salvar(motivo);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response edit(Motivo motivo) {
        try {
            return motivoService.atualizar(motivo);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @DELETE
    @Path("{id}")
    public Response remove(@PathParam("id") Integer id) {
        try {
            return motivoService.excluir(Motivo.class, id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Motivo find(@PathParam("id") Integer id) {
        try {
            return motivoService.buscar(Motivo.class, id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Entidade> findAll() {
        try {
            return motivoService.buscarTodos(Motivo.class);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Path("/cartao")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Motivo> buscarPorCartao() {
        try {
            return motivoService.buscarMotivosCartao();
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Path("/afastamento")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Motivo> buscarPorAfastamento() {
        try {
            return motivoService.buscarMotivosAfastamento();
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }
    @GET
    @Path("/compensacao")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Motivo> buscarPorCompensacao() {
        try {
            return motivoService.buscarMotivosCompensacao();
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }
    

}
