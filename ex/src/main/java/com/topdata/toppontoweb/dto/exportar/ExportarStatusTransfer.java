/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.topdata.toppontoweb.dto.exportar;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.topdata.toppontoweb.dto.gerafrequencia.GeraFrequenciaStatusTransfer;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;
import java.util.Date;
import java.util.UUID;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 *
 * @author tharle.camargo
 */
@JsonIgnoreProperties(value = {
    "thread", "GeraFrequenciaStatusTransfer"
}, ignoreUnknown = true)
public final class ExportarStatusTransfer {
    private String id;
    private Double progresso;
    private Date dataAtualizacao;
    private CONSTANTES.PROCESSO_STATUS mensagem;
    private boolean cancelar;
    private Thread thread;
    private String nomeArquivo;
    private GeraFrequenciaStatusTransfer geraFrequenciaStatusTransfer;
    private boolean processarMarcacoesInvalidas;
    
    public ExportarStatusTransfer(String id) {
        this.progresso = 0d;
        this.id = id;
        this.atualizarDataAtualizacao();
        this.mensagem = CONSTANTES.PROCESSO_STATUS.CARREGANDO_ARQUIVO;
        this.nomeArquivo = "";
        this.cancelar = false;
    }
    
    public ExportarStatusTransfer() {
        this(UUID.randomUUID().toString());
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

    /**
     * @return the mensagem
     */
    public CONSTANTES.PROCESSO_STATUS getMensagem() {
        return mensagem;
    }

    /**
     * @param mensagem the mensagem to set
     */
    public void setMensagem(CONSTANTES.PROCESSO_STATUS mensagem) {
        this.mensagem = mensagem;
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
     * @return the nomeArquivo
     */
    public String getNomeArquivo() {
        return nomeArquivo;
    }

    /**
     * @param nomeArquivo the nomeArquivo to set
     */
    public void setNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
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
     * @return the processarMarcacoesInvalidas
     */
    public boolean isProcessarMarcacoesInvalidas() {
        return processarMarcacoesInvalidas;
    }

    /**
     * @param processarMarcacoesInvalidas the processarMarcacoesInvalidas to set
     */
    public void setProcessarMarcacoesInvalidas(boolean processarMarcacoesInvalidas) {
        this.processarMarcacoesInvalidas = processarMarcacoesInvalidas;
    }


    
}
