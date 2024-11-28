package com.trabalho.multi.trabalhomulti.connection;

import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.jackson2.SimpleGrantedAuthorityMixin;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.config.DefaultDriverOption;
import com.datastax.oss.driver.api.core.config.DriverConfigLoader;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.trabalho.multi.trabalhomulti.position.DataByVehicle;
import com.trabalho.multi.trabalhomulti.position.Position;

@Configuration
@EnableCaching

@EnableRedisRepositories
public class AppConfig {
    @Autowired
    CassandraConfig cassandraConfig;

    @Bean
    @Autowired
    CqlSession session() throws URISyntaxException {
        DriverConfigLoader loader = DriverConfigLoader.programmaticBuilder()
                .withDuration(DefaultDriverOption.REQUEST_TIMEOUT, Duration.ofSeconds(10))
                // .startProfile("slow")
                // .withDuration(DefaultDriverOption.REQUEST_TIMEOUT, Duration.ofSeconds(30))
                // .endProfile()
                .build();

        return CqlSession.builder()
                .withCloudSecureConnectBundle(
                        cassandraConfig.getSecureConnect())
                .withAuthCredentials(CassandraConfig.getUsername(),
                        CassandraConfig.getSecret())
                .withKeyspace(CqlIdentifier.fromCql(CassandraConfig.getKeyspaceName()))
                .withConfigLoader(loader)
                .build();
    }

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration rConfiguration = new RedisStandaloneConfiguration(
                "redis-11448.c1.us-east1-2.gce.redns.redis-cloud.com", 11448);
        rConfiguration.setPassword("g7DfzGBucPDGzVgyEljctK5JEo3PqdxV");
        rConfiguration.setUsername("default");

        return new LettuceConnectionFactory(rConfiguration);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setHashValueSerializer(new StringRedisSerializer());
        template.setConnectionFactory(redisConnectionFactory());
        return template;
    }

    @Bean
    public RedisCacheConfiguration cacheConfiguration() {
        PolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator
                .builder()
                .allowIfBaseType(Object.class)
                .allowIfBaseType(DataByVehicle.class)
                .allowIfBaseType(Position.class)

                .build();
        ObjectMapper objectMapper = new ObjectMapper().addMixIn(SimpleGrantedAuthority.class,
                SimpleGrantedAuthorityMixin.class);
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .activateDefaultTyping(ptv, ObjectMapper.DefaultTyping.EVERYTHING);
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10)).disableCachingNullValues()
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new GenericJackson2JsonRedisSerializer(objectMapper)));

    }

}
