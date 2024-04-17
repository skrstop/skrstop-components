package com.skrstop.framework.components.util.stress;


import com.skrstop.framework.components.util.stress.result.SimpleResultFormater;
import com.skrstop.framework.components.util.stress.result.StressResult;
import com.skrstop.framework.components.util.stress.result.StressResultFormater;
import com.skrstop.framework.components.util.stress.task.StressTask;
import com.skrstop.framework.components.util.stress.task.StressTester;
import lombok.experimental.UtilityClass;

import java.io.StringWriter;

/**
 * @author 蒋时华
 * @date 2018-02-15
 **/
@UtilityClass
public class StressStoreUtil {

    private static StressTester stressTester = new StressTester();
    private static SimpleResultFormater simpleResultFormater = new SimpleResultFormater();

    /**
     * 压力测试，不打印报告
     *
     * @param concurrencyLevel
     * @param totalRequests
     * @param stressTask
     * @return
     */
    public static StressResult test(int concurrencyLevel, int totalRequests, StressTask stressTask) {
        return stressTester.test(concurrencyLevel, totalRequests, stressTask, 0);
    }

    /**
     * 压力测试，不打印报告，指定预热执行次数
     *
     * @param concurrencyLevel
     * @param totalRequests
     * @param stressTask
     * @return
     */
    public static StressResult test(int concurrencyLevel, int totalRequests, StressTask stressTask, int warmUpTime) {
        return stressTester.test(concurrencyLevel, totalRequests, stressTask, warmUpTime);
    }

    /**
     * 压力测试，打印报告
     *
     * @param concurrencyLevel
     * @param totalRequests
     * @param stressTask
     * @return
     */
    public static void testAndPrint(int concurrencyLevel, int totalRequests, StressTask stressTask) {
        testAndPrint(concurrencyLevel, totalRequests, stressTask, (String) null, 0);
    }

    /**
     * 压力测试，打印报告，指定预热执行次数
     *
     * @param concurrencyLevel
     * @param totalRequests
     * @param stressTask
     * @return
     */
    public static void testAndPrint(int concurrencyLevel, int totalRequests, StressTask stressTask, int warmUpTime) {
        testAndPrint(concurrencyLevel, totalRequests, stressTask, (String) null, warmUpTime);
    }

    public static void testAndPrint(int concurrencyLevel, int totalRequests, StressTask stressTask, String testName, int warmUpTime) {
        StressResult stressResult = test(concurrencyLevel, totalRequests, stressTask, warmUpTime);
        String str = format(stressResult);
        System.out.println(str);
    }

    /**
     * 打印压力测试结果报告
     *
     * @param stressResult
     * @return
     */
    public static String format(StressResult stressResult) {
        return format(stressResult, simpleResultFormater);
    }

    /**
     * 打印压力测试结果报告
     *
     * @param stressResult
     * @return
     */
    public static String format(StressResult stressResult, StressResultFormater stressResultFormater) {
        StringWriter sw = new StringWriter();
        stressResultFormater.format(stressResult, sw);
        return sw.toString();
    }

}
