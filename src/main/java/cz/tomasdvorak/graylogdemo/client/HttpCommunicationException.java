package cz.tomasdvorak.graylogdemo.client;

public class HttpCommunicationException extends RuntimeException {

    public HttpCommunicationException(final Throwable cause) {
        super(cause);
    }

    public HttpCommunicationException(final String message) {
        super(message);
    }
}
