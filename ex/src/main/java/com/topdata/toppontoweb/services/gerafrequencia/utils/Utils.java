package com.topdata.toppontoweb.services.gerafrequencia.utils;

import com.topdata.toppontoweb.entity.configuracoes.SequenciaPercentuais;
import com.topdata.toppontoweb.services.gerafrequencia.entity.saldo.Saldo;
import com.topdata.toppontoweb.services.gerafrequencia.entity.saldo.SaldoDiaCompensado;
import com.topdata.toppontoweb.services.gerafrequencia.entity.saldo.SaldoDiaCompensando;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Classe utils do gerafrequencia
 *
 * @author enio.junior
 */
public class Utils {

    private static Calendar calendar;
    private static Date data;

    /**
     * Retorna formato 00:00:00 em string
     *
     * @param data
     * @return
     */
    public static String FormatoHora(Duration data) {
        if (data != null && data != Duration.ZERO) {
            return data.toHours() + new SimpleDateFormat(":mm:ss").format(new Date(data.toMillis()));
        }
        return CONSTANTES.ZERO_HORA;
    }

    /**
     * Retorna formato 000.0 em double
     *
     * @param horasDeveriaTrabalhar
     * @param horasNaoTrabalhadas
     * @return
     */
    public static Double getPercentualAbsenteismo(Duration horasDeveriaTrabalhar, Duration horasNaoTrabalhadas) {
        if (horasDeveriaTrabalhar == null) {
            horasDeveriaTrabalhar = Duration.ZERO;
        }
        if (horasNaoTrabalhadas == null) {
            horasNaoTrabalhadas = Duration.ZERO;
        }
        Double h1 = (double) horasDeveriaTrabalhar.toMillis();
        Double h2 = (double) horasNaoTrabalhadas.toMillis();
        Double res = (h2 / h1) * 100;
        return res;
    }

    /**
     * Retorna formato dd-mm-yyyy em string
     *
     * @param data
     * @return
     */
    public static String FormatoData(Date data) {
        return (data != null) ? new SimpleDateFormat("dd-MM-yyyy").format(data) : "";
    }

    /**
     * Retorna formato ddmmyyyy em string
     *
     * @param data
     * @return
     */
    public static String FormatoDataExportacao(Date data) {
        return (data != null) ? new SimpleDateFormat("ddMMyyyy").format(data) : "00000000";
    }

    /**
     * Retorna formato hhmm em string
     *
     * @param data
     * @return
     */
    public static String FormatoHoraExportacao(Date data) {
        return (data != null) ? new SimpleDateFormat("HHmm").format(data) : "0000";
    }

    /**
     * Retorna formato hhmm em string
     *
     * @param data
     * @return
     */
    public static String FormatoHoraExportacao(Duration data) {
        if (data == null) {
            data = Duration.ZERO;
        }

        return data.toHours() + new SimpleDateFormat("mm").format(new Date(data.toMillis()));
    }

    /**
     * Retorna formato Thu Jan 01 05:00:00 BRT 1970 como data
     *
     * @param data
     * @param quantidade
     * @return
     */
    public static Date AdicionaDiasData(Date data, Integer quantidade) {
        if (data != null) {
            calendar = Calendar.getInstance();
            calendar.setTime(data);
            calendar.add(Calendar.DAY_OF_MONTH, quantidade);
            return calendar.getTime();
        }
        return null;
    }

    /**
     * Retorna formato Thu Jan 01 05:00:00 BRT 1970 como data
     *
     * @param data
     * @param quantidade
     * @return
     */
    public static Date DiminuiDiasData(Date data, Integer quantidade) {
        if (data != null) {
            calendar = Calendar.getInstance();
            calendar.setTime(data);
            calendar.add(Calendar.DAY_OF_MONTH, -quantidade);
            return calendar.getTime();
        }
        return null;
    }

    /**
     * Retorna formato Thu Jan 01 05:00:00 BRT 1970 como data
     *
     * @param data1
     * @param data2
     * @return
     */
    public static Date getIntervaloHoras(Date data1, Date data2) {
        if (data1 != null && data2 != null) {
            if ((data1.getTime() - data2.getTime()) > 0) {
                return FormatoDataHora(Duration.between(data2.toInstant(), data1.toInstant()));
            } else {
                return FormatoDataHora(Duration.between(data1.toInstant(), data2.toInstant()));
            }
        }
        return null;
    }

    /**
     * Retorna formato PT0H em duration
     *
     * @param data1
     * @param data2
     * @return
     */
    public static Duration getIntervaloDuration(Date data1, Date data2) {
        if (data1 != null && data2 != null) {
            return Duration.between(data1.toInstant(), data2.toInstant());
        }
        return Duration.ZERO;
    }

    /**
     * Retorna formato PT1H8M34.285S em duration
     *
     * @param horas
     * @param percentual
     * @return
     */
    public static Duration getAcrescimoPercentualAdicionalNoturno(Duration horas, Double percentual) {
        if (horas != null && percentual != null) {
            if (horas.toMillis() > 0) {
                percentual = (percentual / 100) + 1;
                return Duration.ofMillis(new BigDecimal(horas.toMillis()).multiply(new BigDecimal(percentual)).longValue());
            } else {
                return Duration.ZERO;
            }
        }
        return Duration.ZERO;
    }

    /**
     * Retorna formato PT1H8M34.285S em duration
     *
     * @param horas
     * @param percentual
     * @return
     */
    public static Duration getAcrescimoPercentualTabelasPercentuais(Duration horas, Double percentual) {
        if (horas != null && percentual != null) {
            if (horas.toMillis() > 0) {
                percentual = (percentual / 10000) + 1;
                return Duration.ofMillis(new BigDecimal(horas.toMillis()).multiply(new BigDecimal(percentual)).longValue());
            } else {
                return Duration.ZERO;
            }
        }
        return Duration.ZERO;
    }

    /**
     * Retorna formato PT13M0.427S em duration
     *
     * @param horas
     * @return
     */
    public static Duration getArredondamento(Duration horas) {
        if (horas != null) {
            long segundos = Long.parseLong(new SimpleDateFormat("ss").format(new Date(horas.toMillis())));
            if (segundos > 30) {
                horas = horas.plusMinutes(1);
            }
            horas = horas.minusSeconds(segundos);
            return horas;
        }
        return Duration.ZERO;
    }

    public static Duration getSomenteHorasMinutos(Duration horas) {
        if (horas != null) {
            long segundos = Long.parseLong(new SimpleDateFormat("ss").format(new Date(horas.toMillis())));
            return horas.minusSeconds(segundos);
        }
        return Duration.ZERO;
    }

    /**
     * Retorna formato PT1H8M34.285S em duration
     *
     * @param horas
     * @param diasUteis
     * @param domingos
     * @param feriados
     * @return
     */
    public static Duration getCalculoDsr(Duration horas, Integer diasUteis, Integer domingos, Integer feriados) {
        //(valor / dias uteis) * (domingos + feriados) 
        if (horas != null && horas.toMillis() > 0) {
            BigDecimal bigDecimal = new BigDecimal(horas.toMillis());
            if (diasUteis > 0) {
                bigDecimal = bigDecimal.divide(new BigDecimal(diasUteis), 3, RoundingMode.HALF_EVEN);
            }
            if (domingos + feriados > 0) {
                return Duration.ofMillis(bigDecimal.longValue()).multipliedBy(domingos + feriados);
            }
            return Duration.ofMillis(bigDecimal.longValue());
        }
        return Duration.ZERO;
    }

    /**
     * Retorna formato Thu Jan 01 05:00:00 BRT 1970 como data
     *
     * @return
     */
    public static Date getDataBase() {
        data = getDataDefault();
        return new Calendar.Builder()
                .setDate(data.getYear() + 1900, data.getMonth(), data.getDate())
                .setTimeOfDay(0, 0, 0)
                .build()
                .getTime();
    }

    /**
     * Retorna formato PT0H em duration
     *
     * @param data
     * @return
     */
    public static Duration getHorasAcima24horas(Instant data) {
        if (data != null) {
            return Duration.between(getDataBase().toInstant(), data);
        }
        return Duration.ZERO;
    }

    /**
     * Retorna true ou false
     *
     * @param saldo
     * @param horasTolerancia
     * @return
     */
    public static boolean getUltrapassouTolerancia(Saldo saldo, Instant horasTolerancia) {
        if (saldo != null && horasTolerancia != null) {
            return saldo.getDiurnas().plus(saldo.getNoturnas()).toMillis() > getHorasAcima24horas(horasTolerancia).toMillis();
        }
        return false;
    }

    /**
     * Retorna true ou false
     *
     * @param saldo
     * @param horasTolerancia
     * @return
     */
    public static boolean getUltrapassouTolerancia(Date saldo, Date horasTolerancia) {
        if (saldo != null && horasTolerancia != null) {
            return saldo.getTime() > horasTolerancia.getTime();
        }
        return false;
    }

    /**
     * Retorna formato PT0H em duration
     *
     * @param possuiBancoDeHoras
     * @param diferencaAdicionalNoturno
     * @return
     */
    public static Duration getVisualizaDiferencaAdicionalNoturnoBancoDeHoras(boolean possuiBancoDeHoras, Duration diferencaAdicionalNoturno) {
        return (possuiBancoDeHoras) ? diferencaAdicionalNoturno : Duration.ZERO;
    }

    /**
     * Retorna true ou false
     *
     * @param horario
     * @param horariosAtualizadosList
     * @return
     */
    public static boolean getVerificaHorarioNaoAtualizado(Integer horario, List<Integer> horariosAtualizadosList) {
        if (horariosAtualizadosList != null && horario != null) {
            return horariosAtualizadosList.stream()
                    .filter(f1 -> f1.intValue() == horario)
                    .findAny().orElse(0) == 0;
        }
        return false;
    }

    /**
     * Retorna true ou false
     *
     * @param horario
     * @param percentuaisAtualizadosList
     * @return
     */
    public static boolean getVerificaPercentualNaoAtualizado(Integer horario, List<Integer> percentuaisAtualizadosList) {
        if (percentuaisAtualizadosList != null && horario != null) {
            return percentuaisAtualizadosList.stream()
                    .filter(f1 -> f1.intValue() == horario)
                    .findAny().orElse(0) == 0;
        }
        return false;
    }

    /**
     * Padrão datas Main Retorna formato Thu Jan 01 05:00:00 BRT 1970 como data
     *
     * @param horas
     * @param minutos
     * @return
     */
    public static Date setDatasMain(Integer horas, Integer minutos) {
        calendar = Calendar.getInstance();
        calendar.setTime(getDataDefault());
        calendar.add(Calendar.HOUR_OF_DAY, horas);
        calendar.add(Calendar.MINUTE, minutos);
        return calendar.getTime();
    }

    /**
     * Padrão datas API Retorna formato Thu Jan 01 05:00:00 BRT 1970 como data
     *
     * @param data
     * @return
     */
    public static Date getDataAjustaDiferenca3horas(Date data) {
        if (data != null) {
            data = setDatasMain(getHorasDataDiferenca3horas(data), data.getMinutes());
            calendar = Calendar.getInstance();
            calendar.setTime(getDataBase());
            return new Date(data.getTime() + calendar.getTime().getTime());
        }
        return null;
    }

    /**
     * Retorna formato Thu Jan 01 05:00:00 BRT 1970 como data
     *
     * @param data
     * @return
     */
    public static Date FormatoDataHora(Duration data) {
        if (data != null) {
            calendar = Calendar.getInstance();
            calendar.setTime(getDataBase());
            calendar.add(Calendar.MINUTE, (int) data.toMinutes());
            return calendar.getTime();
        }
        return null;
    }

    public static Integer RetornaDiadaSemana(Date data) {
        if (data != null) {
            calendar = Calendar.getInstance();
            calendar.setTime(data);
            return calendar.get(Calendar.DAY_OF_WEEK) - 1;
        }
        return 0;
    }

    /**
     * Retorna formato Thu Jan 01 05:00:00 BRT 1970 como data
     *
     * @param dia
     * @param data
     * @return
     */
    public static Date getViradaDia(Integer dia, Date data) {
        if (dia == 1) {
            data = AdicionaDiasData(data, dia);
        }
        return data;
    }

    /**
     * Retorna formato Thu Jan 01 05:00:00 BRT 1970 como data
     *
     * @param dia
     * @param data
     * @return
     */
    public static Date getDataPadrao(Integer dia, Date data) {
        if (data != null) {
            calendar = Calendar.getInstance();
            calendar.setTime(getDataBase());
            calendar.add(Calendar.DAY_OF_MONTH, dia);
            calendar.add(Calendar.HOUR_OF_DAY, data.getHours());
            calendar.add(Calendar.MINUTE, data.getMinutes());
            return calendar.getTime();
        }
        return null;
    }

    /**
     * Retorna formato inteiro
     *
     * @param data
     * @return
     */
    public static Integer getHorasDataDiferenca3horas(Date data) {
        if (data != null) {
            calendar = Calendar.getInstance();
            calendar.setTime(data);
            calendar.setTimeZone(TimeZone.getTimeZone("Etc/GMT0"));
            return (int) Duration.between(getDataDefault().toInstant(), calendar.getTime().toInstant())
                    .toHours();
        }
        return 0;
    }

    /**
     * Retorna formato January 1, 1970 00:00:00.000 GMT (Gregorian) em calendar
     *
     * @param data
     * @return
     */
    public static Calendar getMenorData(Date data) {
        if (data != null) {
            calendar = Calendar.getInstance();
            calendar.setTime(data);
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
            return calendar;
        }
        return null;
    }

    /**
     * Retorna formato January 1, 1970 00:00:00.000 GMT (Gregorian) em calendar
     *
     * @param data
     * @param menorData
     * @return
     */
    public static Calendar getMaiorData(Date data, Calendar menorData) {
        if (data != null && menorData != null) {
            calendar = Calendar.getInstance();
            calendar.setTime(data);
            calendar.set(Calendar.DAY_OF_MONTH, menorData.getActualMaximum(Calendar.DAY_OF_MONTH));
            return calendar;
        }
        return null;
    }

    /**
     * Retorna formato inteiro
     *
     * @param dia1
     * @param dia2
     * @return
     */
    public static Integer getDiferencaData(Date dia1, Date dia2) {
        if (dia1 != null && dia2 != null) {
            Comparator<Date> comparatorDia = (Date o1, Date o2) -> new Integer(o1.getDate()).compareTo(o2.getDate());
            Comparator<Date> comparatorMes = (Date o1, Date o2) -> new Integer(o1.getMonth()).compareTo(o2.getMonth());
            Comparator<Date> comparatorAno = (Date o1, Date o2) -> new Integer(o1.getYear()).compareTo(o2.getYear());
            if (comparatorDia.compare(dia1, dia2) == 0
                    && comparatorMes.compare(dia1, dia2) == 0
                    && comparatorAno.compare(dia1, dia2) == 0) {
                return 0;
            } else if (comparatorDia.compare(dia1, dia2) == 1
                    || comparatorMes.compare(dia1, dia2) == 1
                    || comparatorAno.compare(dia1, dia2) == 1) {
                return 1;
            } else {
                return -1;
            }
        }
        return 0;
    }

    /**
     * Retorna formato PT1H8M34.285S em duration
     *
     * @param horas
     * @param percentual
     * @return
     */
    public static Duration getRetirarPercentual(Duration horas, Double percentual) {
        if (horas != null && percentual != null) {
            if (horas.toMillis() > 0) {
                percentual = (percentual / 100) + 1;
                return Duration.ofMillis(new BigDecimal(horas.toMillis()).divide(new BigDecimal(percentual), 3, RoundingMode.HALF_EVEN).longValue());
            } else {
                return Duration.ZERO;
            }
        }
        return Duration.ZERO;
    }

    /**
     * Retorna formato Thu Jan 01 05:00:00 BRT 1970 como data
     *
     * @return
     */
    public static Date getDataDefault() {
        return new Date(0);
    }

    /**
     * Retorna true ou false
     *
     * @param data
     * @return
     */
    public static boolean getDiaUtil(Date data) {
        if (data != null) {
            calendar = Calendar.getInstance();
            calendar.setTime(data);
            return !(calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
                    || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY);
        }
        return false;
    }

    /**
     * Retorna formato inteiro o dia da semana
     *
     * @param data
     * @return
     */
    public static Integer getDiaDaSemana(Date data) {
        if (data != null) {
            calendar = Calendar.getInstance();
            calendar.setTime(data);
            return calendar.get(Calendar.DAY_OF_WEEK);
        }
        return 0;
    }

    /**
     * Retorna o 1º dia do mês em formato data
     *
     * @param data
     * @return
     */
    public static Date getDia1Mes(Date data) {
        if (data != null) {
            return new Calendar.Builder()
                    .setDate(data.getYear() + 1900, data.getMonth(), 1)
                    .setTimeOfDay(0, 0, 0)
                    .build()
                    .getTime();
        }
        return null;
    }

    /**
     * Retorna formato long
     *
     * @param data1
     * @param data2
     * @return
     */
    public static long getQuantidadeDias(Date data1, Date data2) {
        if (data1 != null && data2 != null) {
            data2 = new Timestamp(data2.getTime());
            data1 = new Timestamp(data1.getTime());
            if ((data1.getTime() - data2.getTime()) > 0) {
                return Duration.between(data2.toInstant(), data1.toInstant()).toDays();
            } else {
                return Duration.between(data1.toInstant(), data2.toInstant()).toDays();
            }
        }
        return 0;
    }

    /**
     * Retorna as informações do dia que está compensando
     *
     * @param dia
     * @param saldoDiaCompensandoList
     * @return
     */
    public static SaldoDiaCompensando getSaldoDiaCompensando(Date dia, List<? extends SaldoDiaCompensando> saldoDiaCompensandoList) {
        return saldoDiaCompensandoList
                .stream()
                .filter(f -> f.getDataCompensando().compareTo(dia) == 0)
                .findFirst()
                .orElse(null);
    }

    /**
     * Retorna as informações do dia compensado
     *
     * @param dia
     * @param saldoDiaCompensadoList
     * @return
     */
    public static SaldoDiaCompensado getSaldoDiaCompensado(Date dia, List<? extends SaldoDiaCompensado> saldoDiaCompensadoList) {
        return saldoDiaCompensadoList
                .stream()
                .filter(f -> f.getDataCompensada().compareTo(dia) == 0)
                .findFirst()
                .orElse(null);
    }

    /**
     * Ordenação padrão das listas de sequencia percentuais
     */
    public static class ordenaSequencia implements Comparator<SequenciaPercentuais> {

        @Override
        public synchronized int compare(SequenciaPercentuais o1, SequenciaPercentuais o2) {
            return o1.getSequenciaPercentuaisPK().compareTo(o2.getSequenciaPercentuaisPK());
        }

    }

    public static Date formatoDataSemHorario(Date data) {
        if (data != null) {
            return new Calendar.Builder()
                    .setDate(data.getYear(), data.getMonth(), data.getDate())
                    .setTimeOfDay(0, 0, 0)
                    .build()
                    .getTime();
        }
        return null;
    }

    /**
     * Uso somente da função main
     *
     * @param res
     * @return
     */
    public static String FormatoPercentualMain(Double res) {
        return res + "%";
    }

    public static String FormatoHoraMain(Date data) {
        return (data != null) ? new SimpleDateFormat("HH:mm:ss").format(data) : "";
    }

    public static Duration getVisualizaDiferencaAdicionalNoturnoHoraExtraMain(boolean possuiBancoDeHoras, Duration diferencaAdicionalNoturno) {
        return (possuiBancoDeHoras == false) ? diferencaAdicionalNoturno : Duration.ZERO;
    }

    public static Integer getHorasMain(String hora) {
        if (hora != null && !"".equals(hora)) {
            if (hora.length() == 5) {
                return Integer.parseInt(hora.substring(0, 2));
            } else {
                //998:00
                return Integer.parseInt(hora.substring(0, 3));
            }
        }
        return 0;
    }

    public static Integer getMinutosMain(String hora) {
        if (hora != null && !"".equals(hora)) {
            if (hora.length() == 5) {
                return Integer.parseInt(hora.substring(3, 5));
            } else {
                //998:00
                return Integer.parseInt(hora.substring(4, 6));
            }
        }
        return 0;
    }

}
