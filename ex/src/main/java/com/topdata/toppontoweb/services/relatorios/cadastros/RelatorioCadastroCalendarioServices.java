package com.topdata.toppontoweb.services.relatorios.cadastros;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topdata.toppontoweb.dto.gerafrequencia.GeraFrequenciaStatusTransfer;
import com.topdata.toppontoweb.dto.relatorios.cadastros.RelatorioCadastroCalendarioTransfer;
import com.topdata.toppontoweb.entity.calendario.Calendario;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.calendario.CalendarioService;
import com.topdata.toppontoweb.services.relatorios.GerarRelatorio;
import com.topdata.toppontoweb.services.relatorios.RelatorioHandler;
import com.topdata.toppontoweb.services.gerafrequencia.integracao.GeraFrequenciaServices;
import com.topdata.toppontoweb.services.gerafrequencia.integracao.IGeraFrequencia;
import com.topdata.toppontoweb.dto.gerafrequencia.IGeraFrequenciaTransfer;
import com.topdata.toppontoweb.entity.autenticacao.Operador;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;

/**
 *
 * @author juliano.ezequiel
 */
@Service
public class RelatorioCadastroCalendarioServices implements IGeraFrequencia {

    @Autowired
    private CalendarioService calendarioService;

    @Autowired
    private GeraFrequenciaServices geraFrequenciaServices;

    @Override
    public GeraFrequenciaStatusTransfer iniciar(HttpServletRequest request, IGeraFrequenciaTransfer geraFrequenciaTransfer,  CONSTANTES.Enum_TIPO_PROCESSO tIPO_PROCESSO) throws ServiceException {

        GeraFrequenciaStatusTransfer statusTransfer = this.geraFrequenciaServices.iniciarProcesso(tIPO_PROCESSO);
        Operador operador = geraFrequenciaServices.getOperadorService().getOperadorDaSessao(request);
        statusTransfer.setOperador(operador);
        Thread thread = new Thread(new GerarRelatorioCalendario(this.calendarioService, geraFrequenciaTransfer, this.geraFrequenciaServices, statusTransfer));
        GeraFrequenciaServices.STATUS_MAP.put(statusTransfer.getId(), statusTransfer);
        thread.start();
        return statusTransfer;
    }

    protected class GerarRelatorioCalendario extends GerarRelatorio implements Runnable {

        private final CalendarioService calendarioService;

        public GerarRelatorioCalendario(CalendarioService calendarioService, IGeraFrequenciaTransfer relatorioTransfer, GeraFrequenciaServices relatorioServices, GeraFrequenciaStatusTransfer statusTransfer) {
            super(relatorioTransfer, relatorioServices, statusTransfer);
            this.calendarioService = calendarioService;
        }

        @Override
        public void run() {
            try {
                GeraFrequenciaServices.STATUS_MAP.get(this.statusTransfer.getId()).setPercentual(0d);
                //Lista de objeto
                List<Calendario> lista = new ArrayList<>();

                RelatorioCadastroCalendarioTransfer relatorioCalendarioTransfer = (RelatorioCadastroCalendarioTransfer) this.relatorioTransfer;
                //Audita os dados
                this.geraFrequenciaServices.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.REL_CAD_CALENDARIO, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.GERAR, statusTransfer.getOperador(), relatorioCalendarioTransfer);

                if (relatorioCalendarioTransfer.getCalendario() != null && relatorioCalendarioTransfer.getCalendario().getIdCalendario() != null) {//Um em específico
                    Calendario calendario = this.calendarioService.buscarCalendario(relatorioCalendarioTransfer.getCalendario().getIdCalendario());
                    lista.add(calendario);
                } else { //Todas os calendarios; 
                    lista = this.calendarioService.buscarTodos();
                }

                GeraFrequenciaServices.STATUS_MAP.get(this.statusTransfer.getId()).setPercentual(50d);

                this.carregarDados(lista);
                this.geraFrequenciaServices.processarRelatorio(this.statusTransfer);

                GeraFrequenciaServices.STATUS_MAP.get(this.statusTransfer.getId()).setPercentual(100d);
            } catch (ServiceException ex) {
                this.statusTransfer.setEstado_processo(RelatorioHandler.ESTADO_PROCESSO.ERRO);
            }
        }

    }

}
