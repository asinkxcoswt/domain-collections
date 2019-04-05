package com.asinkxcoswt.domain.behavior.i18n_support;

import com.asinkxcoswt.domain.behavior.common.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Locale;

@Entity
public class I18NFooDetail extends BaseEntity implements I18NDetail {

    public I18NFooDetail() {
        this(null, null, null);
    }

    public I18NFooDetail(Locale locale, String firstName, String lastName) {
        this.locale = locale;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Column(name = "locale")
    private Locale locale;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Override
    public Locale getLocale() {
        return this.locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
