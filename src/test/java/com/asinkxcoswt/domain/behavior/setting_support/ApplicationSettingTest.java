package com.asinkxcoswt.domain.behavior.setting_support;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ApplicationSettingTestConfiguration.class})
public class ApplicationSettingTest {
    @Autowired
    private FooRepository fooRepository;

    @Autowired
    private ApplicationSettingRepository applicationSettingRepository;

    @Test
    public void canGetPriceIncludeVat_usingVatRateFromSetting() {
        // prepare setting vatRate = 7%
        applicationSettingRepository.save(new ApplicationSetting(ApplicationSettingKeys.VAT_RATE, "7"));

        // prepare foo entity with price = 10
        FooEntity fooEntity = fooRepository.create();
        fooEntity.setPrice(BigDecimal.valueOf(10));

        // check that the domain behavior method 'getSettingValue(...) works
        Setting vatRateSetting = fooEntity.getSettingValue(ApplicationSettingKeys.VAT_RATE);
        Assertions.assertEquals("7", vatRateSetting.getRawValue());

        // price include vat should be 10*7/100 = 0.70
        Assertions.assertEquals(new BigDecimal("0.70"), fooEntity.getPriceIncludeVat());
    }
}
