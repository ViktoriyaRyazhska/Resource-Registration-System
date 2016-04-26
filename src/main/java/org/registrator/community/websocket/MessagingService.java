package org.registrator.community.websocket;

/**
 * Messaging handler service. Message are sent to all clients with the same http session id
 *
 */
public interface MessagingService {

    /**
     * Add new client with specified http session
     * @param httpSessionId http session id
     * @param client client
     */
    void addClient(String httpSessionId, MessageEndpoint client);

    /**
     * Unregister the client from service
     * @param client client that need to be unredistered
     */
    void deleteClient(MessageEndpoint client);

    /**
     * Send prepared message to all clients with the same http session id
     */
    void sendMessage(String httpSessionId, Message message);

    /**
     * Prepare and send message
     */
    void sendMessage(String httpSessionId, String message);

    /**
     * Process message from client
     */
    void processMessage(String httpSessionId, Message message);

}
