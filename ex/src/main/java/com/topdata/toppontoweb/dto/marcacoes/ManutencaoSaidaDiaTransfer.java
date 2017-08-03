/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.topdata.toppontoweb.dto.marcacoes;

import com.topdata.toppontoweb.entity.marcacoes.Marcacoes;
import com.topdata.toppontoweb.services.gerafrequencia.entity.marcacoes.MarcacoesDia;
import com.topdata.toppontoweb.utils.CustomDateWeekSerializer;
import java.util.Date;
import java.util.List;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 *
 * @author tharle.camargo
 */
public class ManutencaoSaidaDiaTransfer {
    private Date data;
    private List<MarcacoesDia> horariosTrabalhadosOriginaisEquipamentoList;
    private List<Marcacoes> marcacoesList;
    private boolean descontaDSR;

    public ManutencaoSaidaDiaTransfer(Date data, List<MarcacoesDia> horariosTrabalhadosOriginaisEquipamentoList, List<Marcacoes> marcacoesList, boolean descontaDSR) {
        this.data = data;
        this.horariosTrabalhadosOriginaisEquipamentoList = horariosTrabalhadosOriginaisEquipamentoList;
        this.marcacoesList = marcacoesList;
        this.descontaDSR = descontaDSR;
    }
    
    

    /**
     * @return the data
     */
    @JsonSerialize(using = CustomDateWeekSerializer.class)
    public Date getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(Date data) {
        this.data = data;
    }

    /**
     * @return the horariosTrabalhadosOriginaisEquipamentoList
     */
    public List<MarcacoesDia> getHorariosTrabalhadosOriginaisEquipamentoList() {
        return horariosTrabalhadosOriginaisEquipamentoList;
    }

    /**
     * @param horariosTrabalhadosOriginaisEquipamentoList the horariosTrabalhadosOriginaisEquipamentoList to set
     */
    public void setHorariosTrabalhadosOriginaisEquipamentoList(List<MarcacoesDia> horariosTrabalhadosOriginaisEquipamentoList) {
        this.horariosTrabalhadosOriginaisEquipamentoList = horariosTrabalhadosOriginaisEquipamentoList;
    }

    /**
     * @return the descontaDSR
     */
    public boolean isDescontaDSR() {
        return descontaDSR;
    }

    /**
     * @param descontaDSR the descontaDSR to set
     */
    public void setDescontaDSR(boolean descontaDSR) {
        this.descontaDSR = descontaDSR;
    }

    /**
     * @return the marcacoesList
     */
    public List<Marcacoes> getMarcacoesList() {
        return marcacoesList;
    }

    /**
     * @param marcacoesList the marcacoesList to set
     */
    public void setMarcacoesList(List<Marcacoes> marcacoesList) {
        this.marcacoesList = marcacoesList;
    }

    
    
    
    
    
    
    
    
}
