package com.asinkxcoswt.domain.behavior;

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

    private static ThreadLocal<Object> targetObjectHolder = new ThreadLocal<>();
    private Map<Class<?>, Object> behaviorMap = new HashMap<>();
    private Mixin mixin;

    public static Object getTargetObject() {
        return targetObjectHolder.get();
    }

    public <INTERFACE, IMPL extends INTERFACE> void registerBehavior(Class<INTERFACE> interfaceClass, IMPL impl) {
        this.behaviorMap.put(interfaceClass, impl);
    }

    public void afterPropertiesSet() {
        List<Class<?>> interfaceClassList = new ArrayList<>();
        List<Object> implList = new ArrayList<>();
        for (Map.Entry<Class<?>, Object> entry : this.behaviorMap.entrySet()) {
            interfaceClassList.add(entry.getKey());
            implList.add(entry.getValue());
        }
        this.mixin = Mixin.create(interfaceClassList.toArray(new Class[0]), implList.toArray());
    }

    public <T extends DomainBehaviorSupport> T wrapBehaviors(T domainObject) {
        if (this.mixin == null) {
            throw new IllegalStateException("Mixins are not finalized, forgot to call DomainBehaviorManager::afterPropertiesSet ?");
        }

        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setTarget(domainObject);
        proxyFactory.addAdvice((MethodInterceptor) methodInvocation -> {
            Method method = methodInvocation.getMethod();
            Object target = methodInvocation.getThis();
            if (supports(method)) {
                targetObjectHolder.set(target);
                Object result = method.invoke(mixin, methodInvocation.getArguments());
                targetObjectHolder.remove();
                return result;
            } else {
                return methodInvocation.proceed();
            }
        });


        T wrappedObject = (T) proxyFactory.getProxy();
        Optional<Field> proxyFieldOptional = Stream.of(domainObject.getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(DomainBehaviorProxy.class))
                .findFirst();

        if (proxyFieldOptional.isPresent()) {
            Field proxyField = proxyFieldOptional.get();
            try {
                proxyField.setAccessible(true);
                proxyField.set(domainObject, wrappedObject);
            } catch (IllegalAccessException e) {
                throw new IllegalArgumentException("Cannot bind domain behavior proxy to the field '" + proxyField.getName() + "'", e);
            }
        }

        return wrappedObject;
    }

    private boolean supports(Method method) {
        return this.behaviorMap.containsKey(method.getDeclaringClass()) &&
                method.isAnnotationPresent(DomainBehaviorTarget.class);
    }
}
