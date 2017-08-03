package com.topdata.toppontoweb.dto.relatorios.cadastros;

import com.topdata.toppontoweb.dto.FormatoTransfer;
import com.topdata.toppontoweb.entity.funcionario.Funcionario;
import com.topdata.toppontoweb.services.relatorios.RelatorioHandler;
import java.util.Date;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import com.topdata.toppontoweb.dto.gerafrequencia.IGeraFrequenciaTransfer;
import com.topdata.toppontoweb.entity.autenticacao.Operador;
import com.topdata.toppontoweb.entity.empresa.Departamento;
import com.topdata.toppontoweb.entity.empresa.Empresa;
import java.io.Serializable;

/**
 *
 * @author juliano.ezequiel
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class RelatorioCadastroTransfer implements IGeraFrequenciaTransfer, Serializable {

    private FormatoTransfer formato;
    private Date periodoInicio;
    private Date periodoFim;
    public RelatorioHandler.TIPO_RELATORIO tipo_relatorio;
    
    private Empresa empresa;
    private Departamento departamento;
    private Funcionario funcionario;
    
    private boolean empresaAtiva;
    private boolean departamentoAtivo;
    private boolean funcionarioAtivo;
    
    private Operador operador;

    public RelatorioCadastroTransfer() {
        this.formato = new FormatoTransfer();

        this.periodoFim = new Date();
        this.periodoInicio = new Date();
    }

    public RelatorioCadastroTransfer(RelatorioCadastroTransfer relatorioTransfer) {
        this.formato = relatorioTransfer.formato;

        this.periodoFim = relatorioTransfer.periodoFim;
        this.periodoInicio = relatorioTransfer.periodoInicio;
    }

    public FormatoTransfer getFormato() {
        return formato;
    }

    public void setFormato(FormatoTransfer formato) {
        this.formato = formato;
    }

    public Date getPeriodoInicio() {
        return periodoInicio;
    }

    public void setPeriodoInicio(Date periodoInicio) {
        this.periodoInicio = periodoInicio;
    }

    public Date getPeriodoFim() {
        return periodoFim;
    }

    public void setPeriodoFim(Date periodoFim) {
        this.periodoFim = periodoFim;
    }

    /**
     * @return the empresa
     */
    public Empresa getEmpresa() {
        return empresa;
    }

    /**
     * @param empresa the empresa to set
     */
    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    /**
     * @return the departamento
     */
    public Departamento getDepartamento() {
        return departamento;
    }

    /**
     * @param departamento the departamento to set
     */
    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    /**
     * @return the funcionario
     */
    public Funcionario getFuncionario() {
        return funcionario;
    }

    /**
     * @param funcionario the funcionario to set
     */
    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    /**
     * @return the empresaAtiva
     */
    public boolean isEmpresaAtiva() {
        return empresaAtiva;
    }

    /**
     * @param empresaAtiva the empresaAtiva to set
     */
    public void setEmpresaAtiva(boolean empresaAtiva) {
        this.empresaAtiva = empresaAtiva;
    }

    /**
     * @return the departamentoAtivo
     */
    public boolean isDepartamentoAtivo() {
        return departamentoAtivo;
    }

    /**
     * @param departamentoAtivo the departamentoAtivo to set
     */
    public void setDepartamentoAtivo(boolean departamentoAtivo) {
        this.departamentoAtivo = departamentoAtivo;
    }

    /**
     * @return the funcionarioAtivo
     */
    public boolean isFuncionarioAtivo() {
        return funcionarioAtivo;
    }

    /**
     * @param funcionarioAtivo the funcionarioAtivo to set
     */
    public void setFuncionarioAtivo(boolean funcionarioAtivo) {
        this.funcionarioAtivo = funcionarioAtivo;
    }

    /**
     * @return the operador
     */
    public Operador getOperador() {
        return operador;
    }

    /**
     * @param operador the operador to set
     */
    public void setOperador(Operador operador) {
        this.operador = operador;
    }
    
    
    

}
