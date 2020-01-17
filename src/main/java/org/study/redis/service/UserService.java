package org.study.redis.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.study.redis.model.User;

@Slf4j
@Service
public class UserService {
    private static final String USER_CACHE = "USER_CACHE";

    @Cacheable(value = USER_CACHE, key = "#id")
    public User get(Integer id) {
        User user = new User(id, "lipo");
        log.info("select user = {}", user);
        return user;
    }

    @CachePut(value = USER_CACHE, key = "#id")
    public User update(Integer id, String name) {
        User user = new User(id, name);
        log.info("update user = {}", user);
        return user;
    }

    @CacheEvict(value = USER_CACHE, key = "#id")
    public User delete(Integer id) {
        User user = new User(id, "asdf");
        log.info("delete id = {}", id);
        return user;
    }


    public void update() {

    }
}
