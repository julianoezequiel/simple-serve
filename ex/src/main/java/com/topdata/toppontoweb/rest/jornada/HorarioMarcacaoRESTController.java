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
import com.topdata.toppontoweb.entity.jornada.HorarioMarcacao;
import com.topdata.toppontoweb.services.jornada.HorarioMarcacaoService;
import com.topdata.toppontoweb.services.ServiceException;

/**
 * @version 1.0.4 data 16/08/2016
 * @since 1.0.4 data 16/08/2016
 *
 * @author juliano.ezequiel
 */
@Path("/horariomarcacao")
@Singleton
@Autowire
public class HorarioMarcacaoRESTController {

    @Autowired
    private HorarioMarcacaoService horarioMarcacaoService;

    public HorarioMarcacaoRESTController() {
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response create(List<HorarioMarcacao> entity) {
        try {
            return horarioMarcacaoService.salvar(entity);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }

    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response edit(HorarioMarcacao entity) {
        try {
            return horarioMarcacaoService.atualizar(entity);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response remove(@PathParam("id") Integer id) {
        try {
            return horarioMarcacaoService.excluir(HorarioMarcacao.class, id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public HorarioMarcacao find(@PathParam("id") Integer id) {
        try {
            return horarioMarcacaoService.buscar(HorarioMarcacao.class, id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<Entidade> findAll() {
        try {
            return horarioMarcacaoService.buscarTodos(HorarioMarcacao.class);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Path("horario/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<HorarioMarcacao> buscarPorHorario(@PathParam("id") Integer id) {
        try {
            return horarioMarcacaoService.buscarPorHorario(id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }
}
