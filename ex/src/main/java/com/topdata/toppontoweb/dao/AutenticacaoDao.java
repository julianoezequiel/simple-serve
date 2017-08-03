package com.topdata.toppontoweb.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.topdata.toppontoweb.entity.autenticacao.Operador;
import com.topdata.toppontoweb.entity.autenticacao.Operador_;
import com.topdata.toppontoweb.entity.autenticacao.User;
import com.topdata.toppontoweb.utils.EmailValidator;
import com.topdata.toppontoweb.utils.constantes.MSG;

/**
 * Classe realiza a integração do Spring Security com a base de dados. Esta
 * classe esta configurada como "authentication-manager" no arquivo context.xml
 *
 * @version 1.0.1
 * @since 1.0.1
 * @data 03/05/2016
 * @author juliano.ezequiel
 */
@Repository
public class AutenticacaoDao extends Dao<Operador, Integer> implements UserDetailsService {

    @Autowired
    private MessageSource msg;

    @Autowired
    private EmailValidator emailValidator;

    private HashMap<String, Object> map;

    public AutenticacaoDao() {
        super(Operador.class);
    }

    /**
     * Realiza a autenticação do usuário com o Spring Security por email ou
     * username
     *
     * @param username
     * @return UserDetails
     * @throws UsernameNotFoundException
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            map = new HashMap<>();
            //login por email ou nome de usuário
            if (emailValidator.validate(username) == Boolean.TRUE) {
                map.put(Operador_.email.getName(), username);
            } else {
                map.put(Operador_.usuario.getName(), username);
            }
            //buscao o operador
            List<Operador> operadores = findbyAttributes(map, Operador.class);
            // caso não exista um operador retorna a lança a exceção
            if (operadores.isEmpty()) {
                throw new UsernameNotFoundException(MSG.SEGURANCA.FALHA_AUTENTICACAO.getResource());
            }
          
            //Retorna o User para a autenticacao utilizando os dados do Operador 
            return new User(operadores.get(0));
        } catch (DaoException ex) {
            throw new UsernameNotFoundException(msg.getMessage(MSG.OPERADOR.ERRO_LISTAR.getResource(), new Object[]{}, new Locale(MSG.LOCAL)), ex);
        }
    }
}
