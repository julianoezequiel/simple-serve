package com.api.converter;

import com.api.dto.EmpresaDTO;
import com.api.dto.ModuloDTO;
import com.api.dto.PermissaoDTO;
import com.api.dto.UsuarioDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.io.Serializable;

/**
 *
 * @author juliano.ezequiel
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
    @JsonSubTypes.Type(value = UsuarioDTO.class),
    @JsonSubTypes.Type(value = ModuloDTO.class),
    @JsonSubTypes.Type(value = PermissaoDTO.class),
    @JsonSubTypes.Type(value = EmpresaDTO.class)
})
public abstract class GenericDTO implements Serializable {

}
