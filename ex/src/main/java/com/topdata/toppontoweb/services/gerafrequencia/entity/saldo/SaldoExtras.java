package com.topdata.toppontoweb.services.gerafrequencia.entity.saldo;

import java.time.Duration;

/**
 * Informações de saldo de horas de extras (diurnas e noturnas)
 *
 * @author enio.junior
 */
public class SaldoExtras extends Saldo {

    private boolean todas;

    public SaldoExtras() {
        this.todas = Boolean.FALSE;
    }

    public SaldoExtras(boolean possui, Duration diurnas, Duration noturnas, Duration diferencaAdicionalNoturno) {
        super(possui, diurnas, noturnas, diferencaAdicionalNoturno);
    }

    public SaldoExtras(boolean todas) {
        this.todas = todas;
    }

    public boolean isTodas() {
        return todas;
    }

    public void setTodas(boolean todas) {
        this.todas = todas;
    }

}
