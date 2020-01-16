package org.study.redis.service.impl;

import com.alicp.jetcache.anno.CacheInvalidate;
import com.alicp.jetcache.anno.CacheUpdate;
import com.alicp.jetcache.anno.Cached;
import org.springframework.stereotype.Service;
import org.study.redis.model.User;
import org.study.redis.service.JetCacheService2;

/**
 * @author lipo
 * @version v1.0
 * @date 2020-01-16 14:59
 */
@Service
public class JetCacheServiceImpl2 implements JetCacheService2 {

    @Cached(name = "student_cache:", key = "#id", expire = 3600)
    @Override
    public User getUserById(Integer id) {
        return new User(id, "lipo");
    }

    @CacheUpdate(name = "student_cache:", key = "#user.id", value = "#user")
    @Override
    public void updateUser(User user) {
        System.out.println("updateUser: " + user);
    }

    @CacheInvalidate(name = "student_cache:", key = "#id")
    @Override
    public void deleteUser(Integer id) {
        System.out.println("deleteUser :" + id);
    }
}
