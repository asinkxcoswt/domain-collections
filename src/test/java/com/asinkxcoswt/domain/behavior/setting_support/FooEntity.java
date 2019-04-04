package com.asinkxcoswt.domain.behavior.setting_support;

import com.asinkxcoswt.domain.behavior.DomainBehaviorProxy;
import com.asinkxcoswt.domain.behavior.DomainBehaviorSupport;
import com.asinkxcoswt.domain.behavior.common.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
public class FooEntity extends BaseEntity implements DomainBehaviorSupport, SettingSupport {

    @Transient
    @DomainBehaviorProxy
    private FooEntity self;

    private BigDecimal price = BigDecimal.ZERO;

    public BigDecimal getPrice() {
        return this.price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPriceIncludeVat() {
        Setting vatRateSetting = self.getSettingValue(ApplicationSettingKeys.VAT_RATE);
        BigDecimal vatRate = new BigDecimal(vatRateSetting.getValue(Integer.class));
        return price.multiply(vatRate).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }
}
