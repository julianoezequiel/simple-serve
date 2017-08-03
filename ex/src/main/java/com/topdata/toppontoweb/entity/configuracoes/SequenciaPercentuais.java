package com.topdata.toppontoweb.entity.configuracoes;


import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.xml.bind.annotation.XmlRootElement;

import com.topdata.toppontoweb.entity.Entidade;
import com.topdata.toppontoweb.entity.tipo.TipoDia;
import java.util.Date;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * @version 1.0.5 data 26/07/2016
 * @since 1.0.5 data 26/07/2016
 *
 * @author juliano.ezequiel
 */
@Entity
@Table(name = "SequenciaPercentuais")
@XmlRootElement
public class SequenciaPercentuais implements Entidade, Cloneable {

    public SequenciaPercentuais(Date horas, Double acrescimo,  Integer idSequencia, TipoDia tipoDia) {
        this.horas = horas;
        this.acrescimo = acrescimo;
        this.tipoDia = tipoDia;
        this.idSequencia = idSequencia;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone(); //To change body of generated methods, choose Tools | Templates.
    }

    @Column(name = "Horas")
    @Temporal(TemporalType.TIMESTAMP)
    private Date horas;

    @EmbeddedId
    protected SequenciaPercentuaisPK sequenciaPercentuaisPK = new SequenciaPercentuaisPK();
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "Acrescimo")
    private Double acrescimo;

    @JoinColumn(name = "idPercentuaisAcrescimo", referencedColumnName = "idPercentuaisAcrescimo", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private PercentuaisAcrescimo percentuaisAcrescimo;

    @JoinColumn(name = "IdTipodia", referencedColumnName = "IdTipodia", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private TipoDia tipoDia;

    @Transient
    private Integer idSequencia;

    public SequenciaPercentuais() {
    }

    public SequenciaPercentuais(SequenciaPercentuaisPK sequenciaPercentuaisPK) {
        this.sequenciaPercentuaisPK = sequenciaPercentuaisPK;
    }

    public SequenciaPercentuais(int idSequencia, int idPercentuaisAcrescimo, int idTipodia) {
        this.sequenciaPercentuaisPK = new SequenciaPercentuaisPK(idSequencia, idPercentuaisAcrescimo, idTipodia);
    }

    public SequenciaPercentuaisPK getSequenciaPercentuaisPK() {
        return sequenciaPercentuaisPK;
    }

    public void setSequenciaPercentuaisPK(SequenciaPercentuaisPK sequenciaPercentuaisPK) {
        this.sequenciaPercentuaisPK = sequenciaPercentuaisPK;
    }

    public Double getAcrescimo() {
        return acrescimo;
    }

    public void setAcrescimo(Double acrescimo) {
        this.acrescimo = acrescimo;
    }

    public PercentuaisAcrescimo getPercentuaisAcrescimo() {
        return percentuaisAcrescimo;
    }

    public void setPercentuaisAcrescimo(PercentuaisAcrescimo percentuaisAcrescimo) {
        this.percentuaisAcrescimo = percentuaisAcrescimo;
        if (percentuaisAcrescimo.getIdPercentuaisAcrescimo() != null) {
            this.idSequencia = percentuaisAcrescimo.getIdPercentuaisAcrescimo();
            this.sequenciaPercentuaisPK.setIdPercentuaisAcrescimo(percentuaisAcrescimo.getIdPercentuaisAcrescimo());
        }
    }

    public TipoDia getTipoDia() {
        return tipoDia;
    }

    public void setTipoDia(TipoDia tipoDia) {
        this.tipoDia = tipoDia;
        this.sequenciaPercentuaisPK.setIdTipodia(tipoDia.getIdTipodia());
    }

    public Integer getIdSequencia() {
        return idSequencia;
    }

    public void setIdSequencia(Integer idSequencia) {
        this.idSequencia = idSequencia;
        this.sequenciaPercentuaisPK.setIdSequencia(idSequencia);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (sequenciaPercentuaisPK != null ? sequenciaPercentuaisPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SequenciaPercentuais)) {
            return false;
        }
        SequenciaPercentuais other = (SequenciaPercentuais) object;
        return !((this.sequenciaPercentuaisPK == null && other.sequenciaPercentuaisPK != null) || (this.sequenciaPercentuaisPK != null && !this.sequenciaPercentuaisPK.equals(other.sequenciaPercentuaisPK)));
    }

    @Override
    public String toString() {
        return "Sequências de percentuais ";
    }

    public Date getHoras() {
        return horas;
    }

    public void setHoras(Date horas) {
        this.horas = horas;
    }

}
