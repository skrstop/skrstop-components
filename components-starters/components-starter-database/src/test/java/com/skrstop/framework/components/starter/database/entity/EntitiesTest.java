package com.skrstop.framework.components.starter.database.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.Version;
import com.skrstop.framework.components.starter.database.annotation.property.*;
import com.skrstop.framework.components.starter.database.entity.deleted.*;
import com.skrstop.framework.components.starter.database.entity.deletedVersion.*;
import com.skrstop.framework.components.starter.database.entity.version.*;
import com.skrstop.framework.components.util.constant.StringPoolConst;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;

public class EntitiesTest {

    static class Base extends AbstractBaseEntity {
    }

    static class CreateByTime extends AbstractCreateByTimeBaseEntity {
    }

    static class CreateTime extends AbstractCreateTimeBaseEntity {
    }

    static class CreatorTime extends AbstractCreatorTimeBaseEntity {
    }

    static class UpdateByTime extends AbstractUpdateByTimeBaseEntity {
    }

    static class UpdateTime extends AbstractUpdateTimeBaseEntity {
    }

    static class UpdaterTime extends AbstractUpdaterTimeBaseEntity {
    }

    static class Deleted extends AbstractDeletedBaseEntity {
    }

    static class CreateByTimeDeleted extends AbstractCreateByTimeDeletedBaseEntity {
    }

    static class CreateTimeDeleted extends AbstractCreateTimeDeletedBaseEntity {
    }

    static class CreatorTimeDeleted extends AbstractCreatorTimeDeletedBaseEntity {
    }

    static class UpdateByTimeDeleted extends AbstractUpdateByTimeDeletedBaseEntity {
    }

    static class UpdateTimeDeleted extends AbstractUpdateTimeDeletedBaseEntity {
    }

    static class UpdaterTimeDeleted extends AbstractUpdaterTimeDeletedBaseEntity {
    }

    static class Versioned extends AbstractVersionBaseEntity {
    }

    static class CreateByTimeVersion extends AbstractCreateByTimeVersionBaseEntity {
    }

    static class CreateTimeVersion extends AbstractCreateTimeVersionBaseEntity {
    }

    static class CreatorTimeVersion extends AbstractCreatorTimeVersionBaseEntity {
    }

    static class UpdateByTimeVersion extends AbstractUpdateByTimeVersionBaseEntity {
    }

    static class UpdateTimeVersion extends AbstractUpdateTimeVersionBaseEntity {
    }

    static class UpdaterTimeVersion extends AbstractUpdaterTimeVersionBaseEntity {
    }

    static class DeletedVersion extends AbstractDeletedVersionBaseEntity {
    }

    static class CreateByTimeDeletedVersion extends AbstractCreateByTimeDeletedVersionBaseEntity {
    }

    static class CreateTimeDeletedVersion extends AbstractCreateTimeDeletedVersionBaseEntity {
    }

    static class CreatorTimeDeletedVersion extends AbstractCreatorTimeDeletedVersionBaseEntity {
    }

    static class UpdateByTimeDeletedVersion extends AbstractUpdateByTimeDeletedVersionBaseEntity {
    }

    static class UpdateTimeDeletedVersion extends AbstractUpdateTimeDeletedVersionBaseEntity {
    }

    static class UpdaterTimeDeletedVersion extends AbstractUpdaterTimeDeletedVersionBaseEntity {
    }

    @Test
    public void baseEntity() {
        Base entity = new Base();
        Assert.assertNotNull(entity);
    }

    @Test
    public void createByTimeFields() throws Exception {
        CreateByTime entity = new CreateByTime();
        entity.setCreateBy(1L);
        entity.setCreateTime(LocalDateTime.of(2020, 1, 1, 0, 0));
        Assert.assertEquals(1L, entity.getCreateBy().longValue());
        Assert.assertEquals(LocalDateTime.of(2020, 1, 1, 0, 0), entity.getCreateTime());
        Assert.assertNotNull(findField(CreateByTime.class, "createBy").getAnnotation(PropertyCreateBy.class));
        Assert.assertNotNull(findField(CreateByTime.class, "createTime").getAnnotation(PropertyCreateTime.class));
    }

    @Test
    public void createTimeFields() throws Exception {
        CreateTime entity = new CreateTime();
        entity.setCreateTime(LocalDateTime.now());
        Assert.assertNotNull(entity.getCreateTime());
        Assert.assertNotNull(findField(CreateTime.class, "createTime").getAnnotation(PropertyCreateTime.class));
    }

    @Test
    public void creatorTimeFields() throws Exception {
        CreatorTime entity = new CreatorTime();
        entity.setCreator("creator");
        entity.setCreateBy(2L);
        entity.setCreateTime(LocalDateTime.now());
        Assert.assertEquals("creator", entity.getCreator());
        Assert.assertEquals(2L, entity.getCreateBy().longValue());
        Assert.assertNotNull(entity.getCreateTime());
        Assert.assertNotNull(findField(CreatorTime.class, "creator").getAnnotation(PropertyCreator.class));
    }

    @Test
    public void updateByTimeFields() throws Exception {
        UpdateByTime entity = new UpdateByTime();
        entity.setUpdateBy(3L);
        entity.setUpdateTime(LocalDateTime.now());
        Assert.assertEquals(3L, entity.getUpdateBy().longValue());
        Assert.assertNotNull(entity.getUpdateTime());
        Assert.assertNotNull(findField(UpdateByTime.class, "updateBy").getAnnotation(PropertyUpdateBy.class));
        Assert.assertNotNull(findField(UpdateByTime.class, "updateTime").getAnnotation(PropertyUpdateTime.class));
    }

    @Test
    public void updateTimeFields() throws Exception {
        UpdateTime entity = new UpdateTime();
        entity.setUpdateTime(LocalDateTime.now());
        Assert.assertNotNull(entity.getUpdateTime());
        Assert.assertNotNull(findField(UpdateTime.class, "updateTime").getAnnotation(PropertyUpdateTime.class));
    }

    @Test
    public void updaterTimeFields() throws Exception {
        UpdaterTime entity = new UpdaterTime();
        entity.setUpdater("updater");
        entity.setUpdateBy(4L);
        entity.setUpdateTime(LocalDateTime.now());
        Assert.assertEquals("updater", entity.getUpdater());
        Assert.assertEquals(4L, entity.getUpdateBy().longValue());
        Assert.assertNotNull(findField(UpdaterTime.class, "updater").getAnnotation(PropertyUpdater.class));
    }

    private java.lang.reflect.Field findField(Class<?> clazz, String name) throws Exception {
        for (Class<?> c = clazz; c != null; c = c.getSuperclass()) {
            try {
                return c.getDeclaredField(name);
            } catch (NoSuchFieldException e) {
                // 继续向上找
            }
        }
        throw new NoSuchFieldException(name);
    }

    private void assertDeleted(Class<?> clazz) throws Exception {
        java.lang.reflect.Field field = findField(clazz, "deleted");
        Assert.assertNotNull(field.getAnnotation(PropertyDeleted.class));
        TableLogic tableLogic = field.getAnnotation(TableLogic.class);
        Assert.assertNotNull(tableLogic);
        Assert.assertEquals(StringPoolConst.ZERO, tableLogic.value());
        Assert.assertEquals(StringPoolConst.ONE, tableLogic.delval());
    }

    private void assertVersion(Class<?> clazz) throws Exception {
        java.lang.reflect.Field field = findField(clazz, "version");
        Assert.assertNotNull(field.getAnnotation(PropertyVersion.class));
        Assert.assertNotNull(field.getAnnotation(Version.class));
    }

    @Test
    public void deletedEntities() throws Exception {
        Deleted a = new Deleted();
        a.setDeleted(true);
        Assert.assertTrue(a.getDeleted());
        assertDeleted(Deleted.class);
        CreateByTimeDeleted b = new CreateByTimeDeleted();
        b.setDeleted(false);
        b.setCreateBy(1L);
        Assert.assertFalse(b.getDeleted());
        assertDeleted(CreateByTimeDeleted.class);
        CreateTimeDeleted c = new CreateTimeDeleted();
        c.setDeleted(true);
        Assert.assertTrue(c.getDeleted());
        assertDeleted(CreateTimeDeleted.class);
        CreatorTimeDeleted d = new CreatorTimeDeleted();
        d.setDeleted(true);
        d.setCreator("c");
        Assert.assertTrue(d.getDeleted());
        assertDeleted(CreatorTimeDeleted.class);
        UpdateByTimeDeleted e = new UpdateByTimeDeleted();
        e.setDeleted(true);
        e.setUpdateBy(2L);
        Assert.assertTrue(e.getDeleted());
        assertDeleted(UpdateByTimeDeleted.class);
        UpdateTimeDeleted f = new UpdateTimeDeleted();
        f.setDeleted(true);
        Assert.assertTrue(f.getDeleted());
        assertDeleted(UpdateTimeDeleted.class);
        UpdaterTimeDeleted g = new UpdaterTimeDeleted();
        g.setDeleted(true);
        g.setCreator("u");
        Assert.assertTrue(g.getDeleted());
        assertDeleted(UpdaterTimeDeleted.class);
    }

    @Test
    public void versionEntities() throws Exception {
        Versioned a = new Versioned();
        a.setVersion(1L);
        Assert.assertEquals(1L, a.getVersion().longValue());
        assertVersion(Versioned.class);
        CreateByTimeVersion b = new CreateByTimeVersion();
        b.setVersion(2L);
        Assert.assertEquals(2L, b.getVersion().longValue());
        assertVersion(CreateByTimeVersion.class);
        CreateTimeVersion c = new CreateTimeVersion();
        c.setVersion(3L);
        Assert.assertEquals(3L, c.getVersion().longValue());
        assertVersion(CreateTimeVersion.class);
        CreatorTimeVersion d = new CreatorTimeVersion();
        d.setVersion(4L);
        Assert.assertEquals(4L, d.getVersion().longValue());
        assertVersion(CreatorTimeVersion.class);
        UpdateByTimeVersion e = new UpdateByTimeVersion();
        e.setVersion(5L);
        Assert.assertEquals(5L, e.getVersion().longValue());
        assertVersion(UpdateByTimeVersion.class);
        UpdateTimeVersion f = new UpdateTimeVersion();
        f.setVersion(6L);
        Assert.assertEquals(6L, f.getVersion().longValue());
        assertVersion(UpdateTimeVersion.class);
        UpdaterTimeVersion g = new UpdaterTimeVersion();
        g.setVersion(7L);
        Assert.assertEquals(7L, g.getVersion().longValue());
        assertVersion(UpdaterTimeVersion.class);
    }

    @Test
    public void deletedVersionEntities() throws Exception {
        DeletedVersion a = new DeletedVersion();
        a.setDeleted(true);
        a.setVersion(1L);
        Assert.assertTrue(a.getDeleted());
        assertDeleted(DeletedVersion.class);
        assertVersion(DeletedVersion.class);
        CreateByTimeDeletedVersion b = new CreateByTimeDeletedVersion();
        b.setDeleted(true);
        assertDeleted(CreateByTimeDeletedVersion.class);
        assertVersion(CreateByTimeDeletedVersion.class);
        CreateTimeDeletedVersion c = new CreateTimeDeletedVersion();
        c.setDeleted(true);
        assertDeleted(CreateTimeDeletedVersion.class);
        assertVersion(CreateTimeDeletedVersion.class);
        CreatorTimeDeletedVersion d = new CreatorTimeDeletedVersion();
        d.setDeleted(true);
        assertDeleted(CreatorTimeDeletedVersion.class);
        assertVersion(CreatorTimeDeletedVersion.class);
        UpdateByTimeDeletedVersion e = new UpdateByTimeDeletedVersion();
        e.setDeleted(true);
        assertDeleted(UpdateByTimeDeletedVersion.class);
        assertVersion(UpdateByTimeDeletedVersion.class);
        UpdateTimeDeletedVersion f = new UpdateTimeDeletedVersion();
        f.setDeleted(true);
        assertDeleted(UpdateTimeDeletedVersion.class);
        assertVersion(UpdateTimeDeletedVersion.class);
        UpdaterTimeDeletedVersion g = new UpdaterTimeDeletedVersion();
        g.setDeleted(true);
        assertDeleted(UpdaterTimeDeletedVersion.class);
        assertVersion(UpdaterTimeDeletedVersion.class);
    }
}
