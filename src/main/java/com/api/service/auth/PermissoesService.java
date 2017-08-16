package com.api.service.auth;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.converter.IConverter;
import com.api.dao.PermissaoRepository;
import com.api.dto.PermissaoDTO;
import com.api.entity.Permissao;
import com.api.service.ApiService;
import java.util.stream.Collectors;

@Service
public class PermissoesService extends ApiService implements IConverter<PermissaoDTO, Permissao> {

    @Autowired
    private PermissaoRepository permissaoRepository;

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
        return permissaoDTO;
    }

    @Override
    public Permissao convertFromDTO(PermissaoDTO dto) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
