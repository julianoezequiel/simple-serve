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
import com.topdata.toppontoweb.entity.funcionario.FuncionarioJornada;
import com.topdata.toppontoweb.services.funcionario.jornada.FuncionarioJornadaService;
import com.topdata.toppontoweb.services.ServiceException;

/**
 * Classe de acesso aos métodos REST para o Funcionário Jornada
 *
 * @version 1.0.6 data 15/09/2016
 * @since 1.0.6 data 15/09/2016
 *
 * @author juliano.ezequiel
 */
@Path("/funcionarioJornada")
@Singleton
@Autowire
public class FuncionarioJornadaRESTController {

    @Autowired
    private FuncionarioJornadaService funcionarioJornadaService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(FuncionarioJornada funcionarioJornada) {
        try {
            return funcionarioJornadaService.salvar(funcionarioJornada);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }


    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response edit(FuncionarioJornada funcionarioJornada) {
        try {
            funcionarioJornada.setManual(Boolean.TRUE);
            return funcionarioJornadaService.atualizar(funcionarioJornada);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @DELETE
    @Path("{id}")
    public Response remove(@PathParam("id") Integer id) {
        try {
            return funcionarioJornadaService.excluir(FuncionarioJornada.class, id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public FuncionarioJornada find(@PathParam("id") Integer id) {
        try {
            return funcionarioJornadaService.buscar(FuncionarioJornada.class, id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Entidade> findAll() {
        try {
            return funcionarioJornadaService.buscarTodos(FuncionarioJornada.class);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }


    @GET
    @Path("/funcionario/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<FuncionarioJornada> buscarPorOperador(@PathParam("id") Integer id) {
        try {
            return funcionarioJornadaService.buscarPorFuncionario(id, true);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

}
