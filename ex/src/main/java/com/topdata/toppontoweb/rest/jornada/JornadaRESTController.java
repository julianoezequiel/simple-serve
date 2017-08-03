package com.topdata.toppontoweb.rest.jornada;

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
import com.topdata.toppontoweb.entity.jornada.Jornada;
import com.topdata.toppontoweb.services.jornada.JornadaService;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.dto.JornadaTransfer;

/**
 * @version 1.0.4 data 16/08/2016
 * @since 1.0.4 data 16/08/2016
 *
 * @author juliano.ezequiel
 */
@Path("/jornada")
@Singleton
@Autowire
public class JornadaRESTController {

    @Autowired
    private JornadaService jornadaService;

    public JornadaRESTController() {
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response create(Jornada entity) {
        try {
            return jornadaService.salvar(entity);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

//    @POST
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Path("/transfer")
//    @Transactional
//    public Response create1(JornadaTransfer entity) {
//        try {
//            return jornadaService.salvar(entity);
//        } catch (ServiceException ex) {
//            throw new WebApplicationException(ex.getResponse());
//        }
//    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/copiar")
    @Transactional
    public Response copiar(List<Jornada> entity) {
        try {
            return jornadaService.copiar(entity);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Path("/transfer/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public JornadaTransfer findTransfer(@PathParam("id") Integer id) {
        try {
            return jornadaService.buscarTransfer(Jornada.class, id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response edit(Jornada entity) {
        try {
            return jornadaService.atualizar(entity);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response remove(@PathParam("id") Integer id) {
        try {
            return jornadaService.excluir(Jornada.class, id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Jornada find(@PathParam("id") Integer id) {
        try {
            return jornadaService.buscar(Jornada.class, id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<Entidade> findAll() {
        try {
            return jornadaService.buscarTodos(Jornada.class);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

}
