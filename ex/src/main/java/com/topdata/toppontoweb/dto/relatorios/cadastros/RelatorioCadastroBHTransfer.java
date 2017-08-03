package com.topdata.toppontoweb.dto.relatorios.cadastros;

import com.topdata.toppontoweb.services.relatorios.RelatorioHandler;

/**
 *
 * @author juliano.ezequiel
 */
public class RelatorioCadastroBHTransfer extends RelatorioCadastroTransfer {
    
    private boolean bancosSemDataFim;

    public RelatorioCadastroBHTransfer() {
        super();
        this.tipo_relatorio = RelatorioHandler.TIPO_RELATORIO.CAD_BANCO_DE_HORAS;
        this.bancosSemDataFim = false;
    }

    /**
     * @return the bancosSemDataFim
     */
    public boolean isBancosSemDataFim() {
        return bancosSemDataFim;
    }

    /**
     * @param bancosSemDataFim the bancosSemDataFim to set
     */
    public void setBancosSemDataFim(boolean bancosSemDataFim) {
        this.bancosSemDataFim = bancosSemDataFim;
    }
    
    

}
