/*
 * Copyright 2016 juliano.ezequiel.
 */
package com.topdata.toppontoweb.entity.autenticacao;

import java.io.Serializable;

/**
 *
 * @author juliano.ezequiel
 */
public class Userroles implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer Id;

    private String roles;

    private User user;

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public Userroles() {
    }

    /**
     *
     * @param userid
     */
    public Userroles(Integer userid) {
        this.Id = userid;
    }

    public Userroles(String role) {
        this.roles = role;
    }

    public Integer getId() {
        return Id;
    }

    /**
     *
     * @param Id
     */
    public void setId(Integer Id) {
        this.Id = Id;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (Id != null ? Id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Userroles)) {
            return false;
        }
        Userroles other = (Userroles) object;
        if ((this.Id == null && other.Id != null) || (this.Id != null && !this.Id.equals(other.Id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.souparceiro.entity.Userroles[ userid=" + Id + " ]";
    }

}
