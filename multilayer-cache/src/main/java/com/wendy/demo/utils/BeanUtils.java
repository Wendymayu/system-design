package com.wendy.demo.utils;

import org.springframework.context.ApplicationContext;

/**
 * @Description TODO
 * @Author wendyma
 * @Date 2024/1/31 22:14
 * @Version 1.0
 */
public class BeanUtils {
    public static ApplicationContext applicationContext;

    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }
}
