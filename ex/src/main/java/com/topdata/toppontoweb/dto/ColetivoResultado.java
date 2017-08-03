package com.topdata.toppontoweb.dto;

import java.io.Serializable;

import com.topdata.toppontoweb.entity.Entidade;

/**
 * @version 1.0.0.0 data 10/01/2017
 * @since 1.0.0.0 data 10/01/2017
 *
 * @author juliano.ezequiel
 */
public class ColetivoResultado implements Serializable {

    public enum Enum_ResultadoColetivo {

        LANCADO("Lançado"),
        EXCLUIDO("Excluido"),
        ERRO("Erro");

        private Enum_ResultadoColetivo(String s) {
        }

    }

    private MsgRetorno msgRetorno;
    private Enum_ResultadoColetivo resultado;
    private Entidade entidade;
    private boolean fechamento = false;

    public ColetivoResultado(Entidade entidade, MsgRetorno msgRetorno, Enum_ResultadoColetivo resultado) {
        this.msgRetorno = msgRetorno;
        this.resultado = resultado;
        this.entidade = entidade;
    }

    public ColetivoResultado(Entidade entidade, MsgRetorno msgRetorno, Enum_ResultadoColetivo resultado, boolean fechamento) {
        this.msgRetorno = msgRetorno;
        this.resultado = resultado;
        this.entidade = entidade;
        this.fechamento = fechamento;
    }

    public MsgRetorno getMsgRetorno() {
        return msgRetorno;
    }

    public void setMsgRetorno(MsgRetorno msgRetorno) {
        this.msgRetorno = msgRetorno;
    }

    public Enum_ResultadoColetivo getResultado() {
        return resultado;
    }

    public void setResultado(Enum_ResultadoColetivo resultado) {
        this.resultado = resultado;
    }

    public Entidade getEntidade() {
        return entidade;
    }

    public void setEntidade(Entidade entidade) {
        this.entidade = entidade;
    }

    public boolean isFechamento() {
        return fechamento;
    }

    public void setFechamento(boolean fechamento) {
        this.fechamento = fechamento;
    }

}
