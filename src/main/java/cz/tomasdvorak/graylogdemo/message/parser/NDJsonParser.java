package cz.tomasdvorak.graylogdemo.message.parser;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Expects one JSON object on each line. See http://ndjson.org/
 */
@Service
public class NDJsonParser implements MessageParser {

    private final Logger logger = LoggerFactory.getLogger(NDJsonParser.class);

    /**
     * ObjectMapper is expensive resource to create, create only one per bean and reuse
     */
    private final JsonMapper mapper = new JsonMapper();

    @Override
    public List<MessageAttributes> parseMessages(final InputStream source) throws IOException {

        logger.info("Parsing messages from input stream");

        final List<MessageAttributes> messages = new LinkedList<>();

        final TypeReference<LinkedHashMap<String, Object>> typeRef
                = new TypeReference<LinkedHashMap<String, Object>>() {};

        try (
                final MappingIterator<LinkedHashMap<String, Object>> it = mapper.readerFor(typeRef).readValues(source)
        ) {
            while (it.hasNextValue()) {
                final LinkedHashMap<String, Object> node = it.nextValue();
                messages.add(new MessageAttributes(node));
            }
        }
        logger.info("Found and parsed {} messages", messages.size());
        return messages;
    }
}
