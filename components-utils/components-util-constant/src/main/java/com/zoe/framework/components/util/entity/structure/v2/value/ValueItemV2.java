package com.zoe.framework.components.util.entity.structure.v2.value;

import com.zoe.framework.components.core.common.serializable.SerializableBean;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Map;

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
public class ValueItemV2 extends SerializableBean {
    private static final long serialVersionUID = 7509071702432520408L;

    private String key;
    private String name;
    private String description;
    private String value;
    /*** 单位 */
    private String unitType;
    /*** 单位描述 */
    private String unitTitle;
    /*** 扩展信息 */
    private Map<String, Object> extra;
    private Object extend;
    /*** 作用域 */
    private String scope;

}
