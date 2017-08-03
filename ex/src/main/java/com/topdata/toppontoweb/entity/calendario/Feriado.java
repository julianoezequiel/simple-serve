package com.topdata.toppontoweb.entity.calendario;

import com.topdata.toppontoweb.entity.Entidade;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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

/**
 * @version 1.0.4 data 30/06/2016
 * @since 1.0.4 data 30/06/2016
 *
 * @author juliano.ezequiel
 */
@Entity
@Table(name = "Feriado")
@XmlRootElement
public class Feriado implements Entidade {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "feriado")
    private List<CalendarioFeriado> calendarioFeriadoList;

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idFeriado")
    @SequenceGenerator(name = "seq_fer", initialValue = 1)
    @GeneratedValue(generator = "seq_fer", strategy = GenerationType.IDENTITY)
    private Integer idFeriado;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "Descricao")
    private String descricao;

    public Feriado() {
    }

    public Feriado(Integer idFeriado) {
        this.idFeriado = idFeriado;
    }

    public Feriado(Integer idFeriado, String descricao) {
        this.idFeriado = idFeriado;
        this.descricao = descricao;
    }

    public Integer getIdFeriado() {
        return idFeriado;
    }

    public void setIdFeriado(Integer idFeriado) {
        this.idFeriado = idFeriado;
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
        hash += (idFeriado != null ? idFeriado.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Feriado)) {
            return false;
        }
        Feriado other = (Feriado) object;
        if ((this.idFeriado == null && other.idFeriado != null) || (this.idFeriado != null && !this.idFeriado.equals(other.idFeriado))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Feriado " + this.descricao;
    }

    @XmlTransient
    @JsonIgnore
    public List<CalendarioFeriado> getCalendarioFeriadoList() {
        return calendarioFeriadoList;
    }

    public void setCalendarioFeriadoList(List<CalendarioFeriado> calendarioFeriadoList) {
        this.calendarioFeriadoList = calendarioFeriadoList;
    }

}
