package com.topdata.toppontoweb.entity.funcionario;

import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.topdata.toppontoweb.entity.Entidade;
import com.topdata.toppontoweb.utils.constantes.Enum_MotivoNaoEditavel;
import java.util.ArrayList;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.Transient;

/**
 * @version 1.0.1 10/04/2016
 * @since 1.0.1 10/04/2016
 * @author juliano.ezequiel
 */
@Entity
@Table(name = "Coletivo")
@XmlRootElement
public class Coletivo implements Entidade {

    @OneToMany(mappedBy = "coletivo", fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    private List<FuncionarioBancoHorasFechamento> funcionarioBancoHorasFechamentoList = new ArrayList<>();

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "IdColetivo")
    @SequenceGenerator(name = "seq_usu", initialValue = 1)
    @GeneratedValue(generator = "seq_usu", strategy = GenerationType.IDENTITY)
    private Integer idColetivo;

    @Size(max = 50)
    @Column(name = "Descricao")
    private String descricao;

    @Transient
    private Boolean selecionado = true;

    @OneToMany(mappedBy = "coletivo", fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    private List<FuncionarioCalendario> funcionarioCalendarioList = new ArrayList<>();

    @OneToMany(mappedBy = "coletivo", fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    private List<Afastamento> afastamentoList = new ArrayList<>();

    @OneToMany(mappedBy = "coletivo", fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    private List<FuncionarioBancoHoras> funcionarioBancoHorasList = new ArrayList<>();

    @OneToMany(mappedBy = "coletivo", fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    private List<FuncionarioJornada> funcionarioJornadaList = new ArrayList<>();

    @OneToMany(mappedBy = "coletivo", fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    private List<Compensacao> compensacaoList = new ArrayList<>();

    @Transient
    private Boolean permitido;

    @Transient
    private Boolean editavel = true;

    @Transient
    private Enum_MotivoNaoEditavel motivoNaoEditavel;

    public Coletivo() {
    }

    public Boolean getEditavel() {
        return editavel;
    }

    public Enum_MotivoNaoEditavel getMotivoNaoEditavel() {
        return motivoNaoEditavel;
    }

    public void setEditavel(Boolean editavel) {
        this.editavel = editavel;
    }

    public void setMotivoNaoEditavel(Enum_MotivoNaoEditavel motivoNaoEditavel) {
        this.motivoNaoEditavel = motivoNaoEditavel;
    }

    public Boolean getPermitido() {
        return permitido;
    }

    public void setPermitido(Boolean permitido) {
        this.permitido = permitido;
    }

    public Coletivo(Integer idColetivo) {
        this.idColetivo = idColetivo;
    }

    public Integer getIdColetivo() {
        return idColetivo;
    }

    public void setIdColetivo(Integer idColetivo) {
        this.idColetivo = idColetivo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setSelecionado(Boolean selecionado) {
        this.selecionado = selecionado;
    }

    public Boolean getSelecionado() {
        return selecionado;
    }

    @XmlTransient
    @JsonIgnore
    public List<FuncionarioCalendario> getFuncionarioCalendarioList() {
        return funcionarioCalendarioList;
    }

    public void setFuncionarioCalendarioList(List<FuncionarioCalendario> funcionarioCalendarioList) {
        this.funcionarioCalendarioList = funcionarioCalendarioList;
    }

    @XmlTransient
    @JsonIgnore
    public List<Afastamento> getAfastamentoList() {
        return afastamentoList;
    }

    public void setAfastamentoList(List<Afastamento> afastamentoList) {
        this.afastamentoList = afastamentoList;
    }

    /**
     *
     * @return
     */
    @XmlTransient
    @JsonIgnore
    public List<FuncionarioBancoHoras> getFuncionarioBancoHorasList() {
        return funcionarioBancoHorasList;
    }

    public void setFuncionarioBancoHorasList(List<FuncionarioBancoHoras> funcionarioBancoHorasList) {
        this.funcionarioBancoHorasList = funcionarioBancoHorasList;
    }

    @XmlTransient
    @JsonIgnore
    public List<FuncionarioJornada> getFuncionarioJornadaList() {
        return funcionarioJornadaList;
    }

    public void setFuncionarioJornadaList(List<FuncionarioJornada> funcionarioJornadaList) {
        this.funcionarioJornadaList = funcionarioJornadaList;
    }

    @XmlTransient
    @JsonIgnore
    public List<Compensacao> getCompensacaoList() {
        return compensacaoList;
    }

    public void setCompensacaoList(List<Compensacao> compensacaoList) {
        this.compensacaoList = compensacaoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idColetivo != null ? idColetivo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Coletivo)) {
            return false;
        }
        Coletivo other = (Coletivo) object;
        if ((this.idColetivo == null && other.idColetivo != null) || (this.idColetivo != null && !this.idColetivo.equals(other.idColetivo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String s = this.descricao != null ? this.descricao : "";
        return "Coletivo " + s;
    }

//    @XmlTransient
//    @JsonIgnore
//    public List<FuncionarioBancoHorasFechamento> getFuncionarioBancoHorasFechamentoList() {
//        return funcionarioBancoHorasFechamentoList;
//    }
//
//    public void setFuncionarioBancoHorasFechamentoList(List<FuncionarioBancoHorasFechamento> funcionarioBancoHorasFechamentoList) {
//        this.funcionarioBancoHorasFechamentoList = funcionarioBancoHorasFechamentoList;
//    }
//
    @XmlTransient
    @JsonIgnore
    public List<FuncionarioBancoHorasFechamento> getFuncionarioBancoHorasFechamentoList() {
        return funcionarioBancoHorasFechamentoList;
    }

    public void setFuncionarioBancoHorasFechamentoList(List<FuncionarioBancoHorasFechamento> funcionarioBancoHorasFechamentoList) {
        this.funcionarioBancoHorasFechamentoList = funcionarioBancoHorasFechamentoList;
    }
}
