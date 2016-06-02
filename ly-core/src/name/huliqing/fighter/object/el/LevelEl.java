/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.el;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.fighter.data.ElData;

/**
 * 为等级生成相应的数值
 * @author huliqing
 */
public class LevelEl extends AbstractEl {
    
    // key = 参数名
    private final Map<String, Object> valueMap = new HashMap<String, Object>(1);
    
    // 因为表达式不会在运行过程中发生变化，所以对于levelEl来说是可以进行缓存的。
    // 缓存的最高数量不会超过系统定义的最高等级数
    private final Map<Integer, Double> cacheMap = new HashMap<Integer, Double>();
    
    public LevelEl(ElData data) {
        super(data);
    }
    
    public synchronized double getValue(int level) {
        // 先从缓存中获取。
        Double result = cacheMap.get(level);
        if (result != null) {
            return result.doubleValue();
        }
        
        valueMap.put("level", level);
        String strResult = eval(valueMap);
        if (strResult != null) {
            try {
                
                result = Double.parseDouble(strResult);
                cacheMap.put(level, result);
                return result.doubleValue();
                
            } catch (NumberFormatException nfe) {
                Logger.getLogger(LevelEl.class.getName())
                        .log(Level.WARNING
                        , "Could not convert to float! el={0}, evalResult={1}"
                        , new Object[] {data, strResult});
            }
        }
        return 0;
    }
    
}
