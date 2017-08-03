package com.topdata.toppontoweb.services.gerafrequencia.integracao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topdata.toppontoweb.dto.gerafrequencia.GeraFrequenciaStatusTransfer;
import com.topdata.toppontoweb.services.gerafrequencia.entity.calculo.EntradaApi;
import com.topdata.toppontoweb.services.gerafrequencia.services.calculo.CalculoPeriodoService;

/**
 * Realiza a chamada da api de cálculo de horário para o funcionário
 *
 * @author juliano.ezequiel
 */
public class CalculoIndividual implements Runnable {

    public final static Logger LOGGER = LoggerFactory.getLogger(CalculoIndividual.class.getName());

    private final EntradaApi entradaApi;
    private final GeraFrequenciaStatusTransfer geraFrequenciaStatusTransfer;

    public CalculoIndividual(EntradaApi entradaApi, GeraFrequenciaStatusTransfer geraFrequenciaStatusTransfer) {
        this.entradaApi = entradaApi;
        this.geraFrequenciaStatusTransfer = geraFrequenciaStatusTransfer;
    }

    @Override
    public synchronized void run() {
        try {
            LOGGER.debug("Cálculo individual " + this.entradaApi.getFuncionario().toString());
            CalculoPeriodoService calculoPeriodoService = new CalculoPeriodoService(this.entradaApi);

            final SaidaPadrao saidaPadrao = new SaidaPadrao(
                    this.entradaApi.getFuncionario(), calculoPeriodoService.getSaidaAPI());
            saidaPadrao.setQuantidadeJornadasPorPeriodo(entradaApi.getQuantidadeJornadasPorPeriodo());
            this.geraFrequenciaStatusTransfer.getSaidaPadraoList().add(saidaPadrao);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
