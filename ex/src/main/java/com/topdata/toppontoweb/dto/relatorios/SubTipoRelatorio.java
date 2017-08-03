package com.topdata.toppontoweb.dto.relatorios;

import com.topdata.toppontoweb.services.relatorios.RelatorioHandler.SUB_TIPO_RELATORIO;

/**
 *
 * @author tharle.camargo
 */
public class SubTipoRelatorio {

    private String idTipoRelatorio;
    private String descricao;

    public SubTipoRelatorio() {
        this.idTipoRelatorio = "";
        this.descricao = "";
    }

    public SubTipoRelatorio(SUB_TIPO_RELATORIO eTipoRelatorio) {
        this.idTipoRelatorio = eTipoRelatorio.getIdTipoRelatorio();
        this.descricao = eTipoRelatorio.getDescricao();
    }

    /**
     * @return the idTipoRelatorio
     */
    public String getIdTipoRelatorio() {
        return idTipoRelatorio;
    }

    /**
     * @param idTipoRelatorio the idTipoRelatorio to set
     */
    public void setIdTipoRelatorio(String idTipoRelatorio) {
        this.idTipoRelatorio = idTipoRelatorio;
    }

    /**
     * @return the descricao
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * @param descricao the descricao to set
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

}
