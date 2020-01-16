package org.study.redis.service;

import org.study.redis.model.User;

/**
 * @author lipo
 * @version v1.0
 * @date 2020-01-16 14:58
 */
public interface JetCacheService2 {

    User getUserById(Integer id);


    void updateUser(User user);


    void deleteUser(Integer id);
}
