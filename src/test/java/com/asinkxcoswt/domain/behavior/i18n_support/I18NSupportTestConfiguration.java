package com.asinkxcoswt.domain.behavior.i18n_support;


import com.asinkxcoswt.domain.behavior.setting_support.SettingSupportTestConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackageClasses = {SettingSupportTestConfiguration.class, I18NSupportTestConfiguration.class})
public class I18NSupportTestConfiguration extends SettingSupportTestConfiguration {
}