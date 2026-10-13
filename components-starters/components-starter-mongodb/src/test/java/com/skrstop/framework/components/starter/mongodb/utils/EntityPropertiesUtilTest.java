package com.skrstop.framework.components.starter.mongodb.utils;

import cn.hutool.core.lang.Pair;
import com.skrstop.framework.components.core.exception.defined.illegal.NotSupportedException;
import com.skrstop.framework.components.starter.mongodb.annotation.property.*;
import com.skrstop.framework.components.starter.mongodb.constant.MongodbConst;
import com.skrstop.framework.components.starter.mongodb.repository.SuperRepository;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Property;
import dev.morphia.annotations.Version;
import dev.morphia.query.updates.UpdateOperators;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.*;

public class EntityPropertiesUtilTest {

    @Entity("rich_collection")
    static class RichEntity {
        @Id
        private Long mongoId;
        @PropertyId
        private Long pid;
        @PropertyCreateBy
        private Long createBy;
        @PropertyCreateTime
        private LocalDateTime createTime;
        @PropertyCreator
        private String creator;
        @PropertyUpdateBy
        private Long updateBy;
        @PropertyUpdateTime
        private LocalDateTime updateTime;
        @PropertyUpdater
        private String updater;
        @PropertyVersion
        @Version
        private Long version;
        @PropertyDeleted
        private Boolean deleted;
        @Property("plain_col")
        private String plain;
        private String bare;

        public Long getPid() {
            return pid;
        }

        public void setPid(Long pid) {
            this.pid = pid;
        }

        public Long getCreateBy() {
            return createBy;
        }

        public void setCreateBy(Long createBy) {
            this.createBy = createBy;
        }
    }

    static class PropIdEntity {
        @PropertyId
        private Long pid;
        @PropertyVersion
        private Long propVersion;
    }

    static class VersionOnlyEntity {
        @Version
        private Long version;
    }

    @PropertyCollectionName("named_collection")
    static class NamedEntity {
        @Id
        private Long id;
    }

    static class PlainEntity {
        @Id
        private Long id;
    }

    private SuperRepository repository(boolean onlyWhenNull) {
        SuperRepository repository = Mockito.mock(SuperRepository.class);
        Mockito.when(repository.onlySetCreateInfoWhenNull()).thenReturn(onlyWhenNull);
        Mockito.when(repository.onlySetUpdateInfoWhenNull()).thenReturn(onlyWhenNull);
        return repository;
    }

    @Test
    public void tablePropertiesIdentity() {
        Map<Class<?>, Map<String, Pair<String, Class<?>>>> cache =
                EntityPropertiesUtil.tableProperties(MongodbConst.NAME_RULE_IDENTITY, RichEntity.class);
        Assert.assertEquals("rich_collection",
                cache.get(PropertyCollectionName.class).get("RichEntity").getKey());
        // @Id -> _id
        Assert.assertEquals(MongodbConst.DEFAULT_ID_NAME, cache.get(PropertyId.class).get("mongoId").getKey());
        // @PropertyId -> 字段名
        Assert.assertEquals("pid", cache.get(PropertyId.class).get("pid").getKey());
        Assert.assertEquals(Long.class, cache.get(PropertyId.class).get("pid").getValue());
        Assert.assertEquals("createBy", cache.get(PropertyCreateBy.class).get("createBy").getKey());
        Assert.assertEquals("createTime", cache.get(PropertyCreateTime.class).get("createTime").getKey());
        Assert.assertEquals("creator", cache.get(PropertyCreator.class).get("creator").getKey());
        Assert.assertEquals("updateBy", cache.get(PropertyUpdateBy.class).get("updateBy").getKey());
        Assert.assertEquals("updateTime", cache.get(PropertyUpdateTime.class).get("updateTime").getKey());
        Assert.assertEquals("updater", cache.get(PropertyUpdater.class).get("updater").getKey());
        Assert.assertEquals("version", cache.get(PropertyVersion.class).get("version").getKey());
        Assert.assertEquals("deleted", cache.get(PropertyDeleted.class).get("deleted").getKey());
    }

    @Test
    public void tablePropertiesNamingRules() {
        // camelCase 与 identity 相同
        Map<Class<?>, Map<String, Pair<String, Class<?>>>> camel =
                EntityPropertiesUtil.tableProperties(MongodbConst.NAME_RULE_CAMEL_CASE, RichEntity.class);
        Assert.assertEquals("createBy", findColumn(camel, "createBy"));
        // snakeCase
        Map<Class<?>, Map<String, Pair<String, Class<?>>>> snake =
                EntityPropertiesUtil.tableProperties(MongodbConst.NAME_RULE_SNAKE_CASE, RichEntity.class);
        Assert.assertEquals("create_by", findColumn(snake, "createBy"));
        // lowerCase
        Map<Class<?>, Map<String, Pair<String, Class<?>>>> lower =
                EntityPropertiesUtil.tableProperties(MongodbConst.NAME_RULE_LOWER_CASE, RichEntity.class);
        Assert.assertEquals("createby", findColumn(lower, "createBy"));
        // kebabCase
        Map<Class<?>, Map<String, Pair<String, Class<?>>>> kebab =
                EntityPropertiesUtil.tableProperties(MongodbConst.NAME_RULE_KEBAB_CASE, RichEntity.class);
        Assert.assertEquals("create-by", findColumn(kebab, "createBy"));
        // 未知规则 -> default 分支不转换
        Map<Class<?>, Map<String, Pair<String, Class<?>>>> other =
                EntityPropertiesUtil.tableProperties("unknown", RichEntity.class);
        Assert.assertEquals("createBy", findColumn(other, "createBy"));
    }

    private String findColumn(Map<Class<?>, Map<String, Pair<String, Class<?>>>> cache, String field) {
        return cache.values().stream()
                .filter(m -> m.containsKey(field))
                .map(m -> m.get(field).getKey())
                .findFirst().orElse(null);
    }

    @Test
    public void defaultConstructorAndPropIdOnly() {
        Assert.assertNotNull(new EntityPropertiesUtil());
        // 仅 @PropertyId（无 @Id）与仅 @PropertyVersion（无 @Version）
        Map<Class<?>, Map<String, Pair<String, Class<?>>>> cache =
                EntityPropertiesUtil.tableProperties(MongodbConst.NAME_RULE_IDENTITY, PropIdEntity.class);
        Assert.assertEquals("pid", cache.get(PropertyId.class).get("pid").getKey());
        Assert.assertEquals("propVersion", cache.get(PropertyVersion.class).get("propVersion").getKey());
        // 仅 @Version（无 @PropertyVersion）
        Map<Class<?>, Map<String, Pair<String, Class<?>>>> versionOnly =
                EntityPropertiesUtil.tableProperties(MongodbConst.NAME_RULE_IDENTITY, VersionOnlyEntity.class);
        Assert.assertEquals("version", versionOnly.get(PropertyVersion.class).get("version").getKey());
    }

    @Test
    public void collectionNameRules() {
        // @PropertyCollectionName
        Map<Class<?>, Map<String, Pair<String, Class<?>>>> named =
                EntityPropertiesUtil.tableProperties(MongodbConst.NAME_RULE_IDENTITY, NamedEntity.class);
        Assert.assertEquals("named_collection", EntityPropertiesUtil.getCollectionDbName(named));
        // 无注解 -> simpleName
        Map<Class<?>, Map<String, Pair<String, Class<?>>>> plain =
                EntityPropertiesUtil.tableProperties(MongodbConst.NAME_RULE_IDENTITY, PlainEntity.class);
        Assert.assertEquals("PlainEntity", EntityPropertiesUtil.getCollectionDbName(plain));
        // 无 PropertyCollectionName 缓存 -> null
        Assert.assertNull(EntityPropertiesUtil.getCollectionDbName(new HashMap<>()));
    }

    @Test
    public void idHelpers() {
        Map<Class<?>, Map<String, Pair<String, Class<?>>>> cache =
                EntityPropertiesUtil.tableProperties(MongodbConst.NAME_RULE_IDENTITY, RichEntity.class);
        Assert.assertEquals("mongoId", EntityPropertiesUtil.getColumnPropertyNameId(cache));
        Assert.assertNotNull(EntityPropertiesUtil.getColumnDbNameIdAndValid(cache));
        // RichEntity 有两个 id 字段（@Id 与 @PropertyId）-> 抛异常
        try {
            EntityPropertiesUtil.getColumnTypeId(cache);
            Assert.fail("should throw");
        } catch (RuntimeException e) {
            Assert.assertNotNull(e.getMessage());
        }
        // 单个 id -> 返回类型
        Map<Class<?>, Map<String, Pair<String, Class<?>>>> single =
                EntityPropertiesUtil.tableProperties(MongodbConst.NAME_RULE_IDENTITY, PlainEntity.class);
        Assert.assertEquals(Long.class, EntityPropertiesUtil.getColumnTypeId(single));
        // 空 cache -> getColumnTypeId 为 null
        Assert.assertNull(EntityPropertiesUtil.getColumnTypeId(new HashMap<>()));
        // 无 id -> 抛异常
        Map<Class<?>, Map<String, Pair<String, Class<?>>>> noId =
                EntityPropertiesUtil.tableProperties(MongodbConst.NAME_RULE_IDENTITY, NamedEntity.class);
        noId.remove(PropertyId.class);
        try {
            EntityPropertiesUtil.getColumnPropertyNameId(noId);
            Assert.fail("should throw");
        } catch (NotSupportedException e) {
            Assert.assertNotNull(e.getMessage());
        }
        try {
            EntityPropertiesUtil.getColumnDbNameIdAndValid(noId);
            Assert.fail("should throw");
        } catch (NotSupportedException e) {
            Assert.assertNotNull(e.getMessage());
        }
    }

    @Test
    public void columnHelpersMissingKeys() {
        Map<Class<?>, Map<String, Pair<String, Class<?>>>> empty = new HashMap<>();
        Assert.assertTrue(EntityPropertiesUtil.getColumnPropertyNames(empty, PropertyCreateBy.class).isEmpty());
        Assert.assertTrue(EntityPropertiesUtil.getColumnDbNames(empty, PropertyCreateBy.class).isEmpty());
        Map<Class<?>, Map<String, Pair<String, Class<?>>>> cache =
                EntityPropertiesUtil.tableProperties(MongodbConst.NAME_RULE_IDENTITY, RichEntity.class);
        Set<String> names = EntityPropertiesUtil.getColumnPropertyNames(cache, PropertyCreateBy.class);
        Assert.assertEquals(Collections.singleton("createBy"), names);
        Assert.assertEquals(Collections.singleton("createBy"),
                EntityPropertiesUtil.getColumnDbNames(cache, PropertyCreateBy.class));
    }

    @Test
    public void getAndSetFieldValue() {
        RichEntity entity = new RichEntity();
        Assert.assertNull(EntityPropertiesUtil.getFieldValue(entity, "pid"));
        EntityPropertiesUtil.setFieldValue(repository(false), entity,
                Collections.singleton("pid"), 9L);
        Assert.assertEquals(9L, entity.getPid().longValue());
        // onlyWhenNull=true 且值已存在 -> 不覆盖
        EntityPropertiesUtil.setFieldValue(repository(true), entity,
                Collections.singleton("pid"), 10L);
        Assert.assertEquals(9L, entity.getPid().longValue());
        // 空 fieldNames / null entity -> 直接返回
        EntityPropertiesUtil.setFieldValue(repository(true), entity, Collections.emptySet(), 1L);
        EntityPropertiesUtil.setFieldValue(repository(true), null, Collections.singleton("pid"), 1L);
        Assert.assertEquals(9L, entity.getPid().longValue());
    }

    @Test
    public void setFieldValueUpdates() {
        List<dev.morphia.query.updates.UpdateOperator> updates = new ArrayList<>();
        updates.add(UpdateOperators.set("exist", 1L));
        Set<String> fieldCollect = new HashSet<>();
        fieldCollect.add("exist");
        Set<String> fieldNames = new HashSet<>(Arrays.asList("new1", "new2"));
        // onlyWhenNull=true 且不在 fieldCollect 中 -> 添加
        EntityPropertiesUtil.setFieldValue(repository(true), updates, fieldCollect, fieldNames, 2L);
        Assert.assertEquals(3, updates.size());
        // onlyWhenNull=false -> 直接添加
        List<dev.morphia.query.updates.UpdateOperator> updates2 = new ArrayList<>();
        EntityPropertiesUtil.setFieldValue(repository(false), updates2, fieldCollect,
                Collections.singleton("new3"), 3L);
        Assert.assertEquals(1, updates2.size());
        // 空 fieldNames -> 直接返回
        List<dev.morphia.query.updates.UpdateOperator> updates3 = new ArrayList<>();
        EntityPropertiesUtil.setFieldValue(repository(true), updates3, fieldCollect, Collections.emptySet(), 4L);
        Assert.assertTrue(updates3.isEmpty());
    }
}
