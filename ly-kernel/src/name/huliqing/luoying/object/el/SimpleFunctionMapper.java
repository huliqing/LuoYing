/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.el;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.el.FunctionMapper;

/**
 * 简单的方法匹配器，默认添加了 Math中的所有方法。使用示例：<br>
 * <code><pre>
 * #{Math:pow(x,y)}
 * #{Math:abs(x)}
 * ...
 * </pre></code>
 * 等方式来调用这些方法。
 */
public class SimpleFunctionMapper extends FunctionMapper {
    private static final Logger LOG = Logger.getLogger(SimpleFunctionMapper.class.getName());

    private final static Map<String, Method> MATH_METHOD_CACHE = new HashMap<String, Method>();
    
    private final Map<String, Method> functionMap = new HashMap<String, Method>();
    
    public SimpleFunctionMapper() {
        // 添加默认的Math方法,并缓存math方法。
        if (MATH_METHOD_CACHE.isEmpty()) {
            Method[] methods = Math.class.getMethods();
            for (Method m : methods) {
                addFunction("Math", m.getName(), m);
            }
            MATH_METHOD_CACHE.putAll(functionMap);
        } else {
            functionMap.putAll(MATH_METHOD_CACHE);
        }
    }
    
    @Override
    public Method resolveFunction(String prefix, String localName) {
        String key = prefix + ":" + localName;
        return functionMap.get(key);
    }
    
    /**
     * 添加一个方法,示例如<br>
     * <code><pre>
     * Method pow = ...(从Math中获取到方法的引用)
     * addFunction("Math", "pow", pow);
     * </pre></code> 然后通过表达式：#{Math:pow(x, y)}来调用这个方法。 注意：可添加的方法必须符合如下条件：<br>
     * 1.方法必须是public的<br>
     * 2.方法必须是static的<br>
     * 3.方法必须返回<b>非</b>void类型<br>
     * @param prefix 前缀如：Math
     * @param methodName 方法名如：pow
     * @param method 方法，如：pow
     */
    public void addFunction(String prefix, String methodName, Method method) {
        if (prefix == null || methodName == null || method == null) {
            throw new NullPointerException();
        }
        int modifiers = method.getModifiers();
        if (!Modifier.isPublic(modifiers)) {
            LOG.log(Level.WARNING, "(addFunction) Skip method because it is not public, method={0}", method.getName());
            return;
        }
        if (!Modifier.isStatic(modifiers)) {
            LOG.log(Level.WARNING, "(addFunction) Skip method because it is not static, method={0}", method.getName());
            return;
        }
        Class<?> retType = method.getReturnType();
        if (retType == Void.TYPE) {
            LOG.log(Level.WARNING, "(addFunction) Skip method because it is return void type, method={0}", method.getName());
            return;
        }

        String key = prefix + ":" + methodName;
        functionMap.put(key, method);
    }

}
