package org.registrator.community.controller.administrator;

import org.registrator.community.dto.SettingsDTO;
import org.registrator.community.service.MailService;
import org.registrator.community.service.SettingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.*;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

@Controller
@RequestMapping(value = "/administrator/")
public class SettingsController {

    private static final Logger logger = LoggerFactory.getLogger(SettingsController.class);

    @Autowired
    private SettingsService settingsService;

    @Autowired MailService mailService;

    private static final Validator timeZoneValidator = new SettingsValidator();

    private static final Validator smtpValidator = new SmtpParametersValidator();
    /**
     * Method for showing administrator settings in order to change registration
     * method
     *
     * @param model
     * @return adminSettings.jsp
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/settings", method = RequestMethod.GET)
    public String showSettings(Model model) {
        if (!model.containsAttribute("settings")) {
            model.addAttribute("settings", settingsService.getAllSettingsDTO());
        }
        return "adminSettings";
    }

    /**
     * Method for changing administrator settings for one of the possible
     * options
     *
     * @return adminSettings.jsp
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/settings", method = RequestMethod.POST)
    public String changeSettings(@Valid @ModelAttribute SettingsDTO settings,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes) {
        logger.debug("start changing settings");

        redirectAttributes.addFlashAttribute("settings", settings);
        timeZoneValidator.validate(settings, result);
        if (result.hasErrors()) {
            settings.setError(true);
            logger.debug("Settings have errors {}", result);
            return "adminSettings";
        }
        logger.debug("Settings don't have errors {}", result);

        boolean needToRefreshSMTP = !settings.getSmtpParameters().equals(settingsService.getSmtpParameters());

        settingsService.saveAll(settings);

        if (needToRefreshSMTP) {
            mailService.applyNewParameters(settingsService.getSmtpParameters());
        }

        logger.info("settings are successfully changed");
        settings.setSuccess(true);
        redirectAttributes.addFlashAttribute("settings", settings);
        return "redirect:settings";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseBody
    @RequestMapping(value = "/checkParametersSMTP", method = RequestMethod.POST)
    public SmtpCheckResponse checkParametersSMTP(@ModelAttribute SettingsDTO settings,
                                      BindingResult result,
                                      Model model) {
        smtpValidator.validate(settings, result);
        if (result.hasErrors()) {
            return new SmtpCheckResponse(result);
        }
        Status status = Status.OK;
        if (!mailService.testConnection(settings.getSmtpParameters())) {
            status = Status.ERROR;
        }
        return new SmtpCheckResponse(status);
    }

    private enum Status {
        OK,
        ERROR
    }

    private static class SmtpCheckResponse {


        private Status status;
        Map<String, String> fieldErrors = new HashMap<>();

        public SmtpCheckResponse(Errors errors) {
            this.status = Status.ERROR;
            for (FieldError error : errors.getFieldErrors()) {
                fieldErrors.put(error.getField(), error.getCode());
            }

        }

        public SmtpCheckResponse(Status status) {
            this.status = status;
        }

        public Status getStatus() {
            return status;
        }

        public Map<String, String> getFieldErrors() {
            return fieldErrors;
        }

    }

    private static class SettingsValidator implements Validator {
        @Override
        public boolean supports(Class<?> clazz) {
            return clazz.equals(SettingsDTO.class);
        }

        @Override
        public void validate(Object target, Errors errors) {
            SettingsDTO settingsDTO = (SettingsDTO) target;
            String[] timeZones = TimeZone.getAvailableIDs();
            for (String timeZone : timeZones) {
                if (timeZone.equals(settingsDTO.getTimeZone())) {
                    return;
                }
            }
            errors.rejectValue("timeZone", "msg.settings.timeZone.error", "Wrong time zone");
        }
    }

    private static class SmtpParametersValidator implements Validator {
        @Override
        public boolean supports(Class<?> clazz) {
            return clazz.equals(SettingsDTO.class);
        }

        @Override
        public void validate(Object target, Errors errors) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "smtpParameters.host", "msg.settings.mustBeNotEmpty");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "smtpParameters.username", "msg.settings.mustBeNotEmpty");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "smtpParameters.password", "msg.settings.mustBeNotEmpty");
        }
    }
}