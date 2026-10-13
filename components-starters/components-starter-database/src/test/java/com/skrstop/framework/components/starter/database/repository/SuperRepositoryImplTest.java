package com.skrstop.framework.components.starter.database.repository;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.skrstop.framework.components.core.exception.defined.illegal.NotSupportedException;
import com.skrstop.framework.components.starter.database.annotation.property.PropertyDeleted;
import com.skrstop.framework.components.starter.database.annotation.property.PropertyUpdateBy;
import com.skrstop.framework.components.starter.database.annotation.property.PropertyUpdateTime;
import com.skrstop.framework.components.starter.database.annotation.property.PropertyUpdater;
import com.skrstop.framework.components.starter.database.configuration.GlobalDatabaseProperties;
import com.skrstop.framework.components.starter.database.entity.AbstractBaseEntity;
import com.skrstop.framework.components.starter.database.entity.AbstractCreateByTimeBaseEntity;
import com.skrstop.framework.components.starter.database.mapper.SuperMapper;
import com.skrstop.framework.components.starter.database.repository.impl.SuperRepositoryImpl;
import com.skrstop.framework.components.util.constant.StringPoolConst;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class SuperRepositoryImplTest {

    @Getter
    @Setter
    @TableName("t_rich")
    static class RichEntity extends AbstractCreateByTimeBaseEntity {
        @TableId("id_col")
        private Long id;
        private String name;
        @PropertyDeleted
        @TableLogic(value = StringPoolConst.ZERO, delval = StringPoolConst.ONE)
        private Boolean deleted;
        @PropertyUpdateBy
        @TableField("update_by")
        private Long updateBy;
        @PropertyUpdateTime
        @TableField("update_time")
        private LocalDateTime updateTime;
    }

    @TableName("t_plain")
    static class PlainEntity extends AbstractBaseEntity {
        @TableId("id_col")
        private Long id;
    }

    interface RichMapper extends SuperMapper<RichEntity> {
    }

    @TableName("t_upd_time_only")
    static class UpdateTimeOnlyEntity extends AbstractBaseEntity {
        @TableId("id_col")
        private Long id;
        @PropertyUpdateTime
        @TableField("update_time")
        private LocalDateTime updateTime;
    }

    @TableName("t_updator_only")
    static class UpdatorOnlyEntity extends AbstractBaseEntity {
        @TableId("id_col")
        private Long id;
        @PropertyUpdater
        private String updater;
    }

    interface PlainMapper extends SuperMapper<PlainEntity> {
    }

    interface UpdateTimeOnlyMapper extends SuperMapper<UpdateTimeOnlyEntity> {
    }

    interface UpdatorOnlyMapper extends SuperMapper<UpdatorOnlyEntity> {
    }

    static class TestRepository extends SuperRepositoryImpl<RichMapper, RichEntity> {
        SqlSessionFactory overrideFactory;

        @Override
        protected SqlSessionFactory getSqlSessionFactory() {
            return overrideFactory != null ? overrideFactory : super.getSqlSessionFactory();
        }
    }

    static class UpdateTimeOnlyRepository extends SuperRepositoryImpl<UpdateTimeOnlyMapper, UpdateTimeOnlyEntity> {
    }

    static class UpdatorOnlyRepository extends SuperRepositoryImpl<UpdatorOnlyMapper, UpdatorOnlyEntity> {
    }

    static class PlainRepository extends SuperRepositoryImpl<PlainMapper, PlainEntity> {
        SqlSessionFactory overrideFactory;

        @Override
        protected SqlSessionFactory getSqlSessionFactory() {
            return overrideFactory != null ? overrideFactory : super.getSqlSessionFactory();
        }
    }

    private TestRepository repository;
    private RichMapper mapper;
    private SqlSession sqlSession;
    private AtomicReference<SqlSession> sessionRef = new AtomicReference<>();

    // baseMapper 用 Mockito mock 即可：AbstractRepository 仅在
    // entityClass/mapperClass 字段为空时才会通过 MapperProxyMetadata
    // 反射读取 MapperProxy 的属性，测试直接注入这两个字段绕开，
    // 同时 getSqlSessionFactory() 被子类覆写返回 mock 工厂。
    private static void injectTypes(Object repo, Class<?> mapperClass, Class<?> entityClass) {
        ReflectionTestUtils.setField(repo, "mapperClass", mapperClass);
        ReflectionTestUtils.setField(repo, "entityClass", entityClass);
    }

    private static SqlSessionFactory factoryOf(SqlSession session) {
        SqlSessionFactory factory = Mockito.mock(SqlSessionFactory.class);
        Mockito.when(factory.openSession(ExecutorType.BATCH)).thenReturn(session);
        Mockito.when(factory.openSession(ExecutorType.BATCH, false)).thenReturn(session);
        Mockito.when(factory.getConfiguration()).thenReturn(new MybatisConfiguration());
        return factory;
    }

    @BeforeClass
    public static void initTableInfo() {
        MapperBuilderAssistant assistant = new MapperBuilderAssistant(new MybatisConfiguration(), "");
        TableInfoHelper.initTableInfo(assistant, RichEntity.class);
        TableInfoHelper.initTableInfo(assistant, PlainEntity.class);
    }

    @Before
    public void setUp() {
        mapper = Mockito.mock(RichMapper.class);
        sqlSession = Mockito.mock(SqlSession.class);
        Mockito.when(sqlSession.insert(Mockito.anyString(), Mockito.any())).thenReturn(1);
        Mockito.when(sqlSession.update(Mockito.anyString(), Mockito.any())).thenReturn(1);
        Mockito.when(sqlSession.selectList(Mockito.anyString(), Mockito.any())).thenReturn(Collections.emptyList());
        Mockito.doAnswer(inv -> null).when(sqlSession).commit();
        Mockito.doAnswer(inv -> null).when(sqlSession).close();
        Mockito.doAnswer(inv -> {
            org.apache.ibatis.executor.BatchResult br = new org.apache.ibatis.executor.BatchResult(
                    Mockito.mock(org.apache.ibatis.mapping.MappedStatement.class), "sql");
            ReflectionTestUtils.setField(br, "updateCounts", new int[]{1});
            return java.util.Collections.singletonList(br);
        }).when(sqlSession).flushStatements();
        sessionRef.set(sqlSession);

        repository = new TestRepository();
        ReflectionTestUtils.setField(repository, "baseMapper", mapper);
        ReflectionTestUtils.setField(repository, "globalDatabaseProperties", new GlobalDatabaseProperties());
        injectTypes(repository, RichMapper.class, RichEntity.class);
        // 调用 @PostConstruct init()
        ReflectionTestUtils.invokeMethod(repository, "init");
    }

    private static PlainRepository plainRepositoryOf(PlainMapper plainMapper) {
        PlainRepository plainRepository = new PlainRepository();
        ReflectionTestUtils.setField(plainRepository, "baseMapper", plainMapper);
        ReflectionTestUtils.setField(plainRepository, "globalDatabaseProperties", new GlobalDatabaseProperties());
        injectTypes(plainRepository, PlainMapper.class, PlainEntity.class);
        ReflectionTestUtils.invokeMethod(plainRepository, "init");
        return plainRepository;
    }

    @Test
    public void initCache() {
        // init 已填充缓存
        Assert.assertNotNull(ReflectionTestUtils.getField(repository, "propertyFieldCache"));
        Assert.assertNotNull(ReflectionTestUtils.getField(repository, "tableInfo"));
    }

    @Test
    public void save() {
        Mockito.when(mapper.insert(Mockito.any(RichEntity.class))).thenReturn(1);
        RichEntity entity = new RichEntity();
        entity.setName("n");
        Assert.assertTrue(repository.save(entity));
        // 自动填充创建信息
        Assert.assertEquals(0L, entity.getCreateBy().longValue());
        Assert.assertNotNull(entity.getCreateTime());
        Mockito.verify(mapper).insert(entity);
    }

    @Test
    public void saveBatchEmpty() {
        SqlSessionFactory factory = factoryOf(sqlSession);
        TestRepository r2 = new TestRepository();
        r2.overrideFactory = factory;
        ReflectionTestUtils.setField(r2, "baseMapper", mapper);
        injectTypes(r2, RichMapper.class, RichEntity.class);
        ReflectionTestUtils.setField(r2, "globalDatabaseProperties", new GlobalDatabaseProperties());
        ReflectionTestUtils.invokeMethod(r2, "init");
        try {
            r2.saveBatch(Collections.emptyList());
            Assert.fail("should throw");
        } catch (MybatisPlusException expected) {
            // 空集合 batchSize=0 被 SqlHelper 断言拦截
        }
        Mockito.verify(sqlSession, Mockito.never()).insert(Mockito.anyString(), Mockito.any());
    }

    @Test
    public void saveBatchNonEmpty() {
        Mockito.when(sqlSession.insert(Mockito.contains("insert"), Mockito.any())).thenReturn(1);
        TestRepository r3 = new TestRepository();
        r3.overrideFactory = factoryOf(sqlSession);
        ReflectionTestUtils.setField(r3, "baseMapper", mapper);
        ReflectionTestUtils.setField(r3, "globalDatabaseProperties", new GlobalDatabaseProperties());
        injectTypes(r3, RichMapper.class, RichEntity.class);
        ReflectionTestUtils.invokeMethod(r3, "init");
        RichEntity e1 = new RichEntity();
        RichEntity e2 = new RichEntity();
        Assert.assertTrue(r3.saveBatch(Arrays.asList(e1, e2)));
        Assert.assertNotNull(e1.getCreateTime());
        Mockito.verify(sqlSession, Mockito.atLeastOnce()).insert(Mockito.anyString(), Mockito.any());
    }

    @Test
    public void updateById() {
        Mockito.when(mapper.updateById(Mockito.any(RichEntity.class))).thenReturn(1);
        RichEntity entity = new RichEntity();
        Assert.assertTrue(repository.updateById(entity));
        Assert.assertEquals(0L, entity.getUpdateBy().longValue());
        Assert.assertNotNull(entity.getUpdateTime());
        Mockito.verify(mapper).updateById(entity);
    }

    @Test
    public void updateWrapper() {
        Mockito.when(mapper.update(Mockito.any(), Mockito.any(UpdateWrapper.class))).thenReturn(1);
        UpdateWrapper<RichEntity> wrapper = new UpdateWrapper<>();
        wrapper.eq("id_col", 1L);
        Assert.assertTrue(repository.update(wrapper));
        // 自动填充 update set 片段
        Assert.assertNotNull(wrapper.getSqlSet());
        Mockito.verify(mapper).update(null, wrapper);
    }

    @Test
    public void updateEntityAndWrapper() {
        Mockito.when(mapper.update(Mockito.any(RichEntity.class), Mockito.any(UpdateWrapper.class))).thenReturn(1);
        RichEntity entity = new RichEntity();
        UpdateWrapper<RichEntity> wrapper = new UpdateWrapper<>();
        Assert.assertTrue(repository.update(entity, wrapper));
        Assert.assertEquals(0L, entity.getUpdateBy().longValue());
        Mockito.verify(mapper).update(entity, wrapper);
    }

    @Test
    public void updateBatchById() {
        Mockito.when(sqlSession.update(Mockito.contains("update"), Mockito.any())).thenReturn(1);
        TestRepository spy = Mockito.spy(repository);
        spy.overrideFactory = factoryOf(sqlSession);
        RichEntity e1 = new RichEntity();
        e1.id = 1L;
        RichEntity e2 = new RichEntity();
        e2.id = 2L;
        Assert.assertTrue(spy.updateBatchById(Arrays.asList(e1, e2)));
        Mockito.verify(sqlSession, Mockito.atLeastOnce()).update(Mockito.anyString(), Mockito.any());
    }

    @Test
    public void saveOrUpdateInsertAndNew() {
        // id 为空 -> save
        TestRepository spy = Mockito.spy(repository);
        Mockito.doReturn(true).when(spy).save(Mockito.any(RichEntity.class));
        Mockito.doReturn(false).when(spy).updateById(Mockito.any(RichEntity.class));
        RichEntity entity = new RichEntity();
        Assert.assertFalse(spy.saveOrUpdate(entity));
        Mockito.verify(spy).save(entity);

        // id 非空且存在 -> updateById
        RichEntity exist = new RichEntity();
        exist.id = 9L;
        Mockito.doReturn(exist).when(spy).getById(9L);
        RichEntity entity2 = new RichEntity();
        entity2.id = 9L;
        Assert.assertFalse(spy.saveOrUpdate(entity2));
        Mockito.verify(spy).updateById(entity2);

        // id 非空但不存在 -> save
        Mockito.doReturn(null).when(spy).getById(10L);
        RichEntity entity3 = new RichEntity();
        entity3.id = 10L;
        Assert.assertFalse(spy.saveOrUpdate(entity3));
        Mockito.verify(spy, Mockito.times(2)).save(Mockito.any(RichEntity.class));
    }

    @Test
    public void saveOrUpdateNullEntity() {
        Assert.assertFalse(repository.saveOrUpdate(null));
    }

    @Test
    public void saveOrUpdateBatchEmpty() {
        TestRepository r5 = new TestRepository();
        r5.overrideFactory = factoryOf(sqlSession);
        ReflectionTestUtils.setField(r5, "baseMapper", mapper);
        ReflectionTestUtils.setField(r5, "globalDatabaseProperties", new GlobalDatabaseProperties());
        injectTypes(r5, RichMapper.class, RichEntity.class);
        ReflectionTestUtils.invokeMethod(r5, "init");
        try {
            r5.saveOrUpdateBatch(Collections.emptyList());
            Assert.fail("should throw");
        } catch (MybatisPlusException expected) {
            // 空集合 batchSize=0 被 SqlHelper 断言拦截
        }
    }

    @Test
    public void saveOrUpdateBatchNonEmpty() {
        // 第一个实体无 id -> save 路径（selectList 不被调用）
        // 第二个实体有 id 且 selectList 返回非空 -> update 路径
        Mockito.when(sqlSession.selectList(Mockito.anyString(), Mockito.any()))
                .thenReturn(Collections.emptyList(), Collections.singletonList("exists"));
        Mockito.when(sqlSession.insert(Mockito.contains("insert"), Mockito.any())).thenReturn(1);
        Mockito.when(sqlSession.update(Mockito.contains("update"), Mockito.any())).thenReturn(1);
        TestRepository r6 = new TestRepository();
        r6.overrideFactory = factoryOf(sqlSession);
        ReflectionTestUtils.setField(r6, "baseMapper", mapper);
        ReflectionTestUtils.setField(r6, "globalDatabaseProperties", new GlobalDatabaseProperties());
        injectTypes(r6, RichMapper.class, RichEntity.class);
        ReflectionTestUtils.invokeMethod(r6, "init");
        RichEntity toSave = new RichEntity();
        RichEntity noExist = new RichEntity();
        noExist.id = 6L;
        RichEntity toUpdate = new RichEntity();
        toUpdate.id = 7L;
        Assert.assertTrue(r6.saveOrUpdateBatch(Arrays.asList(toSave, noExist, toUpdate)));
        Assert.assertNotNull(toSave.getCreateTime());
        Mockito.verify(sqlSession).insert(Mockito.anyString(), Mockito.eq(toSave));
        Mockito.verify(sqlSession).update(Mockito.anyString(), Mockito.any());
    }

    @Test
    public void removeById() {
        Mockito.when(mapper.removePhysicalById(Mockito.any(Serializable.class), Mockito.eq("t_rich"), Mockito.eq("id_col"))).thenReturn(1);
        Assert.assertTrue(repository.removeById(1L));
        Mockito.when(mapper.removePhysicalById(Mockito.any(Serializable.class), Mockito.eq("t_rich"), Mockito.eq("id_col"))).thenReturn(0);
        Assert.assertFalse(repository.removeById(1L));
    }

    @Test
    public void removeByIds() {
        Mockito.when(mapper.removePhysicalByIds(Mockito.anyCollection(), Mockito.eq("t_rich"), Mockito.eq("id_col"))).thenReturn(2);
        Assert.assertTrue(repository.removeByIds(Arrays.asList(1L, 2L)));
        Mockito.when(mapper.removePhysicalByIds(Mockito.anyCollection(), Mockito.eq("t_rich"), Mockito.eq("id_col"))).thenReturn(0);
        Assert.assertFalse(repository.removeByIds(Arrays.asList(1L, 2L)));
    }

    @Test
    public void removeByMap() {
        Mockito.when(mapper.removePhysicalByMap(Mockito.anyMap(), Mockito.eq("t_rich"))).thenReturn(1);
        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("name", "n");
        Assert.assertTrue(repository.removeByMap(columnMap));
        Mockito.when(mapper.removePhysicalByMap(Mockito.anyMap(), Mockito.eq("t_rich"))).thenReturn(0);
        Assert.assertFalse(repository.removeByMap(columnMap));
    }

    @Test
    public void removeByWrapper() {
        Mockito.when(mapper.removePhysicalByCustom(Mockito.anyString(), Mockito.any(UpdateWrapper.class), Mockito.eq("t_rich"))).thenReturn(1);
        UpdateWrapper<RichEntity> wrapper = new UpdateWrapper<>();
        wrapper.eq("id_col", 1L);
        Assert.assertTrue(repository.remove(wrapper));
        Mockito.when(mapper.removePhysicalByCustom(Mockito.anyString(), Mockito.any(UpdateWrapper.class), Mockito.eq("t_rich"))).thenReturn(0);
        Assert.assertFalse(repository.remove(wrapper));
    }

    @Test
    public void removeLogicAndUndo() {
        Mockito.when(mapper.update(Mockito.any(), Mockito.any(UpdateWrapper.class))).thenReturn(1);
        // byId
        Assert.assertTrue(repository.removeLogicById(1L));
        // byIds
        Assert.assertTrue(repository.removeLogicByIds(Arrays.asList(1L, 2L)));
        // byMap
        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("name", "n");
        Assert.assertTrue(repository.removeLogicByMap(columnMap));
        // byWrapper
        UpdateWrapper<RichEntity> wrapper = new UpdateWrapper<>();
        Assert.assertTrue(repository.removeLogic(wrapper));
        // undo
        Assert.assertTrue(repository.undoRemoveLogicById(1L));
        Assert.assertTrue(repository.undoRemoveLogicByIds(Arrays.asList(1L)));
        Assert.assertTrue(repository.undoRemoveLogicByMap(columnMap));
        Assert.assertTrue(repository.undoRemoveLogic(new UpdateWrapper<>()));
    }

    @Test
    public void removeLogicNullWrapper() {
        Mockito.when(mapper.update(Mockito.any(), Mockito.any())).thenReturn(1);
        Assert.assertTrue(repository.removeLogic(null));
        Assert.assertTrue(repository.undoRemoveLogic(null));
    }

    @Test
    public void removeLogicLambdaWrapper() {
        Mockito.when(mapper.update(Mockito.any(), Mockito.any())).thenReturn(1);
        // sqlSet 为空 -> paramMap 无该字段 -> setSql 分支
        LambdaUpdateWrapper<RichEntity> wrapper = new LambdaUpdateWrapper<>();
        Assert.assertTrue(repository.removeLogic(wrapper));
        // sqlSet 已含字段但参数值缺失 -> paramNameValuePairs.put 分支
        LambdaUpdateWrapper<RichEntity> wrapper2 = new LambdaUpdateWrapper<>();
        wrapper2.setSql("deleted=#{ew.paramNameValuePairs.MPGENVAL1}");
        Assert.assertTrue(repository.undoRemoveLogic(wrapper2));
        Assert.assertNotNull(wrapper2.getParamNameValuePairs().get("MPGENVAL1"));
        // sqlSet 已含字段且参数值缺失 + undo=false -> put 1
        LambdaUpdateWrapper<RichEntity> wrapper3 = new LambdaUpdateWrapper<>();
        wrapper3.setSql("deleted=#{ew.paramNameValuePairs.MPGENVAL2}");
        Assert.assertTrue(repository.removeLogic(wrapper3));
        // put 分支已执行（后续 setUpdateTimeUpdateInfo 可能覆盖参数值）
        Assert.assertTrue(wrapper3.getParamNameValuePairs().containsKey("MPGENVAL2"));
        // undo=true + sqlSet 为空 -> setSql 0
        LambdaUpdateWrapper<RichEntity> wrapper4 = new LambdaUpdateWrapper<>();
        Assert.assertTrue(repository.undoRemoveLogic(wrapper4));
        // 非 UpdateWrapper/LambdaUpdateWrapper -> 不处理
        QueryWrapper<RichEntity> wrapper5 = new QueryWrapper<>();
        Assert.assertTrue(repository.removeLogic(wrapper5));
    }

    @Test
    public void removeLogicWithoutDeletedFieldThrows() {
        PlainMapper plainMapper = Mockito.mock(PlainMapper.class);
        PlainRepository plainRepository = plainRepositoryOf(plainMapper);
        try {
            plainRepository.removeLogicById(1L);
            Assert.fail("should throw");
        } catch (NotSupportedException e) {
            Assert.assertNotNull(e.getMessage());
        }
        try {
            plainRepository.undoRemoveLogicById(1L);
            Assert.fail("should throw");
        } catch (NotSupportedException e) {
            Assert.assertNotNull(e.getMessage());
        }
    }

    @Test
    public void operatorDefaults() {
        Assert.assertEquals(0L, repository.getOperatorId());
        Assert.assertEquals("", repository.getOperatorName());
        Assert.assertTrue(repository.isAutoSetCreateExtraInfo());
        Assert.assertTrue(repository.isAutoSetUpdateExtraInfo());
        Assert.assertTrue(repository.onlySetCreateInfoWhenNull());
        Assert.assertTrue(repository.onlySetUpdateInfoWhenNull());
    }

    @Test
    public void removeLogicWithWrapperNullSafe() {
        Mockito.when(mapper.update(Mockito.any(), Mockito.any(UpdateWrapper.class))).thenReturn(1);
        UpdateWrapper<RichEntity> wrapper = new UpdateWrapper<>();
        Assert.assertTrue(repository.removeLogic(wrapper));
    }

    @Test
    public void updateNullWrapper() {
        Mockito.when(mapper.update(Mockito.any(), Mockito.any())).thenReturn(1);
        Assert.assertTrue(repository.update((Wrapper<RichEntity>) null));
    }

    @Test
    public void updateLambdaWrapper() {
        Mockito.when(mapper.update(Mockito.any(), Mockito.any())).thenReturn(1);
        LambdaUpdateWrapper<RichEntity> wrapper = new LambdaUpdateWrapper<>();
        Assert.assertTrue(repository.update(wrapper));
        Assert.assertNotNull(wrapper.getSqlSet());
    }

    @Test
    public void updateWrapperAllEmptyColumns() {
        Mockito.when(mapper.update(Mockito.any(), Mockito.any())).thenReturn(1);
        PlainMapper plainMapper = Mockito.mock(PlainMapper.class);
        Mockito.when(plainMapper.update(Mockito.any(), Mockito.any())).thenReturn(1);
        // PlainEntity 无 update 相关字段 -> 三个集合全空 -> 直接返回 wrapper
        PlainRepository plainRepository = plainRepositoryOf(plainMapper);
        Assert.assertTrue(plainRepository.update(new UpdateWrapper<PlainEntity>()));
    }

    @Test
    public void skipFillWhenDisabled() {
        Mockito.when(mapper.insert(Mockito.any(RichEntity.class))).thenReturn(1);
        Mockito.when(mapper.updateById(Mockito.any(RichEntity.class))).thenReturn(1);
        Mockito.when(mapper.update(Mockito.any(), Mockito.any())).thenReturn(1);
        TestRepository noFill = new TestRepository() {
            @Override
            public boolean isAutoSetCreateExtraInfo() {
                return false;
            }

            @Override
            public boolean isAutoSetUpdateExtraInfo() {
                return false;
            }
        };
        ReflectionTestUtils.setField(noFill, "baseMapper", mapper);
        ReflectionTestUtils.setField(noFill, "globalDatabaseProperties", new GlobalDatabaseProperties());
        injectTypes(noFill, RichMapper.class, RichEntity.class);
        ReflectionTestUtils.invokeMethod(noFill, "init");
        RichEntity saved = new RichEntity();
        Assert.assertTrue(noFill.save(saved));
        Assert.assertNull(saved.getCreateBy());
        RichEntity updated = new RichEntity();
        updated.id = 1L;
        Assert.assertTrue(noFill.updateById(updated));
        Assert.assertNull(updated.getUpdateBy());
        Assert.assertTrue(noFill.update(new UpdateWrapper<>()));
    }

    @Test
    public void updateWrapperUpdateTimeOnly() {
        Mockito.when(mapper.update(Mockito.any(), Mockito.any())).thenReturn(1);
        // 仅 updateTime 字段 -> updateBy 集合为空但 updateTime 非空 -> 中间条件为 false
        UpdateTimeOnlyRepository repo = new UpdateTimeOnlyRepository();
        UpdateTimeOnlyMapper m = Mockito.mock(UpdateTimeOnlyMapper.class);
        Mockito.when(m.update(Mockito.any(), Mockito.any())).thenReturn(1);
        ReflectionTestUtils.setField(repo, "baseMapper", m);
        ReflectionTestUtils.setField(repo, "globalDatabaseProperties", new GlobalDatabaseProperties());
        injectTypes(repo, UpdateTimeOnlyMapper.class, UpdateTimeOnlyEntity.class);
        ReflectionTestUtils.invokeMethod(repo, "init");
        Assert.assertTrue(repo.update(new UpdateWrapper<>()));
    }

    @Test
    public void updateWrapperUpdatorOnly() {
        Mockito.when(mapper.update(Mockito.any(), Mockito.any())).thenReturn(1);
        // 仅 updater 字段 -> 前两个集合为空但 updater 非空 -> 最后条件为 false
        UpdatorOnlyRepository repo = new UpdatorOnlyRepository();
        UpdatorOnlyMapper m = Mockito.mock(UpdatorOnlyMapper.class);
        Mockito.when(m.update(Mockito.any(), Mockito.any())).thenReturn(1);
        ReflectionTestUtils.setField(repo, "baseMapper", m);
        ReflectionTestUtils.setField(repo, "globalDatabaseProperties", new GlobalDatabaseProperties());
        injectTypes(repo, UpdatorOnlyMapper.class, UpdatorOnlyEntity.class);
        ReflectionTestUtils.invokeMethod(repo, "init");
        Assert.assertTrue(repo.update(new UpdateWrapper<>()));
    }
}
