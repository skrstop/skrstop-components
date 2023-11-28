package cn.auntec.framework.components.util.stress;


import cn.auntec.framework.components.util.stress.result.SimpleResultFormater;
import cn.auntec.framework.components.util.stress.result.StressResult;
import cn.auntec.framework.components.util.stress.result.StressResultFormater;
import cn.auntec.framework.components.util.stress.task.StressTask;
import cn.auntec.framework.components.util.stress.task.StressTester;

import java.io.StringWriter;

/**
 * @author 蒋时华
 * @date 2018-02-15
 **/
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

    public static void main(String[] args) {

        // demo 1
        StressResult stressResult = StressStoreUtil.test(10, 10, new StressTask() {
            @Override
            public Object doTask() throws Exception {
                /**
                 *  your task code
                 */
                Thread.sleep(10);
                System.out.println(100);
                return null;
            }
        });
        String str = StressStoreUtil.format(stressResult);
        System.out.println(str);

        // demo 2
        StressStoreUtil.testAndPrint(10, 10, new StressTask() {
            @Override
            public Object doTask() throws Exception {
                /**
                 *  your task code
                 */
                Thread.sleep(10);
                System.out.println(100);
                return null;
            }
        });

    }

}
