package com.topdata.toppontoweb.dto;

import com.topdata.toppontoweb.entity.jornada.JornadaHorario;
import java.util.List;

/**
 *
 * @author juliano.ezequiel
 */
public class JornadaHorarioTransfer {

    private List<HorarioTransfer> horarioTransferList;
    private JornadaHorario jornadaHorario;

    public JornadaHorarioTransfer() {
    }

    public JornadaHorarioTransfer(List<HorarioTransfer> horarioTransferList, JornadaHorario jornadaHorario) {
        this.horarioTransferList = horarioTransferList;
        this.jornadaHorario = jornadaHorario;
    }

    public List<HorarioTransfer> getHorarioTransferList() {
        return horarioTransferList;
    }

    public void setHorarioTransferList(List<HorarioTransfer> horarioTransferList) {
        this.horarioTransferList = horarioTransferList;
    }

    public void setJornadaHorario(JornadaHorario jornadaHorario) {
        this.jornadaHorario = jornadaHorario;
    }

    public JornadaHorario getJornadaHorario() {
        return jornadaHorario;
    }

}
