package kr.hoppang.config.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import kr.hoppang.domain.chassis.price.pricecriteria.AdditionalChassisPriceCriteria;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableCaching
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Value("${spring.data.redis.database}")
    private int database;

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        PolymorphicTypeValidator typeValidator = BasicPolymorphicTypeValidator
                .builder()
                .allowIfSubType(Object.class)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.activateDefaultTyping(typeValidator, DefaultTyping.NON_FINAL);
        GenericJackson2JsonRedisSerializer redisSerializer = new GenericJackson2JsonRedisSerializer(
                objectMapper);

        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer));

        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(redisCacheConfiguration)
                .build();
    }

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration serverConfig = new RedisStandaloneConfiguration(host, port);

        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
                .shutdownTimeout(Duration.ofMillis(10000))
                .commandTimeout(Duration.ofMillis(10000))
                .build();

        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(
                serverConfig, clientConfig);

        lettuceConnectionFactory.setShareNativeConnection(false);

        return lettuceConnectionFactory;
    }

    @Bean
    public RedisTemplate<String, Map<String, String>> redisTemplate() {
        RedisTemplate<String, Map<String, String>> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setEnableTransactionSupport(true);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return redisTemplate;
    }

    @Bean
    public RedisTemplate<String, List<AdditionalChassisPriceCriteria>> additionalChassisPriceCriteriaRedisTemplate() {
        RedisTemplate<String, List<AdditionalChassisPriceCriteria>> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());

        // ObjectMapper 설정
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // Jackson 설정 적용
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));

        return redisTemplate;
    }

    @Bean
    public RedisClient redisClient() {
        return RedisClient.create("redis://" + host + ":" + port);
    }

    @Bean
    public StatefulRedisConnection<String, String> connection(RedisClient redisClient) {
        return redisClient.connect();
    }
}
