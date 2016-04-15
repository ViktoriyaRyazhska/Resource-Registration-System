package org.registrator.community.entity;

import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
public class SmtpParameters {

    public enum Protocol {
        SMTP,
        SMTPS
    }

    @Column(length = 100)
    @NotEmpty(message = "{msg.notEmptyField}")
    private String host = "";

    @Column(length = 5)
    @Enumerated(EnumType.STRING)
    @NotEmpty(message = "{msg.notEmptyField}")
    private Protocol protocol = Protocol.SMTP;

    @Column
    @NotEmpty(message = "{msg.notEmptyField}")
    private int port = 465;

    @Column(length = 100)
    private String username = "";

    @Column(length = 20)
    private String password = "";

    @Column
    private boolean tlsEnabled = true;

    public SmtpParameters() {}

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Protocol getProtocol() {
        return protocol;
    }

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getTlsEnabled() {
        return tlsEnabled;
    }

    public void setTlsEnabled(boolean enableTLS) {
        this.tlsEnabled = enableTLS;
    }
}
