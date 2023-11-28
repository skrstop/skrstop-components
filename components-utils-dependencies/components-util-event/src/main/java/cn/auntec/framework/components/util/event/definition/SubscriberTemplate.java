package cn.auntec.framework.components.util.event.definition;

import cn.auntec.framework.components.util.event.core.Subscriber;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 蒋时华
 * @date 2020-05-20 10:16:53
 */
@Slf4j
@SuppressWarnings("all")
public class SubscriberTemplate extends Subscriber<EventTemplate> {

    public SubscriberTemplate() {
    }

    public SubscriberTemplate(String name) {
        super(name);
    }

    public SubscriberTemplate(boolean sync) {
        super(sync);
    }

    public SubscriberTemplate(boolean sync, String name) {
        super(sync, name);
    }

    @Override
    public void onEvent(EventTemplate event) {
        if (event != null) {
            log.debug("eventName: {}", event.getEventName());
        }
        try {
            log.debug("subscriberName: {}", this.getSubscriberName());
            log.debug("init事件：{}", event);
            event.init(event.getEventData());
            log.debug("before事件：{}", event);
            event.before(event.getEventData());
            log.debug("execute事件：{}", event);
            event.execute(event.getEventData());
            log.debug("after事件：{}", event);
            event.after(event.getEventData());
            log.debug("finish事件：{}", event);
            event.finish(event.getEventData());
            log.debug("执行成功回调：{}", event);
            event.onSuccess(event.getEventData());
        } catch (Exception e) {
            log.debug(e.getMessage(), e);
            log.debug("执行失败回调：{}", event);
            event.onFailure(e, event.getEventData());
        }
    }

}
