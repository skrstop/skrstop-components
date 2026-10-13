package com.skrstop.framework.components.util.event.core;

import org.junit.Assert;
import org.junit.Test;

/**
 * 回调接口默认方法测试
 *
 * @author 蒋时华
 */
public class CallbackInterfacesTest {

    @Test
    public void testCallbackDefaults() {
        Callback callback = new Callback() {
        };
        callback.onUnValid();
        callback.onBefore();
        callback.onAfter();
        callback.onSuccess();
        callback.onFailure(new RuntimeException("e"));
        callback.onFailure();
        callback.onExecute();
        callback.onTimeout();
        Assert.assertTrue(callback.onContinue());
        callback.onLogOutput("log", true);
    }

    @Test
    public void testCallbackResultDefaults() {
        CallbackResult<String> callback = new CallbackResult<String>() {
        };
        callback.onUnValid("a");
        callback.onBefore("a");
        callback.onAfter("a");
        callback.onSuccess("a");
        callback.onFailure(new RuntimeException("e"), "a");
        callback.onFailure("a");
        callback.onExecute("a");
        callback.onTimeout("a");
        Assert.assertTrue(callback.onContinue("a"));
        callback.onLogOutput("log", false);
    }

    @Test
    public void testCallbackResultReturnDefaults() {
        CallbackResultReturn<String, Integer> callback = new CallbackResultReturn<String, Integer>() {
            @Override
            public Integer onExecute(String obj) {
                return 1;
            }
        };
        callback.onUnValid("a");
        callback.onBefore("a");
        callback.onAfter("a");
        callback.onSuccess("a");
        callback.onFailure(new RuntimeException("e"), "a");
        callback.onFailure("a");
        callback.onTimeout("a");
        Assert.assertTrue(callback.onContinue("a"));
        Assert.assertEquals(Integer.valueOf(1), callback.onExecute("a"));

        // 无覆写实现 → 覆盖默认 onExecute / onTimeout / onLogOutput
        CallbackResultReturn<String, Integer> defaults = new CallbackResultReturn<String, Integer>() {
        };
        Assert.assertNull(defaults.onExecute("a"));
        Assert.assertNull(defaults.onTimeout("a"));
        Assert.assertNull(defaults.onUnValid("a"));
        Assert.assertNull(defaults.onBefore("a"));
        Assert.assertNull(defaults.onAfter("a"));
        Assert.assertNull(defaults.onSuccess("a"));
        Assert.assertNull(defaults.onFailure("a"));
        Assert.assertNull(defaults.onFailure(new RuntimeException("e"), "a"));
        Assert.assertTrue(defaults.onContinue("a"));
        defaults.onLogOutput("log", true);
    }

    @Test
    public void testEventResultDefault() {
        EventResult<String, Integer> eventResult = new EventResult<String, Integer>() {
            @Override
            public Integer getEventData(String request) {
                return 1;
            }

            @Override
            public String getEventName() {
                return "name";
            }
        };
        Assert.assertNull(eventResult.getEventData());
        Assert.assertEquals(Integer.valueOf(1), eventResult.getEventData("req"));
        Assert.assertEquals("name", eventResult.getEventName());
    }

    @Test
    public void testSimpleInterfaces() {
        Executable executable = () -> {
        };
        executable.execute();
        ExecuteResultable<String> resultable = obj -> {
        };
        resultable.execute("a");
        ExecuteResultReturnable<String, Integer> returnable = obj -> 1;
        Assert.assertEquals(Integer.valueOf(1), returnable.execute("a"));
    }

}
