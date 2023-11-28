package com.jphoebe.framework.components.util.entity.tree;

import com.jphoebe.framework.components.util.entity.UniversalStructureProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author 蒋时华
 * @date 2021-04-21 11:16:41
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class UniversalStructureTreeProperties extends UniversalStructureProperties {
    private static final long serialVersionUID = -3070343277336335706L;

    /*** 是否有自子节点 */
    private boolean hasChildren;
    /*** 子节点 */
    private List<UniversalStructureTreeProperties> childProperties;

}
