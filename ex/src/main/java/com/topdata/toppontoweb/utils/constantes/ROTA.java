package com.topdata.toppontoweb.utils.constantes;

/**
 * Constantes para as rotas dos REST
 *
 * @version 1.0.3 data 09/05/2016
 * @since 1.0.3 data 09/05/2016
 * @author juliano.ezequiel
 */
public class ROTA {

    //Parametros da rotas;
    public static final String PARAM_01 = "param";
    public static final String PARAM_02 = "param2";
    public static final String PARAM_03 = "param3";

    //form param utilizado no login
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";

    //Sub rotas genericas
    public static final String MAX_FIRST = "{" + PARAM_01 + "}/{" + PARAM_02 + "}";
    public static final String QTD = "qtd";
    public static final String DESC = "/descricao/{" + PARAM_01 + "}";
    public static final String ID = "{" + PARAM_01 + "}";
    public static final String REL = "/relatorio/{" + PARAM_01 + "}";

    //* rota metodo principal e sub rotas
    //Autenticacao
    public static final String USER = "/user";
    public static final String USER_AUTH = "/auth";

    //planos
    public static final String PLANO = "/planoAcesso";
    public static final String PLANO_LIBERACAO = "/liberacao";

    //funcionario cargo
    public static final String FUNC_CARGO = "/funcionariocargo";
    public static final String FUNC_CARGO_F_ID = "/funcionario/{" + PARAM_01 + "}";
    public static final String FUNC_CARGO_C_ID = "/cargo/{" + PARAM_01 + "}";

    //operador
    public static final String OPERADOR = "/operador";
    public static final String OPER_USUARIO = "/nome/{" + PARAM_01 + "}";
    public static final String OPER_MUDARSENHA = "/mudarSenha";
    public static final String SEGURANCA = "/seguranca";
    public static final String CARGO = "/cargo";
    public static final String GRUPO = "/grupo";

    //funcionario
    public static final String FUNCIONARIO = "/funcionario";
    public static final String PIS = "/pis/{" + PARAM_01 + "}";
    public static final String MATRICULA = "/matricula/{" + PARAM_01 + "}";

    //empresa
    public static final String EMPRESA = "/empresa";

    //departamento
    public static final String DEPTO = "/departamento";
    public static final String DEPTO_EMPRESA = "/empresa/{" + PARAM_01 + "}";

}
