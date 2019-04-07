package com.asinkxcoswt.domain.behavior.setting_support;


import com.asinkxcoswt.common.bean.SpringApplicationContextHolder;
import com.asinkxcoswt.domain.behavior.DomainBehaviorManager;
import com.asinkxcoswt.domain.behavior.jpa.DomainBehaviorSupportJpaRepositoryFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(repositoryFactoryBeanClass = DomainBehaviorSupportJpaRepositoryFactoryBean.class)
@SpringBootApplication(exclude = {JmxAutoConfiguration.class})
public class SettingSupportTestConfiguration {

    @Bean
    public SpringApplicationContextHolder springApplicationContextHolder() {
        return new SpringApplicationContextHolder();
    }

    @Bean
    public SettingBehavior settingBehavior(ApplicationSettingRepository applicationSettingRepository) {
        return new SettingBehavior(applicationSettingRepository);
    }

    @Bean
    public DomainBehaviorManager domainBehaviorManager() {
        return new DomainBehaviorManager();
    }

    @Autowired
    public void configure(DomainBehaviorManager domainBehaviorManager,
                          SettingBehavior settingBehavior) {
        domainBehaviorManager.registerBehavior(SettingSupport.class, settingBehavior);
        domainBehaviorManager.afterPropertiesSet();
    }
}