package com.topdata.toppontoweb.rest.fechamento;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.sun.jersey.api.spring.Autowire;
import com.sun.jersey.spi.resource.Singleton;
import com.topdata.toppontoweb.dto.gerafrequencia.GeraFrequenciaStatusTransfer;
import com.topdata.toppontoweb.entity.Entidade;
import com.topdata.toppontoweb.entity.fechamento.EmpresaFechamentoPeriodo;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.fechamento.EmpresaFechamentoPeriodoService;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;

/**
 * Classe de acesso aos métodos REST para a FechamentoEmpresaPeriodo
 *
 * @version 1.0.0 data 27/04/2017
 * @since 1.0.0 data 27/04/2017
 *
 * @author juliano.ezequiel
 */
@Path("/empresaFechamentoPeriodo")
@Singleton
@Autowire
public class FechamentoEmpresaPeriodoRESTController {

    @Autowired
    private EmpresaFechamentoPeriodoService empresaFechamentoPeriodoService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public GeraFrequenciaStatusTransfer create(@Context HttpServletRequest request, EmpresaFechamentoPeriodo efp) throws IOException {
        try {
            return this.empresaFechamentoPeriodoService.iniciarFechamento(request, efp, CONSTANTES.Enum_OPERACAO.INCLUIR);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public GeraFrequenciaStatusTransfer edit(@Context HttpServletRequest request, EmpresaFechamentoPeriodo efp) throws IOException {
        try {
            return this.empresaFechamentoPeriodoService.iniciarFechamento(request, efp, CONSTANTES.Enum_OPERACAO.EDITAR);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response remove(@PathParam("id") Integer id) {
        try {
            return this.empresaFechamentoPeriodoService.excluir(EmpresaFechamentoPeriodo.class, id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public EmpresaFechamentoPeriodo find(@PathParam("id") Integer id) {
        try {
            return this.empresaFechamentoPeriodoService.buscar(EmpresaFechamentoPeriodo.class, id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Path("/empresa/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<EmpresaFechamentoPeriodo> buscarPorEmpresa(@PathParam("id") Integer id) {
        try {
            return this.empresaFechamentoPeriodoService.buscarPorEmpresa(id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Path("/status/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public GeraFrequenciaStatusTransfer buscarStatus(@PathParam("id") String id) {
        try {
            return this.empresaFechamentoPeriodoService.statusFechamentoProcessamentoCalculos(id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Path("/cancelar/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response cancelar(@PathParam("id") String id) {
        try {
            return this.empresaFechamentoPeriodoService.cancelarFechamento(id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<Entidade> findAll() {
        try {
            return this.empresaFechamentoPeriodoService.buscarTodos(EmpresaFechamentoPeriodo.class);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Path("/status/processarmarcacoesinvalidas/{id}")
    public void processarMarcacoesInvalidas(@Context HttpServletRequest request, @PathParam("id") String id) {
        this.empresaFechamentoPeriodoService.processarMarcacoesInvalidas(id);
    }

}
