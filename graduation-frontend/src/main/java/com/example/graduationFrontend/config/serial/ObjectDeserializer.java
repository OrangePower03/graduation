package com.example.graduationFrontend.config.serial;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import static com.example.graduationFrontend.constants.HttpConstants.DEFAULT_DATE_FORMAT;

public class ObjectDeserializer {
    public static final DateDeserializer DATE_DESERIALIZER_INSTANCE = new DateDeserializer();

    private static class DateDeserializer extends JsonDeserializer<Date> {
        @Override
        public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String dateText = p.getText();
            if(!dateText.contains("T")) {
                dateText += "T00:00:00Z";
            }
            try {
                return DEFAULT_DATE_FORMAT.parse(dateText);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
