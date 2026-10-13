package com.skrstop.framework.components.util.db.redis.jedis;

import org.junit.Assert;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author 蒋时华
 */
public class JedisUtilTest {

    @Test
    public void testCreate() {
        Assert.assertNotNull(JedisUtil.create());
        Assert.assertNotNull(JedisUtil.create("group"));
        Assert.assertNotNull(JedisUtil.create(new cn.hutool.setting.Setting(), "group"));
        Assert.assertNotNull(JedisUtil.create(new cn.hutool.setting.Setting()));
    }

    @Test
    public void testInitWithDefaultConfig() throws Exception {
        // 使用测试资源里的 config/redis.setting
        JedisUtil jedisUtil = new JedisUtil();
        Assert.assertNotNull(jedisUtil);
        jedisUtil.close();

        // 带分组的默认配置
        JedisUtil grouped = new JedisUtil("group");
        Assert.assertNotNull(grouped);
        grouped.close();

        // 显式 setting + 分组
        cn.hutool.setting.Setting setting = new cn.hutool.setting.Setting();
        setting.put("host", "localhost");
        JedisUtil explicit = new JedisUtil(setting, "group");
        Assert.assertNotNull(explicit);
        explicit.close();
    }

    @Test
    public void testJedisConfigConst() {
        Assert.assertEquals("host", JedisConfigConst.HOST);
    }

    @Test
    public void testGetSetDelWithMockPool() throws Exception {
        JedisUtil jedisUtil = new JedisUtil();

        Jedis jedis = org.mockito.Mockito.mock(Jedis.class);
        org.mockito.Mockito.when(jedis.get("k")).thenReturn("v");
        org.mockito.Mockito.when(jedis.set("k", "v2")).thenReturn("OK");
        org.mockito.Mockito.when(jedis.del(new String[]{"k"})).thenReturn(1L);

        JedisPool pool = org.mockito.Mockito.mock(JedisPool.class);
        org.mockito.Mockito.when(pool.getResource()).thenReturn(jedis);

        java.lang.reflect.Field field = JedisUtil.class.getDeclaredField("pool");
        field.setAccessible(true);
        field.set(jedisUtil, pool);

        Assert.assertEquals("v", jedisUtil.getStr("k"));
        Assert.assertEquals("OK", jedisUtil.setStr("k", "v2"));
        Assert.assertEquals(Long.valueOf(1L), jedisUtil.del("k"));
        jedisUtil.close();
    }

}
