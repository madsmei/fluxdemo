package com.abc.fluxdemo.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration(proxyBeanMethods = false)
@ConfigurationProperties(prefix = "mysql")
@PropertySource(value = "classpath:mysql.properties", ignoreResourceNotFound = true)
@Getter
@Setter
public class MysqlConfig {

    @Value(value = "${mysql.r2dbc.scouce1.url}")
    public String scouce1Url;
    @Value(value = "${mysql.r2dbc.scouce1.port}")
    public Integer scouce1Port;
    @Value(value = "${mysql.r2dbc.scouce1.username}")
    public String scouce1UserName;
    @Value(value = "${mysql.r2dbc.scouce1.password}")
    public String scouce1Password;
    @Value(value = "${mysql.r2dbc.scouce1.database}")
    public String scouce1Database;

    @Value(value = "${mysql.r2dbc.scouce1.max-size}")
    public Integer scouce1PoolMaxSize;
    @Value(value = "${mysql.r2dbc.scouce1.init-size}")
    public Integer scouce1InitSize;
    @Value(value = "${mysql.r2dbc.scouce1.max-idle-time}")
    public Integer scouce1MaxIdleTime;


    @Value(value = "${mysql.r2dbc.scouce2.url}")
    public String scouce2Url;
    @Value(value = "${mysql.r2dbc.scouce2.port}")
    public Integer scouce2Port;
    @Value(value = "${mysql.r2dbc.scouce2.username}")
    public String scouce2UserName;
    @Value(value = "${mysql.r2dbc.scouce2.password}")
    public String scouce2Password;
    @Value(value = "${mysql.r2dbc.scouce2.database}")
    public String scouce2Database;
    @Value(value = "${mysql.r2dbc.scouce2.max-size}")
    public Integer scouce2PoolMaxSize;
    @Value(value = "${mysql.r2dbc.scouce2.init-size}")
    public Integer scouce2InitSize;
    @Value(value = "${mysql.r2dbc.scouce2.max-idle-time}")
    public Integer scouce2MaxIdleTime;

}
