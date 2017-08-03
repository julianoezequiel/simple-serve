package com.topdata.toppontoweb.entity.funcionario;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import com.topdata.toppontoweb.entity.Entidade;
import com.topdata.toppontoweb.entity.empresa.Departamento;
import java.util.Calendar;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @version 1.0.6 data 06/10/2016
 * @version 1.0.4 data 23/08/2016
 * @version 1.0.2 20/04/2016
 * @since 1.0.2 20/04/2016
 *
 * @author juliano.ezequiel
 */
@Entity
@Table(name = "FuncionarioDepartamento")
@XmlRootElement
public class FuncionarioDepartamento implements Entidade {

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idFuncionarioDepartamento")
    @SequenceGenerator(name = "seq_usu", initialValue = 1)
    @GeneratedValue(generator = "seq_usu", strategy = GenerationType.IDENTITY)
    private Integer idFuncionarioDepartamento;

    @Basic(optional = false)
    @NotNull
    @Column(name = "DataDepartamento")
    @Temporal(TemporalType.DATE)
    private Date dataDepartamento;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "Descricao")
    private String descricao;

    @JoinColumn(name = "IdDepartamento", referencedColumnName = "IdDepartamento")
    @ManyToOne(optional = false)
    private Departamento departamento;

    @JoinColumn(name = "IdFuncionario", referencedColumnName = "IdFuncionario")
    @ManyToOne(optional = false)
    private Funcionario funcionario;

    public FuncionarioDepartamento() {
    }

    public FuncionarioDepartamento(Integer idFuncionarioDepartamento) {
        this.idFuncionarioDepartamento = idFuncionarioDepartamento;
    }

    public FuncionarioDepartamento(Funcionario funcionario) {
        if (funcionario.getDepartamento() != null) {
            this.funcionario = funcionario;
            this.departamento = funcionario.getDepartamento();
            this.descricao = funcionario.getDepartamento().getDescricao();
            this.dataDepartamento = Calendar.getInstance().getTime();
        }
    }

    public FuncionarioDepartamento(Integer idFuncionarioDepartamento, Date dataDepartamento, String descricao) {
        this.idFuncionarioDepartamento = idFuncionarioDepartamento;
        this.dataDepartamento = dataDepartamento;
        this.descricao = descricao;
    }

    public Integer getIdFuncionarioDepartamento() {
        return idFuncionarioDepartamento;
    }

    public void setIdFuncionarioDepartamento(Integer idFuncionarioDepartamento) {
        this.idFuncionarioDepartamento = idFuncionarioDepartamento;
    }

    public Date getDataDepartamento() {
        return dataDepartamento;
    }

    public void setDataDepartamento(Date dataDepartamento) {
        this.dataDepartamento = dataDepartamento;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
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
        hash += (idFuncionarioDepartamento != null ? idFuncionarioDepartamento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FuncionarioDepartamento)) {
            return false;
        }
        FuncionarioDepartamento other = (FuncionarioDepartamento) object;
        if ((this.idFuncionarioDepartamento == null && other.idFuncionarioDepartamento != null) || (this.idFuncionarioDepartamento != null && !this.idFuncionarioDepartamento.equals(other.idFuncionarioDepartamento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Funcionario departamento";
    }

}
