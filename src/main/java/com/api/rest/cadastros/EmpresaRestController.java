package com.api.rest.cadastros;

import com.api.dto.EmpresaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.api.entity.Empresa;
import com.api.rest.ApiRestController;
import com.api.service.ServiceException;
import com.api.service.cadastros.empresa.EmpresaService;
import java.util.List;

@RestController
public class EmpresaRestController extends ApiRestController {

    @Autowired
    private EmpresaService empresaService;

//    @RequestMapping(value = "restrict/empresa/paginacao", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.POST)
//    public ResponseEntity<PaginacaoDataTableRetornoTransfer> listar(@RequestBody PaginacaoDataTableTransfer transfer, HttpServletRequest request) throws ServiceException {
//        return new ResponseEntity<>(this.empresaService.listarPaginado(transfer, request), HttpStatus.CREATED);
//    }
    @RequestMapping(value = "teste/empresa", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.GET)
    public ResponseEntity<List<Empresa>> listar() throws ServiceException {
        return new ResponseEntity<>(this.empresaService.listar(), HttpStatus.CREATED);
    }

    @RequestMapping(value = "teste/empresaDTO", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.GET)
    public ResponseEntity<List<EmpresaDTO>> listarDTO() throws ServiceException {
        return new ResponseEntity<>(this.empresaService.buscarTodas(), HttpStatus.CREATED);
    }

}
