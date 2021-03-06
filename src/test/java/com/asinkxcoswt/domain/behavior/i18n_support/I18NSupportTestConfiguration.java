package com.asinkxcoswt.domain.behavior.i18n_support;


import com.asinkxcoswt.common.bean.SpringApplicationContextHolder;
import com.asinkxcoswt.domain.behavior.DomainBehaviorManager;
import com.asinkxcoswt.domain.behavior.jpa.DomainBehaviorSupportJpaRepositoryFactoryBean;
import com.asinkxcoswt.domain.behavior.setting_support.SettingBehavior;
import com.asinkxcoswt.domain.behavior.setting_support.SettingSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(repositoryFactoryBeanClass = DomainBehaviorSupportJpaRepositoryFactoryBean.class)
@SpringBootApplication
public class I18NSupportTestConfiguration {
    @Bean
    public SpringApplicationContextHolder springApplicationContextHolder() {
        return new SpringApplicationContextHolder();
    }

    @Bean
    public SettingBehavior settingBehavior(ApplicationSettingRepository applicationSettingRepository) {
        return new SettingBehavior(applicationSettingRepository);
    }

    @Bean I18NAdvice i18NAdvice() {
        return new I18NAdvice();
    }

    @Bean
    public DomainBehaviorManager domainBehaviorManager() {
        return new DomainBehaviorManager();
    }

    @Autowired
    public void configure(DomainBehaviorManager domainBehaviorManager,
                          SettingBehavior settingBehavior,
                          I18NAdvice i18NAdvice) {

        domainBehaviorManager.registerBehavior(I18NSupport.class, i18NAdvice);
        domainBehaviorManager.registerBehavior(SettingSupport.class, settingBehavior);
        domainBehaviorManager.afterPropertiesSet();
    }
}