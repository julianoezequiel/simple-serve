package com.topdata.toppontoweb.rest.funcionario;

import com.sun.jersey.api.spring.Autowire;
import com.sun.jersey.spi.resource.Singleton;
import com.topdata.toppontoweb.entity.funcionario.Compensacao;
import com.topdata.toppontoweb.entity.Entidade;
import com.topdata.toppontoweb.services.funcionario.compensacao.CompensacaoService;
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
 * @version 1.0.6 data 10/10/2016
 * @since 1.0.6 data 10/10/2016
 *
 * @author juliano.ezequiel
 */
@Path("/compensacao")
@Autowire
@Singleton
public class CompensacaoRESTController {
    
    @Autowired
    private CompensacaoService compensacaoService;
    
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(Compensacao compensacao) {
        try {
            return compensacaoService.salvar(compensacao);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response edit(Compensacao compensacao) {
        try {
            compensacao.setManual(Boolean.TRUE);
            return compensacaoService.atualizar(compensacao);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @DELETE
    @Path("{id}")
    public Response remove(@PathParam("id") Integer id) {
        try {
            return compensacaoService.excluir(Compensacao.class, id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Compensacao find(@PathParam("id") Integer id) {
        try {
            return compensacaoService.buscar(Compensacao.class, id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Entidade> findAll() {
        try {
            return compensacaoService.buscarTodos(Compensacao.class);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Path("/funcionario/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Compensacao> buscarPorOperador(@PathParam("id") Integer id) {
        try {
            return compensacaoService.buscarPorFuncionario(id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }
}
