package com.skrstop.framework.components.util.executor.retry.config;

import com.skrstop.framework.components.util.executor.retry.backoff.*;
import com.skrstop.framework.components.util.executor.retry.exception.InvalidRetryConfigException;
import org.junit.Assert;
import org.junit.Test;

import java.time.Duration;
import java.util.function.Function;

import static java.time.temporal.ChronoUnit.SECONDS;

/**
 * @author 蒋时华
 */
public class RetryConfigBuilderTest {

    @Test
    public void testPresetBuilders() {
        Assert.assertNotNull(new RetryConfigBuilder().fixedBackoff5Tries10Sec().build());
        Assert.assertNotNull(new RetryConfigBuilder().exponentialBackoff5Tries5Sec().build());
        Assert.assertNotNull(new RetryConfigBuilder().fiboBackoff7Tries5Sec().build());
        Assert.assertNotNull(new RetryConfigBuilder().randomExpBackoff10Tries60Sec().build());
        Assert.assertNotNull(RetryConfigBuilderTest.class); // keep imports used
        Assert.assertNotNull(new Object());
    }

    @Test
    public void testBackoffStrategies() {
        RetryConfig fixed = new RetryConfigBuilder().withFixedBackoff().withMaxNumberOfTries(2).withDelayBetweenTries(Duration.ofMillis(1)).build();
        Assert.assertTrue(fixed.getBackoffStrategy() instanceof FixedBackoffStrategy);

        RetryConfig exp = new RetryConfigBuilder().withExponentialBackoff().withMaxNumberOfTries(2).withDelayBetweenTries(Duration.ofMillis(1)).build();
        Assert.assertTrue(exp.getBackoffStrategy() instanceof ExponentialBackoffStrategy);

        RetryConfig fib = new RetryConfigBuilder().withFibonacciBackoff().withMaxNumberOfTries(2).withDelayBetweenTries(Duration.ofMillis(1)).build();
        Assert.assertTrue(fib.getBackoffStrategy() instanceof FibonacciBackoffStrategy);

        RetryConfig nowait = new RetryConfigBuilder().withNoWaitBackoff().withMaxNumberOfTries(2).withDelayBetweenTries(Duration.ofMillis(1)).build();
        Assert.assertTrue(nowait.getBackoffStrategy() instanceof NoWaitBackoffStrategy);

        RetryConfig random = new RetryConfigBuilder().withRandomBackoff().withMaxNumberOfTries(2).withDelayBetweenTries(Duration.ofMillis(1)).build();
        Assert.assertTrue(random.getBackoffStrategy() instanceof RandomBackoffStrategy);

        RetryConfig randomExp = new RetryConfigBuilder().withRandomExponentialBackoff().withMaxNumberOfTries(2).withDelayBetweenTries(Duration.ofMillis(1)).build();
        Assert.assertTrue(randomExp.getBackoffStrategy() instanceof RandomExponentialBackoffStrategy);

        RetryConfig custom = new RetryConfigBuilder()
                .withBackoffStrategy(new FixedBackoffStrategy())
                .withMaxNumberOfTries(2)
                .withDelayBetweenTries(Duration.ofMillis(1))
                .build();
        Assert.assertNotNull(custom.getBackoffStrategy());
    }

    @Test
    public void testSecondBackoffThrows() {
        RetryConfigBuilder builder = new RetryConfigBuilder().withFixedBackoff();
        try {
            builder.withExponentialBackoff();
            Assert.fail("should throw");
        } catch (InvalidRetryConfigException ignored) {
        }
    }

    @Test
    public void testValidationDisabledAllowsAnything() {
        RetryConfigBuilder builder = new RetryConfigBuilder(false);
        Assert.assertFalse(builder.isValidationEnabled());
        builder.setValidationEnabled(false);
        Assert.assertFalse(builder.isValidationEnabled());
        builder.withFixedBackoff().withExponentialBackoff();
        Assert.assertNotNull(builder.build().getBackoffStrategy());
    }

    @Test
    public void testExceptionStrategies() {
        Assert.assertTrue(new RetryConfigBuilder().retryOnAnyException().withMaxNumberOfTries(2).withDelayBetweenTries(Duration.ofMillis(1)).withFixedBackoff().build().isRetryOnAnyException());
        RetryConfig failAny = new RetryConfigBuilder().failOnAnyException().withMaxNumberOfTries(2).withDelayBetweenTries(Duration.ofMillis(1)).withFixedBackoff().build();
        Assert.assertFalse(failAny.isRetryOnAnyException());
        Assert.assertTrue(new RetryConfigBuilder().retryOnSpecificExceptions(IllegalStateException.class).withMaxNumberOfTries(2).withDelayBetweenTries(Duration.ofMillis(1)).withFixedBackoff().build().getRetryOnSpecificExceptions().contains(IllegalStateException.class));
        Assert.assertTrue(new RetryConfigBuilder().retryOnAnyExceptionExcluding(IllegalStateException.class).withMaxNumberOfTries(2).withDelayBetweenTries(Duration.ofMillis(1)).withFixedBackoff().build().getRetryOnAnyExceptionExcluding().contains(IllegalStateException.class));
        Assert.assertTrue(new RetryConfigBuilder().retryOnCausedBy().withMaxNumberOfTries(2).withDelayBetweenTries(Duration.ofMillis(1)).withFixedBackoff().build().shouldRetryOnCausedBy());
    }

    @Test
    public void testSecondExceptionStrategyThrows() {
        RetryConfigBuilder builder = new RetryConfigBuilder().retryOnAnyException();
        try {
            builder.failOnAnyException();
            Assert.fail("should throw");
        } catch (InvalidRetryConfigException ignored) {
        }
    }

    @Test
    public void testValueStrategies() {
        RetryConfig retryOnValue = new RetryConfigBuilder().retryOnReturnValue("bad").withMaxNumberOfTries(2).withDelayBetweenTries(Duration.ofMillis(1)).withFixedBackoff().build();
        Assert.assertTrue(retryOnValue.shouldRetryOnValue());
        Assert.assertTrue(retryOnValue.getValuesToRetryOn().contains("bad"));

        RetryConfig multi = new RetryConfigBuilder().retryOnReturnValues("a", "b").withMaxNumberOfTries(2).withDelayBetweenTries(Duration.ofMillis(1)).withFixedBackoff().build();
        Assert.assertTrue(multi.getValuesToRetryOn().contains("b"));

        RetryConfig excluding = new RetryConfigBuilder().retryOnReturnValuesExcluding("good").withMaxNumberOfTries(2).withDelayBetweenTries(Duration.ofMillis(1)).withFixedBackoff().build();
        Assert.assertTrue(excluding.getValuesToExpect().contains("good"));
    }

    @Test
    public void testCustomExceptionLogic() {
        Function<Exception, Boolean> logic = e -> false;
        RetryConfig config = new RetryConfigBuilder().retryOnCustomExceptionLogic(logic).withMaxNumberOfTries(2).withDelayBetweenTries(Duration.ofMillis(1)).withFixedBackoff().build();
        Assert.assertSame(logic, config.getCustomRetryOnLogic());
    }

    @Test
    public void testCustomLogicWithBuiltInThrows() {
        RetryConfigBuilder builder = new RetryConfigBuilder().retryOnAnyException().retryOnCustomExceptionLogic(e -> false).withMaxNumberOfTries(2).withFixedBackoff();
        try {
            builder.build();
            Assert.fail("should throw");
        } catch (InvalidRetryConfigException ignored) {
        }
    }

    @Test
    public void testMaxNumberOfTriesValidation() {
        try {
            new RetryConfigBuilder().withMaxNumberOfTries(0);
            Assert.fail("should throw");
        } catch (InvalidRetryConfigException ignored) {
        }
        try {
            new RetryConfigBuilder().withMaxNumberOfTries(1).withMaxNumberOfTries(2);
            Assert.fail("should throw");
        } catch (InvalidRetryConfigException ignored) {
        }
        Assert.assertEquals(Integer.MAX_VALUE, new RetryConfigBuilder().retryIndefinitely().withFixedBackoff().withDelayBetweenTries(Duration.ofMillis(1)).build().getMaxNumberOfTries().intValue());
        try {
            new RetryConfigBuilder().withMaxNumberOfTries(1).retryIndefinitely();
            Assert.fail("should throw");
        } catch (InvalidRetryConfigException ignored) {
        }
    }

    @Test
    public void testDelayValidation() {
        try {
            new RetryConfigBuilder().withDelayBetweenTries(Duration.ofMillis(-1));
            Assert.fail("should throw");
        } catch (InvalidRetryConfigException ignored) {
        }
        RetryConfig config = new RetryConfigBuilder().withMaxNumberOfTries(2).withDelayBetweenTries(5L, SECONDS).withFixedBackoff().build();
        Assert.assertEquals(5000L, config.getDelayBetweenRetries().toMillis());
    }

    @Test
    public void testMissingBackoffAndTries() {
        try {
            new RetryConfigBuilder().build();
            Assert.fail("should throw");
        } catch (InvalidRetryConfigException ignored) {
        }
        try {
            new RetryConfigBuilder().withFixedBackoff().build();
            Assert.fail("should throw");
        } catch (InvalidRetryConfigException ignored) {
        }
    }

    @Test
    public void testBackoffValidateConfigRequiresDelay() {
        for (BackoffStrategy strategy : new BackoffStrategy[]{
                new FixedBackoffStrategy(), new ExponentialBackoffStrategy(),
                new FibonacciBackoffStrategy(), new RandomBackoffStrategy(),
                new RandomExponentialBackoffStrategy()}) {
            try {
                strategy.validateConfig(new RetryConfig(false, null, null, 2, null, strategy, null, null, false, null, false));
                Assert.fail("should throw");
            } catch (InvalidRetryConfigException ignored) {
            }
        }
    }

    @Test
    public void testToString() {
        RetryConfig config = new RetryConfigBuilder().fixedBackoff5Tries10Sec().build();
        Assert.assertTrue(config.toString().contains("RetryConfig"));
    }


    @Test
    public void testExceptionStrategyWithValidationDisabled() {
        RetryConfigBuilder builder = new RetryConfigBuilder(false);
        builder.retryOnAnyException();
        Assert.assertTrue(builder.build().isRetryOnAnyException());
    }

}
