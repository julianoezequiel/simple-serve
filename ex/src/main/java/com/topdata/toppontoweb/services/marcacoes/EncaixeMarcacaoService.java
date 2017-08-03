package com.topdata.toppontoweb.services.marcacoes;

import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.entity.marcacoes.EncaixeMarcacao;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoService;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;
import org.springframework.stereotype.Service;

/**
 *
 * @author tharle.camargo
 */
@Service
public class EncaixeMarcacaoService extends TopPontoService<EncaixeMarcacao, Object> {
    public EncaixeMarcacao buscar(CONSTANTES.Enum_ENCAIXE_MARCACAO enumEncaixeMarcacao) throws ServiceException {
        try {
            return (EncaixeMarcacao) this.getDao().find(EncaixeMarcacao.class, enumEncaixeMarcacao.ordinal());
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroBuscar(), ex);
        }
    }
}
