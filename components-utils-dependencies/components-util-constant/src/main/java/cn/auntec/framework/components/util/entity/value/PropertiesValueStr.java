package cn.auntec.framework.components.util.entity.value;

import cn.auntec.framework.components.core.common.serializable.SerializableBean;
import lombok.*;
import lombok.experimental.Accessors;

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
public class PropertiesValueStr extends SerializableBean implements PropertiesValue {
    private static final long serialVersionUID = 7509071702432520408L;

    private String value;

}
