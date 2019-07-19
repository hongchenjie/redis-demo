package org.study.redis.service;

import org.springframework.stereotype.Service;

@Service
public class UserService {
    public void update() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
