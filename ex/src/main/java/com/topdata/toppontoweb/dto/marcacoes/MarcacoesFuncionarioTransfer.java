/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.topdata.toppontoweb.dto.marcacoes;

import com.topdata.toppontoweb.dto.funcionario.FuncionarioTransfer;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tharle.camargo
 */
public class MarcacoesFuncionarioTransfer {
    
    private FuncionarioTransfer funcionario;
    private List<ManutencaoSaidaDiaTransfer> manutencaoSaidaDiaList;

    public MarcacoesFuncionarioTransfer() {
        this.funcionario = null;
        manutencaoSaidaDiaList = new ArrayList<>();
    }

    public MarcacoesFuncionarioTransfer(FuncionarioTransfer funcionario, List<ManutencaoSaidaDiaTransfer> manutencaoSaidaDiaList) {
        this.funcionario = funcionario;
        this.manutencaoSaidaDiaList = manutencaoSaidaDiaList;
    }
    
    

    /**
     * @return the funcionario
     */
    public FuncionarioTransfer getFuncionario() {
        return funcionario;
    }

    /**
     * @param funcionario the funcionario to set
     */
    public void setFuncionario(FuncionarioTransfer funcionario) {
        this.funcionario = funcionario;
    }

    /**
     * @return the manutencaoSaidaDiaList
     */
    public List<ManutencaoSaidaDiaTransfer> getManutencaoSaidaDiaList() {
        return manutencaoSaidaDiaList;
    }

    /**
     * @param manutencaoSaidaDiaList the manutencaoSaidaDiaList to set
     */
    public void setManutencaoSaidaDiaList(List<ManutencaoSaidaDiaTransfer> manutencaoSaidaDiaList) {
        this.manutencaoSaidaDiaList = manutencaoSaidaDiaList;
    }
    
    


    
    
    
    
}
