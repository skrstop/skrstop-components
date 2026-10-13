package com.skrstop.framework.components.starter.annotation.handle.paramAlias;

import com.skrstop.framework.components.starter.annotation.anno.paramAlias.RequestParamAlias;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.bind.ServletRequestParameterPropertyValues;

/**
 * @author 蒋时华
 */
public class AliasExtendedServletRequestDataBinderTest {

    public static class Target {
        @RequestParamAlias({"aliasName"})
        private String name;

        private String plain;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPlain() {
            return plain;
        }

        public void setPlain(String plain) {
            this.plain = plain;
        }
    }

    @Test
    public void testAddBindValuesWithAlias() {
        Target target = new Target();
        AliasExtendedServletRequestDataBinder binder =
                new AliasExtendedServletRequestDataBinder(target, "target");
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("aliasName", "alias-value");
        request.setParameter("plain", "plain-value");

        MutablePropertyValues mpvs = new ServletRequestParameterPropertyValues(request);
        binder.addBindValues(mpvs, request);
        binder.bind(mpvs);

        Assert.assertEquals("alias-value", target.getName());
        Assert.assertEquals("plain-value", target.getPlain());
    }

    @Test
    public void testAddBindValuesOriginalWins() {
        Target target = new Target();
        AliasExtendedServletRequestDataBinder binder =
                new AliasExtendedServletRequestDataBinder(target, "target");
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("name", "original");
        request.setParameter("aliasName", "alias");

        MutablePropertyValues mpvs = new ServletRequestParameterPropertyValues(request);
        // 原始字段存在 → 跳过别名
        binder.addBindValues(mpvs, request);
        binder.bind(new ServletRequestParameterPropertyValues(request));
        Assert.assertEquals("original", target.getName());
    }

    @Test
    public void testAddBindValuesEmptyMpvs() {
        Target target = new Target();
        AliasExtendedServletRequestDataBinder binder =
                new AliasExtendedServletRequestDataBinder(target, "target");
        MockHttpServletRequest request = new MockHttpServletRequest();
        MutablePropertyValues mpvs = new MutablePropertyValues();
        // 空 mpvs → 直接返回
        binder.addBindValues(mpvs, request);
        Assert.assertTrue(mpvs.isEmpty());
    }

    @Test
    public void testAddBindValuesNoAliasMatch() {
        Target target = new Target();
        AliasExtendedServletRequestDataBinder binder =
                new AliasExtendedServletRequestDataBinder(target, "target");
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("plain", "v");

        MutablePropertyValues mpvs = new ServletRequestParameterPropertyValues(request);
        // 别名不在参数中 → 不赋值
        binder.addBindValues(mpvs, request);
        binder.bind(new ServletRequestParameterPropertyValues(request));
        Assert.assertNull(target.getName());
        Assert.assertEquals("v", target.getPlain());
    }

}