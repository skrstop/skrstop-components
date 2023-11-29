package com.zoe.framework.components.util.entity.structure.v2.value;

import com.zoe.framework.components.core.common.serializable.SerializableBean;
import com.zoe.framework.components.util.entity.structure.v2.PropertiesValueV2;
import lombok.*;
import lombok.experimental.Accessors;

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
public class PropertiesValueObjV2 extends SerializableBean implements PropertiesValueV2 {
    private static final long serialVersionUID = 7509071702432520408L;

    private LinkedHashMap<String, ValueItemV2> value;

}
