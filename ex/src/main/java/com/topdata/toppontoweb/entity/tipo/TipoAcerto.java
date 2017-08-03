package com.topdata.toppontoweb.entity.tipo;

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
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import com.topdata.toppontoweb.entity.Entidade;
import com.topdata.toppontoweb.entity.funcionario.FuncionarioBancoHorasFechamento;
import javax.xml.bind.annotation.XmlTransient;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author juliano.ezequiel
 */
@Entity
@Table(name = "TipoAcerto")
@XmlRootElement
public class TipoAcerto implements Entidade {

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "IdTipoAcerto")
    private Integer idTipoAcerto;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "Descricao")
    private String descricao;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoAcerto")
    private List<FuncionarioBancoHorasFechamento> funcionarioBancoHorasFechamentoList;

    public Integer getIdTipoAcerto() {
        return idTipoAcerto;
    }

    public void setIdTipoAcerto(Integer idTipoAcerto) {
        this.idTipoAcerto = idTipoAcerto;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @XmlTransient
    @JsonIgnore
    public List<FuncionarioBancoHorasFechamento> getFuncionarioBancoHorasFechamentoList() {
        return funcionarioBancoHorasFechamentoList;
    }

    public void setFuncionarioBancoHorasFechamentoList(List<FuncionarioBancoHorasFechamento> funcionarioBancoHorasFechamentoList) {
        this.funcionarioBancoHorasFechamentoList = funcionarioBancoHorasFechamentoList;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + Objects.hashCode(this.idTipoAcerto);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TipoAcerto other = (TipoAcerto) obj;
        if (!Objects.equals(this.idTipoAcerto, other.idTipoAcerto)) {
            return false;
        }
        return true;
    }

}
