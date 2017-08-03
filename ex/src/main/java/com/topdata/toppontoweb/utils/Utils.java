package com.topdata.toppontoweb.utils;

import com.topdata.toppontoweb.dto.EntidadeRetorno;
import com.topdata.toppontoweb.dto.MsgRetorno;
import com.topdata.toppontoweb.dto.relatorios.TabelaAgrupada;
import com.topdata.toppontoweb.entity.configuracoes.PercentuaisAcrescimo;
import com.topdata.toppontoweb.entity.configuracoes.SequenciaPercentuais;
import com.topdata.toppontoweb.entity.jornada.HorarioMarcacao;
import com.topdata.toppontoweb.entity.tipo.TipoDia;
import com.topdata.toppontoweb.entity.tipo.TipoDocumento;
import com.topdata.toppontoweb.services.gerafrequencia.entity.bancodehoras.TabelaCreditoSaldoUnico;
import com.topdata.toppontoweb.services.gerafrequencia.entity.calculo.SaidaDia;
import com.topdata.toppontoweb.services.gerafrequencia.entity.marcacoes.MarcacoesDia;
import com.topdata.toppontoweb.services.gerafrequencia.entity.tabela.TabelaSequenciaPercentuais;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;
import com.topdata.toppontoweb.utils.constantes.Regex;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;

/**
 *
 * @author juliano.ezequiel
 */
public class Utils {
    
    public static Date convertStringToDate(String dataFormatoBrasil){
        try{
            //Extrai a data
            String strData = dataFormatoBrasil.replaceAll("^.*(\\d+\\/\\d+\\/\\d{4}).*$", "$1");
            return convertStringToDate(strData, "dd/MM/yyyy");
        }catch(ParseException ex){
            //Deu algum erro, então não é o formato correto
            return null;
        }
    }

    public static String ignorarZerosAEsquerda(String numero) {
        return numero.replaceAll("^0+", "");
    }

    public static String corrigePrecisaoNumero(String numero, int precisao) {
        return  StringUtils.leftPad(numero, precisao, "0");
    }

    public static String corrigePrecisaoString(String palavra, int precisao, boolean inserirDireita) {
        String resultado = palavra;
        
        if (inserirDireita) {
            resultado = StringUtils.rightPad(palavra, precisao, " ");
        } else {
            resultado = StringUtils.leftPad(palavra, precisao, " ");
        }

        return resultado;
    }

    public static String converterStringParaUTF8(String palavra) {
        byte[] bytes = palavra.getBytes();
        return new String(bytes, Charset.forName("UTF-8"));
    }

    public enum LINK {
        PROTOCOLO,
        IP,
        INSTANCIA
    }

    //Retorna a url que será enviado por emal para o operador
    public static String getLinkValido(HttpServletRequest request, String Link, String ipServidor2, String habilitarServidor2) {
        String linkFinal;
        if (Boolean.parseBoolean(habilitarServidor2.replace(" ", "")) == true) {
            linkFinal = ipServidor2 + Link;
        } else {
            String[] uri = request.getRequestURL().toString().split("/");
            HashMap<Utils.LINK, String> h = new HashMap<>();
            h.put(Utils.LINK.PROTOCOLO, uri[0]);
            h.put(Utils.LINK.IP, uri[2]);
            h.put(Utils.LINK.INSTANCIA, uri[3]);

            linkFinal = h.get(Utils.LINK.PROTOCOLO).concat("//").concat(h.get(Utils.LINK.IP)).concat("/").concat(h.get(Utils.LINK.INSTANCIA)).concat(Link);
        }
        return linkFinal;
    }

    private static final ObjectMapper mapper = new ObjectMapper();

    public static String ConvertErrorToJson(Exception ex1) {

        try {
            HashMap<String, Object> mapJson = new HashMap<>();
            mapJson.put("mensagem", ex1.getMessage());
            return mapper.writeValueAsString(mapJson);
        } catch (IOException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }

    public static Long converteInteiroParaLongTime(String s) {
        String[] entrada = s.split(":");
        Long hora = (Long.parseLong(entrada[0]) * 3600 * 1000);
        Long minutos = (Long.parseLong(entrada[1]) * 60 * 1000);
        return hora + minutos;
    }

    /**
     * Converte uma data para o formato "0:00" em horas interiras Este método é
     * utilizado para mostrar os mesmo valores que são apresatado no front
     *
     * @param date
     * @return
     */
    public static String horasInt(Date date) {
        if(date==null){
            return "0:00";
        }
        Long hora;
        if (date instanceof java.sql.Time) {
            hora = (long) (date.getHours());
        } else {
            Date d = new Date();
            if (d.getDate() == date.getDate()) {
                hora = (long) (date.getHours());
            } else {
                hora = date.getTime() / 3600 / 1000;
            }
        }
        Date d = new Date(date.getTime());
        String minutos = Integer.toString(d.getMinutes()).length() == 1 ? "0" + Integer.toString(d.getMinutes()) : Integer.toString(d.getMinutes());
        return hora.toString().length() == 1 ? "0" + hora + ":" + minutos : hora + ":" + minutos;
    }

    public static String convertDateHorasToString(Date data, Boolean isValoresNullFormatados, Boolean isValoresZeradosFormatados) {
        String horario = "";
        if (data != null) {
            Calendar calendar = Calendar.getInstance();
            //        calendar.setTimeZone(TimeZone.getTimeZone("Etc/GMT0"));
            calendar.setTime(data);
            int hours = calendar.get(Calendar.HOUR_OF_DAY);
            int minutes = calendar.get(Calendar.MINUTE);

            if (isValoresZeradosFormatados && hours == 0 && minutes == 0) {
                horario = "--:--";
            } else {
                horario = (hours / 10 == 0 ? "0" : "") + hours + ":" + (minutes / 10 == 0 ? "0" : "") + minutes;
            }
        } else if (isValoresNullFormatados) {
            horario = "--:--";
        } else {
            horario = "00:00";
        }

        return horario;
    }

    public static Date convertStringToDate(String dataStr, String expectedPattern) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(expectedPattern);
        return formatter.parse(dataStr);
    }
//    public static String ConvertToJson(String msg) {
//
//        try {
//            HashMap<String, Object> mapJson = new HashMap<>();
//            mapJson.put("mensagem", msg);
//            return mapper.writeValueAsString(mapJson);
//        } catch (IOException ex) {
//            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return null;
//
//    }

    public static String ConvertToJson(String msg) {

        try {
            HashMap<String, Object> mapJson = new HashMap<>();
            mapJson.put("type", "success");
            mapJson.put("title", "");
            mapJson.put("body", msg);
            return mapper.writeValueAsString(mapJson);

        } catch (IOException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static Date configuraHorarioData(Date data, int hora, int minuto, int segundo) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(data);
        calendar.set(Calendar.SECOND, segundo);
        calendar.set(Calendar.MINUTE, minuto);
        calendar.set(Calendar.HOUR_OF_DAY, hora);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

    public static MsgRetorno extractMsgReturn(Response response) {
        if (response.getEntity() != null) {
            if (response.getEntity() instanceof MsgRetorno) {
                return (MsgRetorno) response.getEntity();
            } else if (response.getEntity() instanceof EntidadeRetorno) {
                return ((EntidadeRetorno) response.getEntity()).getMsgRetorno();
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Subtrai ou adiciona a quantidade de dias da data passada como referencia
     *
     * @param d
     * @param dias
     * @return
     */
    public static Date addDias(Date d, int dias) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.add(Calendar.DAY_OF_MONTH, dias);
        return c.getTime();
    }

    /**
     * Trata uma URL de diretorio (removendo o %20 por exemplo)
     *
     * @param url
     * @return
     */
    public static String corrigirUrl(String url) {
        return URI.create(url).getPath();
    }

    public static Integer quantidadeLinhasArquivo(BufferedReader reader) throws IOException {
        Integer lines = 0;
        while (reader.readLine() != null) {
            lines++;
        }

        return lines;
    }

    public static File salvarArquivo(String nomeCompleto, InputStream is) throws IOException {
        OutputStream outputStream = null;
        try {

            // write the inputStream to a FileOutputStream
            File arquivoSalvo = new File(nomeCompleto);
            outputStream
                    = new FileOutputStream(arquivoSalvo);

            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = is.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }

            return arquivoSalvo;

        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
        }

    }

    /**
     *
     */
    public static class MASK {

        public static String CEI(String valor) {
            if (valor != null) {
                return valor.replaceAll("(\\d{2})(\\d{3})(\\d{5})(\\d{2})", "$1.$2.$3/$4");
            } else {
                return "";
            }
        }

        public static String DOCUMENTO(TipoDocumento tipoDocumento, String valor) {
            if (tipoDocumento.getDescricao().equals(CONSTANTES.Enum_DOCUMENTO.CNPJ.getDescricao())) {
                return CNPJ(valor);
            } else {
                return CPF(valor);
            }
        }

        public static String CNPJ(String valor) {
            if (valor != null) {
                return valor.replaceAll("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})", "$1.$2.$3/$4-$5");
            } else {
                return "";
            }
        }

        public static String CPF(String valor) {
            if (valor != null) {
                return valor.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
            } else {
                return "";
            }
        }

        public static String IP(String valor) {
            if (valor != null) {
                return valor.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
            } else {
                return "";
            }
        }

        public static String PIS(String valor) {
            if (valor != null) {
                return valor.replaceAll("(\\d{3})(\\d{5})(\\d{2})(\\d{1})", "$1.$2.$3-$4");
            } else {
                return "";
            }
        }

        public static String CEP(String valor) {
            if (valor != null) {
                return valor.replaceAll("(\\d{2})(\\d{3})(\\d{3})", "$1.$2-$3");
            } else {
                return "";
            }
        }

        public static String PERCENTUAL(Double valor) {
            if (valor != null && valor != Double.NaN) {
                valor = valor / 100;
                //                return valor.toString();
                String[] valores = valor.toString().split("\\.");
                if (valores.length > 0) {
                    String inteiro = valores[0];
                    String decimal = valores[1];
                    decimal = decimal.length() > 5 ? decimal.substring(0, 5) : decimal;
                    return inteiro + "," + decimal + "%";
                } else {
                    return "";
                }
            } else {
                return "";
            }
        }

        public static String DATA(Date data) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            return sdf.format(data);
        }
        
        public static String BOOLEAN(Boolean flag) {

            return flag ? "Sim" : "Não";
        }

        public static String HORA(Date horaData) {

            return Utils.convertDateHorasToString(horaData, false, false);
        }

        public static String HORA_API(Date horaData, Boolean formatarZeros) {

            return Utils.convertDateHorasToString(horaData, formatarZeros, formatarZeros);
        }

        public static String HORA(Duration duration) {
            return HORA(duration, Boolean.TRUE, 2);
        }

        public static String HORA(Duration duration, Boolean formatarZero, int precisaoHora) {
            String horario = "";

            for (int i = 0; i < precisaoHora; i++) {
                horario = (formatarZero ? "-" : "0") + horario;
            }
            horario += formatarZero ? ":--" : ":00";

            if (duration != null && !duration.isZero()) {
                boolean isNegativo = duration.getSeconds() < 0;

                long horas = Math.abs(duration.toHours());
                long minutos = Math.abs(duration.toMinutes()) - (horas * 60);
                long segundos = Math.abs(duration.getSeconds()) - (horas * 60 * 60) - (minutos * 60);

                if (horas != 0 || minutos != 0 || segundos != 0) {
                    String horaStr = String.valueOf(horas);

//                    horaStr = (horaStr.length() > 1 ? "" : "0") + horaStr;
                    for (int i = horaStr.length(); i < precisaoHora; i++) {
                        horaStr = "0" + horaStr;
                    }

                    String minStr = String.valueOf(minutos);
                    minStr = (minStr.length() > 1 ? "" : "0") + minStr;

                    horario = horaStr + ":" + minStr;

                    if (isNegativo) {
                        horario = "(" + horario + ")";
                    }
                }

            }
            return horario;
        }

        public static String FORMATO_CH(int idHorario) {
            String ch = String.valueOf(idHorario);
            int precisao = 7;//numero fixo por enquanto

            int falta = precisao - ch.length();

            if (falta > 0) {
                for (int i = 0; i < falta; i++) {
                    ch = "0" + ch;
                }
            }

            return ch;
        }

        public static String HORA_TOTAL_HORARIO_MARCACOES(Collection<HorarioMarcacao> horarioMarcacaoList) {
            Long totalHorasLong = new Long(0);
            if (horarioMarcacaoList != null && !horarioMarcacaoList.isEmpty()) {
                totalHorasLong = horarioMarcacaoList.stream().map((horarioMarcacao) -> {
                    Long saida = horarioMarcacao.getHorarioSaida().getTime();
                    Long entrada = horarioMarcacao.getHorarioEntrada().getTime();
                    Long diferencaHorario;
                    if (saida >= entrada) {
                        //No mesmo dia
                        //Caso padrao, a DIFERENÇA é calculada facilmente
                        diferencaHorario = saida - entrada;
                    } else {
                        //Virou o dia
                        //Calcula hora de entrada até a meia noite (24h)
                        Long meiaNoite = new Long(24 * 60 * 60 * 1000);
                        //Depois SOMA o horario de saida
                        diferencaHorario = (meiaNoite - entrada) + saida;
                    }
                    return diferencaHorario;
                }).map((diferencaHorario) -> diferencaHorario).reduce(totalHorasLong, (accumulator, _item) -> accumulator + _item);
            }

            return Utils.horasInt(new Date(totalHorasLong));
        }

        public static String SEQUENCIA_TIPO_DIA(Collection<SequenciaPercentuais> sequenciaPercentuaisList) {
            return sequenciaPercentuaisList.isEmpty() ? "..." : sequenciaPercentuaisList.iterator().next().getTipoDia().getDescricao();
        }

        /**
         * Mascara para adicionar um elemento ao menos em uma lista de duration
         * (utilizado nos relatorios jasper)
         *
         * @param lista
         * @return
         */
        public static Collection INIT_LISTA_DURATION(Collection<Duration> lista) {
            if (lista.isEmpty()) {
                lista.add(Duration.ZERO);
            }

            return lista;
        }

    }

    public static class CONFIGURAR {

        /**
         * Agrupa as sequecias de percentuais por tipo dia
         *
         * @param percentuais
         * @return
         */
        public static List<PercentuaisAcrescimo> AGRUPAR_SEQUENCIA_PERCENTUAIS(PercentuaisAcrescimo percentuais) {

            List<PercentuaisAcrescimo> percentuaisAgrupadosList = new ArrayList<>();
            List<SequenciaPercentuais> sequenciaList = percentuais.getSequenciaPercentuaisList();

            sequenciaList.stream().forEach((sequencia) -> {
                boolean achou = false;
                for (PercentuaisAcrescimo perc : percentuaisAgrupadosList) {

                    //Em teoria, n tem como isso estar vazio
                    if (!perc.getSequenciaPercentuaisList().isEmpty()) {

                        SequenciaPercentuais seqCad = perc.getSequenciaPercentuaisList().get(0);
                        if (Objects.equals(sequencia.getTipoDia().getIdTipodia(), seqCad.getTipoDia().getIdTipodia())) {
                            perc.getSequenciaPercentuaisList().add(sequencia);
                            achou = true;
                            break;
                        }
                    }
                }

                if (!achou) {
                    List<SequenciaPercentuais> seqList = new ArrayList<>();
                    seqList.add(sequencia);

                    PercentuaisAcrescimo percentuaisAcrescimo = new PercentuaisAcrescimo();
                    percentuaisAcrescimo.setSequenciaPercentuaisList(seqList);

                    percentuaisAgrupadosList.add(percentuaisAcrescimo);
                }

            });

            return percentuaisAgrupadosList;

        }

        /**
         * Agrupa as sequecias de percentuais por tipo dia (do banco de horas
         * extras)
         *
         * @param tabelaExtrasTotalList
         * @param tipoDiaList
         * @return
         */
        public static List<TabelaAgrupada> AGRUPAR_SEQUENCIA_PERCENTUAIS(Collection<TabelaSequenciaPercentuais> tabelaExtrasTotalList, Collection<TipoDia> tipoDiaList) {

            List<TabelaAgrupada> tabelaAgrupadasList = new ArrayList<>();

            tipoDiaList.stream().forEach(tipoDia -> {
                if (!Objects.equals(tipoDia.getIdTipodia(), TipoDia.HORAS_NOTURNAS) && !Objects.equals(tipoDia.getIdTipodia(), TipoDia.COMPENSACADO)) {
                    tabelaAgrupadasList.add(new TabelaAgrupada(tipoDia));
                }
            });

            tabelaExtrasTotalList.stream().forEach((TabelaSequenciaPercentuais tabela) -> {
//                boolean achou = false;

                for (TabelaAgrupada tabelaAgrupada : tabelaAgrupadasList) {
                    TipoDia tipoDia = tabelaAgrupada.getTipoDia();
                    List<TabelaSequenciaPercentuais> tabelaList = tabelaAgrupada.getTabelaList();

                    //Em teoria, n tem como isso estar vazio
                    if (Objects.equals(tabela.getTipoDia().getIdTipodia(), tipoDia.getIdTipodia())) {
                        tabelaList.add(tabela);
                        break;
                    }
                }
            });

            return tabelaAgrupadasList;
        }

        /**
         * Agrupa as sequecias de percentuais por tipo dia (do banco de horas)
         *
         * @param tabelaTotalList
         * @param tipoDiaList
         * @return
         */
        public static List<TabelaAgrupada> AGRUPAR_SEQUENCIA_PERCENTUAIS_BANCO_HORAS(Collection<TabelaSequenciaPercentuais> tabelaTotalList, Collection<TipoDia> tipoDiaList) {

            List<TabelaAgrupada> tabelaAgrupadasList = new ArrayList<>();

            tipoDiaList.stream().forEach(tipoDia -> {
                if (!Objects.equals(tipoDia.getIdTipodia(), TipoDia.HORAS_NOTURNAS)) {
                    tabelaAgrupadasList.add(new TabelaAgrupada(tipoDia));
                }
            });

            tabelaTotalList.stream().forEach((TabelaSequenciaPercentuais tabela) -> {
//                boolean achou = false;

                for (TabelaAgrupada tabelaAgrupada : tabelaAgrupadasList) {
                    TipoDia tipoDia = tabelaAgrupada.getTipoDia();
                    List<TabelaSequenciaPercentuais> tabelaList = tabelaAgrupada.getTabelaList();

                    if (Objects.equals(tabela.getTipoDia().getIdTipodia(), tipoDia.getIdTipodia())) {
                        tabelaList.add(tabela);
                        break;
                    }
                }
            });

            return tabelaAgrupadasList;
        }

        /**
         * Busca qual é o total de banco de horas para determinado tipo dia as
         * marcações
         *
         * @param tabelaCreditoSaldoUnicoList
         * @param idTipodia
         * @return
         */
        public static Duration GET_SALDO_CREDITO_POR_DIA(Collection<TabelaCreditoSaldoUnico> tabelaCreditoSaldoUnicoList, int idTipodia) {
            for (TabelaCreditoSaldoUnico creditoSaldoUnico : tabelaCreditoSaldoUnicoList) {
                if (creditoSaldoUnico.getTipoDia() != null && creditoSaldoUnico.getTipoDia().getIdTipodia() == idTipodia) {
                    return creditoSaldoUnico.getSaldo();
                }
            }

            return Duration.ZERO;

        }

        /**
         * Remove todos os dias com horarios previstos repetidos. Isso serve
         * para montar uma lista de horarios previstos unicos e é utilizado no
         * relatorio de espelho fiscal
         *
         * @param saidaDiaList
         * @return
         */
        public static List<SaidaDia> REMOVER_DIAS_HORARIOS_PREVISTOS_REPETIDOS(Collection<SaidaDia> saidaDiaList) {
            List<SaidaDia> result = new ArrayList<>();
            saidaDiaList.stream()
                    .collect(
                            Collectors.groupingBy(saidaDia -> saidaDia.getIdHorario())
                    ).values().stream().forEach(saidaDiaAgrupadoList -> {
                        //Só adiciona o primeiro do agrupado
                        result.add(saidaDiaAgrupadoList.get(0));
                    });
            return result;
        }

        /**
         * Configura a linha do relatorio para ser preenchida por todos os
         * elementos
         *
         * @param col
         * @param qntLinha
         * @return
         */
        public static Collection<HorarioMarcacao> HORARIO_MARCACAO(Collection col, int qntLinha) {
            List<HorarioMarcacao> horarioMarcacaoList;

            if (col == null) {
                horarioMarcacaoList = new ArrayList<>();
            } else {
                horarioMarcacaoList = new ArrayList<>(col);
            }

            int sobras;
            if (horarioMarcacaoList.isEmpty()) {
                sobras = qntLinha;
            } else {
                sobras = horarioMarcacaoList.size() % qntLinha; //Pega o resto da divisao entra o tamanho da lista e a quantidade de itens na lista
                if (sobras > 0) {
                    sobras = qntLinha - sobras; //Pega a diferença (pois é o que falta pra preencher)
                }
            }

            //Para cada itens que sobrou da sobra, adiciona um item com horario de entrada e saida null
            for (int i = 0; i < sobras; i++) {
                horarioMarcacaoList.add(new HorarioMarcacao());
            }
            return horarioMarcacaoList;
        }

        /**
         * Configura a linha do relatorio para ser preenchida por todos os
         * elementos
         *
         * @param col
         * @param qntLinha
         * @param minTamanho
         * @return
         */
        public static Collection<MarcacoesDia> MARCACAO(Collection col, int qntLinha, int minTamanho) {
            List<MarcacoesDia> marcacaoList;

            if (col == null) {
                marcacaoList = new ArrayList<>();
            } else {
                marcacaoList = new ArrayList<>(col);
            }

            //Primeiro adiciona o numero de elementos q faltam para a lista
            int diffTamanho = minTamanho - marcacaoList.size();
            if (diffTamanho > 0) {
                for (int i = 0; i < diffTamanho; i++) {
                    marcacaoList.add(new MarcacoesDia());
                }
            }

            //Depois arruma os espaços vazios
            int sobras;
            if (marcacaoList.isEmpty()) {
                sobras = qntLinha;
            } else {

                sobras = marcacaoList.size() % qntLinha; //Pega o resto da divisao entra o tamanho da lista e a quantidade de itens na lista
                if (sobras > 0) {
                    sobras = qntLinha - sobras; //Pega a diferença (pois é o que falta pra preencher)
                }
            }

            //Para cada itens que sobrou da sobra, adiciona um item com horario de entrada e saida null
            for (int i = 0; i < sobras; i++) {
                marcacaoList.add(new MarcacoesDia());
            }
            return marcacaoList;
        }

        /**
         * Calcula o total de intervalo de uma interjornada (levendo em conta um
         * array de dias)
         *
         * @param col
         * @return
         */
        public static String TOTAL_INTERVALO_INTERJORNADA(Collection col) {
            List<SaidaDia> saidaDiaList;
            if (col == null) {
                saidaDiaList = new ArrayList<>();
            } else {
                saidaDiaList = new ArrayList<>(col);
            }

            Duration total = Duration.ZERO;

            for (SaidaDia saidaDia : saidaDiaList) {
                if (saidaDia != null && saidaDia.getIntervalos() != null && saidaDia.getIntervalos().getSaldoHorasInterJornada() != null) {
                    total = total.plus(saidaDia.getIntervalos().getSaldoHorasInterJornada());
                }
            }

            return MASK.HORA(total);
        }

        public static String TOTAL_INTERVALO_INTRAJORNADA(Collection col) {
            List<SaidaDia> saidaDiaList;
            if (col == null) {
                saidaDiaList = new ArrayList<>();
            } else {
                saidaDiaList = new ArrayList<>(col);
            }
            Duration total = Duration.ZERO;

            for (SaidaDia saidaDia : saidaDiaList) {

                if (saidaDia.getIntervalos() != null && saidaDia.getIntervalos().getSaldoHorasIntraJornadaList() != null) {
                    List<Duration> saldoHorasIntraJornadaList = saidaDia.getIntervalos().getSaldoHorasIntraJornadaList();

                    for (Duration saldoHorasIntra : saldoHorasIntraJornadaList) {
                        if (saldoHorasIntra != null) {
                            total = total.plus(saldoHorasIntra);
                        }
                    }
                }
            }

            return MASK.HORA(total);
        }

    }

}
