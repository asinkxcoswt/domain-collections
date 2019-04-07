package com.asinkxcoswt.domain.behavior.i18n_support;

import com.asinkxcoswt.common.bean.BeanUtils;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

public class I18NAdvice implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        Object targetObject =  methodInvocation.getThis();
        if (targetObject instanceof I18NSupport) {
            Method method = methodInvocation.getMethod();
            Object[] args = methodInvocation.getArguments();
            Optional<?> alternativeObject = resolveAlternativeObject(method, (I18NSupport) targetObject);
            if (alternativeObject.isPresent()) {
                return invokeIdenticalMethodAlternativeObject(method, args, alternativeObject.get());
            }
        }

        return methodInvocation.proceed();
    }

    private Object invokeIdenticalMethodAlternativeObject(Method method, Object[] args, Object alternativeObject) {
        try {
            Method targetMethod = alternativeObject.getClass().getMethod(method.getName(), method.getParameterTypes());
            return targetMethod.invoke(alternativeObject, args);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    private Optional<?> resolveAlternativeObject(Method method, I18NSupport targetObject) {
        if (isQualifiedAsI18NAccessor(method, targetObject)) {
            Optional<?> mayBeDetail = targetObject.getI18NDetailForLocale(targetObject.getCurrentLocale());
            if (mayBeDetail.isPresent()) {
                return mayBeDetail;
            } else {
                return Optional.of(targetObject.getDefaultI18NDetail());
            }

        } else if (isQualifiedAsI18NMutator(method, targetObject)) {
            Optional<?> mayBeDetail = targetObject.getI18NDetailForLocale(targetObject.getCurrentLocale());
            if (mayBeDetail.isPresent()) {
                return mayBeDetail;
            } else if (targetObject.getDefaultI18NDetail().getLocale().equals(targetObject.getCurrentLocale())) {
                return Optional.of(targetObject.getDefaultI18NDetail());
            } else {
                return Optional.of(targetObject.initializeI18NDetailForLocale(targetObject.getCurrentLocale()));
            }
        }

        return Optional.empty();
    }

    private boolean isQualifiedAsI18NMethod(Method method, I18NSupport targetObject) {
        if (BeanUtils.isDeclaredInType(method, I18NDetail.class)) {
            return false;
        }
        if (BeanUtils.isDeclaredInType(method, I18NSupport.class)) {
            return false;
        }
        if (!BeanUtils.isDeclaredInType(method, targetObject.getI18NDetailClass())) {
            return false;
        }

        return true;
    }

    private boolean isQualifiedAsI18NMutator(Method method, I18NSupport targetObject) {
        return BeanUtils.isMutator(method) && isQualifiedAsI18NMethod(method, targetObject);
    }

    private boolean isQualifiedAsI18NAccessor(Method method, I18NSupport targetObject) {
        return BeanUtils.isAccessor(method) && isQualifiedAsI18NMethod(method, targetObject);
    }
}
