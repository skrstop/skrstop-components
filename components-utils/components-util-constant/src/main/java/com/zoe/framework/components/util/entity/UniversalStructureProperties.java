package com.zoe.framework.components.util.entity;

import com.zoe.framework.components.core.common.serializable.SerializableBean;
import com.zoe.framework.components.util.entity.value.PropertiesValueList;
import com.zoe.framework.components.util.entity.value.PropertiesValueStr;
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
public class UniversalStructureProperties extends SerializableBean {
    private static final long serialVersionUID = 7509071702432520408L;

    private String key;
    private String name;
    private String alias;
    private String description;
    /**
     * 数据值类型
     * {@link com.zoe.framework.components.util.constant.DataInputTypeConst}
     */
    private Byte dataType;
    /*** 单位 */
    private String unitType;
    /*** 单位描述 */
    private String unitTitle;
    /*** 值 */
    private PropertiesValueStr value;
    private PropertiesValueList valueList;
    /*** 默认值 */
    private String defaultValue;
    /*** 可选值 */
    private PropertiesValueStr optionalValueStr;
    private PropertiesValueList optionalValueList;
    /*** 扩展信息 */
    private Map<String, Object> extra;
    private Object extend;
    /*** 作用域 */
    private String scope;


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Accessors(chain = true)
    public static class ValueListItem extends SerializableBean {
        private static final long serialVersionUID = -2586440696163783644L;

        private String value;
        private String remark;

    }

}
