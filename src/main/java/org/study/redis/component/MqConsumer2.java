package org.study.redis.component;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;
import org.study.redis.model.User;

/**
 * @author lipo
 * @version v1.0
 * @date 2019-11-13 10:45
 */
@Component("consumer2")
@Slf4j
public class MqConsumer2 implements MessageListener {

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String msg = new String(message.getBody());
        String topic = new String(message.getChannel());
        String p = new String(pattern);
        log.info("topic ={}, msg = {}, pattern = {}", topic, msg, p);

        User user = JSON.parseObject(msg, User.class);
        log.info("user = {}", user);
    }
}
