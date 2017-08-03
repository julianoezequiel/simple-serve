package com.topdata.toppontoweb.entity.calendario;

import com.topdata.toppontoweb.entity.Entidade;
import com.topdata.toppontoweb.entity.funcionario.FuncionarioCalendario;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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

/**
 * @version 1.0.4 data 30/06/2016
 * @since 1.0.4 data 30/06/2016
 *
 * @author juliano.ezequiel
 */
@Entity
@Table(name = "Calendario")
@XmlRootElement
public class Calendario implements Entidade {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "calendario")
    private List<FuncionarioCalendario> funcionarioCalendarioList = new ArrayList<>();

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "IdCalendario")
    @SequenceGenerator(name = "seq_cal", initialValue = 1)
    @GeneratedValue(generator = "seq_cal", strategy = GenerationType.IDENTITY)
    private Integer idCalendario;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "calendario", fetch = FetchType.EAGER, orphanRemoval = true)
    private List<CalendarioFeriado> calendarioFeriadoList = new ArrayList<>();

    @Size(max = 30)
    @Column(name = "Descricao")
    private String descricao;

    public Calendario() {
    }

    public Calendario(Integer idCalendario) {
        this.idCalendario = idCalendario;
    }

    public Integer getIdCalendario() {
        return idCalendario;
    }

    public void setIdCalendario(Integer idCalendario) {
        this.idCalendario = idCalendario;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCalendario != null ? idCalendario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Calendario)) {
            return false;
        }
        Calendario other = (Calendario) object;
        if ((this.idCalendario == null && other.idCalendario != null) || (this.idCalendario != null && !this.idCalendario.equals(other.idCalendario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String s = this.getDescricao() != null ? this.getDescricao() : "";
        return "Calendário " + s;
    }

    @XmlTransient
    @JsonIgnore
    public List<CalendarioFeriado> getCalendarioFeriadoList() {
        return calendarioFeriadoList;
    }

    public void setCalendarioFeriadoList(List<CalendarioFeriado> calendarioFeriadoList) {
        this.calendarioFeriadoList = calendarioFeriadoList;
    }

    @XmlTransient
    @JsonIgnore
    public List<FuncionarioCalendario> getFuncionarioCalendarioList() {
        return funcionarioCalendarioList;
    }

    public void setFuncionarioCalendarioList(List<FuncionarioCalendario> funcionarioCalendarioList) {
        this.funcionarioCalendarioList = funcionarioCalendarioList;
    }

}
