package com.topdata.toppontoweb.services;

import com.topdata.toppontoweb.dto.ColetivoResultado;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.topdata.toppontoweb.dto.EntidadeRetorno;
import com.topdata.toppontoweb.dto.MsgRetorno;
import com.topdata.toppontoweb.utils.constantes.MSG;
import com.topdata.toppontoweb.utils.constantes.MSG.TIPOMSG;
import java.util.List;

/**
 * Retornos genéricos dos responses dos registros
 *
 * @version 1.0.3 data 05/05/2016
 * @since 1.0.3 data 05/05/2016
 *
 * @author juliano.ezequiel
 *
 * @param <Entidade>
 */
@Service
public class TopPontoResponse<Entidade> {

    @Autowired
    private MessageSource msg;

    public Response responseNaoAutorizado(String mensagem) {
        return Response.status(Response.Status.UNAUTHORIZED).entity(
                new MsgRetorno(msg.getMessage(mensagem,
                        new Object[]{}, LocaleContextHolder.getLocale()), TIPOMSG.ALERTA))
                .type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    public Response response(Response.Status status, MSG.CADASTRO mensagem, Class<Entidade> obj, TIPOMSG tipomsg) {
        return Response.status(status).entity(
                new MsgRetorno(msg.getMessage(mensagem.getResource(),
                        new Object[]{obj.getSimpleName()}, LocaleContextHolder.getLocale()), tipomsg))
                .type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    public Response response(Response.Status status, MSG.OPERADOR mensagem, TIPOMSG tipomsg) {
        return Response.status(status).entity(
                new MsgRetorno(msg.getMessage(mensagem.getResource(),
                        new Object[]{}, LocaleContextHolder.getLocale()), tipomsg))
                .type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    public Response sucessoSalvar(Class<Entidade> obj) {
        return Response.status(Response.Status.CREATED).entity(
                new MsgRetorno(msg.getMessage(MSG.CADASTRO.SUCESSO_SALVAR.getResource(),
                        new Object[]{obj.getSimpleName()}, LocaleContextHolder.getLocale()), TIPOMSG.SUCESSO))
                .type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    public Response sucessoSalvar(String mensagem, Entidade e) {
        return Response.status(Response.Status.CREATED).entity(
                new EntidadeRetorno(
                        new MsgRetorno(msg.getMessage(MSG.CADASTRO.SUCESSO_SALVAR.getResource(),
                                new Object[]{mensagem}, LocaleContextHolder.getLocale()), TIPOMSG.SUCESSO), (Entidade) e))
                .type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    public Response sucesso() {
        return Response.status(Response.Status.OK).entity(
                new MsgRetorno(msg.getMessage(MSG.REST.SUCESSO.getResource(),
                        new Object[]{}, LocaleContextHolder.getLocale()), TIPOMSG.SUCESSO))
                .type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    public Response sucessoSalvar(String mensagem) {
        return Response.status(Response.Status.CREATED).entity(
                new MsgRetorno(msg.getMessage(MSG.CADASTRO.SUCESSO_SALVAR.getResource(),
                        new Object[]{mensagem}, LocaleContextHolder.getLocale()), TIPOMSG.SUCESSO))
                .type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    public Response sucessoSalvarOperadorFalhaEnvioEmail(String mensagem, Entidade e) {
        return Response.status(Response.Status.CREATED).entity(
                new EntidadeRetorno(
                        new MsgRetorno(msg.getMessage(MSG.OPERADOR.SUCESSO_SALVAR_SEM_ENVIO_EMAIL.getResource(),
                                new Object[]{mensagem}, LocaleContextHolder.getLocale()), TIPOMSG.SUCESSO), (Entidade) e))
                .type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    public Response sucessoSalvar(Entidade e) {
        return Response.status(Response.Status.CREATED).entity(
                new EntidadeRetorno(
                        new MsgRetorno(msg.getMessage(MSG.CADASTRO.SUCESSO_SALVAR.getResource(),
                                new Object[]{e.toString()}, LocaleContextHolder.getLocale()), TIPOMSG.SUCESSO), (Entidade) e))
                .type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    public Response sucesso(Object e) {
        return Response.status(Response.Status.OK).entity(
                new EntidadeRetorno(e)).type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    public Response sucessoSalvarColetivo(List<ColetivoResultado> e) {
        return Response.status(Response.Status.CREATED).entity(new EntidadeRetorno(e))
                .type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    public Response erroSalvar(Class<Entidade> obj) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(
                new MsgRetorno(msg.getMessage(MSG.CADASTRO.ERRO_SALVAR.getResource(),
                        new Object[]{obj.getSimpleName()}, LocaleContextHolder.getLocale()), TIPOMSG.ALERTA))
                .type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    public Response erroSalvar(Entidade e) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(
                new MsgRetorno(msg.getMessage(MSG.CADASTRO.ERRO_SALVAR.getResource(),
                        new Object[]{e.toString()}, LocaleContextHolder.getLocale()), TIPOMSG.ALERTA))
                .type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    public Response erroSalvar(String s) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(
                new MsgRetorno(msg.getMessage(MSG.CADASTRO.ERRO_SALVAR.getResource(),
                        new Object[]{s}, LocaleContextHolder.getLocale()), TIPOMSG.ALERTA))
                .type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    public Response erro(MSG.CADASTRO mensagem, Class<Entidade> obj) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(
                new MsgRetorno(msg.getMessage(mensagem.getResource(),
                        new Object[]{obj.getSimpleName()}, LocaleContextHolder.getLocale()), TIPOMSG.ALERTA))
                .type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    public Response erro(String mensagem, String mensagem1) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(
                new MsgRetorno(msg.getMessage(mensagem, new Object[]{}, LocaleContextHolder.getLocale()), TIPOMSG.ALERTA))
                .type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    public Response erro(String mensagem) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(
                new MsgRetorno(mensagem, TIPOMSG.ALERTA))
                .type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    public Response erroBuscar() {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(
                new MsgRetorno(msg.getMessage(MSG.CADASTRO.ERRO_BUSCAR.getResource(), new Object[]{}, LocaleContextHolder.getLocale()), TIPOMSG.ERRO))
                .type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    public Response erro(String mensagem, Class<Entidade> obj) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(
                new MsgRetorno(mensagem, TIPOMSG.ERRO))
                .type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    public Response sucessoExcluir(Class<Entidade> obj) {
        return Response.status(Response.Status.OK).entity(
                new MsgRetorno(msg.getMessage(MSG.CADASTRO.SUCESSO_EXCLUIR.getResource(),
                        new Object[]{obj.getSimpleName()}, LocaleContextHolder.getLocale()), TIPOMSG.SUCESSO))
                .type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    public Response sucessoExcluir(String mensagem) {
        return Response.status(Response.Status.OK).entity(
                new MsgRetorno(msg.getMessage(MSG.CADASTRO.SUCESSO_EXCLUIR.getResource(),
                        new Object[]{mensagem}, LocaleContextHolder.getLocale()), TIPOMSG.SUCESSO))
                .type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    public Response sucessoExcluir(String mensagem, String campo) {
        return Response.status(Response.Status.OK).entity(
                new MsgRetorno(msg.getMessage(MSG.CADASTRO.SUCESSO_EXCLUIR.getResource(),
                        new Object[]{mensagem}, LocaleContextHolder.getLocale()), TIPOMSG.SUCESSO))
                .type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    public Response erroExcluir(Class<Entidade> obj) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(
                new MsgRetorno(msg.getMessage(MSG.CADASTRO.ERRO_EXCLUIR.getResource(),
                        new Object[]{obj.getSimpleName()}, LocaleContextHolder.getLocale()), TIPOMSG.ALERTA))
                .type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    public Response erroExcluir(String mensagem) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(
                new MsgRetorno(msg.getMessage(MSG.CADASTRO.ERRO_EXCLUIR.getResource(),
                        new Object[]{mensagem}, LocaleContextHolder.getLocale()), TIPOMSG.ALERTA))
                .type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    public Response sucessoAtualizar(Class<Entidade> obj) {
        return Response.status(Response.Status.OK).entity(
                new MsgRetorno(msg.getMessage(MSG.CADASTRO.SUCESSO_ALTERAR.getResource(),
                        new Object[]{obj.getSimpleName()}, LocaleContextHolder.getLocale()), TIPOMSG.SUCESSO))
                .type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    public Response sucessoAtualizar(String s, Entidade e) {
        return Response.status(Response.Status.OK).entity(
                new EntidadeRetorno(
                        new MsgRetorno(msg.getMessage(MSG.CADASTRO.SUCESSO_ALTERAR.getResource(),
                                new Object[]{s}, LocaleContextHolder.getLocale()), TIPOMSG.SUCESSO), e))
                .type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    public Response sucessoAtualizar(Entidade e) {
        return Response.status(Response.Status.OK).entity(
                new EntidadeRetorno(
                        new MsgRetorno(msg.getMessage(MSG.CADASTRO.SUCESSO_ALTERAR.getResource(),
                                new Object[]{e.toString()}, LocaleContextHolder.getLocale()), TIPOMSG.SUCESSO), e))
                .type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    public Response erroAtualizar(Class<Entidade> obj) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(
                new MsgRetorno(msg.getMessage(MSG.CADASTRO.ERRO_ALTERAR.getResource(),
                        new Object[]{obj.toString()}, LocaleContextHolder.getLocale()), TIPOMSG.ALERTA))
                .type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    public Response erroAtualizar(Entidade e) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(
                new MsgRetorno(msg.getMessage(MSG.CADASTRO.ERRO_ALTERAR.getResource(),
                        new Object[]{e.toString()}, LocaleContextHolder.getLocale()), TIPOMSG.ALERTA))
                .type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    public Response erroAtualizar(String s) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(
                new MsgRetorno(msg.getMessage(MSG.CADASTRO.ERRO_ALTERAR.getResource(),
                        new Object[]{s}, LocaleContextHolder.getLocale()), TIPOMSG.ALERTA))
                .type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    public Response erroValidar(Class<Entidade> obj) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(
                new MsgRetorno(msg.getMessage(MSG.CADASTRO.ERRO_VALIDAR.getResource(),
                        new Object[]{obj.toString()}, LocaleContextHolder.getLocale()), TIPOMSG.ALERTA))
                .type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    public Response erroValidar(String mensagem) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(
                new MsgRetorno(msg.getMessage(MSG.CADASTRO.ERRO_VALIDAR.getResource(),
                        new Object[]{mensagem}, LocaleContextHolder.getLocale()), TIPOMSG.ALERTA))
                .type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    public Response alertaNaoCad(Class<Entidade> obj) {
        return Response.status(Response.Status.NO_CONTENT).entity(
                new MsgRetorno(msg.getMessage(MSG.CADASTRO.ALERTA_NAO_CADASTRADO.getResource(),
                        new Object[]{obj.getSimpleName()}, LocaleContextHolder.getLocale()), TIPOMSG.ALERTA))
                .type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    public Response alertaNaoCad(String mensagem) {
        return Response.status(Response.Status.NO_CONTENT).entity(
                new MsgRetorno(msg.getMessage(MSG.CADASTRO.ALERTA_NAO_CADASTRADO.getResource(),
                        new Object[]{mensagem}, LocaleContextHolder.getLocale()), TIPOMSG.ALERTA))
                .type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    public Response alertaNaoCad() {
        return Response.status(Response.Status.NO_CONTENT).entity(
                new MsgRetorno(msg.getMessage(MSG.CADASTRO.ALERTA_NAO_CADASTRADO2.getResource(),
                        new Object[]{}, LocaleContextHolder.getLocale()), TIPOMSG.ALERTA))
                .type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    public Response alertaJaCad(Class<Entidade> obj) {
        return Response.status(Response.Status.CONFLICT).entity(
                new MsgRetorno(msg.getMessage(MSG.CADASTRO.ALERTA_JA_CADASTRADO.getResource(),
                        new Object[]{obj.getSimpleName()}, LocaleContextHolder.getLocale()), TIPOMSG.ALERTA))
                .type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    public Response alertaJaCad(String s) {
        return Response.status(Response.Status.CONFLICT).entity(
                new MsgRetorno(msg.getMessage(MSG.CADASTRO.ALERTA_JA_CADASTRADO.getResource(),
                        new Object[]{s}, LocaleContextHolder.getLocale()), TIPOMSG.ALERTA))
                .type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    public Response alertaConfict(String s) {
        return Response.status(Response.Status.CONFLICT).entity(
                new MsgRetorno(msg.getMessage(s,
                        new Object[]{}, LocaleContextHolder.getLocale()), TIPOMSG.ALERTA))
                .type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    public Response campoObrigatorio(String campo) {
        return Response.status(Response.Status.PRECONDITION_FAILED).entity(
                new MsgRetorno(msg.getMessage(MSG.CAMPO_OBRIGATORIO,
                        new Object[]{campo}, LocaleContextHolder.getLocale()), TIPOMSG.ALERTA))
                .type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    public Response alertaValidacao(String mensagem) {
        return Response.status(Response.Status.PRECONDITION_FAILED).entity(
                new MsgRetorno(msg.getMessage(mensagem, new Object[]{}, LocaleContextHolder.getLocale()), TIPOMSG.ALERTA))
                .type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    public Response alertaValidacao(String mensagem, String param) {
        return Response.status(Response.Status.PRECONDITION_FAILED).entity(
                new MsgRetorno(msg.getMessage(mensagem, new Object[]{param != null ? param : ""}, LocaleContextHolder.getLocale()), TIPOMSG.ALERTA))
                .type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    public Response alertaValidacaoComGenero(String mensagem, String param) {
        String resourceParam = msg.getMessage(param, new Object[]{}, LocaleContextHolder.getLocale());
        return Response.status(Response.Status.PRECONDITION_FAILED).entity(
                new MsgRetorno(msg.getMessage(mensagem, new Object[]{resourceParam != null ? resourceParam : ""}, LocaleContextHolder.getLocale()), TIPOMSG.ALERTA))
                .type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    public Response alertaSucesso(String mensagem) {
        return Response.status(Response.Status.OK).entity(
                new MsgRetorno(msg.getMessage(mensagem, new Object[]{}, LocaleContextHolder.getLocale()), TIPOMSG.SUCESSO))
                .type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    public Response alerta(String mensagem, String campo) {
        return Response.status(Response.Status.PRECONDITION_FAILED).entity(
                new MsgRetorno(msg.getMessage(mensagem, new Object[]{campo != null ? campo : ""}, LocaleContextHolder.getLocale()), TIPOMSG.ALERTA))
                .type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    public Response alertaNaoImplementado() {
        return Response.status(501).entity(
                new MsgRetorno("Método não implementado", TIPOMSG.ALERTA))
                .type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    public Response alertaTrocaSenha(String mensagem, Entidade e) {
        return Response.status(Response.Status.SEE_OTHER).entity(
                new EntidadeRetorno(
                        new MsgRetorno(msg.getMessage(mensagem, new Object[]{}, LocaleContextHolder.getLocale()), TIPOMSG.ALERTA), e))
                .type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    public Response sucessoValidar(String mensagem) {
        return Response.status(Response.Status.ACCEPTED).entity(
                new MsgRetorno(mensagem, TIPOMSG.ALERTA))
                .type(MediaType.APPLICATION_JSON_TYPE).build();
    }

}
