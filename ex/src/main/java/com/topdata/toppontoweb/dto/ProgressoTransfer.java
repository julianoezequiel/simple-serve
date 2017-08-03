/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.topdata.toppontoweb.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * Classe de transição para o "progresso" da operação
 * @author tharle.camargo
 */
public class ProgressoTransfer {
    private final long id;
    private Integer progresso;
    private Collection<ColetivoResultado> coletivoResultados;
    private Date dataAtualizacao;

    public ProgressoTransfer() {
        this.progresso = 0;
        this.id = Math.round(Math.random() * System.currentTimeMillis()/1000);
        this.coletivoResultados = new ArrayList<>();
        this.atualizarDataAtualizacao();
    }

    /**
     * @return the progresso
     */
    public Integer getProgresso() {
        return progresso;
    }

    /**
     * @param progresso the progresso to set
     */
    public void setProgresso(Integer progresso) {
        this.progresso = progresso;
    }

    public void addProgresso(int i) {
        this.progresso += i;
        this.atualizarDataAtualizacao();
    }

    public long getId() {
        return id;
    }

    public void setColetivoResultados (Collection<ColetivoResultado> resultadosColetivo) {
        this.coletivoResultados.addAll(resultadosColetivo);
    }
    
    public Collection<ColetivoResultado> getColetivoResultados() {
        return this.coletivoResultados;
    }
    
    public void atualizarDataAtualizacao(){
        this.dataAtualizacao = new Date();
    }

    public Date getDataAtualizacao() {
        return this.dataAtualizacao;
    }
}
