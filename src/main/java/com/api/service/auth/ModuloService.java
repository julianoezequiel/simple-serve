package com.api.service.auth;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.converter.IConverter;
import com.api.dao.ModuloRepository;
import com.api.dto.ModuloDTO;
import com.api.entity.Modulo;
import com.api.service.ApiService;

@Service
public class ModuloService extends ApiService implements IConverter<ModuloDTO, Modulo> {

    @Autowired
    private ModuloRepository moduloRepository;

    public List<Modulo> buscarTodosModulos() {
        return this.moduloRepository.findAll();
    }

    public List<ModuloDTO> buscarTodosModulosDTO() {
        List<Modulo> list = this.moduloRepository.findAll();
        List<ModuloDTO> list1 = list.stream().map(m -> convertFromEntity(m)).collect(Collectors.toList());
        return list1;
    }

    @Override
    public ModuloDTO convertFromEntity(Modulo entity) {
        ModuloDTO moduloDTO = this.modelMapper.map(entity, ModuloDTO.class);
        return moduloDTO;
    }

    @Override
    public Modulo convertFromDTO(ModuloDTO dto) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
