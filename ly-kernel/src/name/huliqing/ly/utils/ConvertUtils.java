/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.utils;

import com.jme3.math.Vector3f;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author huliqing
 */
public class ConvertUtils {
    
    public static float toFloat(Object obj, float defValue) {
        if (obj == null) {
            return defValue;
        }
        try {
            return Float.parseFloat(obj.toString());
        } catch (NumberFormatException e) {
            Logger.getLogger(ConvertUtils.class.getName()).log(Level.WARNING
                    , "Could not convert. obj={0}, defValue={1}, error={2}"
                    , new Object[] {obj, defValue, e.getMessage()});
            return defValue;
        }
    }
    
    /**
     * 将字符串数组转换为float数组，注：strArr不能为null,并且必须都是数字类型。
     * 否则会报错。
     * @param strArr
     * @return 
     */
    public static float[] toFloatArray(String[] strArr) {
        float[] result = new float[strArr.length];
        for (int i = 0; i < strArr.length; i++) {
            result[i] = Float.parseFloat(strArr[i]);
        }
        return result;
    }
    
    public static int toInteger(Object obj, int defValue) {
        if (obj == null) {
            return defValue;
        }
        try {
            return Integer.parseInt(obj.toString());
        } catch (NumberFormatException e) {
            Logger.getLogger(ConvertUtils.class.getName()).log(Level.WARNING
                    , "Could not convert. obj={0}, defValue={1}, error={2}"
                    , new Object[] {obj, defValue, e.getMessage()});
            return defValue;
        }
    }
    
    /**
     * 将字符串数组转换为整形数组，注：strArr不能为null,并且必须都是数字类型。
     * 否则会报错。
     * @param strArr
     * @return 
     */
    public static int[] toIntegerArray(String[] strArr) {
        int[] result = new int[strArr.length];
        for (int i = 0; i < strArr.length; i++) {
            result[i] = Integer.parseInt(strArr[i]);
        }
        return result;
    }
    
    public static int[] toIntegerArray(List<Integer> list) {
        if (list == null) {
            return null;
        }
        int[] result = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            result[i] = list.get(i);
        }
        return result;
    }
    
    public static List<Integer> toIntegerList(int[] intArr) {
        if (intArr == null)
            return null;
        List<Integer> list = new ArrayList<Integer>(intArr.length);
        for (int i = 0; i < intArr.length; i++) {
            list.add(intArr[i]);
        }
        return list;
    }
    
    public static List<Integer> toIntegerList(String[] strArr) {
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < strArr.length; i++) {
            list.add(Integer.parseInt(strArr[i]));
        }
        return list;
    }
    
    /**
     * 将字符串数组转换为vector3f, arr至少必须有3个值,并且都能够转化为float.
     * 这个方法会把arr中的arr[0],arr[1],arr[2]转到Vector3f中，多余的会被舍弃．
     * @param arr
     * @param store 存放结果，如果为null则创建一个
     * @return 
     */
    public static Vector3f toVector3f(String[] arr, Vector3f store) {
        if (store == null) {
            store = new Vector3f();
        }
        store.set(Float.parseFloat(arr[0])
                , Float.parseFloat(arr[1])
                , Float.parseFloat(arr[2]));
        
        return store;
    }
    
    /**
     * 将两个数组合并在一起返回
     * @param arr1
     * @param arr2
     * @return 
     */
    public static int[] joinArray(int[] arr1, int[] arr2) {
        int[] result = new int[arr1.length + arr2.length];
        System.arraycopy(arr1, 0, result, 0, arr1.length);
        System.arraycopy(arr2, 0, result, arr1.length, arr2.length);
        return result;
    }
    
    /**
     * 转换为list,如果array为null,则返回null.
     * @param array
     * @return 
     */
    public static List<String> toList(String[] array) {
        if (array == null) 
            return null;
        List<String> result = new ArrayList<String>(array.length);
        Collections.addAll(result, array);
        return result;
    }
    
    public static <T extends Object> List<T> toList(Map<Object, T> maps) {
        if (maps == null) {
            return null;
        }
        List<T> result = new ArrayList<T>(maps.size());
        result.addAll(maps.values());
        return result;
    }
}
