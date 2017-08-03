package com.topdata.toppontoweb.entity.funcionario;

import com.topdata.toppontoweb.entity.Entidade;
import java.util.Calendar;
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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @version 1.0.6 data 06/10/2016
 * @version 1.0.4 data 23/08/2016
 * @since 1.0.4 data 23/08/2016
 *
 * @author juliano.ezequiel
 */
@Entity
@Table(name = "HistoricoFuncionarioCargo")
@XmlRootElement
public class HistoricoFuncionarioCargo implements Entidade {

    @Basic(optional = false)
    @NotNull
    @Column(name = "DataCargo")
    @Temporal(TemporalType.DATE)
    private Date dataCargo;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "Descricao")
    private String descricao;

    @JoinColumn(name = "IdCargo", referencedColumnName = "IdCargo")
    @ManyToOne
    private Cargo cargo;

    @JoinColumn(name = "IdFuncionario", referencedColumnName = "IdFuncionario")
    @ManyToOne(optional = false)
    private Funcionario funcionario;

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idHistoricoFuncionarioCargo")
    @SequenceGenerator(name = "seq_usu", initialValue = 1)
    @GeneratedValue(generator = "seq_usu", strategy = GenerationType.IDENTITY)
    private Integer idHistoricoFuncionarioCargo;

    public HistoricoFuncionarioCargo() {
    }

    public HistoricoFuncionarioCargo(Integer idFuncionarioCargo) {
        this.idHistoricoFuncionarioCargo = idFuncionarioCargo;
    }

    public HistoricoFuncionarioCargo(Funcionario funcionario) {
        this.funcionario = funcionario;
        this.dataCargo = Calendar.getInstance().getTime();
        this.descricao = funcionario.getCargo().getDescricao();
        this.cargo = funcionario.getCargo();
    }

    public HistoricoFuncionarioCargo(Integer idFuncionarioCargo, Date dataCargo, String descricao) {
        this.idHistoricoFuncionarioCargo = idFuncionarioCargo;
        this.dataCargo = dataCargo;
        this.descricao = descricao;
    }

    public Integer getIdHistoricoFuncionarioCargo() {
        return idHistoricoFuncionarioCargo;
    }

    public void setIdHistoricoFuncionarioCargo(Integer idHistoricoFuncionarioCargo) {
        this.idHistoricoFuncionarioCargo = idHistoricoFuncionarioCargo;
    }

    public Date getDataCargo() {
        return dataCargo;
    }

    public void setDataCargo(Date dataCargo) {
        this.dataCargo = dataCargo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
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
        hash += (idHistoricoFuncionarioCargo != null ? idHistoricoFuncionarioCargo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HistoricoFuncionarioCargo)) {
            return false;
        }
        HistoricoFuncionarioCargo other = (HistoricoFuncionarioCargo) object;
        if ((this.idHistoricoFuncionarioCargo == null && other.idHistoricoFuncionarioCargo != null) || (this.idHistoricoFuncionarioCargo != null && !this.idHistoricoFuncionarioCargo.equals(other.idHistoricoFuncionarioCargo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Cargo ";
    }

    public Cargo getIdCargo() {
        return cargo;
    }

    public void setIdCargo(Cargo idCargo) {
        this.cargo = idCargo;
    }

}
