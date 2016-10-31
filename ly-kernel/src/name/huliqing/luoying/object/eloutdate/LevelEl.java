///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.luoying.object.eloutdate;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import name.huliqing.luoying.data.ElData;
//
///**
// * @deprecated 
// * 为等级生成相应的数值
// * @author huliqing
// * @param <T>
// */
//public class LevelEl<T extends ElData> extends AbstractEl<T> {
//    
//    // key = 参数名
//    private final Map<String, Object> valueMap = new HashMap<String, Object>(1);
//    
//    // 因为表达式不会在运行过程中发生变化，所以对于levelEl来说是可以进行缓存的。
//    // 缓存的最高数量不会超过系统定义的最高等级数
//    private final Map<Integer, Float> cacheMap = new HashMap<Integer, Float>();
//    
//    /**
//     * 获取等级值，如果找不到指定的等级值，则该方法始终返回0.
//     * @param level
//     * @return 
//     */
//    public synchronized float getValue(int level) {
//        // 先从缓存中获取
//        Float result = cacheMap.get(level);
//        if (result != null) {
//            return result;
//        }
//        
//        valueMap.put("level", level);
//        String strResult = eval(valueMap);
//        if (strResult != null) {
//            try {
//                
//                result = Float.parseFloat(strResult);
//                cacheMap.put(level, result);
//                return result;
//                
//            } catch (NumberFormatException nfe) {
//                Logger.getLogger(LevelEl.class.getName())
//                        .log(Level.WARNING
//                        , "Could not convert to float! el={0}, evalResult={1}"
//                        , new Object[] {data, strResult});
//            }
//        }
//        return 0;
//    }
//    
//}
