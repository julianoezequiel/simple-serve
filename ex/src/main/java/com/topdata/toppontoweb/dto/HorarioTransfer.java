package com.topdata.toppontoweb.dto;

import com.topdata.toppontoweb.entity.jornada.Horario;
import com.topdata.toppontoweb.entity.jornada.HorarioMarcacao;
import com.topdata.toppontoweb.entity.jornada.Jornada;
import java.util.List;

/**
 *
 * @author juliano.ezequiel
 */
public class HorarioTransfer {

    public HorarioTransfer() {
    }

    public HorarioTransfer(List<HorarioMarcacao> horarioMarcacaoList, Horario horario, Jornada jornada) {
        this.horarioMarcacaoList = horarioMarcacaoList;
        this.horario = horario;
        this.jornada = jornada;
    }

    private List<HorarioMarcacao> horarioMarcacaoList;

    private Horario horario;

    private Jornada jornada;

    public Horario getHorario() {
        return horario;
    }

    public List<HorarioMarcacao> getHorarioMarcacaoList() {
        return horarioMarcacaoList;
    }

    public void setHorario(Horario horario) {
        this.horario = horario;
    }

    public void setHorarioMarcacaoList(List<HorarioMarcacao> horarioMarcacaoList) {
        this.horarioMarcacaoList = horarioMarcacaoList;
    }

    public Jornada getJornada() {
        return jornada;
    }

    public void setJornada(Jornada jornada) {
        this.jornada = jornada;
    }

}
