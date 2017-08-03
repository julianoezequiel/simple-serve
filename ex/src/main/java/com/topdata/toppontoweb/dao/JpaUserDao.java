/**
* @Author: juliano.ezequiel <Juliano>
* @Date:   02-09-2016
* @Email:  juliano.ezequiel@topdata.com.br
* @Project: TopPontoWeb
* @Last modified by:   Juliano
* @Last modified time: 28-10-2016
*/
package com.topdata.toppontoweb.dao;

import com.topdata.toppontoweb.entity.autenticacao.Operador;
import com.topdata.toppontoweb.entity.autenticacao.User;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

public class JpaUserDao extends Dao<Operador, Integer> implements UserDao {

    public JpaUserDao() {
        super(Operador.class);
    }

    private Query query = null;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.findByName(username);
        if (null == user) {
            throw new UsernameNotFoundException("O usuário " + username + " não foi encontrado");
        }
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public User findByName(String name) {
        final CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        final CriteriaQuery<Operador> criteriaQuery = builder.createQuery(this.entityClass);

        Root<Operador> root = criteriaQuery.from(this.entityClass);
        Path<String> namePath = root.get("usuario");
        criteriaQuery.where(builder.equal(namePath, name));

        TypedQuery<Operador> typedQuery = getEntityManager().createQuery(criteriaQuery);
        List<Operador> users = typedQuery.getResultList();

        if (users.isEmpty()) {
            return null;
        }
        return new User(users.get(0));
        //return users.iterator().next();
    }

    @Override
    public Operador getOperadorPorNome(String usuario) {
        ArrayList<Operador> operadores;
        try {
            query = getEntityManager().createQuery("SELECT OBJECT(o) FROM Operador as o WHERE o.nome = :usuario").setParameter("usuario", usuario);
            operadores = (ArrayList<Operador>) query.getResultList();
        } catch (Exception e) {
            System.err.printf(e.getLocalizedMessage());
            return null;
        }
        return operadores.size() > 0 ? operadores.get(0) : null;
    }

}
