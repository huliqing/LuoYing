/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.xml;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.luoying.LuoYingException;

/**
 *
 * @author huliqing
 */
public class CloneHelper {
    
    /**
     * 直接克隆一个ObjectData. 如果给定的objectData为null则返回null. 否则克隆该对象并返回。
     * 这个方法为方便子类直接克隆一个ObjectData而不需要判断是否为null.
     * @param <T>
     * @param objectData
     * @return 
     */
    public static <T extends ObjectData> T cloneObjectData(T objectData) {
        if (objectData == null)
            return null;
        return (T) objectData.clone();
    }
    
    /**
     * 深度克隆一个ObjectData列表，如果给定的列表为null,则返回null, 否则将列表中的ObjectData一个一个的克隆，
     * 并最终返回一个新的列表。这个方法为方便子类在克隆列表的时候不需要判断列表或者列表中对象是否为null。
     * @param <T>
     * @param list
     * @return 
     */
    public static <T extends ObjectData> List<T> cloneObjectDataList(List<T> list) {
        if (list == null)
            return null;
        List<T> clones = new ArrayList<T>(list.size());
        for (T t : list) {
            if (t == null) {
                clones.add(null);
            } else {
                clones.add((T) t.clone());
            }
        }
        return clones;
    }
    
    /**
     * 深度克隆一个列表，如果给定的列表为null,则返回null, 否则将列表中的数据一个一个的克隆，
     * 列表中的对象必须是Cloneable对象类型。
     * @param <T>
     * @param list
     * @return 
     */
    public static <T> List<T> cloneList(List<T> list) {
        if (list == null)
            return null;
        List<T> clones = new ArrayList<T>(list.size());
        for (T t : list) {
            if (t == null) {
                clones.add(null);
                continue;
            }
            clones.add(cloneObject(t));
        }
        return clones;
    }
    
    /**
     * 克隆一个对象,如果目标对象为null，则该方法返回null.否则该方法默认用反射方式调用object的clone方法, 
     * 目标对象必须是Cloneable对象，否则将报错LuoYingException.
     * @param <T>
     * @param object
     * @return 
     */
    public static <T> T cloneObject(T object) {
        if(object == null) {
            return null;
        }
        if (!(object instanceof Cloneable)) {
            throw new LuoYingException("Unsupported Cloneable object, object=" + object.getClass().getName());
        }
        try {
            Method m = object.getClass().getMethod("clone");
            return (T) m.invoke(object);
        } catch(Exception e) {
            throw new LuoYingException("Could not cloneObject for Object=" + object);
        }
    }
    
    /**
     * 克隆long型数组，如果给定的数组为null则该方法直接返回null,否则System.arraycopy方法复杂克隆数组。
     * @param array
     * @return 
     */
    public static long[] cloneLongArray(long[] array) {
        if (array == null) {
            return null;
        }
        long[] clone = new long[array.length];
        System.arraycopy(array, 0, clone, 0, array.length);
        return clone;
    }
    
}
