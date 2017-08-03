package com.topdata.toppontoweb.dto;

import java.util.ArrayList;
import java.util.List;

import com.topdata.toppontoweb.entity.empresa.Departamento;
import com.topdata.toppontoweb.entity.empresa.Empresa;
import com.topdata.toppontoweb.entity.funcionario.Afastamento;
import com.topdata.toppontoweb.entity.funcionario.Coletivo;
import com.topdata.toppontoweb.entity.funcionario.Compensacao;
import com.topdata.toppontoweb.entity.funcionario.Funcionario;
import com.topdata.toppontoweb.entity.funcionario.FuncionarioBancoHoras;
import com.topdata.toppontoweb.entity.funcionario.FuncionarioBancoHorasFechamento;
import com.topdata.toppontoweb.entity.funcionario.FuncionarioCalendario;
import com.topdata.toppontoweb.entity.funcionario.FuncionarioJornada;

/**
 * Classe DTO para lançameto coletivo
 *
 * @version 1.0.0 data 21/12/2016
 * @since 1.0.0 data 21/12/2016
 *
 * @author juliano.ezequiel
 */
public class ColetivoTransfer {

    private List<Afastamento> afastamentoList;
    private List<FuncionarioCalendario> funcionarioCalendarioList;
    private List<FuncionarioJornada> funcionarioJornadaList;
    private List<Compensacao> compensacaoList;
    private List<FuncionarioBancoHoras> funcionarioBancoHorasList;
    private List<FuncionarioBancoHorasFechamento> funcionarioBancoHorasFechamentoList;
    private List<ColetivoResultado> coletivoResultadoList;

    private Coletivo coletivo;
    private List<Funcionario> funcionarioList;
    private List<Empresa> empresaList;
    private List<Departamento> departamentoList;

    public ColetivoTransfer() {
    }

    public ColetivoTransfer(Coletivo coletivo) {
        this.afastamentoList = new ArrayList<>();
        this.funcionarioBancoHorasFechamentoList = new ArrayList<>();
        this.funcionarioBancoHorasList = new ArrayList<>();
        this.compensacaoList = new ArrayList<>();
        this.funcionarioCalendarioList = new ArrayList<>();
        this.funcionarioJornadaList = new ArrayList<>();
        this.coletivoResultadoList = new ArrayList<>();
        this.coletivo = coletivo;
    }

    public Coletivo getColetivo() {
        return coletivo;
    }

    public void setColetivo(Coletivo coletivo) {
        this.coletivo = coletivo;
    }

    public List<Departamento> getDepartamentoList() {
        return departamentoList;
    }

    public List<Empresa> getEmpresaList() {
        return empresaList;
    }

    public List<Funcionario> getFuncionarioList() {
        return funcionarioList;
    }

    public void setDepartamentoList(List<Departamento> departamentoList) {
//        departamentoList.stream().forEach(d -> {
//            d.setFuncionarioList(null);
//            d.setEmpresa(null);
//        });
        this.departamentoList = departamentoList;
    }

    public void setEmpresaList(List<Empresa> empresaList) {
//        empresaList.stream().forEach((Empresa e) -> {
//            Empresa e1 = new Empresa();
//            e1.setRazaoSocial(e.getRazaoSocial());
//            e1.setIdEmpresa(e.getIdEmpresa());
//            e1.setEditavel(e.getEditavel());
//            e = e1;
//        });
        this.empresaList = empresaList;
    }

    public void setFuncionarioList(List<Funcionario> funcionarioList) {
//        funcionarioList.stream().forEach(f -> {
//            Funcionario funcionario = new Funcionario();
//            funcionario.setIdFuncionario(f.getIdFuncionario());
//            funcionario.setNome(f.getNome());
//            f = funcionario;
//        });
        this.funcionarioList = funcionarioList;
    }

    public List<Afastamento> getAfastamentoList() {
        return afastamentoList;
    }

    public void setAfastamentoList(List<Afastamento> afastamentoList) {
        this.afastamentoList = afastamentoList;
    }

    public List<FuncionarioCalendario> getFuncionarioCalendarioList() {
        return funcionarioCalendarioList;
    }

    public void setFuncionarioCalendarioList(List<FuncionarioCalendario> funcionarioCalendarioList) {
        this.funcionarioCalendarioList = funcionarioCalendarioList;
    }

    public List<FuncionarioJornada> getFuncionarioJornadaList() {
        return funcionarioJornadaList;
    }

    public void setFuncionarioJornadaList(List<FuncionarioJornada> funcionarioJornadaList) {
        this.funcionarioJornadaList = funcionarioJornadaList;
    }

    public List<Compensacao> getCompensacaoList() {
        return compensacaoList;
    }

    public void setCompensacaoList(List<Compensacao> compensacaoList) {
        this.compensacaoList = compensacaoList;
    }

    public List<FuncionarioBancoHoras> getFuncionarioBancoHorasList() {
        return funcionarioBancoHorasList;
    }

    public void setFuncionarioBancoHorasList(List<FuncionarioBancoHoras> funcionarioBancoHorasList) {
        this.funcionarioBancoHorasList = funcionarioBancoHorasList;
    }

    public List<FuncionarioBancoHorasFechamento> getFuncionarioBancoHorasFechamentoList() {
        return funcionarioBancoHorasFechamentoList;
    }

    public void setFuncionarioBancoHorasFechamentoList(List<FuncionarioBancoHorasFechamento> funcionarioBancoHorasFechamentoList) {
        this.funcionarioBancoHorasFechamentoList = funcionarioBancoHorasFechamentoList;
    }

    public List<ColetivoResultado> getColetivoResultadoList() {
        return coletivoResultadoList;
    }

    public void setColetivoResultadoList(List<ColetivoResultado> coletivoResultadoList) {
        this.coletivoResultadoList = coletivoResultadoList;
    }

}
