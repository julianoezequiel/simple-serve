package com.topdata.toppontoweb.services.gerafrequencia.entity.tabela;

import com.topdata.toppontoweb.entity.configuracoes.SequenciaPercentuais;
import com.topdata.toppontoweb.entity.tipo.TipoDia;
import java.time.Duration;
import java.util.Date;

/**
 * Tabela de sequencia percentuais cadastrada para efetuar as divisões (extras)
 * / acréscimos (BH)
 *
 * @author enio.junior
 */
public class TabelaSequenciaPercentuais extends SequenciaPercentuais {

    private Duration divisaoDiurnas;
    private Duration resultadoDiurnas;
    private Duration divisaoNoturnas;
    private Duration resultadoNoturnas;

    public TabelaSequenciaPercentuais() {
        this.divisaoDiurnas = Duration.ZERO;
        this.resultadoDiurnas = Duration.ZERO;
        this.divisaoNoturnas = Duration.ZERO;
        this.resultadoNoturnas = Duration.ZERO;
    }

    public TabelaSequenciaPercentuais(Duration divisaoDiurnas, Duration resultadoDiurnas, Duration divisaoNoturnas, Duration resultadoNoturnas, Date horas, Double acrescimo, Integer idSequencia, TipoDia tipoDia) {
        super(horas, acrescimo, idSequencia, tipoDia);
        this.divisaoDiurnas = divisaoDiurnas;
        this.resultadoDiurnas = resultadoDiurnas;
        this.divisaoNoturnas = divisaoNoturnas;
        this.resultadoNoturnas = resultadoNoturnas;
    }

    public Duration getDivisaoDiurnas() {
        return divisaoDiurnas;
    }

    public void setDivisaoDiurnas(Duration divisaoDiurnas) {
        this.divisaoDiurnas = divisaoDiurnas;
    }

    public Duration getResultadoDiurnas() {
        return resultadoDiurnas;
    }

    public void setResultadoDiurnas(Duration resultadoDiurnas) {
        this.resultadoDiurnas = resultadoDiurnas;
    }

    public Duration getDivisaoNoturnas() {
        return divisaoNoturnas;
    }

    public void setDivisaoNoturnas(Duration divisaoNoturnas) {
        this.divisaoNoturnas = divisaoNoturnas;
    }

    public Duration getResultadoNoturnas() {
        return resultadoNoturnas;
    }

    public void setResultadoNoturnas(Duration resultadoNoturnas) {
        this.resultadoNoturnas = resultadoNoturnas;
    }

    public void plus(TabelaSequenciaPercentuais tabela) {
        this.divisaoDiurnas = this.divisaoDiurnas.plus(tabela.divisaoDiurnas);
        this.resultadoDiurnas = this.resultadoDiurnas.plus(tabela.resultadoDiurnas);
        this.divisaoNoturnas = this.divisaoNoturnas.plus(tabela.divisaoNoturnas);
        this.resultadoNoturnas = this.resultadoNoturnas.plus(tabela.resultadoNoturnas);
    }

}
