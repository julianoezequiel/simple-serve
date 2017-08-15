package com.api.service;

import com.api.dao.EmpresaRepository;
import com.api.dto.EmpresaDTO;
import com.api.entity.Empresa;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author juliano.ezequiel
 */
@Service
public class EmpresaService {

    @Autowired
    private EmpresaRepository empresaRepository;
    @Autowired
    private ModelMapper modelMapper;

    public List<EmpresaDTO> buscarTodas() {
        List<Empresa> empresas = this.empresaRepository.findAll();
        return empresas.stream().map(empresa -> convertToDTO(empresa)).collect(Collectors.toList());

    }

    private EmpresaDTO convertToDTO(Empresa empresa) {
        EmpresaDTO empresaDTO = this.modelMapper.map(empresa, EmpresaDTO.class);
        return empresaDTO;
    }

    private Empresa convertToEmpresa(EmpresaDTO empresaDTO) {
        Empresa empresa = this.modelMapper.map(empresaDTO, Empresa.class);
        if (empresaDTO.getId() != null) {
            Empresa empresaOld = this.empresaRepository.findOne(empresaDTO.getId());
            empresa.setId(empresaOld.getId());
        }
        return empresa;
    }
}
