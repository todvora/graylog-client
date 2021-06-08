package cz.tomasdvorak.graylogdemo.message.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface MessageParser {
    List<MessageAttributes> parseMessages(InputStream source) throws IOException;
}
