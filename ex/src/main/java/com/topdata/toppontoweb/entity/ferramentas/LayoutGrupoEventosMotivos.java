package com.topdata.toppontoweb.entity.ferramentas;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import com.topdata.toppontoweb.entity.Entidade;
import com.topdata.toppontoweb.entity.funcionario.Motivo;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;

/**
 * @version 1.0.0 data 10/07/2017
 * @since 1.0.0 data 10/07/2017
 *
 * @author juliano.ezequiel
 */
@Entity
@Table(name = "LayoutGrupoEventosMotivos")
@XmlRootElement
public class LayoutGrupoEventosMotivos implements Entidade {

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idLayoutGrupoEventosMotivos")
    @SequenceGenerator(name = "seq_usu", initialValue = 1)
    @GeneratedValue(generator = "seq_usu", strategy = GenerationType.IDENTITY)
    private Integer idLayoutGrupoEventosMotivos;

    @JoinColumn(name = "IdEvento", referencedColumnName = "IdEvento")
    @ManyToOne
    private Evento evento;

    @JoinColumn(name = "idLayoutArquivoGrupo", referencedColumnName = "idLayoutArquivoGrupo")
    @ManyToOne(optional = false)
    private LayoutArquivoGrupo layoutArquivoGrupo;

    @JoinColumn(name = "IdMotivo", referencedColumnName = "IdMotivo")
    @ManyToOne
    private Motivo motivo;

    public LayoutGrupoEventosMotivos() {
    }

    public LayoutGrupoEventosMotivos(Integer idLayoutGrupoEventosMotivos) {
        this.idLayoutGrupoEventosMotivos = idLayoutGrupoEventosMotivos;
    }

    public Integer getIdLayoutGrupoEventosMotivos() {
        return idLayoutGrupoEventosMotivos;
    }

    public void setIdLayoutGrupoEventosMotivos(Integer idLayoutGrupoEventosMotivos) {
        this.idLayoutGrupoEventosMotivos = idLayoutGrupoEventosMotivos;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public LayoutArquivoGrupo getLayoutArquivoGrupo() {
        return layoutArquivoGrupo;
    }

    public void setLayoutArquivoGrupo(LayoutArquivoGrupo layoutArquivoGrupo) {
        this.layoutArquivoGrupo = layoutArquivoGrupo;
    }

    public Motivo getMotivo() {
        return motivo;
    }

    public void setMotivo(Motivo motivo) {
        this.motivo = motivo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idLayoutGrupoEventosMotivos != null ? idLayoutGrupoEventosMotivos.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LayoutGrupoEventosMotivos)) {
            return false;
        }
        LayoutGrupoEventosMotivos other = (LayoutGrupoEventosMotivos) object;
        if ((this.idLayoutGrupoEventosMotivos == null && other.idLayoutGrupoEventosMotivos != null) || (this.idLayoutGrupoEventosMotivos != null && !this.idLayoutGrupoEventosMotivos.equals(other.idLayoutGrupoEventosMotivos))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Layout grupo de eventos motivos";
    }

}
