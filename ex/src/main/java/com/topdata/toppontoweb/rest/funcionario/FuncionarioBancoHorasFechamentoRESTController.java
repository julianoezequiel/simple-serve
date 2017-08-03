package com.topdata.toppontoweb.rest.funcionario;

import com.topdata.toppontoweb.services.funcionario.bancohoras.fechamento.OpcoesFechamento;
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
import com.topdata.toppontoweb.dto.funcionario.bancohoras.Gatilho;
import com.topdata.toppontoweb.dto.funcionario.bancohoras.SaldoBH;
import com.topdata.toppontoweb.entity.Entidade;
import com.topdata.toppontoweb.entity.funcionario.FuncionarioBancoHorasFechamento;
import com.topdata.toppontoweb.entity.tipo.TipoAcerto;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.funcionario.bancohoras.fechamento.FechamentoSubTotal;
import com.topdata.toppontoweb.services.funcionario.bancohoras.fechamento.FuncionarioBancoDeHorasFechamentoService;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

/**
 * @version 1.0.0 data 19/06/2017
 * @since 1.0.0 data 19/06/2017
 *
 * @author juliano.ezequiel
 */
@Path("/funcionariobancohorasfechamento")
@Autowire
@Singleton
public class FuncionarioBancoHorasFechamentoRESTController {

    @Autowired
    private FuncionarioBancoDeHorasFechamentoService service;

    @Autowired
    private FechamentoSubTotal fechamentoSubTotal;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(FuncionarioBancoHorasFechamento funcionarioBancoHorasFechamento) {
        try {
            funcionarioBancoHorasFechamento.setManual(true);
            return this.service.salvar(funcionarioBancoHorasFechamento);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/saldo")
    public Response verificarSaldo(@Context HttpServletRequest request, SaldoBH debitoCreditoBH) {
        try {
            return this.service.verificarSaldo(debitoCreditoBH);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response edit(FuncionarioBancoHorasFechamento funcionarioBancoHorasFechamento) {
        try {
//            funcionarioBancoHorasFechamento.setManual(true);
            funcionarioBancoHorasFechamento.setManual(Boolean.TRUE);
            return this.service.atualizar(funcionarioBancoHorasFechamento);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @DELETE
    @Path("{id}")
    public Response remove(@PathParam("id") Integer id) {
        try {
            return this.service.excluir(FuncionarioBancoHorasFechamento.class, id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public FuncionarioBancoHorasFechamento find(@PathParam("id") Integer id) {
        try {
            return this.fechamentoSubTotal.verificarSeSaldoInicial(new FuncionarioBancoHorasFechamento(id));
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Entidade> findAll() {
        try {
            return this.service.buscarTodos(FuncionarioBancoHorasFechamento.class);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Path("/funcionario/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<FuncionarioBancoHorasFechamento> buscarPorOperador(@PathParam("id") Integer id) {
        try {
            return this.service.buscarPorFuncionario(id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Path("/tiposacerto")
    @Produces(MediaType.APPLICATION_JSON)
    public List<TipoAcerto> buscarOpcoesFechamento() {
        try {
            return this.service.buscarTiposAcerto();
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/gatilho")
    public Response gatilho(Gatilho gatilho) {
        try {
            return this.service.consultaGatilho(gatilho);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }
}
