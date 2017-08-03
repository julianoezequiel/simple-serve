package com.topdata.toppontoweb.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

/**
 * Realiza a validação do Email. 
 *
 * @author juliano.ezequiel
 */
@Component
public class EmailValidator {

    private final Pattern pattern;
    private Matcher matcher;

    private static final String EMAIL_PATTERN
            = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public EmailValidator() {
        pattern = Pattern.compile(EMAIL_PATTERN);
    }

    /**
     * Realiza a validação do formato de email
     *
     * @param email String para validar
     * @return Booleano
     */
    public boolean validate(final String email) {
        if (email != null && !"".equals(email)) {
            matcher = pattern.matcher(email);
            return matcher.matches();
        }
        return true;
    }
}
