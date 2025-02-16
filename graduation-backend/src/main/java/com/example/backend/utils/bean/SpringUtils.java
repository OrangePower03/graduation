package com.example.backend.utils.bean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

@Component
@SuppressWarnings("unchecked")
public class SpringUtils implements BeanFactoryPostProcessor {
    private static BeanFactory beanFactory;

    public static <T> T getBean(String name) {
        return (T) beanFactory.getBean(name);
    }

    public static <T> T getBean(Class<T> clz) {
        return beanFactory.getBean(clz);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        SpringUtils.beanFactory = beanFactory;
    }
}
