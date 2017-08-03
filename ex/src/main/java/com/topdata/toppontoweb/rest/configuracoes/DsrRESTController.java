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
import org.springframework.transaction.annotation.Transactional;

import com.sun.jersey.api.spring.Autowire;
import com.sun.jersey.spi.resource.Singleton;
import com.topdata.toppontoweb.entity.configuracoes.Dsr;
import com.topdata.toppontoweb.entity.Entidade;
import com.topdata.toppontoweb.services.configuracoes.DsrServices;
import com.topdata.toppontoweb.services.ServiceException;

/**
 * Classe de acesso aos métodos REST para o DSR
 *
 * @version 1.0.0 data 01/07/2016
 * @since 1.0.0 data 01/07/2016
 *
 * @author juliano.ezequiel
 */
@Path("/dsr")
@Singleton
@Autowire
public class DsrRESTController {

    @Autowired
    private DsrServices dsrServices;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response create(Dsr dsr) {
        try {
            return dsrServices.salvar(dsr);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response edit(Dsr dsr) {
        try {
            return dsrServices.atualizar(dsr);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response remove(@PathParam("id") Integer id) {
        try {
            return dsrServices.excluir(Dsr.class, id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Dsr find(@PathParam("id") Integer id) {
        try {
            return (Dsr) dsrServices.buscar(Dsr.class, id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<Entidade> findAll() {
        try {
            return dsrServices.buscarTodos(Dsr.class);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Path("/empresa/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Dsr buscarCeiEmpresa(@PathParam("id") Integer id) {
        try {
            return dsrServices.buscarDsrEmpresa(id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }   

}
