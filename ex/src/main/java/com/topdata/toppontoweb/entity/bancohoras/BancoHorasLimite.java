package com.topdata.toppontoweb.entity.bancohoras;

import com.topdata.toppontoweb.entity.Entidade;
import com.topdata.toppontoweb.entity.tipo.TipoDia;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @version 1.0.1 10/04/2016
 * @since 1.0.1 10/04/2016
 * @author juliano.ezequiel
 */
@Entity
@Table(name = "BancoHorasLimite")
@XmlRootElement
public class BancoHorasLimite implements Entidade {

    @Column(name = "Limite")
    @Temporal(TemporalType.TIMESTAMP)
    private Date limite;

    @EmbeddedId
    protected BancoHorasLimitePK bancoHorasLimitePK = new BancoHorasLimitePK();

    @JoinColumn(name = "IdBancoHoras", referencedColumnName = "IdBancoHoras", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private BancoHoras bancoHoras;

    @JoinColumn(name = "idTipodia", referencedColumnName = "IdTipodia", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private TipoDia tipoDia;

    public BancoHorasLimite() {
    }

    public BancoHorasLimite(BancoHorasLimitePK bancoHorasLimitePK) {
        this.bancoHorasLimitePK = bancoHorasLimitePK;
    }

    public BancoHorasLimite(int idBancoHoras, int idTipodia) {
        this.bancoHorasLimitePK = new BancoHorasLimitePK(idBancoHoras, idTipodia);
    }

    public BancoHorasLimitePK getBancoHorasLimitePK() {
        return bancoHorasLimitePK;
    }

    public void setBancoHorasLimitePK(BancoHorasLimitePK bancoHorasLimitePK) {
        this.bancoHorasLimitePK = bancoHorasLimitePK;
    }

    public BancoHoras getBancoHoras() {
        return bancoHoras;
    }

    public void setBancoHoras(BancoHoras bancoHoras) {
        this.bancoHoras = bancoHoras;
        this.bancoHorasLimitePK.setIdBancoHoras(bancoHoras.getIdBancoHoras());
    }

    public TipoDia getTipoDia() {
        return tipoDia;
    }

    public void setTipoDia(TipoDia tipoDia) {
        this.tipoDia = tipoDia;
        this.bancoHorasLimitePK.setIdTipodia(tipoDia.getIdTipodia());
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (bancoHorasLimitePK != null ? bancoHorasLimitePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BancoHorasLimite)) {
            return false;
        }
        BancoHorasLimite other = (BancoHorasLimite) object;
        if ((this.bancoHorasLimitePK == null && other.bancoHorasLimitePK != null) || (this.bancoHorasLimitePK != null && !this.bancoHorasLimitePK.equals(other.bancoHorasLimitePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Banco horas";
    }

    public Date getLimite() {
        return limite;
    }

    public void setLimite(Date limite) {
        this.limite = limite;
    }

}
