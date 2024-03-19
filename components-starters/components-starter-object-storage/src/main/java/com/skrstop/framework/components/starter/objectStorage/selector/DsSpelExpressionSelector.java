package com.skrstop.framework.components.starter.objectStorage.selector;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.BeanResolver;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;

public class DsSpelExpressionSelector extends DsSelector {

    /**
     * 参数发现器
     */
    private static final ParameterNameDiscoverer NAME_DISCOVERER = new DefaultParameterNameDiscoverer();
    /**
     * Express语法解析器
     */
    private static final ExpressionParser PARSER = new SpelExpressionParser();
    /**
     * 解析上下文的模板
     * 对于默认不设置的情况下,从参数中取值的方式 #param1
     * 设置指定模板 ParserContext.TEMPLATE_EXPRESSION 后的取值方式: #{#param1}
     */
    private ParserContext parserContext = new ParserContext() {

        @Override
        public boolean isTemplate() {
            return false;
        }

        @Override
        public String getExpressionPrefix() {
            return null;
        }

        @Override
        public String getExpressionSuffix() {
            return null;
        }
    };
    private BeanResolver beanResolver;

    @Override
    public boolean matches(String key) {
        return true;
    }

    @Override
    public String doDetermineDataSource(MethodInvocation invocation, String key) {
        Method method = invocation.getMethod();
        Object[] arguments = invocation.getArguments();
        ExpressionRootObject rootObject = new ExpressionRootObject(method, arguments, invocation.getThis());
        StandardEvaluationContext context = new MethodBasedEvaluationContext(rootObject, method, arguments, NAME_DISCOVERER);
        context.setBeanResolver(beanResolver);
        final Object value = PARSER.parseExpression(key, parserContext).getValue(context);
        return value == null ? null : value.toString();
    }

    /**
     * 设置解析上下文
     *
     * @param parserContext 解析上下文
     */
    public void setParserContext(ParserContext parserContext) {
        this.parserContext = parserContext;
    }

    /**
     * 设置bean解析器
     *
     * @param beanResolver bean解析器
     */
    public void setBeanResolver(BeanResolver beanResolver) {
        this.beanResolver = beanResolver;
    }

    @Getter
    @RequiredArgsConstructor
    public static class ExpressionRootObject {
        private final Method method;

        private final Object[] args;

        private final Object target;
    }
}