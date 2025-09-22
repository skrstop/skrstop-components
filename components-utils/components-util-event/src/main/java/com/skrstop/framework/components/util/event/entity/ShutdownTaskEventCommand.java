package com.skrstop.framework.components.util.event.entity;


import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

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
public class ShutdownTaskEventCommand implements Serializable {
    @Serial
    private static final long serialVersionUID = 1655869834939632281L;

    private String param;

}
