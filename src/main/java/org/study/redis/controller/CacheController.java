package org.study.redis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.study.redis.service.UserService;

/**
 * @author lipo
 * @version v1.0
 * @date 2019-11-14 15:47
 */
@RequestMapping("cache")
@RestController
public class CacheController {
    @Autowired
    private UserService userService;

    @GetMapping("get")
    public Object get(Integer id) {
        return userService.get(id);
    }

    @GetMapping("update")
    public Object update(Integer id, String name) {
        return userService.update(id, name);
    }

    @GetMapping("delete")
    public Object delete(Integer id) {
        return userService.delete(id);
    }
}
