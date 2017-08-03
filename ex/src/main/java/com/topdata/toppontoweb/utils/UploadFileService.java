package com.topdata.toppontoweb.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoResponse;
import com.topdata.toppontoweb.utils.constantes.MSG;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @version 1.0.0.0 data 24/01/2017
 * @since 1.0.0.0 data 29/08/2016
 *
 * @author juliano.ezequiel
 */
@Component
public class UploadFileService {

    @Autowired
    private TopPontoResponse topPontoResponse;

    @Value("${img.relativePath}")
    private String path;

    private final long MAX_SIZE = 2048 * 1000; // 2MB

    public Response upload(HttpServletRequest request, InputStream uploadedInputStream,
            FormDataContentDisposition fileDetail, String foto) throws ServiceException {

        //verifica o caminho de upload
        String realPathtoUploads;
        realPathtoUploads = request.getServletContext().getRealPath(path);

        //caso não exista cria o diretório
        if (!new File(realPathtoUploads).exists()) {
            if (new File(realPathtoUploads).mkdirs() == false) {
                System.out.println("Erro");
            }
        }

        //caminho completo para receber o arquivo
        String uploadedFileLocation = realPathtoUploads + foto.toLowerCase();
        
        //salva em disco o arquivo recebido
        writeToFile(uploadedInputStream, uploadedFileLocation);
        File f = new File(uploadedFileLocation);

        if (f.length() > (MAX_SIZE)) {
            f.delete();
            throw new ServiceException(this.topPontoResponse.alertaValidacao(MSG.IMAGEM_TAMANHO_MAX));
        }

        //valida o tipo salvo
        MimetypesFileTypeMap fileTypeMap = new MimetypesFileTypeMap();
        fileTypeMap.addMimeTypes("image/png png PNG");
        String[] type = fileTypeMap.getContentType(f).split("/");
        //caso seja um arquivo inválido
        if (type[0].contains("application")) {
            f.delete();
            throw new ServiceException(this.topPontoResponse.alertaValidacao(MSG.IMAGEM_INVALIDA));
        }
        //caso seja uma extenção inválida
        if ((type.length > 1 && !type[1].equals("jpg") && !type[1].equals("jpeg") && !type[1].equals("png"))) {
            f.delete();
            throw new ServiceException(this.topPontoResponse.alertaValidacao(MSG.IMAGEM_TIPOS_SUPORTADOS));
        }

        return Response.status(200).build();
    }

    //salva o arquivo em discoF
    private void writeToFile(InputStream uploadedInputStream, String uploadedFileLocation) {
        try {
            OutputStream out = new FileOutputStream(new File(uploadedFileLocation));
            int read = 0;
            byte[] bytes = new byte[1024];

            out = new FileOutputStream(new File(uploadedFileLocation));
            while ((read = uploadedInputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
