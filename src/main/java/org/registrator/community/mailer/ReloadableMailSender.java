package org.registrator.community.mailer;

import org.registrator.community.entity.SmtpParameters;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * Mail sender with the ability to reload SMTP properties in atomic operation
 */
public interface ReloadableMailSender extends JavaMailSender {
    boolean testConnection(SmtpParameters smtpParameters);

    void applyNewParameters(SmtpParameters smtpParameters);
}
