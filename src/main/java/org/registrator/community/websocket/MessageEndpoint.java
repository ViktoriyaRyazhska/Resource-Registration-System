package org.registrator.community.websocket;

import java.io.IOException;

/**
 * Created by roman.golyuk on 26.04.2016.
 */
public interface MessageEndpoint {
    void sendMessage(String message) throws IOException;

    String getHttpSessionId();
}
