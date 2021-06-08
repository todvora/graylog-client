package cz.tomasdvorak.graylogdemo.client;

import cz.tomasdvorak.graylogdemo.message.GraylogMessage;
import cz.tomasdvorak.graylogdemo.message.serializer.MessageSerializer;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.io.Closeable;
import java.io.IOException;

@Service
public class GelfHttpClientImpl implements GelfCHttpClient, Closeable {

    private final Logger logger = LoggerFactory.getLogger(GelfHttpClientImpl.class);

    @Autowired
    private MessageSerializer messageSerializer;

    private final CloseableHttpClient client;

    @Value("${graylog.server.url}")
    private String graylogServerUrl;


    public GelfHttpClientImpl() {
        this.client = HttpClients.createDefault();
    }

    @Override
    public void sendMessage(final GraylogMessage message) {
        final HttpPost request = new HttpPost(graylogServerUrl);
        request.setHeader("Content-Type", "application/json");
        final StringEntity entity = new StringEntity(messageSerializer.messageToJson(message), ContentType.APPLICATION_JSON);
        request.setEntity(entity);

        try (final CloseableHttpResponse response = client.execute(request)) {
            final int statusCode = response.getStatusLine().getStatusCode();
            logger.debug("GELF message sent: {}", response.getStatusLine());
            if (statusCode != 202) {
                final String errorMessage = String.format("Unexpected HTTP return code, expected 202, got %d. Status: %s", statusCode, response.getStatusLine());
                throw new HttpCommunicationException(errorMessage);
            }
        } catch (final IOException e) {
            throw new HttpCommunicationException(e);
        }
    }

    @PreDestroy
    @Override
    public void close() throws IOException {
        this.client.close();
    }
}
