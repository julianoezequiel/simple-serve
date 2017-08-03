package com.topdata.toppontoweb.entity.jornada;

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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import com.topdata.toppontoweb.entity.Entidade;

/**
 * @version 1.0.4 data 16/08/2016
 * @since 1.0.4 data 16/08/2016
 *
 * @author juliano.ezequiel
 */
@Entity
@Table(name = "HorarioMarcacao")
@XmlRootElement
public class HorarioMarcacao implements Entidade {

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idHorarioMarcacao")
    @SequenceGenerator(name = "seq_usu", initialValue = 1)
    @GeneratedValue(generator = "seq_usu", strategy = GenerationType.IDENTITY)
    private Integer idHorarioMarcacao;

    @Basic(optional = false)
    @NotNull
    @Column(name = "IdSequencia")
    private int idSequencia;

    @Basic(optional = false)
    @NotNull
    @Column(name = "HorarioEntrada")
    @Temporal(TemporalType.TIMESTAMP)
    private Date horarioEntrada;

    @Basic(optional = false)
    @NotNull
    @Column(name = "HorarioSaida")
    @Temporal(TemporalType.TIMESTAMP)
    private Date horarioSaida;

    @JoinColumn(name = "IdHorario", referencedColumnName = "IdHorario")
    @ManyToOne(optional = false)
    private Horario horario;

    @Transient
    private String horarioEntradaInteiro;
    @Transient
    private String HorarioSaidaInteiro;

    public HorarioMarcacao() {
    }

    public HorarioMarcacao(Integer idHorarioMarcacao) {
        this.idHorarioMarcacao = idHorarioMarcacao;
    }

    public HorarioMarcacao(Integer idHorarioMarcacao, Horario horario, Date horarioEntrada, Date horarioSaida, int idSequencia) {
        this.idHorarioMarcacao = idHorarioMarcacao;
        this.horario = horario;
        this.idSequencia = idSequencia;
        this.horarioEntrada = horarioEntrada;
        this.horarioSaida = horarioSaida;
    }

    public Integer getIdHorarioMarcacao() {
        return idHorarioMarcacao;
    }

    public void setIdHorarioMarcacao(Integer idHorarioMarcacao) {
        this.idHorarioMarcacao = idHorarioMarcacao;
    }

    public int getIdSequencia() {
        return idSequencia;
    }

    public void setIdSequencia(int idSequencia) {
        this.idSequencia = idSequencia;
    }

    public Date getHorarioEntrada() {
        return horarioEntrada;
    }

    public void setHorarioEntrada(Date horarioEntrada) {
        this.horarioEntrada = horarioEntrada;
    }

    public Date getHorarioSaida() {
        return horarioSaida;
    }

    public void setHorarioSaida(Date horarioSaida) {
        this.horarioSaida = horarioSaida;
    }

    public Horario getHorario() {
        return horario;
    }

    public void setHorario(Horario horario) {
        this.horario = horario;
    }

    public String getHorarioEntradaInteiro() {
        return horarioEntradaInteiro;
    }

    public String getHorarioSaidaInteiro() {
        return HorarioSaidaInteiro;
    }

    public void setHorarioEntradaInteiro(String horarioEntradaInteiro) {
        this.horarioEntradaInteiro = horarioEntradaInteiro;
    }

    public void setHorarioSaidaInteiro(String HorarioSaidaInteiro) {
        this.HorarioSaidaInteiro = HorarioSaidaInteiro;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idHorarioMarcacao != null ? idHorarioMarcacao.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HorarioMarcacao)) {
            return false;
        }
        HorarioMarcacao other = (HorarioMarcacao) object;
        if ((this.idHorarioMarcacao == null && other.idHorarioMarcacao != null) || (this.idHorarioMarcacao != null && !this.idHorarioMarcacao.equals(other.idHorarioMarcacao))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Horários ";
    }

}
