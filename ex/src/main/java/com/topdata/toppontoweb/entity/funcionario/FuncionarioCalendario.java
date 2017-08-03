package com.topdata.toppontoweb.entity.funcionario;

import java.util.Date;

import javax.persistence.Basic;
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

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.topdata.toppontoweb.entity.Entidade;
import com.topdata.toppontoweb.entity.calendario.Calendario;
import com.topdata.toppontoweb.utils.CustomDateSerializer;

/**
 * @version 1.0.6 data 29/09/2016
 * @since 1.0.6 data 29/09/2016
 *
 * @author juliano.ezequiel
 */
@Entity
@Table(name = "FuncionarioCalendario")
@XmlRootElement
public class FuncionarioCalendario implements Entidade, Cloneable {

    @Transient
    private Boolean manual;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone(); //To change body of generated methods, choose Tools | Templates.
    }

    @Id
    @Basic(optional = false)
    @NotNull
    @SequenceGenerator(name = "seq_usu", initialValue = 1)
    @GeneratedValue(generator = "seq_usu", strategy = GenerationType.IDENTITY)
    private Integer idFuncionarioCalendario;

    @Basic(optional = false)
    @NotNull
    @Temporal(TemporalType.DATE)
    @JsonSerialize(using = CustomDateSerializer.class)
    private Date dataInicio;

    @JoinColumn(name = "IdCalendario", referencedColumnName = "IdCalendario")
    @ManyToOne(optional = false)
    private Calendario calendario;

    @JoinColumn(name = "idColetivo", referencedColumnName = "IdColetivo")
    @ManyToOne
    private Coletivo coletivo;

    @JoinColumn(name = "IdFuncionario", referencedColumnName = "IdFuncionario")
    @ManyToOne(optional = false)
    private Funcionario funcionario;

    public FuncionarioCalendario() {
    }

    public FuncionarioCalendario(Integer idFuncionarioCalendario) {
        this.idFuncionarioCalendario = idFuncionarioCalendario;
    }

    public FuncionarioCalendario(Integer idFuncionarioCalendario, Date dataInicio) {
        this.idFuncionarioCalendario = idFuncionarioCalendario;
        this.dataInicio = dataInicio;
    }

    public Integer getIdFuncionarioCalendario() {
        return idFuncionarioCalendario;
    }

    public void setIdFuncionarioCalendario(Integer idFuncionarioCalendario) {
        this.idFuncionarioCalendario = idFuncionarioCalendario;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idFuncionarioCalendario != null ? idFuncionarioCalendario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FuncionarioCalendario)) {
            return false;
        }
        FuncionarioCalendario other = (FuncionarioCalendario) object;
        if ((this.idFuncionarioCalendario == null && other.idFuncionarioCalendario != null) || (this.idFuncionarioCalendario != null && !this.idFuncionarioCalendario.equals(other.idFuncionarioCalendario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Calendário do funcionário ";
    }

    public Boolean getManual() {
        return manual;
    }

    public void setManual(Boolean manual) {
        this.manual = manual;
    }

}
