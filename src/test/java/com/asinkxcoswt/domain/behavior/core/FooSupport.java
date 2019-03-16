package com.asinkxcoswt.domain.behavior.core;

import com.asinkxcoswt.domain.behavior.DomainBehaviorNotImplementedException;
import com.asinkxcoswt.domain.behavior.DomainBehaviorTarget;

public interface FooSupport {
    @DomainBehaviorTarget
    default String foo() { throw new DomainBehaviorNotImplementedException(); }
}
