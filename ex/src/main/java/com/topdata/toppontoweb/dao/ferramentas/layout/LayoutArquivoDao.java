package com.topdata.toppontoweb.dao.ferramentas.layout;

import com.topdata.toppontoweb.dao.Dao;
import com.topdata.toppontoweb.entity.ferramentas.LayoutArquivo;
import org.springframework.stereotype.Repository;

/**
 * @version 1.0.0 data 20/07/2017
 * @since 1.0.0 data 20/07/2017
 *
 * @author juliano.ezequiel
 */
@Repository
public class LayoutArquivoDao extends Dao<LayoutArquivo, Object> {

    public LayoutArquivoDao() {
        super(LayoutArquivo.class);
    }

}
