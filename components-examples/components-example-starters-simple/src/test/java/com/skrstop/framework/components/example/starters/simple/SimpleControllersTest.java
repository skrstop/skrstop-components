package com.skrstop.framework.components.example.starters.simple;

import com.skrstop.framework.components.core.common.response.DefaultResult;
import com.skrstop.framework.components.core.common.response.Result;
import com.skrstop.framework.components.core.exception.SkrstopBusinessException;
import com.skrstop.framework.components.core.exception.SkrstopDataRuntimeException;
import com.skrstop.framework.components.core.exception.SkrstopRuntimeException;
import com.skrstop.framework.components.example.starters.simple.controller.ExampleExceptionController;
import com.skrstop.framework.components.example.starters.simple.controller.ExampleFeignController;
import com.skrstop.framework.components.example.starters.simple.controller.ExampleReturnController;
import com.skrstop.framework.components.example.starters.simple.controller.ExampleValidController;
import com.skrstop.framework.components.example.starters.simple.controller.feign.TestFeign;
import com.skrstop.framework.components.example.starters.simple.entity.request.ExampleRequestParam;
import com.skrstop.framework.components.example.starters.simple.service.Example1MysqlService;
import com.skrstop.framework.components.starter.id.service.IdService;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleControllersTest {

    // ---------------- ExampleValidController ----------------

    @Test
    public void testValidController() {
        ExampleValidController controller = new ExampleValidController();
        Assert.assertNotNull(controller.exampleValid("val"));

        ExampleRequestParam valid = ExampleRequestParam.builder()
                .valStr("s").valInt(1).valLong(2L).valBool(true).build();
        Assert.assertNotNull(controller.exampleValid1(valid));
        // 缺省参数 -> 返回错误信息列表
        IResultCheck.check(controller.exampleValid1(new ExampleRequestParam()));

        Assert.assertNotNull(controller.exampleValidGet(valid));
        Assert.assertNotNull(controller.exampleValidPost(valid));
    }

    @Test
    public void testExceptionController() {
        ExampleExceptionController controller = new ExampleExceptionController();
        try {
            controller.exampleException(null);
            Assert.fail();
        } catch (RuntimeException expected) {
            Assert.assertEquals("参数为空", expected.getMessage());
        }
        assertThrows(controller, 1, SkrstopRuntimeException.class);
        assertThrows(controller, 2, SkrstopRuntimeException.class);
        try {
            controller.exampleException(3);
            Assert.fail();
        } catch (SkrstopDataRuntimeException expected) {
            List<Integer> data = (List<Integer>) expected.getThrowableData().getData();
            Assert.assertEquals(java.util.Arrays.asList(1, 2, 3, 4, 5), data);
        }
        assertThrows(controller, 4, SkrstopBusinessException.class);
        try {
            controller.exampleException(5);
            Assert.fail();
        } catch (RuntimeException expected) {
            Assert.assertEquals("未知异常", expected.getMessage());
        }
    }

    private void assertThrows(ExampleExceptionController controller, int arg, Class<?> expected) {
        try {
            controller.exampleException(arg);
            Assert.fail();
        } catch (Exception e) {
            Assert.assertTrue(expected.isInstance(e));
        }
    }

    @Test
    public void testReturnController() {
        ExampleReturnController controller = new ExampleReturnController();
        ReflectionTestUtils.setField(controller, "example1MysqlService", Mockito.mock(Example1MysqlService.class));
        ReflectionTestUtils.setField(controller, "idService", Mockito.mock(IdService.class));

        Object r1 = controller.exampleReturn1();
        Assert.assertTrue(r1 instanceof Map);
        Object r2 = controller.exampleReturn2();
        Assert.assertTrue(r2 instanceof Map);
        Assert.assertNotNull(controller.exampleReturnMap1());
        Assert.assertNotNull(controller.exampleReturnMap2());
        Assert.assertNotNull(controller.exampleReturnPage());
        Assert.assertNotNull(controller.exampleReturnPage2());
    }

    @Test
    public void testFeignController() {
        ExampleFeignController controller = new ExampleFeignController();
        TestFeign testFeign = Mockito.mock(TestFeign.class);
        Mockito.when(testFeign.testFeignRemote2()).thenReturn(Result.Builder.success(new HashMap<>()));
        ReflectionTestUtils.setField(controller, "testFeign", testFeign);

        Map<String, String> remote = controller.testFeignRemote();
        Assert.assertEquals("aaabbb", remote.get("test"));
        Assert.assertNotNull(controller.testFeign());
    }

    interface IResultCheck {
        static void check(Object result) {
            Assert.assertNotNull(result);
        }
    }

    private static Object check(DefaultResult r) {
        return r;
    }
}
