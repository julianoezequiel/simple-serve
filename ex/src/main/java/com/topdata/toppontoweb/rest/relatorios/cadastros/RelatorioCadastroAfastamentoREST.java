package com.topdata.toppontoweb.rest.relatorios.cadastros;


import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;

import com.sun.jersey.api.spring.Autowire;
import com.sun.jersey.spi.resource.Singleton;
import com.topdata.toppontoweb.dto.gerafrequencia.GeraFrequenciaStatusTransfer;
import com.topdata.toppontoweb.dto.relatorios.cadastros.RelatorioCadastroAfastamentoTransfer;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.relatorios.cadastros.RelatorioCadastroAfastamentoServices;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;

/**
 *
 * @author tharle.camargo
 */
@Path("/relatorios/cadastros/afastamento")
@Singleton
@Autowire
public class RelatorioCadastroAfastamentoREST {
   
    @Autowired
    RelatorioCadastroAfastamentoServices services;
    
    @POST
    @Consumes(value = MediaType.APPLICATION_JSON)
    @Produces(value = MediaType.APPLICATION_JSON)
    public GeraFrequenciaStatusTransfer gerarRelatorio(@Context HttpServletRequest request, RelatorioCadastroAfastamentoTransfer relatorioTransfer) throws ServiceException {
        return this.services.iniciar(request, relatorioTransfer,CONSTANTES.Enum_TIPO_PROCESSO.RELATORIO);
    }
       
}
