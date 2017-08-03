package com.topdata.toppontoweb.entity.calendario;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import com.topdata.toppontoweb.entity.Entidade;
import com.topdata.toppontoweb.utils.CustomDateSerializer;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 *
 * @author juliano.ezequiel
 */
@Entity
@Table(name = "CalendarioFeriado")
@XmlRootElement
public class CalendarioFeriado implements Entidade {

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idCalendarioFeriado")
    @SequenceGenerator(name = "seq_cal", initialValue = 1)
    @GeneratedValue(generator = "seq_cal", strategy = GenerationType.IDENTITY)
    private Integer idCalendarioFeriado;

    @Basic(optional = false)
    @NotNull
    @Column(name = "DataInicio")
    @Temporal(TemporalType.DATE)
    @JsonSerialize(using = CustomDateSerializer.class)
    private Date dataInicio;

    @JoinColumn(name = "IdCalendario", referencedColumnName = "IdCalendario")
    @ManyToOne(optional = false)
    private Calendario calendario;

    @JoinColumn(name = "idFeriado", referencedColumnName = "idFeriado")
    @ManyToOne(optional = false)
    private Feriado feriado;

    public CalendarioFeriado() {
    }

    public CalendarioFeriado(Integer idCalendarioFeriado) {
        this.idCalendarioFeriado = idCalendarioFeriado;
    }

    public CalendarioFeriado(Integer idCalendarioFeriado, Date dataInicio) {
        this.idCalendarioFeriado = idCalendarioFeriado;
        this.dataInicio = dataInicio;
    }

    public Integer getIdCalendarioFeriado() {
        return idCalendarioFeriado;
    }

    public void setIdCalendarioFeriado(Integer idCalendarioFeriado) {
        this.idCalendarioFeriado = idCalendarioFeriado;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Calendario getCalendario() {
        return calendario;
    }

    public void setCalendario(Calendario calendario) {
        this.calendario = calendario;
    }

    public Feriado getFeriado() {
        return feriado;
    }

    public void setFeriado(Feriado feriado) {
        this.feriado = feriado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCalendarioFeriado != null ? idCalendarioFeriado.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CalendarioFeriado)) {
            return false;
        }
        CalendarioFeriado other = (CalendarioFeriado) object;
        if ((this.idCalendarioFeriado == null && other.idCalendarioFeriado != null) || (this.idCalendarioFeriado != null && !this.idCalendarioFeriado.equals(other.idCalendarioFeriado))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Calendário";
    }

}
