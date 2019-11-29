package org.study.redis.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.study.redis.model.User;

@Slf4j
@Service
public class UserService {
    private static final String USER_CACHE = "USER_CACHE";

    public void update() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Cacheable(value = USER_CACHE, key = "#id")
    public User get(Integer id) {
        User user = new User(1, "lipo");
        log.info("select user = {}", user);
        return user;
    }


}
