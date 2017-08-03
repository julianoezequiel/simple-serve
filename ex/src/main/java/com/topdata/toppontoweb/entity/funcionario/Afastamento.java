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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.topdata.toppontoweb.entity.Entidade;
import com.topdata.toppontoweb.utils.CustomDateSerializer;

/**
 * @version 1.0.6 data 03/10/2016
 * @since 1.0.1 data 03/03/2016
 *
 * @author juliano.ezequiel
 */
@Entity
@Table(name = "Afastamento")
@XmlRootElement
public class Afastamento implements Entidade, Cloneable {

    @Transient
    private Boolean manual;

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "IdAfastamento")
    @SequenceGenerator(name = "seq_usu", initialValue = 1)
    @GeneratedValue(generator = "seq_usu", strategy = GenerationType.IDENTITY)
    private Integer idAfastamento;

    @Column(name = "DataInicio")
    @Temporal(TemporalType.DATE)
    @JsonSerialize(using = CustomDateSerializer.class)
    private Date dataInicio;

    @Column(name = "DataFim")
    @Temporal(TemporalType.DATE)
    @JsonSerialize(using = CustomDateSerializer.class)
    private Date dataFim;

    @Column(name = "Abonado")
    private Boolean abonado;

    @JoinColumn(name = "idColetivo", referencedColumnName = "IdColetivo")
    @ManyToOne
    private Coletivo coletivo;

    @JoinColumn(name = "IdFuncionario", referencedColumnName = "IdFuncionario")
    @ManyToOne(optional = false)
    private Funcionario funcionario;

    @JoinColumn(name = "IdMotivo", referencedColumnName = "IdMotivo")
    @ManyToOne(optional = false)
    private Motivo motivo;

    public Afastamento() {
    }

    public Afastamento(Integer idAfastamento) {
        this.idAfastamento = idAfastamento;
    }

    public Afastamento(Afastamento af) {
        this.manual = af.manual;
        this.idAfastamento = af.idAfastamento;
        this.dataInicio = af.dataInicio;
        this.dataFim = af.dataFim;
        this.abonado = af.abonado;
        this.coletivo = af.coletivo;
        this.funcionario = af.funcionario;
        this.motivo = af.motivo;
    }

    public Integer getIdAfastamento() {
        return idAfastamento;
    }

    public void setIdAfastamento(Integer idAfastamento) {
        this.idAfastamento = idAfastamento;
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

    public Boolean getAbonado() {
        return abonado;
    }

    public void setAbonado(Boolean abonado) {
        this.abonado = abonado;
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

    /**
     *
     * @param funcionario
     */
    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public Motivo getMotivo() {
        return motivo;
    }

    public void setMotivo(Motivo motivo) {
        this.motivo = motivo;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + Objects.hashCode(this.idAfastamento);
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
        final Afastamento other = (Afastamento) obj;
        if (!Objects.equals(this.idAfastamento, other.idAfastamento)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Afastamento " + (this.motivo != null ? this.motivo.getDescricao() : "");
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone(); //To change body of generated methods, choose Tools | Templates.
    }

    public Boolean getManual() {
        return manual;
    }

    public void setManual(Boolean manual) {
        this.manual = manual;
    }

}
