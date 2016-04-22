package org.registrator.community.dto;

import org.registrator.community.entity.SmtpParameters;

import javax.validation.constraints.NotNull;

public class SettingsDTO {

    private String registrationMethod;
    private String timeZone;
    private boolean success;
    private boolean error;
    private SmtpParametersDTO smtpParametersDTO;

    public SettingsDTO() {}

    public String getRegistrationMethod() {
        return registrationMethod;
    }

    public void setRegistrationMethod(String registrationMethod) {
        this.registrationMethod = registrationMethod;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public boolean getSuccess() {
        return success;
    }

    public boolean getError() {
        return error;
    }

    public void setSmtpParameters(SmtpParametersDTO smtpParameters) {
        this.smtpParametersDTO = smtpParameters;
    }

    public SmtpParametersDTO getSmtpParameters() {
        return smtpParametersDTO;
    }

}
