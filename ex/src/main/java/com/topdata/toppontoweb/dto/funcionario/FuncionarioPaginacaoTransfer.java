package com.topdata.toppontoweb.dto.funcionario;

import com.topdata.toppontoweb.dto.PaginacaoTransfer;
import com.topdata.toppontoweb.entity.empresa.Departamento;
import com.topdata.toppontoweb.entity.empresa.Empresa;
import com.topdata.toppontoweb.entity.funcionario.Funcionario;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 *
 * @author tharle.camargo
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FuncionarioPaginacaoTransfer extends PaginacaoTransfer{
    private Empresa empresa;
    private Departamento departamento;
    private Funcionario funcionario;
    
    private boolean empresaAtiva;
    private boolean departamentoAtivo;
    private boolean funcionarioAtivo;

    public FuncionarioPaginacaoTransfer(Empresa empresa, Departamento departamento, Funcionario funcionario, boolean empresaAtiva, boolean departamentoAtivo, boolean funcionarioAtivo) {
        this.empresa = empresa;
        this.departamento = departamento;
        this.funcionario = funcionario;
        this.empresaAtiva = empresaAtiva;
        this.departamentoAtivo = departamentoAtivo;
        this.funcionarioAtivo = funcionarioAtivo;
    }

    public FuncionarioPaginacaoTransfer() {
        this.empresa = null;
        this.departamento = null;
        this.funcionario = null;
        this.empresaAtiva = true;
        this.departamentoAtivo = true;
        this.funcionarioAtivo = true;
        
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
    
    
    
    
    
}
