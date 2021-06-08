package cz.tomasdvorak.graylogdemo.message.parser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

class NDJsonParserTest {

    @Test
    void parseMessages() throws IOException {

        final String initialString = "" +
                "{\"foo\":\"bar\"}\n" +
                "{\"lorem\":\"ipsum\", \"value\":12}\n";
        final InputStream targetStream = new ByteArrayInputStream(initialString.getBytes());

        final List<MessageAttributes> messages = new NDJsonParser().parseMessages(targetStream);
        Assertions.assertEquals(2, messages.size());
        final MessageAttributes firstMessage = messages.get(0);
        final MessageAttributes secondMessage = messages.get(1);

        Assertions.assertEquals("bar", firstMessage.getAttributes().get("foo"));
        Assertions.assertEquals("ipsum", secondMessage.getAttributes().get("lorem"));
        Assertions.assertEquals(12, secondMessage.getAttributes().get("value"));

        System.out.println(messages);

    }
}