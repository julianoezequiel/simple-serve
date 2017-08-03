package com.topdata.toppontoweb.rest.funcionario;

import java.io.InputStream;
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

import com.sun.jersey.api.spring.Autowire;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
import com.sun.jersey.spi.resource.Singleton;
import com.topdata.toppontoweb.dto.datatable.PaginacaoDataTableRetornoTransfer;
import com.topdata.toppontoweb.dto.datatable.PaginacaoDataTableTransfer;
import com.topdata.toppontoweb.dto.funcionario.FuncionarioPaginacaoTransfer;
import com.topdata.toppontoweb.entity.funcionario.Funcionario;
import com.topdata.toppontoweb.services.funcionario.FuncionarioService;
import com.topdata.toppontoweb.services.ServiceException;
import org.springframework.stereotype.Component;

/**
 * Classe de acesso aos métodos REST para o Funcionário
 *
 * @version 1.0.3 data 09/05/2016
 * @since 1.0.1
 *
 * @author juliano.ezequiel
 */
@Path("/funcionario")
@Singleton
@Autowire
@Component
public class FuncionarioRESTController {

    @Autowired
    private FuncionarioService funcionarioService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(Funcionario funcionario) {
        try {
            return funcionarioService.salvar(funcionario);
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
            return funcionarioService.upload(request, uploadedInputStream, fileDetail, foto);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }

    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response edit(Funcionario funcionario) {
        try {
            return funcionarioService.atualizar(funcionario);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @DELETE
    @Path("{id}")
    public Response remove(@PathParam("id") Integer id) {
        try {
            return funcionarioService.excluir(Funcionario.class, id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Funcionario find(@PathParam("id") Integer id) {
        try {
            return funcionarioService.buscar(Funcionario.class, id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Funcionario> findAll() {
        try {
            return funcionarioService.buscarTodos();
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Path("/ativos")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Funcionario> findAllAtivos() {
        try {
            return funcionarioService.buscarTodosAtivos();
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Path("/grupoPermisso/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Funcionario> buscarPorPermissao(@PathParam("id") Integer id) {
        try {
            return funcionarioService.buscarPorPermissao(id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Path("/departamento/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Funcionario> buscarPorDepartamento(@Context HttpServletRequest request, @PathParam("id") Integer id) {
        try {
            return funcionarioService.buscarPorDepartamento(request, id, false);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Path("/departamento/{id}/ativos")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Funcionario> buscarPorDepartamentoSomenteAtivos(@Context HttpServletRequest request, @PathParam("id") Integer id) {
        try {
            return funcionarioService.buscarPorDepartamento(request, id, true);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Path("qtd")
    @Produces(MediaType.TEXT_PLAIN)
    public String count() {
        try {
            return funcionarioService.quantidade(Funcionario.class);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Path("/pis/{pis}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Funcionario> BuscarPorPis(@PathParam("pis") String pis) {
        try {
            Funcionario f = new Funcionario();
            f.setPis(pis);
            return funcionarioService.buscaPorPisEmpresa(f);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Path("/operador/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Funcionario buscarPorOperador(@PathParam("id") Integer id) {
        try {
            return funcionarioService.buscarPorOperador(id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Path("/matricula/{matri}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Funcionario> buscarPorMatricula(@PathParam("matri") String matricula) {
        try {
            return funcionarioService.buscaPorMatricula(matricula);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Path("/semoperador")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Funcionario> buscarSemOperador() {
        try {
            return funcionarioService.buscaSemOperador();
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @POST
    @Path("/paginacao")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response buscarPorPaginacao(FuncionarioPaginacaoTransfer fpt) {
        try {
            return funcionarioService.buscarPorPaginacao(fpt);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }
    
    @POST
    @Path("/paginacao/grupoPermisso")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON) 
    public PaginacaoDataTableRetornoTransfer buscarParginacaoDataTable(PaginacaoDataTableTransfer transfer) {
        try {
            String busca = "";
//            PaginacaoTransfer transfer = new PaginacaoTransfer(pagina, qntPorPagina, busca, start);
            return funcionarioService.buscarPorPaginacaoDataTable(transfer);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

//    @GET
//    @Path("/paginacao/{pagina}/{qntPorPagina}/{busca}")
//    @Produces(MediaType.APPLICATION_JSON)
//    public List<FuncionarioTransfer> buscarPorPaginacao2(@PathParam("pagina") Integer pagina, @PathParam("qntPorPagina") Integer qntPorPagina, @PathParam("busca") String busca) {
//        try {
//            return funcionarioService.buscarPorPaginacao(busca, pagina, qntPorPagina);
//        } catch (ServiceException ex) {
//            throw new WebApplicationException(ex.getResponse());
//        }
//    }
}
