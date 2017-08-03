/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  juliano.ezequiel
 * Created: 19/09/2016
 */

Select "DIA DA SEMANA","SEQUENCIA MARCACAO 1","SEQUENCIA MARCACAO 2" from (

SELECT jh.idSequencia as "DIA DA SEMANA", hm.IdSequencia as "SEQUENCIA MARCACAO 1", '' as "SEQUENCIA MARCACAO 2" 
--, hm.HorarioEntrada, hm.HorarioSaida
FROM dbo.Jornada j 
inner join dbo.JornadaHorario jh on j.IdJornada = jh.IdJornada 
inner join dbo.Horario h on  h.IdHorario = jh.IdHorario 
inner join dbo.HorarioMarcacao hm on hm.IdHorario=h.IdHorario
WHERE j.IdJornada = 1
AND hm.IdSequencia=1

UNION ALL

SELECT jh.idSequencia as "DIA DA SEMANA",'' as "SEQUENCIA MARCACAO 1", hm.IdSequencia as "SEQUENCIA MARCACAO 2"
--, hm.HorarioEntrada, hm.HorarioSaida
FROM dbo.Jornada j 
inner join dbo.JornadaHorario jh on j.IdJornada = jh.IdJornada 
inner join dbo.Horario h on  h.IdHorario = jh.IdHorario 
inner join dbo.HorarioMarcacao hm on hm.IdHorario=h.IdHorario

WHERE j.IdJornada = 1
AND hm.IdSequencia=2

) as teste
GROUP BY "DIA DA SEMANA","SEQUENCIA MARCACAO 1","SEQUENCIA MARCACAO 2"



SELECT jh.idSequencia, hm.IdSequencia, hm.HorarioEntrada, hm.HorarioSaida,j.JornadaVariavel
FROM dbo.Jornada j 
inner join dbo.JornadaHorario jh on j.IdJornada = jh.IdJornada 
inner join dbo.Horario h on  h.IdHorario = jh.IdHorario 
inner join dbo.HorarioMarcacao hm on hm.IdHorario=h.IdHorario
WHERE j.IdJornada = 2
order by jh.idSequencia


SELECT fc.* 
FROM dbo.FuncionarioCalendario fc, dbo.Calendario c
where fc.IdCalendario=c.IdCalendario
and fc.IdFuncionario=3


select * from FuncionarioCalendario


