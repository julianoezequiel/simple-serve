insert into modulos (id,descricao,modulo) value (1,'Cadastros',1);
insert into modulos (id,descricao,modulo) value (2,'Financeiro',2);
insert into modulos (id,descricao,modulo) value (3,'Controle de Estoque',3);
insert into modulos (id,descricao,modulo) value (4,'Relatórios',4);
insert into modulos (id,descricao,modulo) value (5,'Configurações',5);

insert into permissao (id,descricao,modulo_id,permissao) value (1,'Usuarios',1,1);
insert into permissao (id,descricao,modulo_id,permissao) value (2,'Clientes',1,2);

insert into tipo_documento (id,descricao) value (1,'CNPJ');
insert into tipo_documento (id,descricao) value (2,'CPF');


insert into empresa (id,razao_social,nome_fantasia,documento,tipo_documento_Id,endereco,bairro,cep,cidade,uf,fone,fax,observacao,ativo) value (1,'Simple','Simple','011001010101',1,'rua Henry Ford','Vila Fanny','81010100','Curitiba','PR','(41)3333-5555','(41)5555-3333','observacao',true);


insert into usuario (email,senha,ativo,empresa_id,admin) value ('admin@admin.com','12345',1,true,true);
insert into usuario (email,senha,ativo,empresa_id,admin) value ('juliano@gmail.com','12345',1,true,true);
insert into usuario (email,senha,ativo,empresa_id,admin) value ('usuario@gmail.com','12345',1,true,false);

insert into permissao_usuario (id_permissao,id_usuario) value (1,1);
insert into permissao_usuario (id_permissao,id_usuario) value (2,1);

insert into permissao_usuario (id_permissao,id_usuario) value (2,2);

insert into permissao_usuario (id_permissao,id_usuario) value (2,3);
