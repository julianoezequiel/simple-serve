package com.topdata.toppontoweb.entity.empresa;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.map.annotate.JsonView;

import com.topdata.toppontoweb.entity.Entidade;
import com.topdata.toppontoweb.entity.funcionario.Funcionario;
import com.topdata.toppontoweb.entity.rep.Rep;
import com.topdata.toppontoweb.rest.autenticacao.JsonViews;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.xml.bind.annotation.XmlTransient;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * ATENÇÃO: Retirado o mapeamento biderecional do Rep
 *
 * @version 1.0.5 data 18/07/2016
 * @since 1.0.5 data 18/07/2016
 *
 * @author juliano.ezequiel
 */
@Entity
@Table(name = "Cei")
@XmlRootElement
public class Cei implements Entidade {

    @OneToMany(mappedBy = "cei")
    private List<Funcionario> funcionarioList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cei")
    private List<Rep> repList;

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idCei")
    @SequenceGenerator(name = "seq_usu", initialValue = 1)
    @GeneratedValue(generator = "seq_usu", strategy = GenerationType.IDENTITY)
    @JsonView(JsonViews.Audit.class)
    private Integer idCei;

    @Size(max = 12)
    @Column(name = "Descricao")
    private String descricao;

    @JoinColumn(name = "IdEmpresa", referencedColumnName = "IdEmpresa")
    @ManyToOne(optional = false)
    private Empresa empresa;

    public Cei() {
    }

    public Cei(Integer idCei) {
        this.idCei = idCei;
    }

    public Integer getIdCei() {
        return idCei;
    }

    public void setIdCei(Integer idCei) {
        this.idCei = idCei;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCei != null ? idCei.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cei)) {
            return false;
        }
        Cei other = (Cei) object;
        if ((this.idCei == null && other.idCei != null) || (this.idCei != null && !this.idCei.equals(other.idCei))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "CEI";
    }
//

    @XmlTransient
    @JsonIgnore
    public List<Rep> getRepList() {
        return repList;
    }

    public void setRepList(List<Rep> repList) {
        this.repList = repList;
    }

    @XmlTransient
    @JsonIgnore
    public List<Funcionario> getFuncionarioList() {
        return funcionarioList;
    }

    public void setFuncionarioList(List<Funcionario> funcionarioList) {
        this.funcionarioList = funcionarioList;
    }

}
