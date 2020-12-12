package my.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashMap;
import java.util.Map;

import static my.config.RedisConstants.CACHE_NAME_CAR;

@Configuration
public class RedisConfig  {

    @Value("${my.redis.host}")
    private String host;

    @Value("${my.redis.port}")
    private int port;

    @Bean
    public CacheManager cacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager(redisTemplate());

        // usePrefix를 true로 지정해야 "${cacheName}:${key}" 형태로 Redis에 Key가 생성된다.
        // false(default)이면, "${key}"로 Redis에 Key가 생성된다.
        redisCacheManager.setUsePrefix(true);

        int defaultExpireSec = 600;
        redisCacheManager.setDefaultExpiration(defaultExpireSec);

        // Sets the expire time (in seconds) for cache regions (by key).
        Map<String, Long> expiresByKey = new HashMap<>();
        expiresByKey.put(CACHE_NAME_CAR, 60L);
        redisCacheManager.setExpires(expiresByKey);

        return redisCacheManager;
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        int poolSize = 16; // multi thread 환경에서도 15개 이상은 크게 성능 향상이 없음
        poolConfig.setMinIdle(poolSize);
        poolConfig.setMaxIdle(poolSize);
        poolConfig.setMaxTotal(poolSize);
        //poolConfig.setTestOnBorrow(true); // getConnection 마다 체크하지 말고, idle시에 체크하도록 하자(약 30~40% 성능향상)
        // 5분 주기로 5개의 connection이 유효한지 검사한다. 15분 경과시 모든 connection의 검사가 이루어진다.
        poolConfig.setTestWhileIdle(true);
        poolConfig.setTimeBetweenEvictionRunsMillis(5 * 60L * 1000L); // 5min
        poolConfig.setNumTestsPerEvictionRun(5); // 5 개

        JedisConnectionFactory factory = new JedisConnectionFactory(poolConfig);
        factory.setHostName(host);
        factory.setPort(port);
        factory.setUsePool(true);

        return factory;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());

        // GenericJackson2JsonRedisSerializer 사용
        // 커스텀 ObjectMapper를 사용하고 싶으면, 반드시 GenericJackson2JsonRedisSerializer의 생성자에 있는 ObjectMapper 옵션을 포함하도록 한다.
        // 그렇지 않으면 LinkedHashMap으로 반환되어 캐스팅 에러가 발생한다.
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        /*
        Jackson2JsonRedisSerializer<Object> 사용
        Jackson2JsonRedisSerializer<Object> jsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        jsonRedisSerializer.setObjectMapper(om);

        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(jsonRedisSerializer);
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(jsonRedisSerializer);
        */

        return template;
    }
}
