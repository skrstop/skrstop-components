package com.skrstop.framework.components.starter.annotation.handle.function;

import cn.hutool.extra.spring.SpringUtil;
import com.skrstop.framework.components.starter.annotation.anno.function.ServiceLock;
import com.skrstop.framework.components.starter.annotation.configuration.AnnotationProperties;
import com.skrstop.framework.components.starter.annotation.handle.function.serviceLock.ServiceLockRule;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.aop.framework.ReflectiveMethodInvocation;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.lang.reflect.Method;
import java.util.Collections;

public class ServiceLockAnnotationInterceptorTest {

    // JUnit 每个测试方法新建实例 → 每个测试独立的 ruleMap 缓存
    private ServiceLockAnnotationInterceptor interceptor;

    // 源码校验的是 instanceof AccessLimitRule，需要同时实现两个接口
    public static class OkRule implements ServiceLockRule,
            com.skrstop.framework.components.starter.annotation.handle.function.accessLimit.AccessLimitRule {
        @Override
        public Object handle(org.aopalliance.intercept.MethodInvocation invocation, ServiceLock serviceLock) {
            return "locked";
        }

        @Override
        public boolean canContinue(org.aopalliance.intercept.MethodInvocation invocation, double limitNum) {
            return true;
        }
    }

    public static class NotRule {
    }

    public static class OnlyServiceLockRule implements ServiceLockRule {
        @Override
        public Object handle(org.aopalliance.intercept.MethodInvocation invocation, ServiceLock serviceLock) {
            return "x";
        }
    }

    public static class Methods {
        public String plain() {
            return "plain";
        }

        @ServiceLock(beanName = "okRule")
        public String byName() {
            return "x";
        }

        @ServiceLock(beanName = "missingBean")
        public String byNameMissing() {
            return "x";
        }

        @ServiceLock(beanName = "serviceLockOnly")
        public String byNameWrongRule() {
            return "x";
        }

        @ServiceLock(beanClass = Void.class)
        public String byVoidClass() {
            return "x";
        }

        @ServiceLock(beanClass = OkRule.class)
        public String byClass() {
            return "x";
        }

        @ServiceLock(beanClass = NotRule.class)
        public String byClassNotRule() {
            return "x";
        }

        @ServiceLock(beanClass = MissingClass.class)
        public String byClassMissing() {
            return "x";
        }

        @ServiceLock(beanClass = CommonRule.class)
        public String byClassDuplicate() {
            return "x";
        }

        @ServiceLock(beanClass = UnregisteredRule.class)
        public String byClassUnregistered() {
            return "x";
        }

        @ServiceLock
        public String byDefaultRule() {
            return "x";
        }

        static class MissingClass {
        }

        public interface CommonRule extends ServiceLockRule,
                com.skrstop.framework.components.starter.annotation.handle.function.accessLimit.AccessLimitRule {
        }

        public interface UnregisteredRule extends ServiceLockRule,
                com.skrstop.framework.components.starter.annotation.handle.function.accessLimit.AccessLimitRule {
        }

        public static class Two implements CommonRule {
            @Override
            public Object handle(org.aopalliance.intercept.MethodInvocation invocation, ServiceLock serviceLock) {
                return "x";
            }

            @Override
            public boolean canContinue(org.aopalliance.intercept.MethodInvocation invocation, double limitNum) {
                return true;
            }
        }

        public static class Two2 implements CommonRule {
            @Override
            public Object handle(org.aopalliance.intercept.MethodInvocation invocation, ServiceLock serviceLock) {
                return "x";
            }

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
            interceptor = new ServiceLockAnnotationInterceptor(properties);
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
        // 只实现 ServiceLockRule，不实现 AccessLimitRule → 走 instanceof 校验失败分支
        ctx.getBeanFactory().registerSingleton("serviceLockOnly", new OnlyServiceLockRule());
        ctx.refresh();
        new SpringUtil().setApplicationContext(ctx);
        Assert.assertEquals("locked", invoke("byName"));
        // 第二次调用 → ruleMap 缓存命中
        Assert.assertEquals("locked", invoke("byName"));
        try {
            invoke("byNameMissing");
            Assert.fail();
        } catch (RuntimeException ignored) {
        }
        // bean 存在但未实现 AccessLimitRule 接口
        try {
            invoke("byNameWrongRule");
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
        new SpringUtil().setApplicationContext(ctx);
        Assert.assertEquals("locked", invoke("byClass"));
        // 第二次调用 → ruleMap 缓存命中
        Assert.assertEquals("locked", invoke("byClass"));
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
        new SpringUtil().setApplicationContext(ctx);
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
        new SpringUtil().setApplicationContext(ctx);
        try {
            invoke("byClassDuplicate");
            Assert.fail();
        } catch (RuntimeException ignored) {
        }
        ctx.close();
    }

    @Test
    public void testDefaultRule() throws Throwable {
        // DefaultServiceLockRule → tryLock 成功 → invocation.proceed()
        Assert.assertEquals("proceeded", invoke("byDefaultRule"));
        // 第二次调用 → ruleMap 缓存命中
        Assert.assertEquals("proceeded", invoke("byDefaultRule"));
    }

    @Test
    public void testVoidBeanClassFallsToDefaultRule() throws Throwable {
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
}
