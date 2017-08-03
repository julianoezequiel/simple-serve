package com.topdata.toppontoweb.dto.ferramentas.layout;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * @version 1.0.0 data 20/07/2017
 * @since 1.0.0 data 20/07/2017
 *
 * @author juliano.ezequiel
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LayoutGrupoEventosMotivosTransfer {

    //define se é um motivo caso contrario é somente evento
    private Boolean motivo;
    //id, tanto do evento quanto do motivo
    private Integer id;
    //descrição do evento ou do motivo
    private String descricao;

    public Boolean getMotivo() {
        return motivo;
    }

    public void setMotivo(Boolean motivo) {
        this.motivo = motivo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

}
