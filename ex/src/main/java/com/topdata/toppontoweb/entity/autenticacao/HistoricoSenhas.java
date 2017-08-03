package com.topdata.toppontoweb.entity.autenticacao;

import com.topdata.toppontoweb.entity.Entidade;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @version 1.0.1 10/04/2016
 * @since 1.0.1 10/04/2016
 * @author juliano.ezequiel
 */
@Entity
@Table(name = "HistoricoSenhas")
@XmlRootElement
public class HistoricoSenhas implements Entidade {

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "IdHistoricoSenhas")
    @SequenceGenerator(name = "seq_usu", initialValue = 1)
    @GeneratedValue(generator = "seq_usu", strategy = GenerationType.IDENTITY)
    private Integer idHistoricoSenhas;

    @Column(name = "DataHora")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataHora;

    @Size(max = 100)
    @Column(name = "Senha")
    private String senha;

    @JoinColumn(name = "IdOperador", referencedColumnName = "IdOperador")
    @ManyToOne(optional = false)
    private Operador idOperador;

    public HistoricoSenhas() {
    }

    public HistoricoSenhas(Integer idHistoricoSenhas) {
        this.idHistoricoSenhas = idHistoricoSenhas;
    }

    public HistoricoSenhas(Date dataHora, String senha, Operador operador) {
        this.dataHora = dataHora;
        this.senha = senha;
        this.idOperador = operador;
    }

    public Integer getIdHistoricoSenhas() {
        return idHistoricoSenhas;
    }

    public void setIdHistoricoSenhas(Integer idHistoricoSenhas) {
        this.idHistoricoSenhas = idHistoricoSenhas;
    }

    public Date getDataHora() {
        return dataHora;
    }

    public void setDataHora(Date dataHora) {
        this.dataHora = dataHora;
    }

    public String getSenha() {
        return senha;
    }

    /**
     *
     * @param senha
     */
    public void setSenha(String senha) {
        this.senha = senha;
    }

    /**
     *
     * @return
     */
    public Operador getIdOperador() {
        return idOperador;
    }

    /**
     *
     * @param idOperador
     */
    public void setIdOperador(Operador idOperador) {
        this.idOperador = idOperador;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idHistoricoSenhas != null ? idHistoricoSenhas.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HistoricoSenhas)) {
            return false;
        }
        HistoricoSenhas other = (HistoricoSenhas) object;
        if ((this.idHistoricoSenhas == null && other.idHistoricoSenhas != null) || (this.idHistoricoSenhas != null && !this.idHistoricoSenhas.equals(other.idHistoricoSenhas))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "HistoricoSenhas ";
    }

}
