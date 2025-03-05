package com.example.backend.config.serial;

import com.example.backend.utils.object.DateUtils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.util.NumberUtils;

import java.io.IOException;
import java.util.Date;

public class ObjectDeserializer {
    public static final DateDeserializer DATE_DESERIALIZER_INSTANCE = new DateDeserializer();

    private static class DateDeserializer extends JsonDeserializer<Date> {
        @Override
        public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            String dateText = p.getText();
            try {
                Long timestamp = NumberUtils.parseNumber(dateText, Long.class);
                return new Date(timestamp);
            } catch (NumberFormatException e) {
            }
            if(!dateText.contains(" ")) {
                dateText += " 00:00:00";
            }
            return DateUtils.defaultFormat(dateText);
        }
    }
}
