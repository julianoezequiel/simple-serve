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
import com.topdata.toppontoweb.entity.funcionario.FuncionarioDepartamento;
import com.topdata.toppontoweb.services.funcionario.departamento.FuncionarioDepartamentoService;
import com.topdata.toppontoweb.services.ServiceException;

/**
 * @version 1.0.6 data 06/10/2016
 * @version 1.0.4 data 23/08/2016
 * @since 1.0.4 data 23/08/2016
 *
 * @author juliano.ezequiel
 */
@Path("/funcionariodepartamento")
@Singleton
@Autowire
public class FuncionarioDepartamentoRESTController {

    @Autowired
    private FuncionarioDepartamentoService funcionarioDepartamentoService;

    public FuncionarioDepartamentoRESTController() {
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response create(FuncionarioDepartamento entity) {
        try {
            return funcionarioDepartamentoService.salvar(entity);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response edit(FuncionarioDepartamento entity) {
        try {
            return funcionarioDepartamentoService.atualizar(entity);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response remove(@PathParam("id") Integer id) {
        try {
            return funcionarioDepartamentoService.excluir(FuncionarioDepartamento.class, id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public FuncionarioDepartamento find(@PathParam("id") Integer id) {
        try {
            return funcionarioDepartamentoService.buscar(FuncionarioDepartamento.class, id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<Entidade> findAll() {
        try {
            return funcionarioDepartamentoService.buscarTodos(FuncionarioDepartamento.class);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

}
