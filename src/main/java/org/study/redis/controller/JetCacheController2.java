package org.study.redis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.study.redis.model.User;
import org.study.redis.service.JetCacheService;
import org.study.redis.service.JetCacheService2;

/**
 * @author lipo
 * @version v1.0
 * @date 2020-01-16 14:57
 */
@RestController
@RequestMapping("/jetCache2")
public class JetCacheController2 {
    @Autowired
    private JetCacheService2 jetCacheService2;

    @GetMapping("get")
    public Object get(Integer id) {
        return jetCacheService2.getUserById(id);
    }

    @GetMapping("update")
    public Object update(Integer id, String name) {
        jetCacheService2.updateUser(new User(id, name));
        return "ok";
    }

    @GetMapping("delete")
    public Object delete(Integer id) {
        jetCacheService2.deleteUser(id);
        return "ok";
    }

}
