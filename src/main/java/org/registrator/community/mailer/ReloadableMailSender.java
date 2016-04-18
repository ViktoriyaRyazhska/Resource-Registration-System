package org.registrator.community.mailer;

import org.registrator.community.entity.SmtpParameters;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * Created by roman.golyuk on 18.04.2016.
 */
public interface ReloadableMailSender extends JavaMailSender {
    boolean testConnection(SmtpParameters smtpParameters);

    void applyNewParameters(SmtpParameters smtpParameters);
}
