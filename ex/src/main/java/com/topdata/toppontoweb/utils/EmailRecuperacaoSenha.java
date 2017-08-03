package com.topdata.toppontoweb.utils;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import com.topdata.toppontoweb.entity.autenticacao.Operador;
import com.topdata.toppontoweb.entity.funcionario.Funcionario;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoResponse;
import com.topdata.toppontoweb.services.funcionario.FuncionarioService;
import com.topdata.toppontoweb.utils.constantes.MSG;

/**
 * @version 1.0.4 data 31/08/2016
 * @since 1.0.4 data 31/06/2016
 *
 * @author juliano.ezequiel
 */
@Service
public class EmailRecuperacaoSenha {

    @Autowired
    private MailSender mailSender;

    @Autowired
    private TopPontoResponse topPontoResponse;

    @Autowired
    private FuncionarioService funcionarioService;

    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Value("${email.recuperacao}")
    private String Link;
    @Value("${email.assunto.recuperacao}")
    private String assunto;
    @Value("${email.origem}")
    private String from;
    @Value("${ip.servidor2}")
    private String ipServidor2;
    @Value("${habilitar.servidor2}")
    private String habilitarServidor2;

    public void enviarEmail(HttpServletRequest request, Operador operador) throws ServiceException {
        try {

            String linkFinal = Utils.getLinkValido(request, Link, ipServidor2, habilitarServidor2);

            Funcionario funcionario = this.funcionarioService.buscarPorOperador(operador.getIdOperador());

            if (Objects.isNull(operador.getEmail()) || Objects.equals(operador.getEmail(), "")) {
                throw new ServiceException(this.topPontoResponse.alertaValidacao(MSG.OPERADOR.ALERTA_EMAIL_NAO_CADASTRADO.getResource()));
            }

            SimpleMailMessage message = new SimpleMailMessage();
            String nome = funcionario != null ? funcionario.getNome().split(" ")[0] : operador.getUsuario();

            String msg = "Prezado " + nome + ",\n"
                    + "\n"
                    + "Clique no link " + linkFinal + operador.getCodigoConfirmacaoNovaSenha() + " para alterar a sua senha.\n"
                    + "\n"
                    + "Atenciosamente,\n"
                    + "Administrador TopPonto";

            message.setFrom(from);
            message.setTo(operador.getEmail());
            message.setSubject(assunto);
            message.setText(msg);
            this.mailSender.send(message);
        } catch (ServiceException | MailException e) {
            Logger.getLogger(this.getClass().getSimpleName()).log(Level.SEVERE, e.getMessage());
            throw new ServiceException(this.topPontoResponse.alertaValidacao(MSG.OPERADOR.ALERTA_EMAIL_NAO_ENVIADO.getResource()));
        }
    }

}
