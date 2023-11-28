package com.jphoebe.framework.components.util.event.core.shutdown;

import com.jphoebe.framework.components.util.event.core.CallbackResultReturn;
import com.jphoebe.framework.components.util.event.core.EventResult;
import com.jphoebe.framework.components.util.event.entity.ShutdownTaskEventCommand;
import com.jphoebe.framework.components.util.event.entity.ShutdownTaskEventResult;

/**
 * @author 蒋时华
 * @date 2022-01-20 14:00:26
 */
@SuppressWarnings("all")
public abstract class ShutdownTaskEvent implements CallbackResultReturn<ShutdownTaskEventCommand, ShutdownTaskEventResult>, EventResult<ShutdownTaskEventCommand, ShutdownTaskEventResult> {

    private final String EVENT_NAME = "shutdownTask";

    @Override
    public String getEventName() {
        return EVENT_NAME;
    }

    /**
     * shutdown逻辑处理
     *
     * @param command
     */
    @Override
    public abstract ShutdownTaskEventResult onExecute(ShutdownTaskEventCommand request);

}
