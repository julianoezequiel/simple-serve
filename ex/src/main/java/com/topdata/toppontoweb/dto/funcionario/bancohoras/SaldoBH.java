package com.topdata.toppontoweb.dto.funcionario.bancohoras;

import java.util.Date;

import com.topdata.toppontoweb.entity.funcionario.Funcionario;
import com.topdata.toppontoweb.utils.Utils;

/**
 * @version 1.0.0 data 20/06/2017
 * @since 1.0.0 data 20/06/2017
 *
 * @author juliano.ezequiel
 */
public class SaldoBH {

    private Date dataConsulta;
    private Date credito;
    private Date debito;
    private Funcionario funcionario;
    private Date saldo;

    private String creditoConv;
    private String debitoConv;
    private String saldoConv;

    public SaldoBH() {
    }

    public SaldoBH(Date dataConsulta, Date credito, Date debito, Date saldo) {
        this.dataConsulta = dataConsulta;
        this.credito = credito;
        this.debito = debito;
        this.saldo = saldo;
        this.creditoConv = Utils.MASK.DATA(saldo);
    }

    public Date getDataConsulta() {
        return dataConsulta;
    }

    public void setDataConsulta(Date dataConsulta) {
        this.dataConsulta = dataConsulta;
    }

    public Date getCredito() {
        return credito;
    }

    public void setCredito(Date credito) {
        this.credito = credito;
    }

    public Date getDebito() {
        return debito;
    }

    public void setDebito(Date debito) {
        this.debito = debito;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public Date getSaldo() {
        return saldo;
    }

    public void setSaldo(Date saldo) {
        this.saldo = saldo;
    }

    public void setCreditoConv(String creditoConv) {
        this.creditoConv = creditoConv;
    }

    public void setDebitoConv(String debitoConv) {
        this.debitoConv = debitoConv;
    }

    public void setSaldoConv(String saldoConv) {
        this.saldoConv = saldoConv;
    }

    public String getCreditoConv() {
        return creditoConv;
    }

    public String getDebitoConv() {
        return debitoConv;
    }

    public String getSaldoConv() {
        return saldoConv;
    }

}
