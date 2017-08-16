package com.api.dto;

import com.api.converter.GenericDTO;
import com.api.entity.Empresa;

public class UsuarioDTO extends GenericDTO {

    private Integer id;
    private String email;
    private Empresa empresaId;
    private PermissaoDTO permissoes;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Empresa getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(Empresa empresaId) {
        this.empresaId = empresaId;
    }

    public PermissaoDTO getPermissoes() {
        return permissoes;
    }

    public void setPermissoes(PermissaoDTO permissoes) {
        this.permissoes = permissoes;
    }

}
