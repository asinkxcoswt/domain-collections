package com.asinkxcoswt.domain.behavior.entity_manager_support;

import com.asinkxcoswt.common.bean.SpringApplicationContextHolder;
import com.asinkxcoswt.domain.behavior.DomainBehaviorManager;
import com.asinkxcoswt.domain.behavior.jpa.DomainBehaviorSupportJpaRepositoryFactoryBean;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.persistence.EntityManager;

@SpringBootApplication
@EnableJpaRepositories(repositoryFactoryBeanClass = DomainBehaviorSupportJpaRepositoryFactoryBean.class)
@PropertySource(value="classpath:/test.properties")
public class EntityManagerSupportTestConfiguration {

    @Bean
    public SpringApplicationContextHolder springApplicationContextHolder() {
        return new SpringApplicationContextHolder();
    }

    @Bean
    public DomainBehaviorManager domainBehaviorManager(EntityManager entityManager) {
        DomainBehaviorManager manager = new DomainBehaviorManager();
        manager.registerBehavior(EntityManagerSupport.class, new EntityManagerBehavior(entityManager));
        manager.afterPropertiesSet();
        return manager;
    }
}
