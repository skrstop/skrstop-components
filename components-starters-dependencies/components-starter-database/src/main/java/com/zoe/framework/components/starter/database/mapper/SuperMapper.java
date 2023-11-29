package com.zoe.framework.components.starter.database.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

/**
 * mapper基础扩展类
 *
 * @author 蒋时华
 * @date 2020-06-19 09:30:06
 */
public interface SuperMapper<T> extends BaseMapper<T> {

    /**
     * 物理删除
     *
     * @param ids id集合(逗号分隔)
     * @param ids tableName
     */
    int removePhysicalByIds(@Param("ids") Collection<?> ids, @Param("tableName") String tableName, @Param("idName") String idName);

    /**
     * 物理删除
     *
     * @param id
     * @param tableName
     */
    int removePhysicalById(@Param("id") Serializable id, @Param("tableName") String tableName, @Param("idName") String idName);

    /**
     * 物理删除
     *
     * @param columnMap
     * @param tableName
     */
    int removePhysicalByMap(@Param("columnMap") Map<String, Object> columnMap, @Param("tableName") String tableName);

    /**
     * 物理删除
     *
     * @param customSql
     * @param tableName
     */
    int removePhysicalByCustom(@Param("customSql") String customSql, @Param("ew") Wrapper<T> queryWrapper, @Param("tableName") String tableName);

}
