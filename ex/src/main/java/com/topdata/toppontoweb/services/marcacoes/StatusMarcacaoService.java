package com.topdata.toppontoweb.services.marcacoes;

import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.entity.marcacoes.StatusMarcacao;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoService;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;
import org.springframework.stereotype.Service;

/**
 *
 * @author tharle.camargo
 */
@Service
public class StatusMarcacaoService extends TopPontoService<StatusMarcacao, Object>{
    public StatusMarcacao buscar(CONSTANTES.Enum_STATUS_MARCACAO enumStatusMarcacao) throws ServiceException {
        try {
            return (StatusMarcacao) this.getDao().find(StatusMarcacao.class, enumStatusMarcacao.ordinal());
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroBuscar(), ex);
        }
    }
}
