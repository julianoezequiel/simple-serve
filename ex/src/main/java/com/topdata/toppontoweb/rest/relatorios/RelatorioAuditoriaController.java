package com.topdata.toppontoweb.rest.relatorios;

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
import com.topdata.toppontoweb.dto.relatorios.RelatorioAuditoriaTransfer;
import com.topdata.toppontoweb.dto.gerafrequencia.GeraFrequenciaStatusTransfer;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.relatorios.auditoria.RelatorioAuditoriaServices;
import com.topdata.toppontoweb.services.relatorios.RelatorioHandler;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;

/**
 *
 * @author juliano.ezequiel
 */
@Path("/relatorios/auditoria")
@Singleton
@Autowire
public class RelatorioAuditoriaController {

    @Autowired
    private RelatorioAuditoriaServices relatorioAuditoriaServices;

    @POST
    @Consumes(value = MediaType.APPLICATION_JSON)
    @Produces(value = MediaType.APPLICATION_JSON)
    public GeraFrequenciaStatusTransfer gerarAuditoria(@Context HttpServletRequest request, RelatorioAuditoriaTransfer relatorioTransfer) throws ServiceException {
        relatorioTransfer.tipo_relatorio = RelatorioHandler.TIPO_RELATORIO.AUDITORIA;
        return this.relatorioAuditoriaServices.iniciar(request, relatorioTransfer,CONSTANTES.Enum_TIPO_PROCESSO.RELATORIO);
    }
}
