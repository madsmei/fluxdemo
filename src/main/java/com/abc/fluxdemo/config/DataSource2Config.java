package com.abc.fluxdemo.config;

import dev.miku.r2dbc.mysql.MySqlConnectionConfiguration;
import dev.miku.r2dbc.mysql.MySqlConnectionFactory;
import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

import java.time.Duration;

@Configuration
@EnableR2dbcRepositories(basePackages="com.abc.fluxdemo.repository2",databaseClientRef="scouce2DatabaseClient")
public class DataSource2Config extends AbstractR2dbcConfiguration {

    @Autowired
    private MysqlConfig mysqlConfig;

    @Override
    @Bean(name = "scouce2Connection")
    public ConnectionPool connectionFactory() {
        MySqlConnectionFactory connectionFactory = MySqlConnectionFactory.from(MySqlConnectionConfiguration.builder()
                .username(mysqlConfig.getScouce2UserName())
                .password(mysqlConfig.getScouce2Password())
                .host(mysqlConfig.getScouce2Url())
                .port(mysqlConfig.getScouce2Port())
                .database(mysqlConfig.getScouce2Database())
                .build());

        ConnectionPoolConfiguration.Builder builder = ConnectionPoolConfiguration.builder(connectionFactory).maxSize(mysqlConfig.getScouce2PoolMaxSize())
                .initialSize(mysqlConfig.getScouce2InitSize()).maxIdleTime(Duration.ofMinutes(mysqlConfig.getScouce2MaxIdleTime()));


        return new ConnectionPool(builder.build());
    }

    @Bean(name = "scouce2DatabaseClient")
    public DatabaseClient createDataBaseClient(@Qualifier("scouce2Connection") ConnectionFactory connectionFactory){

        return DatabaseClient.create(connectionFactory);
    }

}
