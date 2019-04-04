package com.asinkxcoswt.domain.behavior.setting_support;

public interface Setting {
    String getKey();
    String getRawValue();
    <T> T getValue(Class<T> targetClass);
}
