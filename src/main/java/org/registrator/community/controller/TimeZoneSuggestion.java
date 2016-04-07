package org.registrator.community.controller;

import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.registrator.community.dto.TimeZoneDTO;
import org.registrator.community.exceptions.ExternalApiCallException;
import org.registrator.community.service.TimeZoneService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.List;

/**
 * Controller for providing REST service for suggesting time zone by name or city
 */
@Controller
public class TimeZoneSuggestion {
    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(TimeZoneSuggestion.class);
    @Autowired
    private TimeZoneService timeZoneService;
    /**
     * Find the list of suitable time zones by name or by city
     *
     * @param searchValue either name of the time zone or city name to look up for time zone
     *                    name of the city must be in current locale of the user
     * @return list of suitable time zones
     */

    @RequestMapping(value = "/timeZones", method = RequestMethod.GET)
    public ResponseEntity<List<TimeZoneDTO>> getTimeZonesSuggestions(@RequestParam("value") String searchValue) {
        List<TimeZoneDTO> timeZones = Collections.emptyList();
        HttpStatus status = HttpStatus.OK;
        try {
            timeZones = timeZoneService.findByNameOrCity(searchValue, LocaleContextHolder.getLocale());
        } catch (ExternalApiCallException e) {
            LOGGER.error("Couldn't execute external api call to find time zone", e);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(timeZones, status);
    }


}
