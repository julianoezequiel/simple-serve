/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.topdata.toppontoweb.rest.ferramentas;

import com.sun.jersey.api.spring.Autowire;
import com.sun.jersey.spi.resource.Singleton;
import com.topdata.toppontoweb.dto.marcacoes.MarcacoesFuncionarioTransfer;
import com.topdata.toppontoweb.dto.relatorios.RelatoriosGeraFrequenciaTransfer;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.marcacoes.ManutencaoMarcacoesService;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author tharle.camargo
 */
@Path("/manutencaomarcacoes")
@Singleton
@Autowire
public class ManutencaoMarcacoesRESTController { 
    @Autowired
    ManutencaoMarcacoesService manutencaoMarcacoesService;
            
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Object iniciarProcessoManutecaoMarcacoes(@Context HttpServletRequest request, RelatoriosGeraFrequenciaTransfer transfer) throws IOException {
        try {
            
            return manutencaoMarcacoesService.iniciarProcessoManutecaoMarcacoes(request, transfer);
            
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }
    
    @GET
    @Path("/status/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response buscarProgresso(@Context HttpServletRequest request, @PathParam("id") String id) {
        
        return manutencaoMarcacoesService.buscarProgresso(id);
    }
    
    @GET
    @Path("/status/cancelar/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response cancelarProgresso(@Context HttpServletRequest request, @PathParam("id") String id) {
        return manutencaoMarcacoesService.cancelarProgresso(id);
    }
    
    //--------------------------
    // Funcionario
    //--------------------------
    @POST
    @Path("/funcionario")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response buscarMarcacoesFuncionario(@Context HttpServletRequest request, RelatoriosGeraFrequenciaTransfer transfer) throws IOException {
        try {
            
            return manutencaoMarcacoesService.buscarMarcacoesPorFuncionarioPeriodo(transfer.getFuncionario().getIdFuncionario(), transfer.getPeriodoInicio(), transfer.getPeriodoFim());
            
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }
    
}
