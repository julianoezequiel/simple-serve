package com.topdata.toppontoweb.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

/**
 *
 * @author juliano.ezequiel
 */
public class CustomCalendarSerializer extends JsonSerializer<Calendar> {

    public static final SimpleDateFormat FORMATTER = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    public static final Locale LOCALE_BRAZIL = new Locale("pt", "BR");
    public static final TimeZone LOCAL_TIME_ZONE = TimeZone.getDefault();

    @Override
    public void serialize(Calendar value, JsonGenerator gen, SerializerProvider arg2)
            throws IOException, JsonProcessingException {
        if (value == null) {
            gen.writeNull();
        } else {
            gen.writeString(FORMATTER.format(value.getTime()));
        }
    }

}
