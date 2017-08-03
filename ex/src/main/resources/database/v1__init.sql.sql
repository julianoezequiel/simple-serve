CREATE TABLE TipoAuditoria (
  idTipoAuditoria INTEGER  NOT NULL   IDENTITY ,
  Descricao VARCHAR(45)      ,
PRIMARY KEY(idTipoAuditoria));
GO




CREATE TABLE TipoDia (
  IdTipodia INTEGER  NOT NULL   IDENTITY ,
  Descricao VARCHAR(45)      ,
PRIMARY KEY(IdTipodia));
GO




CREATE TABLE StatusMarcacao (
  idStatusMarcacao INTEGER  NOT NULL   IDENTITY ,
  Descricao VARCHAR(45)  NOT NULL    ,
PRIMARY KEY(idStatusMarcacao));
GO




CREATE TABLE HorasExtrasAcumulo (
  IdHorasExtrasAcumulo INTEGER  NOT NULL   IDENTITY ,
  Descricao VARCHAR(20)      ,
PRIMARY KEY(IdHorasExtrasAcumulo));
GO




CREATE TABLE Grupo (
  IdGrupo INTEGER  NOT NULL   IDENTITY ,
  Descricao VARCHAR(30)      ,
PRIMARY KEY(IdGrupo));
GO




CREATE TABLE TipoDSR (
  idTipoDSR INTEGER  NOT NULL  ,
  Descricao VARCHAR(45)      ,
PRIMARY KEY(idTipoDSR));
GO




CREATE TABLE Exportacao (
  IdExportacao INTEGER  NOT NULL   IDENTITY ,
  Formato VARCHAR(255)  NOT NULL  ,
  FormatoHoraSexagesimal BIT  NOT NULL  ,
  Descricao VARCHAR(50)    ,
  FormatoDataInicioFimExportacao VARCHAR(20)    ,
  FormatoDataExportacao VARCHAR(20)    ,
  SabadoDiaUtil BIT   DEFAULT ((0)) ,
  DomingoDiaUtil BIT   DEFAULT ((0)) ,
  DelimitadorFaltasMinutos VARCHAR(1)    ,
  DesabilitaZerosDireita BIT    ,
  DelimitadorHora VARCHAR(1)    ,
  Cabecalho VARCHAR(50)    ,
  SomaContadorCabecalho BIT   DEFAULT ((0))   ,
PRIMARY KEY(IdExportacao));
GO




CREATE TABLE ExportacaoEventoFormato (
  IdFormato INTEGER  NOT NULL   IDENTITY ,
  Descricao VARCHAR(20)      ,
PRIMARY KEY(IdFormato));
GO




CREATE TABLE TipoDocumento (
  idTipoDocumento INTEGER  NOT NULL   IDENTITY ,
  Descricao VARCHAR(20)  NOT NULL    ,
PRIMARY KEY(idTipoDocumento));
GO




CREATE TABLE Feriado (
  idFeriado INTEGER  NOT NULL  ,
  Descricao VARCHAR(30)  NOT NULL    ,
PRIMARY KEY(idFeriado));
GO




CREATE TABLE Operacao (
  IdOperacao INTEGER  NOT NULL   IDENTITY ,
  Descricao VARCHAR(45)      ,
PRIMARY KEY(IdOperacao));
GO




CREATE TABLE Modulos (
  idModulo INTEGER  NOT NULL   IDENTITY ,
  Descricao VARCHAR(45)      ,
PRIMARY KEY(idModulo));
GO




CREATE TABLE Parametros (
  ParamNome VARCHAR(20)  NOT NULL  ,
  ParamValor VARCHAR(255)  NOT NULL    ,
PRIMARY KEY(ParamNome));
GO




CREATE TABLE Permissoes (
  IdPermissoes INTEGER  NOT NULL   IDENTITY ,
  Permissoes VARCHAR(20)  NOT NULL  ,
  Descricao VARCHAR(45)      ,
PRIMARY KEY(IdPermissoes));
GO




CREATE TABLE PercentuaisAcrescimo (
  idPercentuaisAcrescimo INTEGER  NOT NULL   IDENTITY ,
  Descricao VARCHAR(50)      ,
PRIMARY KEY(idPercentuaisAcrescimo));
GO




CREATE TABLE LayoutImportacaoExportacaoFuncionarios (
  IdLayoutImportacaoExportacaoFuncionarios INTEGER  NOT NULL   IDENTITY ,
  Descricao VARCHAR(45)    ,
  Formato VARCHAR(255)      ,
PRIMARY KEY(IdLayoutImportacaoExportacaoFuncionarios));
GO




CREATE TABLE Semana (
  IdSemana INTEGER  NOT NULL   IDENTITY ,
  Descricao VARCHAR(20)      ,
PRIMARY KEY(IdSemana));
GO




CREATE TABLE Seguranca (
  IdSeguranca INTEGER  NOT NULL   IDENTITY ,
  TamanhoMinimoSenha INTEGER    ,
  QtdeBloqueioTentativas INTEGER    ,
  QtdeHorasDesbloqueioUsuario INTEGER    ,
  QtdeDiasTrocaSenha INTEGER    ,
  QtdeNaoRepetirSenhas INTEGER    ,
  ComplexidadeLetrasNumeros BIT      ,
PRIMARY KEY(IdSeguranca));
GO




CREATE TABLE ModeloRep (
  IdModeloRep INTEGER  NOT NULL   IDENTITY ,
  Modelo VARCHAR(200)  NOT NULL  ,
  NumeroFabricante VARCHAR(10)  NOT NULL    ,
PRIMARY KEY(IdModeloRep));
GO




CREATE TABLE Mensagem (
  IdMensagem INTEGER  NOT NULL   IDENTITY ,
  Assunto VARCHAR(45)    ,
  Conteudo VARCHAR(255)    ,
  DataHoraEnvio DATETIME    ,
  Remetente VARCHAR(45)      ,
PRIMARY KEY(IdMensagem));
GO




CREATE TABLE TipoFechamento (
  idTipoFechamento INTEGER  NOT NULL   IDENTITY ,
  Descricao VARCHAR(45)      ,
PRIMARY KEY(idTipoFechamento));
GO




CREATE TABLE TipoEquipamento (
  idTipoEquipamento INTEGER  NOT NULL   IDENTITY ,
  Descricao VARCHAR(20)      ,
PRIMARY KEY(idTipoEquipamento));
GO




CREATE TABLE TipoHora (
  idTipoHora INTEGER  NOT NULL   IDENTITY ,
  Descricao VARCHAR(45)      ,
PRIMARY KEY(idTipoHora));
GO




CREATE TABLE Cargo (
  IdCargo INTEGER  NOT NULL   IDENTITY ,
  Descricao VARCHAR(30)    ,
  Ativo BIT      ,
PRIMARY KEY(IdCargo));
GO




CREATE TABLE Calendario (
  IdCalendario INTEGER  NOT NULL   IDENTITY ,
  Descricao VARCHAR(30)      ,
PRIMARY KEY(IdCalendario));
GO




CREATE TABLE Coletivo (
  IdColetivo INTEGER  NOT NULL   IDENTITY ,
  Descricao VARCHAR(45)      ,
PRIMARY KEY(IdColetivo));
GO




CREATE TABLE EncaixeMarcacao (
  idEncaixeMarcacao INTEGER  NOT NULL   IDENTITY ,
  Descricao VARCHAR(45)      ,
PRIMARY KEY(idEncaixeMarcacao));
GO




CREATE TABLE Evento  (
  IdEvento  INTEGER  NOT NULL   IDENTITY ,
  Descricao VARCHAR(20)      ,
PRIMARY KEY(IdEvento ));
GO




CREATE TABLE ToleranciaOcorrencia (
  IdToleranciaOcorrencia INTEGER  NOT NULL   IDENTITY ,
  Descricao VARCHAR(200)  NOT NULL  ,
  TolAntes INTEGER   DEFAULT ((0))   ,
PRIMARY KEY(IdToleranciaOcorrencia));
GO




CREATE TABLE TipoMotivo (
  IdTipoMotivo INTEGER  NOT NULL   IDENTITY ,
  Descricao VARCHAR(30)      ,
PRIMARY KEY(IdTipoMotivo));
GO




CREATE TABLE Motivo (
  IdMotivo INTEGER  NOT NULL   IDENTITY ,
  IdTipoMotivo INTEGER  NOT NULL  ,
  Descricao VARCHAR(30)    ,
  ConsideraExportacao BIT  NOT NULL    ,
PRIMARY KEY(IdMotivo),
  FOREIGN KEY(IdTipoMotivo)
    REFERENCES TipoMotivo(IdTipoMotivo));
GO


CREATE INDEX IFK_Rel_111 ON Motivo (IdTipoMotivo);
GO


CREATE TABLE Horario (
  IdHorario INTEGER  NOT NULL   IDENTITY ,
  IdTipodia INTEGER  NOT NULL  ,
  Descricao VARCHAR(50)    ,
  PreAssinalada BIT  NOT NULL    ,
PRIMARY KEY(IdHorario),
  FOREIGN KEY(IdTipodia)
    REFERENCES TipoDia(IdTipodia));
GO


CREATE INDEX IFK_Rel_98 ON Horario (IdTipodia);
GO


CREATE TABLE HorarioMarcacao (
  IdSequencia INTEGER  NOT NULL   IDENTITY ,
  IdHorario INTEGER  NOT NULL  ,
  HorarioEntrada TIME  NOT NULL  ,
  HorarioSaida TIME  NOT NULL    ,
PRIMARY KEY(IdSequencia, IdHorario),
  FOREIGN KEY(IdHorario)
    REFERENCES Horario(IdHorario));
GO


CREATE INDEX IFK_Rel_102 ON HorarioMarcacao (IdHorario);
GO


CREATE TABLE Operador (
  IdOperador INTEGER  NOT NULL   IDENTITY ,
  IdGrupo INTEGER  NOT NULL  ,
  Usuario VARCHAR(40)    ,
  Senha VARCHAR(100)  NOT NULL  ,
  Email VARCHAR(60)    ,
  UltimoAcesso DATETIME    ,
  TentativasLogin INTEGER    ,
  SenhaBloqueada BIT    ,
  DataHoraBloqueioSenha DATETIME    ,
  TrocaSenhaProximoAcesso BIT    ,
  Foto VARCHAR(255)    ,
  VisualizarMensagens BIT    ,
  VisualizarAlertas BIT    ,
  CodigoConfirmacaoNovaSenha VARCHAR(45)    ,
  UltimoToken VARCHAR(100)    ,
  UltimoIp VARCHAR(20)    ,
  Ativo BIT      ,
PRIMARY KEY(IdOperador),
  FOREIGN KEY(IdGrupo)
    REFERENCES Grupo(IdGrupo));
GO


CREATE INDEX IFK_Rel_100 ON Operador (IdGrupo);
GO


CREATE TABLE Funcionario (
  IdFuncionario INTEGER  NOT NULL   IDENTITY ,
  IdOperador INTEGER    ,
  Pis VARCHAR(14)  NOT NULL  ,
  Nome VARCHAR(40)  NOT NULL  ,
  Ctps VARCHAR(20)    ,
  Matricula VARCHAR(15)    ,
  IdentificacaoExportacao VARCHAR(15)    ,
  DataAdmissao DATE    ,
  DataDemissao DATE    ,
  Foto VARCHAR(255)    ,
  Ativo BIT  NOT NULL    ,
PRIMARY KEY(IdFuncionario),
  FOREIGN KEY(IdOperador)
    REFERENCES Operador(IdOperador));
GO


CREATE INDEX IFK_Rel_87 ON Funcionario (IdOperador);
GO


CREATE TABLE Empresa (
  IdEmpresa INTEGER  NOT NULL   IDENTITY ,
  idTipoDocumento INTEGER  NOT NULL  ,
  RazaoSocial VARCHAR(50)  NOT NULL  ,
  NomeFantasia VARCHAR(50)    ,
  Documento VARCHAR(18)  NOT NULL  ,
  Endereco VARCHAR(50)    ,
  Bairro VARCHAR(20)    ,
  Cep VARCHAR(10)    ,
  Cidade VARCHAR(30)    ,
  Uf VARCHAR(2)    ,
  Fone VARCHAR(20)    ,
  Fax VARCHAR(20)    ,
  Observacao VARCHAR(50)    ,
  Ativo BIT      ,
PRIMARY KEY(IdEmpresa),
  FOREIGN KEY(idTipoDocumento)
    REFERENCES TipoDocumento(idTipoDocumento));
GO


CREATE INDEX IFK_Rel_95 ON Empresa (idTipoDocumento);
GO


CREATE TABLE Funcionalidades (
  idFuncionalidade INTEGER  NOT NULL   IDENTITY ,
  idModulo INTEGER  NOT NULL  ,
  idPai INTEGER    ,
  Descricao VARCHAR(45)      ,
PRIMARY KEY(idFuncionalidade),
  FOREIGN KEY(idModulo)
    REFERENCES Modulos(idModulo));
GO


CREATE INDEX IFK_Rel_12 ON Funcionalidades (idModulo);
GO


CREATE TABLE BancoHoras (
  IdBancoHoras INTEGER  NOT NULL   IDENTITY ,
  idPercentuaisAcrescimo INTEGER    ,
  Descricao VARCHAR(30)  NOT NULL  ,
  TipoLimiteDiario BIT  NOT NULL  ,
  GatilhoPositivo DATETIME  NOT NULL  ,
  GatilhoNegativo DATETIME  NOT NULL  ,
  TrataFaltaDebito BIT    ,
  TrataAbonoDebito BIT  NOT NULL  ,
  TrataAusenciaDebito BIT  NOT NULL  ,
  NaoPagaAdicionalNoturnoBH BIT  NOT NULL  ,
  HabilitaDiaFechamento BIT  NOT NULL  ,
  DiaFechamentoExtra INTEGER  NOT NULL    ,
PRIMARY KEY(IdBancoHoras),
  FOREIGN KEY(idPercentuaisAcrescimo)
    REFERENCES PercentuaisAcrescimo(idPercentuaisAcrescimo));
GO


CREATE INDEX IFK_Rel_98 ON BancoHoras (idPercentuaisAcrescimo);
GO


CREATE TABLE Departamento (
  IdDepartamento INTEGER  NOT NULL   IDENTITY ,
  IdEmpresa INTEGER  NOT NULL  ,
  Descricao VARCHAR(50)    ,
  Ativo BIT      ,
PRIMARY KEY(IdDepartamento),
  FOREIGN KEY(IdEmpresa)
    REFERENCES Empresa(IdEmpresa));
GO


CREATE INDEX IFK_Rel_98 ON Departamento (IdEmpresa);
GO


CREATE TABLE BancoHorasLimite (
  IdBancoHoras INTEGER  NOT NULL  ,
  idTipodia INTEGER  NOT NULL  ,
  Limite DATETIME      ,
PRIMARY KEY(IdBancoHoras, idTipodia),
  FOREIGN KEY(IdBancoHoras)
    REFERENCES BancoHoras(IdBancoHoras),
  FOREIGN KEY(idTipodia)
    REFERENCES TipoDia(idTipodia));
GO


CREATE INDEX IFK_Rel_65 ON BancoHorasLimite (IdBancoHoras);
GO
CREATE INDEX IFK_Rel_92 ON BancoHorasLimite (idTipodia);
GO


CREATE TABLE CalendarioFeriado (
  IdCalendario INTEGER  NOT NULL  ,
  idFeriado INTEGER  NOT NULL  ,
  DataInicio DATE    ,
  DataTermino DATE      ,
PRIMARY KEY(IdCalendario, idFeriado),
  FOREIGN KEY(IdCalendario)
    REFERENCES Calendario(IdCalendario),
  FOREIGN KEY(idFeriado)
    REFERENCES Feriado(idFeriado));
GO


CREATE INDEX IFK_Rel_97 ON CalendarioFeriado (IdCalendario);
GO
CREATE INDEX IFK_Rel_98 ON CalendarioFeriado (idFeriado);
GO


CREATE TABLE OperadorMensagem (
  IdOperador INTEGER  NOT NULL  ,
  IdMensagem INTEGER  NOT NULL  ,
  Lida BIT      ,
PRIMARY KEY(IdOperador),
  FOREIGN KEY(IdOperador)
    REFERENCES Operador(IdOperador),
  FOREIGN KEY(IdMensagem)
    REFERENCES Mensagem(IdMensagem));
GO


CREATE INDEX IFK_Rel_105 ON OperadorMensagem (IdOperador);
GO
CREATE INDEX IFK_Rel_103 ON OperadorMensagem (IdMensagem);
GO


CREATE TABLE FuncionalidadesOperacao (
  idFuncionalidade INTEGER  NOT NULL  ,
  IdOperacao INTEGER  NOT NULL    ,
PRIMARY KEY(idFuncionalidade, IdOperacao),
  FOREIGN KEY(idFuncionalidade)
    REFERENCES Funcionalidades(idFuncionalidade),
  FOREIGN KEY(IdOperacao)
    REFERENCES Operacao(IdOperacao));
GO


CREATE INDEX IFK_Rel_94 ON FuncionalidadesOperacao (idFuncionalidade);
GO
CREATE INDEX IFK_Rel_95 ON FuncionalidadesOperacao (IdOperacao);
GO


CREATE TABLE DepartamentoGrupo (
  IdDepartamento INTEGER  NOT NULL  ,
  IdGrupo INTEGER  NOT NULL    ,
PRIMARY KEY(IdDepartamento, IdGrupo),
  FOREIGN KEY(IdDepartamento)
    REFERENCES Departamento(IdDepartamento),
  FOREIGN KEY(IdGrupo)
    REFERENCES Grupo(IdGrupo));
GO


CREATE INDEX IFK_Rel_100 ON DepartamentoGrupo (IdDepartamento);
GO
CREATE INDEX IFK_Rel_101 ON DepartamentoGrupo (IdGrupo);
GO


CREATE TABLE FuncionarioCargo (
  IdFuncionario INTEGER  NOT NULL  ,
  IdCargo INTEGER  NOT NULL  ,
  DataCargo DATE  NOT NULL  ,
  Descricao VARCHAR(30)  NOT NULL    ,
PRIMARY KEY(IdFuncionario, IdCargo, DataCargo),
  FOREIGN KEY(IdFuncionario)
    REFERENCES Funcionario(IdFuncionario),
  FOREIGN KEY(IdCargo)
    REFERENCES Cargo(IdCargo));
GO


CREATE INDEX IFK_Rel_146 ON FuncionarioCargo (IdFuncionario);
GO
CREATE INDEX IFK_Rel_147 ON FuncionarioCargo (IdCargo);
GO


CREATE TABLE FechamentoHoras (
  IdFechamentoHoras INTEGER  NOT NULL   IDENTITY ,
  IdEmpresa INTEGER  NOT NULL  ,
  IdFuncionario INTEGER  NOT NULL  ,
  DataMarcacao DATE  NOT NULL  ,
  DataInicio DATE  NOT NULL  ,
  DataTermino DATE  NOT NULL  ,
  PossuiJornada BIT  NOT NULL  ,
  PossuiJornadaCadastrada BIT  NOT NULL  ,
  PossuiJornadaLivre BIT  NOT NULL  ,
  PossuiFeriado BIT  NOT NULL  ,
  PossuiAbono BIT  NOT NULL  ,
  PossuiAfastamento BIT  NOT NULL  ,
  PossuiInconsistencia BIT  NOT NULL  ,
  HorasCompensadas TIME  NOT NULL  ,
  StrObs VARCHAR(200)  NOT NULL  ,
  StrLegenda VARCHAR(100)  NOT NULL  ,
  SomenteJustificativa BIT  NOT NULL  ,
  DescricaoJustificativa VARCHAR(200)  NOT NULL  ,
  ObservacaoFrequencia VARCHAR(100)  NOT NULL  ,
  ObservacaoOcorrencia VARCHAR(200)  NOT NULL  ,
  Status VARCHAR(2)  NOT NULL    ,
PRIMARY KEY(IdFechamentoHoras, IdEmpresa, IdFuncionario),
  FOREIGN KEY(IdEmpresa)
    REFERENCES Empresa(IdEmpresa),
  FOREIGN KEY(IdFuncionario)
    REFERENCES Funcionario(IdFuncionario));
GO


CREATE INDEX IFK_Rel_107 ON FechamentoHoras (IdEmpresa);
GO
CREATE INDEX IFK_Rel_108 ON FechamentoHoras (IdFuncionario);
GO


CREATE TABLE EventoMotivo (
  idEvento  INTEGER  NOT NULL  ,
  IdMotivo INTEGER  NOT NULL    ,
PRIMARY KEY(idEvento , IdMotivo),
  FOREIGN KEY(idEvento )
    REFERENCES Evento (idEvento ),
  FOREIGN KEY(IdMotivo)
    REFERENCES Motivo(IdMotivo));
GO


CREATE INDEX IFK_Rel_114 ON EventoMotivo (idEvento );
GO
CREATE INDEX IFK_Rel_115 ON EventoMotivo (IdMotivo);
GO


CREATE TABLE EmpresaExportacao (
  IdEmpresa INTEGER  NOT NULL  ,
  IdExportacao INTEGER  NOT NULL  ,
  PathAFDT VARCHAR(255)    ,
  PathACJEF VARCHAR(255)    ,
  PathArquivo VARCHAR(255)    ,
  PathExportacao VARCHAR(255)    ,
  TipoArquivo INTEGER    ,
  FormatoArquivoTexto BIT    ,
  NumeroDigitos INTEGER    ,
  MascaraMatricula VARCHAR(16)      ,
PRIMARY KEY(IdEmpresa, IdExportacao),
  FOREIGN KEY(IdEmpresa)
    REFERENCES Empresa(IdEmpresa),
  FOREIGN KEY(IdExportacao)
    REFERENCES Exportacao(IdExportacao));
GO


CREATE INDEX IFK_Rel_64 ON EmpresaExportacao (IdEmpresa);
GO
CREATE INDEX IFK_Rel_90 ON EmpresaExportacao (IdExportacao);
GO


CREATE TABLE GrupoPermissoes (
  IdPermissoes INTEGER  NOT NULL  ,
  IdGrupo INTEGER  NOT NULL    ,
PRIMARY KEY(IdPermissoes),
  FOREIGN KEY(IdPermissoes)
    REFERENCES Permissoes(IdPermissoes)
      ON UPDATE CASCADE,
  FOREIGN KEY(IdGrupo)
    REFERENCES Grupo(IdGrupo));
GO


CREATE INDEX IFK_Rel_87 ON GrupoPermissoes (IdPermissoes);
GO
CREATE INDEX IFK_Rel_90 ON GrupoPermissoes (IdGrupo);
GO


CREATE TABLE Hora (
  idHora INTEGER  NOT NULL   IDENTITY ,
  idTipoHora INTEGER  NOT NULL  ,
  IdFechamentoHoras INTEGER  NOT NULL  ,
  IdEmpresa INTEGER  NOT NULL  ,
  IdFuncionario INTEGER  NOT NULL  ,
  Diurna TIME    ,
  Noturna TIME    ,
  InvalidaDiurna TIME    ,
  InvalidaNoturna TIME    ,
  DiurnaSemAbono TIME    ,
  NoturnaSemAbono TIME      ,
PRIMARY KEY(idHora, idTipoHora, IdFechamentoHoras, IdEmpresa, IdFuncionario),
  FOREIGN KEY(idTipoHora)
    REFERENCES TipoHora(idTipoHora),
  FOREIGN KEY(IdFechamentoHoras, IdEmpresa, IdFuncionario)
    REFERENCES FechamentoHoras(IdFechamentoHoras, IdEmpresa, IdFuncionario));
GO


CREATE INDEX IFK_Rel_67 ON Hora (idTipoHora);
GO
CREATE INDEX IFK_Rel_129 ON Hora (IdFechamentoHoras, IdEmpresa, IdFuncionario);
GO


CREATE TABLE Cartao (
  IdFuncionario INTEGER  NOT NULL  ,
  idCartao VARCHAR(16)  NOT NULL  ,
  IdMotivo INTEGER    ,
  DataInicio DATETIME  NOT NULL    ,
PRIMARY KEY(IdFuncionario, idCartao),
  FOREIGN KEY(IdFuncionario)
    REFERENCES Funcionario(IdFuncionario),
  FOREIGN KEY(IdMotivo)
    REFERENCES Motivo(IdMotivo));
GO


CREATE INDEX IFK_Rel_102 ON Cartao (IdFuncionario);
GO
CREATE INDEX IFK_Rel_104 ON Cartao (IdMotivo);
GO


CREATE TABLE SequenciaPercentuais (
  idSequencia INTEGER  NOT NULL  ,
  idPercentuaisAcrescimo INTEGER  NOT NULL  ,
  IdTipodia INTEGER  NOT NULL  ,
  Acrescimo FLOAT    ,
  Horas DATETIME      ,
PRIMARY KEY(idSequencia, idPercentuaisAcrescimo, IdTipodia),
  FOREIGN KEY(idPercentuaisAcrescimo)
    REFERENCES PercentuaisAcrescimo(idPercentuaisAcrescimo),
  FOREIGN KEY(IdTipodia)
    REFERENCES TipoDia(IdTipodia));
GO


CREATE INDEX IFK_Rel_95 ON SequenciaPercentuais (idPercentuaisAcrescimo);
GO
CREATE INDEX IFK_Rel_96 ON SequenciaPercentuais (IdTipodia);
GO


CREATE TABLE FuncionarioDepartamento (
  IdFuncionario INTEGER  NOT NULL  ,
  IdDepartamento INTEGER  NOT NULL  ,
  DataDepartamento DATE  NOT NULL  ,
  Descricao VARCHAR(50)  NOT NULL    ,
PRIMARY KEY(IdFuncionario, IdDepartamento),
  FOREIGN KEY(IdFuncionario)
    REFERENCES Funcionario(IdFuncionario),
  FOREIGN KEY(IdDepartamento)
    REFERENCES Departamento(IdDepartamento));
GO


CREATE INDEX IFK_Rel_91 ON FuncionarioDepartamento (IdFuncionario);
GO
CREATE INDEX IFK_Rel_92 ON FuncionarioDepartamento (IdDepartamento);
GO


CREATE TABLE FuncionarioDSR (
  IdFuncionario INTEGER  NOT NULL  ,
  idTipoDSR INTEGER  NOT NULL  ,
  DataHora DATETIME  NOT NULL    ,
PRIMARY KEY(IdFuncionario, idTipoDSR),
  FOREIGN KEY(IdFuncionario)
    REFERENCES Funcionario(IdFuncionario),
  FOREIGN KEY(idTipoDSR)
    REFERENCES TipoDSR(idTipoDSR));
GO


CREATE INDEX IFK_Rel_98 ON FuncionarioDSR (IdFuncionario);
GO
CREATE INDEX IFK_Rel_101 ON FuncionarioDSR (idTipoDSR);
GO


CREATE TABLE FuncionarioEmpresa (
  IdFuncionario INTEGER  NOT NULL  ,
  IdEmpresa INTEGER  NOT NULL  ,
  DataEmpresa DATE  NOT NULL    ,
PRIMARY KEY(IdFuncionario, IdEmpresa),
  FOREIGN KEY(IdFuncionario)
    REFERENCES Funcionario(IdFuncionario),
  FOREIGN KEY(IdEmpresa)
    REFERENCES Empresa(IdEmpresa));
GO


CREATE INDEX IFK_Rel_93 ON FuncionarioEmpresa (IdFuncionario);
GO
CREATE INDEX IFK_Rel_94 ON FuncionarioEmpresa (IdEmpresa);
GO


CREATE TABLE ExportacaoEvento (
  IdGrupoEvento VARCHAR(10)  NOT NULL  ,
  IdExportacao INTEGER  NOT NULL  ,
  idEvento  INTEGER  NOT NULL  ,
  idFormato INTEGER  NOT NULL  ,
  AdicionalNoturno BIT      ,
PRIMARY KEY(IdGrupoEvento, IdExportacao, idEvento ),
  FOREIGN KEY(IdExportacao)
    REFERENCES Exportacao(IdExportacao),
  FOREIGN KEY(idEvento )
    REFERENCES Evento (idEvento ),
  FOREIGN KEY(idFormato)
    REFERENCES ExportacaoEventoFormato(idFormato));
GO


CREATE INDEX IFK_Rel_110 ON ExportacaoEvento (IdExportacao);
GO
CREATE INDEX IFK_Rel_112 ON ExportacaoEvento (idEvento );
GO
CREATE INDEX IFK_Rel_102 ON ExportacaoEvento (idFormato);
GO


CREATE TABLE FuncionalidadesGrupoOperacao (
  idFuncionalidade INTEGER  NOT NULL  ,
  IdGrupo INTEGER  NOT NULL  ,
  IdOperacao INTEGER  NOT NULL    ,
PRIMARY KEY(idFuncionalidade, IdGrupo, IdOperacao),
  FOREIGN KEY(idFuncionalidade)
    REFERENCES Funcionalidades(idFuncionalidade),
  FOREIGN KEY(IdGrupo)
    REFERENCES Grupo(IdGrupo),
  FOREIGN KEY(IdOperacao)
    REFERENCES Operacao(IdOperacao));
GO


CREATE INDEX IFK_Rel_18 ON FuncionalidadesGrupoOperacao (idFuncionalidade);
GO
CREATE INDEX IFK_Rel_93 ON FuncionalidadesGrupoOperacao (IdGrupo);
GO
CREATE INDEX IFK_Rel_99 ON FuncionalidadesGrupoOperacao (IdOperacao);
GO


CREATE TABLE Abono (
  IdAbono INTEGER  NOT NULL   IDENTITY ,
  idTipoHora INTEGER  NOT NULL  ,
  IdFuncionario INTEGER  NOT NULL  ,
  IdMotivo INTEGER  NOT NULL  ,
  SomenteJustificativa BIT   DEFAULT ((0)) ,
  Data DATE  NOT NULL  ,
  DescontaBanco INTEGER  NOT NULL DEFAULT ((0)) ,
  Horas TIME      ,
PRIMARY KEY(IdAbono),
  FOREIGN KEY(IdFuncionario)
    REFERENCES Funcionario(IdFuncionario),
  FOREIGN KEY(IdMotivo)
    REFERENCES Motivo(IdMotivo),
  FOREIGN KEY(idTipoHora)
    REFERENCES TipoHora(idTipoHora));
GO


CREATE INDEX IFK_Abonos_FK0 ON Abono (IdFuncionario);
GO
CREATE INDEX IFK_Rel_42 ON Abono (IdMotivo);
GO
CREATE INDEX IFK_Rel_110 ON Abono (idTipoHora);
GO


CREATE TABLE Afastamento (
  IdAfastamento INTEGER  NOT NULL   IDENTITY ,
  idColetivo INTEGER    ,
  IdFuncionario INTEGER  NOT NULL  ,
  IdMotivo INTEGER  NOT NULL  ,
  DataInicio DATE    ,
  DataFim DATE    ,
  Abonado BIT   DEFAULT ((0)) ,
  RespeitaLimitesHoraExtra BIT   DEFAULT ((0))   ,
PRIMARY KEY(IdAfastamento),
  FOREIGN KEY(IdFuncionario)
    REFERENCES Funcionario(IdFuncionario),
  FOREIGN KEY(IdMotivo)
    REFERENCES Motivo(IdMotivo),
  FOREIGN KEY(idColetivo)
    REFERENCES Coletivo(idColetivo));
GO


CREATE INDEX IFK_Afastamentos_FK3 ON Afastamento (IdFuncionario);
GO
CREATE INDEX IFK_Rel_44 ON Afastamento (IdMotivo);
GO
CREATE INDEX IFK_Rel_60 ON Afastamento (idColetivo);
GO


CREATE TABLE Compensacao (
  IdCompensacao INTEGER  NOT NULL   IDENTITY ,
  idColetivo INTEGER    ,
  IdFuncionario INTEGER  NOT NULL  ,
  IdMotivo INTEGER  NOT NULL  ,
  DataCompensada DATE    ,
  DataInicio DATE    ,
  DataFim DATE  NOT NULL  ,
  LimiteDiario TIME    ,
  ConsideraDiasSemJornada BIT   DEFAULT ((0)) ,
  MostraJustificativaComoObservacao BIT  NOT NULL    ,
PRIMARY KEY(IdCompensacao),
  FOREIGN KEY(IdFuncionario)
    REFERENCES Funcionario(IdFuncionario),
  FOREIGN KEY(IdMotivo)
    REFERENCES Motivo(IdMotivo),
  FOREIGN KEY(idColetivo)
    REFERENCES Coletivo(idColetivo));
GO


CREATE INDEX IFK_Compensacoes_FK13 ON Compensacao (IdFuncionario);
GO
CREATE INDEX IFK_Rel_45 ON Compensacao (IdMotivo);
GO
CREATE INDEX IFK_Rel_57 ON Compensacao (idColetivo);
GO


CREATE TABLE FuncionarioBancoHoras (
  IdFuncionario INTEGER  NOT NULL   IDENTITY ,
  IdBancoHoras INTEGER  NOT NULL  ,
  DataInicio DATE  NOT NULL  ,
  idColetivo INTEGER    ,
  DataFim DATE  NOT NULL    ,
PRIMARY KEY(IdFuncionario, IdBancoHoras, DataInicio),
  FOREIGN KEY(idColetivo)
    REFERENCES Coletivo(idColetivo),
  FOREIGN KEY(IdFuncionario)
    REFERENCES Funcionario(IdFuncionario),
  FOREIGN KEY(IdBancoHoras)
    REFERENCES BancoHoras(IdBancoHoras));
GO


CREATE INDEX IFK_Rel_56 ON FuncionarioBancoHoras (idColetivo);
GO
CREATE INDEX IFK_Rel_112 ON FuncionarioBancoHoras (IdFuncionario);
GO
CREATE INDEX IFK_Rel_113 ON FuncionarioBancoHoras (IdBancoHoras);
GO


CREATE TABLE ImportacaoExportacaoFuncionarios (
  IdImportacaoExportacaoFuncionarios INTEGER  NOT NULL   IDENTITY ,
  IdOperador INTEGER  NOT NULL  ,
  idFuncionalidade INTEGER  NOT NULL  ,
  IdLayoutImportacaoExportacaoFuncionarios INTEGER  NOT NULL  ,
  DataHora DATETIME      ,
PRIMARY KEY(IdImportacaoExportacaoFuncionarios),
  FOREIGN KEY(IdLayoutImportacaoExportacaoFuncionarios)
    REFERENCES LayoutImportacaoExportacaoFuncionarios(IdLayoutImportacaoExportacaoFuncionarios),
  FOREIGN KEY(idFuncionalidade)
    REFERENCES Funcionalidades(idFuncionalidade),
  FOREIGN KEY(IdOperador)
    REFERENCES Operador(IdOperador));
GO


CREATE INDEX IFK_Rel_92 ON ImportacaoExportacaoFuncionarios (IdLayoutImportacaoExportacaoFuncionarios);
GO
CREATE INDEX IFK_Rel_93 ON ImportacaoExportacaoFuncionarios (idFuncionalidade);
GO
CREATE INDEX IFK_Rel_94 ON ImportacaoExportacaoFuncionarios (IdOperador);
GO


CREATE TABLE Jornada (
  IdJornada INTEGER  NOT NULL   IDENTITY ,
  idPercentuaisAcrescimo INTEGER  NOT NULL  ,
  IdSemana INTEGER    ,
  IdHorasExtrasAcumulo INTEGER    ,
  Descricao VARCHAR(50)    ,
  JornadaVariavel BIT    ,
  CompensaAtrasos BIT    ,
  MarcacoesDiaSeguinteViradaDia BIT  NOT NULL DEFAULT ('True') ,
  DescontaHorasDSR DATETIME  NOT NULL  ,
  ToleranciaAusencia TIME    ,
  ToleranciaExtra TIME  NOT NULL  ,
  DiaFechamentoExtra INTEGER  NOT NULL DEFAULT ((0)) ,
  TrataDomingoDiaNormal BIT    ,
  TrataSabadoDiaNormal BIT    ,
  TrataComoDiaNormal BIT    ,
  TrataFeriadoComoDiaNormal BIT    ,
  PagaHorasFeriado BIT    ,
  LimiteCorteEntrada TIME    ,
  LimiteCorteSaida TIME    ,
  InicioAdicionalNoturno TIME    ,
  TerminoAdicionalNoturno TIME    ,
  PercentualAdicionalNoturno FLOAT    ,
  NaoPagaAdicionalNoturnoHorasNormais BIT    ,
  TotalHoras DATETIME    ,
  TotalPeriodos INTEGER      ,
PRIMARY KEY(IdJornada),
  FOREIGN KEY(IdHorasExtrasAcumulo)
    REFERENCES HorasExtrasAcumulo(IdHorasExtrasAcumulo),
  FOREIGN KEY(IdSemana)
    REFERENCES Semana(IdSemana),
  FOREIGN KEY(idPercentuaisAcrescimo)
    REFERENCES PercentuaisAcrescimo(idPercentuaisAcrescimo));
GO


CREATE INDEX IFK_Rel_103 ON Jornada (IdHorasExtrasAcumulo);
GO
CREATE INDEX IFK_Rel_104 ON Jornada (IdSemana);
GO
CREATE INDEX IFK_Rel_97 ON Jornada (idPercentuaisAcrescimo);
GO


CREATE TABLE FuncionarioCalendario (
  IdFuncionario INTEGER  NOT NULL  ,
  IdCalendario INTEGER  NOT NULL  ,
  idColetivo INTEGER    ,
  DataInicio DATE  NOT NULL    ,
PRIMARY KEY(IdFuncionario, IdCalendario),
  FOREIGN KEY(idColetivo)
    REFERENCES Coletivo(idColetivo),
  FOREIGN KEY(IdFuncionario)
    REFERENCES Funcionario(IdFuncionario),
  FOREIGN KEY(IdCalendario)
    REFERENCES Calendario(IdCalendario));
GO


CREATE INDEX IFK_Rel_55 ON FuncionarioCalendario (idColetivo);
GO
CREATE INDEX IFK_Rel_114 ON FuncionarioCalendario (IdFuncionario);
GO
CREATE INDEX IFK_Rel_115 ON FuncionarioCalendario (IdCalendario);
GO


CREATE TABLE FuncionarioJornada (
  IdJornadaFuncionario INTEGER  NOT NULL   IDENTITY ,
  idColetivo INTEGER    ,
  IdFuncionario INTEGER  NOT NULL  ,
  IdJornada INTEGER  NOT NULL  ,
  SequenciaInicial INTEGER    ,
  DataInicio DATE  NOT NULL  ,
  DataFim DATE      ,
PRIMARY KEY(IdJornadaFuncionario),
  FOREIGN KEY(IdFuncionario)
    REFERENCES Funcionario(IdFuncionario),
  FOREIGN KEY(IdJornada)
    REFERENCES Jornada(IdJornada),
  FOREIGN KEY(idColetivo)
    REFERENCES Coletivo(idColetivo));
GO


CREATE INDEX IFK_Jornadas_Func_FK33 ON FuncionarioJornada (IdFuncionario);
GO
CREATE INDEX IFK_Jornadas_Func_FK34 ON FuncionarioJornada (IdJornada);
GO
CREATE INDEX IFK_Rel_59 ON FuncionarioJornada (idColetivo);
GO


CREATE TABLE Auditoria (
  IdAuditoria INTEGER  NOT NULL   IDENTITY ,
  idTipoAuditoria INTEGER  NOT NULL  ,
  idFuncionalidade INTEGER    ,
  IdOperador INTEGER  NOT NULL  ,
  IdOperacao INTEGER    ,
  DataHora DATETIME    ,
  Conteudo VARCHAR(800)    ,
  EnderecoIp VARCHAR(15)      ,
PRIMARY KEY(IdAuditoria),
  FOREIGN KEY(IdOperacao)
    REFERENCES Operacao(IdOperacao),
  FOREIGN KEY(IdOperador)
    REFERENCES Operador(IdOperador),
  FOREIGN KEY(idFuncionalidade)
    REFERENCES Funcionalidades(idFuncionalidade),
  FOREIGN KEY(idTipoAuditoria)
    REFERENCES TipoAuditoria(idTipoAuditoria));
GO


CREATE INDEX IFK_Rel_112 ON Auditoria (IdOperacao);
GO
CREATE INDEX IFK_Rel_116 ON Auditoria (IdOperador);
GO
CREATE INDEX IFK_Rel_95 ON Auditoria (idFuncionalidade);
GO
CREATE INDEX IFK_Rel_98 ON Auditoria (idTipoAuditoria);
GO


CREATE TABLE BancoHorasFechamentos (
  DataFechamento DATE  NOT NULL  ,
  IdFuncionario INTEGER  NOT NULL  ,
  IdBancoHoras INTEGER  NOT NULL  ,
  idTipoFechamento INTEGER  NOT NULL  ,
  idColetivo INTEGER    ,
  Credito TIME  NOT NULL  ,
  Debito TIME  NOT NULL    ,
PRIMARY KEY(DataFechamento, IdFuncionario, IdBancoHoras),
  FOREIGN KEY(idColetivo)
    REFERENCES Coletivo(idColetivo),
  FOREIGN KEY(IdFuncionario)
    REFERENCES Funcionario(IdFuncionario),
  FOREIGN KEY(IdBancoHoras)
    REFERENCES BancoHoras(IdBancoHoras),
  FOREIGN KEY(idTipoFechamento)
    REFERENCES TipoFechamento(idTipoFechamento));
GO


CREATE INDEX IFK_Rel_54 ON BancoHorasFechamentos (idColetivo);
GO
CREATE INDEX IFK_Rel_108 ON BancoHorasFechamentos (IdFuncionario);
GO
CREATE INDEX IFK_Rel_109 ON BancoHorasFechamentos (IdBancoHoras);
GO
CREATE INDEX IFK_Rel_90 ON BancoHorasFechamentos (idTipoFechamento);
GO


CREATE TABLE HistoricoSenhas (
  IdHistoricoSenhas INTEGER  NOT NULL   IDENTITY ,
  IdOperador INTEGER  NOT NULL  ,
  DataHora DATETIME    ,
  Senha VARCHAR(100)      ,
PRIMARY KEY(IdHistoricoSenhas),
  FOREIGN KEY(IdOperador)
    REFERENCES Operador(IdOperador));
GO


CREATE INDEX IFK_Rel_101 ON HistoricoSenhas (IdOperador);
GO


CREATE TABLE SequenciaMarcacao (
  idSequenciaMarcacao INTEGER  NOT NULL   IDENTITY ,
  IdFechamentoHoras INTEGER  NOT NULL  ,
  IdEmpresa INTEGER  NOT NULL  ,
  IdFuncionario INTEGER  NOT NULL  ,
  Operacao INTEGER    ,
  HorarioMarcacao DATETIME      ,
PRIMARY KEY(idSequenciaMarcacao, IdFechamentoHoras, IdEmpresa, IdFuncionario),
  FOREIGN KEY(IdFechamentoHoras, IdEmpresa, IdFuncionario)
    REFERENCES FechamentoHoras(IdFechamentoHoras, IdEmpresa, IdFuncionario));
GO


CREATE INDEX IFK_Rel_65 ON SequenciaMarcacao (IdFechamentoHoras, IdEmpresa, IdFuncionario);
GO


CREATE TABLE Planos (
  idPlano INTEGER  NOT NULL   IDENTITY ,
  IdEmpresa INTEGER  NOT NULL  ,
  Descricao VARCHAR(60)    ,
  LimiteEmpresas INTEGER    ,
  LimiteFuncionarios INTEGER    ,
  LimiteOperadores INTEGER    ,
  PrazoExpira DATE      ,
PRIMARY KEY(idPlano),
  FOREIGN KEY(IdEmpresa)
    REFERENCES Empresa(IdEmpresa));
GO


CREATE INDEX IFK_Rel_97 ON Planos (IdEmpresa);
GO


CREATE TABLE Cei (
  idCei INTEGER  NOT NULL   IDENTITY ,
  IdEmpresa INTEGER  NOT NULL  ,
  Descricao VARCHAR(12)      ,
PRIMARY KEY(idCei),
  FOREIGN KEY(IdEmpresa)
    REFERENCES Empresa(IdEmpresa));
GO


CREATE INDEX IFK_Rel_99 ON Cei (IdEmpresa);
GO


CREATE TABLE DSR (
  IdDsr INTEGER  NOT NULL   IDENTITY ,
  IdEmpresa INTEGER    ,
  LimiteHorasFaltas DATETIME  NOT NULL  ,
  IncluiHoraExtra BIT  NOT NULL  ,
  IncluiFeriadoComoHoraNormal BIT  NOT NULL  ,
  DescontaAusencia BIT  NOT NULL  ,
  Integral BIT  NOT NULL  ,
  ConsideraMesCheio BIT  NOT NULL  ,
  ConsideraFeriadoSabadoComoDiaNormal BIT  NOT NULL    ,
PRIMARY KEY(IdDsr),
  FOREIGN KEY(IdEmpresa)
    REFERENCES Empresa(IdEmpresa));
GO


CREATE INDEX IFK_Rel_100 ON DSR (IdEmpresa);
GO


CREATE TABLE FechamentoBancoHoras (
  IdFechamentoHoras INTEGER  NOT NULL   IDENTITY ,
  IdEmpresa INTEGER  NOT NULL  ,
  IdFuncionario INTEGER  NOT NULL  ,
  Credito TIME    ,
  Debito TIME    ,
  AcertoCredito TIME    ,
  AcertoDebito TIME    ,
  SaldoCredito TIME    ,
  SaldoDebito TIME    ,
  HoraNoturna TIME    ,
  AusenciaDiurna TIME    ,
  AusenciaNoturna TIME      ,
PRIMARY KEY(IdFechamentoHoras, IdEmpresa, IdFuncionario),
  FOREIGN KEY(IdFechamentoHoras, IdEmpresa, IdFuncionario)
    REFERENCES FechamentoHoras(IdFechamentoHoras, IdEmpresa, IdFuncionario));
GO


CREATE INDEX IFK_Rel_128 ON FechamentoBancoHoras (IdFechamentoHoras, IdEmpresa, IdFuncionario);
GO


CREATE TABLE Ocorrencia (
  idFechamentoOcorrencia INTEGER  NOT NULL   IDENTITY ,
  IdFechamentoHoras INTEGER  NOT NULL  ,
  IdEmpresa INTEGER  NOT NULL  ,
  IdFuncionario INTEGER  NOT NULL    ,
PRIMARY KEY(idFechamentoOcorrencia, IdFechamentoHoras, IdEmpresa, IdFuncionario),
  FOREIGN KEY(IdFechamentoHoras, IdEmpresa, IdFuncionario)
    REFERENCES FechamentoHoras(IdFechamentoHoras, IdEmpresa, IdFuncionario));
GO


CREATE INDEX IFK_Rel_74 ON Ocorrencia (IdFechamentoHoras, IdEmpresa, IdFuncionario);
GO


CREATE TABLE JornadaHorario (
  IdJornada INTEGER  NOT NULL  ,
  IdHorario INTEGER  NOT NULL  ,
  idSequencia INTEGER  NOT NULL    ,
PRIMARY KEY(IdJornada, IdHorario, idSequencia),
  FOREIGN KEY(IdJornada)
    REFERENCES Jornada(IdJornada),
  FOREIGN KEY(IdHorario)
    REFERENCES Horario(IdHorario));
GO


CREATE INDEX IFK_Rel_99 ON JornadaHorario (IdJornada);
GO
CREATE INDEX IFK_Rel_100 ON JornadaHorario (IdHorario);
GO


CREATE TABLE FechamentoBancoHorasLimite (
  IdFechamentoHoras INTEGER  NOT NULL  ,
  IdEmpresa INTEGER  NOT NULL  ,
  IdFuncionario INTEGER  NOT NULL  ,
  idTipodia INTEGER  NOT NULL  ,
  Limite TIME      ,
PRIMARY KEY(IdFechamentoHoras, IdEmpresa, IdFuncionario, idTipodia),
  FOREIGN KEY(IdFechamentoHoras, IdEmpresa, IdFuncionario)
    REFERENCES FechamentoBancoHoras(IdFechamentoHoras, IdEmpresa, IdFuncionario),
  FOREIGN KEY(idTipodia)
    REFERENCES TipoDia(idTipodia));
GO


CREATE INDEX IFK_Rel_93 ON FechamentoBancoHorasLimite (IdFechamentoHoras, IdEmpresa, IdFuncionario);
GO
CREATE INDEX IFK_Rel_94 ON FechamentoBancoHorasLimite (idTipodia);
GO


CREATE TABLE FuncionalidadesPlanoOperacao (
  idFuncionalidade INTEGER  NOT NULL  ,
  idPlano INTEGER  NOT NULL  ,
  IdOperacao INTEGER  NOT NULL    ,
PRIMARY KEY(idFuncionalidade, idPlano, IdOperacao),
  FOREIGN KEY(idFuncionalidade)
    REFERENCES Funcionalidades(idFuncionalidade),
  FOREIGN KEY(idPlano)
    REFERENCES Planos(idPlano),
  FOREIGN KEY(IdOperacao)
    REFERENCES Operacao(IdOperacao));
GO


CREATE INDEX IFK_Rel_98 ON FuncionalidadesPlanoOperacao (idFuncionalidade);
GO
CREATE INDEX IFK_Rel_99 ON FuncionalidadesPlanoOperacao (idPlano);
GO
CREATE INDEX IFK_Rel_97 ON FuncionalidadesPlanoOperacao (IdOperacao);
GO


CREATE TABLE Rep (
  IdRep INTEGER  NOT NULL   IDENTITY ,
  IdEmpresa INTEGER  NOT NULL  ,
  idCei INTEGER  NOT NULL  ,
  idTipoEquipamento INTEGER  NOT NULL  ,
  IdModeloRep INTEGER    ,
  NumeroRep INTEGER    ,
  NumeroSerie VARCHAR(17)  NOT NULL  ,
  NumeroFabricante VARCHAR(10)  NOT NULL  ,
  Local VARCHAR(50)  NOT NULL  ,
  Nsr VARCHAR(11)  NOT NULL  ,
  Arquivo VARCHAR(150)  NOT NULL    ,
PRIMARY KEY(IdRep),
  FOREIGN KEY(IdModeloRep)
    REFERENCES ModeloRep(IdModeloRep),
  FOREIGN KEY(idTipoEquipamento)
    REFERENCES TipoEquipamento(idTipoEquipamento),
  FOREIGN KEY(idCei)
    REFERENCES Cei(idCei),
  FOREIGN KEY(IdEmpresa)
    REFERENCES Empresa(IdEmpresa));
GO


CREATE INDEX IFK_Rel_47 ON Rep (IdModeloRep);
GO
CREATE INDEX IFK_Rel_110 ON Rep (idTipoEquipamento);
GO
CREATE INDEX IFK_Rel_101 ON Rep (idCei);
GO
CREATE INDEX IFK_Rel_101relrep ON Rep (IdEmpresa);
GO


CREATE TABLE Marcacoes (
  idMarcacao INTEGER  NOT NULL  ,
  idEncaixeMarcacao INTEGER    ,
  idStatusMarcacao INTEGER  NOT NULL  ,
  IdRep INTEGER  NOT NULL  ,
  IdEmpresa INTEGER    ,
  IdFuncionario INTEGER    ,
  IdMotivo INTEGER    ,
  Nsr VARCHAR(11)  NOT NULL DEFAULT ((0)) ,
  Pis VARCHAR(11)  NOT NULL DEFAULT ((0)) ,
  NumeroSerie INTEGER  NOT NULL DEFAULT ((0)) ,
  DataHora DATETIME  NOT NULL  ,
  Invalido BIT    ,
  Cartao VARCHAR(16)      ,
PRIMARY KEY(idMarcacao),
  FOREIGN KEY(IdFuncionario)
    REFERENCES Funcionario(IdFuncionario),
  FOREIGN KEY(idStatusMarcacao)
    REFERENCES StatusMarcacao(idStatusMarcacao),
  FOREIGN KEY(IdMotivo)
    REFERENCES Motivo(IdMotivo),
  FOREIGN KEY(IdEmpresa)
    REFERENCES Empresa(IdEmpresa),
  FOREIGN KEY(IdRep)
    REFERENCES Rep(IdRep),
  FOREIGN KEY(idEncaixeMarcacao)
    REFERENCES EncaixeMarcacao(idEncaixeMarcacao));
GO


CREATE INDEX IFK_Bilhetes_FK8 ON Marcacoes (IdFuncionario);
GO
CREATE INDEX IFK_Rel_50 ON Marcacoes (idStatusMarcacao);
GO
CREATE INDEX IFK_Rel_51 ON Marcacoes (IdMotivo);
GO
CREATE INDEX IFK_Rel_96 ON Marcacoes (IdEmpresa);
GO
CREATE INDEX IFK_Rel_97 ON Marcacoes (IdRep);
GO
CREATE INDEX IFK_Rel_98 ON Marcacoes (idEncaixeMarcacao);
GO



