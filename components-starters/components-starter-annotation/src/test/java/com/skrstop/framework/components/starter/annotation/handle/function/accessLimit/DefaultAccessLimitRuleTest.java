package com.skrstop.framework.components.starter.annotation.handle.function.accessLimit;

import org.aopalliance.intercept.MethodInvocation;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class DefaultAccessLimitRuleTest {

    private final DefaultAccessLimitRule rule = new DefaultAccessLimitRule();

    private MethodInvocation invocation(String methodName) throws NoSuchMethodException {
        MethodInvocation invocation = Mockito.mock(MethodInvocation.class);
        Mockito.when(invocation.getMethod()).thenReturn(DefaultAccessLimitRuleTest.class.getDeclaredMethod(methodName));
        return invocation;
    }

    @Test
    public void testAllow() throws Exception {
        Assert.assertTrue(rule.canContinue(invocation("testAllow"), 1000000D));
    }

    @Test
    public void testDeny() throws Exception {
        MethodInvocation invocation = invocation("testDeny");
        // 第一次令牌可立即获取，极小速率下第二次令牌不足
        Assert.assertTrue(rule.canContinue(invocation, 1e-9D));
        Assert.assertFalse(rule.canContinue(invocation, 1e-9D));
    }

    @Test
    public void testRateChange() throws Exception {
        MethodInvocation invocation = invocation("testRateChange");
        Assert.assertTrue(rule.canContinue(invocation, 1000000D));
        // rate 变化后重新创建 limiter
        Assert.assertTrue(rule.canContinue(invocation, 1000000D - 1));
    }
}
