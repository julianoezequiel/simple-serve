/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  juliano.ezequiel
 * Created: 25/01/2017
 */


SELECT TOP 1000 [IdAuditoria]
      ,[idTipoAuditoria]
      ,f.[idFuncionalidade]
	  ,f.Descricao
      ,[IdOperador]
      ,[IdOperacao]
      ,[DataHora]
      ,[Conteudo]
      ,[EnderecoIp]
  FROM [TopPontoWeb_Homologacao].[dbo].[Auditoria] a
  left join [TopPontoWeb_Homologacao].[dbo].Funcionalidades f
  on a.idFuncionalidade=f.idFuncionalidade
  order by IdAuditoria desc
