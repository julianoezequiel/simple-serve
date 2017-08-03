package com.topdata.toppontoweb.entity.ferramentas;

import java.util.ArrayList;
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

/**
 *
 * @version 1.0.0 data 10/07/2017
 * @since 1.0.0 data 10/07/2017
 *
 * @author juliano.ezequiel
 */
@Entity
@Table(name = "LayoutArquivoGrupo")
@XmlRootElement
public class LayoutArquivoGrupo implements Entidade {

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idLayoutArquivoGrupo")
    @SequenceGenerator(name = "seq_usu", initialValue = 1)
    @GeneratedValue(generator = "seq_usu", strategy = GenerationType.IDENTITY)
    private Integer idLayoutArquivoGrupo;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "IdGrupoEvento")
    private String idGrupoEvento;

    @Basic(optional = false)
    @NotNull
    @Column(name = "NoturnasComAdicional")
    private boolean noturnasComAdicional;

    @JoinColumn(name = "IdLayoutArquivo", referencedColumnName = "IdLayoutArquivo")
    @ManyToOne(optional = false)
    private LayoutArquivo layoutArquivo;

    @JoinColumn(name = "idTipoFormatoEvento", referencedColumnName = "idTipoFormatoEvento")
    @ManyToOne(optional = false)
    private TipoFormatoEvento tipoFormatoEvento;

    @OneToMany(mappedBy = "layoutArquivoGrupo")
    private List<LayoutGrupoEventosMotivos> layoutGrupoEventosMotivosList = new ArrayList<>();

    public LayoutArquivoGrupo() {
    }

    public LayoutArquivoGrupo(Integer idLayoutArquivoGrupo) {
        this.idLayoutArquivoGrupo = idLayoutArquivoGrupo;
    }

    public LayoutArquivoGrupo(Integer idLayoutArquivoGrupo, String idGrupoEvento,  String formatoEventos, boolean noturnasComAdicional) {
        this.idLayoutArquivoGrupo = idLayoutArquivoGrupo;
        this.idGrupoEvento = idGrupoEvento;
        this.noturnasComAdicional = noturnasComAdicional;
    }

    public Integer getIdLayoutArquivoGrupo() {
        return idLayoutArquivoGrupo;
    }

    public void setIdLayoutArquivoGrupo(Integer idLayoutArquivoGrupo) {
        this.idLayoutArquivoGrupo = idLayoutArquivoGrupo;
    }

    @XmlTransient
    @JsonIgnore
    public List<LayoutGrupoEventosMotivos> getLayoutGrupoEventosMotivosList() {
        return layoutGrupoEventosMotivosList;
    }

    public void setLayoutGrupoEventosMotivosList(List<LayoutGrupoEventosMotivos> layoutGrupoEventosMotivosList) {
        this.layoutGrupoEventosMotivosList = layoutGrupoEventosMotivosList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idLayoutArquivoGrupo != null ? idLayoutArquivoGrupo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LayoutArquivoGrupo)) {
            return false;
        }
        LayoutArquivoGrupo other = (LayoutArquivoGrupo) object;
        if ((this.idLayoutArquivoGrupo == null && other.idLayoutArquivoGrupo != null) || (this.idLayoutArquivoGrupo != null && !this.idLayoutArquivoGrupo.equals(other.idLayoutArquivoGrupo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Layout de arquivo grupo ";
    }

    public String getIdGrupoEvento() {
        return idGrupoEvento;
    }

    public void setIdGrupoEvento(String idGrupoEvento) {
        this.idGrupoEvento = idGrupoEvento;
    }

    public boolean getNoturnasComAdicional() {
        return noturnasComAdicional;
    }

    public void setNoturnasComAdicional(boolean noturnasComAdicional) {
        this.noturnasComAdicional = noturnasComAdicional;
    }

    public LayoutArquivo getLayoutArquivo() {
        return layoutArquivo;
    }

    public void setLayoutArquivo(LayoutArquivo layoutArquivo) {
        this.layoutArquivo = layoutArquivo;
    }

    public TipoFormatoEvento getTipoFormatoEvento() {
        return tipoFormatoEvento;
    }

    public void setTipoFormatoEvento(TipoFormatoEvento tipoFormatoEvento) {
        this.tipoFormatoEvento = tipoFormatoEvento;
    }

}
