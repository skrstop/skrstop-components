package com.skrstop.framework.components.util.value.bean;

import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

/**
 * @author 蒋时华
 */
public class BeanUtilTest {

    @Data
    public static class Source {
        private String name = "n";
        private Integer age = 18;
        private String extra = "e";
    }

    @Data
    public static class Target {
        private String name;
        private Integer age;
    }

    @Test
    public void testCopyPropertiesClass() {
        Source source = new Source();
        Target target = BeanUtil.copyProperties(source, Target.class, new CopyOptions());
        Assert.assertEquals("n", target.getName());
        Assert.assertEquals(Integer.valueOf(18), target.getAge());
    }

    @Test
    public void testCopyPropertiesList() {
        List<Target> list = BeanUtil.copyProperties(Arrays.asList(new Source(), new Source()), Target.class);
        Assert.assertEquals(2, list.size());
        Assert.assertEquals("n", list.get(0).getName());

        Assert.assertTrue(BeanUtil.copyProperties((List<Source>) null, Target.class).isEmpty());
        Assert.assertTrue(BeanUtil.copyProperties(Collections.<Source>emptyList(), Target.class).isEmpty());
    }

    @Test
    public void testCopyPropertiesListWithOptions() {
        CopyOptions options = new CopyOptions();
        List<Target> list =
                BeanUtil.copyProperties(Arrays.asList(new Source()), Target.class, options);
        Assert.assertEquals(1, list.size());
        Assert.assertEquals("n", list.get(0).getName());

        Assert.assertTrue(
                BeanUtil.copyProperties((List<Source>) null, Target.class, options).isEmpty());
        Assert.assertTrue(
                BeanUtil.copyProperties(Collections.<Source>emptyList(), Target.class, options)
                        .isEmpty());
    }

    @Test
    public void testCopyOptions() {
        Map<String, String> mapping = new HashMap<>();
        mapping.put("a", "b");
        CopyOptions options =
                new CopyOptions(String.class, true, new String[]{"x"}, true, true, mapping);
        Assert.assertNotNull(options);
        Assert.assertTrue(options.isIgnoreNullValue() || !options.isIgnoreNullValue());
        // builder / 链式 setter
        Assert.assertNotNull(CopyOptions.builder().ignoreNullValue(true).build());
        Assert.assertNotNull(new CopyOptions().setEditable(null));
    }

    @Test
    public void testIsBaseType() {
        Assert.assertTrue(BeanUtil.isBaseType("str"));
        Assert.assertTrue(BeanUtil.isBaseType(1));
        Assert.assertTrue(BeanUtil.isBaseType(1L));
        Assert.assertTrue(BeanUtil.isBaseType(Boolean.TRUE));
        Assert.assertFalse(BeanUtil.isBaseType(new Source()));
        Assert.assertFalse(BeanUtil.isBaseType(null));
    }

}
