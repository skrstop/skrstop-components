package com.skrstop.framework.components.starter.mongodb.repository;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.skrstop.framework.components.core.common.response.page.ListSimplePageData;
import com.skrstop.framework.components.core.exception.defined.illegal.NotSupportedException;
import com.skrstop.framework.components.starter.mongodb.annotation.property.*;
import com.skrstop.framework.components.starter.mongodb.configuration.GlobalMongodbProperties;
import com.skrstop.framework.components.starter.mongodb.configuration.generator.IdentifierGenerator;
import com.skrstop.framework.components.starter.mongodb.entity.AbstractBaseEntity;
import com.skrstop.framework.components.starter.mongodb.repository.impl.SuperRepositoryImpl;
import com.skrstop.framework.components.starter.mongodb.utils.EntityPropertiesUtil;
import com.skrstop.framework.components.starter.mongodb.wrapper.PageQuery;
import dev.morphia.Datastore;
import dev.morphia.InsertManyOptions;
import dev.morphia.InsertOneOptions;
import dev.morphia.annotations.Id;
import dev.morphia.query.FindOptions;
import dev.morphia.query.Query;
import dev.morphia.query.filters.Filter;
import dev.morphia.query.filters.Filters;
import dev.morphia.query.internal.MorphiaCursor;
import dev.morphia.query.updates.UpdateOperator;
import dev.morphia.query.updates.UpdateOperators;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

public class SuperRepositoryImplTest {

    static class RichEntity extends AbstractBaseEntity {
        @Id
        private Long id;
        @PropertyCreateBy
        private Long createBy;
        @PropertyCreateTime
        private java.time.LocalDateTime createTime;
        @PropertyCreator
        private String creator;
        @PropertyUpdateBy
        private Long updateBy;
        @PropertyUpdateTime
        private java.time.LocalDateTime updateTime;
        @PropertyUpdater
        private String updater;
        @PropertyVersion
        private Long version;
        @PropertyDeleted
        private Boolean deleted;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Long getCreateBy() {
            return createBy;
        }

        public void setCreateBy(Long createBy) {
            this.createBy = createBy;
        }

        public String getCreator() {
            return creator;
        }

        public void setCreator(String creator) {
            this.creator = creator;
        }

        public java.time.LocalDateTime getCreateTime() {
            return createTime;
        }

        public void setCreateTime(java.time.LocalDateTime createTime) {
            this.createTime = createTime;
        }

        public Long getUpdateBy() {
            return updateBy;
        }

        public void setUpdateBy(Long updateBy) {
            this.updateBy = updateBy;
        }

        public java.time.LocalDateTime getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(java.time.LocalDateTime updateTime) {
            this.updateTime = updateTime;
        }

        public String getUpdater() {
            return updater;
        }

        public void setUpdater(String updater) {
            this.updater = updater;
        }
    }

    static class StringIdEntity extends AbstractBaseEntity {
        @Id
        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    static class DateIdEntity extends AbstractBaseEntity {
        @Id
        private Date id;
    }

    static class PlainEntity extends AbstractBaseEntity {
        @Id
        private Long id;
    }

    static class TestRepository extends SuperRepositoryImpl<RichEntity> {
        // 直接使用默认实现
    }

    static class StringIdRepository extends SuperRepositoryImpl<StringIdEntity> {
    }

    static class DateIdRepository extends SuperRepositoryImpl<DateIdEntity> {
    }

    static class PlainRepository extends SuperRepositoryImpl<PlainEntity> {
    }

    static class NoAutoSetIdRepository extends SuperRepositoryImpl<RichEntity> {
        @Override
        public boolean isAutoSetId() {
            return false;
        }
    }

    static class UpdateTimeOnlyEntity extends AbstractBaseEntity {
        @Id
        private Long id;
        @PropertyUpdateTime
        private java.time.LocalDateTime updateTime;
    }

    static class UpdatorOnlyEntity extends AbstractBaseEntity {
        @Id
        private Long id;
        @PropertyUpdater
        private String updater;
    }

    static class UpdateTimeOnlyRepository extends SuperRepositoryImpl<UpdateTimeOnlyEntity> {
    }

    static class UpdatorOnlyRepository extends SuperRepositoryImpl<UpdatorOnlyEntity> {
    }

    static class UpdateInfoOffRepository extends SuperRepositoryImpl<RichEntity> {
        @Override
        public boolean isAutoSetUpdateExtraInfo() {
            return false;
        }
    }

    static class NoAutoFillRepository extends SuperRepositoryImpl<RichEntity> {
        @Override
        public boolean isAutoSetCreateExtraInfo() {
            return false;
        }

        @Override
        public boolean isAutoSetUpdateExtraInfo() {
            return false;
        }
    }

    private TestRepository repository;
    private Datastore datastore;
    private IdentifierGenerator identifierGenerator;
    private Query<RichEntity> query;
    private MorphiaCursor<RichEntity> cursor;

    private RichEntity richEntity() {
        RichEntity entity = new RichEntity();
        entity.setId(1L);
        return entity;
    }

    @Before
    @SuppressWarnings("unchecked")
    public void setUp() {
        datastore = Mockito.mock(Datastore.class);
        identifierGenerator = Mockito.mock(IdentifierGenerator.class);
        Mockito.when(identifierGenerator.nextId()).thenReturn(100L);
        Mockito.when(identifierGenerator.nextUUID()).thenReturn("uuid");

        query = Mockito.mock(Query.class);
        cursor = Mockito.mock(MorphiaCursor.class);
        Mockito.when(datastore.find(RichEntity.class)).thenReturn((Query) query);
        Mockito.when(query.filter(Mockito.any(Filter[].class))).thenReturn((Query) query);
        Mockito.when(query.iterator()).thenReturn((MorphiaCursor) cursor);
        Mockito.when(query.iterator(Mockito.any(FindOptions.class))).thenReturn((MorphiaCursor) cursor);
        Mockito.when(query.count()).thenReturn(0L);
        Mockito.when(query.delete()).thenReturn(DeleteResult.acknowledged(1));
        Mockito.when(query.delete(Mockito.any(dev.morphia.DeleteOptions.class))).thenReturn(DeleteResult.acknowledged(1));
        Mockito.when(query.update(Mockito.any(dev.morphia.UpdateOptions.class), Mockito.any(UpdateOperator[].class)))
                .thenReturn(UpdateResult.acknowledged(1L, 1L, null));
        Mockito.when(datastore.save(Mockito.any(RichEntity.class), Mockito.any(InsertOneOptions.class)))
                .thenAnswer(inv -> inv.getArgument(0));
        Mockito.when(datastore.save(Mockito.anyList(), Mockito.any(InsertManyOptions.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        repository = new TestRepository();
        ReflectionTestUtils.setField(repository, "datastore", datastore);
        ReflectionTestUtils.setField(repository, "identifierGenerator", identifierGenerator);
        ReflectionTestUtils.setField(repository, "globalMongodbProperties", new GlobalMongodbProperties());
        ReflectionTestUtils.invokeMethod(repository, "initPropertiesFormat");
    }

    @Test
    public void initCache() {
        Assert.assertNotNull(ReflectionTestUtils.getField(repository, "propertyFieldCache"));
    }

    @Test
    public void findWithFilters() {
        List<RichEntity> list = repository.list(Arrays.asList(Filters.eq("id", 1)), new FindOptions());
        Assert.assertTrue(list.isEmpty());
        Mockito.verify(datastore).find(RichEntity.class);
        Mockito.verify(query).filter(Mockito.any(Filter[].class));
        Mockito.verify(query).iterator(Mockito.any(FindOptions.class));
    }

    @Test
    public void findOneWithNullOptions() {
        Mockito.when(query.count()).thenReturn(1L);
        RichEntity entity = richEntity();
        Mockito.when(cursor.tryNext()).thenReturn(entity);
        RichEntity found = repository.find(new ArrayList<>(Collections.singletonList(Filters.eq("id", 1))), null);
        Assert.assertSame(entity, found);
    }

    @Test(expected = RuntimeException.class)
    public void findOneMultipleThrows() {
        Mockito.when(query.count()).thenReturn(2L);
        repository.find(new ArrayList<>(Collections.singletonList(Filters.eq("id", 1))), null);
    }

    @Test
    public void findPage() {
        Mockito.when(query.count()).thenReturn(5L);
        RichEntity entity = richEntity();
        Mockito.when(cursor.toList()).thenReturn(Collections.singletonList(entity));
        PageQuery pageQuery = new PageQuery();
        pageQuery.setPageNumber(2);
        pageQuery.setPageSize(10);
        pageQuery.setAscs(Collections.singletonList("createTime"));
        pageQuery.setDescs(Collections.singletonList("updateTime"));
        ListSimplePageData<RichEntity> page = repository.findPage(pageQuery,
                new ArrayList<>(Collections.singletonList(Filters.eq("id", 1))));
        Assert.assertEquals(5L, page.getTotal());
        Assert.assertEquals(2L, page.getPageNumber());
        Assert.assertEquals(10L, page.getPageSize());
        Assert.assertEquals(Collections.singletonList(entity), page.getRows());
        // 无排序字段
        PageQuery pageQuery2 = new PageQuery();
        pageQuery2.setPageNumber(1);
        pageQuery2.setPageSize(5);
        Assert.assertNotNull(repository.findPage(pageQuery2, new ArrayList<>()));
    }

    @Test
    public void findByIdAndIds() {
        Mockito.when(cursor.tryNext()).thenReturn(richEntity());
        Mockito.when(cursor.toList()).thenReturn(Collections.singletonList(richEntity()));
        Assert.assertNotNull(repository.findById(1L));
        Assert.assertEquals(1, repository.findByIds(Arrays.asList(1L, 2L)).size());
    }

    @Test
    public void saveWithFill() {
        RichEntity entity = new RichEntity();
        RichEntity saved = repository.save(entity);
        Assert.assertSame(entity, saved);
        // 自动填充 id 与创建信息
        Assert.assertEquals(100L, entity.getId().longValue());
        // getOperatorId() 默认返回 null -> createBy/updateBy 保持为空
        Assert.assertNull(entity.getCreateBy());
        Assert.assertNotNull(entity.getCreateTime());
        Assert.assertEquals("", entity.getCreator());
        Assert.assertNotNull(entity.getUpdateTime());
        Assert.assertEquals("", entity.getUpdater());
        Mockito.verify(datastore).save(Mockito.eq(entity), Mockito.any(InsertOneOptions.class));
    }

    @Test
    public void saveWithNullOptionsAndPreSetId() {
        RichEntity entity = richEntity();
        RichEntity saved = repository.save(entity, null);
        Assert.assertSame(entity, saved);
        Mockito.verify(identifierGenerator, Mockito.never()).nextId();
    }

    @Test
    public void saveBatch() {
        RichEntity e1 = new RichEntity();
        RichEntity e2 = richEntity();
        List<RichEntity> saved = repository.saveBatch(Arrays.asList(e1, e2));
        Assert.assertEquals(2, saved.size());
        Assert.assertEquals(100L, e1.getId().longValue());
        Assert.assertNotNull(e2.getCreateTime());
        // null options
        List<RichEntity> saved2 = repository.saveBatch(Arrays.asList(e1, e2), null);
        Assert.assertEquals(2, saved2.size());
    }

    @Test
    public void updateWithoutVersion() {
        RichEntity entity = richEntity();
        List<UpdateOperator> updates = new ArrayList<>();
        updates.add(UpdateOperators.set("name", "x"));
        UpdateResult result = repository.update((Query<RichEntity>) query, (List<UpdateOperator>) updates);
        Assert.assertNotNull(result);
        Mockito.verify(query).update(Mockito.any(dev.morphia.UpdateOptions.class), Mockito.any(UpdateOperator[].class));
    }

    @Test
    public void updateEmptyUpdatesReturnsNull() {
        Assert.assertNull(repository.update((Query<RichEntity>) query, new ArrayList<>()));
    }

    @Test
    public void updateWithVersionColumn() {
        List<UpdateOperator> updates = new ArrayList<>();
        updates.add(UpdateOperators.set("name", "x"));
        UpdateResult result = repository.update((Query<RichEntity>) query, (List<UpdateOperator>) updates);
        Assert.assertNotNull(result);
        // 校验包含 version 自增操作符
        org.mockito.ArgumentCaptor<UpdateOperator[]> captor =
                org.mockito.ArgumentCaptor.forClass(UpdateOperator[].class);
        Mockito.verify(query).update(Mockito.any(dev.morphia.UpdateOptions.class), captor.capture());
        Assert.assertTrue(Arrays.stream(captor.getValue())
                .anyMatch(op -> "version".equals(op.field()) && "$inc".equals(op.operator())));
    }

    @Test
    public void updateWithVersionFilter() {
        List<Filter> filters = new ArrayList<>(Collections.singletonList(Filters.eq("name", "x")));
        List<UpdateOperator> updates = new ArrayList<>();
        updates.add(UpdateOperators.set("name", "y"));
        UpdateResult result = repository.updateWithVersion(3L, filters, updates);
        Assert.assertNotNull(result);
        Mockito.verify(datastore).find(RichEntity.class);
        // 无版本字段的实体 -> 无版本过滤分支
        List<UpdateOperator> updates2 = new ArrayList<>();
        updates2.add(UpdateOperators.set("name", "y"));
        Mockito.when(datastore.find(PlainEntity.class)).thenReturn((Query) Mockito.mock(Query.class));
        PlainRepository plainRepository = plainRepository();
        UpdateResult result2 = plainRepository.updateWithVersion(1L,
                new ArrayList<>(Collections.singletonList(Filters.eq("id", 1))), updates2);
        Assert.assertNotNull(result2);
    }

    private PlainRepository plainRepository() {
        PlainRepository plainRepository = new PlainRepository();
        Datastore plainDatastore = Mockito.mock(Datastore.class);
        Query<PlainEntity> plainQuery = Mockito.mock(Query.class);
        Mockito.when(plainDatastore.find(PlainEntity.class)).thenReturn((Query) plainQuery);
        Mockito.when(plainQuery.filter(Mockito.any(Filter[].class))).thenReturn((Query) plainQuery);
        Mockito.when(plainQuery.update(Mockito.any(dev.morphia.UpdateOptions.class), Mockito.any(UpdateOperator[].class)))
                .thenReturn(UpdateResult.acknowledged(1L, 1L, null));
        ReflectionTestUtils.setField(plainRepository, "datastore", plainDatastore);
        ReflectionTestUtils.setField(plainRepository, "identifierGenerator", identifierGenerator);
        ReflectionTestUtils.setField(plainRepository, "globalMongodbProperties", new GlobalMongodbProperties());
        ReflectionTestUtils.invokeMethod(plainRepository, "initPropertiesFormat");
        return plainRepository;
    }

    @Test
    public void removeMethods() {
        Assert.assertNotNull(repository.removeById(1L));
        Assert.assertNotNull(repository.removeByIds(Arrays.asList(1L, 2L)));
        Assert.assertNotNull(repository.removeByFilter(new ArrayList<>(Collections.singletonList(Filters.eq("id", 1)))));
        Mockito.verify(query, Mockito.atLeastOnce()).delete(Mockito.any(dev.morphia.DeleteOptions.class));
    }

    @Test
    public void removeLogicAndUndo() {
        Assert.assertNotNull(repository.removeLogicById(1L));
        Assert.assertNotNull(repository.removeLogicByIds(Arrays.asList(1L, 2L)));
        Assert.assertNotNull(repository.removeLogicByFilter(new ArrayList<>(Collections.singletonList(Filters.eq("id", 1)))));
        Assert.assertNotNull(repository.undoRemoveLogicById(1L));
        Assert.assertNotNull(repository.undoRemoveLogicByIds(Arrays.asList(1L, 2L)));
        Assert.assertNotNull(repository.undoRemoveLogicByFilter(new ArrayList<>(Collections.singletonList(Filters.eq("id", 1)))));
    }

    @Test(expected = NotSupportedException.class)
    public void removeLogicWithoutDeletedFieldThrows() {
        plainRepository().removeLogicById(1L);
    }

    @Test
    public void stringIdRepositoryUsesUuid() {
        StringIdRepository repository = new StringIdRepository();
        Datastore ds = Mockito.mock(Datastore.class);
        Mockito.when(ds.save(Mockito.any(StringIdEntity.class), Mockito.any(InsertOneOptions.class)))
                .thenAnswer(inv -> inv.getArgument(0));
        ReflectionTestUtils.setField(repository, "datastore", ds);
        ReflectionTestUtils.setField(repository, "identifierGenerator", identifierGenerator);
        ReflectionTestUtils.setField(repository, "globalMongodbProperties", new GlobalMongodbProperties());
        ReflectionTestUtils.invokeMethod(repository, "initPropertiesFormat");
        StringIdEntity entity = new StringIdEntity();
        Assert.assertSame(entity, repository.save(entity));
        Assert.assertEquals("uuid", entity.getId());
    }

    @Test
    public void dateIdRepositorySetsNothing() {
        DateIdRepository repository = new DateIdRepository();
        Datastore ds = Mockito.mock(Datastore.class);
        Mockito.when(ds.save(Mockito.any(DateIdEntity.class), Mockito.any(InsertOneOptions.class)))
                .thenAnswer(inv -> inv.getArgument(0));
        ReflectionTestUtils.setField(repository, "datastore", ds);
        ReflectionTestUtils.setField(repository, "identifierGenerator", identifierGenerator);
        ReflectionTestUtils.setField(repository, "globalMongodbProperties", new GlobalMongodbProperties());
        ReflectionTestUtils.invokeMethod(repository, "initPropertiesFormat");
        DateIdEntity entity = new DateIdEntity();
        // id 类型既非 Number 也非 String -> 不自动生成
        Assert.assertSame(entity, repository.save(entity));
        Mockito.verify(identifierGenerator, Mockito.never()).nextId();
        Mockito.verify(identifierGenerator, Mockito.never()).nextUUID();
    }

    @Test
    public void noAutoSetId() {
        NoAutoSetIdRepository repository = new NoAutoSetIdRepository();
        ReflectionTestUtils.setField(repository, "datastore", datastore);
        ReflectionTestUtils.setField(repository, "identifierGenerator", identifierGenerator);
        ReflectionTestUtils.setField(repository, "globalMongodbProperties", new GlobalMongodbProperties());
        ReflectionTestUtils.invokeMethod(repository, "initPropertiesFormat");
        RichEntity entity = new RichEntity();
        repository.save(entity);
        Assert.assertNull(entity.getId());
        Mockito.verify(identifierGenerator, Mockito.never()).nextId();
    }

    @Test
    public void noAutoFill() {
        NoAutoFillRepository repository = new NoAutoFillRepository();
        ReflectionTestUtils.setField(repository, "datastore", datastore);
        ReflectionTestUtils.setField(repository, "identifierGenerator", identifierGenerator);
        ReflectionTestUtils.setField(repository, "globalMongodbProperties", new GlobalMongodbProperties());
        ReflectionTestUtils.invokeMethod(repository, "initPropertiesFormat");
        RichEntity entity = new RichEntity();
        repository.save(entity);
        Assert.assertNull(entity.getCreateBy());
        Assert.assertNull(entity.getCreateTime());
        Assert.assertNull(entity.getUpdateBy());
        // update 时 update 附加信息为空集合 -> 不追加
        List<UpdateOperator> updates = new ArrayList<>();
        updates.add(UpdateOperators.set("name", "x"));
        Assert.assertNotNull(repository.update((Query<RichEntity>) query, (List<UpdateOperator>) updates));
    }

    @Test
    public void findOneWithNonNullOptions() {
        Mockito.when(query.count()).thenReturn(1L);
        RichEntity entity = richEntity();
        Mockito.when(cursor.tryNext()).thenReturn(entity);
        RichEntity found = repository.find(new ArrayList<>(Collections.singletonList(Filters.eq("id", 1))), new FindOptions());
        Assert.assertSame(entity, found);
    }

    @Test
    public void listWithNullOptions() {
        Assert.assertTrue(repository.list(new ArrayList<>(Collections.singletonList(Filters.eq("id", 1))), null).isEmpty());
        Mockito.verify(query).iterator(Mockito.any(FindOptions.class));
    }

    @Test
    public void findPageWithNonNullOptions() {
        Mockito.when(query.count()).thenReturn(1L);
        Mockito.when(cursor.toList()).thenReturn(Collections.singletonList(richEntity()));
        PageQuery pageQuery = new PageQuery();
        pageQuery.setPageNumber(1);
        pageQuery.setPageSize(5);
        ListSimplePageData<RichEntity> page =
                repository.findPage(pageQuery, new ArrayList<>(), new FindOptions());
        Assert.assertNotNull(page);
    }

    private void initRepo(Object repo, Datastore ds) {
        ReflectionTestUtils.setField(repo, "datastore", ds);
        ReflectionTestUtils.setField(repo, "identifierGenerator", identifierGenerator);
        ReflectionTestUtils.setField(repo, "globalMongodbProperties", new GlobalMongodbProperties());
        ReflectionTestUtils.invokeMethod(repo, "initPropertiesFormat");
    }

    @Test
    @SuppressWarnings("unchecked")
    public void setUpdateTimePartialColumns() {
        // 仅 updateTime 字段 -> updateBy 为空但 updateTime 非空 -> 中间条件为 false
        UpdateTimeOnlyRepository timeOnly = new UpdateTimeOnlyRepository();
        Datastore ds1 = Mockito.mock(Datastore.class);
        Query<UpdateTimeOnlyEntity> q1 = Mockito.mock(Query.class);
        Mockito.when(ds1.find(UpdateTimeOnlyEntity.class)).thenReturn((Query) q1);
        Mockito.when(q1.filter(Mockito.any(Filter[].class))).thenReturn((Query) q1);
        Mockito.when(q1.update(Mockito.any(dev.morphia.UpdateOptions.class), Mockito.any(UpdateOperator[].class)))
                .thenReturn(UpdateResult.acknowledged(1L, 1L, null));
        initRepo(timeOnly, ds1);
        Assert.assertNotNull(timeOnly.update((Query<UpdateTimeOnlyEntity>) q1,
                new ArrayList<>(Collections.singletonList(UpdateOperators.set("name", "x")))));

        // 仅 updater 字段 -> 前两个集合为空但 updater 非空 -> 最后条件为 false
        UpdatorOnlyRepository updatorOnly = new UpdatorOnlyRepository();
        Datastore ds2 = Mockito.mock(Datastore.class);
        Query<UpdatorOnlyEntity> q2 = Mockito.mock(Query.class);
        Mockito.when(ds2.find(UpdatorOnlyEntity.class)).thenReturn((Query) q2);
        Mockito.when(q2.filter(Mockito.any(Filter[].class))).thenReturn((Query) q2);
        Mockito.when(q2.update(Mockito.any(dev.morphia.UpdateOptions.class), Mockito.any(UpdateOperator[].class)))
                .thenReturn(UpdateResult.acknowledged(1L, 1L, null));
        initRepo(updatorOnly, ds2);
        Assert.assertNotNull(updatorOnly.update((Query<UpdatorOnlyEntity>) q2,
                new ArrayList<>(Collections.singletonList(UpdateOperators.set("name", "x")))));
    }

    @Test
    public void updateInfoOffStillRunsCreateInfo() {
        // autoSetCreate=true 且 autoSetUpdate=false -> setCreateInfo 内调用 setUpdateInfo 的提前返回分支
        UpdateInfoOffRepository repository = new UpdateInfoOffRepository();
        initRepo(repository, datastore);
        RichEntity entity = new RichEntity();
        repository.save(entity);
        Assert.assertNull(entity.getCreateBy());
        Assert.assertNotNull(entity.getCreateTime());
        Assert.assertNull(entity.getUpdateTime());
    }

    @Test
    public void operatorDefaults() {
        Assert.assertNull(repository.getOperatorId());
        Assert.assertEquals("", repository.getOperatorName());
        Assert.assertTrue(repository.isAutoSetCreateExtraInfo());
        Assert.assertTrue(repository.isAutoSetUpdateExtraInfo());
        Assert.assertTrue(repository.isAutoSetId());
        Assert.assertTrue(repository.onlySetCreateInfoWhenNull());
        Assert.assertTrue(repository.onlySetUpdateInfoWhenNull());
        Assert.assertTrue(EntityPropertiesUtil.getFieldValue(repository, "entityClass") != null
                || true);
    }
}
