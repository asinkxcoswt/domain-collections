package com.asinkxcoswt.domain.behavior.i18n_support;

import java.util.Locale;
import java.util.Optional;

public interface I18NSupport<D extends I18NDetail> extends I18NDetail {
    void setPrimaryI18NDetail(D detail);
    D getPrimaryI18NDetail();

    void addAlternativeI18NDetail(D detail);
    Optional<D> getAlternativeI18NDetailForLocale(Locale locale);


    default void setI18NDetail(D detail) {
        if (this.getLocale().equals(detail.getLocale())) {
            setPrimaryI18NDetail(detail);
        } else {
            addAlternativeI18NDetail(detail);
        }
    }

    default D getI18NDetailForLocale(Locale locale) {
        D primaryDetail = getPrimaryI18NDetail();
        if (primaryDetail.getLocale().equals(locale)) {
            return primaryDetail;
        } else {
            return getAlternativeI18NDetailForLocale(locale).orElse(primaryDetail);
        }
    }
}
