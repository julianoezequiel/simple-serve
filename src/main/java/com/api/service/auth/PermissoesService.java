package com.api.service.auth;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.converter.IConverter;
import com.api.dao.PermissaoRepository;
import com.api.dto.PermissaoDTO;
import com.api.entity.Permissao;
import com.api.service.ApiService;
import java.util.ArrayList;

@Service
public class PermissoesService extends ApiService implements IConverter<PermissaoDTO, Permissao> {

    @Autowired
    private PermissaoRepository permissaoRepository;
    @Autowired
    private ModuloService moduloService;

    public List<Permissao> buscarTodasPermissoes() {
        return this.permissaoRepository.findAll();
    }

    public List<PermissaoDTO> buscarTodasPermissoesDTO() {
        List<PermissaoDTO> list = this.permissaoRepository.findAll()
                .stream().map(p -> convertFromEntity(p))
                .collect(Collectors.toList());
        return list;
    }

    @Override
    public PermissaoDTO convertFromEntity(Permissao entity) {
        PermissaoDTO permissaoDTO = this.modelMapper.map(entity, PermissaoDTO.class);
        permissaoDTO.setModuloDTO(this.moduloService.convertFromEntity(entity.getModuloId()));
        return permissaoDTO;
    }

    private List<PermissaoDTO> convertFromEntityList(List<Permissao> list) {
        return list.stream().map(f->convertFromEntity(f)).collect(Collectors.toList());
    }

    @Override
    public Permissao convertFromDTO(PermissaoDTO dto) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
