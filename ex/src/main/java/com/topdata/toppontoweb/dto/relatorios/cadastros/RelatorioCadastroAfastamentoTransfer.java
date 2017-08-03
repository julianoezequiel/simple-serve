package com.topdata.toppontoweb.dto.relatorios.cadastros;

import com.topdata.toppontoweb.services.relatorios.RelatorioHandler;

/**
 *
 * @author juliano.ezequiel
 */
public class RelatorioCadastroAfastamentoTransfer extends RelatorioCadastroTransfer {

    public RelatorioCadastroAfastamentoTransfer() {
        super();
        this.tipo_relatorio = RelatorioHandler.TIPO_RELATORIO.CAD_AFASTAMENTO;
    }

}
