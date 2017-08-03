package com.topdata.toppontoweb.dto;

import java.util.List;

import com.topdata.toppontoweb.entity.jornada.Jornada;

/**
 *
 * @author juliano.ezequiel
 */
public class JornadaTransfer {

    private Jornada jornada;

    private List<JornadaHorarioTransfer> jorndaHorarioTransferList;

    private Boolean possuiVinculos;

    public JornadaTransfer() {
    }

    public JornadaTransfer(Jornada jornada, List<JornadaHorarioTransfer> jorndaHorarioTransferList, Boolean possuiVinculos) {
        this.jornada = jornada;
        this.jorndaHorarioTransferList = jorndaHorarioTransferList;
        this.possuiVinculos = possuiVinculos;
    }

    public Boolean getPossuiVinculos() {
        return possuiVinculos;
    }

    public void setPossuiVinculos(Boolean possuiVinculos) {
        this.possuiVinculos = possuiVinculos;
    }

    public Jornada getJornada() {
        return jornada;
    }

    public void setJornada(Jornada jornada) {
        this.jornada = jornada;
    }

    public void setJorndaHorarioTransferList(List<JornadaHorarioTransfer> jorndaHorarioTransferList) {
        this.jorndaHorarioTransferList = jorndaHorarioTransferList;
    }

    public List<JornadaHorarioTransfer> getJorndaHorarioTransferList() {
        return jorndaHorarioTransferList;
    }

}
