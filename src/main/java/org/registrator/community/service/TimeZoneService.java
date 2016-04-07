package org.registrator.community.service;

import org.registrator.community.dto.TimeZoneDTO;
import org.registrator.community.exceptions.ExternalApiCallException;

import java.util.List;
import java.util.Locale;

/**
 * Service to provide search of time zone
 */
public interface TimeZoneService {

    /**
     * Find the list of suitable time zones by name
     *
     * @param searchValue short name of the time zone, or city name in English
     * @return list of suitable time zones
     */
    List<TimeZoneDTO> findByName(String searchValue);


    /**
     * Find the list of suitable time zones by city name in specified locale
     *
     * @param searchValue name of the city
     * @return list of suitable time zones
     */
    List<TimeZoneDTO> findByCity(String searchValue, Locale locale) throws ExternalApiCallException;

    /**
     * Find the list of suitable time zones by name or by city
     *
     * @param searchValue either name of the time zone or city name to look up for time zone
     *                    name of the city must be in current locale of the user
     * @return list of suitable time zones
     */
    List<TimeZoneDTO> findByNameOrCity(String searchValue, Locale locale) throws ExternalApiCallException;
}
