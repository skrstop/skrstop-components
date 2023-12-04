//package com.zoe.framework.components.example.starters.controller;
//
//import com.zoe.framework.components.example.starters.entity.request.ExampleRequestRedis;
//import com.zoe.framework.components.starter.redis.service.RedisService;
//import com.zoe.framework.components.util.value.data.ObjectUtil;
//import lombok.extern.slf4j.Slf4j;
//import org.redisson.api.RLock;
//import org.redisson.api.RedissonClient;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.time.LocalDateTime;
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
//
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
//}
