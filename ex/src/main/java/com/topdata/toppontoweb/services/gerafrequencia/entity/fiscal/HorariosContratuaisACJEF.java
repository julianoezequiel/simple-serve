package com.topdata.toppontoweb.services.gerafrequencia.entity.fiscal;

import com.topdata.toppontoweb.utils.Utils;

/**
 * Formato arquivo de exportação ACJEF
 *
 * @author enio.junior
 */
public class HorariosContratuaisACJEF {


    //Tipo do registro, "2"
    private String campo2TipoRegistro;

    //Código do Horário (CH), no formato "nnnn".
    private String campo3CH;

    //Entrada, no formato "hhmm"
    private String campo4Horarios;

    public HorariosContratuaisACJEF() {
        this.campo2TipoRegistro = "";
        this.campo3CH = "";
        this.campo4Horarios = "";
    }

    public String getCampo2TipoRegistro() {
        return campo2TipoRegistro;
    }

    public void setCampo2TipoRegistro(String campo2TipoRegistro) {
        this.campo2TipoRegistro = String.format("%1s", campo2TipoRegistro);
    }

    public String getCampo3CH() {
        return campo3CH;
    }

    public void setCampo3CH(Integer campo3) {
        this.campo3CH = Utils.corrigePrecisaoNumero(campo3.toString(), 4);
    }

    /**
     * @return the campo4Horarios
     */
    public String getCampo4Horarios() {
        return campo4Horarios;
    }

    /**
     * @param campo4Horarios the campo4Horarios to set
     */
    public void setCampo4Horarios(String campo4Horarios) {
        this.campo4Horarios = campo4Horarios;
    }

    

}
