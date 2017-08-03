package com.topdata.toppontoweb.dto.relatorios.cadastros;

import com.topdata.toppontoweb.entity.jornada.Horario;
import com.topdata.toppontoweb.services.relatorios.RelatorioHandler;

/**
 *
 * @author juliano.ezequiel
 */
public class RelatorioCadastroHorarioTransfer extends RelatorioCadastroTransfer {

    private Horario horario;

    public RelatorioCadastroHorarioTransfer() {
        super();
        this.horario = new Horario();
        this.tipo_relatorio = RelatorioHandler.TIPO_RELATORIO.CAD_HORARIO;
    }

    public RelatorioCadastroHorarioTransfer(RelatorioCadastroHorarioTransfer relatorioTransfer) {
        super(relatorioTransfer);
        this.horario = relatorioTransfer.horario;
        this.tipo_relatorio = RelatorioHandler.TIPO_RELATORIO.CAD_HORARIO;
    }

    /**
     * @return the horario
     */
    public Horario getHorario() {
        return horario;
    }

    /**
     * @param horario the horario to set
     */
    public void setHorario(Horario horario) {
        this.horario = horario;
    }

}
