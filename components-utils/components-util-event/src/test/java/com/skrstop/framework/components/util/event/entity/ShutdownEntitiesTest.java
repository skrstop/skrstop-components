package com.skrstop.framework.components.util.event.entity;

import com.skrstop.framework.components.util.event.core.shutdown.ShutdownTaskEvent;
import com.skrstop.framework.components.util.event.entity.ShutdownTaskEventResult.ShutdownTaskEventResultItem;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;

/**
 * @author 蒋时华
 */
public class ShutdownEntitiesTest {

    @Test
    public void testCommand() {
        ShutdownTaskEventCommand command = ShutdownTaskEventCommand.builder().param("p").build();
        Assert.assertEquals("p", command.getParam());
        command.setParam("p2");
        Assert.assertEquals("p2", command.getParam());
        Assert.assertNotNull(new ShutdownTaskEventCommand());
        Assert.assertNotNull(new ShutdownTaskEventCommand("p3"));
    }

    @Test
    public void testResult() {
        ShutdownTaskEventResult result = ShutdownTaskEventResult.builder()
                .shutdown(true)
                .finish(true)
                .customQueryResult("q")
                .taskResult(Collections.emptyList())
                .build();
        Assert.assertTrue(result.isShutdown());
        Assert.assertTrue(result.isFinish());
        Assert.assertEquals("q", result.getCustomQueryResult());
        Assert.assertTrue(result.getTaskResult().isEmpty());
        result.setShutdown(false);
        result.setFinish(false);
        result.setCustomQueryResult(null);
        result.setTaskResult(null);
        Assert.assertFalse(result.isShutdown());
    }

    @Test
    public void testResultItem() {
        ShutdownTaskEventResultItem item = ShutdownTaskEventResultItem.builder()
                .name("task")
                .currentTotalTask(10)
                .currentHandlingTask(2)
                .currentEndTask(6)
                .currentWaitingTask(2)
                .build();
        Assert.assertEquals("task", item.getName());
        Assert.assertEquals(Integer.valueOf(10), item.getCurrentTotalTask());
        Assert.assertEquals(Integer.valueOf(2), item.getCurrentHandlingTask());
        Assert.assertEquals(Integer.valueOf(6), item.getCurrentEndTask());
        Assert.assertEquals(Integer.valueOf(2), item.getCurrentWaitingTask());
        item.setName("t2");
        Assert.assertEquals("t2", item.getName());
    }

    @Test
    public void testShutdownTaskEvent() {
        ShutdownTaskEvent event = new ShutdownTaskEvent() {
            @Override
            public ShutdownTaskEventResult onExecute(ShutdownTaskEventCommand request) {
                return ShutdownTaskEventResult.builder().shutdown(true).build();
            }

            @Override
            public ShutdownTaskEventResult getEventData(ShutdownTaskEventCommand request) {
                return null;
            }
        };
        Assert.assertEquals("shutdownTask", event.getEventName());
        Assert.assertNull(event.getEventData());
        Assert.assertNull(event.getEventData(new ShutdownTaskEventCommand("x")));
        ShutdownTaskEventResult result = event.onExecute(new ShutdownTaskEventCommand("p"));
        Assert.assertTrue(result.isShutdown());
    }

}
