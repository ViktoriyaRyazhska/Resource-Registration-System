package org.registrator.community.components;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.stereotype.Component;

/** Holds Spring's ApplicationContext to get Bean by type
 *
 */

@Component
public class SpringApplicationContext implements ApplicationContextAware {
    private static ApplicationContext CONTEXT;

    @Override
    public void setApplicationContext(final ApplicationContext context)
            throws BeansException {
        CONTEXT = context;
    }

    public static <T> T getBean(Class<T> clazz) {
        if (CONTEXT == null) {
            return null;
        }
        return CONTEXT.getBean(clazz);
    }
}