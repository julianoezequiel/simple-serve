package com.topdata.toppontoweb.dto.importar;

import com.topdata.toppontoweb.entity.empresa.Empresa;
import java.io.File;
import java.util.Date;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 *
 * @author tharle.camargo
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ImportarTransfer {
    private File arquivoImportado;
    private Empresa empresa;
    private Date ultimaAtualizacao;

    public ImportarTransfer() {
        arquivoImportado = null;
        empresa = new Empresa();
        ultimaAtualizacao = new Date();
    }

    /**
     * @return the arquivoImportado
     */
    public File getArquivoImportado() {
        return arquivoImportado;
    }

    /**
     * @param arquivoImportado the arquivoImportado to set
     */
    public void setArquivoImportado(File arquivoImportado) {
        this.arquivoImportado = arquivoImportado;
    }

    /**
     * @return the empresa
     */
    public Empresa getEmpresa() {
        return empresa;
    }

    /**
     * @param empresa the empresa to set
     */
    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    /**
     * @return the ultimaAtualizacao
     */
    public Date getUltimaAtualizacao() {
        return ultimaAtualizacao;
    }

    /**
     */
    public void atualizarUltimaAtualizacao() {
        this.ultimaAtualizacao = new Date();
    }
    
    
}
