package com.asinkxcoswt.domain.behavior;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.cglib.proxy.Mixin;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class DomainBehaviorManager {

    private Map<Class<?>, Advice> adviceMap = new HashMap<>();
    private DomainBehaviorMixinAdvice defaultAdvice = new DomainBehaviorMixinAdvice();

    public <INTERFACE, IMPL extends INTERFACE> void registerBehavior(Class<INTERFACE> interfaceClass, IMPL impl) {
        this.defaultAdvice.registerBehavior(interfaceClass, impl);
    }

    public void registerBehavior(Class interfaceClass, Advice advice) {
        this.adviceMap.put(interfaceClass, advice);
    }

    public void afterPropertiesSet() {
        this.defaultAdvice.afterPropertiesSet();
    }

    public static Object getTargetObject() {
        return DomainBehaviorMixinAdvice.getTargetObject();
    }

    public <T extends DomainBehaviorSupport> T wrapBehaviors(T domainObject) {
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setTarget(domainObject);
        proxyFactory.addAdvice(this.defaultAdvice);
        adviceMap.forEach((interfaceClass, advice) -> {
            if (interfaceClass.isAssignableFrom(domainObject.getClass())) {
                proxyFactory.addAdvice(advice);
            }
        });

        T wrappedObject = (T) proxyFactory.getProxy();
        injectSelfProxy(domainObject, wrappedObject);

        return wrappedObject;
    }

    private <T extends DomainBehaviorSupport> void injectSelfProxy(T domainObject, T proxyObject) {
        Optional<Field> proxyFieldOptional = Stream.of(domainObject.getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(DomainBehaviorProxy.class))
                .findFirst();

        if (proxyFieldOptional.isPresent()) {
            Field proxyField = proxyFieldOptional.get();
            try {
                proxyField.setAccessible(true);
                proxyField.set(domainObject, proxyObject);
            } catch (IllegalAccessException e) {
                throw new IllegalArgumentException("Cannot bind domain behavior proxy to the field '" + proxyField.getName() + "'", e);
            }
        }
    }
}
