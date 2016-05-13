package org.registrator.community.websocket;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Message to send via WebSocket
 */
public class Message {
    private static final AtomicInteger messageIds = new AtomicInteger(0);

    @JsonProperty("id")
    private int id;
    private String title;
    private String text;
    private MessageCommand command;

    /**
     * Creates new message to show
     */
    public Message(String title, String text) {
        this(title, text, MessageCommand.SHOW);
    }

    /**
     * Creates new message with specified command
     */
    public Message(String title, String text, MessageCommand command) {
        this.id = messageIds.getAndIncrement();
        this.title = title;
        this.text = text;
        this.command = command;
    }

    public Message() {}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public MessageCommand getCommand() {
        return command;
    }

    public void setCommand(MessageCommand command) {
        this.command = command;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Message{" +
                "title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", command=" + command +
                '}';
    }
}
