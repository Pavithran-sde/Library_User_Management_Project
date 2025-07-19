package com.LibraryManagement.LibraryUserManagement.Common.Redis.Resources;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@AllArgsConstructor
@Component
public class RedisResourceLock {

    @Autowired
    private final RedisTemplate<String, String> redisTemplate;


    public boolean tryLock(String resourceType,Long resourceId, String userId, Duration ttl) {
        String key = buildLockKey(resourceType, resourceId);
        Boolean locked = redisTemplate.opsForValue().setIfAbsent(key, userId, ttl);
        return Boolean.TRUE.equals(locked);
    }

    public void releaseLock(String resourceType, Long resourceId, String userId) {
        String key = buildLockKey(resourceType, resourceId);
        String currentOwner = redisTemplate.opsForValue().get(key);

        if (userId.equals(currentOwner)) {
            redisTemplate.delete(key);
        }
    }

    public boolean isLocked(String resourceType, Long resourceId) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(buildLockKey(resourceType, resourceId)));
    }

    public static String buildLockKey(String resourceType, Long resourceId) {

        if(resourceType.equals("table")){
            return "table" + ":lock:"+ resourceId;
        }else if(resourceType.equals("cport")){
            return "cport:" + ":lock:" + resourceId;
        }
        else{
            throw new RuntimeException();
        }
    }

}
