package com.topdata.toppontoweb.entity.tipo;

import com.topdata.toppontoweb.entity.Entidade;
import com.topdata.toppontoweb.entity.rep.Rep;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * @since 1.0
 * @version 1.0
 * @author juliano.ezequiel
 */
@Entity
@Table(name = "TipoEquipamento")
@XmlRootElement
public class TipoEquipamento implements Entidade {

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idTipoEquipamento")
    @SequenceGenerator(name = "seq_usu", initialValue = 1)
    @GeneratedValue(generator = "seq_usu", strategy = GenerationType.IDENTITY)
    private Integer idTipoEquipamento;

    @Size(max = 20)
    @Column(name = "Descricao")
    private String descricao;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoEquipamento")
    private List<Rep> repList;

    public TipoEquipamento() {
    }

    public TipoEquipamento(Integer idTipoEquipamento) {
        this.idTipoEquipamento = idTipoEquipamento;
    }

    public TipoEquipamento(Integer idTipoEquipamento, String descricao) {
        this.idTipoEquipamento = idTipoEquipamento;
        this.descricao = descricao;
    }

    public Integer getIdTipoEquipamento() {
        return idTipoEquipamento;
    }

    public void setIdTipoEquipamento(Integer idTipoEquipamento) {
        this.idTipoEquipamento = idTipoEquipamento;
    }

    /**
     *
     * @return
     */
    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
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
        hash += (idTipoEquipamento != null ? idTipoEquipamento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoEquipamento)) {
            return false;
        }
        TipoEquipamento other = (TipoEquipamento) object;
        if ((this.idTipoEquipamento == null && other.idTipoEquipamento != null) || (this.idTipoEquipamento != null && !this.idTipoEquipamento.equals(other.idTipoEquipamento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Tipo de equipamento";
    }

}
