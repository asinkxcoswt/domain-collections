package com.asinkxcoswt.common.bean;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringApplicationContextHolder implements ApplicationContextAware {
    private static ApplicationContext instance;

    public static ApplicationContext getInstance() {
        if (SpringApplicationContextHolder.instance != null) {
            return SpringApplicationContextHolder.instance;
        } else {
            throw new IllegalStateException("The application context instance was not set through ApplicationContextAware interface, perhaps you forgot to declare SpringApplicationContextHolder as a bean?");
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext ctx) {
        SpringApplicationContextHolder.instance = ctx;
    }
}
