package com.topdata.toppontoweb.utils.constantes;

/**
 * @version 1.0.0 data 14/12/2016
 * @since 1.0.0 data 14/12/2016
 *
 * @author juliano.ezequiel
 */
public class Regex {

    private static final String PATTERN_STRING = "\\d \\w \\u00C0-\\u00FF \\.\\/\"\\!\\@\\#\\$\\%\\¨\\&\\*\\(\\)\\-\\+\\=\\}\\,\\<\\>\\;\\?\\|]+";

    private static final String PATTERN_NUMEROS = "\\d";

    public static String validarString(String s) {
        return s.replaceAll(PATTERN_STRING, "");
    }

    public static String validarNumeros(String s) {
        return s.replaceAll(PATTERN_NUMEROS, "");
    }
}
