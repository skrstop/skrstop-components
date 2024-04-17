package com.skrstop.framework.components.util.stress;

import com.skrstop.framework.components.util.stress.result.StressResult;
import com.skrstop.framework.components.util.stress.task.StressTask;
import org.junit.Test;

/**
 * @author 蒋时华
 * @date 2023-11-30 14:07:23
 */
public class StressStoreUtilTest {

    @Test
    public void test() {
        StressResult stressResult = StressStoreUtil.test(10, 10, new StressTask() {
            @Override
            public void doTask() throws Exception {
                /**
                 *  your task code
                 */
                Thread.sleep(10);
                System.out.println(100);
            }
        });
        String str = StressStoreUtil.format(stressResult);
        System.out.println(str);
    }

    @Test
    public void test2() {
        // demo 2
        StressStoreUtil.testAndPrint(10, 10, new StressTask() {
            @Override
            public void doTask() throws Exception {
                /**
                 *  your task code
                 */
                Thread.sleep(10);
                System.out.println(100);
            }
        });
    }

}
