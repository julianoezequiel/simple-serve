package com.topdata.toppontoweb.rest.calendario;

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
import com.topdata.toppontoweb.entity.calendario.Calendario;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.calendario.CalendarioService;

/**
 * Classe de acesso ao métodos REST para o CRUD do Calendário
 *
 * @version 1.0.4 data 24/06/2016
 * @author 1.0.4 data 24/06/2016
 *
 * @author juliano.ezequiel
 */
@Path("/calendario")
@Singleton
@Autowire
public class CalendarioRESTController {

    /* Injeta o serviço de regras de negócio para o calendário */
    @Autowired
    private CalendarioService calendarioServices;

    /**
     * Insere um novo calendário
     *
     * @param calendario
     * @return Response
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(Calendario calendario) {
        try {
            return calendarioServices.salvar(calendario);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    /**
     * Copia os calendario
     *
     * @param calendarios
     * @return Response
     */
    @POST
    @Path("/copiar")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response copy(List<Calendario> calendarios) {
        try {
            return calendarioServices.copiarCalendarios(calendarios);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    /**
     * Atualiza um calendário existente
     *
     * @param calendario
     * @return Response
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response edit(Calendario calendario) {
        try {
            return calendarioServices.atualizar(calendario);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    /**
     * Exclui um calendário pelo seu id
     *
     * @param id do calendário
     * @return Response
     */
    @DELETE
    @Path("{id}")
    public Response remove(@PathParam("id") Integer id) {
        try {
            return calendarioServices.excluir(Calendario.class, id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    /**
     * Busca um calendário pelo seu id
     *
     * @param id do calendário
     * @return Response
     */
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Calendario find(@PathParam("id") Integer id) {
        try {
            return calendarioServices.buscar(Calendario.class, id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    /**
     * Busca todos os calendários
     *
     * @return Lista de calendário
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Entidade> findAll() {
        try {
            return calendarioServices.buscarTodos(Calendario.class);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

}
