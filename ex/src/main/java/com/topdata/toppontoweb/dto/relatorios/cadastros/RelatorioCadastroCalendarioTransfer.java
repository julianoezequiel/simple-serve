package com.topdata.toppontoweb.dto.relatorios.cadastros;

import com.topdata.toppontoweb.entity.calendario.Calendario;
import com.topdata.toppontoweb.services.relatorios.RelatorioHandler;

/**
 *
 * @author juliano.ezequiel
 */
public class RelatorioCadastroCalendarioTransfer extends RelatorioCadastroTransfer {

    private Calendario calendario;

    public RelatorioCadastroCalendarioTransfer() {
        super();
        this.calendario = new Calendario();
        this.tipo_relatorio = RelatorioHandler.TIPO_RELATORIO.CAD_CALENDARIO;
    }

    public RelatorioCadastroCalendarioTransfer(RelatorioCadastroCalendarioTransfer relatorioTransfer) {
        super(relatorioTransfer);
        this.calendario = relatorioTransfer.calendario;
        this.tipo_relatorio = RelatorioHandler.TIPO_RELATORIO.CAD_CALENDARIO;
    }

    /**
     * @return the calendario
     */
    public Calendario getCalendario() {
        return calendario;
    }

    /**
     * @param calendario the calendario to set
     */
    public void setCalendario(Calendario calendario) {
        this.calendario = calendario;
    }

}
