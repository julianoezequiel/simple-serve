package com.topdata.toppontoweb.dto.funcionario.bancohoras;

import com.topdata.toppontoweb.entity.funcionario.Funcionario;
import com.topdata.toppontoweb.utils.Utils;
import java.util.Date;

/**
 * @version 1.0.0 data 29/06/2017
 * @since 1.0.0 data 29/06/2017
 *
 * @author juliano.ezequiel
 */
public class Gatilho {

    private Boolean manual;
    private Date positivo;
    private Date negativo;
    private Date dataConsulta;
    private Funcionario funcionario;

    private String creditoConv;
    private String debitoConv;

    public Gatilho() {
    }

    public Gatilho(Boolean manual, Date credito, Date debito) {
        this.manual = manual;
        this.positivo = credito;
        this.negativo = debito;
        this.creditoConv = Utils.horasInt(credito);
        this.debitoConv = Utils.horasInt(debito);
    }

    public Date getPositivo() {
        return positivo;
    }

    public Date getNegativo() {
        return negativo;
    }

    public boolean isManual() {
        return manual;
    }

    public void setCreditoConv(String creditoConv) {
        this.creditoConv = creditoConv;
    }

    public void setDebitoConv(String debitoConv) {
        this.debitoConv = debitoConv;
    }

    public String getCreditoConv() {
        return creditoConv;
    }

    public String getDebitoConv() {
        return debitoConv;
    }

    public Date getDataConsulta() {
        return dataConsulta;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public void setDataConsulta(Date dataConsulta) {
        this.dataConsulta = dataConsulta;
    }

    public void setManual(Boolean manual) {
        this.manual = manual;
    }

    public void setNegativo(Date negativo) {
        this.negativo = negativo;
    }

    public void setPositivo(Date positivo) {
        this.positivo = positivo;
    }

}
