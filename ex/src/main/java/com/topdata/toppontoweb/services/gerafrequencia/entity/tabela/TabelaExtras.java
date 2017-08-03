package com.topdata.toppontoweb.services.gerafrequencia.entity.tabela;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Horas da divisão de extras por tipo de dia
 *
 * @author enio.junior
 */
public class TabelaExtras {

    private List<TabelaSequenciaPercentuais> tabelaTipoDiaList;
    private Duration saldoExtrasDiurnas;
    private Duration saldoExtrasNoturnas;
    private TabelaSequenciaPercentuais tabela;

    public TabelaExtras() {
        this.tabelaTipoDiaList = new ArrayList<>();
        this.saldoExtrasDiurnas = Duration.ZERO;
        this.saldoExtrasNoturnas = Duration.ZERO;
    }

    public List<TabelaSequenciaPercentuais> getTabelaTipoDiaList() {
        return tabelaTipoDiaList;
    }

    public void setTabelaTipoDiaList(List<TabelaSequenciaPercentuais> tabelaTipoDiaList) {
        this.tabelaTipoDiaList = tabelaTipoDiaList;
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

    public TabelaSequenciaPercentuais getTabela() {
        return tabela;
    }

    public void setTabela(TabelaSequenciaPercentuais tabela) {
        this.tabela = tabela;
    }

}
