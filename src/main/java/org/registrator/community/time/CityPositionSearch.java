package org.registrator.community.time;

import org.registrator.community.exceptions.ExternalApiCallException;

import java.util.List;

/**
 *
 */
public interface CityPositionSearch {

    List<CityPosition> findCities() throws ExternalApiCallException;

}
