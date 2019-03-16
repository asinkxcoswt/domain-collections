package com.asinkxcoswt.domain.behavior.core;

import com.asinkxcoswt.domain.behavior.DomainBehaviorNotImplementedException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DomainBehaviorCoreTestConfiguration.class})
public class DomainBehaviorCoreTest {

    @Autowired
    private FooRepository fooRepository;

    @Autowired
    private FooBarRepository fooBarRepository;

    @Test
    public void callJpaRepositoryCustomMethod_givenThatTheReturnTypeImplementsDomainBehaviorSupport_shouldReturnTheWrappedObject() {
        FooEntity fooEntityNew = fooRepository.create();
        String result = fooEntityNew.foo();
        Assertions.assertEquals("foo", result);
    }

    @Test
    public void callJpaRepositoryNormalMethod_givenThatTheReturnTypeImplementsDomainBehaviorSupport_shouldReturnTheWrappedObject() {
        FooEntity fooEntityNew = fooRepository.create();
        FooEntity savedFooEntity = fooRepository.save(fooEntityNew);

        FooEntity fooEntity = fooRepository.getOne(savedFooEntity.getUuid());
        Assertions.assertEquals("foo", fooEntity.foo());

        Optional<FooEntity> fooEntityOptional = fooRepository.findById(savedFooEntity.getUuid());
        Assertions.assertEquals("foo", fooEntityOptional.get().foo());

        List<FooEntity> fooEntityList = fooRepository.findAll();
        Assertions.assertIterableEquals(fooEntityList.stream().map(FooSupport::foo).collect(Collectors.toList()), Arrays.asList("foo"));
        Page<FooEntity> fooEntityPage = fooRepository.findAll(PageRequest.of(0,1));
        Assertions.assertEquals("foo", fooEntityPage.get().findFirst().get().foo());

    }

    @Test
    public void callJpaRepositoryNormalMethod_givenThatTheReturnTypeDoesNotImplementsDomainBehaviorSupport_shouldNotReturnTheWrappedObject() {
        long count = fooRepository.count();
        Assertions.assertEquals(0, count);
    }

    @Test
    public void callFooInFooSupport_shouldCallFooInFooBehavior() {
        FooEntity fooEntity = fooRepository.create();
        String fooResult = fooEntity.foo();
        Assertions.assertEquals("foo", fooResult);
    }

    @Test
    public void callFooInFooSupport_givenTheEntityHasOtherMixins_shouldCallFooInFooBehavior() {
        FooBarEntity fooBarEntity = fooBarRepository.create();
        String fooBarResult1 = fooBarEntity.foo();
        Assertions.assertEquals("foo", fooBarResult1);
    }

    @Test
    public void callBarInBarSupport_givenTheBarIsOverriddenByTheEntity_shouldCallBarInTheEntity() {
        FooBarEntity fooBarEntity = fooBarRepository.create();
        String fooBarResult2 = fooBarEntity.bar();
        Assertions.assertEquals("FooBarEntity", fooBarResult2);
    }

    @Test
    public void callFooInFooSupport_giveThatTheEntityForgotToImplementDomainBehaviorSupport_shouldThrowDomainBehaviorNotImplementedException() {
        EntityForgotToImplementDomainBehaviorSupport entity = new EntityForgotToImplementDomainBehaviorSupport();
        Assertions.assertThrows(DomainBehaviorNotImplementedException.class, entity::foo);
    }

    @Test
    public void callFooInFooSupport_viaSelfBoundedDomainBehaviorProxy_shouldHaveTheWrappedBehavior() {
        FooEntity fooEntity = fooRepository.create();
        Assertions.assertEquals("foo", fooEntity.subFoo());
    }
}
