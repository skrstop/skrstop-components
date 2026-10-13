package com.skrstop.framework.components.util.system.process;

import com.skrstop.framework.components.util.event.core.Callback;
import com.skrstop.framework.components.util.event.core.CallbackResult;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 蒋时华
 */
public class ProcessUtilTest {

    private static final List<String> ECHO = Arrays.asList("/bin/sh", "-c", "echo hello");

    @Test
    public void testSetters() {
        ProcessUtil.setLogInfo();
        ProcessUtil.setLogConsole();
        ProcessUtil.setErrorMessageKey(new ArrayList<>(Collections.singletonList("error")));
        ProcessUtil.setErrorMessageKey(null);
    }

    @Test
    public void testExecuteSuccess() {
        AtomicInteger success = new AtomicInteger();
        boolean result = ProcessUtil.execute(ECHO, new Callback() {
            @Override
            public void onSuccess() {
                success.incrementAndGet();
            }
        }, 100L, 5000L);
        Assert.assertTrue(result);
        Assert.assertEquals(1, success.get());
    }

    @Test
    public void testExecuteUnValid() {
        AtomicInteger unValid = new AtomicInteger();
        boolean result = ProcessUtil.execute(Collections.emptyList(), new Callback() {
            @Override
            public void onUnValid() {
                unValid.incrementAndGet();
            }
        }, 100L, 1000L);
        Assert.assertFalse(result);
        Assert.assertEquals(1, unValid.get());
    }

    @Test
    public void testExecuteFailure() {
        List<String> failCmd = Arrays.asList("/bin/sh", "-c", "exit 1");
        AtomicInteger failure = new AtomicInteger();
        AtomicInteger failureWithThrowable = new AtomicInteger();
        boolean result = ProcessUtil.execute(failCmd, new Callback() {
            @Override
            public void onFailure() {
                failure.incrementAndGet();
            }

            @Override
            public void onFailure(Throwable e) {
                failureWithThrowable.incrementAndGet();
            }
        }, 100L, 5000L);
        Assert.assertFalse(result);
        Assert.assertTrue(failure.get() + failureWithThrowable.get() >= 1);
    }

    @Test
    public void testExecuteTimeout() {
        List<String> sleepCmd = Arrays.asList("/bin/sh", "-c", "sleep 1");
        AtomicInteger timeout = new AtomicInteger();
        boolean result = ProcessUtil.execute(sleepCmd, new Callback() {
            @Override
            public void onTimeout() {
                timeout.incrementAndGet();
            }
        }, 50L, 100L);
        Assert.assertFalse(result);
        Assert.assertEquals(1, timeout.get());
    }

    @Test
    public void testExecuteKeyed() {
        AtomicInteger success = new AtomicInteger();
        AtomicInteger onExecute = new AtomicInteger();
        boolean result = ProcessUtil.execute("key1", ECHO, new CallbackResult<ProcessUtil.ProcessResultText>() {
            @Override
            public void onSuccess(ProcessUtil.ProcessResultText obj) {
                Assert.assertEquals("key1", obj.getKey());
                success.incrementAndGet();
            }

            @Override
            public void onExecute(ProcessUtil.ProcessResultText obj) {
                onExecute.incrementAndGet();
            }
        }, 100L, 5000L);
        Assert.assertTrue(result);
        Assert.assertEquals(1, success.get());
    }

    @Test
    public void testExecuteKeyedUnValid() {
        AtomicInteger unValid = new AtomicInteger();
        boolean result = ProcessUtil.execute("key1", Collections.emptyList(), new CallbackResult<ProcessUtil.ProcessResultText>() {
            @Override
            public void onUnValid(ProcessUtil.ProcessResultText obj) {
                unValid.incrementAndGet();
            }
        }, 100L, 1000L);
        Assert.assertFalse(result);
        Assert.assertEquals(1, unValid.get());
    }

    @Test
    public void testExecuteKeyedTimeout() {
        List<String> sleepCmd = Arrays.asList("/bin/sh", "-c", "sleep 1");
        AtomicInteger timeout = new AtomicInteger();
        boolean result = ProcessUtil.execute("key1", sleepCmd, new CallbackResult<ProcessUtil.ProcessResultText>() {
            @Override
            public void onTimeout(ProcessUtil.ProcessResultText obj) {
                timeout.incrementAndGet();
            }
        }, 50L, 100L);
        Assert.assertFalse(result);
        Assert.assertEquals(1, timeout.get());
    }

    @Test
    public void testExecuteResultSuccess() {
        StringBuilder resultStr = new StringBuilder();
        boolean result = ProcessUtil.executeResult(ECHO, new CallbackResult<String>() {
            @Override
            public void onSuccess(String obj) {
                resultStr.append(obj);
            }
        }, 100L, 5000L);
        Assert.assertTrue(result);
        Assert.assertTrue(resultStr.toString().contains("hello"));
    }

    @Test
    public void testExecuteResultUnValid() {
        AtomicInteger unValid = new AtomicInteger();
        boolean result = ProcessUtil.executeResult(Collections.emptyList(), new CallbackResult<String>() {
            @Override
            public void onUnValid(String obj) {
                unValid.incrementAndGet();
            }
        }, 100L, 1000L);
        Assert.assertFalse(result);
        Assert.assertEquals(1, unValid.get());
    }

    @Test
    public void testExecuteResultFailure() {
        List<String> failCmd = Arrays.asList("/bin/sh", "-c", "exit 1");
        AtomicInteger failure = new AtomicInteger();
        boolean result = ProcessUtil.executeResult(failCmd, new CallbackResult<String>() {
            @Override
            public void onFailure(String obj) {
                failure.incrementAndGet();
            }

            @Override
            public void onFailure(Throwable e, String obj) {
                failure.incrementAndGet();
            }
        }, 100L, 5000L);
        Assert.assertFalse(result);
        Assert.assertTrue(failure.get() >= 1);
    }

    @Test
    public void testExecuteResultKeyed() {
        StringBuilder keyCheck = new StringBuilder();
        boolean result = ProcessUtil.executeResult("k", ECHO, new CallbackResult<ProcessUtil.ProcessResultText>() {
            @Override
            public void onSuccess(ProcessUtil.ProcessResultText obj) {
                keyCheck.append(obj.getKey());
            }
        }, 100L, 5000L);
        Assert.assertTrue(result);
        Assert.assertEquals("k", keyCheck.toString());
    }

    @Test
    public void testExecuteResultKeyedTimeout() {
        List<String> sleepCmd = Arrays.asList("/bin/sh", "-c", "sleep 1");
        AtomicInteger timeout = new AtomicInteger();
        boolean result = ProcessUtil.executeResult("k", sleepCmd, new CallbackResult<ProcessUtil.ProcessResultText>() {
            @Override
            public void onTimeout(ProcessUtil.ProcessResultText obj) {
                timeout.incrementAndGet();
            }
        }, 50L, 100L);
        Assert.assertFalse(result);
        Assert.assertEquals(1, timeout.get());
    }

    @Test
    public void testExecuteNullCallback() {
        Assert.assertTrue(ProcessUtil.execute(ECHO, null, 100L, 5000L));
        Assert.assertFalse(ProcessUtil.execute(Collections.emptyList(), null, 100L, 1000L));
        Assert.assertFalse(ProcessUtil.execute(Arrays.asList("/bin/sh", "-c", "exit 1"), null, 100L, 5000L));
    }

    @Test
    public void testExecuteNullCallbackTime() {
        // callbackTime 为 null、timeout 存在 → callbackTime = timeout
        Assert.assertTrue(ProcessUtil.execute(ECHO, null, null, 5000L));
        // 都为 null → callbackTime = MAX_VALUE
        Assert.assertTrue(ProcessUtil.execute(ECHO, null, null, null));
    }

    @Test
    public void testExecuteOnContinueFalse() {
        List<String> sleepCmd = Arrays.asList("/bin/sh", "-c", "sleep 1");
        boolean result = ProcessUtil.execute(sleepCmd, new Callback() {
            private int count = 0;

            @Override
            public boolean onContinue() {
                count++;
                return count < 1;
            }
        }, 50L, 5000L);
        Assert.assertFalse(result);
    }

    @Test
    public void testExecuteKeyedNullCallback() {
        Assert.assertTrue(ProcessUtil.execute("k", ECHO, null, 100L, 5000L));
        Assert.assertFalse(ProcessUtil.execute("k", Collections.emptyList(), null, 100L, 1000L));
    }

    @Test
    public void testExecuteResultNullCallback() {
        Assert.assertTrue(ProcessUtil.executeResult(ECHO, null, 100L, 5000L));
        Assert.assertFalse(ProcessUtil.executeResult(Collections.emptyList(), null, 100L, 1000L));
    }

    @Test
    public void testExecuteResultKeyedNullCallback() {
        Assert.assertTrue(ProcessUtil.executeResult("k", ECHO, null, 100L, 5000L));
    }

    @Test
    public void testExecuteResultNullCallbackTime() {
        Assert.assertTrue(ProcessUtil.executeResult(ECHO, null, null, 5000L));
        Assert.assertTrue(ProcessUtil.executeResult(ECHO, null, null, null));
    }

    @Test
    public void testExecuteNegativeCallbackTime() throws Exception {
        // 中断轮询线程 → future.get 抛出 InterruptedException → onFailure(e)
        AtomicInteger failureWithThrowable = new AtomicInteger();
        List<String> sleepCmd = Arrays.asList("/bin/sh", "-c", "sleep 1");
        final boolean[] result = {true};
        Thread t = new Thread(() -> result[0] = ProcessUtil.execute(sleepCmd, new Callback() {
            @Override
            public void onFailure(Throwable e) {
                failureWithThrowable.incrementAndGet();
            }
        }, 50L, 5000L));
        t.start();
        Thread.sleep(200);
        t.interrupt();
        t.join(5000);
        Assert.assertFalse(result[0]);
        Assert.assertEquals(1, failureWithThrowable.get());
    }

    @Test
    public void testExecuteResultNegativeCallbackTime() throws Exception {
        AtomicInteger failureWithThrowable = new AtomicInteger();
        List<String> sleepCmd = Arrays.asList("/bin/sh", "-c", "sleep 1");
        final boolean[] result = {true};
        Thread t = new Thread(() -> result[0] = ProcessUtil.executeResult(sleepCmd, new CallbackResult<String>() {
            @Override
            public void onFailure(Throwable e, String obj) {
                failureWithThrowable.incrementAndGet();
            }
        }, 50L, 5000L));
        t.start();
        Thread.sleep(200);
        t.interrupt();
        t.join(5000);
        Assert.assertFalse(result[0]);
        Assert.assertEquals(1, failureWithThrowable.get());
    }

    @Test
    public void testExecuteKeyedFailure() {
        List<String> failCmd = Arrays.asList("/bin/sh", "-c", "exit 1");
        AtomicInteger failureWithThrowable = new AtomicInteger();
        AtomicInteger failure = new AtomicInteger();
        boolean result = ProcessUtil.execute("k", failCmd, new CallbackResult<ProcessUtil.ProcessResultText>() {
            @Override
            public void onFailure(Throwable e, ProcessUtil.ProcessResultText obj) {
                failureWithThrowable.incrementAndGet();
            }

            @Override
            public void onFailure(ProcessUtil.ProcessResultText obj) {
                failure.incrementAndGet();
            }
        }, 100L, 5000L);
        Assert.assertFalse(result);
        Assert.assertTrue(failureWithThrowable.get() + failure.get() >= 1);
    }

    @Test
    public void testExecuteResultKeyedFailure() {
        List<String> failCmd = Arrays.asList("/bin/sh", "-c", "exit 1");
        AtomicInteger failure = new AtomicInteger();
        boolean result = ProcessUtil.executeResult("k", failCmd, new CallbackResult<ProcessUtil.ProcessResultText>() {
            @Override
            public void onFailure(Throwable e, ProcessUtil.ProcessResultText obj) {
                failure.incrementAndGet();
            }

            @Override
            public void onFailure(ProcessUtil.ProcessResultText obj) {
                failure.incrementAndGet();
            }
        }, 100L, 5000L);
        Assert.assertFalse(result);
        Assert.assertTrue(failure.get() >= 1);
    }

    @Test
    public void testExecuteKeyedNullCallbackTimeout() {
        List<String> sleepCmd = Arrays.asList("/bin/sh", "-c", "sleep 1");
        Assert.assertFalse(ProcessUtil.execute("k", sleepCmd, null, 50L, 100L));
    }

    @Test
    public void testExecuteResultOnContinueFalse() {
        List<String> sleepCmd = Arrays.asList("/bin/sh", "-c", "sleep 1");
        boolean result = ProcessUtil.executeResult(sleepCmd, new CallbackResult<String>() {
            @Override
            public boolean onContinue(String obj) {
                return false;
            }
        }, 50L, 5000L);
        Assert.assertFalse(result);
    }

    @Test
    public void testClearStreamLogOutput() throws Exception {
        ProcessUtil.setLogInfo();
        ProcessUtil.setLogConsole();
        Process process = new ProcessBuilder(Arrays.asList("/bin/sh", "-c", "echo hello; echo error-x")).start();
        AtomicInteger errorLines = new AtomicInteger();
        AtomicInteger infoLines = new AtomicInteger();
        ProcessUtil.ClearStreamResult clearResult = ProcessUtil.clearStream(process, new Callback() {
            @Override
            public void onLogOutput(String line, Boolean isError) {
                if (isError) {
                    errorLines.incrementAndGet();
                } else {
                    infoLines.incrementAndGet();
                }
            }
        });
        process.waitFor(5, TimeUnit.SECONDS);
        Thread.sleep(300);
        clearResult.close();
        Assert.assertTrue(errorLines.get() >= 1);
        Assert.assertTrue(infoLines.get() >= 1);
    }

    @Test
    public void testShutdownAndResult() throws Exception {
        Process process = new ProcessBuilder(ECHO).start();
        process.waitFor(5, TimeUnit.SECONDS);
        String result = ProcessUtil.resultAndShutdown(process);
        Assert.assertTrue(result.contains("hello"));
        ProcessUtil.shutdown(process);
    }

    @Test
    public void testClearStream() throws Exception {
        Process process = new ProcessBuilder(Arrays.asList("/bin/sh", "-c", "echo hello; echo error-msg >&2")).start();
        AtomicInteger errorLines = new AtomicInteger();
        ProcessUtil.ClearStreamResult clearResult = ProcessUtil.clearStream(process, new Callback() {
            @Override
            public void onLogOutput(String line, Boolean isError) {
                if (isError) {
                    errorLines.incrementAndGet();
                }
            }
        });
        Assert.assertNotNull(clearResult);
        process.waitFor(5, TimeUnit.SECONDS);
        Thread.sleep(300);
        clearResult.close();
        Assert.assertTrue(errorLines.get() >= 1);

        // null process
        Assert.assertNull(ProcessUtil.clearStream(null, null));

        // 手工构造 ClearStreamResult
        ProcessUtil.ClearStreamResult manual = ProcessUtil.ClearStreamResult.builder().build();
        manual.close();
    }

    @Test
    public void testProcessResultModels() {
        ProcessUtil.ProcessResult pr = ProcessUtil.ProcessResult.builder()
                .success(true).result("r").throwable(null).build();
        Assert.assertTrue(pr.getSuccess());
        Assert.assertEquals("r", pr.getResult());
        Assert.assertNull(pr.getThrowable());

        ProcessUtil.ProcessResultText prt = ProcessUtil.ProcessResultText.builder()
                .key("k").result("r").throwable(new RuntimeException("e")).build();
        Assert.assertEquals("k", prt.getKey());
        Assert.assertEquals("r", prt.getResult());
        Assert.assertNotNull(prt.getThrowable());

        ProcessUtil.ProcessResult pr2 = new ProcessUtil.ProcessResult();
        Assert.assertNull(pr2.getSuccess());
        ProcessUtil.ProcessResult pr3 = new ProcessUtil.ProcessResult(true, "r", null);
        Assert.assertTrue(pr3.getSuccess());
        ProcessUtil.ProcessResultText prt2 = new ProcessUtil.ProcessResultText();
        Assert.assertNull(prt2.getKey());
        ProcessUtil.ProcessResultText prt3 = new ProcessUtil.ProcessResultText("k", "r", null);
        Assert.assertEquals("k", prt3.getKey());
    }


    @Test
    public void testExecuteKeyedNullCallbackFailure() {
        // keyed 包装器 + null 回调 + 命令失败 → onFailure(Throwable) 包装器的 null 分支
        List<String> failCmd = Arrays.asList("/bin/sh", "-c", "exit 1");
        Assert.assertFalse(ProcessUtil.execute("k", failCmd, null, 100L, 5000L));
    }

    @Test
    public void testExecuteKeyedNullCallbackContinue() {
        // keyed 包装器 + null 回调 + 长命令且无超时 → onContinue 包装器 return true 后等待成功
        List<String> sleepCmd = Arrays.asList("/bin/sh", "-c", "sleep 0.5");
        Assert.assertTrue(ProcessUtil.execute("k", sleepCmd, null, 50L, null));
    }

    @Test
    public void testExecuteResultKeyedNullCallbackFailure() {
        List<String> failCmd = Arrays.asList("/bin/sh", "-c", "exit 1");
        Assert.assertFalse(ProcessUtil.executeResult("k", failCmd, null, 100L, 5000L));
    }

    @Test
    public void testExecuteResultKeyedNullCallbackContinue() {
        List<String> sleepCmd = Arrays.asList("/bin/sh", "-c", "sleep 0.5");
        Assert.assertTrue(ProcessUtil.executeResult("k", sleepCmd, null, 50L, null));
    }

    @Test
    public void testExecuteResultNullCallbackTimeout() {
        // null 回调 + 超时 → onTimeout/onExecute/onContinue 的 null 分支 + finally cancel
        List<String> sleepCmd = Arrays.asList("/bin/sh", "-c", "sleep 1");
        Assert.assertFalse(ProcessUtil.executeResult(sleepCmd, null, 50L, 100L));
    }

    @Test
    public void testExecuteResultOnFailureFromInterrupt() {
        // 轮询线程被中断 → get 抛 InterruptedException → catch(Throwable) → onFailure(e, result)
        List<String> sleepCmd = Arrays.asList("/bin/sh", "-c", "sleep 1");
        AtomicInteger failure = new AtomicInteger();
        boolean result = ProcessUtil.executeResult(sleepCmd, new CallbackResult<String>() {
            @Override
            public void onExecute(String obj) {
                Thread.currentThread().interrupt();
            }
        }, 50L, 5000L);
        Assert.assertFalse(result);
    }

    @Test
    public void testExecuteResultNullCallbackInterrupt() throws Exception {
        // null 回调 + 轮询被中断 → onFailure 的 null 分支
        List<String> sleepCmd = Arrays.asList("/bin/sh", "-c", "sleep 2");
        Thread interrupter = new Thread(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {
            }
            Thread.currentThread().interrupt();
        });
        // 中断当前（轮询）线程
        Thread main = Thread.currentThread();
        interrupter = new Thread(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {
            }
            main.interrupt();
        });
        interrupter.start();
        try {
            boolean result = ProcessUtil.executeResult(sleepCmd, null, 5000L, null);
            Assert.assertFalse(result);
        } finally {
            Thread.interrupted();
            interrupter.join();
        }
    }

    @Test
    public void testClearStreamErrorKeysAndConsole() throws Exception {
        // 输出包含 error 关键字 → 错误分支；否则 → 信息分支；控制台开关两个状态
        ProcessUtil.setLogInfo();
        ProcessUtil.setLogConsole();
        ProcessUtil.setErrorMessageKey(new ArrayList<>(Collections.singletonList("ERROR")));
        List<String> outCmd = Arrays.asList("/bin/sh", "-c", "echo ERROR-bad; echo good-line");
        AtomicInteger errorOut = new AtomicInteger();
        AtomicInteger infoOut = new AtomicInteger();
        try {
            Process process = new ProcessBuilder(outCmd).start();
            ProcessUtil.ClearStreamResult csr = ProcessUtil.clearStream(process, new Callback() {
                @Override
                public void onLogOutput(String log, Boolean error) {
                    if (error) {
                        errorOut.incrementAndGet();
                    } else {
                        infoOut.incrementAndGet();
                    }
                }
            });
            process.waitFor();
            csr.getInput().get(2, TimeUnit.SECONDS);
            csr.close();
            Assert.assertTrue(errorOut.get() >= 1);
            Assert.assertTrue(infoOut.get() >= 1);

            // error stream 输出（console 开/关两个状态）
            List<String> errCmd = Arrays.asList("/bin/sh", "-c", "echo err-line >&2");
            Process errProcess = new ProcessBuilder(errCmd).start();
            ProcessUtil.ClearStreamResult csr2 = ProcessUtil.clearStream(errProcess, new Callback() {
                @Override
                public void onLogOutput(String log, Boolean error) {
                }
            });
            errProcess.waitFor();
            csr2.close();
            ProcessUtil.setLogConsole();

            // 无回调 + error stream
            List<String> errCmd2 = Arrays.asList("/bin/sh", "-c", "echo err2 >&2");
            Process p3 = new ProcessBuilder(errCmd2).start();
            ProcessUtil.ClearStreamResult csr3 = ProcessUtil.clearStream(p3, null);
            p3.waitFor();
            csr3.close();
        } finally {
            ProcessUtil.setErrorMessageKey(null);
        }
    }

    @Test
    public void testClearStreamIoException() throws Exception {
        // 输入/错误流读取与关闭抛 IOException → 覆盖 catch 分支
        java.io.InputStream badIn = Mockito.mock(java.io.InputStream.class);
        Mockito.when(badIn.read()).thenThrow(new java.io.IOException("read fail"));
        Mockito.doThrow(new java.io.IOException("close fail")).when(badIn).close();
        java.io.InputStream badErr = Mockito.mock(java.io.InputStream.class);
        Mockito.when(badErr.read()).thenThrow(new java.io.IOException("err read fail"));
        Mockito.doThrow(new java.io.IOException("err close fail")).when(badErr).close();

        java.lang.Process process = Mockito.mock(java.lang.Process.class);
        Mockito.when(process.getInputStream()).thenReturn(badIn);
        Mockito.when(process.getErrorStream()).thenReturn(badErr);
        ProcessUtil.ClearStreamResult csr = ProcessUtil.clearStream(process, null);
        Assert.assertNotNull(csr);
        // 等待后台线程结束
        csr.getInput().get(2, TimeUnit.SECONDS);
        csr.getError().get(2, TimeUnit.SECONDS);
        csr.close();
    }

    @Test
    public void testClearStreamCloseCancels() throws Exception {
        // 流阻塞未结束 → close 取消两个后台任务
        java.io.PipedOutputStream sink1 = new java.io.PipedOutputStream();
        java.io.PipedOutputStream sink2 = new java.io.PipedOutputStream();
        java.io.PipedInputStream blockingIn = new java.io.PipedInputStream(sink1);
        java.io.PipedInputStream blockingErr = new java.io.PipedInputStream(sink2);
        java.lang.Process process = Mockito.mock(java.lang.Process.class);
        Mockito.when(process.getInputStream()).thenReturn(blockingIn);
        Mockito.when(process.getErrorStream()).thenReturn(blockingErr);
        ProcessUtil.ClearStreamResult csr = ProcessUtil.clearStream(process, null);
        Thread.sleep(100);
        csr.close();
        Assert.assertTrue(csr.getInput().isCancelled() || csr.getInput().isDone());
        Assert.assertTrue(csr.getError().isCancelled() || csr.getError().isDone());
        sink1.close();
        sink2.close();
        blockingIn.close();
        blockingErr.close();
    }


    @Test
    public void testExecuteCoreNullCallbackTimeoutLoop() {
        // 核心 execute + null 回调 + 超时 → onTimeout/onExecute/onContinue 的 null 分支
        List<String> sleepCmd = Arrays.asList("/bin/sh", "-c", "sleep 1");
        Assert.assertFalse(ProcessUtil.execute(sleepCmd, null, 50L, 100L));
    }

    @Test
    public void testExecuteCoreNullCallbackContinue() {
        List<String> sleepCmd = Arrays.asList("/bin/sh", "-c", "sleep 0.5");
        Assert.assertTrue(ProcessUtil.execute(sleepCmd, null, 50L, null));
    }

    @Test
    public void testExecuteTimeoutNotPositive() {
        // timeout <= 0 → 不触发超时退出，等待命令完成
        List<String> sleepCmd = Arrays.asList("/bin/sh", "-c", "sleep 0.5");
        AtomicInteger onExecute = new AtomicInteger();
        boolean result = ProcessUtil.execute(sleepCmd, new Callback() {
            @Override
            public void onExecute() {
                onExecute.incrementAndGet();
            }
        }, 50L, 0L);
        Assert.assertTrue(result);
        Assert.assertTrue(onExecute.get() >= 1);
    }

    @Test
    public void testExecuteResultCoreNullCallbackTimeoutLoop() {
        List<String> sleepCmd = Arrays.asList("/bin/sh", "-c", "sleep 1");
        Assert.assertFalse(ProcessUtil.executeResult(sleepCmd, null, 50L, 100L));
    }

    @Test
    public void testExecuteResultCoreNullCallbackContinue() {
        List<String> sleepCmd = Arrays.asList("/bin/sh", "-c", "sleep 0.5");
        Assert.assertTrue(ProcessUtil.executeResult(sleepCmd, null, 50L, null));
    }

    @Test
    public void testExecuteResultTimeoutNotPositive() {
        List<String> sleepCmd = Arrays.asList("/bin/sh", "-c", "sleep 0.5");
        AtomicInteger onExecute = new AtomicInteger();
        boolean result = ProcessUtil.executeResult(sleepCmd, new CallbackResult<String>() {
            @Override
            public void onExecute(String obj) {
                onExecute.incrementAndGet();
            }
        }, 50L, 0L);
        Assert.assertTrue(result);
        Assert.assertTrue(onExecute.get() >= 1);
    }

    @Test
    public void testExecuteResultKeyedUnValid() {
        AtomicInteger unValid = new AtomicInteger();
        boolean result = ProcessUtil.executeResult("k", Collections.emptyList(), new CallbackResult<ProcessUtil.ProcessResultText>() {
            @Override
            public void onUnValid(ProcessUtil.ProcessResultText obj) {
                unValid.incrementAndGet();
            }
        }, 100L, 1000L);
        Assert.assertFalse(result);
        Assert.assertEquals(1, unValid.get());
    }

    @Test
    public void testExecuteResultKeyedNullCallbackTimeout() {
        List<String> sleepCmd = Arrays.asList("/bin/sh", "-c", "sleep 1");
        Assert.assertFalse(ProcessUtil.executeResult("k", sleepCmd, null, 50L, 100L));
    }

    @Test
    public void testExecuteResultCoreUnValidNullCallback() {
        Assert.assertFalse(ProcessUtil.executeResult(Collections.emptyList(), null, 100L, 1000L));
    }

    @Test
    public void testClearStreamFlagsOff() throws Exception {
        // 关闭日志/控制台开关（反射恢复默认 false），覆盖各开关的 false 分支
        java.lang.reflect.Field infoF = ProcessUtil.class.getDeclaredField("enableLogInfo");
        infoF.setAccessible(true);
        infoF.set(null, false);
        java.lang.reflect.Field consoleF = ProcessUtil.class.getDeclaredField("enableLogConsole");
        consoleF.setAccessible(true);
        consoleF.set(null, false);
        ProcessUtil.setErrorMessageKey(new ArrayList<>(Collections.singletonList("ERROR")));
        try {
            AtomicInteger errorOut = new AtomicInteger();
            AtomicInteger infoOut = new AtomicInteger();
            // error 关键字分支（console 关）+ info 分支（info/console 关）
            Process p = new ProcessBuilder(Arrays.asList("/bin/sh", "-c", "echo ERROR-bad; echo good-line")).start();
            ProcessUtil.ClearStreamResult csr = ProcessUtil.clearStream(p, new Callback() {
                @Override
                public void onLogOutput(String log, Boolean error) {
                    if (error) {
                        errorOut.incrementAndGet();
                    } else {
                        infoOut.incrementAndGet();
                    }
                }
            });
            p.waitFor();
            csr.getInput().get(2, TimeUnit.SECONDS);
            csr.close();
            Assert.assertTrue(errorOut.get() >= 1);
            Assert.assertTrue(infoOut.get() >= 1);

            // error 关键字分支 + null 回调
            Process p2 = new ProcessBuilder(Arrays.asList("/bin/sh", "-c", "echo ERROR-null")).start();
            ProcessUtil.ClearStreamResult csr2 = ProcessUtil.clearStream(p2, null);
            p2.waitFor();
            csr2.getInput().get(2, TimeUnit.SECONDS);
            csr2.close();

            // error stream 分支（console 关）
            Process p3 = new ProcessBuilder(Arrays.asList("/bin/sh", "-c", "echo err-off >&2")).start();
            ProcessUtil.ClearStreamResult csr3 = ProcessUtil.clearStream(p3, null);
            p3.waitFor();
            csr3.getError().get(2, TimeUnit.SECONDS);
            csr3.close();
        } finally {
            ProcessUtil.setErrorMessageKey(null);
            ProcessUtil.setLogInfo();
            ProcessUtil.setLogConsole();
        }
    }


    @Test
    public void testExecuteCoreNullCallbackInterrupt() throws Exception {
        // null 回调 + 轮询被中断 → execute 核心 catch(Throwable) 的 null 分支
        List<String> sleepCmd = Arrays.asList("/bin/sh", "-c", "sleep 2");
        Thread main = Thread.currentThread();
        Thread interrupter = new Thread(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {
            }
            main.interrupt();
        });
        interrupter.start();
        try {
            Assert.assertFalse(ProcessUtil.execute(sleepCmd, null, 5000L, null));
        } finally {
            Thread.interrupted();
            interrupter.join();
        }
    }

    @Test
    public void testExecuteCoreInterruptWithCallback() {
        // onExecute 中断轮询线程 → catch(Throwable) → onFailure(e)
        List<String> sleepCmd = Arrays.asList("/bin/sh", "-c", "sleep 1");
        AtomicInteger failure = new AtomicInteger();
        boolean result = ProcessUtil.execute(sleepCmd, new Callback() {
            @Override
            public void onExecute() {
                Thread.currentThread().interrupt();
            }
        }, 50L, 5000L);
        Assert.assertFalse(result);
    }

    @Test
    public void testExecuteResultKeyedUnValidNullCallback() {
        Assert.assertFalse(ProcessUtil.executeResult("k", Collections.emptyList(), null, 100L, 1000L));
    }

    @Test
    public void testExecuteResultCoreNullCallbackFailure() {
        Assert.assertFalse(ProcessUtil.executeResult(Arrays.asList("/bin/sh", "-c", "exit 1"), null, 100L, 5000L));
    }

    @Test
    public void testClearStreamInfoNullCallback() throws Exception {
        // info 分支 + null 回调
        ProcessUtil.setErrorMessageKey(new ArrayList<>(Collections.singletonList("ERROR")));
        try {
            Process p = new ProcessBuilder(Arrays.asList("/bin/sh", "-c", "echo plain-line")).start();
            ProcessUtil.ClearStreamResult csr = ProcessUtil.clearStream(p, null);
            p.waitFor();
            csr.getInput().get(2, TimeUnit.SECONDS);
            csr.close();
        } finally {
            ProcessUtil.setErrorMessageKey(null);
        }
    }


}
