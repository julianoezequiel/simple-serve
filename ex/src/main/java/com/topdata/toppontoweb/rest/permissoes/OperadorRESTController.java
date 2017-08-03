package com.topdata.toppontoweb.rest.permissoes;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.engine.JRException;

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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import com.sun.jersey.api.spring.Autowire;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
import com.sun.jersey.spi.resource.Singleton;
import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.entity.Entidade;
import com.topdata.toppontoweb.entity.autenticacao.Operador;
import com.topdata.toppontoweb.services.permissoes.OperadorService;
import com.topdata.toppontoweb.dto.OperadorTransfer;
import com.topdata.toppontoweb.services.ServiceException;
import static com.topdata.toppontoweb.utils.Utils.ConvertErrorToJson;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;

/**
 * @version 1.0.1 data 09/05/2016
 * @since 1.0.1 data 09/05/2016
 * @author juliano.ezequiel
 */
@Path("/operador")
@Singleton
@Autowire
public class OperadorRESTController {

    @Autowired
    private OperadorService operadorService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response incluir(@Context HttpServletRequest request, Operador operador) {
        try {
            return operadorService.salvar(request, operador);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @POST
    @Path("/upload")
    @Consumes({MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON})
    public Response upload(@Context HttpServletRequest request,
            @FormDataParam("file") InputStream uploadedInputStream,
            @FormDataParam("file") FormDataContentDisposition fileDetail, @FormDataParam("foto") String foto) {
        try {
            return operadorService.upload(request, uploadedInputStream, fileDetail, foto);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }

    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response editar(@Context HttpServletRequest request, Operador operador) {
        try {
            return operadorService.atualizar(request, operador);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response exluir(@PathParam("id") Integer id) {
        try {
            return operadorService.excluir(Operador.class, id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @DELETE
    @Path("/nome/{nome}")
    @Transactional
    public Response excluirPorNome(@PathParam("nome") String nome) {
        try {
            return operadorService.excluirPorNome(nome);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public OperadorTransfer buscar(@PathParam("id") Integer id) {
        try {
            return new OperadorTransfer((Operador) operadorService.buscar(Operador.class, id));
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Path("/grupo/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<OperadorTransfer> buscarPorGrupo(@PathParam("id") Integer id) {
        try {
            return operadorService.buscarPorGrupo(id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Path("/grupo/{id}/ativos")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<OperadorTransfer> buscarPorGrupoeAtivos(@PathParam("id") Integer id) {
        try {
            return operadorService.buscarPorGrupoeAtivos(id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Path("/ativo/{ativo}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<OperadorTransfer> buscarOperadorAtivo(@PathParam("ativo") Boolean ativo) {
        try {
            return operadorService.buscarAtivos(ativo);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Path("/grupo")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<OperadorTransfer> buscarPorGrupo() {
        try {
            return operadorService.buscarPorGrupo(null);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Path("/nome/{nome}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public OperadorTransfer buscaPorNome(@PathParam("nome") String nome) {
        try {
            return new OperadorTransfer(operadorService.buscaOperadorPorNome(new Operador(nome)));
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<OperadorTransfer> buscarTodosOperTranfer() {
        try {
            return operadorService.buscarTodosOperTranfer();
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Path("noTransfer")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @PreAuthorize(CONSTANTES.PRE_AUTH_MASTER)
    public List<Entidade> all() {
        try {
            return operadorService.buscarTodos(Operador.class);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Path("{max}/{first}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<OperadorTransfer> buscarMaxMin(@PathParam("max") Integer max, @PathParam("first") Integer first) {
        try {
            List<OperadorTransfer> operadorTransfers = new ArrayList<>();
            for (Object e : operadorService.buscarMaxMin(max, first, Operador.class)) {
                operadorTransfers.add(new OperadorTransfer((Operador) e));
            }
            return operadorTransfers;
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Path("qtd")
    @Produces(MediaType.TEXT_PLAIN)
    @Transactional
    public String quantidade() {
        try {
            return operadorService.quantidade(Operador.class);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @PUT
    @Path("/mudarsenha")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response mudarSenha(Operador operador) {
        try {
            return operadorService.mudarSenha(operador);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }

    }

    @PUT
    @Path("/enviarEmail")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response EnviarEmail(@Context HttpServletRequest request, Operador operador) {
        try {
            return operadorService.reenviarEmailConfirmacao(request, operador);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }

    }

}
