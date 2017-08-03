package com.topdata.toppontoweb.services.gerafrequencia.entity.bancodehoras;

import com.topdata.toppontoweb.services.gerafrequencia.entity.tabela.TabelaSequenciaPercentuais;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Possui todas as informações do banco de horas Crédito / Débito
 *
 * @author enio.junior
 */
public class BancodeHorasApi {

    private boolean possuiBH;
    private Duration credito;
    private Duration creditoFechamento;
    private Duration saldoAcumuladoCredito;
    private Duration debito;
    private Duration debitoFechamento;
    private Duration saldoAcumuladoDebito;   
    private Duration saldoDia;
    private Duration saldoAcumuladoDia;
    private Duration saldoAcumuladoDiaAnterior;  //Esse fica gravado por dia
    private Duration gatilho;
    private Duration saldoAusenciasDiurnas;
    private Duration saldoAusenciasNoturnas;
    private Duration saldoExtrasDiurnas;
    private Duration saldoExtrasNoturnas;
    private Duration totalAcrescimosDiurnas;
    private Duration totalAcrescimosNoturnas;

    private List<TabelaSequenciaPercentuais> acrescimosTipoDiaList;
    private List<TabelaSequenciaPercentuais> acrescimosSomenteNoturnasList;
    private List<TabelaSequenciaPercentuais> tabelaTiposdeDia;
    private List<TabelaCreditoSaldoUnico> tabelaCreditoSaldoUnicoList;
    
    public BancodeHorasApi() {
        this.possuiBH = Boolean.FALSE;
        this.credito = Duration.ZERO;
        this.debito = Duration.ZERO;
        this.saldoDia = Duration.ZERO;
        this.saldoAcumuladoDia = Duration.ZERO;
        this.saldoAcumuladoDiaAnterior = Duration.ZERO;
        this.gatilho = Duration.ZERO;
        this.saldoAcumuladoCredito = Duration.ZERO;
        this.saldoAcumuladoDebito = Duration.ZERO;
        this.acrescimosTipoDiaList = new ArrayList<>();
        this.acrescimosSomenteNoturnasList = new ArrayList<>();
        this.tabelaTiposdeDia = new ArrayList<>();
        this.tabelaCreditoSaldoUnicoList = new ArrayList<>();
        this.creditoFechamento = Duration.ZERO;
        this.debitoFechamento = Duration.ZERO;
        this.saldoAusenciasDiurnas = Duration.ZERO;
        this.saldoAusenciasNoturnas = Duration.ZERO;
        this.saldoExtrasDiurnas = Duration.ZERO;
        this.saldoExtrasNoturnas = Duration.ZERO;
        this.totalAcrescimosDiurnas = Duration.ZERO;
        this.totalAcrescimosNoturnas = Duration.ZERO;
    }

    public BancodeHorasApi(boolean possuiBH, Duration credito, Duration debito, Duration saldoDia, Duration saldoAcumuladoDia, Duration saldoAcumuladoDiaAnterior, Duration gatilho, Duration saldoAcumuladoCredito, Duration saldoAcumuladoDebito, List<TabelaSequenciaPercentuais> acrescimosList, List<TabelaSequenciaPercentuais> acrescimosSomenteNoturnasList, List<TabelaSequenciaPercentuais> tabelaTiposdeDiaList, List<TabelaCreditoSaldoUnico> tabelaCreditoSaldoUnicoList, Duration creditoFechamento, Duration debitoFechamento) {
        this.possuiBH = possuiBH;
        this.credito = credito;
        this.debito = debito;
        this.saldoDia = saldoDia;
        this.saldoAcumuladoDia = saldoAcumuladoDia;
        this.saldoAcumuladoDiaAnterior = saldoAcumuladoDiaAnterior;
        this.gatilho = gatilho;
        this.saldoAcumuladoCredito = saldoAcumuladoCredito;
        this.saldoAcumuladoDebito = saldoAcumuladoDebito;
        this.acrescimosTipoDiaList = acrescimosList;
        this.acrescimosSomenteNoturnasList = acrescimosSomenteNoturnasList;
        this.tabelaTiposdeDia = tabelaTiposdeDiaList;
        this.tabelaCreditoSaldoUnicoList = tabelaCreditoSaldoUnicoList;
        this.creditoFechamento = creditoFechamento;
        this.debitoFechamento = debitoFechamento;
        this.saldoAusenciasDiurnas = Duration.ZERO;
        this.saldoAusenciasNoturnas = Duration.ZERO;
        this.saldoExtrasDiurnas = Duration.ZERO;
        this.saldoExtrasNoturnas = Duration.ZERO;
        this.totalAcrescimosDiurnas = Duration.ZERO;
        this.totalAcrescimosNoturnas = Duration.ZERO;       
    }

    public boolean isPossuiBH() {
        return possuiBH;
    }

    public void setPossuiBH(boolean possuiBH) {
        this.possuiBH = possuiBH;
    }

    public Duration getCredito() {
        return credito;
    }

    public void setCredito(Duration credito) {
        this.credito = credito;
    }

    public Duration getDebito() {
        return debito;
    }

    public void setDebito(Duration debito) {
        this.debito = debito;
    }

    public Duration getSaldoDia() {
        return saldoDia;
    }

    public void setSaldoDia(Duration saldoDia) {
        this.saldoDia = saldoDia;
    }

    public Duration getSaldoAcumuladoDia() {
        return saldoAcumuladoDia;
    }

    public void setSaldoAcumuladoDia(Duration saldoAcumuladoDia) {
        this.saldoAcumuladoDia = saldoAcumuladoDia;
    }

    public Duration getSaldoAcumuladoDiaAnterior() {
        return saldoAcumuladoDiaAnterior;
    }

    public void setSaldoAcumuladoDiaAnterior(Duration saldoAcumuladoDiaAnterior) {
        this.saldoAcumuladoDiaAnterior = saldoAcumuladoDiaAnterior;
    }

    public Duration getGatilho() {
        return gatilho;
    }

    public void setGatilho(Duration gatilho) {
        this.gatilho = gatilho;
    }

    public Duration getSaldoAcumuladoCredito() {
        return saldoAcumuladoCredito;
    }

    public void setSaldoAcumuladoCredito(Duration saldoAcumuladoCredito) {
        this.saldoAcumuladoCredito = saldoAcumuladoCredito;
    }

    public Duration getSaldoAcumuladoDebito() {
        return saldoAcumuladoDebito;
    }

    public void setSaldoAcumuladoDebito(Duration saldoAcumuladoDebito) {
        this.saldoAcumuladoDebito = saldoAcumuladoDebito;
    }

    public List<TabelaSequenciaPercentuais> getAcrescimosTipoDiaList() {
        return acrescimosTipoDiaList;
    }

    public void setAcrescimosTipoDiaList(List<TabelaSequenciaPercentuais> acrescimosTipoDiaList) {
        this.acrescimosTipoDiaList = acrescimosTipoDiaList;
    }

    public List<TabelaSequenciaPercentuais> getAcrescimosSomenteNoturnasList() {
        return acrescimosSomenteNoturnasList;
    }

    public void setAcrescimosSomenteNoturnasList(List<TabelaSequenciaPercentuais> acrescimosSomenteNoturnasList) {
        this.acrescimosSomenteNoturnasList = acrescimosSomenteNoturnasList;
    }

    public Duration getCreditoFechamento() {
        return creditoFechamento;
    }

    public void setCreditoFechamento(Duration creditoFechamento) {
        this.creditoFechamento = creditoFechamento;
    }

    public Duration getDebitoFechamento() {
        return debitoFechamento;
    }

    public void setDebitoFechamento(Duration debitoFechamento) {
        this.debitoFechamento = debitoFechamento;
    }

    public Duration getSaldoAusenciasDiurnas() {
        return saldoAusenciasDiurnas;
    }

    public void setSaldoAusenciasDiurnas(Duration saldoAusenciasDiurnas) {
        this.saldoAusenciasDiurnas = saldoAusenciasDiurnas;
    }

    public Duration getSaldoAusenciasNoturnas() {
        return saldoAusenciasNoturnas;
    }

    public void setSaldoAusenciasNoturnas(Duration saldoAusenciasNoturnas) {
        this.saldoAusenciasNoturnas = saldoAusenciasNoturnas;
    }

    public Duration getSaldoExtrasDiurnas() {
        return saldoExtrasDiurnas;
    }

    public void setSaldoExtrasDiurnas(Duration saldoExtrasDiurnas) {
        this.saldoExtrasDiurnas = saldoExtrasDiurnas;
    }

    public Duration getSaldoExtrasNoturnas() {
        return saldoExtrasNoturnas;
    }

    public void setSaldoExtrasNoturnas(Duration saldoExtrasNoturnas) {
        this.saldoExtrasNoturnas = saldoExtrasNoturnas;
    }

    public Duration getTotalAcrescimosDiurnas() {
        return totalAcrescimosDiurnas;
    }

    public void setTotalAcrescimosDiurnas(Duration totalAcrescimosDiurnas) {
        this.totalAcrescimosDiurnas = totalAcrescimosDiurnas;
    }

    public Duration getTotalAcrescimosNoturnas() {
        return totalAcrescimosNoturnas;
    }

    public void setTotalAcrescimosNoturnas(Duration totalAcrescimosNoturnas) {
        this.totalAcrescimosNoturnas = totalAcrescimosNoturnas;
    }

    public List<TabelaSequenciaPercentuais> getTabelaTiposdeDia() {
        return tabelaTiposdeDia;
    }

    public void setTabelaTiposdeDia(List<TabelaSequenciaPercentuais> tabelaTiposdeDia) {
        this.tabelaTiposdeDia = tabelaTiposdeDia;
    }

    public List<TabelaCreditoSaldoUnico> getTabelaCreditoSaldoUnicoList() {
        return tabelaCreditoSaldoUnicoList;
    }

    public void setTabelaCreditoSaldoUnicoList(List<TabelaCreditoSaldoUnico> tabelaCreditoSaldoUnicoList) {
        this.tabelaCreditoSaldoUnicoList = tabelaCreditoSaldoUnicoList;
    }

    public void plus(BancodeHorasApi bh) {
        if (bh != null) {
            this.credito = this.credito.plus(bh.credito);
            this.debito = this.debito.plus(bh.debito);
            this.saldoDia = this.saldoDia.plus(bh.saldoDia);
            this.saldoAcumuladoDia = this.saldoAcumuladoDia.plus(bh.saldoAcumuladoDia);
            this.saldoAcumuladoDiaAnterior = this.saldoAcumuladoDiaAnterior.plus(bh.saldoAcumuladoDiaAnterior);
            this.gatilho = this.gatilho.plus(bh.gatilho);
            this.saldoAcumuladoCredito = this.saldoAcumuladoCredito.plus(bh.saldoAcumuladoCredito);
            this.saldoAcumuladoDebito = this.saldoAcumuladoDebito.plus(bh.saldoAcumuladoDebito);
            this.creditoFechamento = this.creditoFechamento.plus(bh.creditoFechamento);
            this.debitoFechamento = this.debitoFechamento.plus(bh.debitoFechamento);
            this.saldoAusenciasDiurnas = this.saldoAusenciasDiurnas.plus(bh.saldoAusenciasDiurnas);
            this.saldoAusenciasNoturnas = this.saldoAusenciasNoturnas.plus(bh.saldoAusenciasNoturnas);
            this.saldoExtrasDiurnas = this.saldoExtrasDiurnas.plus(bh.saldoExtrasDiurnas);
            this.saldoExtrasNoturnas = this.saldoExtrasNoturnas.plus(bh.saldoExtrasNoturnas);
            this.totalAcrescimosDiurnas = this.totalAcrescimosDiurnas.plus(bh.totalAcrescimosDiurnas);
            this.totalAcrescimosNoturnas = this.totalAcrescimosNoturnas.plus(bh.totalAcrescimosNoturnas);

            //TODO: Isso vai pesar
            bh.getTabelaTiposdeDia().stream().forEach((tabela) -> {
                boolean encontrouTabela = false;
                for (TabelaSequenciaPercentuais tabelaTotal : this.tabelaTiposdeDia) {
                    if (Objects.equals(tabelaTotal.getTipoDia().getIdTipodia(), tabela.getTipoDia().getIdTipodia())
                            && Objects.equals(tabelaTotal.getIdSequencia(), tabela.getIdSequencia())) {
                        encontrouTabela = true;
                        tabelaTotal.plus(tabela);
                    }
                }

                if (!encontrouTabela) {
                    this.tabelaTiposdeDia.add(tabela);
                }
            });

            bh.tabelaCreditoSaldoUnicoList.stream().forEach((tabela) -> {
                boolean encontrouTabela = false;
                for (TabelaCreditoSaldoUnico tabelaTotal : this.tabelaCreditoSaldoUnicoList) {
                    if (Objects.equals(tabelaTotal.getTipoDia().getIdTipodia(), tabela.getTipoDia().getIdTipodia())) {
                        encontrouTabela = true;
                        tabelaTotal.plus(tabela);
                    }
                }

                if (!encontrouTabela) {
                    this.tabelaCreditoSaldoUnicoList.add(tabela);
                }
            });
        }
    }

}
