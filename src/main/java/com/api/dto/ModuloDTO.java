package com.api.dto;

import com.api.constantes.CONSTANTES;
import com.api.converter.GenericDTO;
import com.api.entity.Permissao;
import java.util.List;

/**
 *
 * @author juliano.ezequiel
 */
public class ModuloDTO extends GenericDTO {

    private Integer id;
    private String descricao;
    private CONSTANTES.MODULOS modulo;
    private List<Permissao> permissaoList;

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

    public List<Permissao> getPermissaoList() {
        return permissaoList;
    }

    public void setPermissaoList(List<Permissao> permissaoList) {
        this.permissaoList = permissaoList;
    }

    public CONSTANTES.MODULOS getModulo() {
        return modulo;
    }

    public void setModulo(CONSTANTES.MODULOS modulo) {
        this.modulo = modulo;
    }

}
