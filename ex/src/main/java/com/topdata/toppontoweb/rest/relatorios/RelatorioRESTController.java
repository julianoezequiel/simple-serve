package com.topdata.toppontoweb.rest.relatorios;

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

import com.sun.jersey.api.spring.Autowire;
import com.sun.jersey.spi.resource.Singleton;
import com.topdata.toppontoweb.dto.gerafrequencia.GeraFrequenciaStatusTransfer;
import com.topdata.toppontoweb.dto.relatorios.RelatoriosGeraFrequenciaTransfer;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.relatorios.RelatorioHandler;
import com.topdata.toppontoweb.services.gerafrequencia.integracao.GeraFrequencia;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;

/**
 *
 * @author juliano.ezequiel
 */
@Path("/relatorios")
@Singleton
@Autowire
public class RelatorioRESTController {

    @Autowired
    GeraFrequencia relatorioServices;

    @GET
    @Path("{id}/{idFormato}")
    @Produces(value = MediaType.MULTIPART_FORM_DATA)
    public Object baixarRelatorio(@PathParam("id") String id, @PathParam("idFormato") String idFormato) throws ServiceException {
        return this.relatorioServices.baixarRelatorio(id, idFormato);
    }

    @GET
    @Path("cancelar/{id}")
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response cancelar(@PathParam("id") String id) throws ServiceException {
        return this.relatorioServices.cancelar(id);
    }

    @GET
    @Path("{id}")
    @Produces(value = MediaType.APPLICATION_JSON)
    public GeraFrequenciaStatusTransfer status(@PathParam("id") String id) throws ServiceException {
        return this.relatorioServices.status(id);
    }

    @POST
    @Consumes(value = MediaType.APPLICATION_JSON)
    @Produces(value = MediaType.APPLICATION_JSON)
    @Path("espelho")
    public GeraFrequenciaStatusTransfer gerarEspelho(@Context HttpServletRequest request, RelatoriosGeraFrequenciaTransfer relatorioTransfer) throws ServiceException {
        
        relatorioTransfer.setMarcacoesInvalidas(true);
        relatorioTransfer.tipo_relatorio = RelatorioHandler.TIPO_RELATORIO.ESPELHO;
        return this.relatorioServices.iniciar(request, relatorioTransfer, CONSTANTES.Enum_TIPO_PROCESSO.RELATORIO);
    }
    
    @POST
    @Consumes(value = MediaType.APPLICATION_JSON)
    @Produces(value = MediaType.APPLICATION_JSON)
    @Path("espelhofiscal")
    public GeraFrequenciaStatusTransfer gerarEspelhoFiscal(@Context HttpServletRequest request, RelatoriosGeraFrequenciaTransfer relatorioTransfer) throws ServiceException {
        relatorioTransfer.setMarcacoesInvalidas(true);
        relatorioTransfer.tipo_relatorio = RelatorioHandler.TIPO_RELATORIO.ESPELHO_FISCAL;
        return this.relatorioServices.iniciar(request, relatorioTransfer, CONSTANTES.Enum_TIPO_PROCESSO.RELATORIO);
    }

    @POST
    @Consumes(value = MediaType.APPLICATION_JSON)
    @Produces(value = MediaType.APPLICATION_JSON)
    @Path("presenca")
    public GeraFrequenciaStatusTransfer gerarPresenca(@Context HttpServletRequest request, RelatoriosGeraFrequenciaTransfer relatorioTransfer) throws ServiceException {
        relatorioTransfer.setMarcacoesInvalidas(false);
        relatorioTransfer.tipo_relatorio = RelatorioHandler.TIPO_RELATORIO.PRESENCA;
        return this.relatorioServices.iniciar(request, relatorioTransfer, CONSTANTES.Enum_TIPO_PROCESSO.RELATORIO);
    }

    @POST
    @Consumes(value = MediaType.APPLICATION_JSON)
    @Produces(value = MediaType.APPLICATION_JSON)
    @Path("frequencia")
    public GeraFrequenciaStatusTransfer gerarFrequencia(@Context HttpServletRequest request, RelatoriosGeraFrequenciaTransfer relatorioTransfer) throws ServiceException {
        relatorioTransfer.setMarcacoesInvalidas(false);
        relatorioTransfer.tipo_relatorio = RelatorioHandler.TIPO_RELATORIO.FREQUENCIA;
        return this.relatorioServices.iniciar(request, relatorioTransfer, CONSTANTES.Enum_TIPO_PROCESSO.RELATORIO);
    }

    @POST
    @Consumes(value = MediaType.APPLICATION_JSON)
    @Produces(value = MediaType.APPLICATION_JSON)
    @Path("horasextras")
    public GeraFrequenciaStatusTransfer gerarHorasextra(@Context HttpServletRequest request, RelatoriosGeraFrequenciaTransfer relatorioTransfer) throws ServiceException {
        relatorioTransfer.setMarcacoesInvalidas(false);
        relatorioTransfer.tipo_relatorio = RelatorioHandler.TIPO_RELATORIO.HORAS_EXTRAS;
        return this.relatorioServices.iniciar(request, relatorioTransfer, CONSTANTES.Enum_TIPO_PROCESSO.RELATORIO);
    }

    @POST
    @Consumes(value = MediaType.APPLICATION_JSON)
    @Produces(value = MediaType.APPLICATION_JSON)
    @Path("bancohoras")
    public GeraFrequenciaStatusTransfer gerarBancohoras(@Context HttpServletRequest request, RelatoriosGeraFrequenciaTransfer relatorioTransfer) throws ServiceException {
        relatorioTransfer.setMarcacoesInvalidas(false);
        relatorioTransfer.tipo_relatorio = RelatorioHandler.TIPO_RELATORIO.BANCO_DE_HORAS;
        return this.relatorioServices.iniciar(request, relatorioTransfer, CONSTANTES.Enum_TIPO_PROCESSO.RELATORIO);
    }

    @POST
    @Consumes(value = MediaType.APPLICATION_JSON)
    @Produces(value = MediaType.APPLICATION_JSON)
    @Path("absenteismo")
    public GeraFrequenciaStatusTransfer gerarAbsenteismo(@Context HttpServletRequest request, RelatoriosGeraFrequenciaTransfer relatorioTransfer) throws ServiceException {
        relatorioTransfer.setMarcacoesInvalidas(false);
        relatorioTransfer.tipo_relatorio = RelatorioHandler.TIPO_RELATORIO.ABSENTEISMO;
        return this.relatorioServices.iniciar(request, relatorioTransfer, CONSTANTES.Enum_TIPO_PROCESSO.RELATORIO);
    }

    @POST
    @Consumes(value = MediaType.APPLICATION_JSON)
    @Produces(value = MediaType.APPLICATION_JSON)
    @Path("interjornada")
    public GeraFrequenciaStatusTransfer gerarInterjornada(@Context HttpServletRequest request, RelatoriosGeraFrequenciaTransfer relatorioTransfer) throws ServiceException {
        relatorioTransfer.setMarcacoesInvalidas(false);
        relatorioTransfer.tipo_relatorio = RelatorioHandler.TIPO_RELATORIO.INTERJORNADA;
        return this.relatorioServices.iniciar(request, relatorioTransfer, CONSTANTES.Enum_TIPO_PROCESSO.RELATORIO);
    }

    @POST
    @Consumes(value = MediaType.APPLICATION_JSON)
    @Produces(value = MediaType.APPLICATION_JSON)
    @Path("intrajornada")
    public GeraFrequenciaStatusTransfer gerarIntrajornada(@Context HttpServletRequest request, RelatoriosGeraFrequenciaTransfer relatorioTransfer) throws ServiceException {
        relatorioTransfer.setMarcacoesInvalidas(false);
        relatorioTransfer.tipo_relatorio = RelatorioHandler.TIPO_RELATORIO.INTRAJORNADA;
        return this.relatorioServices.iniciar(request, relatorioTransfer, CONSTANTES.Enum_TIPO_PROCESSO.RELATORIO);
    }
}
