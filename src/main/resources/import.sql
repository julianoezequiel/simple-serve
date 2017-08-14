insert into tipoDocumento (id,descricao) value (1,'CNPJ');
insert into tipoDocumento (id,descricao) value (2,'CPF');

insert into empresa (id,razaoSocial,nomeFantasia,documento,tipo_documento_Id,endereco,bairro,cep,cidade,uf,fone,fax,observacao,ativo)
value (1,'Simple','Simple','011001010101',1,'rua Henry Ford','Vila Fanny','81010100','Curitiba','PR','(41)3333-5555','(41)5555-3333','observacao',true);


insert into usuario (email,senha,ativo,empresa_id) value ('admin@admin.com','12345',1,true);
insert into usuario (email,senha,ativo,empresa_id) value ('juliano@gmail.com','12345',1,true);