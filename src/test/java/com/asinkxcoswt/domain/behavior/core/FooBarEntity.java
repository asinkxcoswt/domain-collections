package com.asinkxcoswt.domain.behavior.core;

import com.asinkxcoswt.domain.behavior.DomainBehaviorProxy;
import com.asinkxcoswt.domain.behavior.DomainBehaviorSupport;
import com.asinkxcoswt.domain.behavior.common.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
public class FooBarEntity extends BaseEntity implements DomainBehaviorSupport, FooSupport, BarSupport {

    @DomainBehaviorProxy
    @Transient
    private FooBarEntity self;

    @Override
    public String bar() {
        return "FooBarEntity";
    }
}
