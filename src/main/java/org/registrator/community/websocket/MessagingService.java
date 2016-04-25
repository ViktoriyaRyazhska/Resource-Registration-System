package org.registrator.community.websocket;

/**
 * Created by roman.golyuk on 25.04.2016.
 */
public interface MessagingService {
    void addClient(String httpSessionId, MessageEndpoint client);

    void deleteClient(MessageEndpoint client);

    void sendMessage(String httpSessionId, String message);

    void processMessage(String httpSessionId, String message);

}
