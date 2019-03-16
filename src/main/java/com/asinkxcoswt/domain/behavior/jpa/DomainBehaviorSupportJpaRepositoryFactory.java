package com.asinkxcoswt.domain.behavior.jpa;

import com.asinkxcoswt.domain.behavior.DomainBehaviorManager;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.repository.core.support.RepositoryComposition;

import javax.persistence.EntityManager;

public class DomainBehaviorSupportJpaRepositoryFactory extends JpaRepositoryFactory {

    private DomainBehaviorManager domainBehaviorManager;
    public DomainBehaviorSupportJpaRepositoryFactory(EntityManager entityManager) {
        super(entityManager);
    }

    public void setDomainBehaviorManager(DomainBehaviorManager domainBehaviorManager) {
        this.domainBehaviorManager = domainBehaviorManager;
    }

    private <T> T wrapRepository(T repository) {
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setTarget(repository);
        proxyFactory.addAdvice(new DomainBehaviorSupportJpaRepositoryAdvice(this.domainBehaviorManager));
        return (T) proxyFactory.getProxy();
    }

    @Override
    public <T> T getRepository(Class<T> repositoryInterface) {
        T repository = super.getRepository(repositoryInterface);
        return wrapRepository(repository);
    }

    @Override
    public <T> T getRepository(Class<T> repositoryInterface, RepositoryComposition.RepositoryFragments fragments) {
        T repository = super.getRepository(repositoryInterface, fragments);
        return wrapRepository(repository);
    }

    @Override
    public <T> T getRepository(Class<T> repositoryInterface, Object customImplementation) {
        T repository = super.getRepository(repositoryInterface, customImplementation);
        return wrapRepository(repository);
    }

    //    @Override
//    protected JpaRepositoryImplementation<?, ?> getTargetRepository(RepositoryInformation information, EntityManager entityManager) {
//        JpaRepositoryImplementation repository = super.getTargetRepository(information, entityManager);
//        ProxyFactory proxyFactory = new ProxyFactory();
//        proxyFactory.setTarget(repository);
//        proxyFactory.addAdvice(new DomainBehaviorSupportJpaRepositoryAdvice(this.domainBehaviorManager));
//        return (JpaRepositoryImplementation) proxyFactory.getProxy();
//    }
}
