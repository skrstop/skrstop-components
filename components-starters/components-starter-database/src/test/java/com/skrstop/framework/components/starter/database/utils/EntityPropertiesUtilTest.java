package com.skrstop.framework.components.starter.database.utils;

import cn.hutool.core.lang.Pair;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.skrstop.framework.components.core.exception.defined.illegal.NotSupportedException;
import com.skrstop.framework.components.starter.database.annotation.property.*;
import com.skrstop.framework.components.starter.database.entity.AbstractBaseEntity;
import com.skrstop.framework.components.starter.database.entity.AbstractCreateByTimeBaseEntity;
import com.skrstop.framework.components.starter.database.entity.AbstractUpdateByTimeBaseEntity;
import com.skrstop.framework.components.starter.database.repository.SuperRepository;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class EntityPropertiesUtilTest {

    @TableName("rich_table")
    static class RichEntity extends AbstractCreateByTimeBaseEntity {
        @TableId("id_col")
        private Long id;
        @PropertyCreator
        private String creator;
        @PropertyUpdateBy
        private Long updateBy;
        @PropertyUpdateTime
        private LocalDateTime updateTime;
        @PropertyUpdater
        private String updater;
        @PropertyDeleted
        @TableLogic
        private Boolean deleted;
        @PropertyVersion
        @Version
        private Long version;
        private String plain;
    }

    @TableName("kitchen_sink")
    static class KitchenSinkEntity {
        @PropertyId
        private Long pid;
        @TableId
        private Long bareId;
        @TableField
        private Long bareField;
        @Version
        private Long versionOnly;
        @TableLogic
        private Boolean logicOnly;
        @PropertyDeleted
        private Boolean deletedOnly;
    }

    @PropertyTableName("custom_table")
    static class NamedEntity extends AbstractBaseEntity {
        @TableId
        private Long id;
        @TableField("plain_col")
        private String plain;
    }

    @BeforeClass
    public static void initTableInfo() {
        MapperBuilderAssistant assistant = new MapperBuilderAssistant(new MybatisConfiguration(), "");
        TableInfoHelper.initTableInfo(assistant, RichEntity.class);
        TableInfoHelper.initTableInfo(assistant, NamedEntity.class);
    }

    @Test
    public void defaultConstructor() {
        Assert.assertNotNull(new EntityPropertiesUtil());
    }

    @Test
    public void tablePropertiesKitchenSink() {
        Map<Class<?>, Map<String, Pair<String, Class<?>>>> cache =
                EntityPropertiesUtil.tableProperties(true, KitchenSinkEntity.class);
        Assert.assertTrue(cache.containsKey(PropertyId.class));
        Assert.assertEquals("pid", cache.get(PropertyId.class).get("pid").getKey());
        // @TableId 无 value -> 字段名作为列名
        Assert.assertEquals("bareId", cache.get(PropertyId.class).get("bareId").getKey());
        // @TableField 无 value 的 bareField 无属性注解，不进入任何分类缓存
        Assert.assertFalse(cache.get(PropertyId.class).containsKey("bareField"));
        Assert.assertTrue(cache.containsKey(PropertyVersion.class));
        Assert.assertTrue(cache.containsKey(PropertyDeleted.class));
    }

    @Test
    public void tablePropertiesRich() {
        Map<Class<?>, Map<String, Pair<String, Class<?>>>> cache =
                EntityPropertiesUtil.tableProperties(true, RichEntity.class);
        // 表名: @TableName 优先
        Assert.assertEquals("rich_table", EntityPropertiesUtil.getTableDbName(cache));
        // id: @TableId.value 优先
        Assert.assertEquals("id_col", EntityPropertiesUtil.getColumnDbNameIdAndValid(cache));
        Assert.assertEquals("id", EntityPropertiesUtil.getColumnPropertyNameIdAndValid(cache));
        Assert.assertEquals(1, EntityPropertiesUtil.getColumnPropertyNames(cache, PropertyCreateBy.class).size());
        Assert.assertTrue(EntityPropertiesUtil.getColumnDbNames(cache, PropertyCreateBy.class).contains("create_by"));
        Assert.assertFalse(EntityPropertiesUtil.getColumnDbNames(cache, PropertyCreator.class).isEmpty());
        Assert.assertFalse(EntityPropertiesUtil.getColumnDbNames(cache, PropertyUpdateBy.class).isEmpty());
        Assert.assertFalse(EntityPropertiesUtil.getColumnDbNames(cache, PropertyUpdateTime.class).isEmpty());
        Assert.assertFalse(EntityPropertiesUtil.getColumnDbNames(cache, PropertyUpdater.class).isEmpty());
        Assert.assertFalse(EntityPropertiesUtil.getColumnDbNames(cache, PropertyDeleted.class).isEmpty());
        Assert.assertFalse(EntityPropertiesUtil.getColumnDbNames(cache, PropertyVersion.class).isEmpty());
        // 普通字段下划线转换
        Map<String, Pair<String, Class<?>>> none = cache.get(Object.class);
        Assert.assertNull(none);
    }

    @Test
    public void tablePropertiesNamed() {
        Map<Class<?>, Map<String, Pair<String, Class<?>>>> cache =
                EntityPropertiesUtil.tableProperties(true, NamedEntity.class);
        // @PropertyTableName 生效
        Assert.assertEquals("custom_table", EntityPropertiesUtil.getTableDbName(cache));
        // TableField.value 优先于下划线
        Set<String> plainColumns = cache.getOrDefault(Object.class, new HashMap<>()).keySet();
        Assert.assertTrue(cache.containsKey(PropertyId.class));
    }

    @Test
    public void tablePropertiesNoAnnotations() {
        Map<Class<?>, Map<String, Pair<String, Class<?>>>> cache =
                EntityPropertiesUtil.tableProperties(false, AbstractUpdateByTimeBaseEntity.class);
        // 无注解: 表名为类名下划线
        Assert.assertEquals("abstract_update_by_time_base_entity", EntityPropertiesUtil.getTableDbName(cache));
        // mapUnderscoreToCamelCase=false -> 字段名原样
        Assert.assertTrue(EntityPropertiesUtil.getColumnDbNames(cache, PropertyUpdateBy.class).contains("updateBy"));
        Assert.assertTrue(EntityPropertiesUtil.getColumnDbNames(cache, PropertyUpdateTime.class).contains("updateTime"));
    }

    @Test
    public void idAndValidThrows() {
        Map<Class<?>, Map<String, Pair<String, Class<?>>>> cache =
                EntityPropertiesUtil.tableProperties(true, AbstractBaseEntity.class);
        try {
            EntityPropertiesUtil.getColumnDbNameIdAndValid(cache);
            Assert.fail("should throw");
        } catch (NotSupportedException e) {
            Assert.assertNotNull(e.getMessage());
        }
        try {
            EntityPropertiesUtil.getColumnPropertyNameIdAndValid(cache);
            Assert.fail("should throw");
        } catch (NotSupportedException e) {
            Assert.assertNotNull(e.getMessage());
        }
    }

    @Test
    public void getColumnDefaults() {
        Map<Class<?>, Map<String, Pair<String, Class<?>>>> cache = new HashMap<>();
        Assert.assertTrue(EntityPropertiesUtil.getColumnPropertyNames(cache, PropertyCreateBy.class).isEmpty());
        Assert.assertTrue(EntityPropertiesUtil.getColumnDbNames(cache, PropertyCreateBy.class).isEmpty());
        Assert.assertNull(EntityPropertiesUtil.getTableDbName(cache));
    }

    private SuperRepository repository(boolean onlyWhenNull) {
        SuperRepository repository = Mockito.mock(SuperRepository.class);
        Mockito.when(repository.onlySetUpdateInfoWhenNull()).thenReturn(onlyWhenNull);
        Mockito.when(repository.onlySetCreateInfoWhenNull()).thenReturn(onlyWhenNull);
        return repository;
    }

    @Test
    public void setFieldValueEntity() {
        TableInfo tableInfo = TableInfoHelper.getTableInfo(RichEntity.class);
        Assert.assertNotNull(tableInfo);
        RichEntity entity = new RichEntity();
        entity.setCreateBy(null);
        // onlyWhenNull=true 且字段为空 -> 填充
        EntityPropertiesUtil.setFieldValue(repository(true), tableInfo, entity,
                EntityPropertiesUtil.getColumnPropertyNames(
                        EntityPropertiesUtil.tableProperties(true, RichEntity.class), PropertyCreateBy.class), 99L);
        Assert.assertEquals(99L, entity.getCreateBy().longValue());
        // onlyWhenNull=true 且字段非空 -> 不覆盖
        EntityPropertiesUtil.setFieldValue(repository(true), tableInfo, entity,
                EntityPropertiesUtil.getColumnPropertyNames(
                        EntityPropertiesUtil.tableProperties(true, RichEntity.class), PropertyCreateBy.class), 100L);
        Assert.assertEquals(99L, entity.getCreateBy().longValue());
        // onlyWhenNull=false -> 直接覆盖
        EntityPropertiesUtil.setFieldValue(repository(false), tableInfo, entity,
                EntityPropertiesUtil.getColumnPropertyNames(
                        EntityPropertiesUtil.tableProperties(true, RichEntity.class), PropertyCreateBy.class), 100L);
        Assert.assertEquals(100L, entity.getCreateBy().longValue());
        // 空 fieldName 集合 / null entity -> 直接返回
        EntityPropertiesUtil.setFieldValue(repository(true), tableInfo, entity, new HashMap<String, Pair<String, Class<?>>>().keySet(), 1L);
        EntityPropertiesUtil.setFieldValue(repository(true), tableInfo, null,
                EntityPropertiesUtil.getColumnPropertyNames(
                        EntityPropertiesUtil.tableProperties(true, RichEntity.class), PropertyCreateBy.class), 1L);
        Assert.assertEquals(100L, entity.getCreateBy().longValue());
    }

    @Test
    public void setFieldValueUpdateWrapper() {
        Map<Class<?>, Map<String, Pair<String, Class<?>>>> cache =
                EntityPropertiesUtil.tableProperties(true, RichEntity.class);
        Set<String> updateByColumns = EntityPropertiesUtil.getColumnDbNames(cache, PropertyUpdateBy.class);
        // onlyWhenNull=true, paramMap 无该字段 -> set
        UpdateWrapper<RichEntity> wrapper = new UpdateWrapper<>();
        EntityPropertiesUtil.setFieldValue(repository(true), wrapper, wrapper.getParamNameValuePairs(),
                new HashMap<>(), updateByColumns, 7L);
        Assert.assertTrue(wrapper.getSqlSet().contains("update_by"));
        // onlyWhenNull=false -> 直接 set
        UpdateWrapper<RichEntity> wrapper2 = new UpdateWrapper<>();
        EntityPropertiesUtil.setFieldValue(repository(false), wrapper2, wrapper2.getParamNameValuePairs(),
                new HashMap<>(), updateByColumns, 8L);
        Assert.assertTrue(wrapper2.getSqlSet().contains("update_by"));
        // onlyWhenNull=true, paramMap 已含该字段且值非空 -> 不重复 set
        UpdateWrapper<RichEntity> wrapperPreset = new UpdateWrapper<>();
        wrapperPreset.set("update_by", "old");
        EntityPropertiesUtil.setFieldValue(repository(true), wrapperPreset, wrapperPreset.getParamNameValuePairs(),
                com.skrstop.framework.components.starter.database.utils.SuperParamsUtil.getParamMap(wrapperPreset.getSqlSet()),
                updateByColumns, 9L);
        Assert.assertEquals(1, com.skrstop.framework.components.starter.database.utils.SuperParamsUtil
                .getParamMap(wrapperPreset.getSqlSet()).size());
        // onlyWhenNull=true, sqlSet 引用参数但值缺失 -> contains + isNull 组合
        UpdateWrapper<RichEntity> wrapperNoVal = new UpdateWrapper<>();
        wrapperNoVal.setSql("update_by=#{ew.paramNameValuePairs.MPGENVAL9}");
        EntityPropertiesUtil.setFieldValue(repository(true), wrapperNoVal, wrapperNoVal.getParamNameValuePairs(),
                com.skrstop.framework.components.starter.database.utils.SuperParamsUtil.getParamMap(wrapperNoVal.getSqlSet()),
                updateByColumns, 11L);
        // onlyWhenNull=true, sqlSet 已含该字段且参数值存在 -> 条件为 false 不重复 set
        UpdateWrapper<RichEntity> wrapperKeep = new UpdateWrapper<>();
        wrapperKeep.setSql("update_by=#{ew.paramNameValuePairs.MPGENVAL8}");
        wrapperKeep.getParamNameValuePairs().put("MPGENVAL8", "keep");
        EntityPropertiesUtil.setFieldValue(repository(true), wrapperKeep, wrapperKeep.getParamNameValuePairs(),
                com.skrstop.framework.components.starter.database.utils.SuperParamsUtil.getParamMap(wrapperKeep.getSqlSet()),
                updateByColumns, 12L);
        Assert.assertEquals("keep", wrapperKeep.getParamNameValuePairs().get("MPGENVAL8"));
        // 空集合 -> 直接返回
        UpdateWrapper<RichEntity> wrapper3 = new UpdateWrapper<>();
        EntityPropertiesUtil.setFieldValue(repository(true), wrapper3, wrapper3.getParamNameValuePairs(),
                new HashMap<>(), new HashMap<String, Pair<String, Class<?>>>().keySet(), 1L);
        Assert.assertNull(wrapper3.getSqlSet());
    }

    @Test
    public void setFieldValueLambdaUpdateWrapper() {
        Map<Class<?>, Map<String, Pair<String, Class<?>>>> cache =
                EntityPropertiesUtil.tableProperties(true, RichEntity.class);
        Set<String> updateByColumns = EntityPropertiesUtil.getColumnDbNames(cache, PropertyUpdateBy.class);
        // onlyWhenNull=true, paramMap 无该字段(paramMap 中不存在) -> setSql
        LambdaUpdateWrapper<RichEntity> wrapper = new LambdaUpdateWrapper<>();
        EntityPropertiesUtil.setFieldValue(repository(true), wrapper, wrapper.getParamNameValuePairs(),
                new HashMap<>(), updateByColumns, 7L);
        Assert.assertNotNull(wrapper.getSqlSet());
        // onlyWhenNull=false -> setSql
        LambdaUpdateWrapper<RichEntity> wrapper2 = new LambdaUpdateWrapper<>();
        EntityPropertiesUtil.setFieldValue(repository(false), wrapper2, wrapper2.getParamNameValuePairs(),
                new HashMap<>(), updateByColumns, 8L);
        Assert.assertNotNull(wrapper2.getSqlSet());
        // onlyWhenNull=true, sqlSet 已含该字段但参数值缺失 -> put 参数路径
        LambdaUpdateWrapper<RichEntity> wrapperPreset = new LambdaUpdateWrapper<>();
        wrapperPreset.setSql("update_by=#{ew.paramNameValuePairs.MPGENVAL1}");
        EntityPropertiesUtil.setFieldValue(repository(true), wrapperPreset, wrapperPreset.getParamNameValuePairs(),
                com.skrstop.framework.components.starter.database.utils.SuperParamsUtil.getParamMap(wrapperPreset.getSqlSet()),
                updateByColumns, 9L);
        Assert.assertEquals(9L, wrapperPreset.getParamNameValuePairs().get("MPGENVAL1"));
        // onlyWhenNull=false, sqlSet 已含该字段且参数值存在 -> 第二分支 put 路径
        LambdaUpdateWrapper<RichEntity> wrapperPreset2 = new LambdaUpdateWrapper<>();
        wrapperPreset2.setSql("update_by=#{ew.paramNameValuePairs.MPGENVAL2}");
        wrapperPreset2.getParamNameValuePairs().put("MPGENVAL2", "keep");
        EntityPropertiesUtil.setFieldValue(repository(false), wrapperPreset2, wrapperPreset2.getParamNameValuePairs(),
                com.skrstop.framework.components.starter.database.utils.SuperParamsUtil.getParamMap(wrapperPreset2.getSqlSet()),
                updateByColumns, 10L);
        Assert.assertEquals(10L, wrapperPreset2.getParamNameValuePairs().get("MPGENVAL2"));
        // onlyWhenNull=true, sqlSet 已含该字段且参数值存在 -> 条件为 false 不处理
        LambdaUpdateWrapper<RichEntity> wrapperKeep = new LambdaUpdateWrapper<>();
        wrapperKeep.setSql("update_by=#{ew.paramNameValuePairs.MPGENVAL7}");
        wrapperKeep.getParamNameValuePairs().put("MPGENVAL7", "keep");
        EntityPropertiesUtil.setFieldValue(repository(true), wrapperKeep, wrapperKeep.getParamNameValuePairs(),
                com.skrstop.framework.components.starter.database.utils.SuperParamsUtil.getParamMap(wrapperKeep.getSqlSet()),
                updateByColumns, 13L);
        Assert.assertEquals("keep", wrapperKeep.getParamNameValuePairs().get("MPGENVAL7"));
        // 空集合 -> 直接返回
        LambdaUpdateWrapper<RichEntity> wrapper3 = new LambdaUpdateWrapper<>();
        EntityPropertiesUtil.setFieldValue(repository(true), wrapper3, wrapper3.getParamNameValuePairs(),
                new HashMap<>(), new HashMap<String, Pair<String, Class<?>>>().keySet(), 1L);
        Assert.assertNull(wrapper3.getSqlSet());
    }
}
