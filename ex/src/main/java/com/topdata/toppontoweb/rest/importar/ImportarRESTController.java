package com.topdata.toppontoweb.rest.importar;

import com.sun.jersey.api.spring.Autowire;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
import com.sun.jersey.spi.resource.Singleton;
import com.topdata.toppontoweb.dto.importar.ImportarHandler;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.importar.ImportarServices;
import java.io.InputStream;
import java.util.Map;
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
@Path("/importar")
@Singleton
@Autowire
public class ImportarRESTController {
    
    @Autowired
    private ImportarServices service;
    
    @GET
    @Path("/status/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response buscarProgresso(@Context HttpServletRequest request, @PathParam("id") Integer id) {
        
        return service.buscarImportacao(id);
    }
    
    @GET
    @Path("/status/cancelar/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response cancelarProgresso(@Context HttpServletRequest request, @PathParam("id") Integer id) {
        return service.cancelarImportacao(id);
    }
    
    //-----------------------------
    //  INICIO DAS IMPORTAÇÕES
    //-----------------------------
    @POST
    @Path("/ferramentas/marcacoes")
    @Consumes({MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON})
    public Response uploadMarcacoes(@Context HttpServletRequest request,
            @FormDataParam("arquivoImportado") InputStream arquivoImportadoInputStream,
            @FormDataParam("arquivoImportado") FormDataContentDisposition arquivoImportadoDetalhes,
            @FormDataParam("idEmpresa") Integer idEmpresa) throws ServiceException {
        
            return service.ImportarArquivoMarcacoes(request, arquivoImportadoInputStream, arquivoImportadoDetalhes, idEmpresa, ImportarHandler.FERRAMENTAS_MARCACOES);
    }
     
    @POST
    @Path("/arquivosFiscais/importar/afd")
    @Consumes({MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON})
    public Response uploadAFD(@Context HttpServletRequest request,
            @FormDataParam("arquivoImportado") InputStream arquivoImportadoInputStream,
            @FormDataParam("arquivoImportado") FormDataContentDisposition arquivoImportadoDetalhes,
            @FormDataParam("idEmpresa") Integer idEmpresa) throws ServiceException {
        
            return service.ImportarArquivoMarcacoes(request, arquivoImportadoInputStream, arquivoImportadoDetalhes, idEmpresa, ImportarHandler.AFD);
    }
    
    @POST
    @Path("/arquivosFiscais/importar/afd/validar")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response validarCabecalhoAFD(@Context HttpServletRequest request, Map<String, String> params) throws ServiceException {
        String cabecalho = params.get("cabecalho");
        return service.validarCabecalho(request, cabecalho, ImportarHandler.AFD);
    }
    
}
