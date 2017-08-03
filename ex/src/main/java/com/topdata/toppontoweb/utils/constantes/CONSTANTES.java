package com.topdata.toppontoweb.utils.constantes;

import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.stereotype.Component;

import com.topdata.toppontoweb.dto.FormatoTransfer;
import com.topdata.toppontoweb.entity.autenticacao.Permissoes;
import com.topdata.toppontoweb.entity.autenticacao.Seguranca;
import com.topdata.toppontoweb.entity.bancohoras.BancoHoras;
import com.topdata.toppontoweb.entity.calendario.Calendario;
import com.topdata.toppontoweb.entity.empresa.Empresa;
import com.topdata.toppontoweb.entity.funcionario.Afastamento;
import com.topdata.toppontoweb.entity.funcionario.Compensacao;
import com.topdata.toppontoweb.entity.funcionario.Funcionario;
import com.topdata.toppontoweb.entity.jornada.Jornada;
import com.topdata.toppontoweb.entity.rep.Rep;

/**
 * @version 1.0.3 data 05/05/2016
 * @version 1.0.1
 * @since 1.0.1
 * @author juliano.ezequiel
 */
@Component
@XmlRootElement
public class CONSTANTES {

    public static final String PRE_AUTH_MASTER = "hasRole('ROLE_master')";

    public static final String OPERADOR_ADMIN = "ADMIN";
    public static final String OPERADOR_MASTER = "MASTER";
    public static final String OPERADOR_ANONIMO = "ANONYMOUSUSER";

    public static final String PERMISSAO_ADMIN = "ROLE_admin";
    public static final String PERMISSAO_USER = "ROLE_user";

    public static final String GRUPO_ADMIN = "ADMINISTRADOR";
    public static final String GRUPO_MASTER = "MASTER";
    public static final String GRUPO_ANONIMO = "ANONYMOUSUSER";

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static final Double PERC_PADRAO_PROCESSAMENTO_FECHAMENTO = 45d;
    public static final Double PERC_PADRAO_PERSISTENCIA_FECHAMENTO = 45d;
    public static final Double PERC_PADRAO_PROC_RELATORIO = 90d;
    public static final Double PERC_PADRAO_FIM = 100d;
    public static final Double PERC_PADRAO_PROC_INICIO = 0d;
    public static final Double PERC_PADRAO_PROC_INICIO_FECHAMENTO = 45d;

    /**
     * @version 1.0.3 data 04/05/2016
     * @since 1.0.3 data 04/05/2016
     * @author juliano.ezequiel
     */
    public enum Enum_AUDITORIA {

        DEFAULT(""),
        SIS_LOGIN("Login"),
        SIS_LOGOUT("Logout"),
        SIS_TENATIVA_LOGIN("Tentativa Login"),
        CAD_FERIADO("Cadastro feriado"),
        CAD_CALENDARIO_FERIADO("Cadastro calendário feriado"),
        CAD_CARGO("Cadastro cargos"),
        CAD_CONFIGURACOES_GERAIS("Configurações gerais"),
        CAD_LIMITES_BANCO_HORAS("Limites banco de horas");

        private final String descricao;

        private Enum_AUDITORIA(String d) {
            this.descricao = d;
        }

        public String getDescricao() {
            return descricao;
        }

        public static Enum_AUDITORIA getPorDescricao(String s) {
            for (Enum_AUDITORIA auditoria : Enum_AUDITORIA.values()) {
                if (auditoria.getDescricao().equals(s)) {
                    return auditoria;
                }
            }
            return null;
        }
    }

    /**
     * @version 1.0.5 data 05/09/2016
     * @author juliano.ezequiel
     */
    public enum Enum_TIPO_MOTIVO {

        DEFAULT(""),
        CARTAO("Cartão provisório"),
        ABONO("Abono"),
        AFASTAMENTO("Afastamento"),
        COMPENSACAO("Compensação"),
        MARCACAO_DESCONSIDERADA("Marcação Desconsiderada"),
        MARCACAO_INCLUIDA("Marcação incluída");

        private final String descricao;

        private Enum_TIPO_MOTIVO(String d) {
            this.descricao = d;
        }

        public String getDescricao() {
            return descricao;
        }

        public static Enum_TIPO_MOTIVO getPorDescricao(String s) {
            for (Enum_TIPO_MOTIVO tipomotivo : Enum_TIPO_MOTIVO.values()) {
                if (tipomotivo.getDescricao().equals(s)) {
                    return tipomotivo;
                }
            }
            return null;
        }
    }
    
    public enum Enum_MOTIVO {
        MARCACAO_MESMO_MINUTO("Marcação no mesmo minuto");

        private final String descricao;

        private Enum_MOTIVO(String d) {
            this.descricao = d;
        }

        public  String getDescricao() {
            return descricao;
        }

        public static Enum_MOTIVO getPorDescricao(String s) {
            for (Enum_MOTIVO motivo : Enum_MOTIVO.values()) {
                if (motivo.getDescricao().equals(s)) {
                    return motivo;
                }
            }
            return null;
        }

    }

    public enum Enum_TIPO_FECHAMENTO {

        DEFAULT(""),
        ACERTO("Acerto"),
        EDICAO_DE_SALDO("Edição de saldo"),
        SUBTOTAL("Subtotal");

        private final String descricao;

        private Enum_TIPO_FECHAMENTO(String d) {
            this.descricao = d;
        }

        public String getDescricao() {
            return descricao;
        }

        public static Enum_TIPO_FECHAMENTO getPorDescricao(String s) {
            for (Enum_TIPO_FECHAMENTO tipofechamento : Enum_TIPO_FECHAMENTO.values()) {
                if (tipofechamento.getDescricao().equals(s)) {
                    return tipofechamento;
                }
            }
            return null;
        }
    }

    /**
     * @version 1.0.5 data 05/09/2016
     * @author juliano.ezequiel
     */
    public enum Enum_TIPODIA {

        DEFAULT(""),
        NORMAL("Normal"),
        COMPENSADO("Compensado"),
        FOLGA("Folga"),
        FOLGA_DIFERENCIADA("Folga Diferenciada"),
        FERIADO("Feriado"),
        NORMAL_SABADO("Normal Sábado"),
        NORMAL_DOMINGO("Normal Domingo"),
        HORAS_NOTURNAS("Horas Noturnas");

        private final String descricao;

        private Enum_TIPODIA(String d) {
            this.descricao = d;
        }

        public String getDescricao() {
            return descricao;
        }

        public static Enum_TIPODIA getPorDescricao(String s) {
            for (Enum_TIPODIA tipodia : Enum_TIPODIA.values()) {
                if (tipodia.getDescricao().equals(s)) {
                    return tipodia;
                }
            }
            return null;
        }
    }

    /**
     * @version 1.0.3 data 04/05/2016
     * @since 1.0.3 data 04/05/2016
     *
     * @author juliano.ezequiel
     */
    public enum Enum_DOCUMENTO {

        DEFAULT("default"),
        CPF("Cpf"),
        CNPJ("Cnpj");

        private final String descricao;

        private Enum_DOCUMENTO(String descricao) {
            this.descricao = descricao;
        }

        public String getDescricao() {
            return descricao;
        }
    }

    /**
     * @version 1.0.3 data 04/05/2016
     * @since 1.0.1
     * @author juliano.ezequiel
     */
    public enum Enum_FUNCIONALIDADE {

        DEFAULT("Todas", Enum_MODULOS.DEFAULT, "", "#", "fa fa-fx fa-list", "TODAS"),//0
        //Menu cadastro empresas
        CAD_EMPRESAS("Empresas", Enum_MODULOS.CADASTROS, Empresa.class.getSimpleName(), Enum_MODULOS.CADASTROS.codModulo, "fa fa-fx fa-building", "EMPRESA"),//1
        CAD_EMPRESAS_EMPRESAS("Empresas", Enum_MODULOS.CADASTROS, Empresa.class.getSimpleName(), CAD_EMPRESAS.getDescricao() + CAD_EMPRESAS.ordinal(), "fa fa-fx fa-building", "EMPRESAS"),//2
        CAD_EMPRESAS_DEPARTAMENTO("Departamentos", Enum_MODULOS.CADASTROS, Empresa.class.getSimpleName(), CAD_EMPRESAS.getDescricao() + CAD_EMPRESAS.ordinal(), "fa fa-fx fa-sitemap", "EMPRESA_DEPARTAMENTO"),//3
        CAD_EMPRESA_CEI("CEIs", Enum_MODULOS.CADASTROS, Empresa.class.getSimpleName(), CAD_EMPRESAS.getDescricao() + CAD_EMPRESAS.ordinal(), "fa fa-fx fa-list-ol", "EMPRESA_CEI"),//4
        CAD_EMPRESAS_FECHAMENTOS("Fechamentos", Enum_MODULOS.CADASTROS, Empresa.class.getSimpleName(), CAD_EMPRESAS.getDescricao() + CAD_EMPRESAS.ordinal(), "fa fa-fx fa-gavel", "EMPRESA_FECHAMENTO"),//5
        //menu cadastro Jornadas
        CAD_JORNADAS("Jornadas", Enum_MODULOS.CADASTROS, Jornada.class.getSimpleName(), Enum_MODULOS.CADASTROS.codModulo, "fa fa-fx fa-table", "JORNADA"),//6
        //        CAD_JORNADAS_JORNADAS("Jornadas", Enum_MODULOS.CADASTROS, Jornada.class.getSimpleName(), CAD_JORNADAS.getDescricao() + CAD_JORNADAS.ordinal(), "fa fa-fx fa-table", "JORNADAS"),
        //        CAD_JORNADAS_PARAMETROS("Parâmetros", Enum_MODULOS.CADASTROS, Jornada.class.getSimpleName(), CAD_JORNADAS.getDescricao() + CAD_JORNADAS.ordinal(), "fa fa-fx fa-sliders", "JORNADA_PARAMETROS"),
        //        CAD_JORNADAS_LIMITES("Limites", Enum_MODULOS.CADASTROS, Jornada.class.getSimpleName(), CAD_JORNADAS.getDescricao() + CAD_JORNADAS.ordinal(), "fa fa-fx fa-unsorted", "JORNADA_LIMITE"),
        //        CAD_JORNADAS_HORA_EXTRAS("Horas Extras", Enum_MODULOS.CADASTROS, Jornada.class.getSimpleName(), CAD_JORNADAS.getDescricao() + CAD_JORNADAS.ordinal(), "fa fa-fx fa-briefcase", "JORNADA_HORAS_EXTRAS"),
        //Menu cadastro funcionarios
        CAD_FUNCIONARIO("Funcionários", Enum_MODULOS.CADASTROS, Funcionario.class.getSimpleName(), Enum_MODULOS.CADASTROS.codModulo, "fa fa-fx fa-user-circle-o", "FUNCIONARIO"),//7
        CAD_FUNCIONARIO_FUNCIONARIO("Funcionários", Enum_MODULOS.CADASTROS, Funcionario.class.getSimpleName(), CAD_FUNCIONARIO.getDescricao() + CAD_FUNCIONARIO.ordinal(), "fa fa-fx fa-user-circle-o", "FUNCIONARIOS"),//8
        CAD_FUNCIONARIO_CARTAO("Cartão", Enum_MODULOS.CADASTROS, Funcionario.class.getSimpleName(), CAD_FUNCIONARIO.getDescricao() + CAD_FUNCIONARIO.ordinal(), "fa fa-fx fa-credit-card", "FUNCIONARIO_CARTAO"),//9
        CAD_FUNCIONARIO_JORNADAS("Jornadas", Enum_MODULOS.CADASTROS, Funcionario.class.getSimpleName(), CAD_FUNCIONARIO.getDescricao() + CAD_FUNCIONARIO.ordinal(), "fa fa-fx fa-table", "FUNCIONARIO_JORNADA"),//10
        CAD_FUNCIONARIO_CALENDARIO("Calendários", Enum_MODULOS.CADASTROS, Funcionario.class.getSimpleName(), CAD_FUNCIONARIO.getDescricao() + CAD_FUNCIONARIO.ordinal(), "fa fa-fx fa-calendar", "FUNCIONARIO_CALENDARIO"),//11
        CAD_FUNCIONARIO_AFASTAMENTO("Afastamento", Enum_MODULOS.CADASTROS, Funcionario.class.getSimpleName(), CAD_FUNCIONARIO.getDescricao() + CAD_FUNCIONARIO.ordinal(), "fa fa-fx fa-bed", "FUNCIONARIO_AFASTAMENTO"),//12
        CAD_FUNCIONARIO_COMPENSASOES("Compensações", Enum_MODULOS.CADASTROS, Funcionario.class.getSimpleName(), CAD_FUNCIONARIO.getDescricao() + CAD_FUNCIONARIO.ordinal(), "fa fa-fx fa-plus-circle", "FUNCIONARIO_COMPENSACAO"),//13
        CAD_FUNCIONARIO_BANCO_DE_HORAS("Banco de Horas", Enum_MODULOS.CADASTROS, Funcionario.class.getSimpleName(), CAD_FUNCIONARIO.getDescricao() + CAD_FUNCIONARIO.ordinal(), "fa fa-fx fa-clock-o", "FUNCIONARIO_BANCO_DE_HORAS"),//14
        //Menu Cadastro Rep
        CAD_REP("Rep", Enum_MODULOS.CADASTROS, Rep.class.getSimpleName(), Enum_MODULOS.CADASTROS.codModulo, "fa fa-fx fa-window-maximize", "REP"),//15
        //Menu Calendarios
        CAD_CALENDARIO("Calendários", Enum_MODULOS.CADASTROS, Calendario.class.getSimpleName(), Enum_MODULOS.CADASTROS.codModulo, "fa fa-fx fa-calendar", "CALENDARIO"),//16
        //Menu Cadastro Banco de Horas
        CAD_BANCO_HORAS("Banco de Horas", Enum_MODULOS.CADASTROS, BancoHoras.class.getSimpleName(), Enum_MODULOS.CADASTROS.codModulo, "fa fa-fx fa-clock-o", "BANCO_DE_HORAS"),//17
        //        CAD_BANCO_HORAS_BANCO_HORAS("Banco de Horas", Enum_MODULOS.CADASTROS, BancoHoras.class.getSimpleName(), CAD_BANCO_HORAS.getDescricao() + CAD_BANCO_HORAS.ordinal(), "fa fa-fx fa-clock-o", "BANCOS_DE_HORAS"),
        //        CAD_BANCO_DE_HORAS_PARAMETROS("Parâmetros", Enum_MODULOS.CADASTROS, BancoHoras.class.getSimpleName(), CAD_BANCO_HORAS.getDescricao() + CAD_BANCO_HORAS.ordinal(), "fa fa-fx fa-sliders", "BANCO_DE_HORAS_PARAMETROS"),
        //        CAD_BANCO_DE_HORAS_GATILHOS("Gatilhos", Enum_MODULOS.CADASTROS, BancoHoras.class.getSimpleName(), CAD_BANCO_HORAS.getDescricao() + CAD_BANCO_HORAS.ordinal(), "fa fa-fx fa-arrows-h", "BANCO_DE_HORAS_GATILHOS"),
        //        CAD_BANCO_DE_HORAS_PERCENTUAIS_ACRECIMO("Percentuais de Acréscimo", Enum_MODULOS.CADASTROS, BancoHoras.class.getSimpleName(), CAD_BANCO_HORAS.getDescricao() + CAD_BANCO_HORAS.ordinal(), "fa fa-fx fa-percent", "BANCO_DE_HORAS_PERC_ASC"),
        //Menu Cadastro permissoes
        CAD_PERMISSOES("Permissões", Enum_MODULOS.CADASTROS, Permissoes.class.getSimpleName(), Enum_MODULOS.CADASTROS.codModulo, "fa fa-fx fa-key", "PERMISSOES"),//18
        CAD_PERMISSOES_GRUPOS("Grupos", Enum_MODULOS.CADASTROS, Permissoes.class.getSimpleName(), CAD_PERMISSOES.getDescricao() + CAD_PERMISSOES.ordinal(), "fa fa-fx fa-group", "PERMISSOES_GRUPO"),//19
        CAD_PERMISSOES_OPERADORES("Operadores", Enum_MODULOS.CADASTROS, Permissoes.class.getSimpleName(), CAD_PERMISSOES.getDescricao() + CAD_PERMISSOES.ordinal(), "fa fa-fx fa-user-plus", "PERMISSOES_OPERADOR"),//20
        CAD_PERMISSOES_SEGURANCA("Segurança", Enum_MODULOS.CADASTROS, Permissoes.class.getSimpleName(), CAD_PERMISSOES.getDescricao() + CAD_PERMISSOES.ordinal(), "fa fa-fx fa-lock", "PERMISSOES_SEGURANCA"),//21
        //Menu Cadastro Configuracoes
        CAD_CONFIGURACOES("Configurações", Enum_MODULOS.CADASTROS, Seguranca.class.getSimpleName(), Enum_MODULOS.CADASTROS.codModulo, "fa fa-fx fa-gears", "CONFIGURACAO"),//22
        //        CAD_CONFIGURACOES_OCORRENCIAS("Ocorrências", Enum_MODULOS.CADASTROS, Seguranca.class.getSimpleName(), CAD_CONFIGURACOES.getDescricao() + CAD_CONFIGURACOES.ordinal(), "fa fa-fx fa-inbox"),
        CAD_CONFIGURACOES_PERCENTUAIS_ACRESCIMO("Percentuais de Acréscimo", Enum_MODULOS.CADASTROS, Seguranca.class.getSimpleName(), CAD_CONFIGURACOES.getDescricao() + CAD_CONFIGURACOES.ordinal(), "fa fa-fx fa-percent", "CONFIGURACAO_PERC_ASC"),//23
        CAD_CONFIGURACOES_DSR("DSR", Enum_MODULOS.CADASTROS, Seguranca.class.getSimpleName(), CAD_CONFIGURACOES.getDescricao() + CAD_CONFIGURACOES.ordinal(), "fa fa-fx fa-calculator", "CONFIGURACAO_DSR"),//24
        //Menu Lancamento Coletivo
        LC_AFASTAMENTO("Afastamentos", Enum_MODULOS.LANCAMENTOS_COLETIVOS, Afastamento.class.getSimpleName(), Enum_MODULOS.LANCAMENTOS_COLETIVOS.codModulo, "fa fa-fx fa-bed", "LC_AFASTAMENTO"),//25
        LC_CALENDARIO("Calendários", Enum_MODULOS.LANCAMENTOS_COLETIVOS, Calendario.class.getSimpleName(), Enum_MODULOS.LANCAMENTOS_COLETIVOS.codModulo, "fa fa-fx fa-calendar", "LC_CALENDARIO"),//26
        LC_JORNADAS("Jornadas", Enum_MODULOS.LANCAMENTOS_COLETIVOS, Jornada.class.getSimpleName(), Enum_MODULOS.LANCAMENTOS_COLETIVOS.codModulo, "fa fa-fx fa-table", "LC_JORNADA"),//27
        // LC_EXCECOES_JORNADA("Exceções de Jornada", Enum_MODULOS.LANCAMENTOS_COLETIVOS, "", Enum_MODULOS.LANCAMENTOS_COLETIVOS.codModulo, "fa fa-fx fa-times-circle"),
        LC_COMPENSACOES("Compensações", Enum_MODULOS.LANCAMENTOS_COLETIVOS, Compensacao.class.getSimpleName(), Enum_MODULOS.LANCAMENTOS_COLETIVOS.codModulo, "fa fa-fx fa-plus-circle", "LC_COMPENSACAO"),//28
        LC_BANCO_HORAS("Banco de horas", Enum_MODULOS.LANCAMENTOS_COLETIVOS, BancoHoras.class.getSimpleName(), Enum_MODULOS.LANCAMENTOS_COLETIVOS.codModulo, "fa fa-fx fa-clock-o", "LC_BANCO_DE_HORAS"),//29
        LC_BANCO_HORAS_MANUTENCAOES("Manutenções", Enum_MODULOS.LANCAMENTOS_COLETIVOS, BancoHoras.class.getSimpleName(), LC_BANCO_HORAS.getDescricao() + LC_BANCO_HORAS.ordinal(), "fa fa-fx fa-cog", "LC_BH_MANUTENCAO"),//30
        LC_BANCO_HORAS_FECHAMENTOS("Fechamentos", Enum_MODULOS.LANCAMENTOS_COLETIVOS, BancoHoras.class.getSimpleName(), LC_BANCO_HORAS.getDescricao() + LC_BANCO_HORAS.ordinal(), "fa fa-fx fa-gavel", "LC_BH_FECHAMENTO"),//31
        //Menu Relatorios
        REL_ESPELHO("Espelho", Enum_MODULOS.RELATORIOS, "", Enum_MODULOS.RELATORIOS.codModulo, "fa fa-fx fa-clone", "RELATORIO_ESPELHO"),//32
        REL_PRESENCA("Presença", Enum_MODULOS.RELATORIOS, "", Enum_MODULOS.RELATORIOS.codModulo, "fa fa-fx fa-male", "RELATORIO_PRESENCA"),//33
        REL_FREQUENCIA("Frequência", Enum_MODULOS.RELATORIOS, "", Enum_MODULOS.RELATORIOS.codModulo, "fa fa-fx fa-check-square-o", "RELATORIO_FREQUENCIA"),//34
        //        REL_OCORRENCIA("Ocorrência", Enum_MODULOS.RELATORIOS, "", Enum_MODULOS.RELATORIOS.codModulo, "fa fa-fx fa-times-rectangle-o", "RELATORIO_OCORRENCIA"),
        REL_HORAS_EXTRA("Horas extras", Enum_MODULOS.RELATORIOS, "", Enum_MODULOS.RELATORIOS.codModulo, "fa fa-fx fa-briefcase", "RELATORIO_HORAS_EXTRAS"),//35
        REL_BANCO_HORAS("Banco de horas", Enum_MODULOS.RELATORIOS, "", Enum_MODULOS.RELATORIOS.codModulo, "fa fa-fx fa-clock-o", "RELATORIO_BANCO_DE_HORAS"),//36
        REL_ABSENTEISMO("Absenteísmo", Enum_MODULOS.RELATORIOS, "", Enum_MODULOS.RELATORIOS.codModulo, "fa fa-fx fa-user-times", "RELATORIO_ABSENTEISMO"),//37
        REL_INTERJORNADA("InterJornada", Enum_MODULOS.RELATORIOS, "", Enum_MODULOS.RELATORIOS.codModulo, "fa fa-fx fa-table", "RELATORIO_INTERJORNADA"),//38
        REL_INTRAJORNADA("IntraJornada", Enum_MODULOS.RELATORIOS, "", Enum_MODULOS.RELATORIOS.codModulo, "fa fa-fx fa-table", "RELATORIO_INTRAJORNADA"),//39
        REL_AUDITORIA("Auditoria", Enum_MODULOS.RELATORIOS, "", Enum_MODULOS.RELATORIOS.codModulo, "fa fa-fx fa-user-secret", "RELATORIO_AUDITORIA"),//40

        REL_CADASTROS("Cadastro", Enum_MODULOS.RELATORIOS, "", Enum_MODULOS.RELATORIOS.codModulo, "fa fa-fx fa-pencil-square", "RELATORIO_CADASTRO"),//41
        REL_CAD_EMPRESA("Cadastro de Empresas", Enum_MODULOS.RELATORIOS, "", REL_CADASTROS.getDescricao() + REL_CADASTROS.ordinal(), "fa fa-building", "RELATORIO_CADASTRO_EMPRESA"),//42
        REL_CAD_FUNCIONARIO("Cadastro de Funcionários", Enum_MODULOS.RELATORIOS, "", REL_CADASTROS.getDescricao() + REL_CADASTROS.ordinal(), "fa  fa-user-circle-o", "RELATORIO_CADASTRO_FUNCIONARIO"),//43
        REL_CAD_CALENDARIO("Cadastro de Calendários", Enum_MODULOS.RELATORIOS, "", REL_CADASTROS.getDescricao() + REL_CADASTROS.ordinal(), "fa fa-calendar", "RELATORIO_CADASTRO_CALENDARIO"),//44
        REL_CAD_HORARIO("Cadastro de Horários", Enum_MODULOS.RELATORIOS, "", REL_CADASTROS.getDescricao() + REL_CADASTROS.ordinal(), "fa fa-clock-o", "RELATORIO_CADASTRO_HORARIO"),//45
        REL_CAD_BANCO_HORAS("Cadastro de Banco de horas", Enum_MODULOS.RELATORIOS, "", REL_CADASTROS.getDescricao() + REL_CADASTROS.ordinal(), "fa fa-clock-o", "RELATORIO_CADASTRO_BANCO_DE_HORAS"),//46
        REL_AFASTAMENTO("Cadastro de Afastamentos", Enum_MODULOS.RELATORIOS, "", REL_CADASTROS.getDescricao() + REL_CADASTROS.ordinal(), "fa fa-fx fa-bed", "RELATORIO_CADASTRO_AFASTAMENTO"),//47
        //Menu arquivos fiscais
        ARQ_AFDT("AFDT", Enum_MODULOS.ARQUIVOS_FISCAIS, "", Enum_MODULOS.ARQUIVOS_FISCAIS.codModulo, "fa fa-fx fa-file-text", "ARQUIVO_AFDT"),//48
        ARQ_ACJEF("ACJEF", Enum_MODULOS.ARQUIVOS_FISCAIS, "", Enum_MODULOS.ARQUIVOS_FISCAIS.codModulo, "fa fa-fx fa-file-text-o", "ARQUIVO_ACJEF"),//49
        ARQ_AFD("AFD", Enum_MODULOS.ARQUIVOS_FISCAIS, "", Enum_MODULOS.ARQUIVOS_FISCAIS.codModulo, "fa fa-fx fa-file", "ARQUIVO_AFD"),//50
        ARQ_FISCAL("Espelho Fiscal", Enum_MODULOS.ARQUIVOS_FISCAIS, "", Enum_MODULOS.ARQUIVOS_FISCAIS.codModulo, "fa fa-file-o", "ARQUIVO_FISCAL"),//51
        //Menu Ferramentas
        FER_MANUTENCOES_MARCACOES("Manutenção de Marcações", Enum_MODULOS.FERRAMENTAS, "", Enum_MODULOS.FERRAMENTAS.codModulo, "fa fa-fx fa-pencil-square-o", "FERRAMENTA_MANUTENCAO_MARCADORES"),//52

        FER_IMPORTACOES("Importações", Enum_MODULOS.FERRAMENTAS, "", Enum_MODULOS.FERRAMENTAS.codModulo, "fa fa-fx fa-upload", "FERRAMENTA_IMPORTACAO"),//53
        FER_MARCACOES("Marcações", Enum_MODULOS.FERRAMENTAS, "", FER_IMPORTACOES.getDescricao() + FER_IMPORTACOES.ordinal(), "fa fa-bars", "FERRAMENTA_IMPORTACAO_MARCACAO"),//54
        FER_FUNCIONARIOS("Funcionários", Enum_MODULOS.FERRAMENTAS, "", FER_IMPORTACOES.getDescricao() + FER_IMPORTACOES.ordinal(), "fa  fa-user-circle-o", "FERRAMENTA_IMPORTACAO_FUNCIONARIO"),//55

        FER_EXPORTACOES("Exportações", Enum_MODULOS.FERRAMENTAS, "", Enum_MODULOS.FERRAMENTAS.codModulo, "fa fa-fx fa-download ", "FERRAMENTA_EXPORTACAO"),//56
        FER_EXPORTACOES_EVENTOS("Eventos", Enum_MODULOS.FERRAMENTAS, "", FER_EXPORTACOES.getDescricao() + FER_EXPORTACOES.ordinal(), "fa fa-calendar-check-o", "FERRAMENTA_EXPORTACAO_EVENTO"),//57
        FER_EXPORTACOES_FUNCIONARIOS("Funcionários", Enum_MODULOS.FERRAMENTAS, "", FER_EXPORTACOES.getDescricao() + FER_EXPORTACOES.ordinal(), "fa  fa-user-circle-o", "FERRAMENTA_EXPORTACAO_FUNCIONARIO"),//58

        FER_LAYOUT("Layout", Enum_MODULOS.FERRAMENTAS, "", Enum_MODULOS.FERRAMENTAS.codModulo, "fa fa-file-code-o", "FERRAMENTA_LAYOUT"),//59
        FER_LAYOUT_EVENTOS("Eventos", Enum_MODULOS.FERRAMENTAS, "", FER_LAYOUT.getDescricao() + FER_LAYOUT.ordinal(), "fa fa-calendar-check-o", "FERRAMENTA_LAYOUT_EVENTOS"),//60
        FER_LAYOUT_FUNCIONARIOS("Funcionários", Enum_MODULOS.FERRAMENTAS, "", FER_LAYOUT.getDescricao() + FER_LAYOUT.ordinal(), "fa fa-user-circle-o", "FERRAMENTA_LAYOUT_FUNCIONARIO");//61;

        private final Enum_MODULOS modulo;
        private final String descricao;
        private final String className;
        private final String pai;
        private final String icone;
        private final String funcionalidade;

        private Enum_FUNCIONALIDADE(String descricao, Enum_MODULOS modulo, String ClassName, String pai, String icone, String funcionalidade) {
            this.descricao = descricao;
            this.modulo = modulo;
            this.className = ClassName;
            this.pai = pai;
            this.icone = icone;
            this.funcionalidade = funcionalidade;

        }

        public static Enum_FUNCIONALIDADE getByClass(String s) {
            for (Enum_FUNCIONALIDADE enumFUNCOES : Enum_FUNCIONALIDADE.values()) {
                if (enumFUNCOES.getClassName().equals(s)) {
                    return enumFUNCOES;
                }
            }
            return null;
        }

        public String getDescricao() {
            return descricao;
        }

        public Enum_MODULOS getModulo() {
            return modulo;
        }

        public String getClassName() {
            return className;
        }

        public String getPai() {
            return pai;
        }

        public String getIcone() {
            return icone;
        }

        public String getFuncionalidade() {
            return funcionalidade;
        }

    }

    /**
     * @version 1.0.3 data 04/05/2016
     * @since 1.0.3 data 04/05/2016
     * @author juliano.ezequiel
     */
    public enum Enum_MODULOS {

        DEFAULT("MOD", "", "0", ""),
        CADASTROS("MOD_01", "Cadastros", "0", "fa fa-fx fa-pencil"),
        LANCAMENTOS_COLETIVOS("MOD_02", "Lançamentos Coletivos", "0", "fa fa-fx fa-share-square-o"),
        RELATORIOS("MOD_03", "Relatórios", "0", "fa fa-fx fa-print"),
        ARQUIVOS_FISCAIS("MOD_04", "Arquivos Fiscais", "0", "fa fa-fx fa-folder-o"),
        FERRAMENTAS("MOD_05", "Ferramentas", "0", "fa fa-fx fa-wrench");

        private final String descricao;
        private final String icone;
        private final String pai;
        private final String codModulo;

        private Enum_MODULOS(String idMod, String descricao, String pai, String icone) {
            this.descricao = descricao;
            this.icone = icone;
            this.pai = pai;
            this.codModulo = idMod;

        }

        public String getDescricao() {
            return descricao;
        }

        public String getIcone() {
            return icone;
        }

        public String getPai() {
            return pai;
        }

        public String getCodModulo() {
            return codModulo;
        }

    }

    /**
     * @version 1.0.3 data 04/05/2016
     * @since 1.0.3 data 04/05/2016
     * @author juliano.ezequiel
     */
    public enum Enum_OPERACAO {

        DEFAULT("default", ""),
        SISTEMA("SISTEMA", ""),
        INCLUIR("Incluir", "fa fa-fx fa-plus"),
        EDITAR("Editar", "fa fa-fx fa-edit"),
        EXCLUIR("Excluir", "fa fa-fx fa-trash-o"),
        CONSULTAR("Consultar", "fa fa-fx fa-search"),
        GERAR("Gerar", "fa fa-fx fa-arrow-circle-down"),
        IMPORTAR("Importar", "fa fa-fx fa-download");

        private final String descricao;
        private final String icone;

        private Enum_OPERACAO(String descricao, String icone) {
            this.descricao = descricao;
            this.icone = icone;
        }

        public String getDescricao() {
            return descricao;
        }

        public String getIcone() {
            return icone;
        }

    }

    public enum Enum_TIPO_PROCESSO {
        RELATORIO,
        FECHAMENTO_EMPRESA,
        FECHAMENTO_BANCO_HORAS,
        EXPORTAR_ARQUIVO,
        VERIFICACAO_SALDO_BH,
        MANUTENCAO_MARCACOES
    }

    /**
     * @version 1.0.3 data 04/050/2016
     * @since 1.0.3 data 04/05/2016
     * @author juliano.ezequiel
     */
    public enum Enum_PERMISSAO {

        DEFAULT("", ""),
        ROLE_MASTER("ROLE_master", "Acesso Total"),
        ROLE_ADMIN("ROLE_admin", "Incluir/Editar/Excluir/Consultar"),
        ROLE_USER("ROLE_user", "Consultar"),
        ROLE_ANONYMOUS("ROLE_anonymousUser", "Anônimo");

        private final String permissao;
        private final String descricao;

        private Enum_PERMISSAO(String permissao, String descricao) {
            this.descricao = descricao;
            this.permissao = permissao;
        }

        public String getPermissao() {
            return permissao;
        }

        public String getDescricao() {
            return descricao;
        }

        public Enum_PERMISSAO getPorDescricao(String descricao) {
            for (Enum_PERMISSAO permissao1 : values()) {
                if (permissao1.descricao.equals(descricao)) {
                    return permissao1;
                }
            }
            return null;
        }
    }

    public enum Enum_TIPO_EQUIPAMENTO {

        DEFAULT(""),
        REP("Rep"),
        INNER("Inner"),
        OUTRO("Outros");

        private final String descricao;

        private Enum_TIPO_EQUIPAMENTO(String descricao) {
            this.descricao = descricao;
        }

        public String getDescricao() {
            return descricao;
        }

    }

    public enum Enum_TOLERANCIA_OCORENCIA {

        ENTRADA_ANTECIPADA("Entrada Antecipada"),
        SAIDA_ANTECIDA("Saída Antecipada"),
        ENTRADA_ATRASADA("Entrada Atrasada"),
        SAIDA_APOS_HORARIO("Saída após horário");
        private final String descricao;

        private Enum_TOLERANCIA_OCORENCIA(String descricao) {
            this.descricao = descricao;
        }

        public String getDescricao() {
            return descricao;
        }

    }

    public enum Enum_DIA_SEMANA {

        DOMINGO("Domingo", 0),
        SEGUNDA("Segunda-feira", 1),
        TERCA("Terça-feira", 2),
        QUARTA("Quarta-feira", 3),
        QUINTA("Quinta-feira", 4),
        SEXTA("Sexta-feira", 5),
        SABADO("Sábado", 6);

        private final String descricao;
        private final Integer dia;

        private Enum_DIA_SEMANA(String descricao, Integer dia) {
            this.descricao = descricao;
            this.dia = dia;
        }

        public String getDescricao() {
            return descricao;
        }

        public Integer getDia() {
            return dia;
        }

        public static Enum_DIA_SEMANA getById(Integer dia) {
            for (Enum_DIA_SEMANA dia_semana : values()) {
                if (dia.equals(dia_semana.getDia())) {
                    return dia_semana;
                }
            }
            return null;
        }

    }

    public enum Enum_TIPO_JORNADA {
        DEFAULT,
        SEMANAL,
        VARIAVEL,
        LIVRE;
    }

    /**
     * Tipos de formatos de arquivos
     */
    public enum Enum_FORMATO {

        CSV("CSV", "CSV (Comma-separated values )", "text/csv", ".csv"),
        EXCEL("EXCEL", "XLS (Excel)", "application/application/excel", ".xls"),
        HTML("HTML", "HTML", "text/html", ".html"),
        ODT("ODT", "ODT (OpenDocument Text)", "application/vnd.oasis.opendocument.text", ".odt"),
        ODS("ODS", "ODS (OpenDocument Spreadsheet)", "application/vnd.oasis.opendocument.spreadsheet", ".ods"),
        PDF("PDF", "PDF (Portable Document Format)", "application/pdf", ".pdf"),
        PNG("PNG", "PNG", "image/png", ".png"),
        RTF("RTF", "RTF (Rich Text Format)", "application/rtf", ".rtf"),
        TEXTO("TEXTO", "TXT (Texto)", "text/plain", ".txt"),
        TIFF("TIFF", "TIFF (Image)", "image/tiff", ".tif"),
        WORD("WORD", "DOCX (Word)", "application/msword", ".docx"),
        XML("XML", "XML", "application/xml", ".xml");

        private final String idFormato;
        private final String descricao;
        private final String tipo;
        private final String sufixo;

        private Enum_FORMATO(String idFormato, String descricao, String tipo, String sufixo) {
            this.descricao = descricao;
            this.idFormato = idFormato;
            this.tipo = tipo;
            this.sufixo = sufixo;
        }

        public String getDescricao() {
            return descricao;
        }

        public String getIdFormato() {
            return idFormato;
        }

        public String getTipo() {
            return tipo;
        }

        public String getSufixo() {
            return sufixo;
        }

        public static FormatoTransfer getFormatoTransfer(String idFormato) {
            for (Enum_FORMATO eformato : values()) {
                if (eformato.idFormato.equalsIgnoreCase(idFormato)) {
                    return new FormatoTransfer(eformato);
                }
            }
            return new FormatoTransfer();
        }

    }

    public enum Enum_STATUS_MARCACAO {
        DEFAULT(""),//somente para arrumar a base
        ORIGINAL("Original"),
        INCLUIDA("Incluída"),
        DESCONSIDERADA("Desconsiderada"),
        PRE_ASSINALADA("Pré-assinalada"),
        EXCLUIDA("Excluída");

        private final String descricao;

        private Enum_STATUS_MARCACAO(String d) {
            this.descricao = d;
        }

        public String getDescricao() {
            return descricao;
        }

        public static Enum_STATUS_MARCACAO getPorDescricao(String s) {
            for (Enum_STATUS_MARCACAO tipofechamento : Enum_STATUS_MARCACAO.values()) {
                if (tipofechamento.getDescricao().equals(s)) {
                    return tipofechamento;
                }
            }
            return null;
        }

    }

    public enum Enum_ENCAIXE_MARCACAO {
        DEFAULT(""),//somente para arrumar a base
        NAO_DEFINIDO("Não definido"),
        ANTERIOR("Anterior"),
        ATUAL("Atual"),
        SEGUINTE("Seguinte");

        private final String descricao;

        private Enum_ENCAIXE_MARCACAO(String d) {
            this.descricao = d;
        }

        public String getDescricao() {
            return descricao;
        }

        public static Enum_ENCAIXE_MARCACAO getPorDescricao(String s) {
            for (Enum_ENCAIXE_MARCACAO encaixeMarcacao : Enum_ENCAIXE_MARCACAO.values()) {
                if (encaixeMarcacao.getDescricao().equals(s)) {
                    return encaixeMarcacao;
                }
            }
            return null;
        }

    }

    public enum PROCESSO_STATUS {
        CARREGANDO_ARQUIVO,
        PROCESSANDO_ARQUIVO,
        CONCLUIDO,
        ERRO_ABRIR_ARQUIVO,
        ERRO_AUDITAR,
        ERRO_FORMATO_ARQUIVO,
        CANCELADO,
        EMPRESA_POSSUI_FECHAMENTO,
        ERRO_GERAR_ARQUIVO,
        ERRO_CARREGAR_DADOS,
        CARREGANDO_DADOS_GERA_FREQUENCIA,
        GERANDO_ARQUIVO,
        ERRO_CANCELAR,
        ERRO_IMPORTAR_SEM_CABECALHO,
        ERRO_EMPRESAS_DIFERENTES,
        ERRO_IMPORTAR_REP_NAO_ENCONTRADO,
        ERRO_IMPORTAR_EMPRESA_NAO_ENCONTRADA, SUCESSO_IMPORTAR_VALIDAR_CABECALHO, CONFIGURANDO_MARCACOES;
    }
    
    public enum Enum_TIPOS_ACERTO {

        DEFAULT(""),
        ZERAR("Zerar"),
        USAR_GATILHO("Usar Gatilho"),
        MANUAL("Manual");

        private final String descricao;

        private Enum_TIPOS_ACERTO(String s) {
            this.descricao = s;
        }

        public String getDescricao() {
            return descricao;
        }

    }

    public enum Enum_TIPO_LAYOUT {

        DEFAULT(""),
        EVENTOS("Eventos"),
        FUNCIONARIOS("Funcionários");

        private final String descricao;

        private Enum_TIPO_LAYOUT(String s) {
            this.descricao = s;
        }

        public String getDescricao() {
            return descricao;
        }

    }

}
