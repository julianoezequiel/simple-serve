package com.topdata.toppontoweb.services.relatorios;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRTextExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXmlExporter;
import net.sf.jasperreports.engine.export.oasis.JROdsExporter;
import net.sf.jasperreports.engine.export.oasis.JROdtExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.Exporter;
import net.sf.jasperreports.export.ExporterInput;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.export.SimpleHtmlReportConfiguration;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimpleTextReportConfiguration;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import org.springframework.stereotype.Service;

import com.topdata.toppontoweb.dto.FormatoTransfer;
import com.topdata.toppontoweb.dto.gerafrequencia.GeraFrequenciaStatusTransfer;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.gerafrequencia.integracao.GeraFrequenciaServices;
import com.topdata.toppontoweb.utils.Utils;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;

/**
 *
 * @author juliano.ezequiel
 */
@Service
public class ProcessarAquivosRelatorio {

    /**
     *
     * @param statusTransfer
     * @return Resposta de sucesso com o nome do arquivo temporario gerado
     * @throws com.topdata.toppontoweb.services.ServiceException
     */
    public JasperPrint processar(GeraFrequenciaStatusTransfer statusTransfer) throws ServiceException {

        try {
            statusTransfer.setEstado_processo(RelatorioHandler.ESTADO_PROCESSO.CRIANDO_ARQUIVO);
            //adiciona diretorio de imagens
            String caminhoLogoCabecalho;
            FormatoTransfer formato = new FormatoTransfer(CONSTANTES.Enum_FORMATO.PDF);
            if (formato.getIdFormato().equals(CONSTANTES.Enum_FORMATO.HTML.getIdFormato())) {
                //Caso for HTML
                caminhoLogoCabecalho = "assets/relatorio/img/logo-cabecalho.jpg";
            } else {
                //Caso for um arquivo qualquer 
                caminhoLogoCabecalho = getClass().getClassLoader().getResource("relatorio/img/logo-cabecalho.jpg").getPath();
                caminhoLogoCabecalho = Utils.corrigirUrl(caminhoLogoCabecalho);
            }
            
            

            statusTransfer.getParametros().put(RelatorioHandler.FILTRO_IMG_LOGO_CABECALHO, caminhoLogoCabecalho);
//            

//            statusTransfer.getParametros().put("DIRETORIO_RELATORIO", getClass().getClassLoader().getResource("relatorio").getPath());
            //Busca template do relatorio
            InputStream caminhoArquivo = getClass().getClassLoader().getResourceAsStream("relatorio/" + statusTransfer.getTemplate() + ".jrxml");
            JasperDesign jasperDesign = JRXmlLoader.load(caminhoArquivo);
            //Compila
            JasperReport relatorio = JasperCompileManager.compileReport(jasperDesign);

            JasperPrint jasperPrint;
            if (statusTransfer.getDataSource().getData() == null || statusTransfer.getDataSource().getData().isEmpty()) {
                jasperPrint = JasperFillManager.fillReport(relatorio, statusTransfer.getParametros(), new JREmptyDataSource());
            } else {
                jasperPrint = JasperFillManager.fillReport(relatorio, statusTransfer.getParametros(), statusTransfer.getDataSource());
            }
            statusTransfer.setRetrato(jasperPrint.getPageWidth(), jasperPrint.getPageHeight());
            this.saveJasperPrint(jasperPrint, statusTransfer.getId());

            return jasperPrint;
        } catch (JRException | IOException e) {
            GeraFrequenciaServices.STATUS_MAP.get(statusTransfer.getId()).setEstado_processo(RelatorioHandler.ESTADO_PROCESSO.ERRO);
            System.err.println(e);
            throw new ServiceException(e);
        }
    }

    public void exportar(String nomeArquivo, FormatoTransfer formato, String idRelatorioStatus) throws ServiceException {

        File relatorioArquivo = null;
        try {

            JasperPrint jasperPrint = this.getJasperPrint(idRelatorioStatus);

            //Cria arquivo temporario
            String fullRelatorioNome = System.getProperty("java.io.tmpdir") + File.separator + nomeArquivo + formato.getPrefixo();
            relatorioArquivo = new File(fullRelatorioNome);

            //Configura o arquivo para exportar
            Exporter exporter = new HtmlExporter(); //padrao html
            if (formato.getIdFormato().equals(CONSTANTES.Enum_FORMATO.PNG.getIdFormato())) {
                OutputStream ouputStream = new FileOutputStream(relatorioArquivo);
                DefaultJasperReportsContext.getInstance();
                //Configura imagem
                BufferedImage rendered_image = (BufferedImage) JasperPrintManager.printPageToImage(jasperPrint, 0, 1.6f);
//                 Imprime uma imagem a partir da primeira com zoom de 1.6
                ImageIO.write(rendered_image, "png", ouputStream);
            } else if (formato.getIdFormato().equals(CONSTANTES.Enum_FORMATO.TIFF.getIdFormato())) {
                OutputStream ouputStream = new FileOutputStream(relatorioArquivo);
                DefaultJasperReportsContext.getInstance();
                JasperPrintManager printManager = JasperPrintManager.getInstance(DefaultJasperReportsContext.getInstance());

                //Configura imagem
                ImageWriter writer = ImageIO.getImageWritersByFormatName("TIFF").next();
                try (ImageOutputStream output = ImageIO.createImageOutputStream(ouputStream)) {
                    writer.setOutput(output);
                    ImageWriteParam params = writer.getDefaultWriteParam();
                    params.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);

                    writer.prepareWriteSequence(null);

                    for (int i = 0; i < jasperPrint.getPages().size(); i++) {
                        BufferedImage image = (BufferedImage) printManager.printToImage(jasperPrint, i, 1.6f);
                        params.setCompressionType("PackBits");
                        writer.writeToSequence(new IIOImage(image, null, null), params);
                    }

                    writer.endWriteSequence();
                }

            } else {//Esse aqui em baixo tem mesmo formato de "entrada" mas diferentes de saida
                if (formato.getIdFormato().equals(CONSTANTES.Enum_FORMATO.CSV.getIdFormato())) {
                    exporter = new JRCsvExporter();
                    exporter.setExporterOutput(new SimpleWriterExporterOutput(relatorioArquivo));
                } else if (formato.getIdFormato().equals(CONSTANTES.Enum_FORMATO.HTML.getIdFormato())) {
                    exporter = new HtmlExporter();
                    SimpleHtmlReportConfiguration conf = new SimpleHtmlReportConfiguration();
                    conf.setZoomRatio(1.4f);//Gera um html com 1.6 de zoom
                    exporter.setConfiguration(conf);
                    exporter.setExporterOutput(new SimpleHtmlExporterOutput(relatorioArquivo));
                } else if (formato.getIdFormato().equals(CONSTANTES.Enum_FORMATO.TEXTO.getIdFormato())) {
                    exporter = new JRTextExporter();
                    exporter.setExporterOutput(new SimpleWriterExporterOutput(relatorioArquivo));

                    //Definições de tamanho dos caracteres
                    SimpleTextReportConfiguration config = new SimpleTextReportConfiguration();
                    config.setCharWidth(5f);
                    config.setCharHeight(13f);
                    exporter.setConfiguration(config);
                } else if (formato.getIdFormato().equals(CONSTANTES.Enum_FORMATO.XML.getIdFormato())) {
                    exporter = new JRXmlExporter();
                    exporter.setExporterOutput(new SimpleWriterExporterOutput(relatorioArquivo));
                } else if (formato.getIdFormato().equals(CONSTANTES.Enum_FORMATO.RTF.getIdFormato())) {//TODO: Tudo uma coluna só
                    exporter = new JRRtfExporter();
                    exporter.setExporterOutput(new SimpleWriterExporterOutput(relatorioArquivo));
                } else {

                    //Esses aqui tem o mesmo formato de saida e de entrada
                    if (formato.getIdFormato().equals(CONSTANTES.Enum_FORMATO.PDF.getIdFormato())) {
                        exporter = new JRPdfExporter();
                        SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
                        exporter.setConfiguration(configuration);
                    } else if (formato.getIdFormato().equals(CONSTANTES.Enum_FORMATO.EXCEL.getIdFormato())) {
                        exporter = new JRXlsExporter();
                    } else if (formato.getIdFormato().equals(CONSTANTES.Enum_FORMATO.ODT.getIdFormato())) {
                        exporter = new JROdtExporter();
                    } else if (formato.getIdFormato().equals(CONSTANTES.Enum_FORMATO.ODS.getIdFormato())) {
                        exporter = new JROdsExporter();
                    } else if (formato.getIdFormato().equals(CONSTANTES.Enum_FORMATO.WORD.getIdFormato())) {
                        exporter = new JRDocxExporter();
                    }
                    //Padrao pra todo mundo
                    exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(relatorioArquivo));

                }

                //Exporta arquivo
                ExporterInput exporterInput = new SimpleExporterInput(jasperPrint);
                exporter.setExporterInput(exporterInput);
                exporter.exportReport();
            }

        } catch (JRException | IOException | ClassNotFoundException e) {
            if (relatorioArquivo != null) {
                relatorioArquivo.delete();
            }

            throw new ServiceException(e);
        }
    }

    /**
     * Método que salva o objeto de relatorio 'jasperPrint' em um arquivo
     *
     * @param id identificador único do relatorio salvo
     * @param jasperPrint
     * @throws java.io.FileNotFoundException
     */
    public void saveJasperPrint(JasperPrint jasperPrint, String id) throws FileNotFoundException, IOException {
        String fullRelatorioNome = System.getProperty("java.io.tmpdir") + File.separator + id + ".jasperprint";
        File file = new File(fullRelatorioNome);
        FileOutputStream fo = new FileOutputStream(file);
        try (ObjectOutputStream oo = new ObjectOutputStream(fo)) {
            oo.writeObject(jasperPrint); // serializo objeto cat
        } // serializo objeto cat
    }

    /**
     * Método que recupera o objeto de relatorio 'jasperPrint' de um arquivo
     *
     * @param id identificador único do relatorio salvo
     * @return
     * @throws java.io.IOException
     * @throws java.lang.ClassNotFoundException
     */
    public JasperPrint getJasperPrint(String id) throws IOException, ClassNotFoundException {

        String fullRelatorioNome = System.getProperty("java.io.tmpdir") + File.separator + id + ".jasperprint";
        File file = new File(fullRelatorioNome);
        FileInputStream fi = new FileInputStream(file);
        JasperPrint jasperPrint;
        try (ObjectInputStream oi = new ObjectInputStream(fi)) {
            jasperPrint = (JasperPrint) oi.readObject();
        }

        return jasperPrint;
    }

}
