package com.example.graduationFrontend.config.serial;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.Date;

import static com.example.graduationFrontend.constants.HttpConstants.DEFAULT_DATE_FORMAT;

public class ObjectSerializer {
    public final static DateSerializer DATE_INSTANCE = new DateSerializer();

    private static class DateSerializer extends JsonSerializer<Date> {
        @Override
        public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            if(value != null) {
                gen.writeString(DEFAULT_DATE_FORMAT.format(value));
            }
        }

        @Override
        public Class<Date> handledType() {
            return Date.class;
        }
    }

}
