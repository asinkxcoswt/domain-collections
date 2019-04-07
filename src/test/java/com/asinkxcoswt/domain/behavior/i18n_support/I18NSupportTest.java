package com.asinkxcoswt.domain.behavior.i18n_support;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Locale;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {I18NSupportTestConfiguration.class})
public class I18NSupportTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ApplicationSettingRepository applicationSettingRepository;

    @Test
    public void test() throws NoSuchFieldException, IllegalAccessException {
        // prepare setting DEFAULT_LOCALE = en
        applicationSettingRepository.save(new ApplicationSetting(ApplicationSettingKeys.DEFAULT_LOCALE, "en"));

        // prepare a product for test
        Product product = productRepository.create("Samsung Galaxy", "A mobile phone", BigDecimal.valueOf(20000));
        productRepository.save(product);

        // check that the setting default locale works
        Assertions.assertEquals(Locale.forLanguageTag("en"), product.getDefaultI18NDetail().getLocale());

        // now let's change the language to th
        LocaleContextHolder.setLocale(Locale.forLanguageTag("th"));

        // check that the name is resolved to the default language because we have no name in th
        Assertions.assertEquals("Samsung Galaxy", product.getName());

        // let's set the name in language th
        product.setName("ซัมซุง กาแล็กซี");
        productRepository.flush();

        // now the name is resolved to Thai language
        Assertions.assertEquals("ซัมซุง กาแล็กซี", product.getName());

        // the ProductDetail where locale = th was created
        Assertions.assertTrue(product.getI18NDetailForLocale(Locale.forLanguageTag("th")).isPresent());

        // what about changing the language to something else
        LocaleContextHolder.setLocale(Locale.forLanguageTag("jp"));

        // the name is still resolved to the default language
        Assertions.assertEquals("Samsung Galaxy", product.getName());

        // let's set the name in language jp
        product.setName("サムスン ギャラクシー");
        productRepository.flush();

        // now the name is resolved to Japanese language
        Assertions.assertEquals("サムスン ギャラクシー", product.getName());

        // the ProductDetail where locale = jp was created
        Assertions.assertTrue(product.getI18NDetailForLocale(Locale.forLanguageTag("jp")).isPresent());

        // the data for Thai language is still there
        LocaleContextHolder.setLocale(Locale.forLanguageTag("th"));
        Assertions.assertEquals("ซัมซุง กาแล็กซี", product.getName());

        // what if the current language is equal to the default language
        LocaleContextHolder.setLocale(Locale.forLanguageTag("en"));

        // of course, the name should be in the default language
        Assertions.assertEquals("Samsung Galaxy", product.getName());

        // but if we set the name to something else,
        product.setName("Iphone X");

        // there should be no record in ProductDetail where locale = en, the value should be set directly to Product
        Assertions.assertFalse(product.getI18NDetailForLocale(Locale.forLanguageTag("en")).isPresent());

        // the default name should be changed to the new value
        LocaleContextHolder.setLocale(Locale.forLanguageTag("ch"));
        Assertions.assertEquals("Iphone X", product.getName());
    }
}
