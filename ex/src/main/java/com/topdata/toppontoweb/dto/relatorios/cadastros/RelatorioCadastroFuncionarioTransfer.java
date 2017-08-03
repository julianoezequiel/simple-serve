package com.topdata.toppontoweb.dto.relatorios.cadastros;

import com.topdata.toppontoweb.services.relatorios.RelatorioHandler;

/**
 *
 * @author juliano.ezequiel
 */
public final class RelatorioCadastroFuncionarioTransfer extends RelatorioCadastroTransfer {

    private boolean relatorioDetalhado;

    public RelatorioCadastroFuncionarioTransfer() {
        super();
        this.relatorioDetalhado = true;
        this.tipo_relatorio = RelatorioHandler.TIPO_RELATORIO.CAD_FUNCIONARIO;
    }

    public RelatorioCadastroFuncionarioTransfer(RelatorioCadastroFuncionarioTransfer relatorioTransfer) {
        super(relatorioTransfer);
        this.relatorioDetalhado = relatorioTransfer.relatorioDetalhado;
        this.tipo_relatorio = RelatorioHandler.TIPO_RELATORIO.CAD_FUNCIONARIO;
    }

    /**
     * @return the relatorioDetalhado
     */
    public boolean isRelatorioDetalhado() {
        return relatorioDetalhado;
    }

    /**
     * @param relatorioDetalhado the relatorioDetalhado to set
     */
    public void setRelatorioDetalhado(boolean relatorioDetalhado) {
        this.relatorioDetalhado = relatorioDetalhado;
    }

}
