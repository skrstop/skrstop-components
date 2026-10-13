package com.skrstop.framework.components.starter.mongodb.entity;

import cn.hutool.core.util.ReflectUtil;
import com.skrstop.framework.components.starter.mongodb.entity.deleted.*;
import com.skrstop.framework.components.starter.mongodb.entity.deletedVersion.*;
import com.skrstop.framework.components.starter.mongodb.entity.version.*;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EntitiesTest {

    static class CreateByTimeBaseEntityImpl extends AbstractCreateByTimeBaseEntity {
    }

    static class CreateTimeBaseEntityImpl extends AbstractCreateTimeBaseEntity {
    }

    static class CreatorTimeBaseEntityImpl extends AbstractCreatorTimeBaseEntity {
    }

    static class UpdateByTimeBaseEntityImpl extends AbstractUpdateByTimeBaseEntity {
    }

    static class UpdateTimeBaseEntityImpl extends AbstractUpdateTimeBaseEntity {
    }

    static class UpdaterTimeBaseEntityImpl extends AbstractUpdaterTimeBaseEntity {
    }

    static class VersionBaseEntityImpl extends AbstractVersionBaseEntity {
    }

    static class CreateByTimeVersionBaseEntityImpl extends AbstractCreateByTimeVersionBaseEntity {
    }

    static class CreateTimeVersionBaseEntityImpl extends AbstractCreateTimeVersionBaseEntity {
    }

    static class CreatorTimeVersionBaseEntityImpl extends AbstractCreatorTimeVersionBaseEntity {
    }

    static class UpdateByTimeVersionBaseEntityImpl extends AbstractUpdateByTimeVersionBaseEntity {
    }

    static class UpdateTimeVersionBaseEntityImpl extends AbstractUpdateTimeVersionBaseEntity {
    }

    static class UpdaterTimeVersionBaseEntityImpl extends AbstractUpdaterTimeVersionBaseEntity {
    }

    static class DeletedBaseEntityImpl extends AbstractDeletedBaseEntity {
    }

    static class CreateByTimeDeletedBaseEntityImpl extends AbstractCreateByTimeDeletedBaseEntity {
    }

    static class CreateTimeDeletedBaseEntityImpl extends AbstractCreateTimeDeletedBaseEntity {
    }

    static class CreatorTimeDeletedBaseEntityImpl extends AbstractCreatorTimeDeletedBaseEntity {
    }

    static class UpdateByTimeDeletedBaseEntityImpl extends AbstractUpdateByTimeDeletedBaseEntity {
    }

    static class UpdateTimeDeletedBaseEntityImpl extends AbstractUpdateTimeDeletedBaseEntity {
    }

    static class UpdaterTimeDeletedBaseEntityImpl extends AbstractUpdaterTimeDeletedBaseEntity {
    }

    static class DeletedVersionBaseEntityImpl extends AbstractDeletedVersionBaseEntity {
    }

    static class CreateByTimeDeletedVersionBaseEntityImpl extends AbstractCreateByTimeDeletedVersionBaseEntity {
    }

    static class CreateTimeDeletedVersionBaseEntityImpl extends AbstractCreateTimeDeletedVersionBaseEntity {
    }

    static class CreatorTimeDeletedVersionBaseEntityImpl extends AbstractCreatorTimeDeletedVersionBaseEntity {
    }

    static class UpdateByTimeDeletedVersionBaseEntityImpl extends AbstractUpdateByTimeDeletedVersionBaseEntity {
    }

    static class UpdateTimeDeletedVersionBaseEntityImpl extends AbstractUpdateTimeDeletedVersionBaseEntity {
    }

    static class UpdaterTimeDeletedVersionBaseEntityImpl extends AbstractUpdaterTimeDeletedVersionBaseEntity {
    }

    private static final Class<?>[] CONCRETE_ENTITIES = {
            CreateByTimeBaseEntityImpl.class,
            CreateTimeBaseEntityImpl.class,
            CreatorTimeBaseEntityImpl.class,
            UpdateByTimeBaseEntityImpl.class,
            UpdateTimeBaseEntityImpl.class,
            UpdaterTimeBaseEntityImpl.class,
            VersionBaseEntityImpl.class,
            CreateByTimeVersionBaseEntityImpl.class,
            CreateTimeVersionBaseEntityImpl.class,
            CreatorTimeVersionBaseEntityImpl.class,
            UpdateByTimeVersionBaseEntityImpl.class,
            UpdateTimeVersionBaseEntityImpl.class,
            UpdaterTimeVersionBaseEntityImpl.class,
            DeletedBaseEntityImpl.class,
            CreateByTimeDeletedBaseEntityImpl.class,
            CreateTimeDeletedBaseEntityImpl.class,
            CreatorTimeDeletedBaseEntityImpl.class,
            UpdateByTimeDeletedBaseEntityImpl.class,
            UpdateTimeDeletedBaseEntityImpl.class,
            UpdaterTimeDeletedBaseEntityImpl.class,
            DeletedVersionBaseEntityImpl.class,
            CreateByTimeDeletedVersionBaseEntityImpl.class,
            CreateTimeDeletedVersionBaseEntityImpl.class,
            CreatorTimeDeletedVersionBaseEntityImpl.class,
            UpdateByTimeDeletedVersionBaseEntityImpl.class,
            UpdateTimeDeletedVersionBaseEntityImpl.class,
            UpdaterTimeDeletedVersionBaseEntityImpl.class,
    };

    private static Object defaultValue(Class<?> type) {
        if (type == Long.class) {
            return 1L;
        }
        if (type == Boolean.class) {
            return Boolean.TRUE;
        }
        if (type == LocalDateTime.class) {
            return LocalDateTime.now();
        }
        if (type == String.class) {
            return "v";
        }
        return null;
    }

    /**
     * 通过具体子类实例化并读写全部字段，覆盖 lombok getter/setter
     */
    @Test
    public void entitiesGettersAndSetters() {
        for (Class<?> clazz : CONCRETE_ENTITIES) {
            Object entity = ReflectUtil.newInstance(clazz);
            for (Field field : allFields(clazz)) {
                Object value = defaultValue(field.getType());
                if (value == null) {
                    continue;
                }
                String cap = Character.toUpperCase(field.getName().charAt(0)) + field.getName().substring(1);
                ReflectUtil.invoke(entity, "set" + cap, value);
                Assert.assertEquals(clazz.getName() + "." + field.getName(),
                        value, ReflectUtil.invoke(entity, "get" + cap));
            }
        }
    }

    private static List<Field> allFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        for (Class<?> c = clazz; c != null && c != Object.class; c = c.getSuperclass()) {
            for (Field field : c.getDeclaredFields()) {
                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }
                fields.add(field);
            }
        }
        return fields;
    }
}
