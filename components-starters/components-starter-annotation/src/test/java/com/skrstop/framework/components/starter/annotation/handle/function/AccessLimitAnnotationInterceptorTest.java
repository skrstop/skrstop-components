package com.skrstop.framework.components.starter.annotation.handle.function;

import cn.hutool.extra.spring.SpringUtil;
import com.skrstop.framework.components.starter.annotation.anno.function.AccessLimit;
import com.skrstop.framework.components.starter.annotation.configuration.AnnotationProperties;
import com.skrstop.framework.components.starter.annotation.exception.aspect.AccessLimitException;
import com.skrstop.framework.components.starter.annotation.handle.function.accessLimit.AccessLimitRule;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.aop.framework.ReflectiveMethodInvocation;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.lang.reflect.Method;
import java.util.Collections;

public class AccessLimitAnnotationInterceptorTest {

    // JUnit 每个测试方法新建实例 → 每个测试独立的 ruleMap 缓存
    private AccessLimitAnnotationInterceptor interceptor;

    public static class OkRule implements AccessLimitRule {
        @Override
        public boolean canContinue(org.aopalliance.intercept.MethodInvocation invocation, double limitNum) {
            return true;
        }
    }

    public static class DenyRule implements AccessLimitRule {
        @Override
        public boolean canContinue(org.aopalliance.intercept.MethodInvocation invocation, double limitNum) {
            return false;
        }
    }

    public static class NotRule {
    }

    public static class Methods {
        public String plain() {
            return "plain";
        }

        @AccessLimit(beanName = "okRule")
        public String byName() {
            return "byName";
        }

        @AccessLimit(beanName = "missingBean")
        public String byNameMissing() {
            return "x";
        }

        @AccessLimit(beanName = "notRuleBean")
        public String byNameNotRule() {
            return "x";
        }

        @AccessLimit(beanClass = Void.class)
        public String byVoidClass() {
            return "x";
        }

        @AccessLimit(beanClass = OkRule.class)
        public String byClass() {
            return "byClass";
        }

        @AccessLimit(beanClass = NotRule.class)
        public String byClassNotRule() {
            return "x";
        }

        @AccessLimit(beanClass = MissingClass.class)
        public String byClassMissing() {
            return "x";
        }

        @AccessLimit(beanClass = CommonRule.class)
        public String byClassDuplicate() {
            return "x";
        }

        @AccessLimit(beanClass = UnregisteredRule.class)
        public String byClassUnregistered() {
            return "x";
        }

        @AccessLimit(defaultRule = OkRule.class)
        public String byDefaultRule() {
            return "byDefaultRule";
        }

        @AccessLimit(defaultRule = DenyRule.class, message = "deny")
        public String denyWithMessage() {
            return "x";
        }

        @AccessLimit(defaultRule = DenyRule.class, message = "")
        public String denyWithoutMessage() {
            return "x";
        }

        static class MissingClass {
        }

        public interface UnregisteredRule extends AccessLimitRule {
        }

        public interface CommonRule extends AccessLimitRule {
        }

        public static class Two implements CommonRule {
            @Override
            public boolean canContinue(org.aopalliance.intercept.MethodInvocation invocation, double limitNum) {
                return true;
            }
        }

        public static class Two2 implements CommonRule {
            @Override
            public boolean canContinue(org.aopalliance.intercept.MethodInvocation invocation, double limitNum) {
                return true;
            }
        }
    }

    private final AnnotationProperties properties = new AnnotationProperties();

    @After
    public void tearDown() {
        new SpringUtil().setApplicationContext(null);
    }

    private Object invoke(String methodName) throws Throwable {
        Method method = Methods.class.getMethod(methodName);
        if (interceptor == null) {
            interceptor = new AccessLimitAnnotationInterceptor(properties);
        }
        return interceptor.invoke(new ReflectiveMethodInvocation(
                new Methods(), new Methods(), method, new Object[0], null, Collections.emptyList()) {
            @Override
            public Object proceed() {
                return "proceeded";
            }
        });
    }

    @Test
    public void testPlainProceeds() throws Throwable {
        Assert.assertEquals("proceeded", invoke("plain"));
    }

    @Test
    public void testByName() throws Throwable {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.getBeanFactory().registerSingleton("okRule", new OkRule());
        ctx.getBeanFactory().registerSingleton("notRuleBean", new NotRule());
        ctx.refresh();
        SpringUtil springUtil = new SpringUtil();
        springUtil.setApplicationContext(ctx);
        Assert.assertEquals("proceeded", invoke("byName"));
        // 第二次调用 → ruleMap 缓存命中
        Assert.assertEquals("proceeded", invoke("byName"));
        try {
            invoke("byNameMissing");
            Assert.fail();
        } catch (RuntimeException ignored) {
        }
        try {
            invoke("byNameNotRule");
            Assert.fail();
        } catch (RuntimeException ignored) {
        }
        ctx.close();
    }

    @Test
    public void testByClass() throws Throwable {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.getBeanFactory().registerSingleton("okRule", new OkRule());
        ctx.refresh();
        SpringUtil springUtil = new SpringUtil();
        springUtil.setApplicationContext(ctx);
        Assert.assertEquals("proceeded", invoke("byClass"));
        // 第二次调用 → ruleMap 缓存命中
        Assert.assertEquals("proceeded", invoke("byClass"));
        try {
            invoke("byClassNotRule");
            Assert.fail();
        } catch (RuntimeException ignored) {
        }
        ctx.close();
    }

    @Test
    public void testByClassMissing() throws Throwable {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.refresh();
        SpringUtil springUtil = new SpringUtil();
        springUtil.setApplicationContext(ctx);
        try {
            invoke("byClassMissing");
            Assert.fail();
        } catch (RuntimeException ignored) {
        }
        ctx.close();
    }

    @Test
    public void testByClassDuplicate() throws Throwable {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.getBeanFactory().registerSingleton("two1", new Methods.Two());
        ctx.getBeanFactory().registerSingleton("two2", new Methods.Two2());
        ctx.refresh();
        SpringUtil springUtil = new SpringUtil();
        springUtil.setApplicationContext(ctx);
        try {
            invoke("byClassDuplicate");
            Assert.fail();
        } catch (RuntimeException ignored) {
        }
        ctx.close();
    }

    @Test
    public void testDefaultRule() throws Throwable {
        Assert.assertEquals("proceeded", invoke("byDefaultRule"));
        // 第二次调用 → ruleMap 缓存命中
        Assert.assertEquals("proceeded", invoke("byDefaultRule"));
    }

    @Test
    public void testVoidBeanClassFallsToDefaultRule() throws Throwable {
        // beanClass=Void.class → 条件不满足 → 走 defaultRule
        Assert.assertEquals("proceeded", invoke("byVoidClass"));
    }

    @Test
    public void testByClassUnregistered() throws Throwable {
        // beanClass 是合法规则接口但容器中无对应 bean → catch(Exception)
        try {
            invoke("byClassUnregistered");
            Assert.fail();
        } catch (RuntimeException ignored) {
        }
    }

    @Test
    public void testDeny() throws Throwable {
        try {
            invoke("denyWithMessage");
            Assert.fail();
        } catch (AccessLimitException ignored) {
        }
        try {
            invoke("denyWithoutMessage");
            Assert.fail();
        } catch (AccessLimitException ignored) {
        }
    }
}
