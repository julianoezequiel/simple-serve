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
import com.topdata.toppontoweb.entity.funcionario.FuncionarioBancoHoras;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.funcionario.bancohoras.FuncionarioBancoHorasService;

/**
 * @version 1.0.0 data 11/10/2016
 * @since 1.0.0 data 11/10/2016
 *
 * @author juliano.ezequiel
 */
@Path("/funcionariobancohoras")
@Autowire
@Singleton
public class FuncionarioBancoHorasRESTController {

    @Autowired
    private FuncionarioBancoHorasService funcionarioBancoHorasService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(FuncionarioBancoHoras funcionarioBancoHoras) {
        try {
            return funcionarioBancoHorasService.salvar(funcionarioBancoHoras);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response edit(FuncionarioBancoHoras funcionarioBancoHoras) {
        try {
//            funcionarioBancoHoras.setManual(Boolean.TRUE);
            return funcionarioBancoHorasService.atualizar(funcionarioBancoHoras);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Path("/validarexclusao/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response valid(@PathParam("id") Integer id) {
        try {
            return funcionarioBancoHorasService.validarExclusao(id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @DELETE
    @Path("{id}")
    public Response remove(@PathParam("id") Integer id) {
        try {
            return funcionarioBancoHorasService.excluir(FuncionarioBancoHoras.class, id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public FuncionarioBancoHoras find(@PathParam("id") Integer id) {
        try {
            return funcionarioBancoHorasService.buscar(FuncionarioBancoHoras.class, id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Entidade> findAll() {
        try {
            return funcionarioBancoHorasService.buscarTodos(FuncionarioBancoHoras.class);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Path("/funcionario/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<FuncionarioBancoHoras> buscarPorOperador(@PathParam("id") Integer id) {
        try {
            return funcionarioBancoHorasService.buscarPorFuncionario(id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

}
