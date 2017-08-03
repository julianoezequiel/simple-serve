package com.topdata.toppontoweb.services.gerafrequencia.integracao;

/**
 *
 * @author juliano.ezequiel
 */
public class TCalculoPrincipal extends Thread {

    private Boolean parar = false;

    public TCalculoPrincipal(ProcessarGeraFrequencia geraFrequencia, String name) {
        super(geraFrequencia, name);
        this.setPriority(MIN_PRIORITY);
    }

    public Boolean getParar() {
        return parar;
    }

    public void setParar(Boolean parar) {
        this.parar = parar;
    }

}
