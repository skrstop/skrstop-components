package com.jphoebe.framework.components.util.entity.structure.v2;

import com.jphoebe.framework.components.core.common.serializable.SerializableBean;
import com.jphoebe.framework.components.util.entity.tree.UniversalStructureTreeProperties;
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
public class UniversalStructureTreeV2 extends SerializableBean implements IUniversalStructureV2 {
    private static final long serialVersionUID = -8217251831849377757L;

    private String type;
    private String name;
    private String alias;
    private List<UniversalStructureTreeProperties> tree;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)
    public static class UniversalStructureTreePropertiesV2 extends UniversalStructurePropertiesV2 {
        private static final long serialVersionUID = -3070343277336335706L;

        /*** 是否有自子节点 */
        private boolean hasChildren;
        /*** 子节点 */
        private List<UniversalStructureTreePropertiesV2> childProperties;

    }

}
