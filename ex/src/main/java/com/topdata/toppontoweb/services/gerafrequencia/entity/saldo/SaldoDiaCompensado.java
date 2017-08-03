package com.topdata.toppontoweb.services.gerafrequencia.entity.saldo;

import java.time.Duration;
import java.util.Date;

/**
 * Informações de saldo de horas compensadas (diurnas e noturnas)
 *
 * @author enio.junior
 */
public class SaldoDiaCompensado extends Saldo {

    private Date dataCompensada;
    private Date dataInicio;
    private Date dataFim;
    private Date limiteDiario;
    private boolean consideraDiasSemJornada;
    private String status;
    private Duration compensadasDiurnas;
    private Duration compensadasNoturnas;
    private Duration aCompensarDiurnas;
    private Duration aCompensarNoturnas;

    public SaldoDiaCompensado(Date dataCompensada, Date dataInicio, Date dataFim, Date limiteDiario, boolean consideraDiasSemJornada) {
        this.dataCompensada = dataCompensada;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.limiteDiario = limiteDiario;
        this.consideraDiasSemJornada = consideraDiasSemJornada;
    }

    public SaldoDiaCompensado(boolean possui, Duration diurnas, Duration noturnas, Duration diferencaAdicionalNoturno) {
        super(possui, diurnas, noturnas, diferencaAdicionalNoturno);
    }

    public SaldoDiaCompensado(Duration compensadasDiurnas, Duration compensadasNoturnas) {
        this.compensadasDiurnas = compensadasDiurnas;
        this.compensadasNoturnas = compensadasNoturnas;
    }

    public Date getDataCompensada() {
        return dataCompensada;
    }

    public void setDataCompensada(Date dataCompensada) {
        this.dataCompensada = dataCompensada;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getDataFim() {
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    public Date getLimiteDiario() {
        return limiteDiario;
    }

    public void setLimiteDiario(Date limiteDiario) {
        this.limiteDiario = limiteDiario;
    }

    public boolean isConsideraDiasSemJornada() {
        return consideraDiasSemJornada;
    }

    public void setConsideraDiasSemJornada(boolean consideraDiasSemJornada) {
        this.consideraDiasSemJornada = consideraDiasSemJornada;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Duration getCompensadasDiurnas() {
        return compensadasDiurnas;
    }

    public void setCompensadasDiurnas(Duration compensadasDiurnas) {
        this.compensadasDiurnas = compensadasDiurnas;
    }

    public Duration getCompensadasNoturnas() {
        return compensadasNoturnas;
    }

    public void setCompensadasNoturnas(Duration compensadasNoturnas) {
        this.compensadasNoturnas = compensadasNoturnas;
    }

    public Duration getACompensarDiurnas() {
        return aCompensarDiurnas;
    }

    public void setACompensarDiurnas(Duration aCompensarDiurnas) {
        this.aCompensarDiurnas = aCompensarDiurnas;
    }

    public Duration getACompensarNoturnas() {
        return aCompensarNoturnas;
    }

    public void setACompensarNoturnas(Duration aCompensarNoturnas) {
        this.aCompensarNoturnas = aCompensarNoturnas;
    }

}
