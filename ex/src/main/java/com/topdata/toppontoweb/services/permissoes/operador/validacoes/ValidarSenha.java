package com.topdata.toppontoweb.services.permissoes.operador.validacoes;

import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.topdata.toppontoweb.entity.autenticacao.Operador;
import com.topdata.toppontoweb.services.permissoes.OperadorService;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoResponse;
import com.topdata.toppontoweb.services.Validacao;
import com.topdata.toppontoweb.utils.constantes.MSG;

/**
 * @version 1.0.4 data 30/08/2016
 * @since 1.0.3 data 13/05/2016
 *
 * @author juliano.ezequiel
 */
@Service
public class ValidarSenha implements Validacao<Operador, Object> {

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private TopPontoResponse topPontoResponse;

    @Autowired
    private OperadorService operadorService;

    private Operador operador = null;

    @Override

    public Operador validar(Operador operador, Object i) throws ServiceException {
        if (operador.getEmail() != null && !operador.getEmail().equals("")) {
            this.operador = (Operador) operadorService.buscaOperadorPorEmail(operador);
        } else if (operador.getUsuario() != null) {
            this.operador = operadorService.buscaOperadorPorNome(operador);
        } else {
            this.operador = operadorService.buscar(Operador.class, operador.getIdOperador());
        }
        if (!this.encoder.matches(operador.getSenha(), this.operador.getSenha())) {
            throw new ServiceException(
                    topPontoResponse.response(Response.Status.PRECONDITION_FAILED, MSG.OPERADOR.ALERTA_SENHA_ATUAL_NAO_CONFERE, MSG.TIPOMSG.ALERTA));
        }
        operador.setIdOperador(this.operador.getIdOperador());
        return operador;
    }

}
