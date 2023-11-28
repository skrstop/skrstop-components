package cn.auntec.framework.components.util.stress.result;

import cn.auntec.framework.components.util.stress.util.StatisticsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author 蒋时华
 * @date 2018-02-15
 **/
public class SimpleResultFormater implements StressResultFormater {

    protected static Logger log = LoggerFactory.getLogger(SimpleResultFormater.class);

    @Override
    public void format(StressResult stressResult, Writer writer) {
        long testsTakenTime = stressResult.getTestsTakenTime();
        int totalRequests = stressResult.getTotalRequests();
        int concurrencyLevel = stressResult.getConcurrencyLevel();
        float takes = StatisticsUtils.toMs(testsTakenTime);
        List<Long> allTimes = stressResult.getAllTimes();
        long totaleTimes = StatisticsUtils.getTotal(allTimes);
        float averageTime = StatisticsUtils.getAverage(totaleTimes, totalRequests);
        float onTheadAverageTime = averageTime / (float) concurrencyLevel;
//        float tps1 = 1.0E9F * (float) concurrencyLevel * ((float) totalRequests / (float) totaleTimes);
        double tps = 1 / StatisticsUtils.toSecond(averageTime);
        int count_50 = totalRequests / 2;
        int count_66 = totalRequests * 66 / 100;
        int count_75 = totalRequests * 75 / 100;
        int count_80 = totalRequests * 80 / 100;
        int count_90 = totalRequests * 90 / 100;
        int count_95 = totalRequests * 95 / 100;
        int count_98 = totalRequests * 98 / 100;
        int count_99 = totalRequests * 99 / 100;
        int count_100 = totalRequests * 100 / 100;
        long longestRequest = (Long) allTimes.get(allTimes.size() - 1);
        long shortestRequest = (Long) allTimes.get(0);
        StringBuilder view = new StringBuilder();
        view.append("\n\n");
//        view.append(" Concurrency Level:\t").append(concurrencyLevel).append(" --并发数");
//        view.append("\r\n Time taken for tests:\t").append(takes).append(" ms").append(" --测试耗时");
//        view.append("\r\n Complete Requests:\t").append(totalRequests).append(" --完成测试次数");
//        view.append("\r\n Failed Requests:\t").append(stressResult.getFailedRequests()).append(" --失败次数(异常、连接不通...)");
//        view.append("\r\n Requests per second:\t").append(tps).append(" --QPS(TPS) = (并发量 / 平均响应时间)");
//        view.append("\r\n Time per request:\t").append(StatisticsUtils.toMs(averageTime)).append(" ms").append(" --平均耗时");
//        view.append("\r\n Time per request:\t").append(StatisticsUtils.toMs(onTheadAverageTime)).append(" ms (across all concurrent requests)").append("--平均耗时，忽略并发影响");
//        view.append("\r\n Shortest request:\t").append(StatisticsUtils.toMs(shortestRequest)).append(" ms").append(" --最短耗时");
//        view.append("\r\n 在特定时间内提供的请求的百分比 (毫秒) ");
//        view.append("\r\n  50%\t").append(StatisticsUtils.toMs((Long) allTimes.get(count_50))).append("--50% 的耗时在0.005703毫秒以下");
//        view.append("\r\n  50%\t").append(StatisticsUtils.toMs((Long) allTimes.get(count_50)));
//        view.append("\r\n  66%\t").append(StatisticsUtils.toMs((Long) allTimes.get(count_66)));
//        view.append("\r\n  75%\t").append(StatisticsUtils.toMs((Long) allTimes.get(count_75)));
//        view.append("\r\n  80%\t").append(StatisticsUtils.toMs((Long) allTimes.get(count_80)));
//        view.append("\r\n  90%\t").append(StatisticsUtils.toMs((Long) allTimes.get(count_90)));
//        view.append("\r\n  95%\t").append(StatisticsUtils.toMs((Long) allTimes.get(count_95)));
//        view.append("\r\n  98%\t").append(StatisticsUtils.toMs((Long) allTimes.get(count_98)));
//        view.append("\r\n  99%\t").append(StatisticsUtils.toMs((Long) allTimes.get(count_99)));
//        view.append("\r\n 100%\t").append(StatisticsUtils.toMs(longestRequest)).append(" --最长的耗时");

        view.append(" 并发数:\t\t").append(concurrencyLevel);
        view.append("\r\n 测试耗时:\t").append(takes).append(" ms");
        view.append("\r\n 测试次数:\t").append(totalRequests);
        view.append("\r\n 失败次数:\t").append(stressResult.getFailedRequests());
//        view.append("\r\n TPS(QPS):\t").append(tps);
        view.append("\r\n TPS(QPS):\t").append(new BigDecimal(tps).floatValue());
        view.append("\r\n 平均耗时:\t").append(StatisticsUtils.toMs(averageTime)).append(" ms");
//        view.append("\r\n 平均耗时(忽略并发影响):\t").append(StatisticsUtils.toMs(onTheadAverageTime));
        view.append("\r\n 最短耗时:\t").append(StatisticsUtils.toMs(shortestRequest)).append(" ms");
        view.append("\r\n 最长耗时:\t").append(StatisticsUtils.toMs(longestRequest)).append(" ms");
        view.append("\r\n 在特定时间内提供的请求的百分比 (毫秒):");
        view.append("\r\n   50%\t").append(StatisticsUtils.toMs((Long) allTimes.get(count_50)));
        view.append("\r\n   66%\t").append(StatisticsUtils.toMs((Long) allTimes.get(count_66)));
        view.append("\r\n   75%\t").append(StatisticsUtils.toMs((Long) allTimes.get(count_75)));
        view.append("\r\n   80%\t").append(StatisticsUtils.toMs((Long) allTimes.get(count_80)));
        view.append("\r\n   90%\t").append(StatisticsUtils.toMs((Long) allTimes.get(count_90)));
        view.append("\r\n   95%\t").append(StatisticsUtils.toMs((Long) allTimes.get(count_95)));
        view.append("\r\n   98%\t").append(StatisticsUtils.toMs((Long) allTimes.get(count_98)));
        view.append("\r\n   99%\t").append(StatisticsUtils.toMs((Long) allTimes.get(count_99)));

        try {
            writer.write(view.toString());
        } catch (IOException e) {
            log.error("IOException:", e);
        }

    }
}
