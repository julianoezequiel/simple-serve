package com.topdata.toppontoweb.services.gerafrequencia.entity.bancodehoras;

import java.time.Duration;
import java.util.Date;

/**
 * Controle de saldos do banco de horas
 *
 * @author enio.junior
 */
public class BancodeHorasSaldoDia {

    private Duration saldoPrimeiroDiaBancodeHoras;
    private Date dataPrimeiroDiaBancodeHoras;
    private Duration saldoAnteriorDiaPeriodoBancodeHoras;

    public BancodeHorasSaldoDia() {
        this.saldoPrimeiroDiaBancodeHoras = Duration.ZERO;
        this.saldoAnteriorDiaPeriodoBancodeHoras = Duration.ZERO;
    }

    public Duration getSaldoPrimeiroDiaBancodeHoras() {
        return saldoPrimeiroDiaBancodeHoras;
    }

    public void setSaldoPrimeiroDiaBancodeHoras(Duration saldoPrimeiroDiaBancodeHoras) {
        this.saldoPrimeiroDiaBancodeHoras = saldoPrimeiroDiaBancodeHoras;
    }

    public Date getDataPrimeiroDiaBancodeHoras() {
        return dataPrimeiroDiaBancodeHoras;
    }

    public void setDataPrimeiroDiaBancodeHoras(Date dataPrimeiroDiaBancodeHoras) {
        this.dataPrimeiroDiaBancodeHoras = dataPrimeiroDiaBancodeHoras;
    }

    public Duration getSaldoAnteriorDiaPeriodoBancodeHoras() {
        return saldoAnteriorDiaPeriodoBancodeHoras;
    }

    public void setSaldoAnteriorDiaPeriodoBancodeHoras(Duration saldoAnteriorDiaPeriodoBancodeHoras) {
        this.saldoAnteriorDiaPeriodoBancodeHoras = saldoAnteriorDiaPeriodoBancodeHoras;
    }

}
