package org.registrator.community.service.impl;

import org.registrator.community.dto.TimeZoneDTO;
import org.registrator.community.exceptions.ExternalApiCallException;
import org.registrator.community.service.TimeZoneService;
import org.registrator.community.time.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TimeZoneServiceImpl implements TimeZoneService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TimeZoneServiceImpl.class);

    @Override
    public List<TimeZoneDTO> findByName(String searchValue) {

        List<TimeZoneDTO> result = Collections.emptyList();
        if ((searchValue != null) && (!searchValue.isEmpty())) {
            result = Arrays.stream(TimeZone.getAvailableIDs())
                    .filter(id -> id.contains(searchValue))
                    .map(TimeZoneDTO::from)
                    .collect(Collectors.toList());
        }
        return result;
    }

    @Override
    public List<TimeZoneDTO> findByCity(String searchValue, String language) throws ExternalApiCallException {
        CityPositionSearch cityPositionSearch = new CityPositionSearchOSM(searchValue, language);
        List<CityPosition> points = cityPositionSearch.findCities();
        List<TimeZoneDTO> result = new ArrayList<>();

        for (CityPosition cityPosition: points ) {
            TimeZoneSearch timeZoneSearch = new TimeZoneSearchGeoNames(cityPosition);
            TimeZone timeZone = timeZoneSearch.findTimeZoneByPosition();
            String desctiption = cityPosition.getName(language) + "(" + timeZone.getID() + ")";
            result.add(new TimeZoneDTO(timeZone.getID(), timeZone.getID(), desctiption));
        }
        return result;
    }

    @Override
    public List<TimeZoneDTO> findByNameOrCity(String searchValue, String language) throws ExternalApiCallException {

        LOGGER.info(String.format("Time zone search by value '%s'", searchValue));

        List<TimeZoneDTO> result = findByName(searchValue);
        if (result.size() > 0) {
            return result;
        }

        return findByCity(searchValue, language);
    }
}
