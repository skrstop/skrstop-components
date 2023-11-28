package cn.auntec.framework.components.starter.web.event;

import cn.auntec.framework.components.core.exception.AuntecRuntimeException;
import cn.auntec.framework.components.util.event.core.shutdown.ShutdownTaskEvent;
import cn.auntec.framework.components.util.event.entity.ShutdownTaskEventCommand;
import cn.auntec.framework.components.util.event.entity.ShutdownTaskEventResult;
import cn.auntec.framework.components.util.value.data.ObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 蒋时华
 * @date 2022-01-20 14:15:19
 */
@RestController
@RequestMapping("${server.shutdown.task.path:/event/shutdown/task}")
@SuppressWarnings("all")
public class ShutdownEventController {

    @Autowired(required = false)
    private ShutdownTaskEvent shutdownTaskEvent;

    @RequestMapping(value = "/support")
    public Boolean support(ShutdownTaskEventCommand command) throws Throwable {
        if (ObjectUtil.isNull(shutdownTaskEvent)) {
            return false;
        }
        return true;
    }

    @RequestMapping(value = "/command")
    public void shutdown(ShutdownTaskEventCommand command) throws Throwable {
        if (ObjectUtil.isNull(shutdownTaskEvent)) {
            throw new AuntecRuntimeException("未构建 TaskShutdownEvent 事件处理器");
        }
        shutdownTaskEvent.onExecute(command);
    }

    @RequestMapping(value = "/result")
    public ShutdownTaskEventResult shutdownResult(ShutdownTaskEventCommand command) throws Throwable {
        if (ObjectUtil.isNull(shutdownTaskEvent)) {
            throw new AuntecRuntimeException("未构建 TaskShutdownEvent 事件处理器");
        }
        return shutdownTaskEvent.getEventData(command);
    }

}
