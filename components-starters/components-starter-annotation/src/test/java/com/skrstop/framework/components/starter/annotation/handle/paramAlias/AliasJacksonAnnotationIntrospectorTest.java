package com.skrstop.framework.components.starter.annotation.handle.paramAlias;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospectorPair;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.skrstop.framework.components.starter.annotation.anno.paramAlias.RequestParamAlias;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * @author 蒋时华
 */
public class AliasJacksonAnnotationIntrospectorTest {

    private enum TestEnum {
        A,
        @RequestParamAlias({"aliasB1", "aliasB2"})
        B
    }

    private static class FieldBean {
        @RequestParamAlias({"aliasName"})
        private String name;

        private String plain;
    }

    @Test
    public void testFindEnumAliasesDeprecated() throws Exception {
        AliasJacksonAnnotationIntrospector introspector = new AliasJacksonAnnotationIntrospector();
        TestEnum[] enumValues = TestEnum.values();
        String[][] aliasList = new String[enumValues.length][];
        introspector.findEnumAliases(TestEnum.class, enumValues, aliasList);
        Assert.assertArrayEquals(new String[]{"aliasB1", "aliasB2"}, aliasList[1]);
        Assert.assertNull(aliasList[0]);
    }

    @Test
    public void testFindEnumAliases() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        AliasJacksonAnnotationIntrospector introspector = new AliasJacksonAnnotationIntrospector();
        mapper.setAnnotationIntrospector(
                AnnotationIntrospectorPair.pair(new JacksonAnnotationIntrospector(), introspector));

        AnnotatedClass annotatedClass = mapper.getSerializationConfig()
                .introspect(mapper.constructType(TestEnum.class)).getClassInfo();
        TestEnum[] enumValues = TestEnum.values();
        String[][] aliasList = new String[enumValues.length][];
        introspector.findEnumAliases(mapper.getSerializationConfig(), annotatedClass, enumValues, aliasList);
        Assert.assertArrayEquals(new String[]{"aliasB1", "aliasB2"}, aliasList[1]);
        Assert.assertEquals(0, aliasList[0].length);
    }

    private enum EmptyAliasEnum {
        A,
        @RequestParamAlias({})
        B
    }

    private static class EmptyAliasBean {
        @RequestParamAlias({})
        private String name;
    }

    @Test
    public void testFindEnumAliasesEmpty() throws Exception {
        AliasJacksonAnnotationIntrospector introspector = new AliasJacksonAnnotationIntrospector();
        EmptyAliasEnum[] enumValues = EmptyAliasEnum.values();
        String[][] aliasList = new String[enumValues.length][];
        introspector.findEnumAliases(EmptyAliasEnum.class, enumValues, aliasList);
        Assert.assertNull(aliasList[1]);
    }

    @Test
    public void testFindPropertyAliasesEmpty() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        AliasJacksonAnnotationIntrospector introspector = new AliasJacksonAnnotationIntrospector();
        mapper.setAnnotationIntrospector(introspector);
        AnnotatedClass annotatedClass = mapper.getSerializationConfig()
                .introspect(mapper.constructType(EmptyAliasBean.class)).getClassInfo();
        java.util.List<AnnotatedField> fields = new java.util.ArrayList<>();
        annotatedClass.fields().forEach(fields::add);
        AnnotatedField field = fields.stream()
                .filter(f -> f.getName().equals("name")).findFirst().get();
        List<PropertyName> aliases = introspector.findPropertyAliases(field);
        Assert.assertEquals(0, aliases.size());
    }

    @Test
    public void testFindPropertyAliases() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        AliasJacksonAnnotationIntrospector introspector = new AliasJacksonAnnotationIntrospector();
        mapper.setAnnotationIntrospector(introspector);

        AnnotatedClass annotatedClass = mapper.getSerializationConfig()
                .introspect(mapper.constructType(FieldBean.class)).getClassInfo();
        java.util.List<AnnotatedField> fields = new java.util.ArrayList<>();
        annotatedClass.fields().forEach(fields::add);
        AnnotatedField field = fields.stream()
                .filter(f -> f.getName().equals("name")).findFirst().get();
        List<PropertyName> aliases = introspector.findPropertyAliases(field);
        Assert.assertEquals(1, aliases.size());
        Assert.assertEquals("aliasName", aliases.get(0).getSimpleName());

        // 无注解字段 → null
        AnnotatedField plainField = fields.stream()
                .filter(f -> f.getName().equals("plain")).findFirst().get();
        Assert.assertNull(introspector.findPropertyAliases(plainField));
    }

}