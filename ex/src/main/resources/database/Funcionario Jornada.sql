--Anterior
select top 10 * from FuncionarioJornada  where IdFuncionario = 34 and DataInicio < '2016-12-13' ORDER BY DataInicio desc ;
-- Posterior
select top 10 * from FuncionarioJornada where IdFuncionario = 34 and DataInicio > '2016-12-13' ORDER BY DataInicio asc ;

select top 10 * from FuncionarioJornada where IdFuncionario = 34 and DataInicio = '2016-12-27' and ORDER BY DataInicio asc;

-- select * from FuncionarioJornada where IdFuncionario = 7 ORDER BY DataInicio;

select * from FuncionarioJornada WHERE IdFuncionario = 34 ORDER BY DataInicio asc;

select * from FuncionarioJornada WHERE IdFuncionario = 34 and DataInicio >= '2016-12-01' and Excecao = 'true' ORDER BY DataInicio;


select * from FuncionarioJornada WHERE IdFuncionario = 34
AND ((DataInicio between '2016-12-1' and '2016-12-10')
OR (DataFim between '2016-12-1' and '2016-12-10')
OR (DataInicio >= '2016-12-1' AND DataInicio <= '2016-12-10')
OR (DataInicio <= '2016-12-1' AND DataFim >= '2016-12-10') and Excecao = 'true')
ORDER BY DataInicio asc
