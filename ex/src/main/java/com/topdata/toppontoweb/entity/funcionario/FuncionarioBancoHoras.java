package com.topdata.toppontoweb.entity.funcionario;

import java.util.ArrayList;
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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.topdata.toppontoweb.entity.Entidade;
import com.topdata.toppontoweb.entity.bancohoras.BancoHoras;
import com.topdata.toppontoweb.entity.fechamento.EmpresaFechamentoData;
import com.topdata.toppontoweb.utils.CustomDateSerializer;

/**
 * @version 1.0.0 data 26/06/2017
 * @since 1.0.0 data 26/06/2016
 *
 * @author juliano.ezequiel
 */
@Entity
@Table(name = "FuncionarioBancoHoras")
@XmlRootElement
public class FuncionarioBancoHoras implements Entidade, Cloneable {

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idFuncionarioBancoHoras")
    @SequenceGenerator(name = "seq_usu", initialValue = 1)
    @GeneratedValue(generator = "seq_usu", strategy = GenerationType.IDENTITY)
    private Integer idFuncionarioBancoHoras;

    @Basic(optional = false)
    @NotNull
    @Column(name = "DataInicio")
    @JsonSerialize(using = CustomDateSerializer.class)
    @Temporal(TemporalType.DATE)
    private Date dataInicio;

    @Column(name = "DataFim")
    @JsonSerialize(using = CustomDateSerializer.class)
    @Temporal(TemporalType.DATE)
    private Date dataFim;

    @JoinColumn(name = "IdBancoHoras", referencedColumnName = "IdBancoHoras")
    @ManyToOne(optional = false, cascade = CascadeType.REFRESH)
    private BancoHoras bancoHoras;

    @JoinColumn(name = "idColetivo", referencedColumnName = "IdColetivo")
    @ManyToOne
    private Coletivo coletivo;

    @JoinColumn(name = "IdFuncionario", referencedColumnName = "IdFuncionario")
    @ManyToOne(optional = false, cascade = CascadeType.REFRESH)
    private Funcionario funcionario;

    @OneToMany(mappedBy = "idFuncionarioBancoHoras", cascade = CascadeType.ALL)
    private List<EmpresaFechamentoData> empresaFechamentoDataList;

    @OneToMany(mappedBy = "funcionarioBancoHoras", cascade = {CascadeType.REFRESH, CascadeType.ALL})
    private List<FuncionarioBancoHorasFechamento> funcionarioBancoHorasFechamentoList = new ArrayList<>();

    public FuncionarioBancoHoras() {
    }

    public FuncionarioBancoHoras(Integer idFuncionarioBancoHoras) {
        this.idFuncionarioBancoHoras = idFuncionarioBancoHoras;
    }

    public FuncionarioBancoHoras(Integer idFuncionarioBancoHoras, Date dataInicio) {
        this.idFuncionarioBancoHoras = idFuncionarioBancoHoras;
        this.dataInicio = dataInicio;
    }

    public Integer getIdFuncionarioBancoHoras() {
        return idFuncionarioBancoHoras;
    }

    public void setIdFuncionarioBancoHoras(Integer idFuncionarioBancoHoras) {
        this.idFuncionarioBancoHoras = idFuncionarioBancoHoras;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getDataFim() {
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    public BancoHoras getBancoHoras() {
        return bancoHoras;
    }

    public void setBancoHoras(BancoHoras bancoHoras) {
        this.bancoHoras = bancoHoras;
    }

    public Coletivo getColetivo() {
        return coletivo;
    }

    public void setColetivo(Coletivo coletivo) {
        this.coletivo = coletivo;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    @XmlTransient
    @JsonIgnore
    public List<EmpresaFechamentoData> getEmpresaFechamentoDataList() {
        return empresaFechamentoDataList;
    }

    public void setEmpresaFechamentoDataList(List<EmpresaFechamentoData> empresaFechamentoDataList) {
        this.empresaFechamentoDataList = empresaFechamentoDataList;
    }

    @XmlTransient
    @JsonIgnore
    public List<FuncionarioBancoHorasFechamento> getFuncionarioBancoHorasFechamentoList() {
        return funcionarioBancoHorasFechamentoList;
    }

    public void setFuncionarioBancoHorasFechamentoList(List<FuncionarioBancoHorasFechamento> funcionarioBancoHorasFechamentoList) {
        this.funcionarioBancoHorasFechamentoList = funcionarioBancoHorasFechamentoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idFuncionarioBancoHoras != null ? idFuncionarioBancoHoras.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FuncionarioBancoHoras)) {
            return false;
        }
        FuncionarioBancoHoras other = (FuncionarioBancoHoras) object;
        if ((this.idFuncionarioBancoHoras == null && other.idFuncionarioBancoHoras != null) || (this.idFuncionarioBancoHoras != null && !this.idFuncionarioBancoHoras.equals(other.idFuncionarioBancoHoras))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Banco de horas ";
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Transient
    private Boolean manual = false;

    public Boolean getManual() {
        return manual;
    }

    public void setManual(Boolean manual) {
        this.manual = manual;
    }

}
