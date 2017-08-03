/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.topdata.toppontoweb.dto.marcacoes;

import com.topdata.toppontoweb.dto.funcionario.FuncionarioTransfer;

/**
 *
 * @author tharle.camargo
 */
public class ManutencaoMarcacoesFuncionarioTransfer {
    
    private FuncionarioTransfer funcionario;
    
    private boolean possuiInconsistencias;

    public ManutencaoMarcacoesFuncionarioTransfer() {
    }

    public ManutencaoMarcacoesFuncionarioTransfer(FuncionarioTransfer funcionario, boolean possuiInconsistencias) {
        this.funcionario = funcionario;
        this.possuiInconsistencias = possuiInconsistencias;
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
     * @return the possuiInconsistencias
     */
    public boolean isPossuiInconsistencias() {
        return possuiInconsistencias;
    }

    /**
     * @param possuiInconsistencias the possuiInconsistencias to set
     */
    public void setPossuiInconsistencias(boolean possuiInconsistencias) {
        this.possuiInconsistencias = possuiInconsistencias;
    }
    
    
    
}
