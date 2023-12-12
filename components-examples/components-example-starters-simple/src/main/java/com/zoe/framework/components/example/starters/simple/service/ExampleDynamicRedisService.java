package com.zoe.framework.components.example.starters.simple.service;

import com.zoe.framework.components.example.starters.simple.entity.request.ExampleRequestRedis;
import com.zoe.framework.components.starter.redis.configuration.dynamic.annotation.DSRedis;
import com.zoe.framework.components.starter.redis.service.DynamicRedisService;
import com.zoe.framework.components.starter.redis.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author 蒋时华
 * @date 2023-12-08 10:39:13
 */
@Service
public class ExampleDynamicRedisService {

    @Autowired
    private RedisService redisService;
    @Autowired
    private DynamicRedisService dynamicRedisService;
    @Autowired
    private ExampleDynamicRedisService selfService;


    public void primary() {
        ExampleRequestRedis origin = ExampleRequestRedis.builder()
                .valStr("primary")
                .valBol(false)
                .valData(LocalDateTime.now())
                .valLong(11111111L)
                .valInt(11111)
                .build();
        redisService.set("primary", origin, 10);
        this.db1();
    }

    @DSRedis("db0")
    public void db0() {
        ExampleRequestRedis origin = ExampleRequestRedis.builder()
                .valStr("000000")
                .valBol(false)
                .valData(LocalDateTime.now())
                .valLong(11111111L)
                .valInt(11111)
                .build();
        redisService.set("db0", origin, 10);
    }

    @DSRedis("db1")
    public void db1() {
        ExampleRequestRedis origin = ExampleRequestRedis.builder()
                .valStr("111111")
                .valBol(false)
                .valData(LocalDateTime.now())
                .valLong(11111111L)
                .valInt(11111)
                .build();
        redisService.set("db1", origin, 10);
    }

    @DSRedis("db2")
    public void db2() {
        ExampleRequestRedis origin = ExampleRequestRedis.builder()
                .valStr("222222")
                .valBol(false)
                .valData(LocalDateTime.now())
                .valLong(11111111L)
                .valInt(11111)
                .build();
        redisService.set("db2", origin, 10);
    }

    @DSRedis("db0")
    public void mutl() {
        selfService.db1();
        ExampleRequestRedis origin = ExampleRequestRedis.builder()
                .valStr("mutl")
                .valBol(false)
                .valData(LocalDateTime.now())
                .valLong(11111111L)
                .valInt(11111)
                .build();
        redisService.set("mutl", origin, 10);
    }

    public void service() {
        dynamicRedisService.set("db0", "aaaaa", "bbbbbb", 10);
        dynamicRedisService.set("db1", "aaaaa", "bbbbbb", 10);
        dynamicRedisService.set("db2", "aaaaa", "bbbbbb", 10);
    }
}
