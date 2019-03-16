package com.asinkxcoswt.domain.behavior.core;

import com.asinkxcoswt.domain.behavior.DomainBehaviorNotImplementedException;
import com.asinkxcoswt.domain.behavior.DomainBehaviorTarget;

public interface BarSupport {
    @DomainBehaviorTarget
    default String bar() {
        throw new DomainBehaviorNotImplementedException();
    }
}
