package com.topdata.toppontoweb.entity.autenticacao;

import com.topdata.toppontoweb.entity.Entidade;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;
import java.util.List;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * @version 1.0.3
 * @since 1.0.1
 * @author juliano.ezequiel
 */
@Entity
@Table(name = "Permissoes")
@XmlRootElement
public class Permissoes implements Entidade {

    @ManyToMany(mappedBy = "permissoesList")
    private List<Grupo> grupoList;

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "IdPermissoes")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPermissoes;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "Permissoes")
    private String permissoes;

    @Size(max = 45)
    @Column(name = "Descricao")
    private String descricao;

    @Transient
    private CONSTANTES.Enum_PERMISSAO permissao;

    public Permissoes() {
    }

    public Permissoes(Integer idPermissoes) {
        this.idPermissoes = idPermissoes;
    }

    public Permissoes(Integer idPermissoes, String permissoes) {
        this.idPermissoes = idPermissoes;
        this.permissoes = permissoes;
    }

    /**
     *
     * @param permissoes
     * @param descricao
     */
    public Permissoes(String permissoes, String descricao) {
        this.descricao = descricao;
        this.permissoes = permissoes;
    }

    public Permissoes(Integer idPermissoes, String permissoes, String descricao) {
        this.idPermissoes = idPermissoes;
        this.permissoes = permissoes;
        this.descricao = descricao;
    }

    public Permissoes(CONSTANTES.Enum_PERMISSAO permissao) {
        this.permissoes = permissao.getPermissao();
        this.descricao = permissao.getDescricao();
        this.idPermissoes = permissao.ordinal();
        this.permissao = permissao;
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Getters e Setters">
    public Integer getIdPermissoes() {
        return idPermissoes;
    }

    public void setIdPermissoes(Integer idPermissoes) {
        this.idPermissoes = idPermissoes;
    }

    public String getPermissoes() {
        return permissoes;
    }

    public void setPermissoes(String permissoes) {
        this.permissoes = permissoes;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public CONSTANTES.Enum_PERMISSAO getPermissao() {
        return permissao;
    }

    public void setPermissao(CONSTANTES.Enum_PERMISSAO permissao) {
        this.permissao = permissao;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + Objects.hashCode(this.idPermissoes);
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
        final Permissoes other = (Permissoes) obj;
        if (!Objects.equals(this.idPermissoes, other.idPermissoes)) {
            return false;
        }
        return true;
    }

    //</editor-fold>
    @Override
    public String toString() {
        return "Permissoes[ idPermissoes=" + idPermissoes + " ]";
    }

    @XmlTransient
    @JsonIgnore
    public List<Grupo> getGrupoList() {
        return grupoList;
    }

    public void setGrupoList(List<Grupo> grupoList) {
        this.grupoList = grupoList;
    }

}
