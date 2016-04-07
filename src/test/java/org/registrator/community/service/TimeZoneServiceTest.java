package org.registrator.community.service;

import org.registrator.community.dto.TimeZoneDTO;
import org.registrator.community.service.impl.TimeZoneServiceImpl;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.assertEquals;

/**
 *
 */
public class TimeZoneServiceTest {
    private TimeZoneService timeZoneService;

    @BeforeTest
    private void setUp() {
        timeZoneService = new TimeZoneServiceImpl();
    }

    @Test
    public void findByNameTimeZoneName() throws Exception {
        List<TimeZoneDTO> expected = Collections.singletonList(TimeZoneDTO.from("EET"));
        List<TimeZoneDTO> actual = timeZoneService.findByName("EET");
        assertEquals(actual, expected);
    }

    @Test
    public void findByNameEmptyString() throws Exception {
        List<TimeZoneDTO> actual = timeZoneService.findByName("");
        assertEquals(actual.size(), 0);
    }

    @Test
    public void findByNameNull() throws Exception {
        List<TimeZoneDTO> actual = timeZoneService.findByName(null);
        assertEquals(actual.size(), 0);
    }

    @Test
    public void findByNameCityName() throws Exception {

        List<TimeZoneDTO> actual = timeZoneService.findByName("Kie");
        assertThat(actual.size(), greaterThanOrEqualTo(1));
        TimeZoneDTO expectedValue = TimeZoneDTO.from("Europe/Kiev");
        assertThat(actual, containsInAnyOrder(expectedValue));
    }

    @Test
    public void findByCity() throws Exception {
        List<TimeZoneDTO> actual = timeZoneService.findByCity("Льв", Locale.forLanguageTag("uk"));
        assertThat(actual.size(), greaterThanOrEqualTo(1));
        TimeZoneDTO expectedValue = TimeZoneDTO.from("Europe/Kiev");
        assertThat(actual, containsInAnyOrder(expectedValue));

        String description = actual.get(actual.indexOf(expectedValue)).getDescription();
        assertThat(description, containsString("Львів"));
    }

    @Test
    public void findByNameOrCity() throws Exception {

    }
}