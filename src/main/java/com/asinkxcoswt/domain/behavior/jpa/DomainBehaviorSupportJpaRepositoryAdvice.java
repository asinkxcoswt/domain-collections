package com.asinkxcoswt.domain.behavior.jpa;

import com.asinkxcoswt.domain.behavior.DomainBehaviorManager;
import com.asinkxcoswt.domain.behavior.DomainBehaviorSupport;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DomainBehaviorSupportJpaRepositoryAdvice implements MethodInterceptor {

    private DomainBehaviorManager domainBehaviorManager;

    public DomainBehaviorSupportJpaRepositoryAdvice(DomainBehaviorManager domainBehaviorManager) {
        this.domainBehaviorManager = domainBehaviorManager;
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        Object[] unwrappedArgs = Stream.of(methodInvocation.getArguments())
                .map(this::unwrap)
                .toArray();
        Object target = methodInvocation.getThis();
        Method method = methodInvocation.getMethod();
        Object result = method.invoke(target, unwrappedArgs);
        return wrap(result);
    }

    private Object wrap(Object o) {
        if (o instanceof List) {
            return wrapList((List) o);
        } else if (o instanceof Set) {
            return wrapSet((Set) o);
        } else if (o instanceof Example) {
            return wrapExample((Example) o);
        } else if (o instanceof Page) {
            return wrapPage((Page) o);
        } else if (o instanceof Optional) {
            return wrapOptional((Optional) o);
        } else {
            return wrapObject(o);
        }
    }

    private Object unwrap(Object o) {
        if (o instanceof List) {
            return unwrapList((List) o);
        } else if (o instanceof Set) {
            return unwrapSet((Set) o);
        } else if (o instanceof Example) {
            return unwrapExample((Example) o);
        } else if (o instanceof Page) {
            return unwrapPage((Page) o);
        } else if (o instanceof Optional) {
            return unwrapOptional((Optional) o);
        } else {
            return unwrapObject(o);
        }
    }

    private <T> T wrapObject(T o) {
        if (o instanceof DomainBehaviorSupport) {
            return (T) domainBehaviorManager.wrapBehaviors((DomainBehaviorSupport) o);
        } else {
            return o;
        }
    }

    private List<Object> wrapList(List<Object> list) {
        return list.stream()
                .map(this::wrapObject)
                .collect(Collectors.toList());
    }

    private Set<Object> wrapSet(Set<Object> list) {
        return list.stream()
                .map(this::wrapObject)
                .collect(Collectors.toSet());
    }

    private Example<Object> wrapExample(Example<Object> example) {
        return Example.of(this.wrapObject(example.getProbe()), example.getMatcher());
    }

    private Page<Object> wrapPage(Page<Object> page) {
        return page.map(this::wrapObject);
    }

    private Optional<Object> wrapOptional(Optional<Object> optional) {
        return optional.map(this::wrapObject);
    }

    private Object unwrapObject(Object o) {
        if (AopUtils.isAopProxy(o) && o instanceof DomainBehaviorSupport && o instanceof Advised) {
            try {
                return ((Advised) o).getTargetSource().getTarget();
            } catch (Exception e) {
                throw new IllegalArgumentException("Error while unwrapping domain behavior target class", e);
            }
        } else {
            return o;
        }
    }


    private List<Object> unwrapList(List<Object> list) {
        return list.stream()
                .map(this::unwrapObject)
                .collect(Collectors.toList());
    }

    private Set<Object> unwrapSet(Set<Object> list) {
        return list.stream()
                .map(this::unwrapObject)
                .collect(Collectors.toSet());
    }

    private Example<Object> unwrapExample(Example<Object> example) {
        return Example.of(this.unwrapObject(example.getProbe()), example.getMatcher());
    }

    private Page<Object> unwrapPage(Page<Object> page) {
        return page.map(this::unwrapObject);
    }

    private Optional<Object> unwrapOptional(Optional<Object> optional) {
        return optional.map(this::unwrapObject);
    }


}
