package com.topdata.toppontoweb.services.configuracoes;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.springframework.stereotype.Service;

import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.entity.configuracoes.ConfiguracoesGerais;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoService;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;

/**
 *
 * @author tharle.camargo
 */
@Service
public class ConfiguracoesGeraisServices extends TopPontoService<ConfiguracoesGerais, Object> {

    
    //<editor-fold  desc="CRUD">
    public ConfiguracoesGerais buscarConfiguracoesGerais() throws ServiceException{
        List<ConfiguracoesGerais> confGeraisList = new ArrayList<>();
        try {
            confGeraisList = this.getDao().findAll(ConfiguracoesGerais.class);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroBuscar(), ex);
        }
        
        
        if(!confGeraisList.isEmpty()){
            return confGeraisList.get(0);
        }else{
            throw new ServiceException(this.getTopPontoResponse().erroBuscar());
        }
    }
    
    @Override
    public Response atualizar(ConfiguracoesGerais configuracoesGerais) throws ServiceException {
        try {
            configuracoesGerais = (ConfiguracoesGerais) this.getDao().save(validarAtualizar(configuracoesGerais));
            this.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_CONFIGURACOES, CONSTANTES.Enum_AUDITORIA.CAD_CONFIGURACOES_GERAIS, CONSTANTES.Enum_OPERACAO.EDITAR, configuracoesGerais);
            return this.getTopPontoResponse().sucesso();
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroAtualizar(ConfiguracoesGerais.class), ex);
        }
    }
    //</editor-fold>

    private ConfiguracoesGerais validarAtualizar(ConfiguracoesGerais configuracoesGerais) {
        //TODO IMPLEMENTAR
        return configuracoesGerais;
    }
    
}
