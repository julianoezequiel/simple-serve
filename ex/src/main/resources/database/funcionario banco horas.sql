select * from FuncionarioBancoHoras WHERE IdFuncionario = 7
AND ((DataInicio between '2016-12-09' and '2016-12-12')
OR (DataFim between '2016-12-09' and '2016-12-12')
OR (DataInicio >= '2016-12-09' AND DataInicio <= '2016-12-12')
OR (DataInicio <= '2016-12-09' AND DataFim >= '2016-12-12'))

-- usar esta instruçao
SELECT * FROM FuncionarioBancoHoras where IdFuncionario = 7 and DataFim is null and DataInicio <= '2016-12-10'

SELECT * FROM FuncionarioBancoHoras  WHERE IdFuncionario = 7 and  DataFim IS NULL

select * from Cartao WHERE IdFuncionario = 7
AND ((DataInicio between '2016-11-01' and '2016-12-01')
OR (DataValidade between '2016-11-01' and '2016-12-01'))
OR (DataInicio >= '2016-11-01' AND DataInicio <= '2016-12-01')
OR (DataInicio <= '2016-11-01' AND DataValidade >= '2016-12-01')