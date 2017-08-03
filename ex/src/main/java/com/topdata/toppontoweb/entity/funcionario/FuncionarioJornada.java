package com.topdata.toppontoweb.entity.funcionario;

import java.util.Date;
import java.util.Objects;

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

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.topdata.toppontoweb.entity.Entidade;
import com.topdata.toppontoweb.entity.jornada.Jornada;
import com.topdata.toppontoweb.utils.CustomDateSerializer;
import javax.persistence.CascadeType;
import javax.persistence.Transient;

/**
 * @version 1.0.6 data 14/09/2016
 * @since 1.0.1 data 03/05/2016
 *
 * @author juliano.ezequiel
 */
@Entity
@Table(name = "FuncionarioJornada")
@XmlRootElement
public class FuncionarioJornada implements Entidade, Cloneable {

    @Transient
    private Boolean manual;

    @Transient
    private Boolean validarColetivo = false;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone(); //To change body of generated methods, choose Tools | Templates.
    }

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "IdJornadaFuncionario")
    @SequenceGenerator(name = "seq", initialValue = 1)
    @GeneratedValue(generator = "seq", strategy = GenerationType.IDENTITY)
    private Integer idJornadaFuncionario;

    @JoinColumn(name = "idColetivo", referencedColumnName = "IdColetivo")
    @ManyToOne
    private Coletivo coletivo;

    @JoinColumn(name = "IdFuncionario", referencedColumnName = "IdFuncionario")
    @ManyToOne(optional = false, cascade = CascadeType.REFRESH)
    private Funcionario funcionario;

    @JoinColumn(name = "IdJornada", referencedColumnName = "IdJornada")
    @ManyToOne(cascade = CascadeType.REFRESH)
    private Jornada jornada;

    @Column(name = "SequenciaInicial")
    private Integer sequenciaInicial = 0;

    @Basic(optional = false)
    @NotNull
    @Column(name = "DataInicio")
    @Temporal(TemporalType.DATE)
    @JsonSerialize(using = CustomDateSerializer.class)
    private Date dataInicio;

    @Column(name = "DataFim")
    @Temporal(TemporalType.DATE)
    @JsonSerialize(using = CustomDateSerializer.class)
    private Date dataFim;

    @Column(name = "Excecao")
    private Boolean excecaoJornada;

    public FuncionarioJornada() {
    }

    public FuncionarioJornada(Integer idJornadaFuncionario) {
        this.idJornadaFuncionario = idJornadaFuncionario;
    }

    public FuncionarioJornada(Integer idJornadaFuncionario, Date dataInicio) {
        this.idJornadaFuncionario = idJornadaFuncionario;
        this.dataInicio = dataInicio;
    }

    public Boolean getValidarColetivo() {
        return validarColetivo;
    }

    public void setValidarColetivo(Boolean validarColetivo) {
        this.validarColetivo = validarColetivo;
    }

    public Boolean getExcecaoJornada() {
        return excecaoJornada;
    }

    public void setExcecaoJornada(Boolean excecaoJornada) {
        this.excecaoJornada = excecaoJornada;
    }

    public Integer getIdJornadaFuncionario() {
        return idJornadaFuncionario;
    }

    public void setIdJornadaFuncionario(Integer idJornadaFuncionario) {
        this.idJornadaFuncionario = idJornadaFuncionario;
    }

    public Integer getSequenciaInicial() {
        return sequenciaInicial;
    }

    public void setSequenciaInicial(Integer sequenciaInicial) {
        this.sequenciaInicial = sequenciaInicial;
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

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 31 * hash + Objects.hashCode(this.idJornadaFuncionario);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FuncionarioJornada other = (FuncionarioJornada) obj;
        if (!Objects.equals(this.idJornadaFuncionario, other.idJornadaFuncionario)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Jornada do funcionário ";
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public Jornada getJornada() {
        return jornada;
    }

    public void setJornada(Jornada jornada) {
        this.jornada = jornada;
    }

    public Coletivo getColetivo() {
        return coletivo;
    }

    public void setColetivo(Coletivo coletivo) {
        this.coletivo = coletivo;
    }

    public Boolean getManual() {
        return manual;
    }

    public void setManual(Boolean manual) {
        this.manual = manual;
    }

}
