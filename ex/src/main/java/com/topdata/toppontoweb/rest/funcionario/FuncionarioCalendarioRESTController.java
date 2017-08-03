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
import org.springframework.transaction.annotation.Transactional;

import com.sun.jersey.api.spring.Autowire;
import com.sun.jersey.spi.resource.Singleton;
import com.topdata.toppontoweb.entity.Entidade;
import com.topdata.toppontoweb.entity.funcionario.FuncionarioCalendario;
import com.topdata.toppontoweb.services.funcionario.calendario.FuncionarioCalendarioService;
import com.topdata.toppontoweb.services.ServiceException;

/**
 * Classe de acesso aos métodos REST para o Funcionário Jornada
 *
 * @version 1.0.6 data 29/09/2016
 * @since 1.0.6 data 29/09/2016
 *
 * @author juliano.ezequiel
 */
@Path("/funcionariocalendario")
@Singleton
@Autowire
public class FuncionarioCalendarioRESTController {

    @Autowired
    private FuncionarioCalendarioService funcionarioCalendarioService;

    public FuncionarioCalendarioRESTController() {
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response create(FuncionarioCalendario entity) {
        try {
            return funcionarioCalendarioService.salvar(entity);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response edit(FuncionarioCalendario funcionarioCalendario) {
        try {
            funcionarioCalendario.setManual(Boolean.TRUE);
            return funcionarioCalendarioService.atualizar(funcionarioCalendario);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response remove(@PathParam("id") Integer id) {
        try {
            return funcionarioCalendarioService.excluir(FuncionarioCalendario.class, id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }

    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public FuncionarioCalendario find(@PathParam("id") Integer id) {
        try {
            return funcionarioCalendarioService.buscar(FuncionarioCalendario.class, id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<Entidade> findAll() {
        try {
            return funcionarioCalendarioService.buscarTodos(FuncionarioCalendario.class);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Path("/funcionario/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<FuncionarioCalendario> buscarPorOperador(@PathParam("id") Integer id) {
        try {
            return funcionarioCalendarioService.buscarPorFuncionario(id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

}
