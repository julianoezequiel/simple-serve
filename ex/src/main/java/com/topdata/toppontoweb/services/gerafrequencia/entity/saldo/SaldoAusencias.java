package com.topdata.toppontoweb.services.gerafrequencia.entity.saldo;

import java.time.Duration;

/**
 * Informações de saldo de horas de ausência (diurnas e noturnas)
 *
 * @author enio.junior
 */
public class SaldoAusencias extends Saldo {

    private Saldo abonoDia;

    public SaldoAusencias() {
        this.abonoDia = new Saldo();
    }

    public SaldoAusencias(boolean possui, Duration diurnas, Duration noturnas, Duration diferencaAdicionalNoturno) {
        super(possui, diurnas, noturnas, diferencaAdicionalNoturno);
    }

    public SaldoAusencias(Saldo abonoDia) {
        this.abonoDia = abonoDia;
    }

    public Saldo getAbonoDia() {
        return abonoDia;
    }

    public void setAbonoDia(Saldo abonoDia) {
        this.abonoDia = abonoDia;
    }

}
