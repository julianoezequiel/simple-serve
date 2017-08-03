package com.topdata.toppontoweb.services.gerafrequencia.entity.fiscal;

/**
 * Formato linha exportação arquivos
 *
 * @author enio.junior
 */
public class Arquivo {

    private int sequencia;
    private String linha;
    private int codigoHorario;

    public Arquivo(int sequencia, int codigoHorario, String linha) {
        this.sequencia = sequencia;
        this.codigoHorario = codigoHorario;
        this.linha = linha;
    }

    public Arquivo(int sequencia, String linha) {
        this.sequencia = sequencia;
        this.linha = linha;
    }

    public int getSequencia() {
        return sequencia;
    }

    public void setSequencia(int sequencia) {
        this.sequencia = sequencia;
    }

    public String getLinha() {
        return linha;
    }

    public void setLinha(String linha) {
        this.linha = linha;
    }

    public int getCodigoHorario() {
        return codigoHorario;
    }

    public void setCodigoHorario(int codigoHorario) {
        this.codigoHorario = codigoHorario;
    }

}
