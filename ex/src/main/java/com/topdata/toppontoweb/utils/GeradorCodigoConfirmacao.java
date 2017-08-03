package com.topdata.toppontoweb.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.bouncycastle.util.encoders.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.topdata.toppontoweb.entity.autenticacao.Operador;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoResponse;

/**
 * @version 1.0.4 data 30/08/2016
 * @since 1.0.4 data 30/08/2016
 *
 * @author juliano.ezequiel
 */
@Component
public class GeradorCodigoConfirmacao {

    @Autowired
    private TopPontoResponse topPontoResponse;

    public String gerar(Operador operador) throws ServiceException {
        try {
            StringBuilder signatureBuilder = new StringBuilder();
            signatureBuilder.append(operador.getEmail());

            MessageDigest digest = MessageDigest.getInstance("MD5");

            String codigo = new String(Hex.encode(digest.digest(signatureBuilder.toString().getBytes())));

            return codigo;

        } catch (NoSuchAlgorithmException ex) {
            throw new ServiceException(topPontoResponse.erroSalvar(operador.toString()), ex);
        }
    }
}
