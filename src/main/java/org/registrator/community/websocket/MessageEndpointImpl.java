
package org.registrator.community.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.registrator.community.components.SpringApplicationContext;
import org.registrator.community.util.Throwables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.UUID;

@ServerEndpoint(value = "/websocket/messages/{sessionId}")
@PreAuthorize("hasRole('ROLE_ANONYMOUS') or hasRole('ROLE_ADMIN')")
public class MessageEndpointImpl implements MessageEndpoint {

    private static final Logger LOG = LoggerFactory.getLogger(MessageEndpointImpl.class);

    private String httpSessionId;
    private String wsSessionId;
    private Session session;

    private static final MessagingService messagingService = SpringApplicationContext.getBean(MessagingService.class);

    public MessageEndpointImpl() {
        wsSessionId = UUID.randomUUID().toString();
    }

    @OnOpen
    public void start(@PathParam("sessionId") String httpSessionId, Session session) {
        this.httpSessionId = httpSessionId;
        this.session = session;

        messagingService.addClient(httpSessionId, this);
    }


    @OnClose
    public void end() {
        messagingService.deleteClient(this);
    }


    @OnMessage
    public void incoming(String jsonMessage) {
        ObjectMapper mapper = new ObjectMapper();
        Message message = null;
        try {
            message = mapper.readValue(jsonMessage, Message.class);
            messagingService.processMessage(httpSessionId, message);
        } catch (IOException e) {
            LOG.error("Couldn't read message from WebSocket client, message {}, reason {}",
                    jsonMessage, Throwables.getRootCause(e).getMessage(), e);
        }
    }


    @OnError
    public void onError(Throwable t) throws Throwable {
        LOG.error("WebSocket Error: " + t.toString(), t);
    }

    @Override
    public void sendMessage(Message message) throws IOException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            session.getBasicRemote().sendText(mapper.writeValueAsString(message));
        } catch (IOException e) {
            LOG.warn("Sending WebSocket message failed, connection closed. Root cause: {}",
                    Throwables.getRootCause(e).getMessage());
            try {
                session.close();
            } catch (IOException ex) {
                LOG.error("Couldn't close WebSocket session. Root cause: {}",
                        Throwables.getRootCause(ex).getMessage());
            }
            throw new IOException(e);
        }
    }

    @Override
    public String getHttpSessionId() {
        return httpSessionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MessageEndpointImpl that = (MessageEndpointImpl) o;

        return wsSessionId.equals(that.wsSessionId)
                && (httpSessionId != null)
                && (that.httpSessionId != null)
                && (httpSessionId.equals(that.httpSessionId));

    }

    @Override
    public int hashCode() {
        int result = httpSessionId != null ? httpSessionId.hashCode() : 0;
        result = 31 * result + wsSessionId.hashCode();
        return result;
    }
}
