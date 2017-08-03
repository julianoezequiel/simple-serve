/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.topdata.toppontoweb.services;

import com.topdata.toppontoweb.entity.Entidade;
import com.topdata.toppontoweb.entity.Sistema;
import com.topdata.toppontoweb.entity.rep.Rep;
import com.topdata.toppontoweb.utils.constantes.MSG;
import java.util.HashMap;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author tharle.camargo
 */
@Service
public class SistemaService extends TopPontoService<Sistema, Object> {

    public Sistema buscar() throws ServiceException {
        HashMap<String, Object> map = new HashMap<>();
        List  sistemaList = buscarTodos(Sistema.class);
        
        if(sistemaList.size() > 0){
            return (Sistema) sistemaList.get(0);
        }else{
            throw new ServiceException(this.getTopPontoResponse().erro(MSG.CADASTRO.ERRO_BUSCAR, Rep.class));
        }
    }
    
    
    
}
