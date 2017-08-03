package com.topdata.toppontoweb.rest.permissoes;

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
import com.topdata.toppontoweb.entity.autenticacao.Grupo;
import com.topdata.toppontoweb.services.permissoes.GrupoService;
import com.topdata.toppontoweb.services.ServiceException;

/**
 * @version 1.0.2 data 02/05/2016
 * @version 1.0.3 data 05/05/2016
 * @version 1.0.5 data 02/09/2016
 *
 * @since 1.0.2 data 02/05/2016
 *
 * @author juliano.ezequiel
 */
@Path("/grupo")
@Singleton
@Autowire
public class GrupoRESTController {

    @Autowired
    private GrupoService grupoServices;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response incluir(Grupo grupo) {
        try {
            return grupoServices.salvar(grupo);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response editar(Grupo grupo) {
        try {
            return grupoServices.atualizar(grupo);
        } catch (ServiceException se) {
            throw new WebApplicationException(se.getResponse());
        }
    }

    @DELETE
    @Path("{id}")
    public Response exluir(@PathParam("id") Integer id) {
        try {
            return grupoServices.excluir(Grupo.class, id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @POST
    @Path("/lista")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response exluirLista(List<Grupo> list) {
        try {
            return grupoServices.excluirList(list);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Grupo find(@PathParam("id") Integer id) {
        try {
            return (Grupo) grupoServices.buscar(Grupo.class, id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Path("/grupotransfer/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String findGrupoTranfer(@PathParam("id") Integer id) {
        try {
            return grupoServices.getGrupoTransfer(id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Path("/descricao/{desc}")
    @Produces(MediaType.APPLICATION_JSON)
    public Grupo buscaPorDescricao(@PathParam("desc") String descricao) {
        try {
            return grupoServices.buscaPorDescricao(new Grupo(descricao));
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Entidade> findAll() {
        try {
            return grupoServices.buscarTodos(Grupo.class);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

}
