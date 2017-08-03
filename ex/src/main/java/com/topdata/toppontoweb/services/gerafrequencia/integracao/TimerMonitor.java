package com.topdata.toppontoweb.services.gerafrequencia.integracao;

import java.util.TimerTask;

import com.topdata.toppontoweb.dto.gerafrequencia.GeraFrequenciaStatusTransfer;

/**
 * Timer utilizado para monitorar se o cliente que inicio o processo ainda esta
 * aguardando o tremino. Caso o cliente desista do processo. As threads
 * iniciadas para a realização do processo serão finalizadas.
 *
 * @author juliano.ezequiel
 */
public class TimerMonitor extends TimerTask {

    private final GeraFrequenciaServices relatorioServices;
    private final GeraFrequenciaStatusTransfer statusTransfer;

    public TimerMonitor(GeraFrequenciaServices relatorioServices, GeraFrequenciaStatusTransfer statusTransfer) {
        this.relatorioServices = relatorioServices;
        this.statusTransfer = statusTransfer;
    }

    @Override
    public void run() {
        this.relatorioServices.keepAlive(statusTransfer.getId(),this);
    }
}
