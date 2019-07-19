package org.study.redis.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

@RestController
public class RedisController {

    //虽然配置bean泛型是<String, Object>，但是可以注入<String, String>
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private DefaultRedisScript<Boolean> redisScript;

    @RequestMapping("redis")
    public String getKey() {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String visitCount_redis = "visitCount_redis";
        Boolean flag = valueOperations.setIfAbsent(visitCount_redis, "1", 10, TimeUnit.SECONDS);
        if (flag) {
            System.out.println("第一次访问");
        } else {
            valueOperations.increment(visitCount_redis);
        }
        String s = valueOperations.get(visitCount_redis);
        int i  = Integer.valueOf(s);
        if (i > 10) {
            return "此接口十秒内访问超过10次，请稍后访问";
        }
        return s;
    }

    @RequestMapping("/redisTest")
    public void redisTest(){
        ListOperations<String, String> listOperations = redisTemplate.opsForList();
        listOperations.leftPushAll("l1", "a1", "a2");
        listOperations.leftPushAll("l2", "v1", "v2");
        BoundListOperations<String, String> boundListOperations = redisTemplate.boundListOps("l2");
        String o = boundListOperations.rightPop();
        System.out.println(o);
    }

    //http://localhost:9000/luaTest
    @RequestMapping("/luaTest")
    public String luaTest(){
        String key = "testredislua";
        redisTemplate.delete(key);
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String hahaha = "hahaha";
        valueOperations.set(key, hahaha);
        String s = valueOperations.get(key);
        System.out.println(s);

        redisTemplate.execute(redisScript, Collections.singletonList(key), hahaha, "3333");
        String s1 = valueOperations.get(key);
        System.out.println(s1);

        return null;
    }

}
