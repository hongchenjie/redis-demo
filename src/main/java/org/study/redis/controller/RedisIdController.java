package org.study.redis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.study.redis.component.RedisId;

import java.time.Duration;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/redisId")
public class RedisIdController {

    @Autowired
    private RedisId redisId;

    @RequestMapping("/commonId")
    public String commonId(){
        return redisId.getId();
    }

    @RequestMapping("/machineId")
    public String machineId(){
        return redisId.getMachineId();
    }

    @RequestMapping("/deleteOldDays")
    public String deleteOldDays(){
        redisId.deleteOldDays();
        return "deleteOldDays ok";
    }

    //一万个id才半秒，速度很快
    @RequestMapping("/manyId")
    public String manyId(){
        LocalDateTime now = LocalDateTime.now();
        for (int i = 0; i < 10000; i++) {
            redisId.getId();
        }
        System.out.println(Duration.between(now, LocalDateTime.now()));//PT0.511S
        return "manyId ok";
    }

}
