package com.topdata.toppontoweb.services.permissoes.operador.validacoes;

import com.topdata.toppontoweb.entity.autenticacao.Operador;
import com.topdata.toppontoweb.services.Validacao;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoResponse;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;
import com.topdata.toppontoweb.utils.constantes.MSG;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ValidarOperadorSistema implements Validacao<Operador, Object> {

    @Autowired
    private TopPontoResponse topPontoResponse;

    @Override
    public Operador validar(Operador o, Object object) throws ServiceException {
        if (o.getUsuario() != null && 
                o.getUsuario().toUpperCase().equals(CONSTANTES.OPERADOR_ANONIMO)
                || o.getUsuario() != null && o.getUsuario().toUpperCase().equals(CONSTANTES.OPERADOR_MASTER)) {
            throw new ServiceException(this.topPontoResponse.alertaValidacao(MSG.OPERADOR.ALERTA_OPERADOR_SISTEMA.getResource()));
        }
        return o;
    }

}
