package com.topdata.toppontoweb.entity.rep;

import com.topdata.toppontoweb.entity.Entidade;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * @version 1.0.1 10/04/2016
 * @since 1.0.1 10/04/2016
 * @author juliano.ezequiel
 */
@Entity
@Table(name = "ModeloRep")
@XmlRootElement
public class ModeloRep implements Entidade {

    @Basic(optional = false)
    @NotNull
    @Column(name = "NumeroModelo")
    private String numeroModelo;

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "IdModeloRep")
    @SequenceGenerator(name = "seq_usu", initialValue = 1)
    @GeneratedValue(generator = "seq_usu", strategy = GenerationType.IDENTITY)
    private Integer idModeloRep;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "Modelo")
    private String modelo;

    @OneToMany(mappedBy = "modeloRep")
    private List<Rep> repList;

    public ModeloRep() {
    }

    public ModeloRep(Integer idModeloRep) {
        this.idModeloRep = idModeloRep;
    }

    public ModeloRep(Integer idModeloRep, String numeroModelo, String modelo) {
        this.idModeloRep = idModeloRep;
        this.modelo = modelo;
        this.numeroModelo = numeroModelo;
    }

    public Integer getIdModeloRep() {
        return idModeloRep;
    }

    public void setIdModeloRep(Integer idModeloRep) {
        this.idModeloRep = idModeloRep;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    @XmlTransient
    @JsonIgnore
    public List<Rep> getRepList() {
        return repList;
    }

    public void setRepList(List<Rep> repList) {
        this.repList = repList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idModeloRep != null ? idModeloRep.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ModeloRep)) {
            return false;
        }
        ModeloRep other = (ModeloRep) object;
        if ((this.idModeloRep == null && other.idModeloRep != null) || (this.idModeloRep != null && !this.idModeloRep.equals(other.idModeloRep))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ModeloRep[ idModeloRep=" + idModeloRep + " ]";
    }

    public String getNumeroModelo() {
        return numeroModelo;
    }

    public void setNumeroModelo(String numeroModelo) {
        this.numeroModelo = StringUtils.leftPad(numeroModelo, 5, "0");
    }

}
