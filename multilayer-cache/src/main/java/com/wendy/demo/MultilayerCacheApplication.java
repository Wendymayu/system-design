package com.wendy.demo;

import com.wendy.demo.utils.BeanUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class MultilayerCacheApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(MultilayerCacheApplication.class, args);
        BeanUtils.applicationContext = applicationContext;
    }
}
