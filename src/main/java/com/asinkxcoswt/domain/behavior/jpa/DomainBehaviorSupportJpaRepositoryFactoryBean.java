package com.asinkxcoswt.domain.behavior.jpa;

import com.asinkxcoswt.domain.behavior.DomainBehaviorManager;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.querydsl.EntityPathResolver;
import org.springframework.data.querydsl.SimpleEntityPathResolver;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import javax.persistence.EntityManager;
import java.io.Serializable;

public class DomainBehaviorSupportJpaRepositoryFactoryBean<R extends JpaRepository<T, I>, T , I extends Serializable> extends JpaRepositoryFactoryBean<R, T, I> {

    private EntityPathResolver entityPathResolver;
    private DomainBehaviorManager domainBehaviorManager;

    public DomainBehaviorSupportJpaRepositoryFactoryBean(Class<? extends R> repositoryInterface) {
        super(repositoryInterface);
    }

    @Autowired
    @Override
    public void setEntityPathResolver(ObjectProvider<EntityPathResolver> resolver) {
        super.setEntityPathResolver(resolver);
        this.entityPathResolver = resolver.getIfAvailable(() -> SimpleEntityPathResolver.INSTANCE);
    }

    @Autowired
    public void setDomainBehaviorManager(DomainBehaviorManager domainBehaviorManager) {
        this.domainBehaviorManager = domainBehaviorManager;
    }

    @Override
    protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
        DomainBehaviorSupportJpaRepositoryFactory domainBehaviorSupportJpaRepositoryFactory = new DomainBehaviorSupportJpaRepositoryFactory(entityManager);
        domainBehaviorSupportJpaRepositoryFactory.setEntityPathResolver(this.entityPathResolver);
        domainBehaviorSupportJpaRepositoryFactory.setDomainBehaviorManager(this.domainBehaviorManager);
        return domainBehaviorSupportJpaRepositoryFactory;
    }
}
