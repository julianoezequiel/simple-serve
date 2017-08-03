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
import com.topdata.toppontoweb.entity.jornada.Horario;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.jornada.HorarioJornadaSequencia;
import com.topdata.toppontoweb.services.jornada.HorarioService;
import com.topdata.toppontoweb.dto.HorarioTransfer;
import com.topdata.toppontoweb.dto.PaginacaoTransfer;

/**
 * @version 1.0.6 data 06/10/2016
 * @version 1.0.4 data 16/08/2016
 * @since 1.0.4 data 16/08/2016
 *
 * @author juliano.ezequiel
 */
@Path("/horario")
@Singleton
@Autowire
public class HorarioRESTController {

    @Autowired
    private HorarioService horarioService;

    public HorarioRESTController() {
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response create(Horario entity) {
        try {
            return horarioService.salvar(entity);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response edit(Horario entity) {
        try {
            return horarioService.atualizar(entity);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response remove(@PathParam("id") Integer id) {
        try {
            return horarioService.excluir(Horario.class, id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Horario find(@PathParam("id") Integer id) {
        try {
            return horarioService.buscar(Horario.class, id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<Entidade> findAll() {
        try {
            return horarioService.buscarTodos(Horario.class);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }
    
    @GET
    @Path("/jornada/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<HorarioJornadaSequencia> buscarHorarios(@PathParam("id") Integer id) {
        try {
            return (List<HorarioJornadaSequencia>) horarioService.buscarPorJornada(id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    @Path("/transfer")
    public Response create2(HorarioTransfer entity) {
        try {
            return horarioService.salvar(entity);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }
    
    @POST
    @Path("/paginacao")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response buscarPorPaginacao(PaginacaoTransfer pt) {
        try {
            return horarioService.buscarPorPaginacao(pt);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

}
