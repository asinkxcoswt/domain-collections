package com.asinkxcoswt.domain.behavior.core;

import com.asinkxcoswt.domain.behavior.DomainBehaviorProxy;
import com.asinkxcoswt.domain.behavior.DomainBehaviorSupport;
import com.asinkxcoswt.domain.behavior.common.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.UUID;

@Entity
public class FooEntity extends BaseEntity implements DomainBehaviorSupport, FooSupport {

    @DomainBehaviorProxy
    @Transient
    private FooEntity self;

    public String subFoo() {
        return self.foo();
    }

}
