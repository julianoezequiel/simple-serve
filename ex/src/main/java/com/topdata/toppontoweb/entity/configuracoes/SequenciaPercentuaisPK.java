package com.topdata.toppontoweb.entity.configuracoes;

import com.topdata.toppontoweb.entity.Entidade;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 * @version 1.0.5 data 26/07/2016
 * @since 1.0.5 data 26/07/2016
 *
 * @author juliano.ezequiel
 */
@Embeddable
public class SequenciaPercentuaisPK implements Entidade, Comparable<SequenciaPercentuaisPK> {

    @Basic(optional = false)
    @NotNull
    @Column(name = "idSequencia")
    private int idSequencia;

    @Basic(optional = false)
    @NotNull
    @Column(name = "idPercentuaisAcrescimo")
    private int idPercentuaisAcrescimo;

    @Basic(optional = false)
    @NotNull
    @Column(name = "IdTipodia")
    private int idTipodia;

    public SequenciaPercentuaisPK() {
    }

    public SequenciaPercentuaisPK(int idSequencia, int idPercentuaisAcrescimo, int idTipodia) {
        this.idSequencia = idSequencia;
        this.idPercentuaisAcrescimo = idPercentuaisAcrescimo;
        this.idTipodia = idTipodia;
    }

    public int getIdSequencia() {
        return idSequencia;
    }

    public void setIdSequencia(int idSequencia) {
        this.idSequencia = idSequencia;
    }

    public int getIdPercentuaisAcrescimo() {
        return idPercentuaisAcrescimo;
    }

    public void setIdPercentuaisAcrescimo(int idPercentuaisAcrescimo) {
        this.idPercentuaisAcrescimo = idPercentuaisAcrescimo;
    }

    public int getIdTipodia() {
        return idTipodia;
    }

    public void setIdTipodia(int idTipodia) {
        this.idTipodia = idTipodia;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idSequencia;
        hash += (int) idPercentuaisAcrescimo;
        hash += (int) idTipodia;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SequenciaPercentuaisPK)) {
            return false;
        }
        SequenciaPercentuaisPK other = (SequenciaPercentuaisPK) object;
        if (this.idSequencia != other.idSequencia) {
            return false;
        }
        if (this.idPercentuaisAcrescimo != other.idPercentuaisAcrescimo) {
            return false;
        }
        if (this.idTipodia != other.idTipodia) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Sequências de percentuais ";
    }

    @Override
    public int compareTo(SequenciaPercentuaisPK o) {
        if (this.idSequencia < o.idSequencia) {
            return -1;
        }
        if (this.idSequencia > o.idSequencia) {
            return 1;
        }
        return 0;
        
    }

}
