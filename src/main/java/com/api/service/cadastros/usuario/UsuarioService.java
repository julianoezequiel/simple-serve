package com.api.service.cadastros.usuario;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.converter.IConverter;
import com.api.dao.UsuarioRepository;
import com.api.dto.UsuarioDTO;
import com.api.dto.datatable.PaginacaoDataTableRetornoTransfer;
import com.api.dto.datatable.PaginacaoDataTableTransfer;
import com.api.entity.Usuario;
import com.api.service.ApiService;
import com.api.service.ServiceException;
import com.api.service.auth.AuthService;
import com.api.service.auth.PermissoesService;
import com.api.service.cadastros.empresa.EmpresaService;
import org.springframework.http.HttpStatus;

@Service
public class UsuarioService extends ApiService implements IConverter<UsuarioDTO, Usuario> {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private AuthService authService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private EmpresaService empresaService;
    @Autowired
    private PermissoesService permissoesService;

    public Usuario buscarPorEmail(String email) {
        return this.usuarioRepository.buscarPorEmail(email);
    }

    public UsuarioDTO buscarPorId(Integer id, HttpServletRequest request) {
        return convertFromEntity(this.usuarioRepository.findOne(id));
    }

    public List<UsuarioDTO> listarUsuariosTodosDTO() {
        return this.usuarioRepository.findAll().stream().map(u -> convertFromEntity(u)).collect(Collectors.toList());
    }

    public PaginacaoDataTableRetornoTransfer listar(PaginacaoDataTableTransfer transfer, HttpServletRequest request) {
        return new PaginacaoDataTableRetornoTransfer(transfer.getColumns(), Integer.SIZE, Long.MIN_VALUE, Long.MIN_VALUE);
    }

    public UsuarioDTO salvarUsuario(UsuarioDTO usuarioDTO, HttpServletRequest request) throws ServiceException {
        Usuario usuarioSessao = usuarioAutenticado(request);

        Usuario usuario = convertFromDTO(usuarioDTO);
        //email obrigatório
        if (usuarioDTO.getEmail() == null) {
            throw new ServiceException(HttpStatus.PRECONDITION_FAILED, "Email é obrigatório");
        }
        if (this.usuarioRepository.buscarPorEmail(usuarioDTO.getEmail()) != null) {
            throw new ServiceException(HttpStatus.PRECONDITION_FAILED, "Email já cadastrado");
        }
        //adiciona a empresa do usuario da sessão
        usuario.setEmpresaId(usuarioSessao.getEmpresaId());
        return convertFromEntity(this.usuarioRepository.save(usuario));
    }

    public UsuarioDTO salvar(UsuarioDTO usuarioDTO) throws ServiceException {

        Usuario usuario = convertFromDTO(usuarioDTO);

        if (usuarioDTO.getEmail() == null) {
            throw new ServiceException(HttpStatus.PRECONDITION_FAILED, "Email é obrigatório");
        }
        return convertFromEntity(this.usuarioRepository.save(usuario));
    }

    public void excluirUsuario(Integer id, HttpServletRequest request) throws ServiceException {
        Usuario usuario = this.usuarioRepository.findOne(id);
        if (usuario.getAdmin()) {
            throw new ServiceException(HttpStatus.FORBIDDEN, "Operação não permitida, entre em contado com o administrador do sistema");
        } else {
            this.usuarioRepository.delete(usuario);
        }
    }

    @Override
    public UsuarioDTO convertFromEntity(Usuario entity) {
        UsuarioDTO usuarioDTO = null;
        if (entity != null) {
            usuarioDTO = this.modelMapper.map(entity, UsuarioDTO.class);
            usuarioDTO.setEmpresa(this.empresaService.convertFromEntity(entity.getEmpresaId()));
            if (entity.getPermissaoList() != null) {
                usuarioDTO.setPermissoes(entity.getPermissaoList().stream().map(f -> this.permissoesService.convertFromEntity(f)).collect(Collectors.toList()));
            }
        }
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
