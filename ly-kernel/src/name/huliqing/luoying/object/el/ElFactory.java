/*
 * LuoYing is a program used to make 3D RPG game.
 * Copyright (c) 2014-2016 Huliqing <31703299@qq.com>
 * 
 * This file is part of LuoYing.
 *
 * LuoYing is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LuoYing is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with LuoYing.  If not, see <http://www.gnu.org/licenses/>.
 */
package name.huliqing.luoying.object.el;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import org.apache.el.ExpressionFactoryImpl;

/**
 * El表达式
 * @author huliqing
 */
public final class ElFactory {
    
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
    
    /**
     * 判断给定的字符串是不是可能为EL表达式。
     * @param expression
     * @return 
     */
    public static boolean isElExpression(String expression) {
        if (expression == null) {
            return false;
        }
        return expression.trim().startsWith("#{");
    }
}
