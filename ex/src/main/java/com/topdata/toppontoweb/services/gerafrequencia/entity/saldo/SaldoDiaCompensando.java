package com.topdata.toppontoweb.services.gerafrequencia.entity.saldo;

import java.time.Duration;
import java.util.Date;

/**
 * Informações de saldo de horas compensando (diurnas e noturnas)
 *
 * @author enio.junior
 */
public class SaldoDiaCompensando {

    private Date dataCompensando;
    private Duration compensandoDiurnas;
    private Duration compensandoNoturnas;

    public SaldoDiaCompensando() {
        this.compensandoDiurnas = Duration.ZERO;
        this.compensandoNoturnas = Duration.ZERO;
    }

    public Date getDataCompensando() {
        return dataCompensando;
    }

    public void setDataCompensando(Date dataCompensando) {
        this.dataCompensando = dataCompensando;
    }

    public Duration getCompensandoDiurnas() {
        return compensandoDiurnas;
    }

    public void setCompensandoDiurnas(Duration compensandoDiurnas) {
        this.compensandoDiurnas = compensandoDiurnas;
    }

    public Duration getCompensandoNoturnas() {
        return compensandoNoturnas;
    }

    public void setCompensandoNoturnas(Duration compensandoNoturnas) {
        this.compensandoNoturnas = compensandoNoturnas;
    }

}
