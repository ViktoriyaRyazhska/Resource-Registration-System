package org.registrator.community.time;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.registrator.community.exceptions.ExternalApiCallException;

import java.io.IOException;
import java.net.URL;
import java.util.TimeZone;

/**
 * Implementation of time zone search by position based on web-service timezonedb.com
 */
public class TimeZoneSearchGeoNames implements TimeZoneSearch {
    private static final String TIME_ZONE_API =
            "http://api.geonames.org/timezoneJSON?lat=%s&lng=%s&username=koklobok";
    private final CityPosition cityPosition;

    public TimeZoneSearchGeoNames(CityPosition cityPosition) {
        this.cityPosition = cityPosition;
    }

    @Override
    public TimeZone findTimeZoneByPosition() throws ExternalApiCallException {
        String hostname =
                String.format(TIME_ZONE_API, cityPosition.getLat(), cityPosition.getLon());
        ObjectMapper mapper = new ObjectMapper();
        TimeDbResponse response;
        try {
            response = mapper.readValue(new URL(hostname), TimeDbResponse.class);
        } catch (IOException e) {
            throw new ExternalApiCallException("Error when calling geonames.org API", e);
        }
        if (response.getStatus() != null) {
            throw new ExternalApiCallException(
                    "Call to geonames.org failed, reason: " + response.getStatus());
        }

        return TimeZone.getTimeZone(response.getTimezoneId());
    }

    static class TimeDbResponse {
        private Status status;
        private String sunrise;
        private String sunset;
        private String countryCode;
        private String lng;
        private String lat;
        private String time;
        private String countryName;
        private String timezoneId;
        private String abbreviation;
        private String dstOffset;
        private String rawOffset;
        private String gmtOffset;

        public Status getStatus() {
            return status;
        }

        public void setStatus(Status status) {
            this.status = status;
        }

        public String getSunrise() {
            return sunrise;
        }

        public void setSunrise(String sunrise) {
            this.sunrise = sunrise;
        }

        public String getCountryCode() {
            return countryCode;
        }

        public void setCountryCode(String countryCode) {
            this.countryCode = countryCode;
        }

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getCountryName() {
            return countryName;
        }

        public void setCountryName(String countryName) {
            this.countryName = countryName;
        }

        public String getTimezoneId() {
            return timezoneId;
        }

        public void setTimezoneId(String timezoneId) {
            this.timezoneId = timezoneId;
        }

        public String getAbbreviation() {
            return abbreviation;
        }

        public void setAbbreviation(String abbreviation) {
            this.abbreviation = abbreviation;
        }

        public String getDstOffset() {
            return dstOffset;
        }

        public void setDstOffset(String dstOffset) {
            this.dstOffset = dstOffset;
        }

        public String getRawOffset() {
            return rawOffset;
        }

        public void setRawOffset(String rawOffset) {
            this.rawOffset = rawOffset;
        }

        public String getGmtOffset() {
            return gmtOffset;
        }

        public void setGmtOffset(String gmtOffset) {
            this.gmtOffset = gmtOffset;
        }

        public String getSunset() {
            return sunset;
        }

        public void setSunset(String sunset) {
            this.sunset = sunset;
        }

        static class Status {
            private String message;
            private int value;

            public String getMessage() {
                return message;
            }

            public void setMessage(String message) {
                this.message = message;
            }

            public int getValue() {
                return value;
            }

            public void setValue(int value) {
                this.value = value;
            }

            @Override
            public String toString() {
                return message;
            }
        }
    }



}


