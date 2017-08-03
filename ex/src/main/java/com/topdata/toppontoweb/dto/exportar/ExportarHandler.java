package com.topdata.toppontoweb.dto.exportar;

import com.topdata.toppontoweb.entity.empresa.Empresa;
import static com.topdata.toppontoweb.services.TopPontoService.LOGGER;
import com.topdata.toppontoweb.services.gerafrequencia.entity.calculo.SaidaDia;
import com.topdata.toppontoweb.services.gerafrequencia.entity.fiscal.Arquivo;
import com.topdata.toppontoweb.services.gerafrequencia.integracao.SaidaPadrao;
import com.topdata.toppontoweb.services.relatorios.RelatorioHandler;
import com.topdata.toppontoweb.utils.Utils;
import java.io.BufferedWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 *
 * @author tharle.camargo
 */
public enum ExportarHandler{
        
    AFDT{
        @Override
        public void gerarArquivo(ExportarTransfer transfer, ExportarStatusTransfer status, List<SaidaPadrao> saidaPadraoList, BufferedWriter buffer, int max) {
            try {
                //CABEÇALHO
                String cabecalho = gerarCabecalhoPadrao(transfer);
                
                //Adiciona no arquivo
                buffer.append(cabecalho+ "\r\n");
                
                //DETALHE
                int sequencial = 2;
                for (SaidaPadrao saidaPadrao : saidaPadraoList) {
                    for (SaidaDia saidaDia : saidaPadrao.getSaidaDiaList()) {
                        for (Arquivo arquivo : saidaDia.getListaAFDT()) {
                            //Adiciona no arquivo
                            buffer.append(arquivo.getLinha() + "\r\n");
                            sequencial++;
                        }
                    }
                }
                String rodape = gerarRodapePadrao(sequencial);
                buffer.append(rodape);
                
            } catch (IOException ex) {
                LOGGER.debug(ex.getMessage());
            }
        }

        @Override
        public RelatorioHandler.TIPO_RELATORIO getTipoRelatorio() {
             return RelatorioHandler.TIPO_RELATORIO.AFDT;
        }
    },
    ACJEF{
        @Override
        public void gerarArquivo(ExportarTransfer transfer, ExportarStatusTransfer status, List<SaidaPadrao> saidaPadraoList, BufferedWriter buffer, int max) {
            try {
                //CABEÇALHO
                String cabecalho = gerarCabecalhoPadrao(transfer);
                
                //Adiciona no arquivo
                buffer.append(cabecalho+ "\r\n");
                
                //DETALHE
                Integer sequencial = 2;
                for (SaidaPadrao saidaPadrao : saidaPadraoList) {
                    for (SaidaDia saidaDia : saidaPadrao.getSaidaDiaList()) {
                        for (Arquivo arquivo : saidaDia.getListaHorariosContratuaisACJEF()) {
                            //Adiciona no arquivo
                            String sequencialSrt = Utils.corrigePrecisaoNumero(sequencial.toString(), 9);
                            buffer.append(sequencialSrt+arquivo.getLinha() + "\r\n");
                            
                            sequencial++;
                        }
                        
                        for (Arquivo arquivo : saidaDia.getListaACJEF()) {
                            //Adiciona no arquivo
                            String sequencialSrt = Utils.corrigePrecisaoNumero(sequencial.toString(), 9);
                            buffer.append(sequencialSrt+arquivo.getLinha() + "\r\n");
                            
                            sequencial++;
                        }
                    }
                }
                
                String rodape = gerarRodapePadrao(sequencial);
                buffer.append(rodape);
            } catch (IOException ex) {
                LOGGER.debug(ex.getMessage());
            }
        }

        @Override
        public RelatorioHandler.TIPO_RELATORIO getTipoRelatorio() {
            return RelatorioHandler.TIPO_RELATORIO.ACJEF;
        }
    };

    public abstract void  gerarArquivo(ExportarTransfer transfer, ExportarStatusTransfer status, List<SaidaPadrao> saidaPadraoList, BufferedWriter buffer, int max);

    public abstract RelatorioHandler.TIPO_RELATORIO getTipoRelatorio();
    
    public String gerarCabecalhoPadrao(ExportarTransfer transfer){
        Empresa empresa = transfer.getEmpresa();
        //9)  Sequencial do registro, para o cabeçalho é 1
        String cabecalho = Utils.corrigePrecisaoNumero("1", 9);
        
        //1) Tipo do registro (1)
        cabecalho+= com.topdata.toppontoweb.services.gerafrequencia.utils.CONSTANTES.EXPORTACAO_TIPOREGISTRO_1;
        
        //1) 1 para CPF e 2 Para CNPJ
        Integer tipoDocumento = empresa.getTipoDocumento() != null? empresa.getTipoDocumento().getIdTipoDocumento() : 0;
        cabecalho += tipoDocumento.toString();
        //14) CNPJ ou CPF
        cabecalho += Utils.corrigePrecisaoNumero(empresa.getDocumento(), 14);

        //12) CEI do empregador (pegar o primeiro)
        String strCei;
        if(empresa.getCeiList().isEmpty()){
            strCei = "";
        }else{
            strCei = empresa.getCeiList().get(0).getDescricao();
        }
        cabecalho += Utils.corrigePrecisaoNumero(strCei, 12);

        //150) Razao social (espaços em branco)
        cabecalho += Utils.corrigePrecisaoString(empresa.getRazaoSocial(), 150, true);

        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
        //8) Data inicial dos registros no arquivo (ddmmaaaa)
        cabecalho += sdf.format(transfer.getPeriodoInicio());
        //8) Data final dos registros no arquivo (ddmmaaaa)
        cabecalho += sdf.format(transfer.getPeriodoFim());
        //12) Data geração do arquivo (ddmmaaaaHHmm)
        sdf = new SimpleDateFormat("ddMMyyyyHHmm");
        cabecalho += sdf.format(new Date());
        
        return cabecalho;
    }
    
    public String gerarRodapePadrao(Integer sequencial) {
        return Utils.corrigePrecisaoNumero(sequencial.toString(), 9) + "9";
    }
}
