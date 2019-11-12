package org.study.redis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.study.redis.service.RankService;

/**
 * @author lipo
 * @version v1.0
 * @date 2019-11-12 16:21
 */
@RestController
@RequestMapping("rank")
public class RankController {
    @Autowired
    private RankService rankService;

    @GetMapping("init")
    public Object init() {
        rankService.init();
        return "ok";
    }

    @GetMapping("day")
    public Object day() {
        return rankService.day();
    }

    @GetMapping("week")
    public Object week() {
        return rankService.week();
    }

}
