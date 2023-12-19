package com.skrstop.framework.components.starter.web.event;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 蒋时华
 * @date 2022-01-20 14:15:19
 */
@RestController
@RequestMapping("${server.status.path:/event/status/up}")
@SuppressWarnings("all")
public class StatusEventController {

    @RequestMapping
    public Boolean up() throws Throwable {
        return true;
    }

}
