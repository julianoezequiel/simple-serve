package com.topdata.toppontoweb.entity.configuracoes;

import com.topdata.toppontoweb.entity.Entidade;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author tharle.camargo
 */
@Entity
@Table(name = "ConfiguracoesGerais")
@XmlRootElement
public class ConfiguracoesGerais  implements Entidade {
    
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "IdConfiguracoesGerais")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idConfiguracoesGerais;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "LimiteCorteEntrada")
    @Temporal(TemporalType.TIMESTAMP)
    private Date limiteCorteEntrada;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "LimiteCorteSaida")
    @Temporal(TemporalType.TIMESTAMP)
    private Date limiteCorteSaida;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "MarcacoesDiaSeguinteViradaDia")
    private Boolean marcacoesDiaSeguinteViradaDia;

    public ConfiguracoesGerais() {
        this.idConfiguracoesGerais = 0;
        this.limiteCorteEntrada = new Date(0);
        this.limiteCorteSaida = new Date(0);
        this.marcacoesDiaSeguinteViradaDia = false;
    }

    public ConfiguracoesGerais(Integer idConfiguracoesGerais, Integer limiteCorteEntradaEmHoras, Integer limiteCorteSaidaEmHoras, Boolean marcacoesDiaSeguinteViradaDia) {
        this.idConfiguracoesGerais = idConfiguracoesGerais;
        this.limiteCorteEntrada = new Date(limiteCorteEntradaEmHoras);
        this.limiteCorteSaida = new Date(limiteCorteSaidaEmHoras);
        this.marcacoesDiaSeguinteViradaDia = marcacoesDiaSeguinteViradaDia;
    }
    

    public Integer getIdConfiguracoesGerais() {
        return idConfiguracoesGerais;
    }

    public void setIdConfiguracoesGerais(Integer idConfiguracoesGerais) {
        this.idConfiguracoesGerais = idConfiguracoesGerais;
    }

    /**
     * @return the limiteCorteEntrada
     */
    public Date getLimiteCorteEntrada() {
        return limiteCorteEntrada;
    }

    /**
     * @param limiteCorteEntrada the limiteCorteEntrada to set
     */
    public void setLimiteCorteEntrada(Date limiteCorteEntrada) {
        this.limiteCorteEntrada = limiteCorteEntrada;
    }

    /**
     * @return the limiteCorteSaida
     */
    public Date getLimiteCorteSaida() {
        return limiteCorteSaida;
    }

    /**
     * @param limiteCorteSaida the limiteCorteSaida to set
     */
    public void setLimiteCorteSaida(Date limiteCorteSaida) {
        this.limiteCorteSaida = limiteCorteSaida;
    }

    /**
     * @return the marcacoesDiaSeguinteViradaDia
     */
    public Boolean getMarcacoesDiaSeguinteViradaDia() {
        return marcacoesDiaSeguinteViradaDia;
    }

    /**
     * @param marcacoesDiaSeguinteViradaDia the marcacoesDiaSeguinteViradaDia to set
     */
    public void setMarcacoesDiaSeguinteViradaDia(Boolean marcacoesDiaSeguinteViradaDia) {
        this.marcacoesDiaSeguinteViradaDia = marcacoesDiaSeguinteViradaDia;
    }
    
    
    
}
