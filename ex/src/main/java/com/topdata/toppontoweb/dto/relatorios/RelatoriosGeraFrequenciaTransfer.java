package com.topdata.toppontoweb.dto.relatorios;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.topdata.toppontoweb.dto.FormatoTransfer;
import com.topdata.toppontoweb.dto.gerafrequencia.IGeraFrequenciaTransfer;
import com.topdata.toppontoweb.entity.autenticacao.Operador;
import com.topdata.toppontoweb.entity.empresa.Departamento;
import com.topdata.toppontoweb.entity.empresa.Empresa;
import com.topdata.toppontoweb.entity.funcionario.Funcionario;
import com.topdata.toppontoweb.services.relatorios.RelatorioHandler;
import com.topdata.toppontoweb.services.relatorios.RelatorioHandler.TIPO_RELATORIO;
import java.io.Serializable;

/**
 * Objeto de entrada para o Gera frequencia
 *
 * @author juliano.ezequiel
 */
@JsonIgnoreProperties(value = "tipo_relatorio,mapAux", ignoreUnknown = true)
public class RelatoriosGeraFrequenciaTransfer implements IGeraFrequenciaTransfer, Serializable {

    private FormatoTransfer formato;
    private Date periodoInicio;
    private Date periodoFim;

    private Boolean exibirCamposZerados;
    private Boolean exibirTotalHorasJornada;

    private SubTipoRelatorio tipoRelatorio = new SubTipoRelatorio(RelatorioHandler.SUB_TIPO_RELATORIO.INDIVIDUAL);

    private Empresa empresa;
    private Departamento departamento;
    private Funcionario funcionario;

    private Boolean empresaAtiva = true;
    private Boolean departamentoAtivo = true;
    private Boolean funcionarioAtivo = true;

    public TIPO_RELATORIO tipo_relatorio;
    private boolean marcacoesInvalidas = true;
    private boolean retroativo = true;
    
    private Operador operador;

    public RelatoriosGeraFrequenciaTransfer() {

        this.formato = new FormatoTransfer();
        this.periodoFim = new Date();
        this.periodoInicio = new Date();

        this.empresa = null;
        this.departamento = null;
        this.funcionario = null;
    }

    public RelatoriosGeraFrequenciaTransfer(RelatoriosGeraFrequenciaTransfer relatorioTransfer) {
        this.formato = relatorioTransfer.formato;

        this.periodoFim = relatorioTransfer.periodoFim;
        this.periodoInicio = relatorioTransfer.periodoInicio;
    }
//
//    public List<Funcionario> getFuncionarioList() {
//        return funcionarioList;
//    }
//
//    public void setFuncionarioList(List<Funcionario> funcionarioList) {
//        this.funcionarioList = funcionarioList;
//    }

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

    public SubTipoRelatorio getTipoRelatorio() {
        return tipoRelatorio;
    }

    public void setTipoRelatorio(SubTipoRelatorio tipoRelatorio) {
        this.tipoRelatorio = tipoRelatorio;
    }

    public Boolean getExibirCamposZerados() {
        return exibirCamposZerados;
    }

    public void setExibirCamposZerados(Boolean exibirCamposZerados) {
        this.exibirCamposZerados = exibirCamposZerados;
    }

    public Boolean getExibirTotalHorasJornada() {
        return exibirTotalHorasJornada;
    }

    public void setExibirTotalHorasJornada(Boolean exibirTotalHorasJornada) {
        this.exibirTotalHorasJornada = exibirTotalHorasJornada;
    }

    public TIPO_RELATORIO getTipo_relatorio() {
        return tipo_relatorio;
    }

    public void setTipo_relatorio(TIPO_RELATORIO tipo_relatorio) {
        this.tipo_relatorio = tipo_relatorio;
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
    public Boolean isEmpresaAtiva() {
        return empresaAtiva;
    }

    /**
     * @param empresaAtiva the empresaAtiva to set
     */
    public void setEmpresaAtiva(Boolean empresaAtiva) {
        this.empresaAtiva = empresaAtiva;
    }

    /**
     * @return the departamentoAtivo
     */
    public Boolean isDepartamentoAtivo() {
        return departamentoAtivo;
    }

    /**
     * @param departamentoAtivo the departamentoAtivo to set
     */
    public void setDepartamentoAtivo(Boolean departamentoAtivo) {
        this.departamentoAtivo = departamentoAtivo;
    }

    /**
     * @return the funcionarioAtivo
     */
    public Boolean isFuncionarioAtivo() {
        return funcionarioAtivo;
    }

    /**
     * @param funcionarioAtivo the funcionarioAtivo to set
     */
    public void setFuncionarioAtivo(Boolean funcionarioAtivo) {
        this.funcionarioAtivo = funcionarioAtivo;
    }

    public void setMarcacoesInvalidas(boolean marcacoesInvalidas) {
        this.marcacoesInvalidas = marcacoesInvalidas;
    }

    public boolean isMarcacoesInvalidas() {
        return marcacoesInvalidas;
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

    /**
     * @return the retroativo
     */
    public boolean isRetroativo() {
        return retroativo;
    }

    /**
     * @param retroativo the retroativo to set
     */
    public void setRetroativo(boolean retroativo) {
        this.retroativo = retroativo;
    }
    
    
    

}
