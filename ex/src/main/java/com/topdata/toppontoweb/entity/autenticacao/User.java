package com.topdata.toppontoweb.entity.autenticacao;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * @author juliano.ezequiel
 */
public class User implements UserDetails {

    private String name;
    private String password;
    private Operador operador;
    private String IP;

    public User() {
    }

    public User(Operador operador) {
        this.name = operador.getUsuario();
        this.password = operador.getSenha();
        this.operador = operador;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Operador getOperador() {
        return operador;
    }

    public void setOperador(Operador operador) {
        this.operador = operador;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        if (this.getOperador().getGrupo().getPermissoesList() == null) {
            return Collections.emptyList();
        }

        Set<GrantedAuthority> authorities = new HashSet<>();
        for (Permissoes permissoes : this.getOperador().getGrupo().getPermissoesList()) {
            authorities.add(new SimpleGrantedAuthority(permissoes.getPermissoes()));
        }
        return authorities;
    }

    @Override
    public String getUsername() {
        return this.name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getPassword() {
        return this.password;
    }
//</editor-fold>

}
