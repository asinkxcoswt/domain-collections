package com.asinkxcoswt.domain.behavior.i18n_support;

import com.asinkxcoswt.domain.behavior.common.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Locale;

@Entity
public class ProductDetail extends BaseEntity implements I18NDetail {

    @Column(name = "locale")
    private Locale locale;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Override
    public Locale getLocale() {
        return this.locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
