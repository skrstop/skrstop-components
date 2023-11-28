package com.jphoebe.framework.components.util.entity.list;

import com.jphoebe.framework.components.core.common.serializable.SerializableBean;
import com.jphoebe.framework.components.util.entity.IUniversalStructure;
import com.jphoebe.framework.components.util.entity.UniversalStructureProperties;
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
public class UniversalStructureList extends SerializableBean implements IUniversalStructure {
    private static final long serialVersionUID = -8217251831849377757L;

    private String type;
    private String name;
    private String alias;
    private List<UniversalStructureProperties> list;

}
