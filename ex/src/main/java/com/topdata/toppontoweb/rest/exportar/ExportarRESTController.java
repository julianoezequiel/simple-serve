/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.topdata.toppontoweb.rest.exportar;

import com.sun.jersey.api.spring.Autowire;
import com.sun.jersey.spi.resource.Singleton;
import com.topdata.toppontoweb.dto.exportar.ExportarHandler;
import com.topdata.toppontoweb.dto.exportar.ExportarTransfer;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.exportar.ExportarServices;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author tharle.camargo
 */
@Path("/exportar")
@Singleton
@Autowire
public class ExportarRESTController {
    
    @Autowired
    private ExportarServices service;
    
    @GET
    @Path("/baixar/{id}")
    @Produces(value = MediaType.MULTIPART_FORM_DATA)
    public Object baixarRelatorio(@PathParam("id") String id) throws ServiceException {
        return this.service.baixarArquivo(id);
    }
    
    @GET
    @Path("/status/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response buscarProgresso(@Context HttpServletRequest request, @PathParam("id") String id) {
        
        return service.buscarExportacao(id);
    }
    
    @GET
    @Path("/status/cancelar/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response cancelarProgresso(@Context HttpServletRequest request, @PathParam("id") String id) {
        return service.cancelarExportacao(id);
    }
    
    @GET
    @Path("/status/processarmarcacoesinvalidas/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response processarMarcacoesInvalidas(@Context HttpServletRequest request, @PathParam("id") String id) {
        return service.processarMarcacoesInvalidas(id);
    }
    
    //-----------------------------
    //  INICIO DAS EXPORTAÇÕES
    //-----------------------------
    @POST
    @Path("/arquivosfiscais/afdt")
    @Consumes(value = MediaType.APPLICATION_JSON)
    @Produces(value = MediaType.APPLICATION_JSON)
    public Object gerarAFDT(@Context HttpServletRequest request, ExportarTransfer transfer) throws ServiceException {
        
        return service.exportarArquivo(transfer, ExportarHandler.AFDT, request);
    }
    
    @POST
    @Path("/arquivosfiscais/acjef")
    @Consumes(value = MediaType.APPLICATION_JSON)
    @Produces(value = MediaType.APPLICATION_JSON)
    public Object gerarACJEF(@Context HttpServletRequest request, ExportarTransfer transfer) throws ServiceException {
        
        return service.exportarArquivo(transfer, ExportarHandler.ACJEF, request);
    }
}
