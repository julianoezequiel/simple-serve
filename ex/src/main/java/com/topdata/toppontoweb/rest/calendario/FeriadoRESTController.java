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
import com.topdata.toppontoweb.entity.calendario.Feriado;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.calendario.FeriadoServices;

/**
 * Classe de acesso ao métodos REST para o CRUD do Feriado
 *
 * @version 1.0.4 data 30/06/2016
 * @since 1.0.4 data 30/06/2016
 *
 * @author juliano.ezequiel
 */
@Path("/feriado")
@Singleton
@Autowire
public class FeriadoRESTController {

    @Autowired
    private FeriadoServices feriadoServices;

    /**
     * Insere um novo feriado
     *
     * @param feriado
     * @return Response
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response create(Feriado feriado) {
        try {
            return feriadoServices.salvar(feriado);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    /**
     * Atualiza o Feriado
     *
     * @param feriado
     * @return Response
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response edit(Feriado feriado) {
        try {
            return feriadoServices.atualizar(feriado);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    /**
     * Exclui o feriado pelo id
     *
     * @param id do feriado
     * @return Response
     */
    @DELETE
    @Path("{id}")
    @Transactional
    public Response remove(@PathParam("id") Integer id) {
        try {
            return feriadoServices.excluir(Feriado.class, id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    /**
     * Busca o feriado pelo id
     *
     * @param id do feriado
     * @return Feriado
     */
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Feriado find(@PathParam("id") Integer id) {
        try {
            return feriadoServices.buscar(Feriado.class, id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    /**
     * Busca todos os feriados cadastrado
     *
     * @return Lista de Feriado
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<Entidade> findAll() {
        try {
            return feriadoServices.buscarTodos(Feriado.class);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

}
