package com.zoe.framework.components.example.starters.simple.service;

import com.zoe.framework.components.example.starters.simple.entity.request.ExampleRequestRedis;
import com.zoe.framework.components.starter.redis.annotation.DSRedis;
import com.zoe.framework.components.starter.redis.annotation.DSRedisson;
import com.zoe.framework.components.starter.redis.service.DynamicRedisService;
import com.zoe.framework.components.starter.redis.service.RedisService;
import com.zoe.framework.components.starter.redis.service.impl.DynamicAopRedissonClient;
import com.zoe.framework.components.starter.redis.service.impl.DynamicRedissonClient;
import org.redisson.api.RedissonClient;
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

    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private DynamicRedissonClient dynamicRedissonClient;
    @Autowired
    private DynamicAopRedissonClient dynamicAopRedissonClient;


    public void redisPrimary() {
        ExampleRequestRedis origin = ExampleRequestRedis.builder()
                .valStr("primary")
                .valBol(false)
                .valData(LocalDateTime.now())
                .valLong(11111111L)
                .valInt(11111)
                .build();
        redisService.set("primary", origin, 10);
        this.redisDb1();
    }

    @DSRedis("db0")
    public void redisDb0() {
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
    public void redisDb1() {
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
    public void redisDb2() {
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
    public void redisSelfMulti() {
        selfService.redisDb1();
        ExampleRequestRedis origin = ExampleRequestRedis.builder()
                .valStr("mutl")
                .valBol(false)
                .valData(LocalDateTime.now())
                .valLong(11111111L)
                .valInt(11111)
                .build();
        redisService.set("mutl", origin, 10);
    }

    public void redisMulti() {
        dynamicRedisService.set("db0", "aaaaa", "bbbbbb", 10);
        dynamicRedisService.set("db1", "aaaaa", "bbbbbb", 10);
        dynamicRedisService.set("db2", "aaaaa", "bbbbbb", 10);
    }


    public void redissonClient() {
        redissonClient.getLock("lock0").tryLock();
        dynamicRedissonClient.getLock("r_db0", "lock1").tryLock();
        dynamicRedissonClient.getLock("r_db1", "lock2").tryLock();
        dynamicRedissonClient.getLock("r_db2", "lock3").tryLock();
    }

    @DSRedisson("r_db0")
    public void redissonClientAop1() {
        dynamicAopRedissonClient.getLock("lock1_aop").tryLock();
    }

    @DSRedisson("r_db1")
    public void redissonClientAop2() {
        dynamicAopRedissonClient.getLock("lock2_aop").tryLock();
    }

    @DSRedisson("r_db2")
    public void redissonClientAop3() {
        dynamicAopRedissonClient.getLock("lock3_aop").tryLock();
    }
}
