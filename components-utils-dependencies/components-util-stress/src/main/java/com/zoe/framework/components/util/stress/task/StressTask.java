package com.zoe.framework.components.util.stress.task;

/**
 * 并发主体
 *
 * @author 蒋时华
 * @date 2018-02-15
 **/
public interface StressTask {
    Object doTask() throws Exception;
}
