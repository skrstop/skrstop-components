package com.skrstop.framework.components.starter.database.annotation;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.dynamic.datasource.annotation.DsTxEventListener;
import com.baomidou.dynamic.datasource.tx.DsPropagation;
import com.skrstop.framework.components.starter.database.annotation.property.*;
import com.skrstop.framework.components.starter.database.constant.DatabaseConst;
import com.skrstop.framework.components.starter.database.constant.GlobalConfigConst;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.transaction.event.TransactionPhase;

import java.lang.annotation.*;

public class AnnotationsTest {

    @DSDatabase("db1")
    static class DsHolder {
    }

    @Test
    public void dsDatabase() {
        DSDatabase anno = DsHolder.class.getAnnotation(DSDatabase.class);
        Assert.assertNotNull(anno);
        Assert.assertEquals("db1", anno.value());
        // @AliasFor -> DS.value (合并属性)
        Assert.assertEquals("db1", AnnotationUtils.getAnnotationAttributes(anno).get("value"));
        Assert.assertNotNull(DSDatabase.class.getAnnotation(Documented.class));
        Assert.assertNotNull(DSDatabase.class.getAnnotation(DS.class));
        Assert.assertEquals(GlobalConfigConst.DS_DEFAULT, DSDatabase.class.getAnnotation(DS.class).value());
    }

    @DSDatabaseMaster
    static class MasterHolder {
    }

    @DSDatabaseSlave
    static class SlaveHolder {
    }

    @Test
    public void dsMasterSlave() {
        Assert.assertEquals(GlobalConfigConst.DS_MASTER, AnnotationUtils.findAnnotation(MasterHolder.class, DS.class).value());
        Assert.assertEquals(GlobalConfigConst.DS_SLAVE, AnnotationUtils.findAnnotation(SlaveHolder.class, DS.class).value());
        Assert.assertEquals(GlobalConfigConst.DS_MASTER, DSDatabaseMaster.class.getAnnotation(DS.class).value());
        Assert.assertEquals(GlobalConfigConst.DS_SLAVE, DSDatabaseSlave.class.getAnnotation(DS.class).value());
    }

    @DSDatabaseTransactional(rollbackFor = {IllegalStateException.class}, noRollbackFor = {IllegalArgumentException.class}, propagation = DsPropagation.REQUIRES_NEW)
    static class TxHolder {
    }

    @Test
    public void dsDatabaseTransactional() throws Exception {
        Assert.assertNotNull(AnnotationUtils.findAnnotation(TxHolder.class, DSTransactional.class));
        DSDatabaseTransactional anno = TxHolder.class.getAnnotation(DSDatabaseTransactional.class);
        // 默认值
        DSDatabaseTransactional def = Defaults.class.getAnnotation(DSDatabaseTransactional.class);
        Assert.assertArrayEquals(new Class[]{Exception.class}, def.rollbackFor());
        Assert.assertEquals(0, def.noRollbackFor().length);
        Assert.assertEquals(DsPropagation.REQUIRED, def.propagation());
        // 别名
        Assert.assertArrayEquals(new Class[]{IllegalStateException.class}, anno.rollbackFor());
        Assert.assertArrayEquals(new Class[]{IllegalArgumentException.class}, anno.noRollbackFor());
        Assert.assertEquals(DsPropagation.REQUIRES_NEW, anno.propagation());
        // 元注解合并
        java.util.Map<String, Object> attrs = AnnotationUtils.getAnnotationAttributes(anno);
        Assert.assertArrayEquals(new Class[]{IllegalStateException.class}, (Class[]) attrs.get("rollbackFor"));
        Assert.assertEquals(DsPropagation.REQUIRES_NEW, attrs.get("propagation"));
        Assert.assertNotNull(DSDatabaseTransactional.class.getAnnotation(Documented.class));
    }

    @DSDatabaseTransactional
    static class Defaults {
    }

    static class ListenerHolder {
        @DsDatabaseTxEventListener(phase = TransactionPhase.AFTER_ROLLBACK, classes = {IllegalStateException.class}, condition = "c1")
        public void onEvent() {
        }
    }

    static class ListenerDefaults {
        @DsDatabaseTxEventListener
        public void onEvent() {
        }
    }

    @Test
    public void dsDatabaseTxEventListener() throws Exception {
        DsDatabaseTxEventListener anno = ListenerHolder.class.getDeclaredMethod("onEvent").getAnnotation(DsDatabaseTxEventListener.class);
        // 默认值
        DsDatabaseTxEventListener def = ListenerDefaults.class.getDeclaredMethod("onEvent").getAnnotation(DsDatabaseTxEventListener.class);
        Assert.assertEquals(TransactionPhase.AFTER_COMMIT, def.phase());
        Assert.assertEquals(0, def.value().length);
        Assert.assertEquals(0, def.classes().length);
        Assert.assertEquals("", def.condition());
        // 别名
        Assert.assertEquals(TransactionPhase.AFTER_ROLLBACK, anno.phase());
        Assert.assertArrayEquals(new Class[]{IllegalStateException.class}, anno.classes());
        Assert.assertEquals("c1", anno.condition());
        Assert.assertNotNull(DsDatabaseTxEventListener.class.getAnnotation(DsTxEventListener.class));
        Assert.assertNotNull(DsDatabaseTxEventListener.class.getAnnotation(Documented.class));
    }

    @Transactional
    static class SpringTxHolder {
    }

    @Transactional(value = "mgr2", transactionManager = "mgr2", label = {"l1"}, propagation = org.springframework.transaction.annotation.Propagation.REQUIRES_NEW,
            isolation = org.springframework.transaction.annotation.Isolation.SERIALIZABLE, timeout = 5, timeoutString = "6", readOnly = true,
            rollbackFor = {IllegalStateException.class}, rollbackForClassName = {"IE"}, noRollbackFor = {IllegalArgumentException.class}, noRollbackForClassName = {"IAE"})
    static class SpringTxHolder2 {
    }

    @Test
    public void transactionalAnnotation() {
        // 默认值 -> DatabaseConst.TRANSACTION_NAME_DATABASE (合并属性)
        Transactional def = SpringTxHolder.class.getAnnotation(Transactional.class);
        java.util.Map<String, Object> defAttrs = AnnotationUtils.getAnnotationAttributes(def);
        Assert.assertEquals(DatabaseConst.TRANSACTION_NAME_DATABASE, defAttrs.get("value"));
        Assert.assertEquals(DatabaseConst.TRANSACTION_NAME_DATABASE, defAttrs.get("transactionManager"));
        Assert.assertEquals(DatabaseConst.TRANSACTION_NAME_DATABASE, def.value());
        Assert.assertEquals(DatabaseConst.TRANSACTION_NAME_DATABASE, def.transactionManager());
        Assert.assertEquals(0, def.label().length);
        Assert.assertEquals(org.springframework.transaction.annotation.Propagation.REQUIRED, def.propagation());
        Assert.assertEquals(org.springframework.transaction.annotation.Isolation.DEFAULT, def.isolation());
        Assert.assertEquals(org.springframework.transaction.TransactionDefinition.TIMEOUT_DEFAULT, def.timeout());
        Assert.assertEquals("", def.timeoutString());
        Assert.assertFalse(def.readOnly());
        Assert.assertEquals(0, def.rollbackFor().length);
        Assert.assertEquals(0, def.rollbackForClassName().length);
        Assert.assertEquals(0, def.noRollbackFor().length);
        Assert.assertEquals(0, def.noRollbackForClassName().length);
        // 别名
        Transactional anno = SpringTxHolder2.class.getAnnotation(Transactional.class);
        Assert.assertEquals("mgr2", anno.value());
        Assert.assertEquals("mgr2", anno.transactionManager());
        Assert.assertArrayEquals(new String[]{"l1"}, anno.label());
        Assert.assertEquals(org.springframework.transaction.annotation.Propagation.REQUIRES_NEW, anno.propagation());
        Assert.assertEquals(org.springframework.transaction.annotation.Isolation.SERIALIZABLE, anno.isolation());
        Assert.assertEquals(5, anno.timeout());
        Assert.assertEquals("6", anno.timeoutString());
        Assert.assertTrue(anno.readOnly());
        Assert.assertArrayEquals(new Class[]{IllegalStateException.class}, anno.rollbackFor());
        Assert.assertArrayEquals(new String[]{"IE"}, anno.rollbackForClassName());
        Assert.assertArrayEquals(new Class[]{IllegalArgumentException.class}, anno.noRollbackFor());
        Assert.assertArrayEquals(new String[]{"IAE"}, anno.noRollbackForClassName());
        // 元注解
        Assert.assertNotNull(Transactional.class.getAnnotation(org.springframework.transaction.annotation.Transactional.class));
        Assert.assertNotNull(Transactional.class.getAnnotation(Inherited.class));
        Assert.assertNotNull(Transactional.class.getAnnotation(Documented.class));
    }

    @PropertyId
    Object f1;
    @PropertyCreateBy
    Object f2;
    @PropertyCreateTime
    Object f3;
    @PropertyCreator
    Object f4;
    @PropertyDeleted
    Object f5;
    @PropertyUpdateBy
    Object f7;
    @PropertyUpdateTime
    Object f8;
    @PropertyUpdater
    Object f9;
    @PropertyVersion
    Object f10;

    @PropertyTableName("t6")
    static class TableNameHolder {
    }

    @Test
    public void tableNameAnnotation() {
        Assert.assertNotNull(TableNameHolder.class.getAnnotation(PropertyTableName.class));
        Assert.assertEquals("t6", TableNameHolder.class.getAnnotation(PropertyTableName.class).value());
    }

    @Test
    public void propertyAnnotations() throws Exception {
        Class<?>[] all = {PropertyId.class, PropertyCreateBy.class, PropertyCreateTime.class,
                PropertyCreator.class, PropertyDeleted.class, PropertyTableName.class,
                PropertyUpdateBy.class, PropertyUpdateTime.class, PropertyUpdater.class,
                PropertyVersion.class};
        for (Class<?> clazz : all) {
            Assert.assertNotNull(clazz.getAnnotation(Target.class));
            Assert.assertNotNull(clazz.getAnnotation(Retention.class));
            Assert.assertEquals(RetentionPolicy.RUNTIME, clazz.getAnnotation(Retention.class).value());
            Assert.assertNotNull(clazz.getAnnotation(Documented.class));
        }
        Assert.assertNotNull(AnnotationsTest.class.getDeclaredField("f1").getAnnotation(PropertyId.class));
    }
}
