package org.study.redis.component;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.study.redis.model.User;

/**
 * @author lipo
 * @version v1.0
 * @date 2019-11-13 10:44
 */
@Component
@Slf4j
public class MqProducer {

    @Autowired
    private RedisTemplate redisTemplate;

    public void send(String topic, String msg) {
        log.info("topic ={}, msg = {}", topic, msg);
        redisTemplate.convertAndSend(topic, msg);
    }

    public void send(String topic, User user) {
        log.info("topic ={}, user = {}", topic, user);
        send(topic, JSON.toJSONString(user));
    }
}
