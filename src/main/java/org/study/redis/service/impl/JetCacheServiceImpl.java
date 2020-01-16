package org.study.redis.service.impl;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CreateCache;
import org.springframework.stereotype.Service;
import org.study.redis.model.User;
import org.study.redis.service.JetCacheService;

/**
 * @author lipo
 * @version v1.0
 * @date 2020-01-16 14:59
 */
@Service
public class JetCacheServiceImpl implements JetCacheService {
    @CreateCache(name = "user_cache:", expire = 3600, cacheType = CacheType.BOTH, localLimit = 50)
    private Cache<Integer, User> userCache;

    @Override
    public User getUserById(Integer userId) {
        User user = userCache.get(userId);
        if (user == null) {
            user = new User(userId, "lipo");
            userCache.put(userId, user);
        }
        return user;
    }

    @Override
    public void updateUser(User user) {
        userCache.put(user.getId(), user);
        System.out.println("updateUser: " + user);

    }

    @Override
    public void deleteUser(Integer userId) {
        userCache.remove(userId);
        System.out.println("deleteUser :" + userId);
    }
}
