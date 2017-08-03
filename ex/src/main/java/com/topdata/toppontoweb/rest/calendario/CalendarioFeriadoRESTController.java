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
import com.topdata.toppontoweb.entity.calendario.CalendarioFeriado;
import com.topdata.toppontoweb.services.calendario.CalendarioFeriadoServices;
import com.topdata.toppontoweb.services.ServiceException;

/**
 * Classe de acesso ao métodos REST para o CRUD do CalendárioFeriado
 *
 * @version 1.0.4 data 30/06/2016
 * @since 1.0.4 data 30/06/2016
 *
 * @author juliano.ezequiel
 */
@Path("/calendarioferiado")
@Singleton
@Autowire
public class CalendarioFeriadoRESTController {

    /* Injeta o serviço de regras de negócio para o CalendárioFeriado */
    @Autowired
    private CalendarioFeriadoServices calendarioFeriadoServices;

    /**
     * Insere um novo CalendarioFeriado
     *
     * @param calendarioFeriado
     * @return Response
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response create(List<CalendarioFeriado> calendarioFeriado) {
        try {
            return calendarioFeriadoServices.salvar(calendarioFeriado);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    /**
     * Atualiza um CalendarioFeriado
     *
     * @param calendarioFeriado
     * @return Response
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response edit(List<CalendarioFeriado> calendarioFeriado) {
        try {
            return calendarioFeriadoServices.salvar(calendarioFeriado);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    /**
     * Exclui o CalendarioFeriodo pelo id
     *
     * @param id do CalendarioFeriado
     * @return Response
     */
    @DELETE
    @Path("{id}")
    @Transactional
    public Response remove(@PathParam("id") Integer id) {
        try {
            return calendarioFeriadoServices.excluir(CalendarioFeriado.class, id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    /**
     * Busca CalendarioFeriado pelo id
     *
     * @param id do CalendarioFeriado
     * @return CalendarioFeriado
     */
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public CalendarioFeriado find(@PathParam("id") Integer id) {
        try {
            return calendarioFeriadoServices.buscar(CalendarioFeriado.class, id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    /**
     * Busca todos os CalendarioFeriado
     *
     * @return Lista de CaledndarioFeriado
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<Entidade> findAll() {
        try {
            return calendarioFeriadoServices.buscarTodos(CalendarioFeriado.class);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    /**
     * Busca CalendarioFeriado pelo id do calendário
     *
     * @param id
     * @return Lista de CalendarioFeriado
     */
    @GET
    @Path("/calendario/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<CalendarioFeriado> buscaPorCalendario(@PathParam("id") Integer id) {
        try {
            return calendarioFeriadoServices.buscarPorCalendario(id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    /**
     * Busca CalendarioFeriado pelo id do feriado
     *
     * @param id
     * @return lista de CalendarioFeriado
     */
//    @GET
//    @Path("/feriado/{id}")
//    @Produces(MediaType.APPLICATION_JSON)
//    @Transactional
//    public List<CalendarioFeriado> buscaPorFeriado(@PathParam("id") Integer id) {
//        try {
//            return calendarioFeriadoServices.buscarPorFeriado(id);
//        } catch (ServiceException ex) {
//            throw new WebApplicationException(ex.getResponse());
//        }
//    }

    /**
     * Insere um novo CalendarioFeriado
     *
     * @param calendarioFeriado
     * @return Response
     */
    @POST
    @Path("/validar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response validar(List<CalendarioFeriado> calendarioFeriado) {
        try {
            return calendarioFeriadoServices.validarFeriado(calendarioFeriado);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

}
