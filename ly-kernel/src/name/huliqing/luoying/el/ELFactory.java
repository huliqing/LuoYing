/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.el;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import org.apache.el.ExpressionFactoryImpl;

/**
 * El表达式
 * @author huliqing
 */
public final class ELFactory {
    
    private final static ExpressionFactory EXPRESSION_FACTORY = new ExpressionFactoryImpl();
    
    public static ExpressionFactory getExpressionFactory() {
        return EXPRESSION_FACTORY;
    }
    
    /**
     * @param context
     * @param expression
     * @param expectedReturnType
     * @param expectedParamTypes
     * @return 
     * @see ExpressionFactory#createMethodExpression(javax.el.ELContext, java.lang.String, java.lang.Class, java.lang.Class<?>[]) 
     */
    public static MethodExpression createMethodExpression(ELContext context, String expression
            , Class<?> expectedReturnType, Class<?>[] expectedParamTypes) {
        return EXPRESSION_FACTORY.createMethodExpression(context, expression, expectedReturnType, expectedParamTypes);
    }

    /**
     * @see ExpressionFactory#createValueExpression(javax.el.ELContext, java.lang.String, java.lang.Class) 
     * @param context
     * @param expression
     * @param expectedType
     * @return 
     */
    public static ValueExpression createValueExpression(ELContext context, String expression, Class<?> expectedType) {
        return EXPRESSION_FACTORY.createValueExpression(context, expression, expectedType);
    }

    /**
     * @see ExpressionFactory#createValueExpression(java.lang.Object, java.lang.Class) 
     * @param instance
     * @param expectedType
     * @return 
     */
    public static ValueExpression createValueExpression(Object instance, Class<?> expectedType) {
        return EXPRESSION_FACTORY.createValueExpression(instance, expectedType);
    }
}
