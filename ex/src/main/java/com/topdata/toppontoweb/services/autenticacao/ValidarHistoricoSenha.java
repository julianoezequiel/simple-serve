package com.topdata.toppontoweb.services.autenticacao;

import com.topdata.toppontoweb.dao.Dao;
import java.sql.Timestamp;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.topdata.toppontoweb.entity.autenticacao.HistoricoSenhas;
import com.topdata.toppontoweb.entity.autenticacao.Operador;
import com.topdata.toppontoweb.entity.autenticacao.Seguranca;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoResponse;
import com.topdata.toppontoweb.services.Validacao;
import com.topdata.toppontoweb.services.permissoes.OperadorService;
import com.topdata.toppontoweb.services.permissoes.SegurancaService;
import com.topdata.toppontoweb.utils.constantes.MSG;

/**
 * @version 1.0.0.0 data 31/09/2016
 * @since 1.0.0.0 data 05/05/2016
 *
 * @author juliano.ezequiel
 */
@Service
public class ValidarHistoricoSenha implements Validacao<Operador, Object> {

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private SegurancaService segurancaService;

    @Autowired
    private TopPontoResponse topPontoResponse;

    @Autowired
    private OperadorService operadorService;

    @Override
    public Operador validar(Operador operador, Object object) throws ServiceException {
        if (operador.getSenha() != null && operador.getSenha().length() < 60) {
            Seguranca seguranca = segurancaService.getSeguranca();
            if (operador.getIdOperador() != null) {
                operador.setHistoricoSenhasList(this.operadorService.buscar(Operador.class, operador.getIdOperador()).getHistoricoSenhasList());
            }
            if (operador.getHistoricoSenhasList().isEmpty()) {
                operador.setSenha(this.encoder.encode(operador.getSenha()));
                operador.getHistoricoSenhasList().add(new HistoricoSenhas(new Timestamp(new Date().getTime()), operador.getSenha(), operador));

            } else {
                /* valida o histórico de senhas */
                if (seguranca.getQtdeNaoRepetirSenhas() > 0) {
                    int index = operador.getHistoricoSenhasList().size() - 1;

                    for (int i = 0; i < seguranca.getQtdeNaoRepetirSenhas(); i++) {

                        HistoricoSenhas hs = operador.getHistoricoSenhasList().get(index--);

                        if (this.encoder.matches(operador.getSenha(), hs.getSenha())) {
                            throw new ServiceException(topPontoResponse.alertaValidacao(MSG.SEGURANCA.SENHA_RECENTE.getResource()));
                        }
                        if (index < i) {
                            break;
                        }
                    }
                }
                //criptografa a senha
                operador.setSenha(this.encoder.encode(operador.getSenha()));
                operador.getHistoricoSenhasList().add(new HistoricoSenhas(new Timestamp(new Date().getTime()), operador.getSenha(), operador));
                return operador;
            }
        }
        return operador;
    }

}
