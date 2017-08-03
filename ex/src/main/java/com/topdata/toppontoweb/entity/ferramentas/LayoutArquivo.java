package com.topdata.toppontoweb.entity.ferramentas;

import com.topdata.toppontoweb.dto.ferramentas.layout.LayoutArquivoTransfer;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.topdata.toppontoweb.entity.Entidade;
import java.util.ArrayList;
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
@Table(name = "LayoutArquivo")
@XmlRootElement
public class LayoutArquivo implements Entidade {

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "IdLayoutArquivo")
    @SequenceGenerator(name = "seq_usu", initialValue = 1)
    @GeneratedValue(generator = "seq_usu", strategy = GenerationType.IDENTITY)
    private Integer idLayoutArquivo;

    @Size(max = 50)
    @Column(name = "Descricao")
    private String descricao;

    @Size(max = 50)
    @Column(name = "Cabecalho")
    private String cabecalho;

    @Column(name = "SomaContadorCabecalho")
    private Boolean somaContadorCabecalho;

    @Size(max = 255)
    @Column(name = "Formato")
    private String formato;

    @Size(max = 20)
    @Column(name = "FormatoDataPeriodo")
    private String formatoDataPeriodo;

    @Size(max = 20)
    @Column(name = "FormatoDataExportacao")
    private String formatoDataExportacao;

    @Column(name = "SabadoDiaUtil")
    private Boolean sabadoDiaUtil;

    @Column(name = "DomingoDiaUtil")
    private Boolean domingoDiaUtil;

    @Size(max = 1)
    @Column(name = "Delimitador")
    private String delimitador;

    @Basic(optional = false)
    @NotNull
    @Column(name = "HoraSexagesimal")
    private Boolean horaSexagesimal;

    @Basic(optional = false)
    @NotNull
    @Column(name = "PorLinha")
    private boolean porLinha;

    @JoinColumn(name = "idTipoLayout", referencedColumnName = "idTipoLayout")
    @ManyToOne(optional = false)
    private TipoLayout tipoLayout;

    @OneToMany(mappedBy = "layoutArquivo")
    private List<LayoutArquivoGrupo> layoutArquivoGrupoList = new ArrayList<>();

    public LayoutArquivo() {
    }

    public LayoutArquivo(Integer idLayoutArquivo) {
        this.idLayoutArquivo = idLayoutArquivo;
    }

    public Integer getIdLayoutArquivo() {
        return idLayoutArquivo;
    }

    public void setIdLayoutArquivo(Integer idLayoutArquivo) {
        this.idLayoutArquivo = idLayoutArquivo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCabecalho() {
        return cabecalho;
    }

    public void setCabecalho(String cabecalho) {
        this.cabecalho = cabecalho;
    }

    public Boolean getSomaContadorCabecalho() {
        return somaContadorCabecalho;
    }

    public void setSomaContadorCabecalho(Boolean somaContadorCabecalho) {
        this.somaContadorCabecalho = somaContadorCabecalho;
    }

    public String getFormato() {
        return formato;
    }

    public void setFormato(String formato) {
        this.formato = formato;
    }

    public String getFormatoDataPeriodo() {
        return formatoDataPeriodo;
    }

    public void setFormatoDataPeriodo(String formatoDataPeriodo) {
        this.formatoDataPeriodo = formatoDataPeriodo;
    }

    public String getFormatoDataExportacao() {
        return formatoDataExportacao;
    }

    public void setFormatoDataExportacao(String formatoDataExportacao) {
        this.formatoDataExportacao = formatoDataExportacao;
    }

    public Boolean getSabadoDiaUtil() {
        return sabadoDiaUtil;
    }

    public void setSabadoDiaUtil(Boolean sabadoDiaUtil) {
        this.sabadoDiaUtil = sabadoDiaUtil;
    }

    public Boolean getDomingoDiaUtil() {
        return domingoDiaUtil;
    }

    public void setDomingoDiaUtil(Boolean domingoDiaUtil) {
        this.domingoDiaUtil = domingoDiaUtil;
    }

    public String getDelimitador() {
        return delimitador;
    }

    public void setDelimitador(String delimitador) {
        this.delimitador = delimitador;
    }

    public Boolean getHoraSexagesimal() {
        return horaSexagesimal;
    }

    public void setHoraSexagesimal(Boolean horaSexagesimal) {
        this.horaSexagesimal = horaSexagesimal;
    }

    public TipoLayout getTipoLayout() {
        return tipoLayout;
    }

    public void setTipoLayout(TipoLayout tipoLayout) {
        this.tipoLayout = tipoLayout;
    }

    public boolean getPorLinha() {
        return porLinha;
    }

    public void setPorLinha(boolean porLinha) {
        this.porLinha = porLinha;
    }

    @XmlTransient
    @JsonIgnore
    public List<LayoutArquivoGrupo> getLayoutArquivoGrupoList() {
        return layoutArquivoGrupoList;
    }

    public void setLayoutArquivoGrupoList(List<LayoutArquivoGrupo> layoutArquivoGrupoList) {
        this.layoutArquivoGrupoList = layoutArquivoGrupoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idLayoutArquivo != null ? idLayoutArquivo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LayoutArquivo)) {
            return false;
        }
        LayoutArquivo other = (LayoutArquivo) object;
        if ((this.idLayoutArquivo == null && other.idLayoutArquivo != null) || (this.idLayoutArquivo != null && !this.idLayoutArquivo.equals(other.idLayoutArquivo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Layout do arquivo " + (this.descricao != null ? this.descricao : "");
    }

}
