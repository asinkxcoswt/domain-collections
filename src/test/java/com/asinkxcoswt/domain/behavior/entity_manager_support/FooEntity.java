package com.asinkxcoswt.domain.behavior.entity_manager_support;

import com.asinkxcoswt.domain.behavior.DomainBehaviorSupport;
import com.asinkxcoswt.domain.behavior.common.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class FooEntity extends BaseEntity implements DomainBehaviorSupport, EntityManagerSupport {

    @Column(name = "foo")
    private String foo;

    @Column(name = "bar")
    private String bar;

    public String getFoo() {
        return foo;
    }

    public void setFoo(String foo) {
        this.foo = foo;
    }

    public String getBar() {
        return bar;
    }

    public void setBar(String bar) {
        this.bar = bar;
    }
}
