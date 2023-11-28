package com.jphoebe.framework.components.util.entity.structure.v2;

import com.jphoebe.framework.components.core.common.serializable.SerializableBean;
import com.jphoebe.framework.components.util.entity.structure.v2.value.PropertiesValueListV2;
import com.jphoebe.framework.components.util.entity.structure.v2.value.PropertiesValueObjV2;
import com.jphoebe.framework.components.util.entity.structure.v2.value.PropertiesValueStrV2;
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
public class UniversalStructurePropertiesV2 extends SerializableBean {
    private static final long serialVersionUID = 7509071702432520408L;

    private String key;
    private String name;
    private String alias;
    private String description;
    /**
     * 数据值类型
     * {@link com.jphoebe.framework.components.util.constant.DataInputTypeConst}
     */
    private Byte dataType;
    /*** 单位 */
    private String unitType;
    /*** 单位描述 */
    private String unitTitle;
    /*** 值 */
    private String value;
    /*** 值 */
    private PropertiesValueStrV2 valueStr;
    private PropertiesValueListV2 valueList;
    private PropertiesValueObjV2 valueObj;
    /*** 默认值 */
    private String defaultValue;
    /*** 可选值 */
    private PropertiesValueStrV2 optionalValueStr;
    private PropertiesValueListV2 optionalValueList;
    private PropertiesValueObjV2 optionalValueObj;
    /*** 扩展信息 */
    private Map<String, Object> extra;
    private Object extend;
    /*** 作用域 */
    private String scope;

}
