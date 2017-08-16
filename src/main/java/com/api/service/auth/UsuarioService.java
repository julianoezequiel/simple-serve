package com.api.service.auth;

import com.api.converter.IConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.dao.UsuarioRepository;
import com.api.dto.PermissaoDTO;
import com.api.dto.UsuarioDTO;
import com.api.dto.datatable.PaginacaoDataTableRetornoTransfer;
import com.api.dto.datatable.PaginacaoDataTableTransfer;
import com.api.entity.Usuario;
import javax.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;

@Service
public class UsuarioService implements IConverter<UsuarioDTO, Usuario> {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private AuthService authService;
    @Autowired
    private ModelMapper modelMapper;

    public Usuario buscarPorEmail(String email) {
        return this.usuarioRepository.buscarPorEmail(email);
    }

    public PaginacaoDataTableRetornoTransfer listar(PaginacaoDataTableTransfer transfer, HttpServletRequest request) {
        return new PaginacaoDataTableRetornoTransfer(transfer.getColumns(), Integer.SIZE, Long.MIN_VALUE, Long.MIN_VALUE);
    }

    @Override
    public UsuarioDTO convertFromEntity(Usuario entity) {
        UsuarioDTO usuarioDTO = this.modelMapper.map(entity, UsuarioDTO.class);
        return usuarioDTO;
    }

    @Override
    public Usuario convertFromDTO(UsuarioDTO dto) {
        Usuario usuario = this.modelMapper.map(dto, Usuario.class);
        if (dto.getId() != null) {
            Usuario usuarioOld = this.usuarioRepository.findOne(dto.getId());
            usuario.setId(usuarioOld.getId());
        }
        return usuario;
    }
}
