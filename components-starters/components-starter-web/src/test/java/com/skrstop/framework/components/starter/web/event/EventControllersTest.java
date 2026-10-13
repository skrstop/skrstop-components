package com.skrstop.framework.components.starter.web.event;

import com.skrstop.framework.components.core.exception.SkrstopRuntimeException;
import com.skrstop.framework.components.util.event.core.shutdown.ShutdownTaskEvent;
import com.skrstop.framework.components.util.event.entity.ShutdownTaskEventCommand;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

public class EventControllersTest {

    @Test
    public void testStatusEventController() throws Throwable {
        StatusEventController controller = new StatusEventController();
        Assert.assertTrue(controller.up());
    }

    @Test
    public void testShutdownEventControllerNoEvent() throws Throwable {
        ShutdownEventController controller = new ShutdownEventController();
        Assert.assertFalse(controller.support(new ShutdownTaskEventCommand()));
        try {
            controller.shutdown(new ShutdownTaskEventCommand());
            Assert.fail();
        } catch (SkrstopRuntimeException expected) {
        }
        try {
            controller.shutdownResult(new ShutdownTaskEventCommand());
            Assert.fail();
        } catch (SkrstopRuntimeException expected) {
        }
    }

    @Test
    public void testShutdownEventControllerWithEvent() throws Throwable {
        ShutdownEventController controller = new ShutdownEventController();
        ShutdownTaskEvent event = Mockito.mock(ShutdownTaskEvent.class);
        Mockito.when(event.getEventData(Mockito.any())).thenReturn(null);
        ReflectionTestUtils.setField(controller, "shutdownTaskEvent", event);
        Assert.assertTrue(controller.support(new ShutdownTaskEventCommand()));
        controller.shutdown(new ShutdownTaskEventCommand());
        Mockito.verify(event).onExecute(Mockito.any());
        Assert.assertNull(controller.shutdownResult(new ShutdownTaskEventCommand()));
        Mockito.verify(event).getEventData(Mockito.any());
    }
}
