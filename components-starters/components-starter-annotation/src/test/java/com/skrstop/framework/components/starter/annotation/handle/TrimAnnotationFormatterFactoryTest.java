package com.skrstop.framework.components.starter.annotation.handle.trim;

import com.skrstop.framework.components.starter.annotation.anno.trim.Trim;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.format.Parser;
import org.springframework.format.Printer;
import org.springframework.validation.DataBinder;

import java.lang.annotation.Annotation;
import java.util.Locale;
import java.util.Set;

/**
 * @author 蒋时华
 */
public class TrimAnnotationFormatterFactoryTest {

    private Trim trim(final TrimmerType type) {
        return new Trim() {
            @Override
            public TrimmerType value() {
                return type;
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return Trim.class;
            }
        };
    }

    @Test
    public void testGetFieldTypes() {
        TrimAnnotationFormatterFactory factory = new TrimAnnotationFormatterFactory();
        Set<Class<?>> types = factory.getFieldTypes();
        Assert.assertEquals(1, types.size());
        Assert.assertTrue(types.contains(String.class));
    }

    @Test
    public void testGetParserAndPrinter() throws Exception {
        TrimAnnotationFormatterFactory factory = new TrimAnnotationFormatterFactory();
        Parser<?> parser = factory.getParser(trim(TrimmerType.LEFT_RIGHT), String.class);
        Assert.assertEquals("abc", parser.parse(" abc ", Locale.CHINA));
        Printer<Object> printer = (Printer<Object>) factory.getPrinter(trim(TrimmerType.ALL), String.class);
        Assert.assertEquals(" abc ", printer.print(" abc ", Locale.CHINA));
    }

    public static class FormBean {
        @Trim
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    @Test
    public void testAddFormattersViaDataBinder() {
        // 通过 DataBinder 触发 addFormatters 注册与 parse 流程
        StaticApplicationContext context = new StaticApplicationContext();
        context.refresh();
        TrimAnnotationFormatterFactory factory = new TrimAnnotationFormatterFactory();
        DataBinder binder = new DataBinder(new FormBean());
        org.springframework.format.support.FormattingConversionService conversionService =
                new org.springframework.format.support.FormattingConversionService();
        factory.addFormatters(conversionService);
        binder.setConversionService(conversionService);

        org.springframework.beans.MutablePropertyValues values = new org.springframework.beans.MutablePropertyValues();
        values.add("name", "  abc  ");
        binder.bind(values);
        Assert.assertEquals("abc", ((FormBean) binder.getTarget()).getName());
    }

    @Test
    public void testInApplicationContext() {
        // 注册到真实上下文（WebMvcConfigurer 接口方法）
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        try {
            ctx.registerBean(TrimAnnotationFormatterFactory.class);
            ctx.refresh();
            TrimAnnotationFormatterFactory factory = ctx.getBean(TrimAnnotationFormatterFactory.class);
            Assert.assertNotNull(factory);
        } finally {
            ctx.close();
        }
    }

}