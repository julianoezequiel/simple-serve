/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.topdata.toppontoweb.dto;

import com.topdata.toppontoweb.entity.autenticacao.Grupo;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe de retorno de resposta de exclusão dos grupos
 * @author tharle.camargo
 */
public class GrupoExcluirResultado {
    
    private List<Grupo> gruposValidos;
    private List<Grupo> gruposInvalidos;

    public GrupoExcluirResultado() {
        this.gruposInvalidos = new ArrayList<>();
        this.gruposValidos = new ArrayList<>();
    }

    public GrupoExcluirResultado(List<Grupo> gruposValidos, List<Grupo> gruposInvalidos) {
        this.gruposValidos = gruposValidos;
        this.gruposInvalidos = gruposInvalidos;
    }

    public List<Grupo> getGruposInvalidos() {
        return gruposInvalidos;
    }

    public void setGruposInvalidos(List<Grupo> gruposInvalidos) {
        this.gruposInvalidos = gruposInvalidos;
    }

    public List<Grupo> getGruposValidos() {
        return gruposValidos;
    }

    public void setGruposValidos(List<Grupo> gruposValidos) {
        this.gruposValidos = gruposValidos;
    }
    
    
    
    
}
