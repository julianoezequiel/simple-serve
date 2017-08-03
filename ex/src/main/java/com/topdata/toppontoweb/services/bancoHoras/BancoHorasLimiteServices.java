package com.topdata.toppontoweb.services.bancoHoras;

//<editor-fold defaultstate="collapsed" desc="IMPORTS">
import java.util.List;

import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.dao.bancohoras.BancoHorasLimiteDao;
import com.topdata.toppontoweb.entity.bancohoras.BancoHorasLimite;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoService;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;
import com.topdata.toppontoweb.utils.constantes.MSG;
//</editor-fold>

/**
 * @version 1.0.0.0 data 18/01/2017
 * @since 1.0.0.0 data 18/08/2016
 *
 * @author juliano.ezequiel
 */
@Service
public class BancoHorasLimiteServices extends TopPontoService<BancoHorasLimite, Object> {

    @Autowired
    BancoHorasLimiteDao bancoHorasLimiteDao;
    
    public Response salvar(List<BancoHorasLimite> bancoHorasLimiteList) throws ServiceException {

        try {
            for (BancoHorasLimite bancoHorasLimiteNovo : bancoHorasLimiteList) {
                CONSTANTES.Enum_OPERACAO operacao = null;
                //busca o registro original
                BancoHorasLimite bancoHorasLimiteOriginal = this.bancoHorasLimiteDao.find(BancoHorasLimite.class, bancoHorasLimiteNovo.getBancoHorasLimitePK());
                //se o original for diferente do novo verifica se existe fechamento para empresa em algum funcionario
                if (bancoHorasLimiteOriginal != null) {
                    operacao = CONSTANTES.Enum_OPERACAO.EDITAR;
                } else if (bancoHorasLimiteOriginal == null) {
                    operacao = CONSTANTES.Enum_OPERACAO.INCLUIR;
                }
                if (operacao != null) {
                    bancoHorasLimiteNovo.getLimite();
                    this.getDao().save(bancoHorasLimiteNovo);
                    this.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_BANCO_HORAS, CONSTANTES.Enum_AUDITORIA.DEFAULT, operacao, bancoHorasLimiteNovo);
                }
            }
        } catch (DaoException ex) {
            throw new ServiceException(ex);
        }

        return this.getTopPontoResponse().sucessoSalvar(new BancoHorasLimite().toString());

    }

    public List<BancoHorasLimite> buscarPorIdBancoHoras(Class<BancoHorasLimite> aClass, Integer id) throws ServiceException {
        try {
            return bancoHorasLimiteDao.findAllByIdBancoHoras(id);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erro(MSG.CADASTRO.ERRO_BUSCAR.getResource(), new BancoHorasLimite().toString()), ex);
        }
    }

}
