package org.registrator.community.components;

import org.registrator.community.entity.Resource;
import org.registrator.community.service.SettingsService;

import java.util.Calendar;
import java.util.TimeZone;

import javax.persistence.PrePersist;

/**
 * Persistent listener for Resource entity. Used to set 'created at' property on persist.
 * This class created to eliminate dependency from Resource to ResourceService
 */

public class ResourceListener{

    @PrePersist
    public void prePersist(Resource resource) {
        SettingsService settingsService = SpringApplicationContext.getBean(SettingsService.class);
        if (resource.getResourcesId() == null){
            TimeZone timeZone = settingsService.getTimeZone();
            resource.setCreatedAt(Calendar.getInstance(timeZone));
        }
    }
}
