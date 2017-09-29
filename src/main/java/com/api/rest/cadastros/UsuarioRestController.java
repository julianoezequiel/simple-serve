package com.api.rest.cadastros;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.dto.RespostaDTO;
import com.api.dto.TokenDTO;
import com.api.dto.UsuarioDTO;
import com.api.dto.datatable.PaginacaoDataTableRetornoTransfer;
import com.api.dto.datatable.PaginacaoDataTableTransfer;
import com.api.rest.ApiRestController;
import com.api.service.ServiceException;
import com.api.service.cadastros.usuario.UsuarioService;

/**
 *
 * @author juliano.ezequiel
 */
@RestController
public class UsuarioRestController extends ApiRestController {

    @Autowired
    private UsuarioService usuarioService;

    /**
     * Lista os usuários do sistema
     *
     * @param transfer
     * @param request
     * @return {@link TokenDTO}
     * @throws ServiceException
     *
     */
    @RequestMapping(value = "restrict/usuarios/paginacao", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.POST)
    public ResponseEntity<PaginacaoDataTableRetornoTransfer> listar(@RequestBody PaginacaoDataTableTransfer transfer, HttpServletRequest request) throws ServiceException {
        return new ResponseEntity<>(this.usuarioService.listar(transfer, request), HttpStatus.CREATED);
    }

    @RequestMapping(value = "teste/usuario", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.GET)
    public ResponseEntity<List<UsuarioDTO>> listar() throws ServiceException {
        return new ResponseEntity<>(this.usuarioService.listarUsuariosTodosDTO(), HttpStatus.OK);
    }

    /**
     * Cadastrar novo usuario
     *
     * @param usuarioDTO
     * @param request
     * @return
     * @throws ServiceException
     */
    @RequestMapping(value = "restrict/usuario", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.POST)
    public ResponseEntity<RespostaDTO> salvar(@RequestBody UsuarioDTO usuarioDTO, HttpServletRequest request) throws ServiceException {
        return new ResponseEntity<>(new RespostaDTO(this.usuarioService.salvarUsuario(usuarioDTO, request), "Usuário salvo com sucesso!"), HttpStatus.CREATED);
    }

    /**
     * Atualizar usuario
     *
     * @param usuarioDTO
     * @param request
     * @return
     * @throws ServiceException
     */
    @RequestMapping(value = "restrict/usuario", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.PUT)
    public ResponseEntity<RespostaDTO> atualizar(@RequestBody UsuarioDTO usuarioDTO, HttpServletRequest request) throws ServiceException {
        return new ResponseEntity<>(new RespostaDTO(this.usuarioService.salvarUsuario(usuarioDTO, request), "Usuário atualizado com sucesso!"), HttpStatus.OK);
    }

    /**
     * Atualizar usuario
     *
     * @param id
     * @param request
     * @return
     * @throws ServiceException
     */
    @RequestMapping(value = "restrict/usuario/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.DELETE)
    public ResponseEntity<RespostaDTO> excluir(@RequestParam("id") Integer id, HttpServletRequest request) throws ServiceException {
        this.usuarioService.excluirUsuario(id, request);
        return new ResponseEntity<>(new RespostaDTO("Usuário excluído", HttpStatus.OK), HttpStatus.OK);
    }

    /**
     * Cadastrar novo usuario
     *
     * @param usuarioDTO
     * @return
     * @throws ServiceException
     */
    @RequestMapping(value = "teste/usuario", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.POST)
    public ResponseEntity<RespostaDTO> salvar(@RequestBody UsuarioDTO usuarioDTO) throws ServiceException {
        return new ResponseEntity<>(new RespostaDTO(this.usuarioService.salvar(usuarioDTO), "Usuário salvo com sucesso"), HttpStatus.CREATED);
    }

    @RequestMapping(value = "teste/usuario/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.GET)
    public ResponseEntity<UsuarioDTO> buscarPorId(@RequestParam("id") Integer id, HttpServletRequest request) throws ServiceException {
        return new ResponseEntity<>(this.usuarioService.buscarPorId(id, request), HttpStatus.OK);
    }
}
