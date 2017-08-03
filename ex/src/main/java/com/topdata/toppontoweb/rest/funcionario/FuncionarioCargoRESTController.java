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
import com.topdata.toppontoweb.entity.funcionario.HistoricoFuncionarioCargo;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.funcionario.cargo.FuncionarioCargoService;

/**
 * @version 1.0.6 data 06/10/2016
 * @version 1.0.4 data 23/08/2016
 * @since 1.0.4 data 23/08/2016
 *
 * @author juliano.ezequiel
 */
@Path("/funcionariocargo")
@Singleton
@Autowire
public class FuncionarioCargoRESTController {

    @Autowired
    private FuncionarioCargoService funcionarioCargoService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response create(HistoricoFuncionarioCargo entity) {
        try {
            return funcionarioCargoService.salvar(entity);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response edit(HistoricoFuncionarioCargo entity) {

        try {
            return funcionarioCargoService.atualizar(entity);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response remove(@PathParam("id") Integer id) {
        try {
            return funcionarioCargoService.excluir(HistoricoFuncionarioCargo.class, id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public HistoricoFuncionarioCargo find(@PathParam("id") Integer id) {
        try {
            return (HistoricoFuncionarioCargo) funcionarioCargoService.buscar(HistoricoFuncionarioCargo.class, id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Path("/funcionario/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<HistoricoFuncionarioCargo> buscaPorFuncionarioId(@PathParam("id") Integer id) {
        try {
            return funcionarioCargoService.buscarPorFuncionario(id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Path("/cargo/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<HistoricoFuncionarioCargo> buscaPorCargoId(@PathParam("id") Integer id) {
        try {
            return funcionarioCargoService.buscarPorCargo(id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<Entidade> findAll() {
        try {
            return funcionarioCargoService.buscarTodos(HistoricoFuncionarioCargo.class);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Path("{max}/{first}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<Entidade> findRange(@PathParam("max") Integer max, @PathParam("first") Integer first) {
        try {
            return funcionarioCargoService.buscarMaxMin(max, first, HistoricoFuncionarioCargo.class);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Path("qtd")
    @Produces(MediaType.TEXT_PLAIN)
    @Transactional
    public String count() {
        try {
            return funcionarioCargoService.quantidade(HistoricoFuncionarioCargo.class);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

}
