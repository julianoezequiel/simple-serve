package com.topdata.toppontoweb.entity.bancohoras;

import com.topdata.toppontoweb.entity.Entidade;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 * @version 1.0.1 10/04/2016
 * @since 1.0.1 10/04/2016
 * @author juliano.ezequiel
 */
@Embeddable
public class BancoHorasLimitePK implements Entidade {

    @Basic(optional = false)
    @NotNull
    @Column(name = "IdBancoHoras")
    private Integer idBancoHoras;
    @Basic(optional = false)
    @NotNull
    @Column(name = "idTipodia")
    private Integer idTipodia;

    public BancoHorasLimitePK() {
    }

    /**
     *
     * @param idBancoHoras
     * @param idTipodia
     */
    public BancoHorasLimitePK(int idBancoHoras, int idTipodia) {
        this.idBancoHoras = idBancoHoras;
        this.idTipodia = idTipodia;
    }

    public Integer getIdBancoHoras() {
        return idBancoHoras;
    }

    public void setIdBancoHoras(Integer idBancoHoras) {
        this.idBancoHoras = idBancoHoras;
    }

    public Integer getIdTipodia() {
        return idTipodia;
    }

    public void setIdTipodia(Integer idTipodia) {
        this.idTipodia = idTipodia;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idBancoHoras;
        hash += (int) idTipodia;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BancoHorasLimitePK)) {
            return false;
        }
        BancoHorasLimitePK other = (BancoHorasLimitePK) object;
        if (this.idBancoHoras != other.idBancoHoras) {
            return false;
        }
        if (this.idTipodia != other.idTipodia) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Banco horas";
    }

}
