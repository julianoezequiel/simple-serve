package com.topdata.toppontoweb.dto;

import com.topdata.toppontoweb.utils.constantes.MSG.TIPOMSG;

/**
 *
 * @author juliano.ezequiel
 */
public class MsgRetorno {

    private String type;
    private String title = "Título";
    private String body;

    public MsgRetorno() {
    }

    public MsgRetorno(String body, TIPOMSG tipomsg) {
        this.type = tipomsg.getDescricao();
        this.body = body;
        this.title = tipomsg.getTitulo();
    }

    public String getBody() {
        return body;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setType(String type) {
        this.type = type;
    }
}
