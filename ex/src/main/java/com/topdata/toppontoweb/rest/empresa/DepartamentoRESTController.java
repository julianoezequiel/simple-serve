package com.topdata.toppontoweb.rest.empresa;

import java.io.IOException;
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
import com.topdata.toppontoweb.entity.empresa.Departamento;
import com.topdata.toppontoweb.services.empresa.DepartamentoService;
import com.topdata.toppontoweb.services.ServiceException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

/**
 * Classe de acesso aos métodos REST para o Departamento
 *
 * @version 1.0.3 data 18/05/2016
 * @since 1.0.3 data 18/05/2016
 *
 * @author juliano.ezequiel
 */
@Path("/departamento")
@Singleton
@Autowire
public class DepartamentoRESTController {

    @Autowired
    private DepartamentoService departamentoService;

    /**
     * Insere um novo Departamento
     *
     * @param request
     * @param entity
     * @return
     * @throws IOException
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response create(@Context HttpServletRequest request, Departamento entity) throws IOException {
        try {
            return departamentoService.salvar(request, entity);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response edit(@Context HttpServletRequest request, Departamento entity) throws IOException {
        try {
            return departamentoService.atualizar(request, entity);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<Entidade> findAll() {
        try {
            return departamentoService.buscarTodos(Departamento.class);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response remove(@PathParam("id") Integer id) {
        try {
            return departamentoService.excluir(Departamento.class, id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }
    
    @GET
    @Path("ativo")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<Departamento> findAllAtivos() {
        try {
            return departamentoService.buscarTodosAtivos();
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @DELETE
    @Path("/empresa/{id}/descricao/{desc}")
    @Transactional
    public Response removePorDescricaoDepartamentoEmpresa(@PathParam("id") Integer id, @PathParam("desc") String desc) {
        try {
            return departamentoService.excluir(id, desc);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Path("/empresa/grupoacesso/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public String buscaEmpresaDeptTransfer(@PathParam("id") Integer id) {
        try {
            return departamentoService.buscarEmpresaDeptTransfer(id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Path("/empresa/grupoacesso")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public String buscaTodosEmpresaDeptTransfer() {
        try {
            return departamentoService.buscarEmpresaDeptTransfer(null);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Departamento find(@PathParam("id") Integer id) {
        try {
            return departamentoService.buscar(Departamento.class, id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Path("/empresa/{id}/grupo/{idGrupo}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<Departamento> buscarPorEmpresa(@PathParam("id") Integer idEmpresa, @PathParam("idGrupo") Integer idGrupo) {
        try {
            return departamentoService.buscarPorEmpresa(idEmpresa, idGrupo);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }
    
    @GET
    @Path("/empresa/{id}/grupo/{idGrupo}/ativo")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<Departamento> buscarPorEmpresaDepartamentosAtivos(@PathParam("id") Integer idEmpresa, @PathParam("idGrupo") Integer idGrupo) {
        try {
            return departamentoService.buscarPorEmpresaAtivos(idEmpresa, idGrupo);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Path("/empresa/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<Departamento> buscarPorEmpresa(@PathParam("id") Integer id) {
        try {
            return departamentoService.buscarPorEmpresa(id, null);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    /**
     * Busca a lista pela descrição do departamento e pela empresa
     *
     * @since 1.0.3 data 24/05/2016
     * @param id
     * @param desc
     * @return Lista de Departamentos
     */
    @GET
    @Path("/empresa/{id}/descricao/{desc}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<Departamento> buscarPorDescricaoDepartamentoEmpresa(@PathParam("id") Integer id, @PathParam("desc") String desc) {
        try {
            return departamentoService.buscarPorDescricaoDepartamentoEmpresa(id, desc);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    /**
     *
     * @param max
     * @param first
     * @return
     */
    @GET
    @Path("{max}/{first}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<Entidade> findRange(@PathParam("max") Integer max, @PathParam("first") Integer first) {
        try {
            return departamentoService.buscarMaxMin(max, first, Departamento.class);
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
            return departamentoService.quantidade(Departamento.class);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }
}
