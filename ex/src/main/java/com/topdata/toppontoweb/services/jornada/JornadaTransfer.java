package com.topdata.toppontoweb.services.jornada;

import com.topdata.toppontoweb.dto.jornada.HorarioTransfer;
import com.topdata.toppontoweb.entity.jornada.HorarioMarcacao;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * @version 1.0.0.0 data 18/01/2017
 * @since 1.0.0.0 data 27/10/2016
 *
 * @author juliano.ezequiel
 */
@Component
public class JornadaTransfer {

    private String descricao;
    private List<JornadaHorarioTransfer> jornadaHorarioList;

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public List<JornadaHorarioTransfer> getJornadaHorarioList() {
        return jornadaHorarioList;
    }

    public void setJornadaHorarioList(List<JornadaHorarioTransfer> jornadaHorarioList) {
        this.jornadaHorarioList = jornadaHorarioList;
    }

    public class JornadaHorarioTransfer {

        public JornadaHorarioTransfer() {
        }

        private List<HorarioTransfer> horarioList;

        public List<HorarioTransfer> getHorarioList() {
            return horarioList;
        }

        public void setHorarioList(List<HorarioTransfer> horarioList) {
            this.horarioList = horarioList;
        }

    }

}
