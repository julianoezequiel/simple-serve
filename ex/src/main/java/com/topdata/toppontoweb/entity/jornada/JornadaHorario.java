package com.topdata.toppontoweb.entity.jornada;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.topdata.toppontoweb.entity.Entidade;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;

/**
 * @version 1.0.6 data 06/10/2016
 * @version 1.0.4 data 16/08/2016
 * @since 1.0.4 data 16/08/2016
 *
 * @author juliano.ezequiel
 */
@Entity
@Table(name = "JornadaHorario")
@XmlRootElement
public class JornadaHorario implements Entidade {

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idJornadaHorario")
    @SequenceGenerator(name = "seq_usu", initialValue = 1)
    @GeneratedValue(generator = "seq_usu", strategy = GenerationType.IDENTITY)
    private Integer idJornadaHorario;

    @Basic(optional = false)
    @NotNull
    @Column(name = "idSequencia")
    private int idSequencia;

    @JoinColumn(name = "IdHorario", referencedColumnName = "IdHorario")
    @ManyToOne(optional = false,cascade = CascadeType.REFRESH)
    private Horario horario;

    @JoinColumn(name = "IdJornada", referencedColumnName = "IdJornada")
    @ManyToOne(optional = false,cascade = CascadeType.REFRESH)
    private Jornada jornada;

    public JornadaHorario() {
    }

    public JornadaHorario(Integer idJornadaHorario) {
        this.idJornadaHorario = idJornadaHorario;
    }

    public JornadaHorario(Integer idJornadaHorario, int idSequencia) {
        this.idJornadaHorario = idJornadaHorario;
        this.idSequencia = idSequencia;
    }

    public JornadaHorario(int idSequencia, Horario horario, Jornada jornada) {
        this.idSequencia = idSequencia;
        this.horario = horario;
        this.jornada = jornada;
    }

    public Integer getIdJornadaHorario() {
        return idJornadaHorario;
    }

    public void setIdJornadaHorario(Integer idJornadaHorario) {
        this.idJornadaHorario = idJornadaHorario;
    }

    public Integer getIdSequencia() {
        return idSequencia;
    }

    public void setIdSequencia(int idSequencia) {
        this.idSequencia = idSequencia;
    }

    public Horario getHorario() {
        return horario;
    }

    public void setHorario(Horario horario) {
        this.horario = horario;
    }

    public Jornada getJornada() {
        return jornada;
    }

    public void setJornada(Jornada jornada) {
        this.jornada = jornada;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idJornadaHorario != null ? idJornadaHorario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof JornadaHorario)) {
            return false;
        }
        JornadaHorario other = (JornadaHorario) object;
        if ((this.idJornadaHorario == null && other.idJornadaHorario != null) || (this.idJornadaHorario != null && !this.idJornadaHorario.equals(other.idJornadaHorario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Jornada";
    }

}
