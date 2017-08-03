package com.topdata.toppontoweb.utils.constantes;

/**
 * @version 1.0.0
 * @since 1.0.0
 * @author juliano.ezequiel
 */
public class MSG {

    public static final String LOCAL = "pt_BR";

    public final static String ERRO_OPERACAO_NAO_REALIZADA = "operacao.alerta.naoRealizada";
    public final static String CAMPO_OBRIGATORIO = "alerta.campo.obrigatorio";
    public final static String ALERTA_GRUPO_JA_CAD = "grupo.alerta.jacad";
    public final static String NAO_AUTORIZADO = "request.naoAutorizado";

    public final static String IMAGEM_TAMANHO_MAX = "alerta.tamanhoMaxImagem";
    public final static String IMAGEM_INVALIDA = "alerta.tipoArquivoInvalido";
    public final static String IMAGEM_TIPOS_SUPORTADOS = "alerta.tipoImagemNaoSuportado";

    public final static String ERRO = "erro";

    public enum TIPOMSG {

        SUCESSO("success", "Sucesso"), ERRO("error", "Erro"), ALERTA("warning", "Atenção!");
        private final String descricao;
        private final String titulo;

        private TIPOMSG(String s, String t) {
            this.descricao = s;
            this.titulo = t;
        }

        public String getDescricao() {
            return descricao;
        }

        public String getTitulo() {
            return titulo;
        }

    }

    public enum OPERADOR {

        ERRO_SALVAR("operador.erro.salvar"),
        ERRO_EXCLUIR("operador.erro.excluir"),
        ERRO_ATUALIZAR("operador.erro.atualizar"),
        ERRO_LISTAR("operador.erro.atualizar"),
        SUCESSO_SALVAR("operador.sucesso.salvo"),
        SUCESSO_SALVAR_SEM_ENVIO_EMAIL("operador.sucesso.salvoSemEvioEmail"),
        SUCESSO_EXCLUIR("operador.sucesso.excluir"),
        SUCESSO_BUSCAR("operador.sucesso.buscar"),
        SUCESSO_LISTAR("operador.sucesso.listar"),
        SUCESSO_ATUALIZAR("operador.sucesso.atualizar"),
        ALERTA_MAX_SIZE("operador.alerta.max.size"),
        ALERTA_NOME_OBRIGATORIO(""),
        ALERTA_POSSUI_REGISTROS("operador.alerta.possui.registros"),
        ALERTA_NOME_INVALIDO("operador.alerta.nomeInvalido"),
        ALERTA_NAO_CAD("operador.alerta.naocad"),
        ALERTA_JA_CAD("operador.alerta.jacad"),
        ALERTA_NOME_ID_NULL("operador.alerta.id.nome.null"),
        ALERTA_ALTERAR_ADMIN("operador.alerta.admin.naomudar"),
        ALERTA_GRUPO_ADMIN("operador.alerta.grupoAdmin"),
        ALERTA_TROCAR_SENHA("operador.alerta.trocaSenha"),
        ALERTA_TROCAR_SENHA_EXPIRADA("operador.alerta.trocaSenha.expirada"),
        ALERTA_DESATIVADO("operador.alerta.bloqueado"),
        ALERTA_NAO_ENCONTRADO("operador.alerta.naoEncontrador"),
        ALERTA_MAX_TENTATIVA_LOGIN("operador.alerta.maxTentativas"),
        ALERTA_SENHA_INVALIDA("operador.alerta.senhaInvalida"),
        ALERTA_ADMIN_PEMISSOES("operador.alerta.alterarPemissoes"),
        ALERTA_ADMIN_BLOQUEAR("operador.alerta.adminBloq"),
        ALERTA_SENHA_BLOQUEADA("operador.alerta.senhaBloq"),
        ALERTA_MAX_TENTATIVA_LOGIN_TEMPORARIO("operador.alerta.maxTentativasTempo"),
        ALERTA_ALTERAR_ID("operador.alerta.alterarId"),
        ALERTA_EMAIL_JA_CAD("operador.alerta.emailJaCad"),
        ALERTA_EMAIL_INVALIDO("operador.alerta.emailInvalido"),
        ALERTA_SENHA_ATUAL_NAO_CONFERE("operador.alerta.senhaAtualNaoConfere"),
        ALERTA_VICULADO_FUNCIONARIO("operador.alerta.funcionario"),
        ALERTA_CODIGO_INVALIDO("operador.alerta.codigoInvalido"),
        ALERTA_EMAIL_ENVIADO("operador.alerta.recuperacaoSenha"),
        ALERTA_EMAIL_NAO_CADASTRADO("operador.alerta.emailNaoCadastrado"),
        ALERTA_EMAIL_NAO_ENVIADO("operador.alerta.emailnaoEnviado"),
        ALERTA_NAO_CADASTRADO("operador.alerta.naoCadastrado"),
        ALERTA_OPERADOR_SISTEMA("operador.alerta.usoSistema");

        private final String resource;

        private OPERADOR(String descricao) {
            this.resource = descricao;
        }

        public String getResource() {
            return resource;
        }

    }

    public enum REST {

        SUCESSO("rest.sucesso"),
        ERRO_SALVAR("rest.erro.salvar"),
        ERRO_LISTAR("rest.erro.listar"),
        ERRO_EXCLUIR("rest.erro.excluir"),
        ERRO_BUSCAR("rest.erro.buscar"),
        ERRO_ATUALIZAR("rest.erro.atualizar"),
        ERRO_QTD("rest.erro.qtd");

        private final String resource;

        private REST(String descricao) {
            this.resource = descricao;
        }

        public String getResource() {
            return resource;
        }

    }

    public enum DAO {

        ERRO_SALVAR("dao.erro.salvar"),
        ERRO_LISTAR("dao.erro.listar"),
        ERRO_EXCLUIR("dao.erro.excluir"),
        ERRO_BUSCAR("dao.erro.buscar"),
        ERRO_ATUALIZAR("dao.erro.atualizar"),
        ERRO_QTD("dao.erro.qtd");

        private final String resource;

        private DAO(String resource) {
            this.resource = resource;
        }

        public String getResource() {
            return resource;
        }

    }

    public enum SEGURANCA {

        COMPLEX_SENHA("seguranca.alerta.complexSenha"),
        TAM_MIN("seguranca.alerta.minSenha"),
        SENHA_RECENTE("seguranca.alerta.senhaRecente"),
        ERRO("seguranca.validacao.seguranca"),
        CONFIG_VALIDA("seguranca.configuracao.valida"),
        FALHA_AUTENTICACAO("seguranca.alerta.autenticacao"),
        TAMANHO_MINIMO_COMPLEXIDADE("seguranca.alerta.tamanhoMinimoComplex");

        private final String resource;

        private SEGURANCA(String value) {
            this.resource = value;
        }

        public String getResource() {
            return resource;
        }

    }

    public enum FERRAMENTAS {

        FUNCIONARIO_DEMITIDO("ferramentas.alerta.funcionario.demitido"),
        MARCACAO_JA_CADASTRADA("ferramentas.alerta.marcacao.ja.cadastrada");

        private final String resource;

        private FERRAMENTAS(String value) {
            this.resource = value;
        }

        public String getResource() {
            return resource;
        }
    }

    public enum CADASTRO {

        ALERTA_NAO_CADASTRADO("registro.alerta.naocad"),
        ALERTA_NAO_CADASTRADO_FEM("registro.alerta.naocad.fem"),
        ALERTA_NAO_CADASTRADO2("registro.alerta.naocad2"),
        SUCESSO_EXCLUIR("registro.sucesso.exluido"),
        SUCESSO_EXCLUIR_FEM("registro.sucesso.exluida"),
        SUCESSO_ALTERAR("registro.sucesso.alterado"),
        SUCESSO_ALTERAR_FEM("registro.sucesso.alterada"),
        ERRO_ALTERAR("registro.erro.alterar"),
        ERRO_ALTERAR_FEM("registro.erro.alterar.fem"),
        ERRO_SALVAR("registro.erro.salvar"),
        ERRO_SALVAR_FEM("registro.erro.salvar.fem"),
        SUCESSO_SALVAR("registro.sucesso.salvar"),
        ERRO_LISTAR("registro.erro.listar"),
        ERRO_BUSCAR("registro.erro.buscar"),
        ALERTA_MESMA_DESC("registro.alerta.jaCadComDescricao"),
        ALERTA_MESMA_DESC_FEM("registro.alerta.jaCadComDescricao.fem"),
        ERRO_EXCLUIR("registro.erro.excluir"),
        ERRO_QTD("registro.erro.qtd"),
        ERRO_EXCLUIR_GENERICO("registro.erro.excluir.generico"),
        ALERTA_JA_CADASTRADO("registro.alerta.jaCad"),
        ERRO_VALIDAR("registro.erro.validar");

        private final String resource;

        private CADASTRO(String value) {
            this.resource = value;
        }

        public String getResource() {
            return resource;
        }

    }

    public enum GRUPO {

        ALERTA_ALTERAR_ADMIN("grupo.alerta.alterarAdmin"),
        ALERTA_EXCLUIR_ADMIN("grupo.alerta.excluirAdmin"),
        ALERTA_EXCLUIR_COM_OPERADOR("grupo.alerta.ecluirComOperador"),
        ALERTA_NENHUM_GRUPO_EXCLUIDO("grupo.alerta.nenhumGrupoExcluido"),
        ALERTA_ALGUNS_GRUPO_EXCLUIDO("grupo.alerta.algunsGrupoExcluido"),
        ALERTA_GRUPO_DO_SISTEMA("grupo.alerta.usoSistema");

        private final String resource;

        private GRUPO(String valor) {
            this.resource = valor;
        }

        public String getResource() {
            return resource;
        }

    }

    public enum REP {

        ALERTA_NUM_SERIE_JA_CAD("rep.alerta.serieJaCad"),
        ALERTA_NUM_REP_JA_CAD("rep.alerta.numRepJaCad"),
        ALERTA_NUM_SERIE_INVALIDO("rep.alerta.numSerieInvalido"),
        ALERTA_NUM_FABRICANTE_INVALIDO("rep.alerta.numfabricanteInvalido"),
        ALERTA_CODIGO_MODELO_INVALIDO("rep.alerta.codigoModeloInvalido"),
        ALERTA_EXCLUIR_POSSUI_MARCACAO("rep.alerta.excluir.possui.marcacao");

        private final String resource;

        private REP(String valor) {
            this.resource = valor;
        }

        public String getResource() {
            return resource;
        }
    }

    public enum DEPARTAMENTO {

        ALERTA_DESCRICAO_JA_CAD("departamento.alerta.descricaoJaCad"),
        ALERTA_DESCRICAO_OBRIGATORIO("departamento.alerta.descricaoObrigatoria"),
        ALERTA_EXISTE_FUNCIONARIO("departamento.alerta.funcionario.vinculado"),
        ALERTA_EXISTE_FECHAMENTO("departamento.alerta.fechamento"),
        ALERTA_EMPRESA_OBRIGATORIA("departamento.alerta.empresaObrigatoria");

        private final String resource;

        private DEPARTAMENTO(String valor) {
            this.resource = valor;
        }

        public String getResource() {
            return resource;
        }
    }

    public enum FERIADO {

        ALERTA_DESCRICAO_JA_CAD("feriado.alerta.descricaoJaCad"),
        ALERTA_EXCLUSAO_EM_USO("feriado.alerta.emUso");

        private final String resource;

        private FERIADO(String valor) {
            this.resource = valor;
        }

        public String getResource() {
            return resource;
        }
    }

    public enum CALENDARIO {

        ALERTA_DESCRICAO_JA_CAD("calendario.alerta.descricaoJaCad"),
        ALERTA_EXCLUSAO_NAO_PERMITIDA("calendario.alerta.naoExcluir"),
        ALERTA_PERIODO_JA_CADASTRADO("calendario.alerta.periodoJaCadastrado"),
        ALERTA_CALENDARIO_VALIDADO("calendario.alerta.validacao.sucesso"),
        ALERTA_QTD_MAX_COPIAR("calendario.alerta.maxCopiar");

        private final String resource;

        private CALENDARIO(String valor) {
            this.resource = valor;
        }

        public String getResource() {
            return resource;
        }
    }

    public enum CEI {

        ALERTA_CEI_INVALIDO("cei.alerta.invalido"),
        ALERTA_CEI_JA_CADASTRADO("cei.alerta.jaCadastrado"),
        ALERTA_REP_VINCULADO("cei.alerta.possuiVinculos.rep"),
        ALERTA_FUNCIONARIO_VINCULADO("cei.alerta.possuiVinculos.funcionario"),
        ALERTA_CEI_EMPRESA_OBRIGATORIA("cei.alerta.empresaObrigatoria");

        private final String resource;

        private CEI(String valor) {
            this.resource = valor;
        }

        public String getResource() {
            return resource;
        }
    }

    public enum DSR {

        ALERTA_DSR_INVALIDO("dsr.alerta.invalido"),
        ALERTA_DSR_JA_CADASTRADO("dsr.alerta.jaCadastrado");

        private final String resource;

        private DSR(String valor) {
            this.resource = valor;
        }

        public String getResource() {
            return resource;
        }
    }

    public enum PERCENTUAIS_ACRESCIMO {

        ALERTA_PERCENTUAL_INVALIDO("percentuais.alerta.invalido"),
        ALERTA_PERCENTUAL_JA_CADASTRADO("percentuais.alerta.jaCadastrado"),
        ALERTA_EXISTE_VINCULO("percentuais.alerta.existeVinculo"),
        ALERTA_QTD_MAXIMA_COPIA("percentuais.alerta.maximo.copiar");

        private final String resource;

        private PERCENTUAIS_ACRESCIMO(String valor) {
            this.resource = valor;
        }

        public String getResource() {
            return resource;
        }
    }

    public enum SEQUENCIA_PERCENTUAIS_ACRESCIMO {

        ALERTA_SEQUENCIA_PERCENTUAL_INVALIDO("sequencia_percentuais.alerta.invalido"),
        ALERTA_SEQUENCIAPERCENTUAL_JA_CADASTRADO("sequencia_percentuais.alerta.jaCadastrado"),
        ALERTA_SEQUENCIA_OBRIGATORIO("sequencia.alerta.percentuais.obrigatorio");

        private final String resource;

        private SEQUENCIA_PERCENTUAIS_ACRESCIMO(String valor) {
            this.resource = valor;
        }

        public String getResource() {
            return resource;
        }
    }

    public enum BANCOHORAS {

        ALERTA_DESCRICAO_JA_CAD("bancohoras.alerta.descricaoJaCad"),
        ALERTA_EXCLUSAO_NAO_PERMITIDA("bancohoras.alerta.naoExcluir"),
        ALERTA_EXISTE_FECHAMENTO("bancohoras.alerta.existeFechamento"),
        ALERTA_MENSAL_PERCENTUAL("bancohoras.alerta.mensalPercentual");

        private final String resource;

        private BANCOHORAS(String valor) {
            this.resource = valor;
        }

        public String getResource() {
            return resource;
        }
    }

    public enum MOTIVOS {

        ALERTA_DESCRICAO_JA_CAD("motivo.alerta.descricaoJaCad"),
        ALERTA_EXCLUSAO_NAO_PERMITIDA_ABONO("motivo.alerta.excluirAbono"),
        ALERTA_EXCLUSAO_NAO_PERMITIDA_MARCACAO("motivo.alerta.excluirmarcacao"),
        ALERTA_EXCLUSAO_NAO_PERMITIDA_AFASTAMENTO("motivo.alerta.excluirafastamento"),
        ALERTA_ALTERAR_NAO_PERMITIDA_ABONO("motivo.alerta.alterarAbovo"),
        ALERTA_ALTERAR_NAO_PERMITIDA_CARTAO("motivo.alerta.alterarCartao"),
        ALERTA_EXCLUSAO_NAO_PERMITIDA_CARTAO("motivo.alerta.excluirCartao"),
        ALERTA_EXCLUSAO_NAO_PERMITIDA_COMPENSACAO("motivo.alerta.excluirCompensacao"),
        ALERTA_ALTERAR_NAO_PERMITIDA_COMPENSACAO("motivo.alerta.alteracaoCompensacao");

        private final String resource;

        private MOTIVOS(String valor) {
            this.resource = valor;
        }

        public String getResource() {
            return resource;
        }
    }

    public enum CARTOES {

        ALERTA_CARTAO_EM_USO("cartao.alerta.emUso"),
        ALERTA_DATA_INICIO_ANTERIOR_ADMISSAO("cartao.alerta.dataInicioAnteriorAdmissao"),
        ALERTA_JA_POSSUI_ESTA_DATA_INICIO("cartao.alerta.jaPossuiEstaDataInicio"),
        ALERTA_DATA_INICIO_POSTERIO_DEMISSAO("cartao.alerta.dataInicioPosterioDemissao"),
        ALERTA_PROBLEMA_AO_BUSCAR("cartao.alerta.problema.ao.buscar");

        private final String resource;

        private CARTOES(String valor) {
            this.resource = valor;
        }

        public String getResource() {
            return resource;
        }

        @Override
        public String toString() {
            return getResource();
        }
    }

    public enum EMPRESA {

        ALERTA_CNPJ_JA_CADASTRADO("empresa.alerta.cnpjJaCadastrado"),
        ALERTA_CPF_JA_CADASTRADO("empresa.alerta.cpfJaCadastrado"),
        ALERTA_VINCULO_PLANO("empresa.alerta.vinculoPlano"),
        ALERTA_POSSUI_FECHAMENTO("empresa.alerta.viculoFechamento"),
        ALERTA_POSSUI_FECHAMENTO_EM_ANDAMENTO("empresa.alerta.viculoFechamentoEmAndamento"),
        ALERTA_VINCULO_FUNCIONARIO("empresa.alerta.vinculoFuncionario"),
        ALERTA_VINCULO_FUNCIONARIOS("empresa.alerta.vinculoFuncionarios"),
        ALERTA_POSSUI_DSR("empresa.alerta.vinculoDsr"),
        ALERTA_VINCULO_REP("empresa.alerta.vinculoRep");

        private final String resource;

        private EMPRESA(String valor) {
            this.resource = valor;
        }

        public String getResource() {
            return resource;
        }
    }

    public enum FECHAMENTO {

        ALERTA_SEM_FUNCIOANRIO("fechamento.alerta.empresaSemFuncionario"),
        ALERTA_FUNCIONARIO_BANCO_HORAS_ABERTO("fechamento.alerta.funcionarioBancoHorasAberto"),
        ALERTA_EXISTE_FECHAMENTO_PERIODO("fechamento.alerta.existeFechamento"),
        ALERTA_EXISTE_FECHAMENTO_PERIODO_CADASTRO("fechamento.alerta.existeFechamentoCadastro");

        private final String resource;

        private FECHAMENTO(String valor) {
            this.resource = valor;
        }

        public String getResource() {
            return resource;
        }
    }

    public enum PLANOS {

        ALERTA_LIMITE_OPERADOR("planos.alerta.limiteOperador"),
        ALERTA_LIMITE_FUNCIONARIOS("cartao.alerta.emUso"),
        ALERTA_LIMITE_EMPRESAS("cartao.alerta.dataInicioAnteriorAdmissao"),
        ALERTA_LIMITE_PLANO_EXPIROU("cartao.alerta.jaPossuiEstaDataInicio"),
        ALERTA_NAO_EXISTE_UM_PLANO_VALIDO("planos.alerta.planoInvalido");

        private final String resource;

        private PLANOS(String valor) {
            this.resource = valor;
        }

        public String getResource() {
            return resource;
        }
    }

    public enum FUNCIONARIO {

        ALERTA_FUNCIONARIO_NAO_CAD("funcionario.alerta.naoCad"),
        ALERTA_FUNCIONARIO_PIS_JA_CAD("funcionario.alerta.pisJaCad"),
        ALERTA_DATA_INICIAL_ANTERIOR_ADMISSAO("funcionario.alerta.dataInicialAnterioAdmissao"),
        ALERTA_DATA_FINAL_ANTERIOR_ADMISSAO("funcionario.alerta.dataFinalAnterioAdmissao"),
        ALERTA_DATA_COMPENSADA_ANTERIOR_ADMISSAO("funcionario.alerta.dataCompensadaAnterioAdmissao"),
        ALERTA_DATA_INICIAL_POSTERIOR_DEMISSAO("funcionario.alerta.dataInicialPosterioDemissao"),
        ALERTA_DATA_FINAL_POSTERIOR_DEMISSAO("funcionario.alerta.dataFinalPosterioDemissao"),
        ALERTA_DATA_COMPENSADA_POSTERIOR_DEMISSAO("funcionario.alerta.dataCompensadaPosterioDemissao"),
        ALERTA_DATA_INICIAL_MAIOR_DATA_FIM("funcionario.alerta.dataInicioMaiorDataFim"),
        ALERTA_PIS_INVALIDO("funcionario.alerta.pisInvalido"),
        ALERTA_CARGO_EM_USO("funcionario.alerta.funcionario.uso"),
        ALERTA_FUNCIONARIO_VINCULO_OPERADOR("funcionario.alerta.vinculoOperador"),
        ALERTA_POSSUI_FECHAMENTOS("funcionario.alerta.excluir.possuiFechamento"),
        ALERTA_POSSUI_MARCACOES("funcionario.alerta.excluir.possuiMarcacoes"),
        ALERTA_DATA_CARTAO_ANTERIOR_ADMISSAO("funcionario.alerta.dataCartaoAnteriorAdmissao"),
        //        ALERTA_DATA_CARTAO_POSTERIOR_ADMISSAO("funcionario.alerta.dataCartaoPosteriordemissao"),
        ALERTA_DATA_JORNADA_ANTERIOR_ADMISSAO("funcionario.alerta.dataJornadaAnteriorAdmissao"),
        //        ALERTA_DATA_JORNADA_POSTERIOR_ADMISSAO("funcionario.alerta.dataJornadaPosteriordemissao"),
        ALERTA_DATA_CALENDARIO_ANTERIOR_ADMISSAO("funcionario.alerta.dataCalendarioAnteriorAdmissao"),
        //        ALERTA_DATA_CALENDARIO_POSTERIOR_ADMISSAO("funcionario.alerta.dataCalendarioPosteriordemissao"),
        ALERTA_DATA_AFASTAMENTO_ANTERIOR_ADMISSAO("funcionario.alerta.dataAfastamentoAnteriorAdmissao"),
        //        ALERTA_DATA_AFASTAMENTO_POSTERIROR_ADMISSAO("funcionario.alerta.dataAfastamentoPosteriordemissao"),
        ALERTA_DATA_COMPENSACAO_ANTERIOR_ADMISSAO("funcionario.alerta.dataCompensacaoAnteriorAdmissao"),
        //        ALERTA_DATA_COMPENSACAO_POSTERIOR_ADMISSAO("funcionario.alerta.dataCompensacaoPosteriordemissao"),
        ALERTA_DATA_BHMANUTENCAO_ANTERIOR_ADMISSAO("funcionario.alerta.dataBHManutencaoAnteriorAdmissao"),
        //        ALERTA_DATA_BHMANUTENCAO_POSTERIOR_ADMISSAO("funcionario.alerta.dataBHManutencaoPosteriordemissao"),
        ALERTA_DATA_BHFECHAMENTO_ANTERIOR_ADMISSAO("funcionario.alerta.dataBHFechamentoAnteriorAdmissao");
//        ALERTA_DATA_BHFECHAMENTO_POSTERIOR_ADMISSAO("funcionario.alerta.dataBHFechamentoPosteriordemissa");

        private final String resource;

        private FUNCIONARIO(String valor) {
            this.resource = valor;
        }

        public String getResource() {
            return resource;
        }

    }

    public enum FUNCIONARIO_JORNADA {

        ALERTA_DATA_INICIAL_JA_CADASTRADA("funcionarioJornada.alerta.jaPossuiEstaDataInicio"),
        ALERTA_PERIODO_INVAIDO("funcionarioJornada.alerta.excecao.periodoInvalido"),
        ALERTA_EXCECAO_JORNADA_NAO_CADASTRADA("funcionarioJornada.alerta.excecao.jornadaNaoCad"),
        ALERTA_DATA_INICIAL_MAIOR_DATA_FIM("funcionarioJornada.alerta.dataInicioMenorDataFim"),
        ALERTA_EXCECAO_JA_CADASTRADA("funcionarioJornada.alerta.excecao.periodoJaCadastrado"),
        ALERTA_EXCECAO_EXCLUIR_PRIMEIRA_JORNADA("funcionarioJornada.alerta.excecao.excluir"),
        ALERTA_ENTRE_JORNADA("funcionarioJornada.alerta.entreJornada"),
        ALERTA_GENERO_ENTIDADE("funcionarioJornada.alerta.generoEntidade"),
        ALERTA_CAMPO_SEQUENCIA_INICIAL("funcionarioJornada.alerta.campoSequenciaInicial");

        private final String resource;

        private FUNCIONARIO_JORNADA(String valor) {
            this.resource = valor;
        }

        public String getResource() {
            return resource;
        }
    }

    public enum FUNCIONARIO_CALENDARIO {

        ALERTA_DATA_INICIAL_JA_CADASTRADA("funcionarioCalendario.alerta.jaPossuiEstaDataInicio"),
        ALERTA_GENERO_ENTIDADE("funcionarioCalendario.alerta.generoEntidade");

        private final String resource;

        private FUNCIONARIO_CALENDARIO(String valor) {
            this.resource = valor;
        }

        public String getResource() {
            return resource;
        }
    }

    public enum FUNCIONARIO_AFASTAMENTO {

        ALERTA_DATA_INICIAL_POSTERIOR_RETORNO("funcionarioAfastamento.alerta.dataInicioPosteriorRetorno"),
        ALERTA_MESMO_PERIODO("funcionarioAfastamento.alerta.mesmoPeriodo"),
        ALERTA_PERIODO_INVALIDO("funcionarioAfastamento.alerta.periodoObrigatorio"),
        ALERTA_GENERO_ENTIDADE("funcionarioAfastamento.alerta.generoEntidade");

        private final String resource;

        private FUNCIONARIO_AFASTAMENTO(String valor) {
            this.resource = valor;
        }

        public String getResource() {
            return resource;
        }
    }

    public enum FUNCIONARIO_COMPENSACAO {

        ALERTA_GENERO_ENTIDADE("funcionarioCompensacao.alerta.generoEntidade"),
        ALERTA_JA_EXISTE_UMA_DATA_DENTRO_DO_PERIODO("funcionarioCompensacao.alerta.jaExisteUmaDataDentroDoPeriodo"),
        ALERTA_DATA_INICIAL_POSTERIOR_DATA_FINAL("funcioanrioCompensacao.alerta.dataInicialPosteriorDataFinal"),
        ALERTA_DATA_COMPENSADA_DENTRO_PERIODO_COMPENSACAO("funcionarioCompensacao.alerta.dataCompensadaDentroPeriodoCompensacao"),
        ALERTA_DATA_COMPESACAO_JA_CADASTRADA("funcionarioCompensacao.alerta.data.compesada.jaCadastrada"),
        ALERTA_PERIODO_JA_CADASTRADO("funcionarioCompensacao.alerta.periodoJaCadastrado");

        private final String resource;

        private FUNCIONARIO_COMPENSACAO(String valor) {
            this.resource = valor;
        }

        public String getResource() {
            return resource;
        }
    }

    public enum FUNCIONARIO_BANCO_DE_HORAS {

        ALERTA_PERIODO_INVALIDO("funcionarioBancoHoras.alerta.periodoInvalido"),
        ALERTA_PERIODO_JA_CADASTRADO("funcionarioBancoHoras.alerta.periodoJaCadastrado"),
        ALERTA_DATA_INICIAL_POSTERIOR_ESTA_DATA("funcionarioBancoHoras.alerta.dataInicioPosteriorEstaData"),
        ALERTA_SUB_TOTAIS("funcionarioBancoHoras.alerta.subtotais"),
        ALERTA_RECALCULAR_SUB_TOTAIS("funcionarioBancoHoras.alerta.recalcularSubtotais"),
        ALERTA_DATA_INICIAL_JA_CADASTRADA("funcionarioBancoHoras.alerta.dataInicialJaCadastrada"),
        ALERTA_DATA_FINAL_MAXIMO("funcionarioBancoHoras.alerta.dataFinal.maximo"),
        ALERTA_EXCLUSAO_NAO_PERMITIDA("funcionarioBancoHoras.alerta.exclusao.NaoPermitida"),
        ALERTA_BANCO_ABERTO("funcionarioBancoHoras.alerta.bancoAberto"),
        ALERTA_GENERO_ENTIDADE("funcionarioBancoHoras.alerta.generoEntidade"),
        ALERTA_FECHAMENTO_BH("funcionarioBancoHoras.alerta.fechamentoBH"),
        ALERTA_EXCLUSAO_SUBTOTAL("funcionarioBancoHoras.alerta.excluirSubtotal"),
        ALERTA_EDITAR_SUBTOTAL("funcionarioBancoHoras.alerta.EditarSubtotal"),
        ALERTA_INCLUIR_BANCO_NAO_ENCONTRADO("funcionarioBancoHoras.alerta.bancoDeHorasNaoEncontrado"),
        ALERTA_FECHAMENTO_GATILHO_NAO_ATINGIDO("funcionarioBancoHoras.alerta.gatihoNaoAtingido"),
        ALERTA_FECHAMENTO_JA_CADASTRADO_NA_DATA("funcionarioBancoHoras.alerta.jaPossuiFechamento"),
        ALERTA_FECHAMENTO_ACERTO_JA_CADASTRADO_NA_DATA("funcionarioBancoHoras.alerta.jaPossuiAcerto"),
        ALERTA_FECHAMENTO_SUBTOTAL_JA_CADASTRADO_NA_DATA("funcionarioBancoHoras.alerta.jaPossuiSubtotal"),
        ALERTA_FECHAMENTO_EDICAOSALDO_JA_CADASTRADO_NA_DATA("funcionarioBancoHoras.alerta.jaPossuiEdicaoSaldo"),
        ALERTA_FECHAMENTO_SALDO_ZERADO("funcionarioBancoHoras.alerta.saldoZerado"),
        ALERTA_FECHAMENTO_GATILHOS_ZERADOS("funcionarioBancoHoras.alerta.gatilhosZerados"),
        ALERTA_FECHAMENTO_ACERTO_MANUAL_ZERADO("funcionarioBancoHoras.alerta.acertoManualZerado"),
        ALERTA_FECHAMENTO_EDICAO_SALDO_SUBTOTAL_POSTERIOR("funcionarioBancoHoras.alerta.acertoEdicaoSaldoSubtotalPosterior");

        private final String resource;

        private FUNCIONARIO_BANCO_DE_HORAS(String valor) {
            this.resource = valor;
        }

        public String getResource() {
            return resource;
        }
    }

    public enum JORNADA {

        ALERTA_SEMANAL_QTD_DIAS("jornada.alerta.semanalQtdDias"),
        ALERTA_LIMITE_MAXIMO_HORARIOS("jornada.alerta.limiteMaximoHorarios"),
        ALERTA_DECRICAO_JA_CADASTRADA("jornada.alerta.descricaoJaCadastrada"),
        ALERTA_EXCLUIR("jornada.alerta.excluir"),
        ALERTA_JORNADA_SEM_HORARIO("jornada.alerta.semHorario"),
        ALERTA_EXCLUIR_VINCULO_FUNCIONARIO("jornada.alerta.vinculoComFuncionario"),
        ALERTA_EDITAR_VINCULO_FUNCIONARIO("jornada.alerta.atualizar"),
        ALERTA_PARAMETRO_PERCENTUAL_MAX("jornada.alerta.parametros.max"),
        ALERTA_PARAMETRO_INICIO_FIM("jornada.alerta.parametros.inicioFim"),
        ALERTA_PARAMETRO_PERCENTUAL_OBRIGATORIO("jornada.alerta.parametros.percentual"),
        ALERTA_HORA_EXTRA_DIA_SEMANA("jornada.alerta.horaExtra.diaSemana"),
        ALERTA_HORA_EXTRA_DIA_MES("jornada.alerta.horaExtra.diaMes"),
        ALERTA_HORA_EXTRA_DIA_INVALIDO("jornada.alerta.horaExtra.diaInvalido"),
        ALERTA_MAXIMO_COPIA("jornada.alerta.maximo.copiar");

        private final String resource;

        private JORNADA(String valor) {
            this.resource = valor;
        }

        public String getResource() {
            return resource;
        }
    }

    public enum HORARIO {

        ALERTA_TOTAL_HORAS_MAIOR_24("horario.alerta.somaHorariosMaiorQue24Horas"),
        ALERTA_ATUALIZAR("horario.alerta.atualizar"),
        ALERTA_DECRICAO_JA_CADASTRADA("horario.alerta.descricao.jaCadastrado"),
        ALERTA_EXCLUIR("horario.alerta.excluir"),
        ALERTA_SEM_MARCACAO("horario.alerta.semMarcacao");

        private final String resource;

        private HORARIO(String valor) {
            this.resource = valor;
        }

        public String getResource() {
            return resource;
        }
    }

    public enum HORARIO_MARCACAO {

        ALERTA_HORA_INICIAL_MAIOR_FINAL("horarioMarcacao.alerta.horaInicialMaiorHoraFinal"),
        ALERTA_PERIODO_JA_CADASTRADO("horarioMarcacao.alerta.periodoJaCadastrado");

        private final String resource;

        private HORARIO_MARCACAO(String valor) {
            this.resource = valor;
        }

        public String getResource() {
            return resource;
        }
    }

    public enum JORNADA_HORARIO {

        ALERTA_INICIA_ANTERIO_ULTIMO("jornadaHorario.alerta.inicioAnteriorUltimo"),
        ALERTA_NAO_INFERIOR_11_HORAS("jornadaHorario.alerta.naoInferior11Horas"),
        ALERTA_QDT_DIAS_JORNADA_SEMANAL("jornadaHorario.alerta.qtdDiasSemanal"),
        ALERTA_QDT_DIAS_JORNADA("jornadaHorario.alerta.qtdDias"),
        ALERTA_ALTERACAO_NAO_PERMITIDA("jornadaHorario.alerta.alteracao.naoPermitida");

        private final String resource;

        private JORNADA_HORARIO(String valor) {
            this.resource = valor;
        }

        public String getResource() {
            return resource;
        }
    }

    public enum COLETIVO {

        ALERTA_EXCLUIR_COLETIVO("coletivo.alerta.excluir");
        private final String resource;

        private COLETIVO(String valor) {
            this.resource = valor;
        }

        public String getResource() {
            return resource;
        }
    }
}
