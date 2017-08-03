/**
 * @Author: juliano.ezequiel <Juliano>
 * @Date: 02-09-2016
 * @Email: juliano.ezequiel@topdata.com.br
 * @Project: TopPontoWeb
 * @Last modified by: Juliano
 * @Last modified time: 28-10-2016
 */
package com.topdata.toppontoweb.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.topdata.toppontoweb.entity.jornada.Horario;
import com.topdata.toppontoweb.entity.tipo.TipoFechamento;
import com.topdata.toppontoweb.entity.autenticacao.Funcionalidades;
import com.topdata.toppontoweb.entity.autenticacao.FuncionalidadesGrupoOperacao;
import com.topdata.toppontoweb.entity.autenticacao.FuncionalidadesPlanoOperacao;
import com.topdata.toppontoweb.entity.autenticacao.Grupo;
import com.topdata.toppontoweb.entity.autenticacao.HistoricoSenhas;
import com.topdata.toppontoweb.entity.autenticacao.Modulos;
import com.topdata.toppontoweb.entity.autenticacao.Operador;
import com.topdata.toppontoweb.entity.autenticacao.Permissoes;
import com.topdata.toppontoweb.entity.autenticacao.Planos;
import com.topdata.toppontoweb.entity.autenticacao.Seguranca;
import com.topdata.toppontoweb.entity.calendario.Feriado;
import com.topdata.toppontoweb.entity.configuracoes.Dsr;
import com.topdata.toppontoweb.entity.empresa.Departamento;
import com.topdata.toppontoweb.entity.empresa.Empresa;
import com.topdata.toppontoweb.entity.jornada.HorarioMarcacao;
import com.topdata.toppontoweb.entity.jornada.Jornada;
import com.topdata.toppontoweb.entity.jornada.JornadaHorario;
import com.topdata.toppontoweb.entity.rep.ModeloRep;
import com.topdata.toppontoweb.entity.tipo.Operacao;
import com.topdata.toppontoweb.entity.tipo.Semana;
import com.topdata.toppontoweb.entity.tipo.TipoAuditoria;
import com.topdata.toppontoweb.entity.tipo.TipoDia;
import com.topdata.toppontoweb.entity.tipo.TipoDocumento;
import com.topdata.toppontoweb.entity.tipo.TipoEquipamento;
import com.topdata.toppontoweb.entity.tipo.TipoMotivo;
import com.topdata.toppontoweb.services.funcionario.cargo.CargoService;
import com.topdata.toppontoweb.services.funcionario.cargo.FuncionarioCargoService;
import com.topdata.toppontoweb.services.funcionario.FuncionarioService;
import com.topdata.toppontoweb.services.permissoes.OperadorService;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES.Enum_AUDITORIA;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES.Enum_DOCUMENTO;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES.Enum_FUNCIONALIDADE;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES.Enum_MODULOS;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES.Enum_OPERACAO;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES.Enum_PERMISSAO;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES.Enum_TIPO_MOTIVO;

/**
 *
 * @author juliano.ezequiel
 */
public class DataBaseInitializer {

    @Autowired
    private Dao dao;

    @Autowired
    private OperadorService operadorService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private FuncionarioCargoService funcionarioCargoService;

    @Autowired
    private FuncionarioService funcionarioService;

    @Autowired
    private CargoService cargoService;

    HashMap<String, Grupo> mapGrupos = new HashMap<>();
    HashMap<Enum_PERMISSAO, Permissoes> mapPermissoes = new HashMap<>();
    HashMap<Enum_FUNCIONALIDADE, Funcionalidades> mapFuncionalidade = new HashMap<>();
    HashMap<Enum_OPERACAO, Operacao> mapOperacoes = new HashMap<>();

    private Boolean init;

    protected DataBaseInitializer() {
    }

    public DataBaseInitializer(Dao dao, OperadorService operadorServices, Boolean init) {
        this.dao = dao;
        this.operadorService = operadorServices;
        this.init = init;
    }

    public void initDataBase() {
        try {
//            List<Operador> list = dao.findAll(Operador.class);
//            if (list.isEmpty()) {;
//            criarPermissoes();
//            criarGrupos();
//            criarGruposPermiscao();
//            criarTipDoc();
//            criarEmpresa();
//            criarDepartamento();
//            criarModulos();
//            criarOperacoes();
//
//            criarFuncioanlidades();
//            criarFuncionalidadeGrupo();
//
//            criarPlano();
//            criarFuncionalidadesPlano();
//
//            criarTiposAuditoria();
//            criarConfigSeguranca();
//            criarOperadores();

//            criarModelosRep();
//            criarTipoEquipamento();
//            criarToleranciaOcorrencias();
//            criarTiposDia();
//            criarSemana();
//
//            criarDsrDefault();
//
//            criarFeriadosDefault();
//
//            criarTiposMotivos();
//
//            criarJornadas();
//            criarHorarios();
//            criarJornadaHorario();
//
//            criarTiposFechamentos();
//            } else {
//                criarFuncioanlidades();
//            }
            verificarDados();
        } catch (DaoException | ServiceException ex) {
            Logger.getLogger(DataBaseInitializer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void criarGrupos() throws DaoException {
//        dao.save(new Grupo(1, CONSTANTES.GRUPO_MASTER));
//        dao.save(new Grupo(2, CONSTANTES.GRUPO_ADMIN));
//        dao.save(new Grupo(4, CONSTANTES.GRUPO_ANONIMO));

    }

    //Grupos de permissoes são fixos para a autenticação do security, todos os usuário deverão fazer
    public void criarGruposPermiscao() throws DaoException {

        dao.findAll(Grupo.class).forEach(new ConsumerImplGrupo());
        dao.findAll(Permissoes.class).forEach(new ConsumerImplPermissoes());
        //Master
        mapGrupos.get(CONSTANTES.GRUPO_MASTER).getPermissoesList().add(mapPermissoes.get(Enum_PERMISSAO.ROLE_MASTER));
        mapGrupos.get(CONSTANTES.GRUPO_MASTER).getPermissoesList().add(mapPermissoes.get(Enum_PERMISSAO.ROLE_ADMIN));
        mapGrupos.get(CONSTANTES.GRUPO_MASTER).getPermissoesList().add(mapPermissoes.get(Enum_PERMISSAO.ROLE_USER));

        //Admin
        mapGrupos.get(CONSTANTES.GRUPO_ADMIN).getPermissoesList().add(mapPermissoes.get(Enum_PERMISSAO.ROLE_ADMIN));
        mapGrupos.get(CONSTANTES.GRUPO_ADMIN).getPermissoesList().add(mapPermissoes.get(Enum_PERMISSAO.ROLE_USER));

        //Anonimo
        mapGrupos.get(CONSTANTES.GRUPO_ANONIMO).getPermissoesList().add(mapPermissoes.get(Enum_PERMISSAO.ROLE_ANONYMOUS));

//        for (Grupo g : mapGrupos.values()) {
//            dao.save(g);
//        }

    }

    public void criarPermissoes() throws ServiceException, DaoException {
        dao.save(new Permissoes(Enum_PERMISSAO.ROLE_MASTER));
        dao.save(new Permissoes(Enum_PERMISSAO.ROLE_ADMIN));
        dao.save(new Permissoes(Enum_PERMISSAO.ROLE_USER));
        dao.save(new Permissoes(Enum_PERMISSAO.ROLE_ANONYMOUS));
    }

    public void criarOperadores() throws ServiceException, DaoException {
        dao.findAll(Grupo.class).forEach(new ConsumerImplGrupo());
        dao.findAll(Permissoes.class).forEach(new ConsumerImplPermissoes());

        Operador operador;
        //operador admin
        if ((operador = operadorService.buscaOperadorPorNome(new Operador(CONSTANTES.OPERADOR_ADMIN))) == null) {
            operador = new Operador();
        }
        operador.setAtivo(Boolean.TRUE);
        operador.setSenhaBloqueada(Boolean.FALSE);
        operador.setTrocaSenhaProximoAcesso(Boolean.FALSE);
        operador.setSenha(this.passwordEncoder.encode(CONSTANTES.OPERADOR_ADMIN));
        operador.setUsuario(CONSTANTES.OPERADOR_ADMIN);
        operador.setUltimoAcesso(Calendar.getInstance().getTime());
        operador.setGrupo(mapGrupos.get(CONSTANTES.GRUPO_ADMIN));
        operador.setEmail("email1@topdata.com");
        operador.setVisualizarAlertas(true);
        operador.setVisualizarMensagens(true);
        operador.setFoto("funcionario");

        dao.save(operador);

        HistoricoSenhas historicoSenhas = new HistoricoSenhas();
        historicoSenhas.setDataHora(new Timestamp(new Date().getTime()));
        historicoSenhas.setIdOperador(operadorService.buscaOperadorPorNome(operador));
        historicoSenhas.setSenha(operador.getSenha());
        historicoSenhas.setIdHistoricoSenhas(1);
        dao.save(historicoSenhas);

        //operador master
        if ((operador = operadorService.buscaOperadorPorNome(new Operador(CONSTANTES.OPERADOR_MASTER))) == null) {
            operador = new Operador();
        }
        operador.setAtivo(Boolean.TRUE);
        operador.setSenhaBloqueada(Boolean.FALSE);
        operador.setTrocaSenhaProximoAcesso(Boolean.FALSE);
        operador.setSenha(this.passwordEncoder.encode(CONSTANTES.OPERADOR_MASTER));
        operador.setUsuario(CONSTANTES.OPERADOR_MASTER);
        operador.setUltimoAcesso(Calendar.getInstance().getTime());
        operador.setGrupo(mapGrupos.get(CONSTANTES.GRUPO_MASTER));
        operador.setEmail("email3@topdata.com");
        operador.setVisualizarAlertas(false);
        operador.setVisualizarMensagens(false);
        operador.setFoto("funcionarioold");
        dao.save(operador);

        historicoSenhas = new HistoricoSenhas();
        historicoSenhas.setDataHora(new Timestamp(new Date().getTime()));
        historicoSenhas.setIdOperador(operadorService.buscaOperadorPorNome(operador));
        historicoSenhas.setSenha(operador.getSenha());
        historicoSenhas.setIdHistoricoSenhas(3);
        dao.save(historicoSenhas);

        //operador anônimo
        if ((operador = operadorService.buscaOperadorPorNome(new Operador(CONSTANTES.OPERADOR_ANONIMO))) == null) {
            operador = new Operador();
        }
        operador.setAtivo(Boolean.TRUE);
        operador.setSenhaBloqueada(Boolean.FALSE);
        operador.setTrocaSenhaProximoAcesso(Boolean.FALSE);
        operador.setSenha(this.passwordEncoder.encode(CONSTANTES.OPERADOR_ANONIMO));
        operador.setUsuario(CONSTANTES.OPERADOR_ANONIMO);
        operador.setUltimoAcesso(Calendar.getInstance().getTime());
        operador.setGrupo(mapGrupos.get(CONSTANTES.GRUPO_ANONIMO));
        operador.setEmail("email4@topdata.com");

        dao.save(operador);

        historicoSenhas = new HistoricoSenhas();
        historicoSenhas.setDataHora(new Timestamp(new Date().getTime()));
        historicoSenhas.setIdOperador(operadorService.buscaOperadorPorNome(operador));
        historicoSenhas.setSenha(operador.getSenha());
        historicoSenhas.setIdHistoricoSenhas(4);
        dao.save(historicoSenhas);

        //operador anônimo
        if ((operador = operadorService.buscaOperadorPorNome(new Operador("juliano"))) == null) {
            operador = new Operador();
        }
        operador.setAtivo(Boolean.TRUE);
        operador.setSenhaBloqueada(Boolean.FALSE);
        operador.setTrocaSenhaProximoAcesso(Boolean.FALSE);
        operador.setSenha(this.passwordEncoder.encode("123456"));
        operador.setUsuario("juliano");
        operador.setUltimoAcesso(Calendar.getInstance().getTime());
        operador.setGrupo(mapGrupos.get(CONSTANTES.GRUPO_ADMIN));
        operador.setEmail("juliano.ezequiel@topdata.com");
        operador.setVisualizarAlertas(false);
        operador.setVisualizarMensagens(true);
        operador.setFoto("funcionario");
        dao.save(operador);

        historicoSenhas = new HistoricoSenhas();
        historicoSenhas.setDataHora(new Timestamp(new Date().getTime()));
        historicoSenhas.setIdOperador(operadorService.buscaOperadorPorNome(operador));
        historicoSenhas.setSenha(operador.getSenha());
        historicoSenhas.setIdHistoricoSenhas(5);
        dao.save(historicoSenhas);

//        for (Integer i = 0; i < 200; i++) {
//            operador = new Operador();
//            operador.setAtivo(Boolean.TRUE);
//            operador.setSenha(this.passwordEncoder.encode(CONSTANTES.OPERADOR_USER));
//            operador.setUsuario(i.toString());
//            operador.setUltimoAcesso(Calendar.getInstance());
//            operador.setGrupo(mapGrupos.get(CONSTANTES.GRUPO_USER));
//            operador.setEmail(i.toString() + "@topdata.com");
//            dao.save(operador);
//
//            historicoSenhas = new HistoricoSenhas();
//            historicoSenhas.setDataHora(new Timestamp(new Date().getTime()));
//            historicoSenhas.setIdOperador(operadorService.buscaOperadorPorNome(operador));
//            historicoSenhas.setSenha(operador.getSenha());
//            historicoSenhas.setIdHistoricoSenhas(1);
//            dao.save(historicoSenhas);
//        }
    }

    public void criarConfigSeguranca() throws ServiceException, DaoException {
        Seguranca seguranca = new Seguranca();
        seguranca.setComplexidadeLetrasNumeros(Boolean.FALSE);
        seguranca.setQtdeBloqueioTentativas(3);
        seguranca.setQtdeDiasTrocaSenha(30);
//        seguranca.setQtdeHorasDesbloqueioUsuario(1);
        seguranca.setQtdeNaoRepetirSenhas(3);
        seguranca.setTamanhoMinimoSenha(6);
        seguranca.setIdSeguranca(1);
        dao.save(seguranca);
    }

    public void criarModulos() throws DaoException, ServiceException {
        //Valores default para os modulos
        if (dao.findAll(Modulos.class).isEmpty()) {
            for (Enum_MODULOS modulos : Enum_MODULOS.values()) {
                if (modulos != Enum_MODULOS.DEFAULT) {
                    dao.save(new Modulos(modulos));
                }
            }
        }
    }

    public void criarOperacoes() throws DaoException, ServiceException {

        //Valores default para as operações
        dao.save(new Operacao(Enum_OPERACAO.SISTEMA));
        mapOperacoes.put(Enum_OPERACAO.INCLUIR, (Operacao) dao.save(new Operacao(Enum_OPERACAO.INCLUIR)));
        mapOperacoes.put(Enum_OPERACAO.EDITAR, (Operacao) dao.save(new Operacao(Enum_OPERACAO.EDITAR)));
        mapOperacoes.put(Enum_OPERACAO.EXCLUIR, (Operacao) dao.save(new Operacao(Enum_OPERACAO.EXCLUIR)));
        mapOperacoes.put(Enum_OPERACAO.CONSULTAR, (Operacao) dao.save(new Operacao(Enum_OPERACAO.CONSULTAR)));
        mapOperacoes.put(Enum_OPERACAO.GERAR, (Operacao) dao.save(new Operacao(Enum_OPERACAO.GERAR)));
        mapOperacoes.put(Enum_OPERACAO.IMPORTAR, (Operacao) dao.save(new Operacao(Enum_OPERACAO.IMPORTAR)));

    }

    public void criarFuncioanlidades() throws DaoException, ServiceException {
        List<Operacao> operacoesList = new ArrayList<>();
        if (dao.findAll(Funcionalidades.class).isEmpty()) {
            for (Enum_FUNCIONALIDADE funcionalidade : Enum_FUNCIONALIDADE.values()) {
                if (funcionalidade != Enum_FUNCIONALIDADE.DEFAULT) {
                    operacoesList.clear();
                    Funcionalidades funcionalidades = new Funcionalidades(funcionalidade, (Modulos) dao.find(Modulos.class, funcionalidade.getModulo().ordinal()));
                    switch (funcionalidade.name()) {
                        case "CAD_EMPRESAS_EMPRESAS":
                        case "CAD_EMPRESAS_DEPARTAMENTO":
                        case "CAD_EMPRESAS_FECHAMENTOS":
                        case "CAD_EMPRESA_CEI":
                        case "CAD_JORNADAS_JORNADAS":
                        case "CAD_FUNCIONARIO_FUNCIONARIO":
                        case "CAD_FUNCIONARIO_JORNADAS":
                        case "CAD_FUNCIONARIO_CALENDARIO":
                        case "CAD_FUNCIONARIO_AFASTAMENTO":
                        case "CAD_FUNCIONARIO_COMPENSASOES":
                        case "CAD_FUNCIONARIO_BANCO_DE_HORAS":
                        case "CAD_FUNCIONARIO_CARTAO":
                        case "CAD_REP":
                        case "CAD_CALENDARIO":
                        case "CAD_BANCO_HORAS_BANCO_HORAS":
                        case "CAD_PERMISSOES_GRUPOS":
                        case "CAD_PERMISSOES_OPERADORES":
                        case "CAD_CONFIGURACOES_DSR":
                        case "CAD_CONFIGURACOES_PERCENTUAIS_ACRESCIMO":
                        case "LC_AFASTAMENTO":
                        case "LC_CALENDARIO":
                        case "LC_JORNADAS":
                        case "LC_COMPENSACOES":
                        case "LC_BANCO_HORAS_MANUTENCAOES":
                        case "LC_BANCO_HORAS_FECHAMENTOS":
                            funcionalidades.getOperacaoList().add(mapOperacoes.get(Enum_OPERACAO.INCLUIR));
                            funcionalidades.getOperacaoList().add(mapOperacoes.get(Enum_OPERACAO.EDITAR));
                            funcionalidades.getOperacaoList().add(mapOperacoes.get(Enum_OPERACAO.EXCLUIR));
                            funcionalidades.getOperacaoList().add(mapOperacoes.get(Enum_OPERACAO.CONSULTAR));
                            break;
                        case "CAD_JORNADAS_PARAMETROS":
                        case "CAD_JORNADAS_LIMITES":
                        case "CAD_JORNADAS_HORA_EXTRAS":
                        case "CAD_BANCO_DE_HORAS_PARAMETROS":
                        case "CAD_BANCO_DE_HORAS_GATILHOS":
                        case "CAD_BANCO_DE_HORAS_PERCENTUAIS_ACRECIMO":
                        case "CAD_PERMISSOES_SEGURANCA":
                        case "CAD_CONFIGURACOES_OCORRENCIAS":
                        case "LC_EXCECOES_JORNADA":
                            funcionalidades.getOperacaoList().add(mapOperacoes.get(Enum_OPERACAO.EDITAR));
                            funcionalidades.getOperacaoList().add(mapOperacoes.get(Enum_OPERACAO.CONSULTAR));
                            break;
                        case "REL_ESPELHO":
                        case "REL_PRESENCA":
                        case "REL_FREQUENCIA":
                        case "REL_OCORRENCIA":
                        case "REL_HORAS_EXTRA":
                        case "REL_BANCO_HORAS":
                        case "REL_AFASTAMENTO":
                        case "REL_ABSENTEISMO":
                        case "REL_INTERJORNADA":
                        case "REL_INTRAJORNADA":
                        case "REL_AUDITORIA":
                        case "REL_CAD_EMPRESA":
                        case "REL_CAD_FUNCIONARIO":
                        case "REL_CAD_CALENDARIO":
                        case "REL_CAD_HORARIO":
                        case "REL_CAD_BANCO_HORAS":
                        case "ARQ_AFDT":
                        case "ARQ_ACJEF":
                        case "ARQ_AFD":
                        case "ARQ_FISCAL":
                        case "FER_MARCACOES":
                        case "FER_FUNCIONARIOS":
                        case "FER_EXPORTACOES_EVENTOS":
                        case "FER_EXPORTACOES_FUNCIONARIOS":
                        case "FER_IMPORTACOES":
                            funcionalidades.getOperacaoList().add(mapOperacoes.get(Enum_OPERACAO.CONSULTAR));
                            break;
                    }
                    dao.save(funcionalidades);
                }
            }
        }
    }

    public void criarPlano() throws DaoException {
        // plano teste
        Planos plano = new Planos();
        plano.setDescricao("Plano teste");
        plano.setIdEmpresa((Empresa) dao.find(Empresa.class, 1));
        plano.setIdPlano(1);
        plano.setLimiteEmpresas(2);
        plano.setLimiteFuncionarios(25);
        plano.setLimiteOperadores(5);
        plano.setPrazoExpira(new Date(117, 6, 30));
        dao.save(plano);

    }

    public void criarFuncionalidadesPlano() throws DaoException {
        //funcionalidades para o plano teste
        FuncionalidadesPlanoOperacao funcionalidadesPlanoOperacao = new FuncionalidadesPlanoOperacao();
        Planos plano = (Planos) dao.find(Planos.class, 1);
        funcionalidadesPlanoOperacao.setPlanos(plano);
        for (Funcionalidades funcionalidade : mapFuncionalidade.values()) {
//            if (getRandomBoolean()) {
            funcionalidadesPlanoOperacao.setFuncionalidades(funcionalidade);
            for (Operacao o : mapOperacoes.values()) {
                funcionalidadesPlanoOperacao.setOperacao(o);
                dao.save(funcionalidadesPlanoOperacao);
            }
//            }
        }
    }

    public void criarTiposAuditoria() throws DaoException {
        for (Enum_AUDITORIA auditoria : Enum_AUDITORIA.values()) {
            dao.save(new TipoAuditoria(auditoria));
        }

    }

    public void criarFuncionalidadeGrupo() throws ServiceException, DaoException {

        dao.findAll(Funcionalidades.class).forEach(new ConsumerImplFunc());

        FuncionalidadesGrupoOperacao funcionalidadesGrupoOperacao = new FuncionalidadesGrupoOperacao();

        //grupo master possui todas as funcionalidades e todas as operacoes
        funcionalidadesGrupoOperacao.setGrupo(mapGrupos.get(CONSTANTES.GRUPO_MASTER));
        for (Funcionalidades funcionalidade : mapFuncionalidade.values()) {
            funcionalidadesGrupoOperacao.setFuncionalidades(funcionalidade);
            for (Operacao o : mapOperacoes.values()) {
                funcionalidadesGrupoOperacao.setOperacao(o);
                dao.save(funcionalidadesGrupoOperacao);
            }
        }

        //grupo admin possui todas as funcionalidades
        funcionalidadesGrupoOperacao = new FuncionalidadesGrupoOperacao();
        funcionalidadesGrupoOperacao.setGrupo(mapGrupos.get(CONSTANTES.GRUPO_ADMIN));

        for (Funcionalidades funcionalidade : mapFuncionalidade.values()) {
            funcionalidadesGrupoOperacao.setFuncionalidades(funcionalidade);
            for (Operacao o : mapOperacoes.values()) {
                funcionalidadesGrupoOperacao.setOperacao(o);
                dao.save(funcionalidadesGrupoOperacao);
            }
        }

    }

    private void criarTipDoc() throws ServiceException, DaoException {
        dao.save(new TipoDocumento(Enum_DOCUMENTO.CPF));
        dao.save(new TipoDocumento(Enum_DOCUMENTO.CNPJ));
    }

    private void criarEmpresa() throws DaoException {
        Empresa empresa;
        String docs[] = {"64872396000140", "13037022825", "07901567000188", "86267252000105", "12358110000109"};

        for (Integer i = 0; i < 5; i++) {
            empresa = new Empresa();
            empresa.setDocumento(docs[i]);
            empresa.setTipoDocumento((TipoDocumento) dao.find(TipoDocumento.class, docs[i].length() < 12 ? Enum_DOCUMENTO.CPF.ordinal() : Enum_DOCUMENTO.CNPJ.ordinal()));
            empresa.setRazaoSocial("Razão Social " + i.toString());
            empresa.setAtivo(getRandomBoolean());
            empresa.setBairro("Bairro " + i.toString());
            empresa.setCep("000000000" + i.toString());
            empresa.setCidade("Curitiba");
            empresa.setEndereco("Avenida");
            empresa.setFone("3333333333");
            empresa.setIdEmpresa(i + 1);
            dao.save(empresa);
        }
    }

    private void criarDepartamento() throws DaoException {
        Empresa empresa = (Empresa) dao.find(Empresa.class, 1);
        dao.save(new Departamento(1, "Engenharia", empresa));
        dao.save(new Departamento(2, "Administração", empresa));
        dao.save(new Departamento(3, "Recursos Humanos", empresa));

        empresa = (Empresa) dao.find(Empresa.class, 3);
        dao.save(new Departamento(4, "Engenharia", empresa));
        dao.save(new Departamento(5, "Administração", empresa));
        dao.save(new Departamento(6, "Recursos Humanos", empresa));

    }

    private void criarModelosRep() throws DaoException {
        dao.save(new ModeloRep(1, "00024", "INNER REP BIO 2i"));
        dao.save(new ModeloRep(2, "00025", "INNER REP BARRAS 2i"));
        dao.save(new ModeloRep(3, "00026", "INNER REP BARRAS"));
        dao.save(new ModeloRep(4, "00054", "INNER REP BIO"));
        dao.save(new ModeloRep(5, "00061", "INNER REP PROX"));
        dao.save(new ModeloRep(6, "00062", "INNER REP PROX 2i"));
        dao.save(new ModeloRep(7, "00063", "INNER REP BIO BARRAS 2i"));
        dao.save(new ModeloRep(8, "00064", "INNER REP BIO PROX 2i"));
        dao.save(new ModeloRep(9, "00364", "INNER REP PLUS"));
        dao.save(new ModeloRep(10, "00365", "INNER REP PLUS BIO PROX"));
        dao.save(new ModeloRep(11, "00366", "INNER REP PLUS BIO BARRAS"));
        dao.save(new ModeloRep(12, "00000", "OUTROS"));
    }

    private void criarTipoEquipamento() throws DaoException {
        dao.save(new TipoEquipamento(1, CONSTANTES.Enum_TIPO_EQUIPAMENTO.REP.getDescricao()));
        dao.save(new TipoEquipamento(2, CONSTANTES.Enum_TIPO_EQUIPAMENTO.INNER.getDescricao()));
        dao.save(new TipoEquipamento(3, CONSTANTES.Enum_TIPO_EQUIPAMENTO.OUTRO.getDescricao()));
    }

//    private void criarToleranciaOcorrencias() throws DaoException {
//        dao.save(new ToleranciaOcorrencia(1, CONSTANTES.TOLERANCIA_OCORENCIA.ENTRADA_ANTECIPADA.getDescricao()));
//        dao.save(new ToleranciaOcorrencia(2, CONSTANTES.TOLERANCIA_OCORENCIA.SAIDA_ANTECIDA.getDescricao()));
//        dao.save(new ToleranciaOcorrencia(3, CONSTANTES.TOLERANCIA_OCORENCIA.ENTRADA_ATRASADA.getDescricao()));
//        dao.save(new ToleranciaOcorrencia(4, CONSTANTES.TOLERANCIA_OCORENCIA.SAIDA_APOS_HORARIO.getDescricao()));
//    }
    private void criarDsrDefault() {
        try {
            if (dao.findAll(Dsr.class).isEmpty()) {
                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(0);
                dao.save(new Dsr(1, c.getTime(), false, false, false, false));
            }
        } catch (DaoException ex) {
            Logger.getLogger(DataBaseInitializer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void criarTiposDia() throws DaoException {
        for (CONSTANTES.Enum_TIPODIA tipodia : CONSTANTES.Enum_TIPODIA.values()) {
            if (!tipodia.equals(CONSTANTES.Enum_TIPODIA.DEFAULT)) {
                dao.save(new TipoDia(tipodia.ordinal(), tipodia.getDescricao()));
            }
        }
    }

    private void criarSemana() throws DaoException {
        dao.save(new Semana(1, "Domingo"));
        dao.save(new Semana(2, "Segunda"));
        dao.save(new Semana(3, "terça"));
        dao.save(new Semana(4, "Quarta"));
        dao.save(new Semana(5, "Quinta"));
        dao.save(new Semana(6, "Sexta"));
        dao.save(new Semana(7, "Sabado"));
    }

    private void criarFeriadosDefault() throws DaoException {
        dao.save(new Feriado(1, "Ano Novo"));
        dao.save(new Feriado(2, "Carnaval"));
        dao.save(new Feriado(3, "Sexta-feira da Paixão"));
        dao.save(new Feriado(4, "Páscoa"));
        dao.save(new Feriado(5, "Tiradentes"));
        dao.save(new Feriado(6, "Dia do Trabalho"));
        dao.save(new Feriado(7, "Corpus Christi"));
        dao.save(new Feriado(8, "Independência do Brasil"));
        dao.save(new Feriado(9, "Nossa Senhora Aparecida"));
        dao.save(new Feriado(10, "Finados"));
        dao.save(new Feriado(11, "Natal"));
    }

    private void criarTiposMotivos() throws DaoException {
        for (Enum_TIPO_MOTIVO tipomotivo : Enum_TIPO_MOTIVO.values()) {
            if (!tipomotivo.equals(Enum_TIPO_MOTIVO.DEFAULT)) {
                dao.save(new TipoMotivo(tipomotivo.ordinal(), tipomotivo.getDescricao()));
            }
        };
    }

    private void criarJornadas() throws DaoException {
        Jornada jornada = new Jornada();
        jornada.setDescricao("Jornada 01 ");
//        jornada.setJornadaVariavel(Boolean.FALSE);

        dao.save(jornada);

        jornada = new Jornada();
        jornada.setDescricao("Jornada 02 ");
//        jornada.setJornadaVariavel(Boolean.TRUE);

        dao.save(jornada);

    }

    private void criarHorarios() throws DaoException {

        TipoDia tipoDiaNormal = (TipoDia) dao.find(TipoDia.class, CONSTANTES.Enum_TIPODIA.NORMAL.ordinal());
        TipoDia tipoDiaFeriado = (TipoDia) dao.find(TipoDia.class, CONSTANTES.Enum_TIPODIA.FERIADO.ordinal());
        TipoDia tipoDiaFolga = (TipoDia) dao.find(TipoDia.class, CONSTANTES.Enum_TIPODIA.FOLGA.ordinal());
        TipoDia tipoDiaNormalSabado = (TipoDia) dao.find(TipoDia.class, CONSTANTES.Enum_TIPODIA.NORMAL_SABADO.ordinal());

        dao.save(new Horario(1, "8h as 18:00h", Boolean.FALSE, tipoDiaNormal));
        dao.save(new Horario(2, "21h as 03:00h", Boolean.FALSE, tipoDiaFeriado));
        dao.save(new Horario(3, "9h as 14:00h", Boolean.FALSE, tipoDiaFolga));
        dao.save(new Horario(4, "6h as 13:00h", Boolean.FALSE, tipoDiaNormalSabado));

        List<Horario> horarios = dao.findAll(Horario.class);

        Calendar calendarEntrada = Calendar.getInstance();
        Calendar calendarSaida = Calendar.getInstance();

        calendarEntrada.add(Calendar.HOUR_OF_DAY, 8);
        calendarSaida.add(Calendar.HOUR_OF_DAY, 18);

        HorarioMarcacao horarioMarcacao = new HorarioMarcacao(1, horarios.get(0), calendarEntrada.getTime(), calendarSaida.getTime(), 1);
        dao.save(horarioMarcacao);

        horarioMarcacao = new HorarioMarcacao(2, horarios.get(0), calendarEntrada.getTime(), calendarSaida.getTime(), 2);
        dao.save(horarioMarcacao);

        horarioMarcacao = new HorarioMarcacao(3, horarios.get(0), calendarEntrada.getTime(), calendarSaida.getTime(), 3);
        dao.save(horarioMarcacao);

        horarioMarcacao = new HorarioMarcacao(4, horarios.get(1), calendarEntrada.getTime(), calendarSaida.getTime(), 1);
        dao.save(horarioMarcacao);

        horarioMarcacao = new HorarioMarcacao(5, horarios.get(1), calendarEntrada.getTime(), calendarSaida.getTime(), 2);
        dao.save(horarioMarcacao);

        horarioMarcacao = new HorarioMarcacao(6, horarios.get(1), calendarEntrada.getTime(), calendarSaida.getTime(), 3);
        dao.save(horarioMarcacao);

    }

    private void criarJornadaHorario() throws DaoException {

        List<Jornada> jornadaList = dao.findAll(Jornada.class);
        List<Horario> horarioList = dao.findAll(Horario.class);

        dao.save(new JornadaHorario(1, horarioList.get(0), jornadaList.get(0)));
        dao.save(new JornadaHorario(2, horarioList.get(0), jornadaList.get(0)));
        dao.save(new JornadaHorario(3, horarioList.get(0), jornadaList.get(0)));
        dao.save(new JornadaHorario(4, horarioList.get(0), jornadaList.get(0)));
        dao.save(new JornadaHorario(5, horarioList.get(0), jornadaList.get(0)));
        dao.save(new JornadaHorario(6, horarioList.get(0), jornadaList.get(0)));
        dao.save(new JornadaHorario(7, horarioList.get(0), jornadaList.get(0)));
        dao.save(new JornadaHorario(8, horarioList.get(0), jornadaList.get(1)));
        dao.save(new JornadaHorario(9, horarioList.get(0), jornadaList.get(1)));
        dao.save(new JornadaHorario(10, horarioList.get(0), jornadaList.get(1)));
    }

    private void criarTiposFechamentos() throws DaoException {
//        dao.save(new TipoFechamento(1, "Acerto"));
//        dao.save(new TipoFechamento(2, "Edição de Saldo"));
//        dao.save(new TipoFechamento(3, "Subtotal"));
    }

    private class ConsumerImplGrupo implements Consumer<Grupo> {

        @Override
        public void accept(Grupo g) {
            mapGrupos.put(g.getDescricao(), g);
        }
    }

    private class ConsumerImplPermissoes implements Consumer<Permissoes> {

        @Override
        public void accept(Permissoes t) {
            mapPermissoes.put(Enum_PERMISSAO.values()[t.getIdPermissoes()], t);
        }
    }

    private class ConsumerImplFunc implements Consumer<Funcionalidades> {

        @Override
        public void accept(Funcionalidades t) {
            for (Enum_FUNCIONALIDADE funcionalidade : Enum_FUNCIONALIDADE.values()) {
                if (funcionalidade.ordinal() == t.getId()) {
                    mapFuncionalidade.put(funcionalidade, t);
                }
            }
        }
    }

    private class ConsumerImplOper implements Consumer<Operacao> {

        @Override
        public void accept(Operacao o) {
            mapOperacoes.put(Enum_OPERACAO.values()[o.getIdOperacao() - 1], o);
        }
    }

    private static Random rnd = new Random();

    public static boolean getRandomBoolean() {
        return rnd.nextBoolean();
    }

    public void verificarDados() throws ServiceException, DaoException {
        List<Permissoes> p = dao.findAll(Permissoes.class);
        Long totalPer = dao.count(Permissoes.class);
        Logger.getLogger("Permiss�o").log(Level.INFO, "Permissoes {0}", p.toString());
        Logger.getLogger("Total_Permissoes").log(Level.INFO, "Total Permissoes : {0}", totalPer);
        Long totalMod = dao.count(Modulos.class);
        Logger.getLogger("Total_Modulos").log(Level.INFO, "Total : {0} ", totalMod);
        Operacao op = (Operacao) dao.find(Operacao.class, 1);
        Operacao op2 = (Operacao) dao.find(Operacao.class, 2);
        Long totalOperacao = dao.count(Operacao.class);
        Logger.getLogger("Total_Operacao").log(Level.INFO, "Total_Operacao :{0} ", totalOperacao);
        Grupo g = (Grupo) dao.find(Grupo.class, 1);
        Grupo g2 = (Grupo) dao.find(Grupo.class, 2);
        Long totalGrp = dao.count(Grupo.class);
        Logger.getLogger("Total_Grupo").log(Level.INFO, "Total_Grupo : {0} ", totalGrp);
        Funcionalidades f = (Funcionalidades) dao.find(Funcionalidades.class, 1);
        Funcionalidades f1 = (Funcionalidades) dao.find(Funcionalidades.class, 50);
        Funcionalidades f2 = (Funcionalidades) dao.find(Funcionalidades.class, 80);
        List<Funcionalidades> funcoeses = dao.findAll(Funcionalidades.class);
        List<Funcionalidades> funcoeses2 = dao.find(3, 10, Funcionalidades.class);
        Long totalFunc = dao.count(Funcionalidades.class);
        Logger.getLogger("Total_Funcoes").log(Level.INFO, "Total_Funcoes : {0}", totalFunc);
        List<Operador> operadorList = dao.findAll(Operador.class);
        List<Operador> operadorList2 = dao.find(2, 1, Operador.class);
        Long totalOper = dao.count(Operador.class);
        Logger.getLogger("Total_Operador").log(Level.INFO, "Total_Operador : {0}", totalOper);
        List<HistoricoSenhas> historicoSenhases = dao.findAll(HistoricoSenhas.class);
    }

}
