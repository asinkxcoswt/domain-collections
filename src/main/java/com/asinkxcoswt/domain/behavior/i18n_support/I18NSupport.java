package com.asinkxcoswt.domain.behavior.i18n_support;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Locale;
import java.util.Optional;

public interface I18NSupport<D extends I18NDetail> {

    @JsonIgnore
    Optional<D> getI18NDetailForLocale(Locale locale);

    @JsonIgnore
    D getDefaultI18NDetail();

    @JsonIgnore
    Class<D> getI18NDetailClass();

    @JsonIgnore
    Locale getCurrentLocale();

    @JsonIgnore
    D initializeI18NDetailForLocale(Locale locale);
}
