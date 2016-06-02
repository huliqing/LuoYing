/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.atest;

import com.jme3.animation.SkeletonControl;
import com.jme3.math.FastMath;

/**
 * @author huliqing
 */
public class Test1 {
    
    public static void main(String[] args) {
        System.out.println("pow(10)=" + FastMath.pow(10, 2));
    }
      
//    public Double getMathValue(List<MapJ> map,String option){  
//        double d = 0;  
//        try {  
//           
//            Object o = engine.eval(option);  
//            d = Double.parseDouble(o.toString());  
//        } catch (ScriptException e) {  
//            System.out.println("无法识别表达式");  
//            return null;  
//        }  
//        return d;  
//    }  

//    public static void main(String[] args) throws ScriptException {
//         Context cx = Context.enter();
//        // 由于java使用的是JVM bytecode 而android使用的是Dalvik bytecode.
//        // 所以这里必须关闭对于JVMbytecode的优化,以兼容android的运行
//        cx.setOptimizationLevel(-1);
//        Scriptable scope = cx.initStandardObjects();
//        
//        for (int i = 0; i < 20; i++) {
//            scope.put("i", scope, i);
////            String evalStr = "-20 + 25 * Math.pow(1.05, i)";
//            String evalStr = "i * 2 + 3 * Math.pow(1.06, i)";
//            String strResult = cx.evaluateString(scope, evalStr, "el", 1, null).toString();
//            System.out.println(i + "=" + strResult);
//        }
//        Context.exit();
//    }
    

}
