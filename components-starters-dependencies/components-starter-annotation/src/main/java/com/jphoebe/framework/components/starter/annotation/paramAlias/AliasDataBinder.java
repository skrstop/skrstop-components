package com.jphoebe.framework.components.starter.annotation.paramAlias;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.web.bind.ServletRequestDataBinder;

import javax.servlet.ServletRequest;
import java.lang.reflect.Field;

/**
 * @author: wangzh
 * @date: 2021/3/11 19:44
 * @description: desc...
 */
public class AliasDataBinder extends ServletRequestDataBinder {

    public AliasDataBinder(Object target, String objectName) {
        super(target, objectName);
    }

    /**
     * 复写addBindValues方法
     *
     * @param mpvs    这里面存的就是请求参数的key-value对
     * @param request 请求本身, 这里没有用到
     */
    @Override
    protected void addBindValues(MutablePropertyValues mpvs, ServletRequest request) {
        super.addBindValues(mpvs, request);
        if (mpvs.isEmpty()) {
            return;
        }
        // 处理要绑定参数的对象
        Class<?> targetClass = getTarget().getClass();
        // 获取对象的所有字段(拿到Test类的字段)
        Field[] fields = targetClass.getDeclaredFields();
        // 处理所有字段
        for (Field field : fields) {
            // 原始字段上的注解
            RequestParamAlias valueFromAnnotation = field.getAnnotation(RequestParamAlias.class);
            // 若参数中包含原始字段或者字段没有别名注解, 则跳过该字段
            if (mpvs.contains(field.getName()) || valueFromAnnotation == null) {
                continue;
            }
            // 参数中没有原始字段且字段上有别名注解, 则依次取别名列表中的别名, 在参数中最先找到的别名的值赋值给原始字段
            for (String alias : valueFromAnnotation.value()) {
                // 若参数中包含该别名, 则把别名的值赋值给原始字段
                if (mpvs.contains(alias)) {
                    // 给原始字段赋值
                    mpvs.add(field.getName(), mpvs.getPropertyValue(alias).getValue());
                    // 跳出循环防止取其它别名
                    break;
                }
            }
        }
    }


}
