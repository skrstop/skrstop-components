package com.skrstop.framework.components.util.entity.list;

import com.skrstop.framework.components.util.entity.IUniversalStructure;
import com.skrstop.framework.components.util.entity.UniversalStructureProperties;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
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
public class UniversalStructureList implements Serializable, IUniversalStructure {
    @Serial
    private static final long serialVersionUID = -8217251831849377757L;

    private String type;
    private String name;
    private String alias;
    private List<UniversalStructureProperties> list;

}
