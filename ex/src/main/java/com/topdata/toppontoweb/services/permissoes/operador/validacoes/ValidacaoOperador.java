package com.topdata.toppontoweb.services.permissoes.operador.validacoes;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topdata.toppontoweb.dao.Dao;
import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.entity.autenticacao.Operador;
import com.topdata.toppontoweb.entity.autenticacao.Operador_;
import com.topdata.toppontoweb.entity.autenticacao.Planos;
import com.topdata.toppontoweb.entity.funcionario.Funcionario;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoResponse;
import com.topdata.toppontoweb.services.Validacao;
import com.topdata.toppontoweb.services.ValidacoesCadastro;
import com.topdata.toppontoweb.services.autenticacao.ValidarComplexidadeSenha;
import com.topdata.toppontoweb.services.autenticacao.ValidarDataTrocarSenha;
import com.topdata.toppontoweb.services.autenticacao.ValidarHistoricoSenha;
import com.topdata.toppontoweb.services.autenticacao.ValidarTamanhoSenha;
import com.topdata.toppontoweb.services.funcionario.FuncionarioService;
import com.topdata.toppontoweb.services.permissoes.OperadorService;
import com.topdata.toppontoweb.services.permissoes.PlanoService;
import com.topdata.toppontoweb.services.validacoes.ValidarNaoCadastrado;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;
import com.topdata.toppontoweb.utils.constantes.MSG;

/**
 * @version 1.0.4 data 31/08/2016
 * @since 1.0.3 data 03/05/2016
 *
 * @author juliano.ezequiel
 */
@Service
public class ValidacaoOperador implements ValidacoesCadastro<Operador, Object> {

    @Autowired
    private ValidarComplexidadeSenha complexidadeSenha;
    @Autowired
    private ValidarDataTrocarSenha dataTrocarSenha;
    @Autowired
    private ValidarTamanhoSenha tamanhoSenha;
    @Autowired
    private ValidarNaoCadastrado validarNaoCad;
    @Autowired
    private ValidarIdentificador validarIndentificador;
    @Autowired
    private ValidarJaCad validarJaCad;
    @Autowired
    private ValidarOperadorSistema validarAdmin;
    @Autowired
    private ValidarOperadorCamposObrigatorios validarCamposObrigatorios;
    @Autowired
    private ValidarEmail validarEmail;
    @Autowired
    private AtualizarValoresEntreEntidades atualizarValoresEntreEntidades;
    @Autowired
    private ValidarHistoricoSenha validarHistoricoSenha;
    @Autowired
    private ValidarExluirAdmi validarExluirAdmi;
    @Autowired
    private ValidarSenha validarSenha;
    @Autowired
    private FuncionarioService funcionarioService;
    @Autowired
    private TopPontoResponse topPontoResponse;
    @Autowired
    private PlanoService planoService;
    @Autowired
    private OperadorService operadorService;
    @Autowired
    private ValidarOperadorSistema validarOperadorSistema;

    @Autowired
    private Dao dao;

    private List<Validacao> validacoes;

    public ValidacaoOperador() {
        this.validacoes = new ArrayList<>();
    }

    @Override
    public Operador validarSalvar(Operador operador) throws ServiceException {
        
        validarNomeAdmin(operador);
        
        
        validacoes = new ArrayList<>();

        validacoes.add(validarIndentificador);
        validacoes.add(validarOperadorSistema);
        validacoes.add(validarJaCad);
        validacoes.add(complexidadeSenha);
        validacoes.add(tamanhoSenha);
        validacoes.add(validarHistoricoSenha);
        operador.setUltimoAcesso(Calendar.getInstance().getTime());

        validacoes.add(validarCamposObrigatorios);
        validacoes.add(validarEmail);
        validacoes.add(new VValidarLimiteOperadorPlano());

        for (Validacao validacao : validacoes) {
            operador = (Operador) validacao.validar(operador, null);
        }
        return operador;
    }

    @Override
    public Operador validarExcluir(Operador operador) throws ServiceException {

        HashMap<String, Object> mapParameter = new HashMap<>();
        mapParameter.put(Operador_.usuario.getName(), operador.getUsuario());
        mapParameter.put(Operador_.idOperador.getName(), operador.getIdOperador());

        validacoes = new ArrayList<>();

        validacoes.add(validarNaoCad);
        validacoes.add(validarExluirAdmi);
//        validacoes.add(new ValidarVinculo());
        validacoes.add(new VValidarVinculoFuncionario());

        for (Validacao validacao : validacoes) {
            operador = (Operador) validacao.validar(operador, mapParameter);
        }
        return operador;
    }

    @Override
    public Operador validarAtualizar(Operador operador) throws ServiceException {
        
        validarNomeAdmin(operador);

        validacoes = new ArrayList<>();

        validacoes.add(validarIndentificador);
        validacoes.add(validarOperadorSistema);
        validacoes.add(validarEmail);

        if (operador.getSenha() != null) {
            validacoes.add(complexidadeSenha);
            validacoes.add(tamanhoSenha);
            validacoes.add(validarHistoricoSenha);
        }
        validacoes.add(atualizarValoresEntreEntidades);

        for (Validacao validacao : validacoes) {
            operador = (Operador) validacao.validar(operador, null);
        }

        return operador;
    }

    private class ValidarVinculo implements Validacao<Operador, Object> {

        @Override
        public Operador validar(Operador operador, Object i) throws ServiceException {
            if (operador != null) {

                if (operador.getAuditoriaList().size() > 1) {
                    throw new ServiceException(topPontoResponse.alerta(MSG.OPERADOR.ALERTA_POSSUI_REGISTROS.getResource(), operador.getUsuario()));
                }
            }
            return operador;
        }

    }

    private class VValidarVinculoFuncionario implements Validacao<Operador, Object> {

        @Override
        public Operador validar(Operador operador, Object i) throws ServiceException {
            Funcionario f = funcionarioService.buscarPorOperador(operador.getIdOperador());
            if (f != null) {
                funcionarioService.removeOperadorFuncionario(operador);
            }
            return operador;
        }

    }

    private class VValidarLimiteOperadorPlano implements Validacao<Operador, Object> {

        @Override
        public Operador validar(Operador operador, Object i) throws ServiceException {
            Planos p = planoService.buscarPlanoValido();
            if (Integer.parseInt(operadorService.quantidade(Operador.class)) > p.getLimiteOperadores()) {
                throw new ServiceException(topPontoResponse.alertaValidacao(MSG.PLANOS.ALERTA_LIMITE_OPERADOR.getResource(), p.getLimiteOperadores().toString()));
            }

            return operador;
        }

    }

    private void validarNaoCadastrado(Operador operador) throws ServiceException {
        try {
            HashMap<String, Object> mapParameter = new HashMap<>();
            mapParameter.put(Operador_.usuario.getName(), operador.getUsuario());
            mapParameter.put(Operador_.idOperador.getName(), operador.getIdOperador());
            List<Operador> operadorList = this.dao.findbyAttributes(mapParameter, Operador.class);
            if (!operadorList.isEmpty()) {
                throw new ServiceException(this.topPontoResponse.alertaNaoCad(operador.toString()));
            }
        } catch (DaoException ex) {
            throw new ServiceException(this.topPontoResponse.erroValidar(new Operador().toString()),ex);
        }
    }
    
    private void validarNomeAdmin(Operador o) throws ServiceException{
        if(o.getUsuario() != null && o.getUsuario().toUpperCase().equals(CONSTANTES.OPERADOR_ADMIN)){
            //verifica se o usuário é o admin msmo
            if(o.getIdOperador() != null){
                Operador operadorBase = operadorService.buscar(Operador.class, o.getIdOperador());
                
                //Busca o operador na base, se ele for diferente de admin... não é o admin
                if(operadorBase.getUsuario() != null && !operadorBase.getUsuario().toUpperCase().equals(CONSTANTES.OPERADOR_ADMIN)){
                    throw new ServiceException(this.topPontoResponse.alertaValidacao(MSG.OPERADOR.ALERTA_OPERADOR_SISTEMA.getResource()));
                }
            }else{
                throw new ServiceException(this.topPontoResponse.alertaValidacao(MSG.OPERADOR.ALERTA_OPERADOR_SISTEMA.getResource()));
            }
        }
    }
}
