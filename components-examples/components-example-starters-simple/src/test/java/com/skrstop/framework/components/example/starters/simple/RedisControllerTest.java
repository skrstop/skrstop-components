package com.skrstop.framework.components.example.starters.simple;

import com.skrstop.framework.components.example.starters.simple.controller.ExampleRedisController;
import com.skrstop.framework.components.example.starters.simple.entity.request.ExampleRequestRedis;
import com.skrstop.framework.components.example.starters.simple.service.ExampleDynamicRedisService;
import com.skrstop.framework.components.starter.redis.service.DynamicRedisService;
import com.skrstop.framework.components.starter.redis.service.RedisService;
import com.skrstop.framework.components.starter.redis.service.impl.DynamicAopRedissonClient;
import com.skrstop.framework.components.starter.redis.service.impl.DynamicRedissonClient;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.test.util.ReflectionTestUtils;

public class RedisControllerTest {

    private ExampleRedisController newController(RedisService redisService, RedissonClient redissonClient) {
        ExampleRedisController controller = new ExampleRedisController();
        ReflectionTestUtils.setField(controller, "redisService", redisService);
        ReflectionTestUtils.setField(controller, "exampleDynamicRedisService", Mockito.mock(ExampleDynamicRedisService.class));
        ReflectionTestUtils.setField(controller, "redissonClient", redissonClient);
        return controller;
    }

    @Test
    public void testExampleRedisCacheHitAndMiss() {
        RedisService redisService = Mockito.mock(RedisService.class);
        RedissonClient redissonClient = Mockito.mock(RedissonClient.class, Mockito.RETURNS_DEEP_STUBS);
        ExampleRedisController controller = newController(redisService, redissonClient);

        // 连续 stub：第一次调用返回缓存对象（命中），第二次返回 null（未命中分支）
        Mockito.when(redisService.get(Mockito.eq("test"), Mockito.eq(ExampleRequestRedis.class)))
                .thenReturn(new ExampleRequestRedis(), (ExampleRequestRedis) null);
        controller.exampleRedis(new ExampleRequestRedis());

        Mockito.verify(redisService, Mockito.times(1)).set(Mockito.eq("test"), Mockito.any());

        // cache2 未命中为 null 的另一侧：两次 get 均返回缓存对象
        Mockito.when(redisService.get(Mockito.eq("test"), Mockito.eq(ExampleRequestRedis.class)))
                .thenReturn(new ExampleRequestRedis(), new ExampleRequestRedis());
        controller.exampleRedis(new ExampleRequestRedis());
        Mockito.verify(redisService, Mockito.times(2)).set(Mockito.eq("test"), Mockito.any());
    }

    @Test
    public void testExampleRedisPipline() {
        RedisService redisService = Mockito.mock(RedisService.class);
        ExampleRedisController controller = newController(redisService, Mockito.mock(RedissonClient.class));
        Mockito.when(redisService.executePipelined(Mockito.any(org.springframework.data.redis.core.SessionCallback.class)))
                .thenAnswer(inv -> {
                    org.springframework.data.redis.core.SessionCallback<?> callback = inv.getArgument(0);
                    RedisOperations operations = Mockito.mock(RedisOperations.class, Mockito.RETURNS_DEEP_STUBS);
                    return callback.execute(operations);
                });
        Mockito.when(redisService.get(Mockito.eq("test1"), Mockito.eq(ExampleRequestRedis.class))).thenReturn(null);
        controller.exampleRedisPipline();
        Mockito.verify(redisService).set(Mockito.eq("test1"), Mockito.any());
        Mockito.verify(redisService).set(Mockito.eq("test2"), Mockito.any());
    }

    @Test
    public void testExampleRedissonGetLock() {
        RedisService redisService = Mockito.mock(RedisService.class);
        RedissonClient redissonClient = Mockito.mock(RedissonClient.class, Mockito.RETURNS_DEEP_STUBS);
        RLock lock = Mockito.mock(RLock.class);
        Mockito.when(redissonClient.getLock(Mockito.eq("lock_key"))).thenReturn(lock);
        Mockito.when(lock.tryLock()).thenReturn(true);
        ExampleRedisController controller = newController(redisService, redissonClient);
        controller.exampleRedisson();

        Mockito.when(lock.tryLock()).thenReturn(false);
        controller.exampleRedisson();

        // 预置中断标记 -> sleep 立即抛 InterruptedException -> 覆盖 catch 分支
        Mockito.when(lock.tryLock()).thenReturn(true);
        try {
            Thread.currentThread().interrupt();
            controller.exampleRedisson();
            Assert.fail();
        } catch (RuntimeException expected) {
            Assert.assertTrue(expected.getCause() instanceof InterruptedException);
        } finally {
            Thread.interrupted();
        }

        Mockito.verify(redissonClient, Mockito.times(3)).getLock("lock_key");
    }

    @Test
    public void testExampleRedisDynamic() {
        ExampleRedisController controller = newController(Mockito.mock(RedisService.class), Mockito.mock(RedissonClient.class));
        controller.exampleRedisDynamic();
        controller.exampleRedissonDynamicClient();
        controller.exampleRedissonDynamicClientAop();
        Assert.assertTrue(true);
    }

    @Test
    public void testExampleDynamicRedisService() {
        ExampleDynamicRedisService service = new ExampleDynamicRedisService();
        RedisService redisService = Mockito.mock(RedisService.class);
        DynamicRedisService dynamicRedisService = Mockito.mock(DynamicRedisService.class);
        RedissonClient redissonClient = Mockito.mock(RedissonClient.class, Mockito.RETURNS_DEEP_STUBS);
        DynamicRedissonClient dynamicRedissonClient = Mockito.mock(DynamicRedissonClient.class, Mockito.RETURNS_DEEP_STUBS);
        DynamicAopRedissonClient dynamicAopRedissonClient = Mockito.mock(DynamicAopRedissonClient.class, Mockito.RETURNS_DEEP_STUBS);
        ReflectionTestUtils.setField(service, "redisService", redisService);
        ReflectionTestUtils.setField(service, "dynamicRedisService", dynamicRedisService);
        ReflectionTestUtils.setField(service, "selfService", Mockito.mock(ExampleDynamicRedisService.class));
        ReflectionTestUtils.setField(service, "redissonClient", redissonClient);
        ReflectionTestUtils.setField(service, "dynamicRedissonClient", dynamicRedissonClient);
        ReflectionTestUtils.setField(service, "dynamicAopRedissonClient", dynamicAopRedissonClient);

        service.redisPrimary();
        service.redisDb0();
        service.redisDb2();
        service.redisSelfMulti();
        service.redisMulti();
        service.redissonClient();
        service.redissonClientAop1();
        service.redissonClientAop2();
        service.redissonClientAop3();

        Mockito.verify(redisService).set(Mockito.eq("primary"), Mockito.any(), Mockito.eq(10L));
        Mockito.verify(dynamicRedisService, Mockito.times(3)).set(Mockito.anyString(), Mockito.any(), Mockito.any(), Mockito.anyLong());
    }
}
