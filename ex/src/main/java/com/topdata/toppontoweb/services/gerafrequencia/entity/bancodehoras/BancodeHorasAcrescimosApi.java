package com.topdata.toppontoweb.services.gerafrequencia.entity.bancodehoras;

import com.topdata.toppontoweb.services.gerafrequencia.entity.tabela.TabelaSequenciaPercentuais;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Acréscimos cadastrados no saldo de crédito de BH
 *
 * @author enio.junior
 */
public class BancodeHorasAcrescimosApi {

    private BancodeHorasApi bancodeHoras;
    private TabelaSequenciaPercentuais tabela;
    private List<TabelaSequenciaPercentuais> tabelaTipoDiaList;
    private List<TabelaSequenciaPercentuais> tabelaNoturnasList;
    
    private Duration limiteDia;

    public BancodeHorasAcrescimosApi() {
        this.tabelaTipoDiaList = new ArrayList<>();
        this.tabelaNoturnasList = new ArrayList<>();
        this.limiteDia = Duration.ZERO;
    }

    public BancodeHorasApi getBancodeHoras() {
        return bancodeHoras;
    }

    public void setBancodeHoras(BancodeHorasApi bancodeHoras) {
        this.bancodeHoras = bancodeHoras;
    }

    public TabelaSequenciaPercentuais getTabela() {
        return tabela;
    }

    public void setTabela(TabelaSequenciaPercentuais tabela) {
        this.tabela = tabela;
    }

    public List<TabelaSequenciaPercentuais> getTabelaTipoDiaList() {
        return tabelaTipoDiaList;
    }

    public void setTabelaTipoDiaList(List<TabelaSequenciaPercentuais> tabelaTipoDiaList) {
        this.tabelaTipoDiaList = tabelaTipoDiaList;
    }

    public List<TabelaSequenciaPercentuais> getTabelaNoturnasList() {
        return tabelaNoturnasList;
    }

    public void setTabelaNoturnasList(List<TabelaSequenciaPercentuais> tabelaNoturnasList) {
        this.tabelaNoturnasList = tabelaNoturnasList;
    }

    public Duration getLimiteDia() {
        return limiteDia;
    }

    public void setLimiteDia(Duration limiteDia) {
        this.limiteDia = limiteDia;
    }

}
