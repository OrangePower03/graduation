package com.example.backend.config.serial;

import com.example.backend.utils.object.DateUtils;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.Date;

public class ObjectSerializer {
    public final static DateSerializer DATE_INSTANCE = new DateSerializer();
    public final static LongSerializer LONG_INSTANCE = new LongSerializer();

    private static class DateSerializer extends JsonSerializer<Date> {
        @Override
        public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            if(value != null) {
                gen.writeString(DateUtils.defaultFormat(value));
            }
        }

        @Override
        public Class<Date> handledType() {
            return Date.class;
        }
    }

    public static class LongSerializer extends JsonSerializer<Long> {
        @Override
        public void serialize(Long value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            if(value != null) {
                gen.writeString(value.toString());
//                gen.writeNumber(value);
            }
        }

        @Override
        public Class<Long> handledType() {
            return Long.class;
        }
    }
}
