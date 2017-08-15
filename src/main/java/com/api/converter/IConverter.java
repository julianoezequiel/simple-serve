package com.api.converter;

/**
 *
 * @author juliano.ezequiel
 * @param <DTO>
 * @param <ENT>
 *
 */
public interface IConverter<DTO extends GenericDTO, ENT extends GenericEntity> {

    public DTO convertFromEntity(ENT entity);

    public ENT convertFromDTO(DTO dto);

}
