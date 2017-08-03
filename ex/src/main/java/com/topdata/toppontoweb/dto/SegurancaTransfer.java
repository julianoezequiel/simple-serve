package com.topdata.toppontoweb.dto;

import com.topdata.toppontoweb.entity.autenticacao.Seguranca;

/**
 * @version 1.0.4 data 15/06/2016
 * @since 1.0.4 data 15/06/2016
 *
 * @author juliano.ezequiel
 */
public class SegurancaTransfer {

    private final Seguranca seguranca;

    public SegurancaTransfer() {
        this.seguranca = null;
    }

    public Integer getId() {
        return this.seguranca.getIdSeguranca();
    }

    public Integer getTamanhoMinimoSenha() {
        return this.seguranca.getTamanhoMinimoSenha();
    }

    public Integer getQtdeBloqueioTentativas() {
        return this.seguranca.getQtdeBloqueioTentativas();
    }

    public Integer getQtdeDiasTrocaSenha() {
        return this.seguranca.getQtdeDiasTrocaSenha();
    }
    
    public Integer getQtdeNaoRepetirSenhas(){
        return this.seguranca.getQtdeNaoRepetirSenhas();
    }
    
    public Boolean getComplexidadeLetrasNumeros(){
        return this.seguranca.getComplexidadeLetrasNumeros();
    }
    
//    public Integer getQtdeHorasDesbloqueioUsuario(){
//        return this.seguranca.getQtdeHorasDesbloqueioUsuario();
//    }

}
