package org.registrator.community.service.impl;

import org.registrator.community.dao.SettingsRepository;
import org.registrator.community.dto.SettingsDTO;
import org.registrator.community.dto.SmtpParametersDTO;
import org.registrator.community.entity.Settings;
import org.registrator.community.entity.SmtpParameters;
import org.registrator.community.enumeration.RegistrationMethod;
import org.registrator.community.service.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.TimeZone;

/**
 * Implementation of SettingsService
 *
 * @see org.registrator.community.service.SettingsService
 */

@Service
public class SettingsServiceImpl implements SettingsService {

    @Autowired
    private SettingsRepository settingsRepository;

    private static volatile Settings currentSettings;

    @Override
    public SettingsDTO getAllSettingsDTO() {
        Settings settings = getCurrentSettings();
        SettingsDTO settingsDTO = new SettingsDTO();
        settingsDTO.setRegistrationMethod(settings.getRegistrationMethod().toString());
        settingsDTO.setTimeZone(settings.getTimeZone().getID());
        settingsDTO.setSmtpParameters(SmtpParametersDTO.from(settings.getSmtpParameters()));
        return settingsDTO;
    }

    private Settings getCurrentSettings() {
        if (currentSettings == null) {
            synchronized (this) {
                if (currentSettings == null) {
                    currentSettings = settingsRepository.getAllSettings();
                    //TODO RG move this 'if' in repository
                    if (currentSettings == null) {
                        currentSettings = new Settings();
                    }
                }
            }
        }
        return currentSettings;
    }

    @Override
    @Transactional
    public synchronized void saveAll(SettingsDTO settingsDTO) {
        Settings settings = getCurrentSettings();
        settings.setRegistrationMethod(RegistrationMethod.valueOf(settingsDTO.getRegistrationMethod()));
        settings.setTimeZone(TimeZone.getTimeZone(settingsDTO.getTimeZone()));
        settings.setSmtpParameters(parseSmtpParameters(settingsDTO.getSmtpParameters()));
        settingsRepository.save(settings);
    }

    @Override
    @Transactional
    public synchronized void setTimeZone(TimeZone timeZone) {
        Settings settings = getCurrentSettings();
        settings.setTimeZone(timeZone);
        settingsRepository.save(settings);
    }

    @Override
    public TimeZone getTimeZone() {
        return getCurrentSettings().getTimeZone();
    }

    @Override
    @Transactional
    public synchronized void setRegistrationMethod(RegistrationMethod registrationMethod) {
        Settings settings = getCurrentSettings();
        settings.setRegistrationMethod(registrationMethod);
        settingsRepository.save(settings);
    }

    @Override
    public RegistrationMethod getRegistrationMethod() {
        return getCurrentSettings().getRegistrationMethod();
    }

    @Override
    public SmtpParameters getSmtpParameters() {
        SmtpParameters result = getCurrentSettings().getSmtpParameters();
        if (result == null) {
            result = new SmtpParameters();
        }
        return result;
    }

    @Override
    public SmtpParameters parseSmtpParameters(SmtpParametersDTO smtpParametersDTO) {
        SmtpParameters result = SmtpParameters.from(smtpParametersDTO);
        // if password was not changed use current
        if (!smtpParametersDTO.getPasswordChanged()) {
            result.setPassword(getCurrentSettings().getSmtpParameters().getPassword());
        }
        return result;
    }
}
