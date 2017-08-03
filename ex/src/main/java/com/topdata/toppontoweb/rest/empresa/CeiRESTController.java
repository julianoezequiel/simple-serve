package com.topdata.toppontoweb.rest.empresa;

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
import org.springframework.transaction.annotation.Transactional;

import com.sun.jersey.api.spring.Autowire;
import com.sun.jersey.spi.resource.Singleton;
import com.topdata.toppontoweb.entity.Entidade;
import com.topdata.toppontoweb.entity.empresa.Cei;
import com.topdata.toppontoweb.services.empresa.CeiServices;
import com.topdata.toppontoweb.services.ServiceException;

/**
 * @version 1.0.5 data 18/07/2016
 * @since 1.0.5 data 18/07/2016
 *
 * @author juliano.ezequiel
 */
@Path("/cei")
@Singleton
@Autowire
public class CeiRESTController {

    @Autowired
    private CeiServices ceiServices;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response create(Cei entity) {
        try {
            return ceiServices.salvar(entity);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response edit(Cei entity) {
        try {
            return ceiServices.atualizar(entity);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response remove(@PathParam("id") Integer id) {
        try {
            return ceiServices.excluir(Cei.class, id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Cei find(@PathParam("id") Integer id) {
        try {
            return (Cei) ceiServices.buscar(Cei.class, id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<Entidade> findAll() {
        try {
            return ceiServices.buscarTodos(Cei.class);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }
    
    @GET
    @Path("/empresa/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<Cei> buscarCeiEmpresa(@PathParam("id") Integer id) {
        try {
            return ceiServices.buscarCeiEmpresa(id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

}
