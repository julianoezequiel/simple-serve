package com.topdata.toppontoweb.entity.tipo;

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
import com.topdata.toppontoweb.entity.jornada.Horario;
import com.topdata.toppontoweb.entity.bancohoras.BancoHorasLimite;
import com.topdata.toppontoweb.entity.configuracoes.SequenciaPercentuais;

/**
 * @version 1.0.6 data 06/10/2016
 * @version 1.0.5 data 26/07/2016
 * @since 1.0.1
 *
 * @author juliano.ezequiel
 */
@Entity
@Table(name = "TipoDia")
@XmlRootElement
public class TipoDia implements Entidade {
    
    public final static Integer HORAS_NOTURNAS = 8;
    public final static Integer COMPENSACADO = 2;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipodia")
    private List<Horario> horarioList;

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "IdTipodia")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idTipodia;

    @Size(max = 45)
    @Column(name = "Descricao")
    private String descricao;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoDia")
    private List<BancoHorasLimite> bancoHorasLimiteList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoDia")
    private List<SequenciaPercentuais> sequenciaPercentuaisList;

    public TipoDia() {
    }

    public TipoDia(Integer idTipodia) {
        this.idTipodia = idTipodia;
    }

    public TipoDia(Integer idTipodia, String descricao) {
        this.idTipodia = idTipodia;
        this.descricao = descricao;
    }

    public Integer getIdTipodia() {
        return idTipodia;
    }

    public void setIdTipodia(Integer idTipodia) {
        this.idTipodia = idTipodia;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @XmlTransient
    @JsonIgnore
    public List<BancoHorasLimite> getBancoHorasLimiteList() {
        return bancoHorasLimiteList;
    }

    public void setBancoHorasLimiteList(List<BancoHorasLimite> bancoHorasLimiteList) {
        this.bancoHorasLimiteList = bancoHorasLimiteList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTipodia != null ? idTipodia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoDia)) {
            return false;
        }
        TipoDia other = (TipoDia) object;
        if ((this.idTipodia == null && other.idTipodia != null) || (this.idTipodia != null && !this.idTipodia.equals(other.idTipodia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Tipo de Dia";
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
    public List<Horario> getHorarioList() {
        return horarioList;
    }

    public void setHorarioList(List<Horario> horarioList) {
        this.horarioList = horarioList;
    }

}
