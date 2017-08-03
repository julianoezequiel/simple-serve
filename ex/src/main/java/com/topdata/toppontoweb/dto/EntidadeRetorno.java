package com.topdata.toppontoweb.dto;

import java.util.List;

import com.topdata.toppontoweb.entity.Entidade;
import java.io.Serializable;

/**
 * @version 1.0.5 data 02/08/2016
 * @param <Entidade>
 * @since 1.0.5 data 02/08/2016
 *
 * @author juliano.ezequiel
 */
public class EntidadeRetorno<Entidade> implements Serializable {

    private MsgRetorno msgRetorno;
    private Entidade entidade;
    private List<ColetivoResultado> coletivoResultados;

    public EntidadeRetorno(Entidade entidade) {
        this.entidade = entidade;
    }

    public EntidadeRetorno(List<ColetivoResultado> coletivoResultados) {
        this.coletivoResultados = coletivoResultados;
    }

    public EntidadeRetorno(MsgRetorno msgRetorno, Entidade entidade) {
        this.msgRetorno = msgRetorno;
        this.entidade = entidade;
    }

    public Entidade getEntidade() {
        return entidade;
    }

    public MsgRetorno getMsgRetorno() {
        return msgRetorno;
    }

    public List<ColetivoResultado> getColetivoResultados() {
        return coletivoResultados;
    }

    public void setColetivoResultados(List<ColetivoResultado> coletivoResultados) {
        this.coletivoResultados = coletivoResultados;
    }

    public void setEntidade(Entidade entidade) {
        this.entidade = entidade;
    }

    public void setMsgRetorno(MsgRetorno msgRetorno) {
        this.msgRetorno = msgRetorno;
    }

}
