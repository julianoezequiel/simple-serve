
insert into Modulo values (1,'Cadastros');
insert into Modulo values (2,'Lançamentos Coletivos');
insert into Modulo values (3,'Relatórios');
insert into Modulo values (4,'Arquivos');
insert into Modulo values (5,'Ferramentas');

insert into Funcoes values (1, 'Empresa' , '',1);
insert into Funcoes values (2, 'Incluir/Editar/Excluir/Consultar',1,1);
insert into Funcoes values (3, 'Consultar',2,1);

insert into Funcoes values (4, 'Cargo','',1);
insert into Funcoes values (5, 'Incluir/Editar/Excluir/Consultar',4,1);
insert into Funcoes values (6, 'Consultar',4,1);

insert into Funcoes values (7, 'Horário','',1);
insert into Funcoes values (8, 'Incluir/Editar/Excluir/Consultar',7,1);
insert into Funcoes values (9, 'Consultar',7,1);

insert into Funcoes values (10, '', 'Jornada');
insert into Funcoes values (11, 10, 'Incluir/Editar/Excluir/Consultar');
insert into Funcoes values (12, 10, 'Consultar');

insert into Funcoes values (13, '', 'Funcionário');
insert into Funcoes values (14, 13, 'Incluir/Editar/Excluir/Consultar');
insert into Funcoes values (15, 13, 'Consultar');

insert into Funcoes values (16, '', 'Motivo');
insert into Funcoes values (17, 16, 'Incluir/Editar/Excluir/Consultar');
insert into Funcoes values (18, 16, 'Consultar');

insert into Funcoes values (19, '', 'Calendario');
insert into Funcoes values (20, 19, 'Incluir/Editar/Excluir/Consultar');
insert into Funcoes values (21, 19, 'Consultar');

insert into Funcoes values (22, '', 'Banco de Horas');
insert into Funcoes values (23, 22, 'Incluir/Editar/Excluir/Consultar');
insert into Funcoes values (24, 22, 'Consultar');

insert into Funcoes values (25, '', 'Exportação');
insert into Funcoes values (26, 25, 'Incluir/Editar/Excluir/Consultar');
insert into Funcoes values (27, 25, 'Consultar');

insert into Funcoes values (28, '', 'Grupo de Acesso');
insert into Funcoes values (29, 28, 'Incluir/Editar/Excluir/Consultar')
insert into Funcoes values (30, 28, 'Consultar')

insert into Funcoes values (31, '', 'Operador')
insert into Funcoes values (32, 30, 'Incluir/Editar/Excluir/Consultar')
insert into Funcoes values (33, 30, 'Consultar')

insert into Funcoes values (34, '', 'Grupo Empresas')
insert into Funcoes values (35, 33, 'Incluir/Editar/Excluir/Consultar')
insert into Funcoes values (36, 33, 'Consultar')

insert into Funcoes values (37, '', 'Configurações')
insert into Funcoes values (38, 36, 'Ocorrências')
insert into Funcoes values (39, 36, 'DSR')
//------------ fim do 1
insert into Funcoes values (40, '', 'Afastamentos')
insert into Funcoes values (41, '', 'Calendários')
insert into Funcoes values (42, '', 'Jornadas')
insert into Funcoes values (43, '', 'Exceções de Jornada')
insert into Funcoes values (44, '', 'Compensações')

insert into Funcoes values (45, '', 'Banco de horas')
insert into Funcoes values (46, 44, 'Incluir/Editar/Excluir/Consultar')
insert into Funcoes values (47, 44, 'Manutenções')

insert into Funcoes values (48, '', 'Espelho')

insert into Funcoes values (49, '', 'Presença')
insert into Funcoes values (50, '', 'Freqüência')
insert into Funcoes values (51, '', 'Ocorrências')
insert into Funcoes values (52, '', 'Horas extras')
insert into Funcoes values (53, '', 'Banco de horas')

insert into Funcoes values (54, '', 'Cadastros')
insert into Funcoes values (55, 53, 'Empresas')
insert into Funcoes values (56, 53, 'Funcionários')
insert into Funcoes values (57, 53, 'Calendários')
insert into Funcoes values (58, 53, 'Horários')
insert into Funcoes values (59, 53, 'Banco de horas')
insert into Funcoes values (60, 53, 'Jornadas')

insert into Funcoes values (61, '', 'AFDT')
insert into Funcoes values (62, '', 'ACJEF')
insert into Funcoes values (63, '', 'AFD')
insert into Funcoes values (64, '', 'Espelho Fiscal')

insert into Funcoes values (65, '', 'Marcações')
insert into Funcoes values (66, 64, 'Manutenção')
insert into Funcoes values (67, 64, 'Verificação')
insert into Funcoes values (68, 64, 'Geração Pré-assinaladas')

insert into Funcoes values (69, '', 'Importações')
insert into Funcoes values (70, 68, 'Bilhetes')
insert into Funcoes values (71, 68, 'Funcionários')

insert into Funcoes values (72, '', 'Exportações')
insert into Funcoes values (73, 71, 'Eventos')
insert into Funcoes values (74, 71, 'Funcionários')

insert into Grupo values ('Administradores');

insert into Operacao values (1,'Cadastro')
insert into Operacao values (2,'Alteração')
insert into Operacao values (3,'Exclusão')
