package com.topdata.toppontoweb.services.autenticacao;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topdata.toppontoweb.dao.Dao;
import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.entity.autenticacao.HistoricoSenhas;
import com.topdata.toppontoweb.entity.autenticacao.HistoricoSenhas_;
import com.topdata.toppontoweb.entity.autenticacao.Operador;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoResponse;
import com.topdata.toppontoweb.services.Validacao;
import com.topdata.toppontoweb.services.permissoes.SegurancaService;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;
import com.topdata.toppontoweb.utils.constantes.MSG;

/**
 * Realiza a verificação entre as datas da -ultima troca de senha do Operador
 * com a data máxima configurada para o sistema.
 *
 * @see Validacao
 *
 * @version 1.0.0.0 data 02/05/2016
 * @since 1.0.0.0 data 02/05/2016
 *
 * @author juliano.ezequiel
 */
@Service
public class ValidarDataTrocarSenha implements Validacao<Operador, Object> {

    @Autowired
    private Dao dao;

    @Autowired
    private SegurancaService segurancaService;

    @Autowired
    private TopPontoResponse topPontoResponse;

    private HashMap<String, Object> map;

    @Override
    public Operador validar(Operador operador, Object object) throws ServiceException {
        try {
            if (!operador.getUsuario().equals(CONSTANTES.OPERADOR_MASTER) && segurancaService.getSeguranca().getQtdeDiasTrocaSenha() > 0) {
                map = new HashMap<>();
                map.put(HistoricoSenhas_.idOperador.getName(), operador);
                List<HistoricoSenhas> historicoSenhasList = dao.findbyAttributes(map, HistoricoSenhas.class);

                if (historicoSenhasList.size() > 0) {
                    HistoricoSenhas historicoSenhas = historicoSenhasList.get(historicoSenhasList.size() - 1);

                    Calendar dataCadastroSenha = Calendar.getInstance();
                    dataCadastroSenha.setTime(historicoSenhas.getDataHora());
                    dataCadastroSenha.add(Calendar.DAY_OF_MONTH, segurancaService.getSeguranca().getQtdeDiasTrocaSenha());

                    if (Calendar.getInstance().after(dataCadastroSenha)) {
                        operador.setTrocaSenhaProximoAcesso(Boolean.TRUE);
                        operador = (Operador) dao.save(operador);
                        throw new ServiceException(topPontoResponse.alertaTrocaSenha(MSG.OPERADOR.ALERTA_TROCAR_SENHA_EXPIRADA.getResource(), operador));
                    }
                }
            }
        } catch (DaoException ex) {
            throw new ServiceException(topPontoResponse.erroValidar(Operador.class), ex);
        }
        return operador;
    }

}
