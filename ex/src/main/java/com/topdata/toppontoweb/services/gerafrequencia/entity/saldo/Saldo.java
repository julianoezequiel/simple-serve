package com.topdata.toppontoweb.services.gerafrequencia.entity.saldo;

import java.time.Duration;

/**
 * Informações de saldo de horas (diurnas e noturnas)
 *
 * @author enio.junior
 */
public class Saldo {

    private boolean possui = false;
    private Duration diurnas;
    private Duration noturnas;
    private Duration diferencaAdicionalNoturno;

    public Saldo() {
        possui = false;
        diurnas = Duration.ZERO;
        noturnas = Duration.ZERO;
        diferencaAdicionalNoturno = Duration.ZERO;
    }

    public Saldo(boolean possui, Duration diurnas, Duration noturnas, Duration diferencaAdicionalNoturno) {
        this.possui = possui;
        this.diurnas = diurnas;
        this.noturnas = noturnas;
        this.diferencaAdicionalNoturno = diferencaAdicionalNoturno;
    }

    public boolean isPossui() {
        return possui;
    }

    public void setPossui(boolean possui) {
        this.possui = possui;
    }

    public Duration getDiurnas() {
        return diurnas;
    }

    public void setDiurnas(Duration diurnas) {
        this.diurnas = diurnas;
    }

    public Duration getNoturnas() {
        return noturnas;
    }

    public void setNoturnas(Duration noturnas) {
        this.noturnas = noturnas;
    }

    public Duration getDiferencaadicionalnoturno() {
        return diferencaAdicionalNoturno;
    }

    public void setDiferencaadicionalnoturno(Duration diferencaAdicionalNoturno) {
        this.diferencaAdicionalNoturno = diferencaAdicionalNoturno;
    }

    public void plus(Saldo saldo) {
        if (saldo != null) {
            this.diurnas = this.diurnas.plus(saldo.diurnas);
            this.noturnas = this.noturnas.plus(saldo.noturnas);
            this.diferencaAdicionalNoturno = this.diferencaAdicionalNoturno.plus(saldo.diferencaAdicionalNoturno);
        }
    }

}
