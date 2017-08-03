package com.topdata.toppontoweb.entity.configuracoes;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.topdata.toppontoweb.entity.Entidade;
import com.topdata.toppontoweb.entity.bancohoras.BancoHoras;
import com.topdata.toppontoweb.entity.jornada.Jornada;
import javax.persistence.FetchType;
import javax.persistence.Transient;

/**
 * @version 1.0.5 data 19/07/2016
 * @since 1.0.5 data 19/07/2016
 *
 * @author juliano.ezequiel
 */
@Entity
@Table(name = "PercentuaisAcrescimo")
@XmlRootElement
public class PercentuaisAcrescimo implements Entidade, Cloneable {

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @OneToMany(mappedBy = "percentuaisAcrescimo")
    private List<Jornada> jornadaList;

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idPercentuaisAcrescimo")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPercentuaisAcrescimo;

    @Size(max = 50)
    @Column(name = "Descricao")
    private String descricao;

    @OneToMany(cascade = {CascadeType.ALL, CascadeType.REFRESH}, orphanRemoval = true, mappedBy = "percentuaisAcrescimo", fetch = FetchType.EAGER)
    private List<SequenciaPercentuais> sequenciaPercentuaisList = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "percentuaisAcrescimo")
    private List<BancoHoras> bancoHorasList = new ArrayList<>();

    @Transient
    private Boolean possuiVinculos;

    public PercentuaisAcrescimo() {
    }

    public PercentuaisAcrescimo(Integer idPercentuaisAcrescimo) {
        this.idPercentuaisAcrescimo = idPercentuaisAcrescimo;
    }

    public Boolean getPossuiVinculos() {
//        Boolean b = (this.jornadaList != null && !this.jornadaList.isEmpty()) || (this.bancoHorasList != null && !this.bancoHorasList.isEmpty());
//        this.possuiVinculos = b;
        return possuiVinculos;
    }

    public void setPossuiVinculos(Boolean possuiVinculos) {
        this.possuiVinculos = possuiVinculos;
    }

    public Integer getIdPercentuaisAcrescimo() {
        return idPercentuaisAcrescimo;
    }

    public void setIdPercentuaisAcrescimo(Integer idPercentuaisAcrescimo) {
        this.idPercentuaisAcrescimo = idPercentuaisAcrescimo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @XmlTransient
    @JsonIgnore
    public List<SequenciaPercentuais> getSequenciaPercentuaisList() {
        return sequenciaPercentuaisList;
    }

    public void setSequenciaPercentuaisList(List<SequenciaPercentuais> sequenciaPercentuaisList) {
        this.sequenciaPercentuaisList = sequenciaPercentuaisList;
    }

    @XmlTransient
    @JsonIgnore
    public List<BancoHoras> getBancoHorasList() {
        return bancoHorasList;
    }

    public void setBancoHorasList(List<BancoHoras> bancoHorasList) {
        this.bancoHorasList = bancoHorasList;
    }

    @XmlTransient
    @JsonIgnore
    public List<Jornada> getJornadaList() {
        return jornadaList;
    }

    public void setJornadaList(List<Jornada> jornadaList) {
        this.jornadaList = jornadaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPercentuaisAcrescimo != null ? idPercentuaisAcrescimo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PercentuaisAcrescimo)) {
            return false;
        }
        PercentuaisAcrescimo other = (PercentuaisAcrescimo) object;
        if ((this.idPercentuaisAcrescimo == null && other.idPercentuaisAcrescimo != null) || (this.idPercentuaisAcrescimo != null && !this.idPercentuaisAcrescimo.equals(other.idPercentuaisAcrescimo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String s = this.getDescricao() != null ? this.getDescricao() : "";
        return "Percentuais de acréscimo " + s;
    }

}
