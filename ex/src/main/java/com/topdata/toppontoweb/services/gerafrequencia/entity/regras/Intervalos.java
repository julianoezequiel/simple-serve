package com.topdata.toppontoweb.services.gerafrequencia.entity.regras;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Dados relatórios de absenteísmo, intrajornada e interjornada
 * @author enio.junior
 */
public class Intervalos {
    
    private List<Duration> saldoHorasIntraJornadaList; 
    private Duration saldoHorasInterJornada;
    private Duration saldoHorasDeveriaTrabalharAbsenteismo;
    private Duration saldoHorasNaoTrabalhadasAbsenteismo;
    private Double indiceAbsenteismo;

    public Intervalos() {
        this.saldoHorasIntraJornadaList = new ArrayList<>();
        this.saldoHorasInterJornada = Duration.ZERO;
        this.saldoHorasDeveriaTrabalharAbsenteismo = Duration.ZERO;
        this.saldoHorasNaoTrabalhadasAbsenteismo = Duration.ZERO;
        this.indiceAbsenteismo = 0d;
    }

    public List<Duration> getSaldoHorasIntraJornadaList() {
        return saldoHorasIntraJornadaList;
    }

    public void setSaldoHorasIntraJornadaList(List<Duration> saldoHorasIntraJornadaList) {
        this.saldoHorasIntraJornadaList = saldoHorasIntraJornadaList;
    }

    public Duration getSaldoHorasInterJornada() {
        return saldoHorasInterJornada;
    }

    public void setSaldoHorasInterJornada(Duration saldoHorasInterJornada) {
        this.saldoHorasInterJornada = saldoHorasInterJornada;
    }

    public Double getIndiceAbsenteismo() {
        return indiceAbsenteismo;
    }

    public void setIndiceAbsenteismo(Double indiceAbsenteismo) {
        this.indiceAbsenteismo = indiceAbsenteismo;
    }

    public Duration getSaldoHorasDeveriaTrabalharAbsenteismo() {
        return saldoHorasDeveriaTrabalharAbsenteismo;
    }

    public void setSaldoHorasDeveriaTrabalharAbsenteismo(Duration saldoHorasDeveriaTrabalharAbsenteismo) {
        this.saldoHorasDeveriaTrabalharAbsenteismo = saldoHorasDeveriaTrabalharAbsenteismo;
    }

    public Duration getSaldoHorasNaoTrabalhadasAbsenteismo() {
        return saldoHorasNaoTrabalhadasAbsenteismo;
    }

    public void setSaldoHorasNaoTrabalhadasAbsenteismo(Duration saldoHorasNaoTrabalhadasAbsenteismo) {
        this.saldoHorasNaoTrabalhadasAbsenteismo = saldoHorasNaoTrabalhadasAbsenteismo;
    }
        
    /**
     * Retorna um intervalo zerado
     * @return 
     */
    public static Intervalos ZERO(){
        return new Intervalos();
    }
        
    /**
     * Soma dois intervalos
     * @param intervalo
     */
    public void plus(Intervalos intervalo){
        this.saldoHorasInterJornada = this.saldoHorasInterJornada.plus(intervalo.saldoHorasInterJornada);
        this.saldoHorasDeveriaTrabalharAbsenteismo = this.saldoHorasDeveriaTrabalharAbsenteismo.plus(intervalo.saldoHorasDeveriaTrabalharAbsenteismo);
        this.saldoHorasNaoTrabalhadasAbsenteismo = this.saldoHorasNaoTrabalhadasAbsenteismo.plus(intervalo.saldoHorasNaoTrabalhadasAbsenteismo);
        this.indiceAbsenteismo += intervalo.indiceAbsenteismo;
    }
        
}
