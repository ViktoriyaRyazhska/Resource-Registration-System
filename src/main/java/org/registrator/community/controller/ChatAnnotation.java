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
package org.registrator.community.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.context.request.RequestContextHolder;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@ServerEndpoint(value = "/websocket/chat/{sessionId}")
@PreAuthorize("hasRole('ROLE_ANONYMOUS') or hasRole('ROLE_ADMIN')")
public class ChatAnnotation {

    private static final Logger logger = LoggerFactory.getLogger(ChatAnnotation.class);

    private static final Map<String, List<ChatAnnotation>> connections = new ConcurrentHashMap<>();

    private String sessionId;
    private Session session;

    public ChatAnnotation() {

    }

    @OnOpen
    public void start(@PathParam("sessionId") String sessionId, Session session) {
        this.session = session;
        this.sessionId = sessionId;

        List<ChatAnnotation> list = connections.get(sessionId);
        if (list == null) {
            list = new CopyOnWriteArrayList<>();
        }
        list.add(this);
        connections.put(sessionId, list);
        String message = String.format("* %s %s", this.sessionId, "has joined.");
        broadcast(message);
    }


    @OnClose
    public void end() {
        connections.remove(sessionId);
        String message = String.format("* %s %s",
                sessionId, "has disconnected.");
        broadcast(message);
    }


    @OnMessage
    public void incoming(String message) {
        broadcast(message);
    }




    @OnError
    public void onError(Throwable t) throws Throwable {
        logger.error("Chat Error: " + t.toString(), t);
    }


    private static void broadcast(String msg) {
        for (Map.Entry<String, List<ChatAnnotation>> clientEntry : connections.entrySet()) {
            for (ChatAnnotation client: clientEntry.getValue()) {
                try {
                    synchronized (client) {
                        client.session.getBasicRemote().sendText(msg);
                    }
                } catch (IOException e) {
                    logger.debug("Chat Error: Failed to send message to client", e);
                    connections.remove(client.sessionId);
                    try {
                        client.session.close();
                    } catch (IOException e1) {
                        // Ignore
                    }
                    String message = String.format("* %s %s",
                            client.sessionId, "has been disconnected.");
                    broadcast(message);
                }
            }
        }
    }
}
