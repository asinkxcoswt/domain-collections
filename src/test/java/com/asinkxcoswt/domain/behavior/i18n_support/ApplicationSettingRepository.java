package com.asinkxcoswt.domain.behavior.i18n_support;

import com.asinkxcoswt.domain.behavior.setting_support.SettingQueryService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ApplicationSettingRepository extends JpaRepository<ApplicationSetting, UUID>, SettingQueryService {
}
