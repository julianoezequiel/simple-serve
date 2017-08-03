package com.topdata.toppontoweb.rest.funcionario;

import com.sun.jersey.api.spring.Autowire;
import com.sun.jersey.spi.resource.Singleton;
import com.topdata.toppontoweb.entity.Entidade;
import com.topdata.toppontoweb.entity.funcionario.Afastamento;
import com.topdata.toppontoweb.services.funcionario.afastamento.AfastamentoService;
import com.topdata.toppontoweb.services.ServiceException;
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

/**
 * @version 1.0.6 data 04/10/2016
 * @since 1.0.6 data 04/10/2016
 *
 * @author juliano.ezequiel
 */
@Path("/afastamento")
@Autowire
@Singleton
public class AfastamentoRESTController {

    @Autowired
    private AfastamentoService afastamentoService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(Afastamento afastamento) {
        try {
            return afastamentoService.salvar(afastamento);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/salvarLista")
    public Response salvarLista(List<Afastamento> afastamentoList) {
        try {
            return afastamentoService.salvarLista(afastamentoList);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response edit(Afastamento afastamento) {
        try {
            afastamento.setManual(Boolean.TRUE);
            return afastamentoService.atualizar(afastamento);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @DELETE
    @Path("{id}")
    public Response remove(@PathParam("id") Integer id) {
        try {
            return afastamentoService.excluir(Afastamento.class, id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Afastamento find(@PathParam("id") Integer id) {
        try {
            return afastamentoService.buscar(Afastamento.class, id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Entidade> findAll() {
        try {
            return afastamentoService.buscarTodos(Afastamento.class);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Path("/funcionario/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Afastamento> buscarPorOperador(@PathParam("id") Integer id) {
        try {
            return afastamentoService.buscarPorFuncionario(id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }
    
}
