package com.topdata.toppontoweb.services.gerafrequencia.entity.fiscal;

import com.topdata.toppontoweb.utils.Utils;

/**
 * Formato arquivo de exportação AFDT
 *
 * @author enio.junior
 */
public class AFDT {

    //Sequencial do registro no arquivo (Começando por 2)
    private String campo1Sequencial;

    //Tipo do registro, "2"
    private String campo2TipoRegistro;

    //Data da marcação do ponto, no formato "ddmmaaaa"
    private String campo3DataMarcacao;

    //Horário da marcação do ponto, no formato "hhmm"
    private String campo4HoraMarcacao;

    //Número do PIS do empregado
    private String campo5PIS;

    //Número de fabricação do REP onde foi feito o registro
    private String campo6NumeroRep;

    //Tipo de marcação, "E" para ENTRADA,"S" para SAÍDA ou "D" para registro a ser DESCONSIDERADO
    private String campo7TipoMarcacao;

    //Número seqüencial por empregado e jornada para o conjunto Entrada/Saída
    private String campo8NumeroSequencialFuncionario;

    //Tipo de registro: "O" para registro eletrônico ORIGINAL, "I" para registro INCLUÍDO por digitação, "P" para intervalo PRÉ-ASSINALADO
    private String campo9TipoRegistro;

    //Motivo: Campo a ser preenchido se o campo 7 for "D" ou se o campo 9 for "I"
    private String campo10Motivo;

    public AFDT() {
        this.campo1Sequencial = "";
        this.campo2TipoRegistro = "";
        this.campo3DataMarcacao = "";
        this.campo4HoraMarcacao = "";
        this.campo5PIS = "";
        this.campo6NumeroRep = "";
        this.campo7TipoMarcacao = "";
        this.campo8NumeroSequencialFuncionario = "";
        this.campo9TipoRegistro = "";
        this.campo10Motivo = Utils.corrigePrecisaoString("", 100, true);;
    }

    public String getCampo1Sequencial() {
        return campo1Sequencial;
    }

    public void setCampo1Sequencial(Integer campo1) {
        this.campo1Sequencial = Utils.corrigePrecisaoNumero(campo1.toString(), 9);
    }

    public String getCampo2TipoRegistro() {
        return campo2TipoRegistro;
    }

    public void setCampo2TipoRegistro(String campo2TipoRegistro) {
        this.campo2TipoRegistro = String.format("%1s", campo2TipoRegistro);
    }

    public String getCampo3DataMarcacao() {
        return campo3DataMarcacao;
    }

    public void setCampo3DataMarcacao(String campo3DataMarcacao) {
        this.campo3DataMarcacao = Utils.corrigePrecisaoNumero(campo3DataMarcacao, 8);
    }

    public String getCampo4HoraMarcacao() {
        return campo4HoraMarcacao;
    }

    public void setCampo4HoraMarcacao(String campo4HoraMarcacao) {
        this.campo4HoraMarcacao = Utils.corrigePrecisaoNumero(campo4HoraMarcacao, 4);
    }

    public String getCampo5PIS() {
        return campo5PIS;
    }

    public void setCampo5PIS(String campo5PIS) {
        this.campo5PIS = Utils.corrigePrecisaoNumero(campo5PIS, 12);
    }

    public String getCampo6NumeroRep() {
        return campo6NumeroRep;
    }

    public void setCampo6NumeroRep(String campo6) {
        this.campo6NumeroRep = Utils.corrigePrecisaoNumero(campo6, 17);
    }

    public String getCampo7TipoMarcacao() {
        return campo7TipoMarcacao;
    }

    public void setCampo7TipoMarcacao(String campo7TipoMarcacao) {
        this.campo7TipoMarcacao = String.format("%1s", campo7TipoMarcacao);
    }

    public String getCampo8NumeroSequencialFuncionario() {
        return campo8NumeroSequencialFuncionario;
    }

    public void setCampo8NumeroSequencialFuncionario(Integer campo8) {
        this.campo8NumeroSequencialFuncionario = Utils.corrigePrecisaoNumero(campo8.toString(), 2);
    }

    public String getCampo9TipoRegistro() {
        return campo9TipoRegistro;
    }

    public void setCampo9TipoRegistro(String campo9TipoRegistro) {
        this.campo9TipoRegistro = String.format("%1s", campo9TipoRegistro);
    }

    public String getCampo10Motivo() {
        return campo10Motivo;
    }
    
    
    public void setCampo10Motivo(String campo10Motivo) {
        try{
            //Ignora os 4 primeiros digitos pois vem no formato "I - Motivo" e 
            // nós queremos somente o "Motivo"
            campo10Motivo = campo10Motivo.substring(4);
            this.campo10Motivo = Utils.corrigePrecisaoString(campo10Motivo, 100, true);
        }catch(StringIndexOutOfBoundsException ex){
            //String tem menos de 3 caracteres, entao seta vazio
            this.campo10Motivo = Utils.corrigePrecisaoString("", 100, true);;
        }
    }

}
