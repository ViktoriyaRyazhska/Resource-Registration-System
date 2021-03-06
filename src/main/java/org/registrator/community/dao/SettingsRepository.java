package org.registrator.community.dao;

import org.registrator.community.entity.Settings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

/**
 * Interface for getting and saving application settings
 */

public interface SettingsRepository extends JpaRepository<Settings, Integer> {

    @Query("Select s From Settings s where not s.id is NULL")
    @Transactional
    Settings getAllSettings();

}
