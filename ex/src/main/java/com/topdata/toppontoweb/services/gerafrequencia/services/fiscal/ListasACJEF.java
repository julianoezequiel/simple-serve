/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.topdata.toppontoweb.services.gerafrequencia.services.fiscal;

import com.topdata.toppontoweb.entity.jornada.Horario;
import com.topdata.toppontoweb.entity.jornada.HorarioMarcacao;
import com.topdata.toppontoweb.services.gerafrequencia.entity.fiscal.Arquivo;
import com.topdata.toppontoweb.services.gerafrequencia.entity.calculo.Calculo;
import com.topdata.toppontoweb.services.gerafrequencia.entity.calculo.SaidaDia;
import com.topdata.toppontoweb.services.gerafrequencia.entity.fiscal.ACJEF;
import com.topdata.toppontoweb.services.gerafrequencia.entity.fiscal.HorariosContratuaisACJEF;
import com.topdata.toppontoweb.services.gerafrequencia.entity.tabela.TabelaSequenciaPercentuais;
import com.topdata.toppontoweb.services.gerafrequencia.utils.CONSTANTES;
import com.topdata.toppontoweb.services.gerafrequencia.utils.Utils;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Campos linha arquivo ACJEF
 *
 * @author enio.junior
 */
public class ListasACJEF {

    private final List<Arquivo> horariosContratuaisACJEFList;
    private final List<Arquivo> ACJEFList;
    private final Calculo calculo;
    private ACJEF acjef;
    private HorariosContratuaisACJEF horariosContratuaisACJEF;
    private boolean grava;
    private int codigoHorario;
    
    private Horario horario;

    public ListasACJEF(Calculo calculo) {
        this.ACJEFList = new ArrayList<>();
        this.horariosContratuaisACJEFList = new ArrayList<>();
        this.calculo = calculo;
    }

    public List<Arquivo> getHorariosContratuaisACJEFList() {
        return this.horariosContratuaisACJEFList;
    }

    public List<Arquivo> getACJEFList() {
        return this.ACJEFList;
    }

    public void setInsereHorariosContratuaisACJEFList() {
        this.horariosContratuaisACJEF = new HorariosContratuaisACJEF();
        this.grava = true;

        if (this.calculo.getRegrasService().isCumprirHorario()) {
            this.horario = this.calculo.getFuncionarioService().getFuncionarioJornadaService().getHorarioDia();
            if (this.horario != null && this.horario.getHorarioMarcacaoList() != null &&  this.horario.getHorarioMarcacaoList().size() > 0) {

                this.horariosContratuaisACJEF.setCampo2TipoRegistro(CONSTANTES.EXPORTACAO_TIPOREGISTRO_2);
                
                //Filtra todos os horarios marcacoes que já foram utilizados
                List<Arquivo> existeHorario = this.horariosContratuaisACJEFList
                                    .stream()
                                    .filter(arquivo -> arquivo.getCodigoHorario() == this.horario.getIdHorario())
                                    .collect(Collectors.toList());
                
                if(existeHorario != null && existeHorario.size() > 0){//já existe o horario
                    this.grava = false;
                }else{
                    this.grava = true;
                    this.codigoHorario = this.horario.getIdHorario();
                    this.horariosContratuaisACJEF.setCampo3CH(this.codigoHorario);
                    String campo4Horarios = "";
                        
                    for (HorarioMarcacao horarioMarcacao : this.horario.getHorarioMarcacaoList()) {
                            campo4Horarios += Utils.FormatoHoraExportacao(horarioMarcacao.getHorarioEntrada());
                            campo4Horarios += Utils.FormatoHoraExportacao(horarioMarcacao.getHorarioSaida());
                    }
                    
                    this.horariosContratuaisACJEF.setCampo4Horarios(campo4Horarios);
                }
                
                if (this.grava) {
                    gravaLinhaHorariosContratuaisACJEF();
                }
            }
        }

    }

    public void setInsereACJEFList(SaidaDia saidaDia) {
        this.acjef = new ACJEF();
        this.acjef.setCampo2TipoRegistro(CONSTANTES.EXPORTACAO_TIPOREGISTRO_3);
        this.acjef.setCampo3PIS(this.calculo.getEntradaAPI().getFuncionario().getPis());

        if (this.calculo.getFuncionarioService()
                .getFuncionarioJornadaService().getJornadaDiaFuncionario().getIdJornadaFuncionario() != null) {

            this.acjef.setCampo4InicioJornada(Utils.FormatoDataExportacao(
                    this.calculo.getFuncionarioService().getFuncionarioJornadaService().getJornadaDiaFuncionario().getDataInicio()));

            if (this.horario != null && this.horario.getHorarioMarcacaoList() != null &&  this.horario.getHorarioMarcacaoList().size() > 0) {
                this.acjef.setCampo5PrimeiroHorarioJornada(Utils.FormatoHoraExportacao(this.horario.getHorarioMarcacaoList().get(0).getHorarioEntrada()));
                this.acjef.setCampo6CH(this.horario.getIdHorario());
            }
        }

        this.acjef.setCampo7HorasDiurnas(Utils.FormatoHoraExportacao(saidaDia.getSaldoNormais().getDiurnas()));
        this.acjef.setCampo8HorasNoturnas(Utils.FormatoHoraExportacao(saidaDia.getSaldoNormais().getNoturnas()));

        setExtrasList(saidaDia, this.acjef);

        this.acjef.setCampo21HorasFaltas(Utils.FormatoHoraExportacao(saidaDia.getSaldoAusencias()
                .getDiurnas().plus(saidaDia.getSaldoAusencias().getNoturnas())));

        if (calculo.getRegrasService().isBancoDeHoras()) {
            if (saidaDia.getBancodeHoras().getSaldoDia().isNegative()) {
                this.acjef.setCampo22SinalHorasCompensar(2);
            } else {
                this.acjef.setCampo22SinalHorasCompensar(1);
            }
            this.acjef.setCampo23SaldoHorasCompensar(Utils.FormatoHoraExportacao(saidaDia.getBancodeHoras().getSaldoDia()));
        }

        gravaLinhaACJEF();
    }

    private void setExtrasList(SaidaDia saidaDia, ACJEF acjef) {
        if (saidaDia.getTabelaExtrasList() != null && saidaDia.getTabelaExtrasList().size() > 0) {

            List<TabelaSequenciaPercentuais> extrasList = saidaDia.getTabelaExtrasList()
                    .stream()
                    .sorted(Comparator.comparing(TabelaSequenciaPercentuais::getIdSequencia))
                    .collect(Collectors.toList());

            TabelaSequenciaPercentuais extras1 = extrasList.get(0);
            acjef.setCampo10PercentualHorasExtras1(extras1.getAcrescimo().intValue());
            if(!extras1.getDivisaoDiurnas().isZero()){
                acjef.setCampo12HorasExtras2(Utils.FormatoHoraExportacao(extras1.getDivisaoDiurnas()));
                acjef.setCampo14ModalidadeHoraExtra2("D");
            }else if(!extras1.getDivisaoNoturnas().isZero()){
                acjef.setCampo12HorasExtras2(Utils.FormatoHoraExportacao(extras1.getDivisaoNoturnas()));
                acjef.setCampo14ModalidadeHoraExtra2("N");
            }

            if (extrasList.size() > 1) {
                TabelaSequenciaPercentuais extras2 = extrasList.get(1);
                acjef.setCampo13PercentualHorasExtras2(extras2.getAcrescimo().intValue());
                if(!extras2.getDivisaoDiurnas().isZero()){
                    acjef.setCampo12HorasExtras2(Utils.FormatoHoraExportacao(extras2.getDivisaoDiurnas()));
                    acjef.setCampo14ModalidadeHoraExtra2("D");
                }else if(!extras2.getDivisaoNoturnas().isZero()){
                    acjef.setCampo12HorasExtras2(Utils.FormatoHoraExportacao(extras2.getDivisaoNoturnas()));
                    acjef.setCampo14ModalidadeHoraExtra2("N");
                }
            }

            if (extrasList.size() > 2) {
                TabelaSequenciaPercentuais extras3 = extrasList.get(1);
                acjef.setCampo13PercentualHorasExtras2(extras3.getAcrescimo().intValue());
                if(!extras3.getDivisaoDiurnas().isZero()){
                    acjef.setCampo12HorasExtras2(Utils.FormatoHoraExportacao(extras3.getDivisaoDiurnas()));
                    acjef.setCampo14ModalidadeHoraExtra2("D");
                }else if(!extras3.getDivisaoNoturnas().isZero()){
                    acjef.setCampo12HorasExtras2(Utils.FormatoHoraExportacao(extras3.getDivisaoNoturnas()));
                    acjef.setCampo14ModalidadeHoraExtra2("N");
                }

            }

            if (extrasList.size() > 3) {
                TabelaSequenciaPercentuais extras4 = extrasList.get(1);
                acjef.setCampo13PercentualHorasExtras2(extras4.getAcrescimo().intValue());
                if(!extras4.getDivisaoDiurnas().isZero()){
                    acjef.setCampo12HorasExtras2(Utils.FormatoHoraExportacao(extras4.getDivisaoDiurnas()));
                    acjef.setCampo14ModalidadeHoraExtra2("D");
                }else if(!extras4.getDivisaoNoturnas().isZero()){
                    acjef.setCampo12HorasExtras2(Utils.FormatoHoraExportacao(extras4.getDivisaoNoturnas()));
                    acjef.setCampo14ModalidadeHoraExtra2("N");
                }
            }
        }
    }

    private void gravaLinhaHorariosContratuaisACJEF() {
        String linha = this.horariosContratuaisACJEF.getCampo2TipoRegistro()
                + this.horariosContratuaisACJEF.getCampo3CH()
                + this.horariosContratuaisACJEF.getCampo4Horarios();
        this.horariosContratuaisACJEFList.add(new Arquivo(0, this.codigoHorario, linha));
    }

    private void gravaLinhaACJEF() {
        String linha = this.acjef.getCampo2TipoRegistro()
                + this.acjef.getCampo3PIS()
                + this.acjef.getCampo4InicioJornada()
                + this.acjef.getCampo5PrimeiroHorarioJornada()
                + this.acjef.getCampo6CH()
                + this.acjef.getCampo7HorasDiurnas()
                + this.acjef.getCampo8HorasNoturnas()
                + this.acjef.getCampo9HorasExtras1()
                + this.acjef.getCampo10PercentualHorasExtras1()
                + this.acjef.getCampo11ModalidadeHoraExtra1()
                + this.acjef.getCampo12HorasExtras2()
                + this.acjef.getCampo13PercentualHorasExtras2()
                + this.acjef.getCampo14ModalidadeHoraExtra2()
                + this.acjef.getCampo15HorasExtras3()
                + this.acjef.getCampo16PercentualHorasExtras3()
                + this.acjef.getCampo17ModalidadeHorasExtras3()
                + this.acjef.getCampo18HorasExtras4()
                + this.acjef.getCampo19PercentualHorasExtras4()
                + this.acjef.getCampo20ModalidadeHorasExtras4()
                + this.acjef.getCampo21HorasFaltas()
                + this.acjef.getCampo22SinalHorasCompensar()
                + this.acjef.getCampo23SaldoHorasCompensar();
        this.ACJEFList.add(new Arquivo(0, linha));
    }

}
