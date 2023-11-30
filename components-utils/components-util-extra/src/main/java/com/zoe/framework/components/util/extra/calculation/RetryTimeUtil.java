package com.zoe.framework.components.util.extra.calculation;

import lombok.experimental.UtilityClass;

/**
 * 重试工具类
 *
 * @author 蒋时华
 * @date 2019/9/2
 */
@UtilityClass
public class RetryTimeUtil {

    /**
     * 比较两个时间差是否满足重试条件
     *
     * <pre>
     *     公式为：当前时间 - 创建时间 >= 2的n次幂 * 间隔秒因子
     *     <li>当前时间从ThreadLocal中获取传入</li>
     *     <li>n次幂由重试次数决定</li>
     *     <li>间隔秒因子由系统配置，如30s、60s等</li>
     * </pre>
     *
     * @param currentTime       当前时间
     * @param targetTriggerTime 目标触发时间
     * @param retryCount        重试次数
     * @param intervalSec       间隔秒因子
     * @return
     */
    public static boolean ifRetry(Long currentTime, Long targetTriggerTime, int retryCount, int intervalSec) {
        // 获取时间差
        long offsetSec = (currentTime - targetTriggerTime) / 1000;
        // 得到2的n次幂
        long power = new Double(Math.pow(2, retryCount)).longValue();
        // 返回是否满足重试条件
        return offsetSec >= power * intervalSec;
    }

    public static Long getNextRetryTime(Long currentTime, int retryCount, int intervalSec) {
        // 得到2的n次幂
        long power = new Double(Math.pow(2, retryCount)).longValue();
        // 返回是否满足重试条件
        return currentTime + power * intervalSec;
    }

}
