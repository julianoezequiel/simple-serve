package com.topdata.toppontoweb.services.relatorios;

import com.topdata.toppontoweb.dto.FormatoTransfer;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author tharle.camargo
 */
@Service
public class FormatoServices {

    public List<FormatoTransfer> buscarTodos() {

        List<FormatoTransfer> lista = new ArrayList<>();
        lista.add(new FormatoTransfer(CONSTANTES.Enum_FORMATO.PDF));
//        lista.add(new FormatoTransfer(CONSTANTES.Enum_FORMATO.WORD));
        lista.add(new FormatoTransfer(CONSTANTES.Enum_FORMATO.EXCEL));
        lista.add(new FormatoTransfer(CONSTANTES.Enum_FORMATO.TEXTO));
//        lista.add(new FormatoTransfer(CONSTANTES.Enum_FORMATO.RTF));
//        lista.add(new FormatoTransfer(CONSTANTES.Enum_FORMATO.ODT));
//        lista.add(new FormatoTransfer(CONSTANTES.Enum_FORMATO.ODS));
        lista.add(new FormatoTransfer(CONSTANTES.Enum_FORMATO.TIFF));
        lista.add(new FormatoTransfer(CONSTANTES.Enum_FORMATO.CSV));

        return lista;
    }

}
