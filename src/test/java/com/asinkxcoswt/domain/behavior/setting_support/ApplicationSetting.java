package com.asinkxcoswt.domain.behavior.setting_support;

import com.asinkxcoswt.domain.behavior.DomainBehaviorSupport;
import com.asinkxcoswt.domain.behavior.common.BaseEntity;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.io.IOException;
import java.io.UncheckedIOException;

@Entity
public class ApplicationSetting extends BaseEntity implements DomainBehaviorSupport, Setting {

    @Column(name = "key")
    private String key;

    @Column(name = "value")
    private String value;

    private ApplicationSetting() {}

    public ApplicationSetting(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String getKey() {
        return this.key;
    }

    @Override
    public String getRawValue() {
        return this.value;
    }

    @Override
    public <T> T getValue(Class<T> targetClass) {
        if (targetClass.equals(String.class)) {
            return (T) this.value;
        }

        try {
            return new ObjectMapper().readValue(this.value, targetClass);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
