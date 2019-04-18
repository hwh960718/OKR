package com.mobvista.okr.service;

import com.mobvista.okr.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author Weier Gu (顾炜)
 * @date 2018/6/15 17:07
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class RedisServiceTest {

    @Resource
    private RedisService redisService;


    /**
     * redis压力测试
     */
    @Test
    public void lock() {
        System.out.println("lock ========= start");
        String key = "test:lock:";
        for (int i = 0; i < 10; i++) {
            boolean lock = redisService.lock(key + i, 30);
            System.out.println(i + ":" + lock);
        }
        System.out.println("lock ========= end");
    }


    /**
     * redis压力测试
     */
    @Test
    public void lock02() {
        System.out.println("lock ========= start");
        String key = "test:lock:";
        for (int i = 0; i < 1000; i++) {
            int finalI = i;
            new Thread(() -> {
                boolean lock = redisService.lock(key + finalI, 10);
                System.out.println(finalI + ":" + lock);
            }).run();

            new Thread(() -> {
                redisService.unLock(key + finalI);
                System.out.println(finalI + " : unlocked");
            }).run();
        }
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("lock ========= end");
    }


    /**
     * redis压力测试
     */
    @Test
    public void unLock() {
        System.out.println("lock ========= start");
        String key = "test:lock:";
        for (int i = 0; i < 100; i++) {
            redisService.unLock(key + i);
            System.out.println(i + "unlock");
        }
        System.out.println("lock ========= end");
    }
}