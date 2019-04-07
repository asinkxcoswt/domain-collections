package com.asinkxcoswt.common.bean;

import java.lang.reflect.Method;
import java.util.Optional;

public class BeanUtils {
    public static boolean isGetter(Method method){
        if(!method.getName().startsWith("get")) {
            return false;
        }
        if(method.getParameterTypes().length != 0) {
            return false;
        }
        if(void.class.equals(method.getReturnType())) {
            return false;
        }

        return true;
    }

    public static boolean isSetter(Method method){
        if(!method.getName().startsWith("set")) {
            return false;
        }
        if(method.getParameterTypes().length != 1) {
            return false;
        }

        return true;
    }

    public static boolean isAccessor(Method method) {
        return isGetter(method);
    }

    public static boolean isMutator(Method method) {
        return isSetter(method);
    }

    public static Optional<String> getFieldNameForGetter(Method method) {
        if (!isGetter(method)) {
            return Optional.empty();
        }

        String fieldPart = method.getName().replaceFirst("get", "");
        return Optional.of(fieldPart.substring(0, 1).toLowerCase() + fieldPart.substring(1));
    }

    public static Optional<String> getFieldNameForSetter(Method method) {
        if (!isSetter(method)) {
            return Optional.empty();
        }

        String fieldPart = method.getName().replaceFirst("set", "");
        return Optional.of(fieldPart.substring(0, 1).toLowerCase() + fieldPart.substring(1));
    }

    public static boolean isDeclaredInType(Method method, Class<?> cls) {
        try {
            return cls.getDeclaredMethod(method.getName(), method.getParameterTypes())
                    .getReturnType()
                    .equals(method.getReturnType());
        } catch (NoSuchMethodException e) {
            return false;
        }
    }
}
