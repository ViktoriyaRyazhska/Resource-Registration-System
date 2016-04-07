package org.registrator.community.time;

import org.registrator.community.exceptions.ExternalApiCallException;

import java.util.TimeZone;

/**
 * Interface to provide search of time zone by city name
 */
@FunctionalInterface
public interface TimeZoneSearch {

    TimeZone findTimeZoneByPosition() throws ExternalApiCallException;

}
