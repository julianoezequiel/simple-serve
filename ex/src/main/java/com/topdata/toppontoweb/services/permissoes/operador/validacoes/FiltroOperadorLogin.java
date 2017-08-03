package com.topdata.toppontoweb.services.permissoes.operador.validacoes;

import com.topdata.toppontoweb.entity.autenticacao.Operador;
import com.topdata.toppontoweb.services.Validacao;
import com.topdata.toppontoweb.services.ServiceException;
import org.springframework.stereotype.Service;

@Service
public class FiltroOperadorLogin implements Validacao<Operador, Object> {

    @Override
    public Operador validar(Operador o, Object object) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
