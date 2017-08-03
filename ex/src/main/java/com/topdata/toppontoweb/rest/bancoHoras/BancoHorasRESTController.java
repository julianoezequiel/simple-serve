package com.topdata.toppontoweb.rest.bancoHoras;

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
import com.topdata.toppontoweb.entity.bancohoras.BancoHoras;
import com.topdata.toppontoweb.services.bancoHoras.BancoHorasServices;
import com.topdata.toppontoweb.services.ServiceException;

/**
 * @version 1.0.4 data 18/08/2016
 * @since 1.0.4 data 18/08/2016
 *
 * @author juliano.ezequiel
 */
@Path("/bancohoras")
@Singleton
@Autowire
public class BancoHorasRESTController {

    @Autowired
    private BancoHorasServices bancoHorasServices;

    public BancoHorasRESTController() {
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response create(BancoHoras entity) {
        try {
            return bancoHorasServices.salvar(entity);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response edit(BancoHoras entity) {
        try {
            return bancoHorasServices.atualizar(entity);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }

    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response remove(@PathParam("id") Integer id) {
        try {
            return bancoHorasServices.excluir(BancoHoras.class, id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }

    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public BancoHoras find(@PathParam("id") Integer id) {
        try {
            return bancoHorasServices.buscar(BancoHoras.class, id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<Entidade> findAll() {
        try {
            return bancoHorasServices.buscarTodos(BancoHoras.class);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }

    }

}
