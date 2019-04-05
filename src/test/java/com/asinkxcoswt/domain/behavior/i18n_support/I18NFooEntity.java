package com.asinkxcoswt.domain.behavior.i18n_support;

import com.asinkxcoswt.domain.behavior.DomainBehaviorProxy;
import com.asinkxcoswt.domain.behavior.DomainBehaviorSupport;
import com.asinkxcoswt.domain.behavior.setting_support.ApplicationSettingKeys;
import com.asinkxcoswt.domain.behavior.setting_support.Setting;
import com.asinkxcoswt.domain.behavior.setting_support.SettingSupport;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

@Entity
public class I18NFooEntity extends I18NFooDetail implements DomainBehaviorSupport, I18NSupport<I18NFooDetail>, SettingSupport {

    @Transient
    @DomainBehaviorProxy
    private I18NFooEntity self;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @MapKey(name = "locale")
    private Map<Locale, I18NFooDetail> fooDetailMap = new HashMap<>();

    public I18NFooEntity() {
        this(null, null);
    }

    public I18NFooEntity(String firstName, String lastName) {
        super(null, firstName, lastName);
    }

    @Override
    public void setPrimaryI18NDetail(I18NFooDetail detail) {
        super.setFirstName(detail.getFirstName());
        super.setLastName(detail.getLastName());
        super.setLocale(detail.getLocale());
    }

    @Override
    public I18NFooDetail getPrimaryI18NDetail() {
        return this;
    }

    @Override
    public void addAlternativeI18NDetail(I18NFooDetail detail) {
        fooDetailMap.put(detail.getLocale(), detail);
    }

    @Override
    public Optional<I18NFooDetail> getAlternativeI18NDetailForLocale(Locale locale) {
        return Optional.ofNullable(fooDetailMap.get(locale));
    }

    @Override
    public void setLocale(Locale locale) {
        throw new UnsupportedOperationException("Main entity is enforced to have default language from setting.");
    }

    @Override
    public Locale getLocale() {
        Setting setting = self.getSettingValue(ApplicationSettingKeys.DEFAULT_LOCALE);
        return Locale.forLanguageTag(setting.getValue(String.class));
    }
}
