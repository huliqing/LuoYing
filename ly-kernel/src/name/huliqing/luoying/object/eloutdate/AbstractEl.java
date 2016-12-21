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
//package name.huliqing.luoying.object.eloutdate;
//
//import java.util.HashMap;
//import java.util.LinkedHashSet;
//import java.util.Map;
//import java.util.Set;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//import name.huliqing.luoying.data.ElData;
//import name.huliqing.luoying.xml.DataFactory;
//import org.mozilla.javascript.Context;
//import org.mozilla.javascript.Script;
//import org.mozilla.javascript.Scriptable;
//
///**
// * @deprecated 
// * 关于Rhino的相关资料参考：
// * https://developer.mozilla.org/en-US/docs/Mozilla/Projects/Rhino/Scopes_and_Contexts
// * @author huliqing
// * @param <T>
// */
//public class AbstractEl<T extends ElData> implements El<T> {
//    private static final Logger LOG = Logger.getLogger(AbstractEl.class.getName());
//    // scriptable性能开销太大，需要在多线程中共享
//    private static Scriptable jsShareScope;
//    
//    protected final static String PARAM_PATTERN = "\\{.+?\\}";
//    protected T data;
//    // 去除了参数符号和参数中的空格后的紧凑的表达式。
//    // 注：表达式中的其它部分仍保留空格,因为其它部分有可能包含字符串常量，其中可能包含正常的空白间隔
//    protected String expResult;
//    // 在表达式中提取出来的所有参数，不重复，不包含"[]"符号和空格
//    protected Set<String> params = new LinkedHashSet<String>();
//
//    @Override
//    public void setData(T data) {
//        this.data = data;
//        decode();
//    }
//    
//    @Override
//    public T getData() {
//        return data;
//    }
//    
//    @Override
//    public void updateDatas() {
//        // ignore
//    }
//    
//    private void decode() {
//        // 1.去除表达式中参数中的所有空格
//        // 2.提取所有参数名
//        String expression = data.getExpression();
//        Pattern pattern = Pattern.compile(PARAM_PATTERN);
//        Matcher m = pattern.matcher(expression);
//        StringBuffer sb = new StringBuffer(expression.length());
//        while (m.find()) {
//            String group = expression.substring(m.start() + 1, m.end() - 1).replaceAll(" ", "");
//            m.appendReplacement(sb, group);
//            if (!params.contains(group)) {
//                params.add(group);
//            }
//        }
//        m.appendTail(sb);
//        expResult = sb.toString();
//        
////        if (Config.debug) {
////            LOG.log(Level.INFO, "decode result. elResult={0}, params={1}", new Object[] {expResult, params});
////        }
//    }
//    
//    /**
//     * 计算表达式的值,因为该方法可能在多线程中运行，必须确保线程安全
//     * @param valueMap
//     * @return 
//     */
//    protected synchronized String eval(Map<String, Object> valueMap) {
//        try {
//            Context jsContext = Context.enter();
//            // 由于java使用的是JVM bytecode 而android使用的是Dalvik bytecode.
//            // 所以这里必须关闭对于JVMbytecode的优化,以兼容android的运行
//            jsContext.setOptimizationLevel(-1);
//            checkCreateShareScope(jsContext);
//            
//            String result = evalValue(jsContext, jsShareScope, expResult, valueMap);
//            
////            if (Config.debug) {
////                LOG.log(Level.INFO, "result value={0}", result);
////            }
//            return result;
//        } finally {
//            Context.exit();
//        }
//    }
//    
//    private void checkCreateShareScope(Context jsContext) {
//        if (jsShareScope == null) {
//            jsShareScope = jsContext.initStandardObjects();
//            // 首次初始化时需要把脚本都载入上下文
//            Script script;
//            for (String str : DataFactory.getJavaScripts()) {
//                script = jsContext.compileString(str, "script", 0, null);
//                script.exec(jsContext, jsShareScope);
////                if (Config.debug) {
////                    LOG.log(Level.INFO, "Execute script: {0}", str);
////                }
//            }
//        }
//    }
//    
//    private String evalValue(Context cx, Scriptable scope, String evalStr, Map<String, Object> valueMap) {
//        try {
//            for (String param : params) {
//                Object value = valueMap.get(param);
//                if (value == null) {
//                    LOG.log(Level.WARNING
//                            , "Param value not found for evalValue, elId={0}, elResultStr={1}, need param={2}, now use \"0\" instead."
//                            , new Object[]{data, expResult, param});
//                    value = 0;
//                }
//                scope.put(param, scope, value);
//            }
//            String strResult = cx.evaluateString(scope, evalStr, "evalValue", 0, null).toString();
//            return strResult;
//        } catch (Exception e) {
//            LOG.log(Level.SEVERE, "Could not evalValue, evalStr={0}, error={1}"
//                    , new Object[] {evalStr, e.getMessage()});
//            return null;
//        }
//    }
//    
//    public static void main(String[] args) {
//        ElData data = new ElData();
//        data.setExpression("{s1} + {s2}");
//        AbstractEl ae = new AbstractEl();
//        ae.setData(data);
//        
//        long startTime = System.currentTimeMillis();
//        Map<String, Object> vmap = new HashMap<String, Object>();
//        double result = 0;
//        for (int i = 0; i < 10000; i++) {
//            vmap.put("s1", i);
//            vmap.put("s2", 0);
//            result += Double.parseDouble(ae.eval(vmap));
//        }
//        System.out.println("useTime=" + (System.currentTimeMillis() - startTime) + ", result=" + result);
//    }
//}
