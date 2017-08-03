package com.topdata.toppontoweb.services.gerafrequencia.entity.marcacoes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Marcações coletadas (trabalhadas)
 *
 * @author enio.junior
 */
public class MarcacoesList {

    private Date dia;
    private List<MarcacoesDia> marcacoesDiasList;

    public MarcacoesList() {
        this.marcacoesDiasList = new ArrayList<>();
    }

    public Date getDia() {
        return dia;
    }

    public void setDia(Date dia) {
        this.dia = dia;
    }

    public List<MarcacoesDia> getMarcacaoList() {
        return marcacoesDiasList;
    }

    public void setMarcacaoList(List<MarcacoesDia> marcacoesDiasList) {
        this.marcacoesDiasList = marcacoesDiasList;
    }

}
