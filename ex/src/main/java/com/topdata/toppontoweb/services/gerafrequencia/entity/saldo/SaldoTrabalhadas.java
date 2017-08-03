package com.topdata.toppontoweb.services.gerafrequencia.entity.saldo;

import java.time.Duration;

/**
 * Informações de saldo de horas trabalhadas (diurnas e noturnas)
 *
 * @author enio.junior
 */
public class SaldoTrabalhadas extends Saldo {

    private Saldo extrasNaoCompensaAtrasos;
    private Saldo ausenciasNaoCompensaAtrasos;

    public SaldoTrabalhadas() {
    }

    public SaldoTrabalhadas(boolean possui, Duration diurnas, Duration noturnas, Duration diferencaAdicionalNoturno) {
        super(possui, diurnas, noturnas, diferencaAdicionalNoturno);
    }

    public SaldoTrabalhadas(Saldo extrasNaoCompensaAtrasos, Saldo ausenciasNaoCompensaAtrasos) {
        this.extrasNaoCompensaAtrasos = extrasNaoCompensaAtrasos;
        this.ausenciasNaoCompensaAtrasos = ausenciasNaoCompensaAtrasos;
    }

    public Saldo getExtras() {
        return extrasNaoCompensaAtrasos;
    }

    public void setExtras(Saldo extrasNaoCompensaAtrasos) {
        this.extrasNaoCompensaAtrasos = extrasNaoCompensaAtrasos;
    }

    public Saldo getAusencias() {
        return ausenciasNaoCompensaAtrasos;
    }

    public void setAusencias(Saldo ausenciasNaoCompensaAtrasos) {
        this.ausenciasNaoCompensaAtrasos = ausenciasNaoCompensaAtrasos;
    }

}
