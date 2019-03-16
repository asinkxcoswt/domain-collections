package com.asinkxcoswt.domain.behavior.core;

import com.asinkxcoswt.common.bean.SpringApplicationContextHolder;
import com.asinkxcoswt.domain.behavior.DomainBehaviorManager;
import com.asinkxcoswt.domain.behavior.jpa.DomainBehaviorSupportJpaRepositoryFactoryBean;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(repositoryFactoryBeanClass = DomainBehaviorSupportJpaRepositoryFactoryBean.class)
@PropertySource(value="classpath:/test.properties")
public class DomainBehaviorCoreTestConfiguration {

    @Bean
    public SpringApplicationContextHolder springApplicationContextHolder() {
        return new SpringApplicationContextHolder();
    }

    @Bean
    public DomainBehaviorManager domainBehaviorManager() {
        DomainBehaviorManager manager = new DomainBehaviorManager();
        manager.registerBehavior(FooSupport.class, new FooBehavior());
        manager.registerBehavior(BarSupport.class, new BarBehavior());
        manager.afterPropertiesSet();
        return manager;
    }
}
