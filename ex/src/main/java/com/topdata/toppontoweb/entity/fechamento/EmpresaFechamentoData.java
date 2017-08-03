package com.topdata.toppontoweb.entity.fechamento;

import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.topdata.toppontoweb.entity.Entidade;
import com.topdata.toppontoweb.entity.funcionario.Funcionario;
import com.topdata.toppontoweb.entity.funcionario.FuncionarioBancoHoras;
import javax.persistence.FetchType;

/**
 *
 * @author juliano.ezequiel
 */
@Entity
@Table(name = "EmpresaFechamentoData")
@XmlRootElement
public class EmpresaFechamentoData implements Entidade {

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idEmpresaFechamentoData")
    @SequenceGenerator(name = "seq", initialValue = 1)
    @GeneratedValue(generator = "seq", strategy = GenerationType.IDENTITY)
    private Integer idEmpresaFechamentoData;

    @Column(name = "Data")
    @Temporal(TemporalType.DATE)
    private Date data;

    @Column(name = "PossuiHorasPrevistas")
    private Boolean possuiHorasPrevistas;

    @Column(name = "HorasPrevistasDiurnas")
    @Temporal(TemporalType.TIMESTAMP)
    private Date horasPrevistasDiurnas;

    @Column(name = "HorasPrevistasNoturnas")
    @Temporal(TemporalType.TIMESTAMP)
    private Date horasPrevistasNoturnas;

    @Column(name = "HorasPrevistasDiferencaAdicionalNoturno")
    @Temporal(TemporalType.TIMESTAMP)
    private Date horasPrevistasDiferencaAdicionalNoturno;

    @Column(name = "PossuiHorasTrabalhadas")
    private Boolean possuiHorasTrabalhadas;

    @Column(name = "HorasTrabalhadasDiurnas")
    @Temporal(TemporalType.TIMESTAMP)
    private Date horasTrabalhadasDiurnas;

    @Column(name = "HorasTrabalhadasNoturnas")
    @Temporal(TemporalType.TIMESTAMP)
    private Date horasTrabalhadasNoturnas;

    @Column(name = "HorasTrabalhadasDiferencaAdicionalNoturno")
    @Temporal(TemporalType.TIMESTAMP)
    private Date horasTrabalhadasDiferencaAdicionalNoturno;

    @Column(name = "PossuiNormais")
    private Boolean possuiNormais;

    @Column(name = "PossuiExtras")
    private Boolean possuiExtras;

    @Column(name = "PossuiTodasComoExtras")
    private Boolean possuiTodasComoExtras;

    @Column(name = "PossuiExtrasCompensacao")
    private Boolean possuiExtrasCompensacao;

    @Column(name = "PossuiAusencias")
    private Boolean possuiAusencias;

    @Column(name = "PossuiAusenciasCompensadas")
    private Boolean possuiAusenciasCompensadas;

    @Column(name = "PossuiAusenciasAbonadas")
    private Boolean possuiAusenciasAbonadas;

    @Column(name = "PossuiAfastadoComAbono")
    private Boolean possuiAfastadoComAbono;

    @Column(name = "SaldoNormaisDiurnas")
    @Temporal(TemporalType.TIMESTAMP)
    private Date saldoNormaisDiurnas;

    @Column(name = "SaldoNormaisNoturnas")
    @Temporal(TemporalType.TIMESTAMP)
    private Date saldoNormaisNoturnas;

    @Column(name = "SaldoExtrasDiurnas")
    @Temporal(TemporalType.TIMESTAMP)
    private Date saldoExtrasDiurnas;

    @Column(name = "SaldoExtrasNoturnas")
    @Temporal(TemporalType.TIMESTAMP)
    private Date saldoExtrasNoturnas;

    @Column(name = "SaldoExtrasDiferencaAdicionalNoturno")
    @Temporal(TemporalType.TIMESTAMP)
    private Date saldoExtrasDiferencaAdicionalNoturno;

    @Column(name = "SaldoExtrasCompensacaoDiurnas")
    @Temporal(TemporalType.TIMESTAMP)
    private Date saldoExtrasCompensacaoDiurnas;

    @Column(name = "SaldoExtrasCompensacaoNoturnas")
    @Temporal(TemporalType.TIMESTAMP)
    private Date saldoExtrasCompensacaoNoturnas;

    @Column(name = "SaldoAusenciasDiurnas")
    @Temporal(TemporalType.TIMESTAMP)
    private Date saldoAusenciasDiurnas;

    @Column(name = "SaldoAusenciasNoturnas")
    @Temporal(TemporalType.TIMESTAMP)
    private Date saldoAusenciasNoturnas;

    @Column(name = "SaldoAusenciasCompensadasDiurnas")
    @Temporal(TemporalType.TIMESTAMP)
    private Date saldoAusenciasCompensadasDiurnas;

    @Column(name = "SaldoAusenciasCompensadasNoturnas")
    @Temporal(TemporalType.TIMESTAMP)
    private Date saldoAusenciasCompensadasNoturnas;

    @Column(name = "SaldoAusenciasAbonadasDiurnas")
    @Temporal(TemporalType.TIMESTAMP)
    private Date saldoAusenciasAbonadasDiurnas;

    @Column(name = "SaldoAusenciasAbonadasNoturnas")
    @Temporal(TemporalType.TIMESTAMP)
    private Date saldoAusenciasAbonadasNoturnas;

    @Column(name = "BHDiferencaAdicionalNoturno")
    @Temporal(TemporalType.TIMESTAMP)
    private Date bHDiferencaAdicionalNoturno;

    @Column(name = "BHSaldoAcumuladoDiaAnterior")
    @Temporal(TemporalType.TIMESTAMP)
    private Date bHSaldoAcumuladoDiaAnterior;

    @Column(name = "BHCredito")
    @Temporal(TemporalType.TIMESTAMP)
    private Date bHCredito;

    @Column(name = "BHDebito")
    @Temporal(TemporalType.TIMESTAMP)
    private Date bHDebito;

    @Column(name = "BHSaldoDia")
    @Temporal(TemporalType.TIMESTAMP)
    private Date bHSaldoDia;

    @Column(name = "BHSaldoCredito")
    @Temporal(TemporalType.TIMESTAMP)
    private Date bHSaldoCredito;

    @Column(name = "BHSaldoDebito")
    @Temporal(TemporalType.TIMESTAMP)
    private Date bHSaldoDebito;

    @Column(name = "BHGatilho")
    @Temporal(TemporalType.TIMESTAMP)
    private Date bHGatilho;

    @Column(name = "BHSaldoAcumuladoDia")
    @Temporal(TemporalType.TIMESTAMP)
    private Date bHSaldoAcumuladoDia;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idEmpresaFechamentoData", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<FechamentoTabelaExtrasDSR> fechamentoTabelaExtrasDSRList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idEmpresaFechamentoData", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<FechamentoTabelaExtras> fechamentoTabelaExtrasList;

    @JoinColumn(name = "idEmpresaFechamentoPeriodo", referencedColumnName = "idEmpresaFechamentoPeriodo")
    @ManyToOne(optional = false)
    private EmpresaFechamentoPeriodo idEmpresaFechamentoPeriodo;

    @JoinColumn(name = "IdFuncionario", referencedColumnName = "IdFuncionario")
    @ManyToOne(optional = false)
    private Funcionario idFuncionario;

    @JoinColumn(name = "idFuncionarioBancoHoras", referencedColumnName = "idFuncionarioBancoHoras")
    @ManyToOne
    private FuncionarioBancoHoras idFuncionarioBancoHoras;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idEmpresaFechamentoData", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<FechamentoBHTabelaAcrescimos> fechamentoBHTabelaAcrescimosList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idEmpresaFechamentoData", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<FechamentoBHTabelaSomenteNoturnas> fechamentoBHTabelaSomenteNoturnasList;

    public EmpresaFechamentoData() {
    }

    public EmpresaFechamentoData(Integer idEmpresaFechamentoData) {
        this.idEmpresaFechamentoData = idEmpresaFechamentoData;
    }

    public Integer getIdEmpresaFechamentoData() {
        return idEmpresaFechamentoData;
    }

    public void setIdEmpresaFechamentoData(Integer idEmpresaFechamentoData) {
        this.idEmpresaFechamentoData = idEmpresaFechamentoData;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Boolean getPossuiHorasPrevistas() {
        return possuiHorasPrevistas;
    }

    public void setPossuiHorasPrevistas(Boolean possuiHorasPrevistas) {
        this.possuiHorasPrevistas = possuiHorasPrevistas;
    }

    public Date getHorasPrevistasDiurnas() {
        return horasPrevistasDiurnas;
    }

    public void setHorasPrevistasDiurnas(Date horasPrevistasDiurnas) {
        this.horasPrevistasDiurnas = horasPrevistasDiurnas;
    }

    public Date getHorasPrevistasNoturnas() {
        return horasPrevistasNoturnas;
    }

    public void setHorasPrevistasNoturnas(Date horasPrevistasNoturnas) {
        this.horasPrevistasNoturnas = horasPrevistasNoturnas;
    }

    public Date getHorasPrevistasDiferencaAdicionalNoturno() {
        return horasPrevistasDiferencaAdicionalNoturno;
    }

    public void setHorasPrevistasDiferencaAdicionalNoturno(Date horasPrevistasDiferencaAdicionalNoturno) {
        this.horasPrevistasDiferencaAdicionalNoturno = horasPrevistasDiferencaAdicionalNoturno;
    }

    public Boolean getPossuiHorasTrabalhadas() {
        return possuiHorasTrabalhadas;
    }

    public void setPossuiHorasTrabalhadas(Boolean possuiHorasTrabalhadas) {
        this.possuiHorasTrabalhadas = possuiHorasTrabalhadas;
    }

    public Date getHorasTrabalhadasDiurnas() {
        return horasTrabalhadasDiurnas;
    }

    public void setHorasTrabalhadasDiurnas(Date horasTrabalhadasDiurnas) {
        this.horasTrabalhadasDiurnas = horasTrabalhadasDiurnas;
    }

    public Date getHorasTrabalhadasNoturnas() {
        return horasTrabalhadasNoturnas;
    }

    public void setHorasTrabalhadasNoturnas(Date horasTrabalhadasNoturnas) {
        this.horasTrabalhadasNoturnas = horasTrabalhadasNoturnas;
    }

    public Date getHorasTrabalhadasDiferencaAdicionalNoturno() {
        return horasTrabalhadasDiferencaAdicionalNoturno;
    }

    public void setHorasTrabalhadasDiferencaAdicionalNoturno(Date horasTrabalhadasDiferencaAdicionalNoturno) {
        this.horasTrabalhadasDiferencaAdicionalNoturno = horasTrabalhadasDiferencaAdicionalNoturno;
    }

    public Boolean getPossuiNormais() {
        return possuiNormais;
    }

    public void setPossuiNormais(Boolean possuiNormais) {
        this.possuiNormais = possuiNormais;
    }

    public Boolean getPossuiExtras() {
        return possuiExtras;
    }

    public void setPossuiExtras(Boolean possuiExtras) {
        this.possuiExtras = possuiExtras;
    }

    public Boolean getPossuiTodasComoExtras() {
        return possuiTodasComoExtras;
    }

    public void setPossuiTodasComoExtras(Boolean possuiTodasComoExtras) {
        this.possuiTodasComoExtras = possuiTodasComoExtras;
    }

    public Boolean getPossuiExtrasCompensacao() {
        return possuiExtrasCompensacao;
    }

    public void setPossuiExtrasCompensacao(Boolean possuiExtrasCompensacao) {
        this.possuiExtrasCompensacao = possuiExtrasCompensacao;
    }

    public Boolean getPossuiAusencias() {
        return possuiAusencias;
    }

    public void setPossuiAusencias(Boolean possuiAusencias) {
        this.possuiAusencias = possuiAusencias;
    }

    public Boolean getPossuiAusenciasCompensadas() {
        return possuiAusenciasCompensadas;
    }

    public void setPossuiAusenciasCompensadas(Boolean possuiAusenciasCompensadas) {
        this.possuiAusenciasCompensadas = possuiAusenciasCompensadas;
    }

    public Boolean getPossuiAusenciasAbonadas() {
        return possuiAusenciasAbonadas;
    }

    public void setPossuiAusenciasAbonadas(Boolean possuiAusenciasAbonadas) {
        this.possuiAusenciasAbonadas = possuiAusenciasAbonadas;
    }

    public Boolean getPossuiAfastadoComAbono() {
        return possuiAfastadoComAbono;
    }

    public void setPossuiAfastadoComAbono(Boolean possuiAfastadoComAbono) {
        this.possuiAfastadoComAbono = possuiAfastadoComAbono;
    }

    public Date getSaldoNormaisDiurnas() {
        return saldoNormaisDiurnas;
    }

    public void setSaldoNormaisDiurnas(Date saldoNormaisDiurnas) {
        this.saldoNormaisDiurnas = saldoNormaisDiurnas;
    }

    public Date getSaldoNormaisNoturnas() {
        return saldoNormaisNoturnas;
    }

    public void setSaldoNormaisNoturnas(Date saldoNormaisNoturnas) {
        this.saldoNormaisNoturnas = saldoNormaisNoturnas;
    }

    public Date getSaldoExtrasDiurnas() {
        return saldoExtrasDiurnas;
    }

    public void setSaldoExtrasDiurnas(Date saldoExtrasDiurnas) {
        this.saldoExtrasDiurnas = saldoExtrasDiurnas;
    }

    public Date getSaldoExtrasNoturnas() {
        return saldoExtrasNoturnas;
    }

    public void setSaldoExtrasNoturnas(Date saldoExtrasNoturnas) {
        this.saldoExtrasNoturnas = saldoExtrasNoturnas;
    }

    public Date getSaldoExtrasDiferencaAdicionalNoturno() {
        return saldoExtrasDiferencaAdicionalNoturno;
    }

    public void setSaldoExtrasDiferencaAdicionalNoturno(Date saldoExtrasDiferencaAdicionalNoturno) {
        this.saldoExtrasDiferencaAdicionalNoturno = saldoExtrasDiferencaAdicionalNoturno;
    }

    public Date getSaldoExtrasCompensacaoDiurnas() {
        return saldoExtrasCompensacaoDiurnas;
    }

    public void setSaldoExtrasCompensacaoDiurnas(Date saldoExtrasCompensacaoDiurnas) {
        this.saldoExtrasCompensacaoDiurnas = saldoExtrasCompensacaoDiurnas;
    }

    public Date getSaldoExtrasCompensacaoNoturnas() {
        return saldoExtrasCompensacaoNoturnas;
    }

    public void setSaldoExtrasCompensacaoNoturnas(Date saldoExtrasCompensacaoNoturnas) {
        this.saldoExtrasCompensacaoNoturnas = saldoExtrasCompensacaoNoturnas;
    }

    public Date getSaldoAusenciasDiurnas() {
        return saldoAusenciasDiurnas;
    }

    public void setSaldoAusenciasDiurnas(Date saldoAusenciasDiurnas) {
        this.saldoAusenciasDiurnas = saldoAusenciasDiurnas;
    }

    public Date getSaldoAusenciasNoturnas() {
        return saldoAusenciasNoturnas;
    }

    public void setSaldoAusenciasNoturnas(Date saldoAusenciasNoturnas) {
        this.saldoAusenciasNoturnas = saldoAusenciasNoturnas;
    }

    public Date getSaldoAusenciasCompensadasDiurnas() {
        return saldoAusenciasCompensadasDiurnas;
    }

    public void setSaldoAusenciasCompensadasDiurnas(Date saldoAusenciasCompensadasDiurnas) {
        this.saldoAusenciasCompensadasDiurnas = saldoAusenciasCompensadasDiurnas;
    }

    public Date getSaldoAusenciasCompensadasNoturnas() {
        return saldoAusenciasCompensadasNoturnas;
    }

    public void setSaldoAusenciasCompensadasNoturnas(Date saldoAusenciasCompensadasNoturnas) {
        this.saldoAusenciasCompensadasNoturnas = saldoAusenciasCompensadasNoturnas;
    }

    public Date getSaldoAusenciasAbonadasDiurnas() {
        return saldoAusenciasAbonadasDiurnas;
    }

    public void setSaldoAusenciasAbonadasDiurnas(Date saldoAusenciasAbonadasDiurnas) {
        this.saldoAusenciasAbonadasDiurnas = saldoAusenciasAbonadasDiurnas;
    }

    public Date getSaldoAusenciasAbonadasNoturnas() {
        return saldoAusenciasAbonadasNoturnas;
    }

    public void setSaldoAusenciasAbonadasNoturnas(Date saldoAusenciasAbonadasNoturnas) {
        this.saldoAusenciasAbonadasNoturnas = saldoAusenciasAbonadasNoturnas;
    }

    public Date getBHDiferencaAdicionalNoturno() {
        return bHDiferencaAdicionalNoturno;
    }

    public void setBHDiferencaAdicionalNoturno(Date bHDiferencaAdicionalNoturno) {
        this.bHDiferencaAdicionalNoturno = bHDiferencaAdicionalNoturno;
    }

    public Date getBHSaldoAcumuladoDiaAnterior() {
        return bHSaldoAcumuladoDiaAnterior;
    }

    public void setBHSaldoAcumuladoDiaAnterior(Date bHSaldoAcumuladoDiaAnterior) {
        this.bHSaldoAcumuladoDiaAnterior = bHSaldoAcumuladoDiaAnterior;
    }

    public Date getBHCredito() {
        return bHCredito;
    }

    public void setBHCredito(Date bHCredito) {
        this.bHCredito = bHCredito;
    }

    public Date getBHDebito() {
        return bHDebito;
    }

    public void setBHDebito(Date bHDebito) {
        this.bHDebito = bHDebito;
    }

    public Date getBHSaldoDia() {
        return bHSaldoDia;
    }

    public void setBHSaldoDia(Date bHSaldoDia) {
        this.bHSaldoDia = bHSaldoDia;
    }

    public Date getBHSaldoCredito() {
        return bHSaldoCredito;
    }

    public void setBHSaldoCredito(Date bHSaldoCredito) {
        this.bHSaldoCredito = bHSaldoCredito;
    }

    public Date getBHSaldoDebito() {
        return bHSaldoDebito;
    }

    public void setBHSaldoDebito(Date bHSaldoDebito) {
        this.bHSaldoDebito = bHSaldoDebito;
    }

    public Date getBHGatilho() {
        return bHGatilho;
    }

    public void setBHGatilho(Date bHGatilho) {
        this.bHGatilho = bHGatilho;
    }

    public Date getBHSaldoAcumuladoDia() {
        return bHSaldoAcumuladoDia;
    }

    public void setBHSaldoAcumuladoDia(Date bHSaldoAcumuladoDia) {
        this.bHSaldoAcumuladoDia = bHSaldoAcumuladoDia;
    }

    @XmlTransient
    @JsonIgnore
    public List<FechamentoTabelaExtrasDSR> getFechamentoTabelaExtrasDSRList() {
        return fechamentoTabelaExtrasDSRList;
    }

    public void setFechamentoTabelaExtrasDSRList(List<FechamentoTabelaExtrasDSR> fechamentoTabelaExtrasDSRList) {
        this.fechamentoTabelaExtrasDSRList = fechamentoTabelaExtrasDSRList;
    }

    @XmlTransient
    @JsonIgnore
    public List<FechamentoTabelaExtras> getFechamentoTabelaExtrasList() {
        return fechamentoTabelaExtrasList;
    }

    public void setFechamentoTabelaExtrasList(List<FechamentoTabelaExtras> fechamentoTabelaExtrasList) {
        this.fechamentoTabelaExtrasList = fechamentoTabelaExtrasList;
    }

    public EmpresaFechamentoPeriodo getIdEmpresaFechamentoPeriodo() {
        return idEmpresaFechamentoPeriodo;
    }

    public void setIdEmpresaFechamentoPeriodo(EmpresaFechamentoPeriodo idEmpresaFechamentoPeriodo) {
        this.idEmpresaFechamentoPeriodo = idEmpresaFechamentoPeriodo;
    }

    public Funcionario getIdFuncionario() {
        return idFuncionario;
    }

    public void setIdFuncionario(Funcionario idFuncionario) {
        this.idFuncionario = idFuncionario;
    }

    public FuncionarioBancoHoras getIdFuncionarioBancoHoras() {
        return idFuncionarioBancoHoras;
    }

    public void setIdFuncionarioBancoHoras(FuncionarioBancoHoras idFuncionarioBancoHoras) {
        this.idFuncionarioBancoHoras = idFuncionarioBancoHoras;
    }

    @XmlTransient
    @JsonIgnore
    public List<FechamentoBHTabelaAcrescimos> getFechamentoBHTabelaAcrescimosList() {
        return fechamentoBHTabelaAcrescimosList;
    }

    public void setFechamentoBHTabelaAcrescimosList(List<FechamentoBHTabelaAcrescimos> fechamentoBHTabelaAcrescimosList) {
        this.fechamentoBHTabelaAcrescimosList = fechamentoBHTabelaAcrescimosList;
    }

    @XmlTransient
    @JsonIgnore
    public List<FechamentoBHTabelaSomenteNoturnas> getFechamentoBHTabelaSomenteNoturnasList() {
        return fechamentoBHTabelaSomenteNoturnasList;
    }

    public void setFechamentoBHTabelaSomenteNoturnasList(List<FechamentoBHTabelaSomenteNoturnas> fechamentoBHTabelaSomenteNoturnasList) {
        this.fechamentoBHTabelaSomenteNoturnasList = fechamentoBHTabelaSomenteNoturnasList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEmpresaFechamentoData != null ? idEmpresaFechamentoData.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EmpresaFechamentoData)) {
            return false;
        }
        EmpresaFechamentoData other = (EmpresaFechamentoData) object;
        return !((this.idEmpresaFechamentoData == null && other.idEmpresaFechamentoData != null) || (this.idEmpresaFechamentoData != null && !this.idEmpresaFechamentoData.equals(other.idEmpresaFechamentoData)));
    }

    @Override
    public String toString() {
        String id = this.idEmpresaFechamentoData != null ? this.idEmpresaFechamentoData.toString() : "";
        return "Empresa fechamento data " + id;
    }

}
