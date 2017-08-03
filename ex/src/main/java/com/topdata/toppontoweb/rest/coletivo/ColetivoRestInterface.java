package com.topdata.toppontoweb.rest.coletivo;

import java.util.List;

import javax.ws.rs.core.Response;

import org.springframework.stereotype.Component;

import com.topdata.toppontoweb.dto.ColetivoTransfer;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

/**
 * Contrato obrigatório para REST de lançamentos coletivos
 *
 * @version 1.0.0 data 21/12/2016
 * @since 1.0.0 data 21/12/2016
 *
 * @author juliano.ezequiel
 */
@Component
public interface ColetivoRestInterface {

    public Response salvar(@Context HttpServletRequest request, ColetivoTransfer coletivoTransfer);

    public Response atualizar(@Context HttpServletRequest request, ColetivoTransfer coletivoTransfer);

    public List<ColetivoTransfer> listar(@Context HttpServletRequest request);

    public ColetivoTransfer listar(@Context HttpServletRequest request, Integer id);

    public Response excluir(@Context HttpServletRequest request, Integer id);

}
