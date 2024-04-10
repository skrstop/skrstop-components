//package com.skrstop.framework.components.example.starters.simple.controller;
//
//import com.skrstop.framework.components.example.starters.simple.entity.request.ExampleRequestRedis;
//import com.skrstop.framework.components.example.starters.simple.service.ExampleDynamicRedisService;
//import com.skrstop.framework.components.starter.redis.service.RedisService;
//import com.skrstop.framework.components.util.value.data.ObjectUtil;
//import lombok.extern.slf4j.Slf4j;
//import org.redisson.api.RLock;
//import org.redisson.api.RedissonClient;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.dao.DataAccessException;
//import org.springframework.data.redis.core.RedisOperations;
//import org.springframework.data.redis.core.SessionCallback;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.concurrent.TimeUnit;
//
///**
// * @author 蒋时华
// * @date 2023-11-30 17:52:44
// */
//@RestController
//@Slf4j
//@Validated
//public class ExampleRedisController {
//
//    @Autowired
//    private RedisService redisService;
//    @Autowired
//    private ExampleDynamicRedisService exampleDynamicRedisService;
//    @Autowired
//    private RedissonClient redissonClient;
//
//    /**
//     * redis 样例
//     */
//    @GetMapping("/exampleRedis")
//    public void exampleRedis() {
//        ExampleRequestRedis origin = ExampleRequestRedis.builder()
//                .valStr("aaaaaa")
//                .valBol(false)
//                .valData(LocalDateTime.now())
//                .valLong(11111111L)
//                .valInt(11111)
//                .build();
//        String cacheKey = "test";
//        redisService.set(cacheKey, origin);
//        ExampleRequestRedis cache = redisService.get(cacheKey, ExampleRequestRedis.class);
//        System.out.println(cache.toString());
//        redisService.remove(cacheKey);
//        ExampleRequestRedis cache2 = redisService.get(cacheKey, ExampleRequestRedis.class);
//        if (ObjectUtil.isNull(cache2)) {
//            System.out.println("cache already removed");
//        }
//    }
//
//    /**
//     * redis pipline 样例
//     */
//    @GetMapping("/exampleRedisPipline")
//    public void exampleRedisPipline() {
//        ExampleRequestRedis origin = ExampleRequestRedis.builder()
//                .valStr("aaaaaa")
//                .valBol(false)
//                .valData(LocalDateTime.now())
//                .valLong(11111111L)
//                .valInt(11111)
//                .build();
//        redisService.set("test1", origin);
//        redisService.set("test2", origin);
//
//        List<?> list = redisService.executePipelined(new SessionCallback<Object>() {
//            @Override
//            public <K, V> Object execute(RedisOperations<K, V> operations) throws DataAccessException {
//                V test1 = operations.opsForValue().get("test1");
//                operations.delete((K) "test1");
//                operations.delete((K) "test2");
//                return null;
//            }
//        });
//        ExampleRequestRedis test = redisService.get("test1", ExampleRequestRedis.class);
//        System.out.println("aaa");
//    }
//
//
//    /**
//     * Redisson 样例
//     */
//    @GetMapping("/exampleRedisson")
//    public void exampleRedisson() {
//
//        RLock lockKey = redissonClient.getLock("lock_key");
//        boolean tryLock = lockKey.tryLock();
//        if (tryLock) {
//            try {
//                //TODO do something
//                TimeUnit.SECONDS.sleep(5);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            } finally {
//                lockKey.unlock();
//            }
//            System.out.println("get lock success, and do something");
//            return;
//        }
//        System.out.println("get lock fail");
//    }
//
//    /**
//     * redis 样例
//     */
//    @GetMapping("/exampleRedisDynamic")
//    public void exampleRedisDynamic() {
////        exampleDynamicRedisService.primary();
//        exampleDynamicRedisService.redisDb0();
//        exampleDynamicRedisService.redisDb1();
//        exampleDynamicRedisService.redisDb2();
////        exampleDynamicRedisService.mutl();
////        exampleDynamicRedisService.service();
//    }
//
//    /**
//     * redisson client 样例
//     */
//    @GetMapping("/exampleRedissonDynamicClient")
//    public void exampleRedissonDynamicClient() {
//        exampleDynamicRedisService.redissonClient();
//    }
//
//    /**
//     * redisson aop client 样例
//     */
//    @GetMapping("/exampleRedissonDynamicClientAop")
//    public void exampleRedissonDynamicClientAop() {
//        exampleDynamicRedisService.redissonClientAop1();
//        exampleDynamicRedisService.redissonClientAop2();
//        exampleDynamicRedisService.redissonClientAop3();
//    }
//
//}
