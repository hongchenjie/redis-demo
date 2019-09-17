package org.study.redis.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class RedisInitId {
    private String DISTRIBUTE_INIT_ID = "DISTRIBUTE_INIT_ID";

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String LUA_SCRIPT =
            "local id = redis.call('GET', KEYS[1])\n" +
                    "if not id then\n" +
                    "  redis.call('SET', KEYS[1], ARGV[1])\n" +
                    "end\n" +
                    "return redis.call('INCR', KEYS[1])\n";


    private final RedisScript<Long> idScript = new DefaultRedisScript<>(LUA_SCRIPT, Long.class);

    public Long getId() {
        return doGetId(DISTRIBUTE_INIT_ID, "100000");
    }

    public Long doGetId(String id, String initValue) {
        //RedisTemplate.execute有问题，返回Object，不能返回范型Long
        return redisTemplate.execute(idScript, Collections.singletonList(id), initValue);
    }
}
