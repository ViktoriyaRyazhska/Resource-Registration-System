/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.registrator.community.websocket;

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
    public void incoming(String message) {
        messagingService.processMessage(httpSessionId, message);
    }


    @OnError
    public void onError(Throwable t) throws Throwable {
        LOG.error("WebSocket Error: " + t.toString(), t);
    }

    @Override
    public void sendMessage(String message) throws IOException {
        try {
            session.getBasicRemote().sendText(message);
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
