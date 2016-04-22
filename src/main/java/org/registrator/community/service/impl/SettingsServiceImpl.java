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

    @Override
    public SettingsDTO getAllSettingsDTO() {
        Settings settings = settingsRepository.getAllSettings();
        SettingsDTO settingsDTO = new SettingsDTO();
        settingsDTO.setRegistrationMethod(settings.getRegistrationMethod().toString());
        settingsDTO.setTimeZone(settings.getTimeZone().getID());
        settingsDTO.setSmtpParameters(SmtpParametersDTO.from(settings.getSmtpParameters()));
        return settingsDTO;
    }

    @Override
    @Transactional
    public Settings getAllSettings() {
        Settings result = settingsRepository.getAllSettings();
        if (result == null) {
            result = new Settings();
        }
        return result;
    }

    @Override
    @Transactional
    public void saveAll(SettingsDTO settingsDTO) {
        Settings settings = getAllSettings();
        settings.setRegistrationMethod(RegistrationMethod.valueOf(settingsDTO.getRegistrationMethod()));
        settings.setTimeZone(TimeZone.getTimeZone(settingsDTO.getTimeZone()));
        settings.setSmtpParameters(SmtpParameters.from(settingsDTO.getSmtpParameters()));
        settingsRepository.save(settings);
    }

    @Override
    @Transactional
    public void setTimeZone(TimeZone timeZone) {
        Settings settings = getAllSettings();
        settings.setTimeZone(timeZone);
        settingsRepository.save(settings);
    }

    @Override
    @Transactional
    public TimeZone getTimeZone() {
        return settingsRepository.getAllSettings().getTimeZone();
    }

    @Override
    @Transactional
    public void setRegistrationMethod(RegistrationMethod registrationMethod) {
        Settings settings = getAllSettings();
        settings.setRegistrationMethod(registrationMethod);
        settingsRepository.save(settings);
    }

    @Override
    @Transactional
    public RegistrationMethod getRegistrationMethod() {
        return getAllSettings().getRegistrationMethod();
    }

    @Override
    @Transactional
    public SmtpParameters getSmtpParameters() {
        SmtpParameters result = getAllSettings().getSmtpParameters();
        if (result == null) {
            result = new SmtpParameters();
        }
        return result;
    }
}
