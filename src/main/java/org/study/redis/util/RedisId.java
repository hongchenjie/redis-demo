package org.study.redis.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

@Service
public class RedisId {
    private String DISTRIBUTE_ID = "DISTRIBUTE_ID:";
    private String date = LocalDate.now().toString().replace("-", "");

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private RedisLockRegistry redisLockRegistry;

    @Scheduled(cron = "0 0 0 0/1 * *")
    public void init() {
        deleteOldDays();

    }

    public void deleteOldDays() {
        //置换当前日期
        date = LocalDate.now().toString().replace("-", "");

        //删除前天的id
        Lock lock = redisLockRegistry.obtain("DELETE_OLD_DAYS");
        try {
            boolean b = lock.tryLock(1, TimeUnit.MINUTES);
            if (!b) {
                return;
            }
            Set<String> keys = redisTemplate.keys(DISTRIBUTE_ID + "*");
            if (keys == null || keys.size() == 0) {
                return;
            }

            String yesterday = LocalDate.now().minusDays(1).toString().replace("-", "");
            keys.forEach(key -> {
                Set<Object> daySet = redisTemplate.opsForHash().keys(key);
                if (daySet != null && daySet.size() > 0) {
                    daySet.forEach(day -> {
                        if (day.toString().compareTo(yesterday) < 0) {
                            redisTemplate.opsForHash().delete(key, day);
                        }
                    });
                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public String getId() {
        return doGetId("COMMON");
    }

    public String getMachineId() {
        return doGetId("MACHINE");
    }

    private String doGetId(String key) {
        Long increment = redisTemplate.opsForHash().increment(DISTRIBUTE_ID + key, date, 1);
        return date + String.format("%08d", increment);
    }

}
