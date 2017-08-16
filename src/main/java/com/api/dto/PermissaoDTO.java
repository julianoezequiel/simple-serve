package com.api.dto;

import com.api.converter.GenericDTO;

/**
 *
 * @author juliano.ezequiel
 */
public class PermissaoDTO extends GenericDTO {

    private Integer id;
    private String descricao;
    private ModuloDTO moduloDTO;

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

    public ModuloDTO getModuloDTO() {
        return moduloDTO;
    }

    public void setModuloDTO(ModuloDTO moduloDTO) {
        this.moduloDTO = moduloDTO;
    }

}
