package com.asinkxcoswt.domain.behavior.setting_support;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ApplicationSettingRepository extends JpaRepository<ApplicationSetting, UUID>, SettingQueryService {
}
