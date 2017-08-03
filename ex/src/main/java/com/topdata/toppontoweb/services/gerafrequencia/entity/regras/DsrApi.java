package com.topdata.toppontoweb.services.gerafrequencia.entity.regras;

import com.topdata.toppontoweb.services.gerafrequencia.entity.tabela.TabelaSequenciaPercentuais;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Informações para o cálculo do DSR
 *
 * @author enio.junior
 */
public class DsrApi {

    private List<TabelaSequenciaPercentuais> tabelaExtrasDSRList;
    private int diasUteis;
    private int domingos;
    private int feriados;
    private Date dataInicio;
    private Date dataFim;
    private Date ultimoDomingoMes;
    private Duration saldoMesSeguinte;
    private Duration extrasDSR;
    private Duration faltasDSR;
    private Duration somaExtras;
    private boolean descontoProximoMes;
    private Duration somaFaltas;
    private Duration somaAusencias;

    public DsrApi() {
        this.tabelaExtrasDSRList = new ArrayList<>();
        this.diasUteis = 0;
        this.domingos = 0;
        this.feriados = 0;
        this.saldoMesSeguinte = Duration.ZERO;
        this.extrasDSR = Duration.ZERO;
        this.faltasDSR = Duration.ZERO;
        this.somaExtras = Duration.ZERO;
        this.descontoProximoMes = Boolean.FALSE;
        this.somaFaltas = Duration.ZERO;
        this.somaAusencias = Duration.ZERO;
    }

    public Date getUltimoDomingoMes() {
        return ultimoDomingoMes;
    }

    public void setUltimoDomingoMes(Date ultimoDomingoMes) {
        this.ultimoDomingoMes = ultimoDomingoMes;
    }

    public List<TabelaSequenciaPercentuais> getTabelaExtrasDSRList() {
        return tabelaExtrasDSRList;
    }

    public void setTabelaExtrasDSRList(List<TabelaSequenciaPercentuais> tabelaExtrasDSRList) {
        this.tabelaExtrasDSRList = tabelaExtrasDSRList;
    }

    public int getDiasUteis() {
        return diasUteis;
    }

    public void setDiasUteis(int diasUteis) {
        this.diasUteis = diasUteis;
    }

    public int getDomingos() {
        return domingos;
    }

    public void setDomingos(int domingos) {
        this.domingos = domingos;
    }

    public int getFeriados() {
        return feriados;
    }

    public void setFeriados(int feriados) {
        this.feriados = feriados;
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

    public Duration getSaldoMesSeguinte() {
        return saldoMesSeguinte;
    }

    public void setSaldoMesSeguinte(Duration saldoMesSeguinte) {
        this.saldoMesSeguinte = saldoMesSeguinte;
    }

    public Duration getExtrasDSR() {
        return extrasDSR;
    }

    public void setExtrasDSR(Duration extrasDSR) {
        this.extrasDSR = extrasDSR;
    }

    public Duration getFaltasDSR() {
        return faltasDSR;
    }

    public void setFaltasDSR(Duration faltasDSR) {
        this.faltasDSR = faltasDSR;
    }

    public Duration getSomaExtras() {
        return somaExtras;
    }

    public void setSomaExtras(Duration somaExtras) {
        this.somaExtras = somaExtras;
    }

    public boolean isDescontoProximoMes() {
        return descontoProximoMes;
    }

    public void setDescontoProximoMes(boolean descontoProximoMes) {
        this.descontoProximoMes = descontoProximoMes;
    }

    public Duration getSomaFaltas() {
        return somaFaltas;
    }

    public void setSomaFaltas(Duration somaFaltas) {
        this.somaFaltas = somaFaltas;
    }

    public Duration getSomaAusencias() {
        return somaAusencias;
    }

    public void setSomaAusencias(Duration somaAusencias) {
        this.somaAusencias = somaAusencias;
    }

}
