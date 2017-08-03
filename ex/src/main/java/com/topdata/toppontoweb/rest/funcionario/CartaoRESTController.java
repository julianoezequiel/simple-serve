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
import com.topdata.toppontoweb.entity.funcionario.Cartao;
import com.topdata.toppontoweb.services.funcionario.cartao.CartaoService;
import com.topdata.toppontoweb.services.ServiceException;

/**
 * @version 1.0.5 data 05/09/2016
 * @since 1.0.5 data 05/09/2016
 *
 * @author juliano.ezequiel
 */
@Path("/cartao")
@Singleton
@Autowire
public class CartaoRESTController {

    @Autowired
    private CartaoService cartaoService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(Cartao cartao) {
        try {
            return cartaoService.salvar(cartao);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response edit(Cartao cartao) {
        try {
            return cartaoService.atualizar(cartao);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @DELETE
    @Path("{id}")
    public Response remove(@PathParam("id") Integer id) {
        try {
            return cartaoService.excluir(Cartao.class, id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Cartao find(@PathParam("id") Integer id) {
        try {
            return cartaoService.buscar(Cartao.class, id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Entidade> findAll() {
        try {
            return cartaoService.buscarTodos(Cartao.class);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Path("/funcionario/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Cartao> buscarPorOperador(@PathParam("id") Integer id) {
        try {
            return cartaoService.buscarPorFuncionario(id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

}
