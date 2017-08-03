package com.topdata.toppontoweb.dto.importar;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.topdata.toppontoweb.entity.marcacoes.Importacao;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;
import java.util.Date;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 *
 * @author tharle.camargo
 */
@JsonIgnoreProperties(value = {
    "thread",
}, ignoreUnknown = true)
public final class ImportarStatusTransfer {
    private final Integer id;
    private Double progresso;
    private Date dataAtualizacao;
    private CONSTANTES.PROCESSO_STATUS mensagem;
    private Importacao importacao;
    private Boolean cancelar;
    private Thread thread;
    
    public ImportarStatusTransfer(Integer id) {
        this.progresso = 0d;
        this.id = id;
        this.atualizarDataAtualizacao();
        this.mensagem = CONSTANTES.PROCESSO_STATUS.CARREGANDO_ARQUIVO;
        this.importacao = null;
        this.cancelar = false;
    }

    public Integer getId(){
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

    /**
     * @return the importacao
     */
    public Importacao getImportacao() {
        return importacao;
    }

    /**
     * @param importacao the importacao to set
     */
    public void setImportacao(Importacao importacao) {
        this.importacao = importacao;
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
    
}
