package cz.tomasdvorak.graylogdemo.client;

import cz.tomasdvorak.graylogdemo.message.GraylogMessage;

public interface GelfCHttpClient {
    void sendMessage(GraylogMessage message);
}
