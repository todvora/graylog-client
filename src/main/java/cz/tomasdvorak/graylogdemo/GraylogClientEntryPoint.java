package cz.tomasdvorak.graylogdemo;

import cz.tomasdvorak.graylogdemo.client.GelfCHttpClient;
import cz.tomasdvorak.graylogdemo.message.GraylogMessage;
import cz.tomasdvorak.graylogdemo.message.parser.MessageParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
public class GraylogClientEntryPoint implements CommandLineRunner {

    @Autowired
    private MessageParser messageParser;

    @Autowired
    private GelfCHttpClient gelfClient;

    @Value("${graylog.client.host}")
    private String host;

    @Value("${graylog.client.message}")
    private String message;

    @Value("${graylog.client.inputFile}")
    private String inputFilePath;

    private final Logger logger = LoggerFactory.getLogger(GraylogClientEntryPoint.class);

    @Override
    public void run(final String... args) throws Exception {
        logger.info("Graylog client started");
        logger.info("Messages from file {} will be parsed and forwarded to the server", inputFilePath);
        try (
                final InputStream source = getClass().getResourceAsStream(inputFilePath)
        ) {
            final long messagesSent = messageParser.parseMessages(source) // read all messages from the resource
                    .stream()
                    .map(m -> new GraylogMessage(host, message, m)) // convert them to GELF messages
                    .peek(m -> gelfClient.sendMessage(m)) // send each synchronously to the Graylog server
                    .count();

            logger.info("Sending messages finished, sent {} messages", messagesSent);
            logger.info("All done, application terminating now");
        }
    }
}
