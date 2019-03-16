package com.asinkxcoswt.domain.behavior.entity_manager_support;

import com.asinkxcoswt.domain.behavior.DomainBehaviorNotImplementedException;
import com.asinkxcoswt.domain.behavior.DomainBehaviorTarget;

public interface EntityManagerSupport {
    @DomainBehaviorTarget
    default void detach() { throw new DomainBehaviorNotImplementedException(); }
}
