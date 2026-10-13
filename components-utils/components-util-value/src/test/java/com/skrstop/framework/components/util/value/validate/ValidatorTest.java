package com.skrstop.framework.components.util.value.validate.validator;

import com.skrstop.framework.components.util.value.validate.annotation.LengthSize;
import com.skrstop.framework.components.util.value.validate.annotation.NotContainBlank;
import com.skrstop.framework.components.util.value.validate.annotation.NotContainNull;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Payload;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.junit.Assert;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author 蒋时华
 */
public class ValidatorTest {

    private final Validator validator = Validation.byDefaultProvider().configure()
            .messageInterpolator(new ParameterMessageInterpolator()).buildValidatorFactory().getValidator();

    public static class LengthSizeSize implements Annotation, LengthSize {
        @Override
        public boolean size() {return true;}

        @Override
        public long min() {return 1;}

        @Override
        public long max() {return 3;}

        @Override
        public boolean ignoreNull() {return false;}

        @Override
        public String charset() {return "UTF-8";}

        @Override
        public String message() {return "msg";}

        @Override
        public Class<?>[] groups() {return new Class[0];}

        @Override
        public Class<? extends Payload>[] payload() {return new Class[0];}

        @Override
        public Class<? extends Annotation> annotationType() {return LengthSize.class;}
    }

    public static class LengthSizeByte implements Annotation, LengthSize {
        @Override
        public boolean size() {return false;}

        @Override
        public long min() {return 3;}

        @Override
        public long max() {return 6;}

        @Override
        public boolean ignoreNull() {return false;}

        @Override
        public String charset() {return "UTF-8";}

        @Override
        public String message() {return "msg";}

        @Override
        public Class<?>[] groups() {return new Class[0];}

        @Override
        public Class<? extends Payload>[] payload() {return new Class[0];}

        @Override
        public Class<? extends Annotation> annotationType() {return LengthSize.class;}
    }

    public static class LengthSizeIgnoreNull implements Annotation, LengthSize {
        @Override
        public boolean size() {return true;}

        @Override
        public long min() {return 1;}

        @Override
        public long max() {return 3;}

        @Override
        public boolean ignoreNull() {return true;}

        @Override
        public String charset() {return "UTF-8";}

        @Override
        public String message() {return "msg";}

        @Override
        public Class<?>[] groups() {return new Class[0];}

        @Override
        public Class<? extends Payload>[] payload() {return new Class[0];}

        @Override
        public Class<? extends Annotation> annotationType() {return LengthSize.class;}
    }

    @Test
    public void testLengthStrForString() {
        LengthStrConstraintValidatorForString v = new LengthStrConstraintValidatorForString();
        v.initialize(new LengthSizeSize());
        Assert.assertTrue(v.isValid("ab", null));
        Assert.assertFalse(v.isValid("", null));
        Assert.assertFalse(v.isValid("abcd", null));
        v.initialize(new LengthSizeByte());
        Assert.assertTrue(v.isValid("中", null));
        Assert.assertTrue(v.isValid("中中", null));
        v.initialize(new LengthSizeIgnoreNull());
        Assert.assertTrue(v.isValid(null, null));
        // ignoreNull=true 且值非空 → isNull 被短路
        Assert.assertTrue(v.isValid("ab", null));
        // ignoreNull=false 且 null → 按空串处理
        v.initialize(new LengthSizeSize());
        Assert.assertFalse(v.isValid(null, null));
    }

    @Test
    public void testLengthStrForObj() {
        LengthStrConstraintValidatorForObj v = new LengthStrConstraintValidatorForObj();
        v.initialize(new LengthSizeSize());
        Assert.assertTrue(v.isValid("ab", null));
        Assert.assertFalse(v.isValid("abcd", null));
        Assert.assertFalse(v.isValid(null, null));
        v.initialize(new LengthSizeByte());
        Assert.assertTrue(v.isValid("中", null));
        Assert.assertTrue(v.isValid("中中", null));
        v.initialize(new LengthSizeIgnoreNull());
        Assert.assertTrue(v.isValid(null, null));
        Assert.assertTrue(v.isValid("ab", null));
        v.initialize(new LengthSizeSize());
        Assert.assertFalse(v.isValid(null, null));
    }

    @Test
    public void testLengthStrForCollection() {
        LengthStrConstraintValidatorForCollection v = new LengthStrConstraintValidatorForCollection();
        v.initialize(new LengthSizeSize());
        Assert.assertTrue(v.isValid(Arrays.asList("a", "ab"), null));
        Assert.assertFalse(v.isValid(Arrays.asList("", "abcd"), null));
        Assert.assertFalse(v.isValid(null, null));
        v.initialize(new LengthSizeByte());
        Assert.assertTrue(v.isValid(Arrays.asList("中"), null));
        Assert.assertTrue(v.isValid(Arrays.asList("中中"), null));
        v.initialize(new LengthSizeIgnoreNull());
        Assert.assertTrue(v.isValid(null, null));
        Assert.assertTrue(v.isValid(Arrays.asList("a"), null));
        v.initialize(new LengthSizeSize());
        Assert.assertFalse(v.isValid(null, null));
        // 非空但空集合 → false
        Assert.assertFalse(v.isValid(Collections.emptyList(), null));
        // 非空集合 → true
        Assert.assertTrue(v.isValid(Arrays.asList("a"), null));
    }

    @Test
    public void testNotContainBlank() {
        NotContainBlankConstraintValidatorForCollection v = new NotContainBlankConstraintValidatorForCollection();
        Assert.assertFalse(v.isValid(null, null));
        Assert.assertTrue(v.isValid(Arrays.asList("a", "b"), null));
        Assert.assertFalse(v.isValid(Arrays.asList("a", null), null));
        Assert.assertFalse(v.isValid(Arrays.asList("a", "  "), null));
    }

    @Test
    public void testNotContainNull() {
        NotContainNullConstraintValidatorForCollection v = new NotContainNullConstraintValidatorForCollection();
        Assert.assertFalse(v.isValid(null, null));
        Assert.assertTrue(v.isValid(Arrays.asList("a", "b"), null));
        Assert.assertFalse(v.isValid(Arrays.asList("a", null), null));
    }

    public static class ParamBean {
        @LengthSize(min = 1, max = 3)
        private final String name;
        @NotContainNull
        @NotContainBlank
        private final List<String> items;

        public ParamBean(String name, List<String> items) {
            this.name = name;
            this.items = items;
        }

        public String getName() {return name;}

        public List<String> getItems() {return items;}
    }

    @Test
    public void testAnnotationBasedValidation() {
        Set<ConstraintViolation<ParamBean>> vs = validator.validate(new ParamBean("abc", Arrays.asList("a", "b")));
        Assert.assertTrue(vs.isEmpty());
        vs = validator.validate(new ParamBean("abcd", Arrays.asList("a", "  ")));
        Assert.assertEquals(2, vs.size());
        vs = validator.validate(new ParamBean("abc", Arrays.asList("a", null)));
        Assert.assertEquals(2, vs.size());
        Set<ConstraintViolation<ParamBean>> vs2 = validator.validate(new ParamBean("abc", Arrays.asList("ab", "cde")));
        Assert.assertTrue(vs2.isEmpty());
    }

}