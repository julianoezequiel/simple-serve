package com.api.dto;

import com.api.converter.GenericDTO;
import java.util.List;

public class UsuarioDTO extends GenericDTO {

    private Integer id;
    private String email;
    private EmpresaDTO empresa;
    private List<PermissaoDTO> permissoes;

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

    public List<PermissaoDTO> getPermissoes() {
        return permissoes;
    }

    public void setPermissoes(List<PermissaoDTO> permissoes) {
        this.permissoes = permissoes;
    }

    public void setEmpresa(EmpresaDTO empresa) {
        this.empresa = empresa;
    }

    public EmpresaDTO getEmpresa() {
        return empresa;
    }

}
