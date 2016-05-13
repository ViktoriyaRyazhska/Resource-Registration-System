package org.registrator.community.websocket;

import java.io.IOException;

/**
 * Transport messages to client
 */
public interface MessageEndpoint {
    void sendMessage(Message message) throws IOException;

    String getHttpSessionId();
}
