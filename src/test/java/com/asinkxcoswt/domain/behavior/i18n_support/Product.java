package com.asinkxcoswt.domain.behavior.i18n_support;

import com.asinkxcoswt.domain.behavior.DomainBehaviorProxy;
import com.asinkxcoswt.domain.behavior.DomainBehaviorSupport;
import com.asinkxcoswt.domain.behavior.setting_support.ApplicationSettingKeys;
import com.asinkxcoswt.domain.behavior.setting_support.Setting;
import com.asinkxcoswt.domain.behavior.setting_support.SettingSupport;
import org.springframework.context.i18n.LocaleContextHolder;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

@Entity
public class Product extends ProductDetail implements DomainBehaviorSupport, I18NSupport<ProductDetail>, SettingSupport {

    @Transient
    @DomainBehaviorProxy
    private Product self;

    @Column
    private BigDecimal price;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @MapKey(name = "locale")
    private Map<Locale, ProductDetail> detailMap = new HashMap<>();

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }


    @Override
    public Optional<ProductDetail> getI18NDetailForLocale(Locale locale) {
        return Optional.ofNullable(detailMap.get(locale));
    }

    @Override
    public ProductDetail getDefaultI18NDetail() {
        return this;
    }

    @Override
    public Class<ProductDetail> getI18NDetailClass() {
        return ProductDetail.class;
    }

    @Override
    public Locale getCurrentLocale() {
        return LocaleContextHolder.getLocale();
    }

    @Override
    public ProductDetail initializeI18NDetailForLocale(Locale locale) {
        ProductDetail detail = new ProductDetail();
        detail.setLocale(locale);
        this.detailMap.put(locale, detail);
        return detail;
    }

    @Override
    public Locale getLocale() {
        Setting setting = self.getSettingValue(ApplicationSettingKeys.DEFAULT_LOCALE);
        return Locale.forLanguageTag(setting.getValue(String.class));
    }

    @Override
    public void setLocale(Locale locale) {
        throw new UnsupportedOperationException();
    }
}
