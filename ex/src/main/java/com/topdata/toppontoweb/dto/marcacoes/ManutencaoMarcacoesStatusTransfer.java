/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.topdata.toppontoweb.dto.marcacoes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.topdata.toppontoweb.dto.gerafrequencia.GeraFrequenciaStatusTransfer;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 *
 * @author tharle.camargo
 */
@JsonIgnoreProperties(value = {
    "thread", "GeraFrequenciaStatusTransfer"
}, ignoreUnknown = true)
public final class ManutencaoMarcacoesStatusTransfer {
    
    private String id;
    private Double progresso;
    private Date dataAtualizacao;
    private CONSTANTES.PROCESSO_STATUS status;
    private boolean cancelar;
    private Thread thread;
    private GeraFrequenciaStatusTransfer geraFrequenciaStatusTransfer;
    private List<ManutencaoMarcacoesFuncionarioTransfer> manutencaoMarcacaoFuncionarioList;
    
    public ManutencaoMarcacoesStatusTransfer(String id) {
        this.progresso = 0d;
        this.id = id;
        this.atualizarDataAtualizacao();
        this.cancelar = false;
        this.manutencaoMarcacaoFuncionarioList = null;
    }
    
    public ManutencaoMarcacoesStatusTransfer() {
        this(UUID.randomUUID().toString());
        this.manutencaoMarcacaoFuncionarioList = null;
    }
    
    public String getId(){
        return id;
    }
    
    /**
     * @return the progresso
     */
    public Double getProgresso() {
        return progresso;
    }

    /**
     * @param progresso the progresso to set
     */
    public void setProgresso(Integer progresso) {
        this.progresso = progresso.doubleValue();
        this.atualizarDataAtualizacao();
    }
    
    public void setProgresso(Double progresso) {
        this.progresso = progresso;
        this.atualizarDataAtualizacao();
    }

    public void addProgresso(int i) {
        this.progresso += i;
        this.atualizarDataAtualizacao();
    }
    
    public void addProgresso(Double i) {
        this.progresso += i;
        this.atualizarDataAtualizacao();
    }
    
    public void atualizarDataAtualizacao(){
        this.dataAtualizacao = new Date();
    }

    public Date getDataAtualizacao() {
        return this.dataAtualizacao;
    }

    public CONSTANTES.PROCESSO_STATUS getStatus() {
        return status;
    }

    public void setStatus(CONSTANTES.PROCESSO_STATUS status) {
        this.status = status;
    }

    public boolean isCancelar() {
        return this.cancelar;
    }

    public void setCancelar(Boolean cancelar) {
        this.cancelar = cancelar;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    @JsonIgnore
    public Thread getThread() {
        return thread;
    }

    /**
     * @return the geraFrequenciaStatusTransfer
     */
    public GeraFrequenciaStatusTransfer getGeraFrequenciaStatusTransfer() {
        return geraFrequenciaStatusTransfer;
    }

    /**
     * @param geraFrequenciaStatusTransfer the geraFrequenciaStatusTransfer to set
     */
    public void setGeraFrequenciaStatusTransfer(GeraFrequenciaStatusTransfer geraFrequenciaStatusTransfer) {
        this.geraFrequenciaStatusTransfer = geraFrequenciaStatusTransfer;
    }

    /**
     * @return the manutencaoMarcacaoFuncionarioList
     */
    public List<ManutencaoMarcacoesFuncionarioTransfer> getManutencaoMarcacaoFuncionarioList() {
        return manutencaoMarcacaoFuncionarioList;
    }

    /**
     * @param manutencaoMarcacaoFuncionarioList the manutencaoMarcacaoFuncionarioList to set
     */
    public void setManutencaoMarcacaoFuncionarioList(List<ManutencaoMarcacoesFuncionarioTransfer> manutencaoMarcacaoFuncionarioList) {
        this.manutencaoMarcacaoFuncionarioList = manutencaoMarcacaoFuncionarioList;
    }
    
    
    
}
