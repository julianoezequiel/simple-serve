package com.topdata.toppontoweb.services.coletivo;

//<editor-fold defaultstate="collapsed" desc="IMPORTS">
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import com.topdata.toppontoweb.dto.ColetivoResultado;
import com.topdata.toppontoweb.dto.ColetivoTransfer;
import com.topdata.toppontoweb.dto.ProgressoTransfer;
import com.topdata.toppontoweb.entity.Entidade;
import com.topdata.toppontoweb.entity.autenticacao.Operador;
import com.topdata.toppontoweb.entity.funcionario.Afastamento;
import com.topdata.toppontoweb.entity.funcionario.Coletivo;
import com.topdata.toppontoweb.entity.funcionario.Funcionario;
import com.topdata.toppontoweb.services.ServiceException;
import java.util.Collection;
//</editor-fold>

/**
 * Contrato obrigatório para o SERVIÇO de lançamentos coletivos
 *
 * @version 1.0.0.0 data 21/12/2016
 * @param <T>
 * @since 1.0.0.0 data 21/12/2016
 *
 * @author juliano.ezequiel
 */
public interface ColetivoSeviceInterface<T> {

    public void salvarColetivo(HttpServletRequest request, ColetivoTransfer coletivoTransfer, ProgressoTransfer progresso) throws ServiceException;

    public void atualizarColetivo(HttpServletRequest request, ColetivoTransfer coletivoTransfer, ProgressoTransfer progresso) throws ServiceException;

    public List<ColetivoTransfer> listarColetivos(HttpServletRequest request) throws ServiceException;

    public ColetivoTransfer buscarColetivo(HttpServletRequest request, Integer id) throws ServiceException;

    public List<Funcionario> filtrarFuncionario(List<T> list, Operador operador);

    public Response excluirColetivo(HttpServletRequest request, Integer id) throws ServiceException;

    public List<ColetivoResultado> removerEntidadeTipoDoColetivo(Coletivo coletivo) throws ServiceException;
    
    public void validacoesEdicao(Collection<T> list);
}
