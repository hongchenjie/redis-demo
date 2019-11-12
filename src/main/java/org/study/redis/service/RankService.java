package org.study.redis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.locks.Lock;

/**
 * @author lipo
 * @version v1.0
 * @date 2019-11-12 16:22
 */
@Service
public class RankService {

    private static final String INTEGRAL_RANK = "INTEGRAL_RANK";
    private static final String INTEGRAL_RANK_WEEK_INIT = INTEGRAL_RANK + ":WEEK_INIT";
    private static final String INTEGRAL_RANK_WEEK_TOTAL = INTEGRAL_RANK + ":WEEK_TOTAL";
    private static final String INTEGRAL_RANK_MONTH = INTEGRAL_RANK + ":MONTH";
    private static final String INTEGRAL_RANK_TODAY = INTEGRAL_RANK + ":TODAY";

    private static final Random RANDOM = new Random();

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private RedisLockRegistry redisLockRegistry;

    /**
     * 初始化数据
     * @author lipo
     * @date 2019-11-12 16:24
     */
    public void init() {
        ZSetOperations<String, Object> zSetOperations = redisTemplate.opsForZSet();
        LocalDate now = LocalDate.now();
        for (int j = 0; j < 40; j++) {
            String set = INTEGRAL_RANK + ":" + now.minusDays(j).toString();
            for (int i = 0; i < 20; i++) {
                //分数累加，用户不存在时，添加
                zSetOperations.incrementScore(set, RANDOM.nextInt(100) + "", i);
            }
        }
    }

    /**
     * 日排名
     * @author lipo
     * @date 2019-11-12 16:23
     */
    public Object day() {
        ZSetOperations<String, Object> zSetOperations = redisTemplate.opsForZSet();
        String day = INTEGRAL_RANK + ":" + LocalDate.now().toString();
        //索引从0开始，包含start和end，共end - start + 1个元素
        Set<ZSetOperations.TypedTuple<Object>> typedTuples = zSetOperations.reverseRangeWithScores(day, 0, 9);
        /*
         * [{
         * 	"score": 265.0,//积分
         * 	"value": "74"//用户
         * }, {
         * 	"score": 263.0,
         * 	"value": "37"
         * }]
         */
        return typedTuples;
    }

    /**
     * 周排名
     * intersectAndStore交集，相同的用户存到第三个集合中，积分是前两个集合分数的和，不相同的用户不保存，原来两个集合数据不变
     * @author lipo
     * @date 2019-11-12 17:27
     */
    public Object week() {
        ZSetOperations<String, Object> zSetOperations = redisTemplate.opsForZSet();
        LocalDate now = LocalDate.now();

        //是否新的一天
        boolean isNewDay = isNewDay(now);
        if (isNewDay) {
            refreshWeek(zSetOperations, now);
        }

        //加上今天积分
        String day = INTEGRAL_RANK + ":" + now.toString();
        redisTemplate.delete(INTEGRAL_RANK_WEEK_TOTAL);
        zSetOperations.unionAndStore(INTEGRAL_RANK_WEEK_INIT, day, INTEGRAL_RANK_WEEK_TOTAL);

        Set<ZSetOperations.TypedTuple<Object>> typedTuples = zSetOperations.reverseRangeWithScores(INTEGRAL_RANK_WEEK_TOTAL, 0, 9);

        return typedTuples;
    }

    /**
     * 刷新周积分初始值，枷锁
     * @author lipo
     * @date 2019-11-12 18:57
     */
    private void refreshWeek(ZSetOperations<String, Object> zSetOperations, LocalDate now) {
        Lock lock = redisLockRegistry.obtain("refreshWeek");
        try {
            lock.lock();

            //删除key
            redisTemplate.delete(INTEGRAL_RANK_WEEK_INIT);

            //一周前6天积分之和
            for (int i = 1; i < 7; i++) {
                String yesterday = INTEGRAL_RANK + ":" + now.minusDays(i).toString();

                //并集，积分是相同用户积分的和
                zSetOperations.unionAndStore(INTEGRAL_RANK_WEEK_INIT, yesterday, INTEGRAL_RANK_WEEK_INIT);
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * 是否是新的一天
     * @author lipo
     * @date 2019-11-12 18:53
     */
    private boolean isNewDay(LocalDate now) {
        //今天标记
        Object o = redisTemplate.opsForValue().get(INTEGRAL_RANK_TODAY);
        //首次执行
        if (o == null) {
            redisTemplate.opsForValue().set(INTEGRAL_RANK_TODAY, now.toString());
            return true;
        }

        //今天再次执行
        if (now.toString().equals(o.toString())) {
            return false;
        }

        //今天首次执行
        redisTemplate.opsForValue().set(INTEGRAL_RANK_TODAY, now.toString());
        return true;
    }

}
