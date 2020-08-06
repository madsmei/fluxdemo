package com.abc.fluxdemo.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.Config;
import org.redisson.config.ReadMode;
import org.redisson.config.SubscriptionMode;
import org.redisson.config.TransportMode;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Configuration(proxyBeanMethods = false)
@ConfigurationProperties(prefix = "redis")
@PropertySource(value = "classpath:redis.properties", ignoreResourceNotFound = true)
@Getter
@Setter
public class RedisConfig {

    private MadsRedis madsRedis;

    @Bean(name = "madsRedisRedissonClient")
    @ConditionalOnProperty(name = "redis.mads-redis.endPoint")
    public RedissonClient madsRedisRedissonClient() {
        return redissonClient(madsRedis);
    }
    @Bean("madsRedisRedissonTemplate")
    public ReactiveRedisTemplate<String, String> madsRedisRedissonTemplate(@Qualifier("madsRedisRedissonClient")RedissonClient redissonClient) {
        return getRedisTemplate(redissonClient);
    }




    private ReactiveRedisTemplate<String, String> getRedisTemplate(RedissonClient redissonClient) {

        RedisSerializationContext.RedisSerializationContextBuilder<String, String> builder =
                RedisSerializationContext.newSerializationContext(new StringRedisSerializer());

        RedisSerializationContext<String, String> context = builder.key(new StringRedisSerializer()).value(new StringRedisSerializer()).build();

        return new ReactiveRedisTemplate<>(new RedissonConnectionFactory(redissonClient),context);
    }

    private RedissonClient redissonClient(Redis redis) {
        List<String> endpointList = parseRedissonEndpoints(redis);
        if (endpointList.size() == 1) {
            Config config = new Config().setCodec(StringCodec.INSTANCE).setTransportMode(TransportMode.NIO);
            config.useSingleServer().setAddress(endpointList.get(0));
            return Redisson.create(config);
        } else {
            Config config = new Config().setCodec(StringCodec.INSTANCE).setTransportMode(TransportMode.NIO);
                config
                    .useClusterServers()
                    .setScanInterval(2000)
                    .setReadMode(ReadMode.MASTER)
                    .setSubscriptionMode(SubscriptionMode.SLAVE)
                    .setPingConnectionInterval(10000)//10秒
                    .setSubscriptionConnectionPoolSize(64)
                    .setSubscriptionsPerConnection(50)
                    .addNodeAddress(endpointList.toArray(new String[0]));
            return Redisson.create(config);
        }
    }

    private List<String> parseRedissonEndpoints(Redis redis) {
        return Arrays.stream(redis.endPoint).map(p -> "redis://" + p).collect(Collectors.toList());
    }

    public static class MadsRedis extends Redis{}

    @ToString
    @Getter
    @Setter
    public abstract static class Redis {

        protected String[] endPoint;

        protected int database;

    }

}
