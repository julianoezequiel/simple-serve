package com.topdata.toppontoweb.services.relatorios.gerafrequencia;

import com.topdata.toppontoweb.dto.gerafrequencia.GeraFrequenciaStatusTransfer;
import com.topdata.toppontoweb.dto.gerafrequencia.IGeraFrequenciaTransfer;
import com.topdata.toppontoweb.services.gerafrequencia.integracao.GeraFrequenciaServices;
import com.topdata.toppontoweb.services.relatorios.GerarRelatorio;
import com.topdata.toppontoweb.services.relatorios.RelatorioHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author juliano.ezequiel
 */
public class GeraFrequenciaRelatorio extends GerarRelatorio {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeraFrequenciaRelatorio.class);

    public GeraFrequenciaRelatorio(IGeraFrequenciaTransfer relatorioTransfer, GeraFrequenciaServices geraFrequenciaServices1, GeraFrequenciaStatusTransfer statusTransfer) {
        super(relatorioTransfer, geraFrequenciaServices1, statusTransfer);
    }

    @Override
    public void run() {
        try {
            this.carregarDados(this.statusTransfer.getSaidaPadraoList());
            this.geraFrequenciaServices.processarRelatorio(this.statusTransfer);
            //finaliza o processo
            GeraFrequenciaServices.STATUS_MAP.get(this.statusTransfer.getId()).setPercentual(100d);
        } catch (Exception e) {
            LOGGER.debug(this.getClass().getSimpleName(), e);
            GeraFrequenciaServices.STATUS_MAP.get(statusTransfer.getId()).setEstado_processo(RelatorioHandler.ESTADO_PROCESSO.ERRO);
        }

    }

}
