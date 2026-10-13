package com.skrstop.framework.components.starter.mongodb.annotation;

import com.skrstop.framework.components.starter.mongodb.constant.MongodbConst;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;
import java.util.Map;

public class AnnotationsTest {

    @DSMongo("slave")
    static class Holder {
    }

    static class MethodHolder {
        @DSMongo
        public void method() {
        }
    }

    static class ExprHolder {
        @DSMongo("#key")
        public void method() {
        }
    }

    @Transactional
    static class TxHolder {
        @TransactionalReactive
        public void method() {
        }
    }

    @Test
    public void dsMongoDefaults() throws Exception {
        // 方法上无 value 的注解 -> 默认 master
        Method method = MethodHolder.class.getMethod("method");
        DSMongo dsMongo = method.getAnnotation(DSMongo.class);
        Assert.assertNotNull(dsMongo);
        Map<String, Object> attrs = AnnotationUtils.getAnnotationAttributes(dsMongo);
        Assert.assertEquals("master", attrs.get("value"));
        // 类上指定 value
        DSMongo slave = AnnotationUtils.findAnnotation(Holder.class, DSMongo.class);
        Assert.assertEquals("slave", slave.value());
    }

    @Test
    public void dsMongoOnMethod() throws Exception {
        Method method = MethodHolder.class.getMethod("method");
        DSMongo dsMongo = method.getAnnotation(DSMongo.class);
        Assert.assertEquals("master", dsMongo.value());
        Method exprMethod = ExprHolder.class.getMethod("method");
        Assert.assertEquals("#key", exprMethod.getAnnotation(DSMongo.class).value());
    }

    @Test
    public void transactionalAliases() {
        Transactional tx = TxHolder.class.getAnnotation(Transactional.class);
        Assert.assertNotNull(tx);
        Map<String, Object> attrs = AnnotationUtils.getAnnotationAttributes(tx);
        // @AliasFor 已合成：value/transactionManager 均为默认 mongo 事务管理器名
        Assert.assertEquals(MongodbConst.TRANSACTION_NAME_MONGO, attrs.get("value"));
        Assert.assertEquals(MongodbConst.TRANSACTION_NAME_MONGO, attrs.get("transactionManager"));
        Assert.assertEquals(false, attrs.get("readOnly"));
    }

    @Test
    public void transactionalReactiveAliases() throws Exception {
        Method method = TxHolder.class.getMethod("method");
        TransactionalReactive tx = method.getAnnotation(TransactionalReactive.class);
        Assert.assertNotNull(tx);
        Map<String, Object> attrs = AnnotationUtils.getAnnotationAttributes(tx);
        Assert.assertEquals(MongodbConst.TRANSACTION_REACTIVE_NAME_MONGO, attrs.get("value"));
        Assert.assertEquals(MongodbConst.TRANSACTION_REACTIVE_NAME_MONGO, attrs.get("transactionManager"));
    }
}
