package com.topdata.toppontoweb.services.gerafrequencia.services.fiscal;

import com.topdata.toppontoweb.entity.marcacoes.Marcacoes;
import com.topdata.toppontoweb.entity.rep.Rep;
import com.topdata.toppontoweb.services.gerafrequencia.entity.fiscal.AFDT;
import com.topdata.toppontoweb.services.gerafrequencia.entity.fiscal.Arquivo;
import com.topdata.toppontoweb.services.gerafrequencia.entity.calculo.Calculo;
import com.topdata.toppontoweb.services.gerafrequencia.entity.marcacoes.MarcacoesDia;
import com.topdata.toppontoweb.services.gerafrequencia.utils.CONSTANTES;
import com.topdata.toppontoweb.services.gerafrequencia.utils.Utils;
import java.util.ArrayList;
import java.util.List;

/**
 * Campos linha arquivo AFDT
 *
 * @author enio.junior
 */
public class ListaAFDT {

    private final List<Arquivo> AFDTList;
    private AFDT afdt;
    private int sequencia;
    private final Calculo calculo;

    public ListaAFDT(Calculo calculo) {
        this.AFDTList = new ArrayList<>();
        this.sequencia = 2;
        this.calculo = calculo;
    }

    public List<Arquivo> getAFDTList() {
        return this.AFDTList;
    }

    public void setInsere() {
        this.afdt = new AFDT();

        //genéricos
        this.afdt.setCampo2TipoRegistro(CONSTANTES.EXPORTACAO_TIPOREGISTRO_2);
        this.afdt.setCampo3DataMarcacao(Utils.FormatoDataExportacao(this.calculo.getDiaProcessado()));
        this.afdt.setCampo5PIS(this.calculo.getEntradaAPI().getFuncionario().getPis());

        setMarcacoesEntradaeSaida();
        setMarcacoesDesconsideradas();
    }

    private void setMarcacoesEntradaeSaida() {
        List<MarcacoesDia> marcacoesDias = calculo.getFuncionarioMarcacoesService().getMarcacoesJornadaDia(calculo.getDiaProcessado());
        if (marcacoesDias != null && marcacoesDias.size() > 0) {
            //Então possui marcação no dia para exportar
            marcacoesDias.stream()
                    .forEach((MarcacoesDia marcacoesDia) -> {
                        //grupo de entrada e saída
                        this.afdt.setCampo8NumeroSequencialFuncionario(marcacoesDia.getIdSequencia());
                        //marcações específicas
                        setMarcacaoEntrada(marcacoesDia);

                        //Não grava a inconsistência
                        if (marcacoesDia.getHorarioSaida() != null) {
                            setMarcacaoSaida(marcacoesDia);
                        }
                    });
        }
    }

    private void setMarcacoesDesconsideradas() {
        List<Marcacoes> marcacoesDesconsideradas = this.calculo.getFuncionarioMarcacoesService().getMarcacoesDesconsideradas(this.calculo.getDiaProcessado());
        if (marcacoesDesconsideradas.size() > 0) {
            //Então possui marcação desconsiderada no dia para exportar
            marcacoesDesconsideradas.stream()
                    .forEach((Marcacoes marcacoes) -> {
                        setMarcacaoDesconsiderada(marcacoes);
                    });
        }
    }

    private void setMarcacaoEntrada(MarcacoesDia marcacoesDia) {
        this.afdt.setCampo1Sequencial(this.sequencia);
        this.afdt.setCampo4HoraMarcacao(Utils.FormatoHoraExportacao(marcacoesDia.getHorarioEntrada()));
        this.afdt.setCampo6NumeroRep(marcacoesDia.getNumeroSerieEntrada());
        this.afdt.setCampo7TipoMarcacao(CONSTANTES.LEGENDA_MARCACAO_STATUS_ENTRADA);
        this.afdt.setCampo9TipoRegistro(getStatusMarcacao(marcacoesDia.getStatusMarcacaoEntrada().getDescricao()));
        //Somente adiciona motivo se for uma marcacao incluida
        if(marcacoesDia.getStatusMarcacaoEntrada().getDescricao().equals(CONSTANTES.MARCACAO_INCLUIDA)){
            this.afdt.setCampo10Motivo(marcacoesDia.getMotivoStatusEntrada());
        }
        gravaMarcacao();
    }

    private void setMarcacaoSaida(MarcacoesDia marcacoesDia) {
        this.afdt.setCampo1Sequencial(this.sequencia);
        this.afdt.setCampo4HoraMarcacao(Utils.FormatoHoraExportacao(marcacoesDia.getHorarioSaida()));
        this.afdt.setCampo6NumeroRep(marcacoesDia.getNumeroSerieSaida());
        this.afdt.setCampo7TipoMarcacao(CONSTANTES.LEGENDA_MARCACAO_STATUS_SAIDA);
        this.afdt.setCampo9TipoRegistro(getStatusMarcacao(marcacoesDia.getStatusMarcacaoSaida().getDescricao()));
        if(marcacoesDia.getStatusMarcacaoEntrada().getDescricao().equals(CONSTANTES.MARCACAO_INCLUIDA)){
            this.afdt.setCampo10Motivo(marcacoesDia.getMotivoStatusEntrada());
        }
        gravaMarcacao();
    }
    
    private String configurarSerieCompleta(Marcacoes marcacoes){
        String serieCompleta = "00000000000000000";
        Rep rep = marcacoes.getRep();
        if (rep.getTipoEquipamento().getIdTipoEquipamento()
                != com.topdata.toppontoweb.utils.constantes.CONSTANTES.Enum_TIPO_EQUIPAMENTO.OUTRO.ordinal()) {
            
            if (rep.getTipoEquipamento().getIdTipoEquipamento()
                    == com.topdata.toppontoweb.utils.constantes.CONSTANTES.Enum_TIPO_EQUIPAMENTO.INNER.ordinal()) {
                serieCompleta = com.topdata.toppontoweb.utils.Utils.corrigePrecisaoNumero(rep.getNumeroSerie(), 17);
            }else{
                serieCompleta = rep.getNumeroFabricante();
                serieCompleta += rep.getNumeroModelo();
                serieCompleta += com.topdata.toppontoweb.utils.Utils.corrigePrecisaoNumero(rep.getNumeroSerie(), 7);
            }
            
        }
        
        return serieCompleta;
    }

    private void setMarcacaoDesconsiderada(Marcacoes marcacoes) {
        this.afdt.setCampo1Sequencial(this.sequencia);
        this.afdt.setCampo4HoraMarcacao(Utils.FormatoHoraExportacao(marcacoes.getDataHora()));
        this.afdt.setCampo6NumeroRep(configurarSerieCompleta(marcacoes));
        this.afdt.setCampo7TipoMarcacao(CONSTANTES.LEGENDA_DESCONSIDERADA);
        this.afdt.setCampo8NumeroSequencialFuncionario(0); //Não tem número seqüencial
        this.afdt.setCampo9TipoRegistro(getStatusMarcacao(marcacoes.getIdStatusMarcacao().getDescricao()));
        //Sempre adiciona o motivo para marcacoes desconcideradas
        this.afdt.setCampo10Motivo(marcacoes.getMotivo() != null? marcacoes.getMotivo().getDescricao() : "");
        gravaMarcacao();
    }

    private String getStatusMarcacao(String status) {
        switch (status) {
            case "Original":
                return CONSTANTES.LEGENDA_ORIGINAL;
            case "Incluída":
                return CONSTANTES.LEGENDA_INCLUIDA;
            case "Pré-assinalada":
                return CONSTANTES.LEGENDA_PREASSINALADA;
            default:
                return "";
        }
    }

    private void gravaMarcacao() {
        String linha = afdt.getCampo1Sequencial()
                + afdt.getCampo2TipoRegistro()
                + afdt.getCampo3DataMarcacao()
                + afdt.getCampo4HoraMarcacao()
                + afdt.getCampo5PIS()
                + afdt.getCampo6NumeroRep()
                + afdt.getCampo7TipoMarcacao()
                + afdt.getCampo8NumeroSequencialFuncionario()
                + afdt.getCampo9TipoRegistro()
                + afdt.getCampo10Motivo();
        this.AFDTList.add(new Arquivo(this.sequencia, linha));
        this.sequencia = this.sequencia + 1;
    }

}
