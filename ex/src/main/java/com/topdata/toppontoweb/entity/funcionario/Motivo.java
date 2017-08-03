package com.topdata.toppontoweb.entity.funcionario;

import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.topdata.toppontoweb.entity.Entidade;
import com.topdata.toppontoweb.entity.ferramentas.LayoutArquivoGrupo;
import com.topdata.toppontoweb.entity.ferramentas.LayoutGrupoEventosMotivos;
import com.topdata.toppontoweb.entity.marcacoes.Marcacoes;
import com.topdata.toppontoweb.entity.tipo.TipoMotivo;

/**
 *
 * @author juliano.ezequiel
 */
@Entity
@Table(name = "Motivo")
@XmlRootElement
public class Motivo implements Entidade {

    @OneToMany(mappedBy = "motivo")
    private List<LayoutGrupoEventosMotivos> layoutGrupoEventosMotivosList;

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "IdMotivo")
    @SequenceGenerator(name = "seq_usu", initialValue = 1)
    @GeneratedValue(generator = "seq_usu", strategy = GenerationType.IDENTITY)
    private Integer idMotivo;

    @Size(max = 30)
    @Column(name = "Descricao")
    private String descricao;

    @JoinColumn(name = "IdTipoMotivo", referencedColumnName = "IdTipoMotivo")
    @ManyToOne(optional = false)
    private TipoMotivo idTipoMotivo;

    @OneToMany(mappedBy = "motivo")
    private List<Cartao> cartaoList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "motivo")
    private List<Afastamento> afastamentoList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "motivo")
    private List<Abono> abonoList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "motivo")
    private List<Compensacao> compensacaoList;

    @OneToMany(mappedBy = "motivo")
    private List<Marcacoes> marcacoesList;

    public Motivo() {
    }

    public Motivo(Integer idMotivo) {
        this.idMotivo = idMotivo;
    }

    public Motivo(Integer id, String descricao) {
        this.idMotivo = id;
        this.descricao = descricao;
    }

    public Integer getIdMotivo() {
        return idMotivo;
    }

    public void setIdMotivo(Integer idMotivo) {
        this.idMotivo = idMotivo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public TipoMotivo getIdTipoMotivo() {
        return idTipoMotivo;
    }

    public void setIdTipoMotivo(TipoMotivo idTipoMotivo) {
        this.idTipoMotivo = idTipoMotivo;
    }

    @XmlTransient
    @JsonIgnore
    public List<Cartao> getCartaoList() {
        return cartaoList;
    }

    public void setCartaoList(List<Cartao> cartaoList) {
        this.cartaoList = cartaoList;
    }

    @XmlTransient
    @JsonIgnore
    public List<Afastamento> getAfastamentoList() {
        return afastamentoList;
    }

    public void setAfastamentoList(List<Afastamento> afastamentoList) {
        this.afastamentoList = afastamentoList;
    }

    @XmlTransient
    @JsonIgnore
    public List<Abono> getAbonoList() {
        return abonoList;
    }

    public void setAbonoList(List<Abono> abonoList) {
        this.abonoList = abonoList;
    }

    @XmlTransient
    @JsonIgnore
    public List<Compensacao> getCompensacaoList() {
        return compensacaoList;
    }

    public void setCompensacaoList(List<Compensacao> compensacaoList) {
        this.compensacaoList = compensacaoList;
    }

    @XmlTransient
    @JsonIgnore
    public List<Marcacoes> getMarcacoesList() {
        return marcacoesList;
    }

    public void setMarcacoesList(List<Marcacoes> marcacoesList) {
        this.marcacoesList = marcacoesList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idMotivo != null ? idMotivo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Motivo)) {
            return false;
        }
        Motivo other = (Motivo) object;
        if ((this.idMotivo == null && other.idMotivo != null) || (this.idMotivo != null && !this.idMotivo.equals(other.idMotivo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Motivo ";
    }

    @XmlTransient
    @JsonIgnore
    public List<LayoutGrupoEventosMotivos> getLayoutGrupoEventosMotivosList() {
        return layoutGrupoEventosMotivosList;
    }

    public void setLayoutGrupoEventosMotivosList(List<LayoutGrupoEventosMotivos> layoutGrupoEventosMotivosList) {
        this.layoutGrupoEventosMotivosList = layoutGrupoEventosMotivosList;
    }

}
