package com.topdata.toppontoweb.rest.autenticacao;

/**
 *
 * @author juliano.ezequiel
 */
public class CustomAuthenticationProvider {

    public synchronized String getReturnStringMethod() {
        //get data from database (call your method)

        if (false) {
            return "IS_AUTHENTICATED_ANONYMOUSLY";
        }
        return "user,admin";
    }
}
