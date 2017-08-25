package com.api.constantes;

/**
 * Contantes e enumeradores do Sitema
 *
 * @author juliano.ezequiel
 *
 */
public class CONSTANTES {

    public static final String URL_AUTH = "/auth";
    public static final String URL_USER = "/restrict/usuario";
    public static String AUTH_KEY = "123456";
    public static String SUCCESS = "success-toast";
    public static String ERROR = "error-toast";
    
    public enum MODULOS {
        DEFAULT,
        CADASTROS,
        FINANCEIRO,
        CONTROEL_ESTOQUE,
        RELATORIOS,
        CONFIGURACOES
    }
    
    public enum PERMISSAO{
        DEFAULT,
        USUARIOS,
        CLIENTES,
    }
    
    
}
