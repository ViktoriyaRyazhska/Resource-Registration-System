package org.registrator.community.service;

import org.registrator.community.dto.SettingsDTO;
import org.registrator.community.dto.SmtpParametersDTO;
import org.registrator.community.entity.Settings;
import org.registrator.community.entity.SmtpParameters;
import org.registrator.community.enumeration.RegistrationMethod;

import java.util.TimeZone;

/**
 * Service to get and save application settings.
 */
public interface SettingsService {

    RegistrationMethod getRegistrationMethod();

    void setRegistrationMethod(RegistrationMethod registrationMethod);

    TimeZone getTimeZone();

    void setTimeZone(TimeZone timeZone);

    /**
     * get current SMTP parameters from database
     */
    SmtpParameters getSmtpParameters();

    /**
     * get SMTP parameters from client
     */
    SmtpParameters parseSmtpParameters(SmtpParametersDTO smtpParametersDTO);

    SettingsDTO getAllSettingsDTO();

    void saveAll(SettingsDTO settings);

}
