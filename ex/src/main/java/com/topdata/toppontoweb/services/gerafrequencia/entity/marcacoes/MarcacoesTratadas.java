package com.topdata.toppontoweb.services.gerafrequencia.entity.marcacoes;

import java.util.Date;

/**
 * Marcação que foi atualizada
 *
 * @author enio.junior
 */
public class MarcacoesTratadas {

    private Date horarioMarcacao;
    private String idLegenda;
    private String descricaoMotivo;

    public MarcacoesTratadas() {
        this.idLegenda = "";
        this.descricaoMotivo = "";
    }

    public Date getHorarioMarcacao() {
        return horarioMarcacao;
    }

    public void setHorarioMarcacao(Date horarioMarcacao) {
        this.horarioMarcacao = horarioMarcacao;
    }

    public String getIdLegenda() {
        return idLegenda;
    }

    public void setIdLegenda(String idLegenda) {
        this.idLegenda = idLegenda;
    }

    public String getDescricaoMotivo() {
        return descricaoMotivo;
    }

    public void setDescricaoMotivo(String descricaoMotivo) {
        this.descricaoMotivo = descricaoMotivo;
    }

}
