package com.topdata.toppontoweb.services.funcionario.bancohoras.fechamento;

/**
 *
 * @author juliano.ezequiel
 */
public class OpcoesFechamento {

    private String descricao;
    private Integer idOpcao;

    public OpcoesFechamento() {
    }

    public OpcoesFechamento(String descricao, Integer idOpcao) {
        this.descricao = descricao;
        this.idOpcao = idOpcao;
    }

    public String getDescricao() {
        return descricao;
    }

    public Integer getIdOpcao() {
        return idOpcao;
    }

}
