package com.skrstop.framework.components.starter.feign.protostuff.configuration.interceptor;

import org.springframework.core.NamedThreadLocal;

import java.util.ArrayDeque;
import java.util.Deque;

public final class DynamicFeignClientMethodContextHolder {

    /**
     * 为什么要用链表存储(准确的是栈)
     * <pre>
     * 为了支持嵌套切换，如ABC三个service都是不同的数据源
     * 其中A的某个业务要调B的方法，B的方法需要调用C的方法。一级一级调用切换，形成了链。
     * 传统的只设置当前线程的方式不能满足此业务需求，必须使用栈，后进先出。
     * </pre>
     */
    private static final ThreadLocal<Deque<Boolean>> LOOKUP_KEY_HOLDER = new NamedThreadLocal<Deque<Boolean>>("dynamic-feign-client-method") {
        @Override
        protected Deque<Boolean> initialValue() {
            return new ArrayDeque<>();
        }
    };

    private DynamicFeignClientMethodContextHolder() {
    }

    public static boolean peek() {
        return Boolean.TRUE.equals(LOOKUP_KEY_HOLDER.get().peek());
    }

    public static void push(boolean useFeign) {
        LOOKUP_KEY_HOLDER.get().push(useFeign);
    }

    public static void poll() {
        Deque<Boolean> deque = LOOKUP_KEY_HOLDER.get();
        deque.poll();
        if (deque.isEmpty()) {
            LOOKUP_KEY_HOLDER.remove();
        }
    }

    /**
     * 强制清空本地线程
     * <p>
     * 防止内存泄漏，如手动调用了push可调用此方法确保清除
     * </p>
     */
    public static void clear() {
        LOOKUP_KEY_HOLDER.remove();
    }
}