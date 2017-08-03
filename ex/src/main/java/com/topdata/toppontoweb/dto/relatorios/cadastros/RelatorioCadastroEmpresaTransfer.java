package com.topdata.toppontoweb.dto.relatorios.cadastros;

import com.topdata.toppontoweb.entity.empresa.Empresa;
import com.topdata.toppontoweb.services.relatorios.RelatorioHandler;

/**
 *
 * @author juliano.ezequiel
 */
public class RelatorioCadastroEmpresaTransfer extends RelatorioCadastroTransfer {

    private Empresa empresa;
    private Boolean isCEI;
    private Boolean isDepartamento;
    private Boolean isEmpresasAtivas;

    public RelatorioCadastroEmpresaTransfer() {
        super();
        this.empresa = new Empresa();
        this.isCEI = true;
        this.isDepartamento = true;
        this.isEmpresasAtivas = true;
        this.tipo_relatorio = RelatorioHandler.TIPO_RELATORIO.CAD_EMPRESA;
    }

    public RelatorioCadastroEmpresaTransfer(RelatorioCadastroEmpresaTransfer relatorioTransfer) {
        super(relatorioTransfer);
        this.empresa = relatorioTransfer.empresa;
        this.isCEI = relatorioTransfer.isCEI;
        this.isDepartamento = relatorioTransfer.isDepartamento;
        this.isEmpresasAtivas = relatorioTransfer.isEmpresasAtivas;
        this.tipo_relatorio = RelatorioHandler.TIPO_RELATORIO.CAD_EMPRESA;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public Boolean getIsDepartamento() {
        return isDepartamento;
    }

    public Boolean getIsEmpresasAtivas() {
        return isEmpresasAtivas;
    }

    public Boolean getIsCEI() {
        return isCEI;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public void setIsCEI(Boolean isCEI) {
        this.isCEI = isCEI;
    }

    public void setIsDepartamento(Boolean isDepartamento) {
        this.isDepartamento = isDepartamento;
    }

    public void setIsEmpresasAtivas(Boolean isEmpresasAtivas) {
        this.isEmpresasAtivas = isEmpresasAtivas;
    }

}
