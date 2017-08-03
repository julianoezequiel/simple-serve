package com.topdata.toppontoweb.services.gerafrequencia.integracao;

import javax.servlet.http.HttpServletRequest;

import com.topdata.toppontoweb.dto.gerafrequencia.GeraFrequenciaStatusTransfer;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.dto.gerafrequencia.IGeraFrequenciaTransfer;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;

/**
 *
 * @author juliano.ezequiel
 */
public interface IGeraFrequencia {

    public GeraFrequenciaStatusTransfer iniciar(HttpServletRequest request, 
            IGeraFrequenciaTransfer iGeraFrequenciaTransfer, CONSTANTES.Enum_TIPO_PROCESSO tIPO_PROCESSO) throws ServiceException;
}
