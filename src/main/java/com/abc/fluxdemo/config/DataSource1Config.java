package com.abc.fluxdemo.config;

import dev.miku.r2dbc.mysql.MySqlConnectionConfiguration;
import dev.miku.r2dbc.mysql.MySqlConnectionFactory;
import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
import io.r2dbc.spi.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

import java.time.Duration;

/*****
 *  目前只发现 通过包扫描的方式实现多数据源的配置。是最简单的。。。
 * @Author Mads
 * @Date 2020/8/6
**/
@Configuration
@EnableR2dbcRepositories(basePackages="com.abc.fluxdemo.repository1",databaseClientRef="scouce1DatabaseClient")
@RequiredArgsConstructor
public class DataSource1Config extends AbstractR2dbcConfiguration {


    @Autowired
    private MysqlConfig mysqlConfig;

    @Override
    @Bean(name = "scouce1Connection")
    public ConnectionPool connectionFactory() {
        MySqlConnectionFactory connectionFactory = MySqlConnectionFactory.from(MySqlConnectionConfiguration.builder()
                .username(mysqlConfig.getScouce1UserName())
                .password(mysqlConfig.getScouce1Password())
                .host(mysqlConfig.getScouce1Url())
                .port(mysqlConfig.getScouce1Port())
                .database(mysqlConfig.getScouce1Database())
                .build());


        ConnectionPoolConfiguration.Builder builder = ConnectionPoolConfiguration.builder(connectionFactory).maxSize(mysqlConfig.getScouce1PoolMaxSize())
                .initialSize(mysqlConfig.getScouce1InitSize()).maxIdleTime(Duration.ofMinutes(mysqlConfig.getScouce1MaxIdleTime()));


        return new ConnectionPool(builder.build());
    }

    @Bean(name = "scouce1DatabaseClient")
    public DatabaseClient createDataBaseClient(@Qualifier("scouce1Connection")ConnectionFactory connectionFactory){

        return DatabaseClient.create(connectionFactory);
    }
}
