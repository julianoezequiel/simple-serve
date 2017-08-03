package com.topdata.toppontoweb.dto.relatorios;

import com.topdata.toppontoweb.entity.tipo.TipoDia;
import com.topdata.toppontoweb.services.gerafrequencia.entity.tabela.TabelaSequenciaPercentuais;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tharle.camargo
 */
public class TabelaAgrupada {
    
    private final TipoDia tipoDia;
    private final List<TabelaSequenciaPercentuais> tabelaList;

    public TabelaAgrupada() {
        this.tipoDia = new TipoDia();
        this.tabelaList = new ArrayList<>();
    }
    
    public TabelaAgrupada(TabelaSequenciaPercentuais tabela) {
        this.tipoDia = tabela.getTipoDia();
        
        this.tabelaList =  new ArrayList<>();
        this.tabelaList.add(tabela);
    }
    
    public TabelaAgrupada(TipoDia tipoDia) {
        this.tipoDia = tipoDia;
        this.tabelaList =  new ArrayList<>();
    }
    

    /**
     * @return the tipoDia
     */
    public TipoDia getTipoDia() {
        return tipoDia;
    }

    /**
     * @return the tabelaList
     */
    public List<TabelaSequenciaPercentuais> getTabelaList() {
        return tabelaList;
    }
    
    public void add(TabelaSequenciaPercentuais tabela){
        this.tabelaList.add(tabela);
    }
    
    
    
}
