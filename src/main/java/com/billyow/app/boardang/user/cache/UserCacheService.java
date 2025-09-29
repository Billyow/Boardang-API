package com.billyow.app.boardang.user.cache;
import com.billyow.app.boardang.user.model.User;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class UserCacheService {

    private static final String USER_CACHE_PREFIX = "user:";

    private final RedisTemplate<String, Object> redisTemplate;

    public UserCacheService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Optional<User> getUserByEmail(String email) {
        String key = USER_CACHE_PREFIX + "email:" + email;
        Object cached = redisTemplate.opsForValue().get(key);
        return Optional.ofNullable((User) cached);
    }

    public void cacheUserByEmail(String email, User user) {
        String key = USER_CACHE_PREFIX + "email:" + email;
        redisTemplate.opsForValue().set(key, user, 10, TimeUnit.MINUTES); // TTL opcional
    }

    public void evictUserByEmail(String email) {
        String key = USER_CACHE_PREFIX + "email:" + email;
        redisTemplate.delete(key);
    }
}
