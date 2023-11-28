package com.jphoebe.framework.components.util.entity.structure.v2;

import com.jphoebe.framework.components.core.common.serializable.SerializableBean;
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
public class UniversalStructureListV2 extends SerializableBean implements IUniversalStructureV2 {
    private static final long serialVersionUID = -8217251831849377757L;

    private String type;
    private String name;
    private String alias;
    private List<UniversalStructurePropertiesV2> list;

}
