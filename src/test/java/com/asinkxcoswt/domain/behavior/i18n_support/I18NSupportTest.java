package com.asinkxcoswt.domain.behavior.i18n_support;

import com.asinkxcoswt.domain.behavior.setting_support.ApplicationSetting;
import com.asinkxcoswt.domain.behavior.setting_support.ApplicationSettingKeys;
import com.asinkxcoswt.domain.behavior.setting_support.ApplicationSettingRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Locale;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {I18NSupportTestConfiguration.class})
public class I18NSupportTest {

    @Autowired
    private I18NFooRepository fooRepository;

    @Autowired
    private ApplicationSettingRepository applicationSettingRepository;

    @Test
    public void test() {
        // prepare setting vatRate = 7%
        applicationSettingRepository.save(new ApplicationSetting(ApplicationSettingKeys.DEFAULT_LOCALE, "th"));

        I18NFooEntity fooEntity = fooRepository.create("John", "Smith");

        Assertions.assertEquals(Locale.forLanguageTag("th"), fooEntity.getLocale());
        Assertions.assertThrows(UnsupportedOperationException.class, () -> fooEntity.setLocale(Locale.ENGLISH));

        Assertions.assertEquals("John", fooEntity.getPrimaryI18NDetail().getFirstName());
        Assertions.assertFalse(fooEntity.getAlternativeI18NDetailForLocale(Locale.ENGLISH).isPresent());
        Assertions.assertEquals("John", fooEntity.getI18NDetailForLocale(Locale.ENGLISH).getFirstName());
        Assertions.assertEquals("John", fooEntity.getI18NDetailForLocale(Locale.JAPANESE).getFirstName());
        Assertions.assertEquals("John", fooEntity.getI18NDetailForLocale(Locale.forLanguageTag("th")).getFirstName());


        I18NFooDetail fooDetailTh = new I18NFooDetail(Locale.forLanguageTag("th"), "Sara", "Corner");
        fooEntity.setI18NDetail(fooDetailTh);

        Assertions.assertEquals("Sara", fooEntity.getPrimaryI18NDetail().getFirstName());
        Assertions.assertFalse(fooEntity.getAlternativeI18NDetailForLocale(Locale.ENGLISH).isPresent());
        Assertions.assertEquals("Sara", fooEntity.getI18NDetailForLocale(Locale.ENGLISH).getFirstName());
        Assertions.assertEquals("Sara", fooEntity.getI18NDetailForLocale(Locale.JAPANESE).getFirstName());
        Assertions.assertEquals("Sara", fooEntity.getI18NDetailForLocale(Locale.forLanguageTag("th")).getFirstName());

        I18NFooDetail fooDetailEn = new I18NFooDetail(Locale.ENGLISH, "Jack", "Giant Killer");
        fooEntity.setI18NDetail(fooDetailEn);

        Assertions.assertEquals("Sara", fooEntity.getPrimaryI18NDetail().getFirstName());
        Assertions.assertTrue(fooEntity.getAlternativeI18NDetailForLocale(Locale.ENGLISH).isPresent());
        Assertions.assertEquals("Jack", fooEntity.getI18NDetailForLocale(Locale.ENGLISH).getFirstName());
        Assertions.assertEquals("Sara", fooEntity.getI18NDetailForLocale(Locale.JAPANESE).getFirstName());
        Assertions.assertEquals("Sara", fooEntity.getI18NDetailForLocale(Locale.forLanguageTag("th")).getFirstName());

        fooRepository.save(fooEntity);
    }
}
