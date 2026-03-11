package cn.master.nexus.common.config;

import cn.master.nexus.common.util.LogUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author : 11's papa
 * @since : 2026/3/3, 星期二
 **/
@Component
@RequiredArgsConstructor
public class RedissonCacheComp {
    private final CacheManager caffeineCacheManager;
    private final RedisCacheManager redisCacheManager;

    /**
     * 获取 Redis 缓存桶
     */
    public <T> T getCache(String key, Class<T> resultType) {
        // L1: 从 Caffeine 读取
        Cache caffeeinCache = caffeineCacheManager.getCache("localCache");
        assert caffeeinCache != null;
        T localValue = caffeeinCache.get(key, resultType);
        if (Objects.nonNull(localValue)) {
            LogUtils.debug("[Cache Hit] L1(Caffeine) Hit for key: {}", key);
            return localValue;
        }
        // L2: 从 redis 读取
        Cache redisCache = redisCacheManager.getCache("redisCache");
        assert redisCache != null;
        T redisValue = redisCache.get(key, resultType);
        if (Objects.nonNull(redisValue)) {
            LogUtils.debug("[Cache Hit] L2(Redis) Hit for key: {}", key);
            caffeeinCache.put(key, redisValue);
            return redisValue;
        }
        return null;
    }

    public void setCache(String key, Object value) {
        Cache caffeeinCache = caffeineCacheManager.getCache("localCache");
        assert caffeeinCache != null;
        caffeeinCache.put(key, value);
        Cache redisCache = redisCacheManager.getCache("redisCache");
        assert redisCache != null;
        redisCache.put(key, value);
    }


    public void evict(String key) {
        Cache caffeeinCache = caffeineCacheManager.getCache("localCache");
        assert caffeeinCache != null;
        caffeeinCache.evict(key);
        Cache redisCache = redisCacheManager.getCache("redisCache");
        assert redisCache != null;
        redisCache.evict(key);
        Cache nullValueCache = redisCacheManager.getCache("nullValueCache");
        assert nullValueCache != null;
        nullValueCache.evict(key);
    }
}
