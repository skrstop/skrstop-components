package com.skrstop.framework.components.util.executor.retry.backoff;

import org.junit.Assert;
import org.junit.Test;

import java.time.Duration;

/**
 * @author 蒋时华
 */
public class BackoffStrategyTest {

    @Test
    public void testFixedBackoff() {
        FixedBackoffStrategy strategy = new FixedBackoffStrategy();
        Assert.assertEquals(Duration.ofMillis(100), strategy.getDurationToWait(3, Duration.ofMillis(100)));
    }

    @Test
    public void testNoWaitBackoff() {
        Assert.assertEquals(Duration.ZERO, new NoWaitBackoffStrategy().getDurationToWait(3, Duration.ofSeconds(10)));
    }

    @Test
    public void testExponentialBackoff() {
        ExponentialBackoffStrategy strategy = new ExponentialBackoffStrategy();
        Assert.assertEquals(Duration.ofMillis(100), strategy.getDurationToWait(1, Duration.ofMillis(100)));
        Assert.assertEquals(Duration.ofMillis(200), strategy.getDurationToWait(2, Duration.ofMillis(100)));
        Assert.assertEquals(Duration.ofMillis(400), strategy.getDurationToWait(3, Duration.ofMillis(100)));
    }

    @Test
    public void testFibonacciBackoff() {
        FibonacciBackoffStrategy strategy = new FibonacciBackoffStrategy();
        Assert.assertEquals(Duration.ofMillis(0), strategy.getDurationToWait(0, Duration.ofMillis(100)));
        Assert.assertEquals(Duration.ofMillis(100), strategy.getDurationToWait(1, Duration.ofMillis(100)));
        // index 3 -> fib 2
        Assert.assertEquals(Duration.ofMillis(200), strategy.getDurationToWait(3, Duration.ofMillis(100)));
        // index far beyond list -> capped
        Duration capped = strategy.getDurationToWait(1000, Duration.ofMillis(100));
        Assert.assertEquals(Duration.ofMillis(100 * strategy.getFibonacciNumbers().get(FibonacciBackoffStrategy.MAX_NUM_OF_FIB_NUMBERS - 1)), capped);
        Assert.assertEquals(27, strategy.getFibonacciNumbers().size());
    }

    @Test
    public void testRandomBackoff() {
        RandomBackoffStrategy strategy = new RandomBackoffStrategy(5);
        Duration d = strategy.getDurationToWait(1, Duration.ofMillis(100));
        Assert.assertTrue(d.toMillis() % 100 == 0);
        Assert.assertTrue(d.toMillis() / 100 < 5);
    }

    @Test
    public void testRandomExponentialBackoff() {
        RandomExponentialBackoffStrategy strategy = new RandomExponentialBackoffStrategy();
        Duration d = strategy.getDurationToWait(2, Duration.ofMillis(100));
        Assert.assertTrue(d.toMillis() % 100 == 0);
    }

}
