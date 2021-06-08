package cz.tomasdvorak.graylogdemo.message.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.tomasdvorak.graylogdemo.message.GraylogMessage;
import cz.tomasdvorak.graylogdemo.message.parser.MessageAttributes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;

public class MessageSerializerImplTest {

    private MessageSerializerImpl serializer;
    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        this.serializer = new MessageSerializerImpl();
    }

    @Test
    void verifyAdditionalFields() throws JsonProcessingException {

        final Map<String, Object> additionalFields = new LinkedHashMap<>();
        additionalFields.put("ClientDeviceType", "mobile");
        additionalFields.put("ClientIP", "225.127.185.42");
        additionalFields.put("ClientStatus", 200);

        final GraylogMessage message = new GraylogMessage("junit", "junit-serialization-test", new MessageAttributes(additionalFields));

        final String serialized = serializer.messageToJson(message);
        final JsonNode parsed = mapper.readValue(serialized, JsonNode.class);

        // additional fields should start underscore and have the right data type
        Assertions.assertEquals("mobile", parsed.get("_ClientDeviceType").asText());
        Assertions.assertEquals("225.127.185.42", parsed.get("_ClientIP").asText());
        Assertions.assertEquals(200, parsed.get("_ClientStatus").asInt());
    }
}