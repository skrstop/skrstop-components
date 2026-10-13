package com.skrstop.framework.components.util.stress.result;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;

/**
 * @author 蒋时华
 */
public class SimpleResultFormaterTest {

    private StressResult buildResult() {
        StressResult result = new StressResult();
        result.setConcurrencyLevel(2);
        result.setTotalRequests(2);
        result.setTestsTakenTime(2000000L);
        result.setFailedRequests(0);
        result.setAllTimes(Arrays.asList(1000000L, 3000000L));
        return result;
    }

    @Test
    public void testFormat() {
        StringBuilder sb = new StringBuilder();
        java.io.StringWriter sw = new java.io.StringWriter();
        new SimpleResultFormater().format(buildResult(), sw);
        String out = sw.toString();
        Assert.assertTrue(out.contains("并发数"));
        Assert.assertTrue(out.contains("50%"));
    }

    @Test
    public void testFormatWriterThrows() {
        Writer throwing = new Writer() {
            @Override
            public void write(char[] cbuf, int off, int len) throws IOException {
                throw new IOException("boom");
            }

            @Override
            public void flush() {
            }

            @Override
            public void close() {
            }
        };
        // IOException 被捕获并记录，不向外抛出
        new SimpleResultFormater().format(buildResult(), throwing);
    }

}
