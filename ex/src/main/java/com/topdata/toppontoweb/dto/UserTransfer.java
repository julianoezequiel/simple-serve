package com.topdata.toppontoweb.dto;

import com.topdata.toppontoweb.entity.autenticacao.Operador;
import java.util.Map;

public class UserTransfer {

    private final String name;

    private final Map<String, Boolean> roles;

    private final OperadorTransfer operadorTransfer;

    public UserTransfer(String userName, Map<String, Boolean> roles, Operador operador) {
        this.name = userName;
        this.roles = roles;
        this.operadorTransfer = new OperadorTransfer(operador);
    }

    public String getName() {
        return this.name;
    }

    public Map<String, Boolean> getRoles() {
        return this.roles;
    }

    public OperadorTransfer getOperadorTransfer() {
        return operadorTransfer;
    }

}
