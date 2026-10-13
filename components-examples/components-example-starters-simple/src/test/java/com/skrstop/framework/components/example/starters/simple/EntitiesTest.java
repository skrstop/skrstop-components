package com.skrstop.framework.components.example.starters.simple;

import com.skrstop.framework.components.example.starters.simple.entity.mongo.Example1Mongo;
import com.skrstop.framework.components.example.starters.simple.entity.mongo.Example2Mongo;
import com.skrstop.framework.components.example.starters.simple.entity.mongo.ExampleMongoChild;
import com.skrstop.framework.components.example.starters.simple.entity.msyql.Example1;
import com.skrstop.framework.components.example.starters.simple.entity.request.ExampleRequestParam;
import com.skrstop.framework.components.example.starters.simple.entity.request.ExampleRequestRedis;
import com.skrstop.framework.components.example.starters.simple.entity.response.ExamplePrivacyInfo;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * lombok 生成方法的真实调用覆盖
 */
public class EntitiesTest {

    @Test
    public void testExample1() {
        Example1 e = new Example1();
        e.setId(1L);
        e.setName("n");
        e.setAge(20);
        e.setBirth(LocalDateTime.now());
        e.setDie(true);
        e.setStatus(false);
        Assert.assertEquals(1L, (long) e.getId());
        Assert.assertEquals("n", e.getName());
        Assert.assertEquals(20, (int) e.getAge());
        Assert.assertNotNull(e.getBirth());
        Assert.assertTrue(e.getDie());
        Assert.assertFalse(e.getStatus());
        Assert.assertNotNull(e.toString());
        Assert.assertEquals(e, e);
        Assert.assertEquals(e.hashCode(), e.hashCode());
        Example1 e2 = Example1.builder().id(2L).name("m").age(1).birth(LocalDateTime.now()).die(false).status(true).build();
        Assert.assertNotNull(e2);
        Assert.assertNotEquals(e, e2);
    }

    @Test
    public void testMongoEntities() {
        ExampleMongoChild child = new ExampleMongoChild();
        child.setValStr("s");
        child.setValInt(1);
        child.setValLong(2L);
        child.setValBol(true);
        child.setValData(LocalDateTime.now());
        Assert.assertEquals("s", child.getValStr());
        Assert.assertNotNull(child.toString());
        Assert.assertEquals(child, child);

        Example1Mongo m1 = Example1Mongo.builder()
                .id("a").valStr("s").valInt(1).valLong(2L).valBol(true)
                .valData(LocalDateTime.now()).child(child).build();
        m1.setId("b");
        Assert.assertEquals("b", m1.getId());
        Assert.assertSame(child, m1.getChild());
        Assert.assertNotNull(m1.toString());
        Assert.assertEquals(m1, m1);

        Example2Mongo m2 = new Example2Mongo();
        m2.setId(1L);
        m2.setValStr("x");
        m2.setValInt(2);
        m2.setValLong(3L);
        m2.setValBol(false);
        m2.setValData(LocalDateTime.now());
        m2.setChild(child);
        m2.setVersion(14L);
        Assert.assertEquals(1L, (long) m2.getId());
        Assert.assertEquals(14L, (long) m2.getVersion());
        Assert.assertNotNull(m2.toString());
        Assert.assertEquals(m2, m2);
    }

    @Test
    public void testRequestEntities() {
        ExampleRequestParam p = new ExampleRequestParam();
        p.setValStr("s");
        p.setValInt(1);
        p.setValLong(2L);
        p.setValBool(true);
        p.setValDateTime(LocalDateTime.now());
        Assert.assertEquals("s", p.getValStr());
        Assert.assertNotNull(p.toString());
        Assert.assertEquals(p, p);
        Assert.assertNotNull(ExampleRequestParam.builder().valStr("a").build());

        ExampleRequestRedis r = new ExampleRequestRedis();
        r.setValStr("s");
        r.setValInt(1);
        r.setValLong(2L);
        r.setValBol(true);
        r.setValData(LocalDateTime.now());
        r.setValData2(LocalDate.now());
        r.setValData3(LocalTime.now());
        r.setValDecimal(new BigDecimal("1.1"));
        Assert.assertEquals("s", r.getValStr());
        Assert.assertNotNull(r.toString());
        Assert.assertEquals(r, r);
        Assert.assertNotNull(ExampleRequestRedis.builder().valStr("a").build());
    }

    @Test
    public void testPrivacyInfoEntity() {
        ExamplePrivacyInfo info = new ExamplePrivacyInfo();
        info.setValueDefault("d");
        info.setValueSetNull("s");
        info.setValueBankCard("b");
        info.setValueIdCard("i");
        info.setValuePhone("p");
        info.setValueEmail("e");
        info.setCustom("c");
        info.setOther1(1);
        info.setOther2(2L);
        info.setOther3(true);
        info.setOther4(LocalDateTime.now());
        Assert.assertEquals("d", info.getValueDefault());
        Assert.assertNotNull(info.toString());
        Assert.assertEquals(info, info);
        Assert.assertNotNull(ExamplePrivacyInfo.builder().valueDefault("d").build());
    }
}
