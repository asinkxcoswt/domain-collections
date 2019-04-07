package com.asinkxcoswt.domain.behavior;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.cglib.proxy.Mixin;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class DomainBehaviorMixinAdvice implements MethodInterceptor {

    private Mixin mixin;
    private Map<Class<?>, Object> behaviorMap = new HashMap<>();
    private static ThreadLocal<Object> targetObjectHolder = new ThreadLocal<>();

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
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

    public static Object getTargetObject() {
        return targetObjectHolder.get();
    }

    private boolean supports(Method method) {
        return this.behaviorMap.containsKey(method.getDeclaringClass()) &&
                method.isAnnotationPresent(DomainBehaviorTarget.class);
    }
}
