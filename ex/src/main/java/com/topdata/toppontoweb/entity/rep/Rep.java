package com.topdata.toppontoweb.entity.rep;

import com.topdata.toppontoweb.entity.Entidade;
import com.topdata.toppontoweb.entity.marcacoes.Marcacoes;
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
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonView;

import com.topdata.toppontoweb.entity.empresa.Cei;
import com.topdata.toppontoweb.entity.empresa.Empresa;
import com.topdata.toppontoweb.entity.tipo.TipoEquipamento;
import com.topdata.toppontoweb.rest.autenticacao.JsonViews;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

/**
 * Classe entidade do Rep
 *
 * @version 1.0.1 10/04/2016
 * @since 1.0.1 10/04/2016
 *
 * @author juliano.ezequiel
 */
@Entity
@Table(name = "Rep")
@XmlRootElement
public class Rep implements Entidade {

    @JoinColumn(name = "IdEmpresa", referencedColumnName = "IdEmpresa")
    @ManyToOne(optional = false)
    private Empresa empresa;

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "IdRep")
    @SequenceGenerator(name = "seq_usu", initialValue = 1)
    @GeneratedValue(generator = "seq_usu", strategy = GenerationType.IDENTITY)
    @JsonView(JsonViews.Audit.class)
    private Integer idRep;

    @Basic(optional = false)
    @NotNull
    @Column(name = "Nsr")
    private String nsr;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "rep")
    private List<Marcacoes> marcacoesList;

    @Column(name = "NumeroRep")
    private Integer numeroRep;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 17)
    @Column(name = "NumeroSerie")
    private String numeroSerie;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "NumeroFabricante")
    private String numeroFabricante;

    @Size(min = 1, max = 10)
    @Column(name = "NumeroModelo")
    private String numeroModelo;

    @Size(min = 1, max = 150)
    @Column(name = "Arquivo")
    private String arquivo;

    @JoinColumn(name = "idCei", referencedColumnName = "idCei")
    @ManyToOne
    private Cei cei;

    @JoinColumn(name = "IdModeloRep", referencedColumnName = "IdModeloRep")
    @ManyToOne
    private ModeloRep modeloRep;

    @JoinColumn(name = "idTipoEquipamento", referencedColumnName = "idTipoEquipamento")
    @ManyToOne
    private TipoEquipamento tipoEquipamento;
    
    @Column(name = "FormatoNumeroDigitos")
    private Integer formatoNumeroDigitos;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "Local")
    private String local;
    
    @Transient
    private boolean existeMarcacoes = false;

    public Rep() {
    }

    public Rep(Integer idRep) {
        this.idRep = idRep;
    }

    public Rep(Integer idRep, String numeroSerie, String numeroFabricante, String nsr, String arquivo) {
        this.idRep = idRep;
        this.numeroSerie = numeroSerie;
        this.numeroFabricante = numeroFabricante;
        this.nsr = nsr;
        this.arquivo = arquivo;
    }

    public Integer getIdRep() {
        return idRep;
    }

    public void setIdRep(Integer idRep) {
        this.idRep = idRep;
    }

    public Integer getNumeroRep() {
        return numeroRep;
    }

    public void setNumeroRep(Integer numeroRep) {
        this.numeroRep = numeroRep;
    }

    public String getNumeroSerie() {
        return numeroSerie;
    }

    public void setNumeroSerie(String numeroSerie) {      
        this.numeroSerie = numeroSerie;
    }

    public String getNumeroFabricante() {
        return numeroFabricante;
    }

    public void setNumeroFabricante(String numeroFabricante) {
        this.numeroFabricante = StringUtils.leftPad(numeroFabricante, 5, "0");
    }
    
     public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getArquivo() {
        return arquivo;
    }

    public void setArquivo(String arquivo) {
        this.arquivo = arquivo;
    }

    public ModeloRep getModeloRep() {
        return modeloRep;
    }

    public void setModeloRep(ModeloRep modeloRep) {
        this.modeloRep = modeloRep;
    }

    public TipoEquipamento getTipoEquipamento() {
        return tipoEquipamento;
    }

    public void setTipoEquipamento(TipoEquipamento tipoEquipamento) {
        this.tipoEquipamento = tipoEquipamento;
    }

    @XmlTransient
    @JsonIgnore
    public List<Marcacoes> getMarcacoesList() {
        return marcacoesList;
    }

    public void setMarcacoesList(List<Marcacoes> marcacoesList) {
        this.marcacoesList = marcacoesList;
    }

    public void setExisteMarcacoes(boolean existeMarcacoes) {
        this.existeMarcacoes = existeMarcacoes;
    }
    
    public boolean isExisteMarcacoes(){
        return this.existeMarcacoes;
    }

    public String getNsr() {
        return nsr;
    }

    public void setNsr(String nsr) {
        this.nsr = nsr;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idRep != null ? idRep.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Rep)) {
            return false;
        }
        Rep other = (Rep) object;
        if ((this.idRep == null && other.idRep != null) || (this.idRep != null && !this.idRep.equals(other.idRep))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String s = this.numeroSerie != null ? this.numeroSerie : "";
        return "Rep " + s;
    }

    public Cei getCei() {
        return cei;
    }

    public void setCei(Cei cei) {
        this.cei = cei;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public String getNumeroModelo() {
        return numeroModelo;
    }

    public void setNumeroModelo(String numeroModelo) {
        this.numeroModelo = StringUtils.leftPad(numeroModelo, 5, "0");
    }

    /**
     * @return the formatoNumeroDigitos
     */
    public Integer getFormatoNumeroDigitos() {
        return formatoNumeroDigitos;
    }

    /**
     * @param formatoNumeroDigitos the formatoNumeroDigitos to set
     */
    public void setFormatoNumeroDigitos(Integer formatoNumeroDigitos) {
        this.formatoNumeroDigitos = formatoNumeroDigitos;
    }
    
    /**
     * Aplica o formato cadastrado no REP no numero do cartao
     * @param  numeroCartao 
     * @return 
     */    
    public String aplicarFormatoCartao(String numeroCartao) {
        String verdadeiroNumeroCartao = "";
        for(int i = 0; i < 16; i++){
            Long casa  = Math.round(Math.pow(2, i));
            if((this.formatoNumeroDigitos & casa) != 0){
                verdadeiroNumeroCartao+= numeroCartao.substring(i, i+1);
            }
        }
        
        return verdadeiroNumeroCartao;
    }
    
    
    

}
