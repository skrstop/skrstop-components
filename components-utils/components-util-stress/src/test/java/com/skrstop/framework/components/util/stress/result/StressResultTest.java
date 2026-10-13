package com.skrstop.framework.components.util.stress.result;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

/**
 * @author 蒋时华
 */
public class StressResultTest {

    @Test
    public void testGettersSetters() {
        StressResult result = new StressResult();
        result.setConcurrencyLevel(4);
        Assert.assertEquals(4, result.getConcurrencyLevel());

        result.setTotalRequests(100);
        Assert.assertEquals(100, result.getTotalRequests());

        result.setTestsTakenTime(1234L);
        Assert.assertEquals(1234L, result.getTestsTakenTime());

        result.setFailedRequests(2);
        Assert.assertEquals(2, result.getFailedRequests());

        result.setAllTimes(Arrays.asList(1L, 2L));
        Assert.assertEquals(Arrays.asList(1L, 2L), result.getAllTimes());

        result.setWorkers(Collections.emptyList());
        Assert.assertTrue(result.getWorkers().isEmpty());
    }

}
