package com.topdata.toppontoweb.entity.autenticacao;

import java.util.List;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import com.topdata.toppontoweb.entity.Entidade;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;

/**
 * Entidade representa os módulos de acesso ao sistema. è utilizado para agrupar
 * as funcionalidades.
 *
 * @version 1.0.3 04/05/2016
 * @since 1.0.3 04/05/2016
 *
 * @see Funcionalidades
 * @author juliano.ezequiel
 */
@Entity
@Table(name = "Modulos")
@XmlRootElement
public class Modulos implements Entidade {

    @OneToMany(mappedBy = "idModulo", fetch = FetchType.EAGER)
    private List<Funcionalidades> funcionalidadesList;

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idModulo")
    @SequenceGenerator(name = "seq_usu", initialValue = 1)
    @GeneratedValue(generator = "seq_usu", strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(max = 60)
    @Column(name = "Descricao")
    private String text;

    @Transient
    private CONSTANTES.Enum_MODULOS modulo;

    @Transient
    private String icon;

    @Transient
    private String parent;

    public Modulos() {
    }

    public Modulos(Integer idModulo, String descricao) {
        this.id = idModulo;
        this.text = descricao;
    }

    public Modulos(CONSTANTES.Enum_MODULOS modulos) {
        this.text = modulos.getDescricao();
        this.id = modulos.ordinal();
    }

    public Modulos(Integer idModulo) {
        this.id = idModulo;
    }

    public Modulos(String text) {
        this.text = text;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getIcon() {
        return this.modulo.getIcone();
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public CONSTANTES.Enum_MODULOS getModulo() {
        for (CONSTANTES.Enum_MODULOS modulos1 : CONSTANTES.Enum_MODULOS.values()) {
            if (modulos1.ordinal() == this.id) {
                this.modulo = modulos1;
            }
        }
        return this.modulo;
    }

    public void setModulo(CONSTANTES.Enum_MODULOS modulo) {
        this.modulo = modulo;
    }

    public String getParent() {
        for (CONSTANTES.Enum_MODULOS modulos : CONSTANTES.Enum_MODULOS.values()) {
            if (modulos.ordinal() == this.id) {
                this.parent = modulos.getPai();
            }
        }
        return this.parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.id);
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
        final Modulos other = (Modulos) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Modulos[ idModulo=" + id + " ]";
    }

//    @XmlTransient
//    @JsonIgnore
    public List<Funcionalidades> getFuncionalidadesList() {
        return funcionalidadesList;
    }

    public void setFuncionalidadesList(List<Funcionalidades> funcionalidadesList) {
        this.funcionalidadesList = funcionalidadesList;
    }

}
