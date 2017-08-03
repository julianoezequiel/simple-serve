package com.topdata.toppontoweb.services.permissoes.operador.validacoes;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topdata.toppontoweb.dao.Dao;
import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.entity.autenticacao.Operador;
import com.topdata.toppontoweb.entity.autenticacao.Operador_;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoResponse;
import com.topdata.toppontoweb.services.Validacao;

/**
 *
 * @author juliano.ezequiel
 */
@Service
public class ValidarNaoCad implements Validacao<Operador, Object> {

    @Autowired
    private Dao dao;

    @Autowired
    private TopPontoResponse topPontoResponse;

    private HashMap<String, Object> mapParameter;

    public ValidarNaoCad() {
        this.mapParameter = new HashMap<>();
    }

    @Override
    public Operador validar(Operador operador, Object object) throws ServiceException {

        try {
            mapParameter = new HashMap<>();
            mapParameter.put(Operador_.usuario.getName(), operador.getUsuario());
            mapParameter.put(Operador_.idOperador.getName(), operador.getIdOperador());

            List<Operador> operadors = dao.findbyAttributes(mapParameter, Operador.class);

            if (!operadors.isEmpty()) {
                throw new ServiceException(topPontoResponse.alertaNaoCad(Operador.class));
            }
        } catch (DaoException ex) {
            throw new ServiceException(topPontoResponse.erroValidar(Operador.class), ex);
        }
        return operador;
    }

}
