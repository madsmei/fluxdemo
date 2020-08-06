package com.abc.fluxdemo;

import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;

@SpringBootApplication(exclude = {RedisAutoConfiguration.class, RedissonAutoConfiguration.class}
        ,proxyBeanMethods = false)
public class FluxdemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(FluxdemoApplication.class, args);
    }

}
