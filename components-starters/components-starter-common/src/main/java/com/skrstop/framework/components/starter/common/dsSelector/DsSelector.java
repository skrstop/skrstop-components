package com.skrstop.framework.components.starter.common.dsSelector;

import org.aopalliance.intercept.MethodInvocation;

/**
 * @author 蒋时华
 * @date 2023-12-07 19:25:03
 */
public abstract class DsSelector {

    /**
     * 下一个执行器
     */
    private DsSelector nextDsSelector;

    /**
     * 设置下一个执行器
     *
     * @param dsProcessor 执行器
     */
    public void setNextProcessor(DsSelector dsProcessor) {
        this.nextDsSelector = dsProcessor;
    }

    /**
     * 抽象匹配条件 匹配才会走当前执行器否则走下一级执行器
     *
     * @param key DS注解里的内容
     * @return 是否匹配
     */
    public abstract boolean matches(String key);

    /**
     * 决定数据源
     * <pre>
     *     调用底层doDetermineDatasource，
     *     如果返回的是null则继续执行下一个，否则直接返回
     * </pre>
     *
     * @param invocation 方法执行信息
     * @param key        DS注解里的内容
     * @return 数据源名称
     */
    public String determineDataSource(MethodInvocation invocation, String key) {
        if (matches(key)) {
            String datasource = doDetermineDataSource(invocation, key);
            if (datasource == null && nextDsSelector != null) {
                return nextDsSelector.determineDataSource(invocation, key);
            }
            return datasource;
        }
        if (nextDsSelector != null) {
            return nextDsSelector.determineDataSource(invocation, key);
        }
        return null;
    }

    /**
     * 抽象最终决定数据源
     *
     * @param invocation 方法执行信息
     * @param key        DS注解里的内容
     * @return 数据源名称
     */
    public abstract String doDetermineDataSource(MethodInvocation invocation, String key);

}
