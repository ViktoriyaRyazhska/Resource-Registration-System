package org.registrator.community.websocket;

import org.registrator.community.util.LocalizationConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Implementation of WebSocket messaging service
 */
@Service
public class MessagingServiceImpl implements MessagingService {
    private static final Logger LOG = LoggerFactory.getLogger(MessagingServiceImpl.class);

    private static final Map<String, List<MessageEndpoint>> connections = new ConcurrentHashMap<>();

    @Override
    public void addClient(String httpSessionId, MessageEndpoint client) {
        LOG.debug("Subscribing new WebSocket client {}", httpSessionId);
        List<MessageEndpoint> clientList = connections.get(httpSessionId);
        if (clientList == null) {
            clientList = new CopyOnWriteArrayList<>();
        }
        clientList.add(client);
        connections.put(httpSessionId, clientList);
        LOG.debug("WebSocket client {} was successfully subscribed", httpSessionId);
    }

    @Override
    public void deleteClient(MessageEndpoint client) {
        String httpSessionId = client.getHttpSessionId();
        LOG.debug("Unsubscribing WebSocket client {}", httpSessionId);
        connections.get(httpSessionId).remove(client);
        LOG.debug("WebSocket client {} was disconnected", httpSessionId);
    }

    @Override
    public void sendMessage(String httpSessionId, Message message) {
        LOG.debug("Sending message to WebSocket client {}", httpSessionId);
        broadcastMessage(httpSessionId, message);
    }

    @Override
    public void sendMessage(String httpSessionId, String messageText) {
        Message message = new Message(LocalizationConst.WS_TITLE, messageText);
        sendMessage(httpSessionId, message);
    }

    @Override
    public void processMessage(String httpSessionId, Message message) {
        LOG.debug("Processing message from WebSocket client {}, message: {}", httpSessionId, message);
        if (message.getCommand() == MessageCommand.CLOSE) {
            LOG.debug("Sending close command");
            broadcastMessage(httpSessionId, message);
        } else {
            LOG.warn("Get malformed message from client, message {}", message);
        }
    }

    private void broadcastMessage(String clientId, Message message) {
        List<MessageEndpoint> connectionList = connections.get(clientId);
        if (connectionList == null) {
            return;
        }

        Iterator<MessageEndpoint> iterator = connectionList.iterator();
        while (iterator.hasNext()) {
            MessageEndpoint endpoint = iterator.next();
            try {
                endpoint.sendMessage(message);
            } catch (IOException e) {
                //everything was already logged
                iterator.remove();
            }
        }
    }
}
