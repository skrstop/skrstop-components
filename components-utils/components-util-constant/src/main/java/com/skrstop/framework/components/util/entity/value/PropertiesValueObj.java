package com.skrstop.framework.components.util.entity.value;

import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.LinkedHashMap;

/**
 * @author 蒋时华
 * @date 2021-04-21 10:19:53
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class PropertiesValueObj implements Serializable, PropertiesValue {
    private static final long serialVersionUID = 7509071702432520408L;

    private LinkedHashMap<String, ValueItem> value;

}
