package com.topdata.toppontoweb.entity.funcionario;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.topdata.toppontoweb.entity.Entidade;
import com.topdata.toppontoweb.entity.empresa.Empresa;
import java.util.Calendar;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 * @version 1.0.6 data 06/10/2016
 * @version 1.0.4 data 23/08/2016
 * @version 1.0.2 20/04/2016
 * @since 1.0.2 20/04/2016
 *
 * @author juliano.ezequiel
 */
@Entity
@Table(name = "FuncionarioEmpresa")
@XmlRootElement
public class FuncionarioEmpresa implements Entidade {

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idFuncionarioEmpresa")
    @SequenceGenerator(name = "seq_usu", initialValue = 1)
    @GeneratedValue(generator = "seq_usu", strategy = GenerationType.IDENTITY)
    private Integer idFuncionarioEmpresa;

    @Basic(optional = false)
    @NotNull
    @Column(name = "DataEmpresa")
    @Temporal(TemporalType.DATE)
    private Date dataEmpresa;

    @JoinColumn(name = "IdEmpresa", referencedColumnName = "IdEmpresa")
    @ManyToOne(optional = false)
    private Empresa empresa;

    @JoinColumn(name = "IdFuncionario", referencedColumnName = "IdFuncionario")
    @ManyToOne(optional = false)
    private Funcionario funcionario;

    public FuncionarioEmpresa() {
    }

    public FuncionarioEmpresa(Funcionario funcionario) {
        this.funcionario = funcionario;
        this.empresa = funcionario.getDepartamento().getEmpresa();
        this.dataEmpresa = Calendar.getInstance().getTime();
    }

    public FuncionarioEmpresa(Integer idFuncionarioEmpresa) {
        this.idFuncionarioEmpresa = idFuncionarioEmpresa;
    }

    public FuncionarioEmpresa(Integer idFuncionarioEmpresa, Date dataEmpresa) {
        this.idFuncionarioEmpresa = idFuncionarioEmpresa;
        this.dataEmpresa = dataEmpresa;
    }

    public Integer getIdFuncionarioEmpresa() {
        return idFuncionarioEmpresa;
    }

    public void setIdFuncionarioEmpresa(Integer idFuncionarioEmpresa) {
        this.idFuncionarioEmpresa = idFuncionarioEmpresa;
    }

    public Date getDataEmpresa() {
        return dataEmpresa;
    }

    public void setDataEmpresa(Date dataEmpresa) {
        this.dataEmpresa = dataEmpresa;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
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
        hash += (idFuncionarioEmpresa != null ? idFuncionarioEmpresa.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FuncionarioEmpresa)) {
            return false;
        }
        FuncionarioEmpresa other = (FuncionarioEmpresa) object;
        if ((this.idFuncionarioEmpresa == null && other.idFuncionarioEmpresa != null) || (this.idFuncionarioEmpresa != null && !this.idFuncionarioEmpresa.equals(other.idFuncionarioEmpresa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Funcionario empresa";
    }

}
