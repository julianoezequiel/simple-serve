/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.topdata.toppontoweb.dto;

/**
 *
 * @author tharle.camargo
 */
public class PaginacaoTransfer {
    private Integer pagina;
    private Integer qntPorPagina;
    private String busca;
    
    public PaginacaoTransfer() {
        this.pagina = -1;
        this.qntPorPagina = -1;
        this.busca = "";
    }

    public PaginacaoTransfer(Integer pagina, Integer qntPorPagina, String busca) {
        this.pagina = pagina;
        this.qntPorPagina = qntPorPagina;
        this.busca = busca;
    }

    
    

    /**
     * @return the pagina
     */
    public Integer getPagina() {
        return pagina;
    }

    /**
     * @param pagina the pagina to set
     */
    public void setPagina(Integer pagina) {
        this.pagina = pagina;
    }
    
    /**
     * @return the qntPorPagina
     */
    public Integer getQntPorPagina() {
        return qntPorPagina;
    }

    /**
     * @param qntPorPagina the qntPorPagina to set
     */
    public void setQntPorPagina(Integer qntPorPagina) {
        this.qntPorPagina = qntPorPagina;
    }
    
    /**
     * @return the busca
     */
    public String getBusca() {
        return busca;
    }

    /**
     * @param busca the busca to set
     */
    public void setBusca(String busca) {
        this.busca = busca;
    }

    
    
}
