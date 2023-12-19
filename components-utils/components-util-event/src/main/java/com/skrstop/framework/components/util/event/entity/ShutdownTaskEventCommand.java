package com.skrstop.framework.components.util.event.entity;


import com.skrstop.framework.components.core.common.serializable.SerializableBean;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * @author 蒋时华
 * @date 2022-01-20 14:03:09
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class ShutdownTaskEventCommand extends SerializableBean {
    private static final long serialVersionUID = 1655869834939632281L;

    private String param;

}
