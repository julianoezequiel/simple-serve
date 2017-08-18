package com.api.service.cadastros.empresa;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.converter.IConverter;
import com.api.dao.EmpresaRepository;
import com.api.dto.EmpresaDTO;
import com.api.entity.Empresa;

/**
 *
 * @author juliano.ezequiel
 */
@Service
public class EmpresaService implements IConverter<EmpresaDTO, Empresa> {

    @Autowired
    private EmpresaRepository empresaRepository;
    @Autowired
    private ModelMapper modelMapper;

    public List<Empresa> listar() {
        return this.empresaRepository.findAll();
    }

    public List<EmpresaDTO> buscarTodas() {
        List<Empresa> empresas = this.empresaRepository.findAll();
        return empresas.stream().map(empresa -> convertFromEntity(empresa)).collect(Collectors.toList());

    }

    @Override
    public EmpresaDTO convertFromEntity(Empresa entity) {
        EmpresaDTO empresaDTO = this.modelMapper.map(entity, EmpresaDTO.class);
        return empresaDTO;
    }

    @Override
    public Empresa convertFromDTO(EmpresaDTO dto) {
        Empresa empresa = this.modelMapper.map(dto, Empresa.class);
        if (dto.getId() != null) {
            Empresa empresaOld = this.empresaRepository.findOne(dto.getId());
            empresa.setId(empresaOld.getId());
        }
        return empresa;
    }
}
