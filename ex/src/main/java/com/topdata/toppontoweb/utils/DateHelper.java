package com.topdata.toppontoweb.utils;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 *
 * @author juliano.ezequiel
 */
public class DateHelper {

    /**
     * Adiciona a quantidade de dias a uma data
     *
     * @param dateBase
     * @param qtdDay
     * @return
     */
    public static Date addDay(Date dateBase, int qtdDay) {
        if (dateBase instanceof java.sql.Date) {
            dateBase = new Date(dateBase.getTime());
        }
        LocalDateTime ldt = LocalDateTime.ofInstant(dateBase.toInstant(), ZoneId.systemDefault());
        ldt.plusDays(qtdDay);
        return Date.from(ldt.toInstant(ZoneOffset.UTC));
    }

    public static Date ajustarData(Date dateBase) {
        GregorianCalendar calendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        calendar.setTimeInMillis(dateBase.getTime());
        return calendar.getTime();
    }

    public static Date DurationToDate(Duration data) {
        if (data != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date(0));
            calendar.add(Calendar.MINUTE, (int) data.toMinutes());
            return calendar.getTime();
        }
        return null;
    }

    public static Date minusHours(Date dateBase, int qtdHours) {
        if (dateBase instanceof java.sql.Date) {
            dateBase = new Date(dateBase.getTime());
        }
        LocalDateTime ldt = LocalDateTime.ofInstant(dateBase.toInstant(), ZoneId.systemDefault());
        ldt.minusHours(qtdHours);
        return Date.from(ldt.toInstant(ZoneOffset.UTC));
    }

    public static boolean ValidarDatasPeriodo(Date dataInicio1, Date dataTermino1, Date dataInicio2, Date dataTermino2) {

        return (dataInicio1.compareTo(dataInicio2) <= 0 && dataTermino1.compareTo(dataInicio2) >= 0)
                || (dataTermino1.compareTo(dataTermino2) >= 0 && dataInicio1.compareTo(dataTermino2) <= 0)
                || (dataInicio2.compareTo(dataInicio1) <= 0 && dataTermino2.compareTo(dataInicio1) >= 0)
                || (dataTermino2.compareTo(dataTermino1) >= 0 && dataInicio2.compareTo(dataTermino1) <= 0);

    }

    public static Date Max() {
        return new Date("2100/01/01");
    }

    public static Date DataPadrao() {
//        GregorianCalendar gc = new  GregorianCalendar(1970, Calendar.JANUARY, 1);
//        return gc.getTime();
        return new Date(0);
    }

}
