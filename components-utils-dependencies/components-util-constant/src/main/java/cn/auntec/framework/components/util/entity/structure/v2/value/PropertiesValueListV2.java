package cn.auntec.framework.components.util.entity.structure.v2.value;

import cn.auntec.framework.components.core.common.serializable.SerializableBean;
import cn.auntec.framework.components.util.entity.structure.v2.PropertiesValueV2;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

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
public class PropertiesValueListV2 extends SerializableBean implements PropertiesValueV2 {
    private static final long serialVersionUID = 7509071702432520408L;

    private List<ValueItemV2> value;

}
