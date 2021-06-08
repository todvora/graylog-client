package cz.tomasdvorak.graylogdemo.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import cz.tomasdvorak.graylogdemo.message.parser.MessageAttributes;
import cz.tomasdvorak.graylogdemo.message.serializer.CustomDateSerializer;

import java.util.Date;

public class GraylogMessage {
    @JsonProperty
    private final String version = "1.1";
    @JsonProperty
    private final String host;
    @JsonProperty("short_message")
    private final String shortMessage;
    @JsonProperty
    @JsonSerialize(using = CustomDateSerializer.class)
    private final Date timestamp;

    @JsonUnwrapped
    private final MessageAttributes additionalAttributes;

    public GraylogMessage(final String host, final String shortMessage, final MessageAttributes additionalAttributes) {
        this.host = host;
        this.shortMessage = shortMessage;
        this.additionalAttributes = additionalAttributes;
        timestamp = new Date();
    }
}
