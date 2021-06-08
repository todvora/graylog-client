package cz.tomasdvorak.graylogdemo.message.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.tomasdvorak.graylogdemo.message.GraylogMessage;
import org.springframework.stereotype.Service;

@Service
public class MessageSerializerImpl implements MessageSerializer {


    /**
     * ObjectMapper is expensive resource to create, create only one per bean and reuse
     */
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String messageToJson(final GraylogMessage message) throws MessageSerializationException {
        try {
            return mapper.writeValueAsString(message);
        } catch (final JsonProcessingException e) {
            throw new MessageSerializationException(e);
        }
    }
}
