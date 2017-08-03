package com.topdata.toppontoweb.entity.tipo;

import com.topdata.toppontoweb.entity.Entidade;
import com.topdata.toppontoweb.entity.empresa.Empresa;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;
import java.util.List;
import java.util.Objects;
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
 *
 * @author juliano.ezequiel
 */
@Entity
@Table(name = "TipoDocumento")
@XmlRootElement
public class TipoDocumento implements Entidade {

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idTipoDocumento")
    @SequenceGenerator(name = "seq", initialValue = 1)
    @GeneratedValue(generator = "seq", strategy = GenerationType.IDENTITY)
    private Integer idTipoDocumento;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "Descricao")
    private String descricao;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoDocumento")
    private List<Empresa> empresaList;

    public TipoDocumento() {
    }

    public TipoDocumento(CONSTANTES.Enum_DOCUMENTO documento) {
        this.descricao = documento.getDescricao();
        this.idTipoDocumento = documento.ordinal();
    }

    /**
     *
     * @param idTipoDocumento
     */
    public TipoDocumento(Integer idTipoDocumento) {
        this.idTipoDocumento = idTipoDocumento;
    }

    /**
     *
     * @param idTipoDocumento
     * @param descricao
     */
    public TipoDocumento(Integer idTipoDocumento, String descricao) {
        this.idTipoDocumento = idTipoDocumento;
        this.descricao = descricao;
    }

    public Integer getIdTipoDocumento() {
        return idTipoDocumento;
    }

    public void setIdTipoDocumento(Integer idTipoDocumento) {
        this.idTipoDocumento = idTipoDocumento;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @XmlTransient
    @JsonIgnore
    public List<Empresa> getEmpresaList() {
        return empresaList;
    }

    public void setEmpresaList(List<Empresa> empresaList) {
        this.empresaList = empresaList;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 31 * hash + Objects.hashCode(this.idTipoDocumento);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TipoDocumento other = (TipoDocumento) obj;
        if (!Objects.equals(this.idTipoDocumento, other.idTipoDocumento)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "TipoDocumento[ idTipoDocumento=" + idTipoDocumento + " ]";
    }

}
