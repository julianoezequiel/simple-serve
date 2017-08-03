package com.topdata.toppontoweb.services.gerafrequencia.entity.bancodehoras;

import com.topdata.toppontoweb.entity.tipo.TipoDia;
import java.time.Duration;

/**
 * Tabela de crédito saldo único por tipo de dia relatório de frequência
 *
 * @author enio.junior
 */
public class TabelaCreditoSaldoUnico {

    private TipoDia tipoDia;
    private Duration saldo;

    public TabelaCreditoSaldoUnico() {
        this.saldo = Duration.ZERO;
    }

    public TabelaCreditoSaldoUnico(TipoDia tipoDia) {
        this.saldo = Duration.ZERO;
        this.tipoDia = tipoDia;
    }

    public TipoDia getTipoDia() {
        return tipoDia;
    }

    public void setTipoDia(TipoDia tipoDia) {
        this.tipoDia = tipoDia;
    }

    public Duration getSaldo() {
        return saldo;
    }

    public void setSaldo(Duration saldo) {
        this.saldo = saldo;
    }

    public void plus(TabelaCreditoSaldoUnico tabela) {
        this.saldo = this.saldo.plus(tabela.saldo);
    }

}
