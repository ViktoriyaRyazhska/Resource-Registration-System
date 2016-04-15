package org.registrator.community.time;

import java.util.HashMap;
import java.util.Map;

/**
 * Immutable class to represent city, holds position and locale based names
 */

public class CityPosition {

	private String id;
	
	private Double lat;
	
	private Double lon;
	
	private Map<String, String> localeNames;

    public CityPosition(String id, String lat, String lon) {
        this.id = id;
        this.lat = Double.valueOf(lat);
        this.lon = Double.valueOf(lon);
        this.localeNames = new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLon() {
        return lon;
    }

    public void addName(String localeCode, String name) {
        localeNames.put(localeCode, name);
    }

    public String getName(String localeCode) {
        return localeNames.get(localeCode);
    }

}