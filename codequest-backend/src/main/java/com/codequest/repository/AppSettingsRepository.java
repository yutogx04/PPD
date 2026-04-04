package com.codequest.repository;

import com.codequest.entity.AppSettings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppSettingsRepository extends JpaRepository<AppSettings, Long> {
}
