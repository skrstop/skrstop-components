package com.jphoebe.framework.components.util.value.bean;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Map;

/**
 * @author 蒋时华
 * @date 2020-06-08 17:57:25
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@Accessors(chain = true)
public class CopyOptions extends cn.hutool.core.bean.copier.CopyOptions {
    private static final long serialVersionUID = 8250541988462117776L;

    /**
     * 限制的类或接口，必须为目标对象的实现接口或父类，用于限制拷贝的属性，例如一个类我只想复制其父类的一些属性，就可以将editable设置为父类
     */
    protected Class<?> editable;
    /**
     * 是否忽略空值，当源对象的值为null时，true: 忽略而不注入此值，false: 注入null
     */
    protected boolean ignoreNullValue;
    /**
     * 忽略的目标对象中属性列表，设置一个属性列表，不拷贝这些属性值
     */
    protected String[] ignoreProperties;
    /**
     * 是否忽略字段注入错误
     */
    protected boolean ignoreError;
    /**
     * 是否忽略字段大小写
     */
    protected boolean ignoreCase;
    /**
     * 拷贝属性的字段映射，用于不同的属性之前拷贝做对应表用
     */
    protected Map<String, String> fieldMapping;

    public CopyOptions(Class<?> editable
            , boolean ignoreNullValue
            , String[] ignoreProperties
            , boolean ignoreError
            , boolean ignoreCase
            , Map<String, String> fieldMapping
    ) {
        super(editable, ignoreNullValue, ignoreProperties);
        super.setIgnoreError(ignoreError);
        super.setIgnoreCase(ignoreCase);
        super.setFieldMapping(fieldMapping);
    }
}
