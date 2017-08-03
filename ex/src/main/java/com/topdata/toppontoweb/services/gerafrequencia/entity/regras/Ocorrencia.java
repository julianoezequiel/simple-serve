package com.topdata.toppontoweb.services.gerafrequencia.entity.regras;

/**
 * Ocorrências do dia de acordo com as marcações coletadas
 *
 * @author enio.junior
 */
public class Ocorrencia {

    private boolean entradaAntecipada;
    private boolean entradaAtrasada;
    private boolean saidaAntecipada;
    private boolean saidaAposHorario;

    public Ocorrencia() {
        this.entradaAntecipada = Boolean.FALSE;
        this.entradaAtrasada = Boolean.FALSE;
        this.saidaAntecipada = Boolean.FALSE;
        this.saidaAposHorario = Boolean.FALSE;
    }

    public boolean isEntradaAntecipada() {
        return entradaAntecipada;
    }

    public void setEntradaAntecipada(boolean entradaAntecipada) {
        this.entradaAntecipada = entradaAntecipada;
    }

    public boolean isEntradaAtrasada() {
        return entradaAtrasada;
    }

    public void setEntradaAtrasada(boolean entradaAtrasada) {
        this.entradaAtrasada = entradaAtrasada;
    }

    public boolean isSaidaAntecipada() {
        return saidaAntecipada;
    }

    public void setSaidaAntecipada(boolean saidaAntecipada) {
        this.saidaAntecipada = saidaAntecipada;
    }

    public boolean isSaidaAposHorario() {
        return saidaAposHorario;
    }

    public void setSaidaAposHorario(boolean saidaAposHorario) {
        this.saidaAposHorario = saidaAposHorario;
    }

}
