package com.topdata.toppontoweb.dao;

import com.topdata.toppontoweb.entity.autenticacao.Operador;
import com.topdata.toppontoweb.entity.autenticacao.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends InterfaceDao<Operador, Integer>, UserDetailsService {
    
    public User findByName(String name) throws DaoException;

    public Operador getOperadorPorNome(String nome) throws DaoException;

}
