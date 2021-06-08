package cz.tomasdvorak.graylogdemo.message.serializer;

public class MessageSerializationException extends RuntimeException {
    public MessageSerializationException(final Throwable cause) {
        super(cause);
    }
}
