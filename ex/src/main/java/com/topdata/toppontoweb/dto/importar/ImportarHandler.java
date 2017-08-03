/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.topdata.toppontoweb.dto.importar;

import com.topdata.toppontoweb.entity.empresa.Empresa;
import com.topdata.toppontoweb.entity.funcionario.Funcionario;
import com.topdata.toppontoweb.entity.funcionario.Motivo;
import com.topdata.toppontoweb.entity.marcacoes.EncaixeMarcacao;
import com.topdata.toppontoweb.entity.marcacoes.Importacao;
import com.topdata.toppontoweb.entity.marcacoes.Marcacoes;
import com.topdata.toppontoweb.entity.marcacoes.StatusMarcacao;
import com.topdata.toppontoweb.entity.rep.Rep;
import com.topdata.toppontoweb.services.ServiceException;
import static com.topdata.toppontoweb.services.TopPontoService.LOGGER;
import com.topdata.toppontoweb.services.importar.ImportarServices;
import com.topdata.toppontoweb.utils.Utils;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;
import java.io.BufferedReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import javax.ws.rs.core.Response;
import org.apache.commons.lang.math.NumberUtils;

/**
 *
 * @author tharle.camargo
 */
public enum ImportarHandler {
    FERRAMENTAS_MARCACOES{
        @Override
        public void importarArquivo(ImportarStatusTransfer status, Empresa empresa, Importacao importacao, ImportarServices importarServices,  BufferedReader reader, double percentualPorLinha) throws ServiceException, ParseException, NumberFormatException, IOException{
            
            //Busca encaixe marcacao
            EncaixeMarcacao encaixeMarcacao = importarServices.getEncaixeMarcacaoService().buscar(CONSTANTES.Enum_ENCAIXE_MARCACAO.NAO_DEFINIDO);
            LOGGER.debug("IMPORTANDO MARCACOES VIA ARQUIVO ANTIGO:");
            
            //é pre-calcula "motivo marcacao mesmo minuto"
            String descricaoMotivo = CONSTANTES.Enum_MOTIVO.MARCACAO_MESMO_MINUTO.getDescricao();
            Integer idTipoMotivo = CONSTANTES.Enum_TIPO_MOTIVO.MARCACAO_DESCONSIDERADA.ordinal();
            Motivo motivoMarcacaoMesmoMinuto = importarServices.getMotivoService().buscaPorDescricao(descricaoMotivo, idTipoMotivo);
            
            Marcacoes marcacoes;
            String linha;
            while (!status.isCancelar() && (linha = reader.readLine()) != null) {
                try{
                    LOGGER.debug("Lendo: "+linha);
                    status.addProgresso(percentualPorLinha);
                    //Altera status para "PROCESSANDO_LINHA_DE_IMPORTACAO" 5% -> 95% (90%)
                    //Processando X de totalLinhas...
                    //010 01/05/10 08:00 0000000000000080 003
                    String[] linhaProcessada =  linha.split("\\s");
                    if(linhaProcessada.length < 5){
                        continue;
                    }
                    
                    String dataSrt = linhaProcessada[1] + " "+ linhaProcessada[2];
                    String expectedPattern = "dd/MM/yy HH:mm";
                    Date dataMarcacao =  Utils.convertStringToDate(dataSrt, expectedPattern);
                    
                    
                    String numeroCartaoOriginal = linhaProcessada[3];
                    String numeroRepStr = linhaProcessada[4];
                    Integer numeroRep = Integer.parseInt(numeroRepStr);


                    //ou não encontrou o equipamento
                    //ou equipamento não é vinculado a empresa
                    Rep rep = importarServices.getRepService().buscarPorNumeroRep(numeroRep, empresa);
                    
                    //Aplica o formato do rep no numero do cartao para obter o verdadeiro numero do cartao
                    String numeroCartao = rep.aplicarFormatoCartao(numeroCartaoOriginal);
                    
                    //Ignora os "zeros" à esquerda
                    numeroCartao = Utils.ignorarZerosAEsquerda(numeroCartao);
                    
                    //ou não encontrou o cartão do funcionário na data
                    //ou se não é o ultimo cartão do funcionario
                    //ou funcionário foi demitido
                    Funcionario funcionario = importarServices.getFuncionarioService().validarBuscarPorCartao(numeroCartao, empresa, dataMarcacao);
                    
                    //ou se o número de dígitos informado na tela é diferente do que está no arquivo
                    //ou se essa marcação já não foi importado (não pode repetir)
                    //Sim idStatusMarcacao = 3 (Desconsiderada)
                    //Não idStatusMarcacao = 1 (Original)
                    // Ou ignorada!
                    StatusMarcacao statusMarcacao = importarServices.getMarcacoesService().calcularStatusMarcacao(dataMarcacao, numeroCartaoOriginal, rep, funcionario, empresa, importacao);
                    
                    Motivo motivo = null;
                    if(statusMarcacao.getIdStatusMarcacao() == CONSTANTES.Enum_STATUS_MARCACAO.DESCONSIDERADA.ordinal()){
                        motivo = motivoMarcacaoMesmoMinuto;
                    }

                    Boolean invalido;
                    if(funcionario == null || importarServices.getFechamentoServices().isExisteFechamento(empresa, dataMarcacao)){//Invalido?
                        //Caso não exista funcionario ou existe um fechamento para a empresa na data da marcação...
                        //é uma marcação invalida
                        importacao.addQuantidadeInvalidas(1);
                        invalido = true;
                    }else{
                        importacao.addQuantidadeValidas(1);
                        invalido = false;
                    }
                            
                    marcacoes = new Marcacoes(funcionario, empresa, rep, dataMarcacao, statusMarcacao, numeroCartaoOriginal, numeroCartao, invalido,  encaixeMarcacao, motivo, importacao);
                    
                    //  TODO: Refazer o SALVAR!
                    importarServices.getMarcacoesService().salvar(marcacoes);
                    
                    
                }catch(ServiceException e){
//                    LOGGER.error(this.getClass().getSimpleName(), e);
                    //Ocorreu algum problema?? Ignora!
                    importacao.addQuantidadeIgnoradas(1);
                }
            }
            
            //Seta como concluido
            status.setMensagem(CONSTANTES.PROCESSO_STATUS.CONCLUIDO);
        }

        @Override
        public Response validarCabecalho(String cabecalho, ImportarServices importarServices) {
            //não implementado
            return null;
        }

        @Override
        public CONSTANTES.Enum_FUNCIONALIDADE getFuncionalidade() {
            return CONSTANTES.Enum_FUNCIONALIDADE.FER_MARCACOES;
        }
    },
    AFD{
        @Override
        public void importarArquivo(ImportarStatusTransfer status, Empresa empresa, Importacao importacao, ImportarServices importarServices, BufferedReader reader, double percentualPorLinha) throws ServiceException, ParseException, NumberFormatException, IOException {
            String linhaOriginal;
            
            LOGGER.debug("IMPORTANDO MARCACOES VIA ARQUIVO AFD:");
            
            //Ler cabeçalho
            Rep rep;
            boolean isCrc = false;
            if(!status.isCancelar() && (linhaOriginal = reader.readLine()) != null){
                String linha = Utils.converterStringParaUTF8(linhaOriginal);
                LOGGER.debug("Cabeçalho : "+linha);
                
                if(linha.length() > 232){
                    //Tem CRC
                    isCrc = true;
                    if(!validarLinhaCRC(linhaOriginal)){
                        LOGGER.debug("ERRO AO VALIDAR CRC, ARQUIVO ESTÁ CORROMPIDO");
                        status.setMensagem(CONSTANTES.PROCESSO_STATUS.ERRO_ABRIR_ARQUIVO);
                        return;
                    }
                }
                
                //9) codigo (ignora) (0 - 9)
                //1) Tipo de registro (cabeçalho '1') (9-10)
                String tipoRegistro = linha.substring(9, 10);
                if(!tipoRegistro.equals("1")){
                    //Caso o primeiro registro n for 1... (10 - 11)
                    //ERRO
                    
                    LOGGER.debug("ERRO AO IMPORTAR AFD: Cabeçalho não é a primeira linha.");
                    status.setMensagem(CONSTANTES.PROCESSO_STATUS.ERRO_IMPORTAR_SEM_CABECALHO);
                    return;
                }
                
                
                //1) CNPJ = '1' e CPF = '2' (ignora)
                //14) Documento
                String documento = linha.substring(11, 25);
                empresa = importarServices.getEmpresaService().buscaPorDocumento(documento);
                
                if(empresa == null){
                    LOGGER.debug("ERRO AO IMPORTAR AFD: Empresa cadastrada ("+documento+") não encontrada.");
                    status.setMensagem(CONSTANTES.PROCESSO_STATUS.ERRO_IMPORTAR_EMPRESA_NAO_ENCONTRADA);
                    return;
                }
                
                //12) CEI (Ignora) (25-37)
                //150)  Razao Social (Ignora) (37 - 187)

                //17) N. fabricacao ou Série completa
                String serieCompleta = linha.substring(187, 204);
                //     5) Número do Fabricante
                String numeroFabricante = serieCompleta.substring(0, 5);
                //     5) Modelo
                String numeroModelo = serieCompleta.substring(5, 10);
                //     7) Número de Série
                String numeroSerie = serieCompleta.substring(10, 17);
                
                rep = importarServices.getRepService().buscarPorSerieCompletaEmpresa(empresa, numeroFabricante, numeroModelo, numeroSerie);
                
                //8) Data inicial dos registros (ddmmaaaa) (ignora)
                //8) Data final dos registros (ddmmaaaa) (ignora)
                //12) Data de geracao do arquivo (ddmmaaaahhmm) (ignora)
                
                if(rep  == null){
                    //Rep não encontrado
                    //ERRO
                    LOGGER.debug("ERRO AO IMPORTAR AFD: Equipamento (REP) não encontrado.");
                    status.setMensagem(CONSTANTES.PROCESSO_STATUS.ERRO_IMPORTAR_REP_NAO_ENCONTRADO);
                    return;
                }
            }else{
                //Arquivo vazio
                //ERRO
                return;
            }
            
            
            //Busca encaixe marcacao
            EncaixeMarcacao encaixeMarcacao = importarServices.getEncaixeMarcacaoService().buscar(CONSTANTES.Enum_ENCAIXE_MARCACAO.NAO_DEFINIDO);
            
            //é pre-calcula "motivo marcacao mesmo minuto"
            String descricaoMotivo = CONSTANTES.Enum_MOTIVO.MARCACAO_MESMO_MINUTO.getDescricao();
            Integer idTipoMotivo = CONSTANTES.Enum_TIPO_MOTIVO.MARCACAO_DESCONSIDERADA.ordinal();
            Motivo motivoMarcacaoMesmoMinuto = importarServices.getMotivoService().buscaPorDescricao(descricaoMotivo, idTipoMotivo);
            
            //Ler detalhes
            String ultimoNsr = null;
            while (!status.isCancelar() && (linhaOriginal = reader.readLine()) != null) {
                String linha = Utils.converterStringParaUTF8(linhaOriginal);
                LOGGER.debug("Lendo: "+linha);
                try{
                    status.addProgresso(percentualPorLinha);
                    
                    //Valida CRC se existir
                    if(isCrc && !validarLinhaCRC(linhaOriginal)){
                        LOGGER.debug("ERRO AO VALIDAR CRC, ARQUIVO ESTÁ CORROMPIDO");
                        status.setMensagem(CONSTANTES.PROCESSO_STATUS.ERRO_ABRIR_ARQUIVO);
                        return;
                    }
                    
                    //Ler Registro marcação ponto
                    //9)  codigo (NSR)
                    String nsrDocumentoStr = linha.substring(0, 9);
                    nsrDocumentoStr = Utils.ignorarZerosAEsquerda(nsrDocumentoStr);
                    
                    if(!NumberUtils.isNumber(nsrDocumentoStr)){
                        //Não é um nsr, ignora
                        continue;
                    }
                    
                    //Verifica se nsr é maior do que o do REP
                    Integer nsrDocumento = Integer.parseInt(nsrDocumentoStr);
                    Integer nsrRep =  Integer.parseInt(rep.getNsr());
                    if(nsrDocumento <= nsrRep){
                        //nsr do documento não deve ser menor que o do rep
                        //ignora
//                        importacao.addQuantidadeIgnoradas(1);
                        importacao.addQuantidadeIgnoradas(1);
                        continue;
                    }
                    
                    //1)  Tipo de registro (marcacoes '3')
                    String tipoRegistro = linha.substring(9, 10);
                    
                    Marcacoes marcacoes;
                    
                    if(tipoRegistro.equals("3")){//Marcação
                        //12) Data da marcação (ddmmaaaahhmm)
//                        String diaStr = linha.substring(10, 22);
//                        String horaStr = linha.substring(18, 22);
                        String dataSrt = linha.substring(10, 22);
                        String expectedPattern = "ddMMyyyyHHmm";
                        Date dataMarcacao =  Utils.convertStringToDate(dataSrt, expectedPattern);
                        
                        //12) PIS do empregado (ignora o primeiro digito que é zero)
                        String pis = linha.substring(23, 34);
                        
                        //ou se o número de dígitos informado na tela é diferente do que está no arquivo
                        //ou se essa marcação já não foi importado (não pode repetir)
                        //Sim idStatusMarcacao = 3 (Desconsiderada)
                        //Não idStatusMarcacao = 1 (Original)
                        // Ou ignorada!
                        StatusMarcacao statusMarcacao = importarServices.getMarcacoesService().calcularStatusMarcacaoPorNSR(nsrDocumentoStr, dataMarcacao, pis,  rep, empresa);
                        
                        Motivo motivo = null;
                        if(statusMarcacao.getIdStatusMarcacao() == CONSTANTES.Enum_STATUS_MARCACAO.DESCONSIDERADA.ordinal()){
                            motivo = motivoMarcacaoMesmoMinuto;
                        }

                        
                        //Busca funcionario pelo pis
                        Funcionario funcionario = importarServices.getFuncionarioService().buscarPorPisEmpresa(pis, empresa);
                        
                        boolean invalida;
                        if(funcionario == null || importarServices.getFechamentoServices().isExisteFechamento(empresa, dataMarcacao)){
                            //Verifica se não existe o funcionario para o Pis   
                            //ou se possuí fechamento de horas para essa empresa e dia
                            
                            //Marcação valida
                            invalida = true;
                            importacao.addQuantidadeInvalidas(1);
                        }else{
                            //Marcação invalida
                            invalida = false;
                            importacao.addQuantidadeValidas(1);   
                        }
                        
                        marcacoes = new Marcacoes(funcionario, pis, dataMarcacao, empresa, rep, statusMarcacao, nsrDocumentoStr, encaixeMarcacao, motivo, invalida);
                        
                        ultimoNsr = nsrDocumentoStr;
                    }else{
                        //não é um tipo registro tratado, passa para a próxima linha
//                        importacao.addQuantidadeIgnoradas(1);
//                        importacao.addQuantidadeIgnoradas(1);
                        continue;
                    }
                    
                    //Vincula a importacao
                    marcacoes.setImportacao(importacao);
                    
                    //  SALVAR marcação!
                    importarServices.getMarcacoesService().salvar(marcacoes);
                    
                }catch(ServiceException e){
//                    LOGGER.error(this.getClass().getSimpleName(), e);
                    //Ocorreu algum problema?? Ignora!
                    importacao.addQuantidadeIgnoradas(1);
                }
            }
            
            if(ultimoNsr != null){
                importarServices.getRepService().atualizarNSR(rep.getIdRep(), ultimoNsr);
            }
            
            //Seta como concluido
            status.setMensagem(CONSTANTES.PROCESSO_STATUS.CONCLUIDO);
        }

        @Override
        public Response validarCabecalho(String cabecalho, ImportarServices importarServices) throws ServiceException {
            String linha = Utils.converterStringParaUTF8(cabecalho);
            
            Response response;
            
            //9) codigo (ignora) (0 - 9)
            //1) Tipo de registro (cabeçalho '1') (9-10)
            String tipoRegistro = cabecalho.substring(9, 10);
            if(!tipoRegistro.equals("1")){
                //Caso o primeiro registro n for 1... (10 - 11)
                //ERRO
                LOGGER.debug("ERRO AO IMPORTAR AFD: Cabeçalho não é a primeira linha.");
                return importarServices.getTopPontoResponse().sucessoValidar(CONSTANTES.PROCESSO_STATUS.ERRO_IMPORTAR_SEM_CABECALHO.toString());
            }


            //1) CNPJ = '1' e CPF = '2' (ignora)
            //14) Documento
            String documento = linha.substring(11, 25);
            Empresa empresa = importarServices.getEmpresaService().buscaPorDocumento(documento);
            
            if(empresa == null){
                LOGGER.debug("ERRO AO IMPORTAR AFD: Empresa ["+documento+"] não emcontrada.");
                return importarServices.getTopPontoResponse().sucessoValidar(CONSTANTES.PROCESSO_STATUS.ERRO_IMPORTAR_EMPRESA_NAO_ENCONTRADA.toString());
            }

            //12) CEI (Ignora) (25-37)
            //150)  Razao Social (Ignora) (37 - 187)

            //17) N. fabricacao ou Série completa
            String serieCompleta = linha.substring(187, 204);
            //     5) Número do Fabricante
            String numeroFabricante = serieCompleta.substring(0, 5);
            //     5) Modelo
            String numeroModelo = serieCompleta.substring(5, 10);
            //     7) Número de Série
            String numeroSerie = serieCompleta.substring(10, 17);

            Rep rep = importarServices.getRepService().buscarPorSerieCompletaEmpresa(empresa, numeroFabricante, numeroModelo, numeroSerie);

            //8) Data inicial dos registros (ddmmaaaa) (ignora)
            //8) Data final dos registros (ddmmaaaa) (ignora)
            //12) Data de geracao do arquivo (ddmmaaaahhmm) (ignora)

            if(rep  == null){
                //Rep não encontrado
                //ERRO
                LOGGER.debug("ERRO AO IMPORTAR AFD: Equipamento (REP) ["+numeroFabricante+"." + numeroModelo+"." + numeroSerie+"] não encontrado.");
                return importarServices.getTopPontoResponse().sucessoValidar(CONSTANTES.PROCESSO_STATUS.ERRO_IMPORTAR_REP_NAO_ENCONTRADO.toString());
            }
            
            return importarServices.getTopPontoResponse().sucessoValidar(CONSTANTES.PROCESSO_STATUS.SUCESSO_IMPORTAR_VALIDAR_CABECALHO.toString());
        }
        
        @Override
        public CONSTANTES.Enum_FUNCIONALIDADE getFuncionalidade() {
            return CONSTANTES.Enum_FUNCIONALIDADE.ARQ_AFD;
        }
    };
    
    public Boolean validarLinhaCRC(String linha){
        String codigoCRC = linha.substring(linha.length() - 4, linha.length());
        String linhaSemCRC = linha.substring(0, linha.length() - 4);
//        LOGGER.debug("Validando linha "+linhaSemCRC);
//        LOGGER.debug("CODIGO CRC : "+codigoCRC);
        
        
//         int[] table = {//Uses irreducible polynomial:  1 + x^2 + x^15 + x^16
//            0x0000, 0x8048, 0x80D8, 0x0090, 0x81F8, 0x01B0, 0x0120, 0x8168, 0x83B8, 0x03F0, 0x0360, 0x8328, 0x0240, 0x8208, 0x8298, 0x02D0,
//    0x8738, 0x0770, 0x07E0, 0x87A8, 0x06C0, 0x8688, 0x8618, 0x0650, 0x0480, 0x84C8, 0x8458, 0x0410, 0x8578, 0x0530, 0x05A0, 0x85E8,
//    0x8E38, 0x0E70, 0x0EE0, 0x8EA8, 0x0FC0, 0x8F88, 0x8F18, 0x0F50, 0x0D80, 0x8DC8, 0x8D58, 0x0D10, 0x8C78, 0x0C30, 0x0CA0, 0x8CE8,
//    0x0900, 0x8948, 0x89D8, 0x0990, 0x88F8, 0x08B0, 0x0820, 0x8868, 0x8AB8, 0x0AF0, 0x0A60, 0x8A28, 0x0B40, 0x8B08, 0x8B98, 0x0BD0,
//    0x9C38, 0x1C70, 0x1CE0, 0x9CA8, 0x1DC0, 0x9D88, 0x9D18, 0x1D50, 0x1F80, 0x9FC8, 0x9F58, 0x1F10, 0x9E78, 0x1E30, 0x1EA0, 0x9EE8,
//    0x1B00, 0x9B48, 0x9BD8, 0x1B90, 0x9AF8, 0x1AB0, 0x1A20, 0x9A68, 0x98B8, 0x18F0, 0x1860, 0x9828, 0x1940, 0x9908, 0x9998, 0x19D0,
//    0x1200, 0x9248, 0x92D8, 0x1290, 0x93F8, 0x13B0, 0x1320, 0x9368, 0x91B8, 0x11F0, 0x1160, 0x9128, 0x1040, 0x9008, 0x9098, 0x10D0,
//    0x9538, 0x1570, 0x15E0, 0x95A8, 0x14C0, 0x9488, 0x9418, 0x1450, 0x1680, 0x96C8, 0x9658, 0x1610, 0x9778, 0x1730, 0x17A0, 0x97E8,
//    0xB838, 0x3870, 0x38E0, 0xB8A8, 0x39C0, 0xB988, 0xB918, 0x3950, 0x3B80, 0xBBC8, 0xBB58, 0x3B10, 0xBA78, 0x3A30, 0x3AA0, 0xBAE8,
//    0x3F00, 0xBF48, 0xBFD8, 0x3F90, 0xBEF8, 0x3EB0, 0x3E20, 0xBE68, 0xBCB8, 0x3CF0, 0x3C60, 0xBC28, 0x3D40, 0xBD08, 0xBD98, 0x3DD0,
//    0x3600, 0xB648, 0xB6D8, 0x3690, 0xB7F8, 0x37B0, 0x3720, 0xB768, 0xB5B8, 0x35F0, 0x3560, 0xB528, 0x3440, 0xB408, 0xB498, 0x34D0,
//    0xB138, 0x3170, 0x31E0, 0xB1A8, 0x30C0, 0xB088, 0xB018, 0x3050, 0x3280, 0xB2C8, 0xB258, 0x3210, 0xB378, 0x3330, 0x33A0, 0xB3E8,
//    0x2400, 0xA448, 0xA4D8, 0x2490, 0xA5F8, 0x25B0, 0x2520, 0xA568, 0xA7B8, 0x27F0, 0x2760, 0xA728, 0x2640, 0xA608, 0xA698, 0x26D0,
//    0xA338, 0x2370, 0x23E0, 0xA3A8, 0x22C0, 0xA288, 0xA218, 0x2250, 0x2080, 0xA0C8, 0xA058, 0x2010, 0xA178, 0x2130, 0x21A0, 0xA1E8,
//    0xAA38, 0x2A70, 0x2AE0, 0xAAA8, 0x2BC0, 0xAB88, 0xAB18, 0x2B50, 0x2980, 0xA9C8, 0xA958, 0x2910, 0xA878, 0x2830, 0x28A0, 0xA8E8,
//    0x2D00, 0xAD48, 0xADD8, 0x2D90, 0xACF8, 0x2CB0, 0x2C20, 0xAC68, 0xAEB8, 0x2EF0, 0x2E60, 0xAE28, 0x2F40, 0xAF08, 0xAF98, 0x2FD0};
//
//
//        byte[] bytes = linhaSemCRC.getBytes();
//        int crc = 0x0000;
//        for (byte b : bytes) {//1 1000 0000 0000 0101
//            crc = (crc >>> 8) ^ table[(crc ^ b) & 0xff];
//        }
//        
//        LOGGER.debug("RESULTADO  1 -: CRC16 = " + Integer.toHexString(crc));
////-----------------------------------------------
//        int crc2 = 0xFFFF;         
//        int polynomial = 0xA001;   
//
//        // byte[] testBytes = "123456789".getBytes("ASCII");
//
//        byte[] bytes2 = linhaSemCRC.getBytes();
//
//        for (byte b : bytes2) {
//            for (int i = 0; i < 8; i++) {
//                boolean bit = ((b   >> (7-i) & 1) == 1);
//                boolean c15 = ((crc2 >> 15    & 1) == 1);
//                crc2 <<= 1;
//                if (c15 ^ bit) crc2 ^= polynomial;
//             }
//        }
//
//        crc2 &= 0xffff;
//        
//        LOGGER.debug("RESULTADO  2: CRC16 = " + Integer.toHexString(crc2));
        
        return true;
    }
    
    public abstract void  importarArquivo(ImportarStatusTransfer status, Empresa empresa, Importacao importacao, ImportarServices importarServices,  BufferedReader reader, double percentualPorLinha) throws ServiceException, ParseException, NumberFormatException, IOException;

    public abstract Response validarCabecalho(String cabecalho, ImportarServices importarServices) throws ServiceException;
    
    public abstract CONSTANTES.Enum_FUNCIONALIDADE getFuncionalidade();
}
