package com.topdata.toppontoweb.services.validacoes;

import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoResponse;
import com.topdata.toppontoweb.services.Validacao;
import java.lang.annotation.Annotation;
import java.util.HashMap;

import java.lang.reflect.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @version 1.0.3 data 09/05/216
 * @param <T>
 * @since 1.0.3 data 09/05/2016
 *
 * @author juliano.ezequiel
 */
@Service
public class ValidarCamposObrigatorio<T> implements Validacao<T, HashMap<String, Object>> {

    @Autowired
    private TopPontoResponse topPontoResponse;

    @Override
    public T validar(T o, HashMap<String, Object> i) throws ServiceException {
        Field fieldList[] = o.getClass().getDeclaredFields();
        for (Field f : fieldList) {
            // atributo de serialização não verificamos...
            if (f.getName().equals("serialVersionUID")) {
                continue;
            }
            // para acessar atributo private...
            f.setAccessible(true);
            // pegando o valor do atributo...
            Object value = null;
            try {
                value = f.get(o);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            // comparando valor lido...
            Boolean p;
            if (value == null) {
                p = false; // sem valor
            } else {
                if (value.toString().trim().length() == 0) {
                    p = false;
                } else {
                    p = true;
                }
            }
            // false = caso coluna nao tenha anotação nullable...
            Boolean nullAble = false;
            Annotation[] anotacoes = f.getAnnotations();
            for (Annotation an : anotacoes) {
                nullAble = an.toString().contains("nullable=false");
            }
            // Regra...
            if (nullAble) // tem nullable=false, preenchimento obrigatório
            {
                if (!p) // nao preencheu
                {
                    throw new ServiceException(topPontoResponse.campoObrigatorio(f.getName()));

                }
            }
        }
        return o;
    }

}
