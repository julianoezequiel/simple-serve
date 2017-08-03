package com.topdata.toppontoweb.dto.fechamento;

import com.topdata.toppontoweb.dto.gerafrequencia.GeraFrequenciaStatusTransfer;

import com.topdata.toppontoweb.utils.constantes.CONSTANTES;

/**
 *
 * @author juliano.ezequiel
 */
public class FechamentoStatusTransfer extends GeraFrequenciaStatusTransfer {

    public FechamentoStatusTransfer() {
        super(null);
    }

    private CONSTANTES.Enum_OPERACAO operacao;

    public CONSTANTES.Enum_OPERACAO getOperacao() {
        return operacao;
    }

    public void setOperacao(CONSTANTES.Enum_OPERACAO operacao) {
        this.operacao = operacao;
    }

    @Override
    public String toString() {
        return "FechamentoStatusTransfer{" + "id=" + getId() + ", data=" + getData() + '}';
    }

}
