package cz.tomasdvorak.graylogdemo.message.serializer;

import cz.tomasdvorak.graylogdemo.message.GraylogMessage;

public interface MessageSerializer {
    String messageToJson(GraylogMessage message) throws MessageSerializationException;
}
