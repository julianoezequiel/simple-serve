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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import com.sun.jersey.api.spring.Autowire;
import com.sun.jersey.spi.resource.Singleton;
import com.topdata.toppontoweb.entity.Entidade;
import com.topdata.toppontoweb.entity.empresa.Empresa;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.empresa.EmpresaService;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;

/**
 * Classe de acesso aos métodos REST para a Empresa
 *
 * @version 1.0.0 data 17/05/2016
 * @since 1.0.0 data 17/05/2016
 *
 * @author juliano.ezequiel
 */
@Path("/empresas")
@Singleton
@Autowire
public class EmpresaRESTController {

    @Autowired
    private EmpresaService empresaService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(Empresa entity) throws IOException {
        try {
            return empresaService.salvar(entity);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("testar/{qtd}")
    @PreAuthorize(CONSTANTES.PRE_AUTH_MASTER)
    public Response create1000(@PathParam("qtd") Integer id) throws IOException {
        try {
            return empresaService.testar(id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response edit(Empresa entity) throws IOException {
        try {
            return empresaService.atualizar(entity);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response remove(@PathParam("id") Integer id) {
        try {
            return empresaService.excluir(Empresa.class, id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Empresa find(@PathParam("id") Integer id) {
        try {
            return empresaService.buscar(Empresa.class, id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<Entidade> findAll() {
        try {
            return empresaService.buscarTodos(Empresa.class, false);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Path("/documento/{documento}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Empresa buscarPorCnpj(@PathParam("documento") String documento) {
        try {
            Empresa empresa = new Empresa();
            empresa.setDocumento(documento);
            return empresaService.buscaPorDocumento(empresa);
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
            return empresaService.quantidade(Empresa.class);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Path("/grupo/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<Empresa> buscaPorGrupoPermissao(@PathParam("id") Integer id) {
        try {
            return empresaService.buscaPorGrupoPermissao(id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Path("/grupo/ativas/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<Empresa> buscaPorGrupoPermissaoAtivas(@PathParam("id") Integer id) {
        try {
            return empresaService.buscaPorGrupoPermissaoAtivas(id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Path("/dsr/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<Empresa> buscaEmpresaPorDsr(@PathParam("id") Integer id) {
        try {
            return empresaService.buscaPorDrs(id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    /**
     * Lista as empresas que estão na permissão do grupo de acesso e não possuem
     * Dsr
     *
     * @param id
     * @return Lista de Empresas
     */
    @GET
    @Path("/gruposemdsr/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<Empresa> buscaEmpresaSemDsr(@PathParam("id") Integer id) {
        try {
            return empresaService.buscaSemDsr(id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

}
