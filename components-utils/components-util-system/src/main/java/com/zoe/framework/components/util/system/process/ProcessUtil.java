package com.zoe.framework.components.util.system.process;

import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.system.SystemUtil;
import com.zoe.framework.components.core.common.serializable.SerializableBean;
import com.zoe.framework.components.core.exception.ZoeRuntimeException;
import com.zoe.framework.components.util.constant.StringPoolConst;
import com.zoe.framework.components.util.event.core.Callback;
import com.zoe.framework.components.util.event.core.CallbackResult;
import com.zoe.framework.components.util.executor.ThreadPoolUtil;
import com.zoe.framework.components.util.value.data.CollectionUtil;
import com.zoe.framework.components.util.value.data.ObjectUtil;
import com.zoe.framework.components.util.value.data.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

/**
 * @author 蒋时华
 * @date 2021-11-24 13:40:59
 */
@Slf4j
@UtilityClass
public class ProcessUtil extends RuntimeUtil {

    private static boolean enableLogInfo = false;
    private static boolean enableLogConsole = false;
    private static Collection<String> errorMessageKeys = new ArrayList<String>();

    static {
        enableLogInfo = SystemUtil.getBoolean("enabledProcessLogInfo", false);
        enableLogConsole = SystemUtil.getBoolean("enabledConsole", false);
        errorMessageKeys = CollectionUtil.newArrayList("error");
    }

    public static void setLogInfo() {
        enableLogInfo = true;
    }

    public static void setLogConsole() {
        enableLogConsole = true;
    }

    public static void setErrorMessageKey(Collection<String> errorKeys) {
        if (ObjectUtil.isNull(errorKeys)) {
            errorKeys = new ArrayList<>();
        }
        errorMessageKeys = errorKeys;
    }

    /**
     * 执行命令，并开启监控进度线程，不能使用有界线程池，需要单独开启线程处理, 使用有界线程池会导致线程不够，无法获取进度信息，导致上层消息超时
     *
     * @param command
     * @param callback 指定进度回调
     * @param command  执行进度回调间隔时间, 毫秒
     * @param command  执行超时时间, 毫秒
     */
    public static boolean execute(String key, List<String> command, CallbackResult<ProcessResultText> callback, Long callbackTime, Long timeout) {
        return execute(command, new Callback() {
            @Override
            public void onSuccess() {
                if (ObjectUtil.isNotNull(callback)) {
                    callback.onSuccess(ProcessResultText.builder()
                            .key(key)
                            .build());
                }
            }

            @Override
            public void onUnValid() {
                if (ObjectUtil.isNotNull(callback)) {
                    callback.onUnValid(ProcessResultText.builder()
                            .key(key)
                            .build());
                }
            }

            @Override
            public void onFailure(Throwable e) {
                if (ObjectUtil.isNotNull(callback)) {
                    callback.onFailure(ProcessResultText.builder()
                            .key(key)
                            .throwable(e)
                            .build());
                }
            }

            @Override
            public void onFailure() {
                if (ObjectUtil.isNotNull(callback)) {
                    callback.onFailure(ProcessResultText.builder()
                            .key(key)
                            .build());
                }
            }

            @Override
            public void onExecute() {
                if (ObjectUtil.isNotNull(callback)) {
                    callback.onExecute(ProcessResultText.builder()
                            .key(key)
                            .build());
                }
            }

            @Override
            public void onTimeout() {
                if (ObjectUtil.isNotNull(callback)) {
                    callback.onTimeout(ProcessResultText.builder()
                            .key(key)
                            .build());
                }
            }

            @Override
            public boolean onContinue() {
                if (ObjectUtil.isNotNull(callback)) {
                    return callback.onContinue(ProcessResultText.builder()
                            .key(key)
                            .build());
                }
                return true;
            }
        }, callbackTime, timeout);
    }

    /**
     * 执行命令，并开启监控进度线程，不能使用有界线程池，需要单独开启线程处理, 使用有界线程池会导致线程不够，无法获取进度信息，导致上层消息超时
     *
     * @param command
     * @param callback 指定进度回调
     * @param command  执行进度回调间隔时间, 毫秒
     * @param command  执行超时时间, 毫秒
     */
    public static boolean execute(List<String> command, Callback callback, Long callbackTime, Long timeout) {
        if (CollectionUtil.isEmpty(command)) {
            // un valid
            if (ObjectUtil.isNotNull(callback)) {
                callback.onUnValid();
            }
            return false;
        }
        if (ObjectUtil.isNull(callbackTime)) {
            if (ObjectUtil.isNotNull(timeout)) {
                callbackTime = timeout;
            } else {
                callbackTime = Long.MAX_VALUE;
            }
        }
        ProcessBuilder pb = new ProcessBuilder(command);
        // 创建执行线程
        Future<ProcessResult> resultFuture = ThreadPoolUtil.execAsync(() -> {
            boolean success = true;
            Throwable throwable = null;
            pb.redirectErrorStream(true);
            Process process = null;
            ClearStreamResult clearStreamResult = null;
            try {
                process = pb.start();
                clearStreamResult = ProcessUtil.clearStream(process, callback);
                int i = process.waitFor();
                if (i != 0) {
                    String cmd = command.stream().collect(Collectors.joining(" "));
                    String error = "执行命令失败, 处理状态码: " + i + " 执行命令：" + cmd;
                    log.error(error);
                    throw new ZoeRuntimeException(error);
                }
            } catch (Throwable e) {
                log.error(e.getMessage(), e);
                log.error("执行命令失败, 错误信息：{}", e.getMessage());
                success = false;
                throwable = e;
            } finally {
                ProcessUtil.shutdown(process);
                if (ObjectUtil.isNotNull(clearStreamResult)) {
                    clearStreamResult.close();
                }
            }
            return ProcessResult.builder()
                    .success(success)
                    .throwable(throwable)
                    .build();
        });
        // 轮训进度信息
        long currentExecuteTime = 0;
        try {
            while (true) {
                try {
                    ProcessResult result = resultFuture.get(callbackTime, TimeUnit.MILLISECONDS);
                    if (ObjectUtil.isNull(result)) {
                        result = ProcessResult.builder().success(false).build();
                    }
                    if (result.getSuccess()) {
                        // success
                        if (ObjectUtil.isNotNull(callback)) {
                            callback.onSuccess();
                        }
                        return true;
                    } else {
                        // fail
                        if (ObjectUtil.isNotNull(callback)) {
                            if (ObjectUtil.isNull(result.getThrowable())) {
                                callback.onFailure();
                            } else {
                                callback.onFailure(result.getThrowable());
                            }
                        }
                        return false;
                    }
                } catch (TimeoutException e) {
                    // 任务正在进行中，do nothing
                    currentExecuteTime += callbackTime;
                    log.debug("尝试获取结果失败，命令还在处理中, 相关时间参数: 当前执行耗时 = {} , 回调检查间隔 = {} , 超时限制 = {}",
                            currentExecuteTime, callbackTime, timeout);
                    if (ObjectUtil.isNotNull(timeout) && timeout > 0 && currentExecuteTime >= timeout) {
                        log.debug("命令执行超时退出，当前耗时:{}, 触发阈值：{}", currentExecuteTime, timeout);
                        // 超时
                        if (ObjectUtil.isNotNull(callback)) {
                            callback.onTimeout();
                        }
                        return false;
                    }
                    // 正在执行中
                    if (ObjectUtil.isNotNull(callback)) {
                        callback.onExecute();
                    }
                    // 是否继续
                    if (ObjectUtil.isNotNull(callback)) {
                        boolean c = callback.onContinue();
                        if (!c) {
                            // 当前可能已经执行完，默认放弃结果
                            return false;
                        }
                    }
                } catch (Throwable e) {
                    // 其他异常，退出
                    log.error(e.getMessage(), e);
                    if (ObjectUtil.isNotNull(callback)) {
                        callback.onFailure(e);
                    }
                    return false;
                }
            }
        } finally {
            if (ObjectUtil.isNotNull(resultFuture) && !resultFuture.isDone()) {
                resultFuture.cancel(true);
            }
        }
    }

    /**
     * 执行命令，并开启监控进度线程，不能使用有界线程池，需要单独开启线程处理, 使用有界线程池会导致线程不够，无法获取进度信息，导致上层消息超时
     *
     * @param command
     * @param callback 指定进度回调
     * @param command  执行进度回调间隔时间, 毫秒
     * @param command  执行超时时间, 毫秒
     */
    public static boolean executeResult(String key, List<String> command, CallbackResult<ProcessResultText> callback, Long callbackTime, Long timeout) {
        return executeResult(command, new CallbackResult<String>() {

            @Override
            public void onUnValid(String obj) {
                if (ObjectUtil.isNotNull(callback)) {
                    callback.onUnValid(ProcessResultText.builder()
                            .key(key)
                            .result(obj)
                            .build());
                }
            }

            @Override
            public void onSuccess(String obj) {
                if (ObjectUtil.isNotNull(callback)) {
                    callback.onSuccess(ProcessResultText.builder()
                            .key(key)
                            .result(obj)
                            .build());
                }
            }

            @Override
            public void onFailure(Throwable e, String obj) {
                if (ObjectUtil.isNotNull(callback)) {
                    callback.onFailure(ProcessResultText.builder()
                            .key(key)
                            .result(obj)
                            .throwable(e)
                            .build());
                }
            }

            @Override
            public void onFailure(String obj) {
                if (ObjectUtil.isNotNull(callback)) {
                    callback.onFailure(ProcessResultText.builder()
                            .key(key)
                            .result(obj)
                            .build());
                }
            }

            @Override
            public void onExecute(String obj) {
                if (ObjectUtil.isNotNull(callback)) {
                    callback.onExecute(ProcessResultText.builder()
                            .key(key)
                            .result(obj)
                            .build());
                }
            }

            @Override
            public void onTimeout(String obj) {
                if (ObjectUtil.isNotNull(callback)) {
                    callback.onTimeout(ProcessResultText.builder()
                            .key(key)
                            .result(obj)
                            .build());
                }
            }

            @Override
            public boolean onContinue(String obj) {
                if (ObjectUtil.isNotNull(callback)) {
                    return callback.onContinue(ProcessResultText.builder()
                            .key(key)
                            .result(obj)
                            .build());
                }
                return true;
            }

        }, callbackTime, timeout);
    }

    /**
     * 执行命令，并开启监控进度线程，不能使用有界线程池，需要单独开启线程处理, 使用有界线程池会导致线程不够，无法获取进度信息，导致上层消息超时
     *
     * @param command
     * @return
     */
    public static boolean executeResult(List<String> command, CallbackResult<String> callback, Long callbackTime, Long timeout) {
        if (CollectionUtil.isEmpty(command)) {
            // un valid
            if (ObjectUtil.isNotNull(callback)) {
                callback.onUnValid(StringPoolConst.EMPTY);
            }
            return false;
        }
        if (ObjectUtil.isNull(callbackTime)) {
            if (ObjectUtil.isNotNull(timeout)) {
                callbackTime = timeout;
            } else {
                callbackTime = Long.MAX_VALUE;
            }
        }
        ProcessBuilder pb = new ProcessBuilder(command);
        Future<ProcessResult> resultFuture = ThreadPoolUtil.execAsync(() -> {
            boolean success = true;
            Throwable throwable = null;
            String resultStr = StringPoolConst.EMPTY;
            pb.redirectErrorStream(true);
            Process process = null;
            try {
                process = pb.start();
                int i = process.waitFor();
                if (i != 0) {
                    String cmd = command.stream().collect(Collectors.joining(" "));
                    String error = "执行命令失败, 处理状态码: " + i + " 执行命令：" + cmd;
                    log.error(error);
                    throw new ZoeRuntimeException(error);
                }
                resultStr = resultAndShutdown(process);
            } catch (Throwable e) {
                log.error(e.getMessage(), e);
                log.error("执行命令失败, 错误信息：{}", e.getMessage());
                success = false;
                throwable = e;
            } finally {
                ProcessUtil.shutdown(process);
            }
            return ProcessResult.builder()
                    .success(success)
                    .result(resultStr)
                    .throwable(throwable)
                    .build();
        });
        // 轮训进度信息
        long currentExecuteTime = 0;
        try {
            while (true) {
                ProcessResult result = ProcessResult.builder().success(false).build();
                try {
                    result = resultFuture.get(callbackTime, TimeUnit.MILLISECONDS);
                    if (ObjectUtil.isNull(result)) {
                        result = ProcessResult.builder().success(false).build();
                    }
                    if (result.getSuccess()) {
                        // success
                        if (ObjectUtil.isNotNull(callback)) {
                            callback.onSuccess(result.getResult());
                        }
                        return true;
                    } else {
                        // fail
                        if (ObjectUtil.isNotNull(callback)) {
                            if (ObjectUtil.isNull(result.getThrowable())) {
                                callback.onFailure(result.getResult());
                            } else {
                                callback.onFailure(result.getThrowable(), result.getResult());
                            }
                        }
                        return false;
                    }
                } catch (TimeoutException e) {
                    // 任务正在进行中，do nothing
                    log.debug("尝试获取结果失败，命令还在处理中, 相关时间参数: 当前执行耗时 = {} , 回调检查间隔 = {} , 超时限制 = {}",
                            currentExecuteTime, callbackTime, timeout);
                    currentExecuteTime += callbackTime;
                    if (ObjectUtil.isNotNull(timeout) && timeout > 0 && currentExecuteTime >= timeout) {
                        log.debug("命令执行超时退出，当前耗时:{}, 触发阈值：{}", currentExecuteTime, timeout);
                        // 超时
                        if (ObjectUtil.isNotNull(callback)) {
                            callback.onTimeout(result.getResult());
                        }
                        return false;
                    }
                    // 正在执行中
                    if (ObjectUtil.isNotNull(callback)) {
                        callback.onExecute(result.getResult());
                    }
                    // 是否继续
                    if (ObjectUtil.isNotNull(callback)) {
                        boolean c = callback.onContinue(result.getResult());
                        if (!c) {
                            // 当前可能已经执行完，默认放弃结果
                            return false;
                        }
                    }
                } catch (Throwable e) {
                    // 其他异常，退出
                    log.error(e.getMessage(), e);
                    if (ObjectUtil.isNotNull(callback)) {
                        callback.onFailure(e, result.getResult());
                    }
                    return false;
                }
            }
        } finally {
            if (ObjectUtil.isNotNull(resultFuture) && !resultFuture.isDone()) {
                resultFuture.cancel(true);
            }
        }
    }

    /**
     * 关闭进程
     *
     * @param process
     */
    public static void shutdown(Process process) {
        RuntimeUtil.destroy(process);
    }

    /**
     * 获取信息并关闭进程
     *
     * @param process
     * @return
     */
    public static String resultAndShutdown(Process process) {
        return RuntimeUtil.getResult(process);
    }

    /**
     * 即时清理缓冲区，不能使用有界线程池，需要单独开启线程处理, 使用有界线程池会导致线程不够，缓冲区无法消费，线程卡死
     *
     * @param process
     */
    public static ClearStreamResult clearStream(Process process, Callback callback) {
        if (process == null) {
            return null;
        }
        return ClearStreamResult.builder()
                // 处理InputStream的线程
                .input(ThreadPoolUtil.execAsync(() -> {
                    BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String line = null;
                    try {
                        while ((line = in.readLine()) != null) {
                            if (CollectionUtil.isNotEmpty(errorMessageKeys)) {
                                for (String errorMessageKey : errorMessageKeys) {
                                    if (StrUtil.containsIgnoreCase(line, errorMessageKey)) {
                                        // 有些错误不会使用标准输出，比如：ffmpeg
                                        log.error("processErr: " + line);
                                        if (enableLogConsole) {
                                            System.out.println("processErr: " + line);
                                        }
                                        if (ObjectUtil.isNotNull(callback)) {
                                            callback.onLogOutput(line, true);
                                        }
                                    } else {
                                        if (enableLogInfo) {
                                            log.info("processInfo: " + line);
                                        }
                                        if (enableLogConsole) {
                                            System.out.println("processInfo: " + line);
                                        }
                                        if (ObjectUtil.isNotNull(callback)) {
                                            callback.onLogOutput(line, false);
                                        }
                                    }
                                }
                            }
                        }
                    } catch (IOException e) {
                        log.error(e.getMessage(), e);
                    } finally {
                        try {
                            in.close();
                        } catch (IOException e) {
                            log.error(e.getMessage(), e);
                        }
                    }
                }))
                // 处理ErrorStream的线程
                .error(ThreadPoolUtil.execAsync(() -> {
                    BufferedReader err = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                    String line = null;
                    try {
                        while ((line = err.readLine()) != null) {
                            log.error("processErr: " + line);
                            if (enableLogConsole) {
                                System.out.println("processErr: " + line);
                            }
                            if (ObjectUtil.isNotNull(callback)) {
                                callback.onLogOutput(line, true);
                            }
                        }
                    } catch (IOException e) {
                        log.error(e.getMessage(), e);
                    } finally {
                        try {
                            err.close();
                        } catch (IOException e) {
                            log.error(e.getMessage(), e);
                        }
                    }
                }))
                .build();
    }


    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Accessors(chain = true)
    public static class ClearStreamResult extends SerializableBean implements Closeable {
        private static final long serialVersionUID = 2240060032300338391L;
        private Future<?> input;
        private Future<?> error;

        @Override
        public void close() {
            if (ObjectUtil.isNotNull(input) && !input.isDone()) {
                input.cancel(true);
            }
            if (ObjectUtil.isNotNull(error) && !error.isDone()) {
                error.cancel(true);
            }
        }

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Accessors(chain = true)
    public static class ProcessResult extends SerializableBean {
        private static final long serialVersionUID = 2240060032300338391L;
        private Boolean success;
        private String result;
        private Throwable throwable;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Accessors(chain = true)
    public static class ProcessResultText extends SerializableBean {
        private static final long serialVersionUID = 2240060032300338391L;
        private String key;
        private String result;
        private Throwable throwable;
    }

}
