/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.topdata.toppontoweb.services.gerafrequencia.entity.fiscal;

import com.topdata.toppontoweb.utils.Utils;

/**
 * Formato arquivo de exportação ACJEF
 *
 * @author enio.junior
 */
public class ACJEF {
    //Tipo do registro, "3"
    private String campo2TipoRegistro;

    //Número do PIS do empregado
    private String campo3PIS;

    //Data de início da jornada, no formato "ddmmaaaa"
    private String campo4InicioJornada;

    //Primeiro horário de entrada da jornada, no formato "hhmm"
    private String campo5PrimeiroHorarioJornada;

    //Código do horário (CH) previsto para a jornada, no formato "nnnn"
    private String campo6CH;

    //Horas diurnas não extraordinárias, no formato "hhmm"
    private String campo7HorasDiurnas;

    //Horas noturnas não extraordinárias, no formato "hhmm"
    private String campo8HorasNoturnas;

    //Horas extras 1, no formato "hhmm"
    private String campo9HorasExtras1;

    //Percentual do adicional de horas extras 1,onde as 2 primeiras posições indicam a parte inteira e as 2 seguintes a fração decimal
    private String campo10PercentualHorasExtras1;

    //Modalidade da hora extra 1, assinalado com "D" se as horas extras forem diurnas e "N" se forem noturnas
    private String campo11ModalidadeHoraExtra1;

    //Horas extras 2, no formato "hhmm"
    private String campo12HorasExtras2;

    //Percentual do adicional de horas extras 2,onde as 2 primeiras posições indicam a parte inteira e as 2 seguintes a fração decimal
    private String campo13PercentualHorasExtras2;

    //Modalidade da hora extra 2, assinalado com "D" se as horas extras forem diurnas e "N" se forem noturnas
    private String campo14ModalidadeHoraExtra2;

    //Horas extras 3, no formato "hhmm"
    private String campo15HorasExtras3;

    //Percentual do adicional de horas extras 3,onde as 2 primeiras posições indicam a parte inteira e as 2 seguintes a fração decimal
    private String campo16PercentualHorasExtras3;

    //Modalidade da hora extra 3, assinalado com "D" se as horas extras forem diurnas e "N" se forem noturnas
    private String campo17ModalidadeHorasExtras3;

    //Horas extras 4, no formato "hhmm"
    private String campo18HorasExtras4;

    //Percentual do adicional de horas extras 4,onde as 2 primeiras posições indicam a parte inteira e as 2 seguintes a fração decimal
    private String campo19PercentualHorasExtras4;

    //Modalidade da hora extra 4, assinalado com "D" se as horas extras forem diurnas e "N" se forem noturnas
    private String campo20ModalidadeHorasExtras4;

    //Horas de faltas e/ou atrasos
    private String campo21HorasFaltas;

    //Sinal de horas para compensar. "1" se for horas a maior e "2" se for horas a menor
    private String campo22SinalHorasCompensar;

    //Saldo de horas para compensar no formato "hhnn"
    private String campo23SaldoHorasCompensar;

    public ACJEF() {
        this.campo2TipoRegistro = "";
        this.campo3PIS = "";
        this.campo4InicioJornada = "";
        this.campo5PrimeiroHorarioJornada = "";
        this.campo6CH = "";
        this.campo7HorasDiurnas = "";
        this.campo8HorasNoturnas = "";
        this.campo9HorasExtras1 = "";
        this.campo10PercentualHorasExtras1 = "";
        this.campo11ModalidadeHoraExtra1 = "";
        this.campo12HorasExtras2 = "";
        this.campo13PercentualHorasExtras2 = "";
        this.campo14ModalidadeHoraExtra2 = "";
        this.campo15HorasExtras3 = "";
        this.campo16PercentualHorasExtras3 = "";
        this.campo17ModalidadeHorasExtras3 = "";
        this.campo18HorasExtras4 = "";
        this.campo19PercentualHorasExtras4 = "";
        this.campo20ModalidadeHorasExtras4 = "";
        this.campo21HorasFaltas = "";
        this.campo22SinalHorasCompensar = "";
        this.campo23SaldoHorasCompensar = "";
    }

    public String getCampo2TipoRegistro() {
        return campo2TipoRegistro;
    }

    public void setCampo2TipoRegistro(String campo2TipoRegistro) {
        this.campo2TipoRegistro = String.format("%1s", campo2TipoRegistro);
    }

    public String getCampo3PIS() {
        return campo3PIS;
    }

    public void setCampo3PIS(String campo3PIS) {
        this.campo3PIS = Utils.corrigePrecisaoNumero(campo5PrimeiroHorarioJornada, 12);
    }

    public String getCampo4InicioJornada() {
        return campo4InicioJornada;
    }

    public void setCampo4InicioJornada(String campo4InicioJornada) {
        this.campo4InicioJornada = Utils.corrigePrecisaoNumero( campo4InicioJornada, 8);
    }

    public String getCampo5PrimeiroHorarioJornada() {
        return Utils.corrigePrecisaoNumero(campo5PrimeiroHorarioJornada, 4);
    }

    public void setCampo5PrimeiroHorarioJornada(String campo5PrimeiroHorarioJornada) {
        this.campo5PrimeiroHorarioJornada = String.format("%4s", campo5PrimeiroHorarioJornada);
    }

    public String getCampo6CH() {
        return Utils.corrigePrecisaoNumero(campo6CH, 4);
    }

    public void setCampo6CH(Integer campo6) {
        this.campo6CH = Utils.corrigePrecisaoNumero(campo6.toString(), 4);
    }

    public String getCampo7HorasDiurnas() {
        return campo7HorasDiurnas;
    }

    public void setCampo7HorasDiurnas(String campo7HorasDiurnas) {
        this.campo7HorasDiurnas = Utils.corrigePrecisaoNumero(campo7HorasDiurnas, 4);
    }

    public String getCampo8HorasNoturnas() {
        return campo8HorasNoturnas;
    }

    public void setCampo8HorasNoturnas(String campo8HorasNoturnas) {
        this.campo8HorasNoturnas = Utils.corrigePrecisaoNumero(campo8HorasNoturnas, 4);
    }

    public String getCampo9HorasExtras1() {
        return Utils.corrigePrecisaoNumero(campo9HorasExtras1, 4);
    }

    public void setCampo9HorasExtras1(String campo9HorasExtras1) {
        this.campo9HorasExtras1 = Utils.corrigePrecisaoNumero(campo9HorasExtras1, 4);
    }

    public String getCampo10PercentualHorasExtras1() {
        return Utils.corrigePrecisaoNumero(campo10PercentualHorasExtras1, 4);
    }

    public void setCampo10PercentualHorasExtras1(Integer campo10) {
        this.campo10PercentualHorasExtras1 = Utils.corrigePrecisaoNumero(campo10.toString(), 4);
    }

    public String getCampo11ModalidadeHoraExtra1() {
        return Utils.corrigePrecisaoString(campo11ModalidadeHoraExtra1, 1, true);
    }

    public void setCampo11ModalidadeHoraExtra1(String campo11ModalidadeHoraExtra1) {
        this.campo11ModalidadeHoraExtra1 = String.format("%1s", campo11ModalidadeHoraExtra1);
    }

    public String getCampo12HorasExtras2() {
        return Utils.corrigePrecisaoNumero(campo12HorasExtras2, 4);
    }

    public void setCampo12HorasExtras2(String campo12HorasExtras2) {
        this.campo12HorasExtras2 = Utils.corrigePrecisaoNumero(campo12HorasExtras2, 4);
    }

    public String getCampo13PercentualHorasExtras2() {
        return Utils.corrigePrecisaoNumero(campo13PercentualHorasExtras2, 4);
    }

    public void setCampo13PercentualHorasExtras2(Integer campo13) {
        this.campo13PercentualHorasExtras2 = Utils.corrigePrecisaoNumero(campo13.toString(), 4);
    }

    public String getCampo14ModalidadeHoraExtra2() {
        return Utils.corrigePrecisaoString(campo14ModalidadeHoraExtra2, 1, true);
    }

    public void setCampo14ModalidadeHoraExtra2(String campo14ModalidadeHoraExtra2) {
        this.campo14ModalidadeHoraExtra2 = String.format("%1s", campo14ModalidadeHoraExtra2);
    }

    public String getCampo15HorasExtras3() {
        return Utils.corrigePrecisaoNumero(campo15HorasExtras3, 4);
    }

    public void setCampo15HorasExtras3(String campo15HorasExtras3) {
        this.campo15HorasExtras3 = Utils.corrigePrecisaoNumero(campo15HorasExtras3, 4);
    }

    public String getCampo16PercentualHorasExtras3() {
        return Utils.corrigePrecisaoNumero(campo16PercentualHorasExtras3, 4);
    }

    public void setCampo16PercentualHorasExtras3(Integer campo16) {
        this.campo16PercentualHorasExtras3 = Utils.corrigePrecisaoNumero(campo16.toString(), 4);
    }

    public String getCampo17ModalidadeHorasExtras3() {
        return Utils.corrigePrecisaoString(campo17ModalidadeHorasExtras3, 1, true);
    }

    public void setCampo17ModalidadeHorasExtras3(String campo17ModalidadeHorasExtras3) {
        this.campo17ModalidadeHorasExtras3 = String.format("%1s", campo17ModalidadeHorasExtras3);
    }

    public String getCampo18HorasExtras4() {
        return Utils.corrigePrecisaoNumero(campo18HorasExtras4, 4);
    }

    public void setCampo18HorasExtras4(String campo18HorasExtras4) {
        this.campo18HorasExtras4 = Utils.corrigePrecisaoNumero(campo18HorasExtras4, 4);
    }

    public String getCampo19PercentualHorasExtras4() {
        return Utils.corrigePrecisaoNumero(campo19PercentualHorasExtras4, 4);
    }

    public void setCampo19PercentualHorasExtras4(Integer campo19) {
        this.campo19PercentualHorasExtras4 = Utils.corrigePrecisaoNumero(campo19.toString(), 4);
    }

    public String getCampo20ModalidadeHorasExtras4() {
        return Utils.corrigePrecisaoString(campo20ModalidadeHorasExtras4, 1, true);
    }

    public void setCampo20ModalidadeHorasExtras4(String campo20ModalidadeHorasExtras4) {
        this.campo20ModalidadeHorasExtras4 = String.format("%1s", campo20ModalidadeHorasExtras4);
    }

    public String getCampo21HorasFaltas() {
        return campo21HorasFaltas;
    }

    public void setCampo21HorasFaltas(String campo21HorasFaltas) {
        this.campo21HorasFaltas = Utils.corrigePrecisaoNumero(campo21HorasFaltas, 4);
    }

    public String getCampo22SinalHorasCompensar() {
        return Utils.corrigePrecisaoNumero(campo22SinalHorasCompensar, 1);
    }

    public void setCampo22SinalHorasCompensar(Integer campo22) {
        this.campo22SinalHorasCompensar = String.format("%01d", campo22);
    }

    public String getCampo23SaldoHorasCompensar() {
        return Utils.corrigePrecisaoNumero(campo23SaldoHorasCompensar, 4);
    }

    public void setCampo23SaldoHorasCompensar(String campo23SaldoHorasCompensar) {
        this.campo23SaldoHorasCompensar = Utils.corrigePrecisaoNumero(campo23SaldoHorasCompensar, 4);
    }

}
