package com.dp.spring.parallel.common.formatters;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LocalTimeDeserializer extends JsonDeserializer<LocalTime> {

    @Override
    public LocalTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String timeStr = p.getValueAsString();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_TIME;
        return LocalTime.parse(timeStr, formatter);
    }
}
