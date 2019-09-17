package org.study.redis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.study.redis.component.RedisInitId;

@RestController
@RequestMapping("/redisInitId")
public class RedisInitIdController {

    @Autowired
    private RedisInitId redisInitId;

    //http://localhost:9000/redisInitId/commonId
    @RequestMapping("/commonId")
    public Object commonId(){
        return redisInitId.getId();
    }

}
