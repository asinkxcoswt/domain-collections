package com.asinkxcoswt.domain.behavior.entity_manager_support;

import com.asinkxcoswt.domain.behavior.DomainBehaviorManager;

import javax.persistence.EntityManager;

public class EntityManagerBehavior implements EntityManagerSupport {

    private EntityManager entityManager;

    public EntityManagerBehavior(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void detach() {
        entityManager.detach(DomainBehaviorManager.getTargetObject());
    }
}
