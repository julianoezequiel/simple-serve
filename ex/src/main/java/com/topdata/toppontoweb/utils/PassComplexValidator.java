package com.topdata.toppontoweb.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

/**
 *
 * @author juliano.ezequiel
 */
@Component
public class PassComplexValidator {

    private final Pattern patternNum;
    
    private static final String SENHA_PATTERN = "((?=.*\\d)(?=.*[a-zA-Z \\u00C0-\\u00FF \\.\\/\\\"\\!\\@\\#\\$\\%\\¨\\&\\*\\(\\)\\-\\+\\=\\}\\,\\<\\>\\;\\?\\|\\:]).+)";
    

    public PassComplexValidator() {
        patternNum = Pattern.compile(SENHA_PATTERN);
    }

    public boolean validate(final String hex) {
        if (hex == null) {
            return false;
        }
        Matcher matcherNum = patternNum.matcher(hex);
    
        return matcherNum.matches();

    }

//    public static void main(String[] args) {
//        PassComplexValidator complexValidator = new PassComplexValidator();
//
//        System.out.println(complexValidator.validate("12"));
//        System.out.println(complexValidator.validate("1@"));
//        System.out.println(complexValidator.validate("aa"));
//        System.out.println(complexValidator.validate("A@"));
//        System.out.println(complexValidator.validate("A2"));
//        System.out.println(complexValidator.validate("2A"));
//
//    }

}
