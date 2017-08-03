/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.topdata.toppontoweb.utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

/**
 *
 * @author tharle.camargo
 */
public class CustomHourSerializer extends JsonSerializer<Date>{
    private static final SimpleDateFormat HOURFORMAT = new SimpleDateFormat("HH:mm");

    @Override
    public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException, org.codehaus.jackson.JsonProcessingException {

        String formattedDate = HOURFORMAT.format(date);
        formattedDate = formattedDate.replaceAll("^0", "");
        jsonGenerator.writeString(formattedDate);
    }
}
