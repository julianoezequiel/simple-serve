package com.topdata.toppontoweb.dto;

import com.topdata.toppontoweb.utils.constantes.CONSTANTES;
import java.io.Serializable;

/**
 *
 * @author tharle.camargo
 */
public class FormatoTransfer implements Serializable {

    private String idFormato;
    private String descricao;
    private String tipo;
    private String prefixo;

    public FormatoTransfer() {
        this.idFormato = "";
        this.descricao = "";
        this.tipo = "";
        this.prefixo = "";
    }

    public FormatoTransfer(CONSTANTES.Enum_FORMATO eFormato) {
        this.idFormato = eFormato.getIdFormato();
        this.descricao = eFormato.getDescricao();
        this.tipo = eFormato.getTipo();
        this.prefixo = eFormato.getSufixo();
    }

    public FormatoTransfer(String idFormato) {
        this.idFormato = idFormato;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getIdFormato() {
        return idFormato;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setIdFormato(String idFormato) {
        this.idFormato = idFormato;
    }

    public String getPrefixo() {
        return prefixo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setPrefixo(String prefixo) {
        this.prefixo = prefixo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

}
