package cz.tomasdvorak.graylogdemo.message.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.Date;

/**
 * Convert java date to unix timestamp (seconds since UNIX epoch)
 * https://docs.graylog.org/en/4.0/pages/gelf.html
 */
public class CustomDateSerializer extends JsonSerializer<Date> {

    @Override
    public void serialize(final Date value, final JsonGenerator gen, final SerializerProvider serializers) throws IOException {
        gen.writeNumber(value.getTime() / 1000);
    }
}
