package com.topdata.toppontoweb.services.jornada;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @version 1.0.0.0 data 18/01/2017
 * @since 1.0.0.0 data 15/09/2016
 *
 * @author juliano.ezequiel
 */
public class HorarioJornadaSequencia {

    private Integer idHorario;
    private Integer idHoraMarcacao;
    private Date entrada;
    private Date saida;
    private StringBuffer descricao = new StringBuffer();
    private Boolean jornadaVariavel;

    private List<String[]> marcacoes = new ArrayList<>();

    public HorarioJornadaSequencia() {
    }

    public HorarioJornadaSequencia(Integer idHorario, Integer idHoraMarcacao, Date entrada, Date saida, Boolean jornadaVariavel) {
        this.idHorario = idHorario;
        this.idHoraMarcacao = idHoraMarcacao;
        this.entrada = entrada;
        this.saida = saida;
        this.jornadaVariavel = jornadaVariavel;
    }

    public List<String[]> getMarcacoes() {
        return marcacoes;
    }

    public void setMarcacoes(List<String[]> marcacoes) {
        this.marcacoes = marcacoes;
    }

    public void setJornadaVariavel(Boolean jornadaVariavel) {
        this.jornadaVariavel = jornadaVariavel;
    }

    public Boolean getJornadaVariavel() {
        return jornadaVariavel;
    }

    public StringBuffer getDescricao() {
        return descricao;
    }

    public void setDescricao(StringBuffer descricao) {
        this.descricao = descricao;
    }

    public Integer getIdHorario() {
        return idHorario;
    }

    public void setIdHorario(Integer idHorario) {
        this.idHorario = idHorario;
    }

    public Integer getIdHoraMarcacao() {
        return idHoraMarcacao;
    }

    public void setIdHoraMarcacao(Integer idHoraMarcacao) {
        this.idHoraMarcacao = idHoraMarcacao;
    }

    public Date getEntrada() {
        return entrada;
    }

    public void setEntrada(Date entrada) {
        this.entrada = entrada;
    }

    public Date getSaida() {
        return saida;
    }

    public void setSaida(Date saida) {
        this.saida = saida;
    }

}
