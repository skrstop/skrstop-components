package com.skrstop.framework.components.example.starters.simple;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;

public class ExampleStartersSimpleApplicationBootstrapTest {

    @Test
    public void testMain() {
        try (MockedStatic<SpringApplication> mocked = Mockito.mockStatic(SpringApplication.class)) {
            ExampleStartersSimpleApplicationBootstrap.main(new String[0]);
            mocked.verify(() -> SpringApplication.run(ExampleStartersSimpleApplicationBootstrap.class, new String[0]));
        }
        Assert.assertTrue(true);
    }
}
