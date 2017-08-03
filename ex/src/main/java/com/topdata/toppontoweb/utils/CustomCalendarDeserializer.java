package com.topdata.toppontoweb.utils;

import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;

/**
 *
 * @author juliano.ezequiel
 */
public class CustomCalendarDeserializer extends JsonDeserializer<Calendar> {

   
    @Override
    public Calendar deserialize(JsonParser jsonparser, DeserializationContext context)
            throws IOException, JsonProcessingException {
        String dateAsString = jsonparser.getText();
        try {
            Date date = CustomCalendarSerializer.FORMATTER.parse(dateAsString);
            Calendar calendar = Calendar.getInstance(
                    CustomCalendarSerializer.LOCAL_TIME_ZONE,
                    CustomCalendarSerializer.LOCALE_BRAZIL
            );
            calendar.setTime(date);
            return calendar;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

}
