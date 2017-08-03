package com.topdata.toppontoweb.utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

/**
 * Serializa a data para o formato brasileiro
 *
 * @version 1.0.4 data 15/08/2016
 * @since 1.0.4 data 15/08/2016
 *
 * @author juliano.ezequiel
 */
public class CustomDateSerializer extends JsonSerializer<Date> {

    private static final SimpleDateFormat DATEFORMAT = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException, JsonProcessingException {

        String formattedDate = DATEFORMAT.format(date);
        jsonGenerator.writeString(formattedDate);
    }

}
