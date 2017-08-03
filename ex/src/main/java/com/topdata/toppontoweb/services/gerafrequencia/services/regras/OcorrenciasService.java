package com.topdata.toppontoweb.services.gerafrequencia.services.regras;

import com.topdata.toppontoweb.entity.jornada.HorarioMarcacao;
import com.topdata.toppontoweb.services.gerafrequencia.entity.calculo.SaidaDia;
import com.topdata.toppontoweb.services.gerafrequencia.entity.regras.Ocorrencia;
import com.topdata.toppontoweb.services.gerafrequencia.utils.Utils;
import java.util.Date;

/**
 * Ocorrências do dia de acordo com as marcações coletadas
 *
 * @author enio.junior
 */
public class OcorrenciasService {

    private final Ocorrencia ocorrencia;
    private int sequenciaTrabalhado;
    private int qtdeHorariosTrabalhadosXjornada;

    public OcorrenciasService() {
        this.ocorrencia = new Ocorrencia();
        
        //Quantidade de horários que devem ser verificados as ocorrências
        //Pois pode ter mais marcações trabalhadas, menos ou nenhuma.
        this.qtdeHorariosTrabalhadosXjornada = 0;
        
        //Tem que ter contador separado da jornada, pois a jornada semanal não bate
        //o idsequencia com o trabalhado
        this.sequenciaTrabalhado = 0;
    }

    public Ocorrencia getOcorrencias(SaidaDia saidaDia, Date toleranciaOcorrencia) {
        if (saidaDia.getHorariosPrevistosList() != null 
                && saidaDia.getHorariosTrabalhadosList() != null
                && toleranciaOcorrencia != null) {
                        
            if (saidaDia.getHorariosTrabalhadosList().size() > saidaDia.getHorariosPrevistosList().size()) {
                qtdeHorariosTrabalhadosXjornada = saidaDia.getHorariosPrevistosList().size();
            } else {
                qtdeHorariosTrabalhadosXjornada = saidaDia.getHorariosTrabalhadosList().size();
            }

            saidaDia.getHorariosPrevistosList()
                    .forEach((HorarioMarcacao horarioMarcacao) -> {
                        if (sequenciaTrabalhado < qtdeHorariosTrabalhadosXjornada) {
                            if (horarioMarcacao.getHorarioEntrada() != null && saidaDia.getHorariosTrabalhadosList().get(sequenciaTrabalhado).getHorarioEntrada() != null) {
                                setOcorrenciasEntrada(horarioMarcacao, saidaDia, toleranciaOcorrencia);
                                if (saidaDia.getHorariosTrabalhadosList().get(sequenciaTrabalhado).getHorarioSaida() != null) {
                                    setOcorrenciasSaida(horarioMarcacao, saidaDia, toleranciaOcorrencia);
                                }
                            }
                        }
                        sequenciaTrabalhado++;
                    });
        }
        return ocorrencia;
    }

    private void setOcorrenciasEntrada(HorarioMarcacao horarioMarcacao, SaidaDia saidaDia, Date toleranciaOcorrencia) {
        if (horarioMarcacao != null && saidaDia.getHorariosPrevistosList() != null && toleranciaOcorrencia != null) {
            if ((horarioMarcacao.getHorarioEntrada().getTime() - saidaDia.getHorariosTrabalhadosList().get(sequenciaTrabalhado).getHorarioEntrada().getTime()) > 0) {
                if (Antecipada(horarioMarcacao.getHorarioEntrada(), saidaDia.getHorariosTrabalhadosList().get(sequenciaTrabalhado).getHorarioEntrada(), toleranciaOcorrencia)) {
                    ocorrencia.setEntradaAntecipada(true);
                }
            } else if (AposHorario(horarioMarcacao.getHorarioEntrada(), saidaDia.getHorariosTrabalhadosList().get(sequenciaTrabalhado).getHorarioEntrada(), toleranciaOcorrencia)) {
                ocorrencia.setEntradaAtrasada(true);
            }
        }
    }

    private void setOcorrenciasSaida(HorarioMarcacao horarioMarcacao, SaidaDia saidaDia, Date toleranciaOcorrencia) {
        if (horarioMarcacao != null && saidaDia.getHorariosPrevistosList() != null && toleranciaOcorrencia != null) {
            if ((horarioMarcacao.getHorarioSaida().getTime() - saidaDia.getHorariosTrabalhadosList().get(sequenciaTrabalhado).getHorarioSaida().getTime()) > 0) {
                if (Antecipada(horarioMarcacao.getHorarioSaida(), saidaDia.getHorariosTrabalhadosList().get(sequenciaTrabalhado).getHorarioSaida(), toleranciaOcorrencia)) {
                    ocorrencia.setSaidaAntecipada(true);
                }
            } else if (AposHorario(horarioMarcacao.getHorarioSaida(), saidaDia.getHorariosTrabalhadosList().get(sequenciaTrabalhado).getHorarioSaida(), toleranciaOcorrencia)) {
                ocorrencia.setSaidaAposHorario(true);
            }
        }
    }

    public boolean Antecipada(Date marcacao1, Date marcacao2, Date toleranciaOcorrencia) {
        if (marcacao1 != null && marcacao2 != null && toleranciaOcorrencia != null) {
            return Utils.getUltrapassouTolerancia(Utils.FormatoDataHora(Utils.getIntervaloDuration(marcacao2, marcacao1)), toleranciaOcorrencia);
        }
        return false;
    }

    public boolean AposHorario(Date marcacao1, Date marcacao2, Date toleranciaOcorrencia) {
        if (marcacao1 != null && marcacao2 != null && toleranciaOcorrencia != null) {
            return Utils.getUltrapassouTolerancia(Utils.FormatoDataHora(Utils.getIntervaloDuration(marcacao1, marcacao2)), toleranciaOcorrencia);
        }
        return false;
    }
}
