/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.data;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;
import com.jme3.scene.UserData;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import name.huliqing.fighter.utils.ConvertUtils;

/**
 *
 * @author huliqing
 */
@Serializable
public class DataAttribute implements Savable {

    // 扩展参数
    protected Map<String, Object> data;
    
    public DataAttribute() {
        this.data = new HashMap<String, Object>();
    }
    
    public DataAttribute(Map data) {
        if (data == null) {
            throw new NullPointerException("Data could not be null!");
        }
        this.data = data;
    }
    
//    /**
//     * 添加一个参数到本地data中，如果参数已经存在则覆盖该参数。注意：value将会被转换为字符串.
//     * @param key
//     * @param value 
//     */
//    public final void putAttribute(String key, String value) {
//        if (data == null) {
//            data = new HashMap<String, Object>();
//        }
//        data.put(key, String.valueOf(value));
//    }
    
    /**
     * 设置一个参数值，如果value为null则清除该值。
     * @param key
     * @param value 
     */
    public final void setAttribute(String key, Object value) {
        if (value == null) {
            data.remove(key);
            return;
        }
        data.put(key, value);
    }
    
    /**
     * 获取参数，如果不存在则返回null, 如果为空值也返回null
     * @param key
     * @return 
     */
    public String getAttribute(String key) {
        if (!data.containsKey(key)) {
            return null;
        }
        String value = data.get(key).toString();
        if ("".equals(value)) {
            return null;
        }
        return value;
    }

    public final <T extends Savable> T getAsSavable(String key, Class<T> type) {
        if (!data.containsKey(key)) {
            return null;
        }
        Object object = data.get(key);
        return (T) object;
    }
    
    public final String getAttribute(String key, String defValue) {
        String value = getAttribute(key);
        if (value == null) {
            return defValue;
        }
        return value;
    }
    
    public final Integer getAsInteger(String key) {
        String value = getAttribute(key);
        if (value == null) {
            return null;
        }
        return Integer.parseInt(value);
    }
    
    public final int getAsInteger(String key, int defValue) {
        String value = getAttribute(key);
        if (value == null) {
            return defValue;
        }
        return Integer.parseInt(value);
    }
    
    /**
     * 获取参数值，并以数组形式返回，原始参数值格式必须是使用半角逗号","分隔的,
     * 如：属性值 "1,3,2" 将返回为数组 {"1","3","2"}.
     * 如果属性值为null或空，则返回null.
     * @param key
     * @return 
     */
    public final String[] getAsArray(String key) {
        String temp = getAttribute(key);
        if (temp == null) 
            return null;
        String[] result = temp.split(",");
        for (int i = 0; i < result.length; i++) {
            result[i] = result[i].trim();
        }
        return result;
    }
    
    /**
     * 把参数获取为整形数组，如果没有设置该参数则返回null.
     * @param key
     * @return 
     */
    public final int[] getAsIntegerArray(String key) {
        String[] strArr = getAsArray(key);
        if (strArr != null) {
            return ConvertUtils.toIntegerArray(strArr);
        }
        return null;
    }
    
    /**
     * 把参数获取为整形集合，如果没有设置该参数则返回null.
     * @param key
     * @return 
     */
    public final List<Integer> getAsIntegerList(String key) {
        String[] strArr = getAsArray(key);
        if (strArr != null) {
            return ConvertUtils.toIntegerList(strArr);
        }
        return null;
    }
    
    public final Float getAsFloat(String key) {
        String value = getAttribute(key);
        if (value == null) {
            return null;
        }
        return Float.parseFloat(value);
    }
    
    public final float getAsFloat(String key, float defValue) {
        String value = getAttribute(key);
        if (value == null) {
            return defValue;
        }
        return Float.parseFloat(value);
    }
    
    public final float[] getAsFloatArray(String key) {
        String value = getAttribute(key);
        if (value == null) {
            return null;
        }
        String[] tempArr = value.split(",");
        float[] resultArr = new float[tempArr.length];
        for (int i = 0; i < resultArr.length; i++) {
            resultArr[i] = Float.parseFloat(tempArr[i]);
        }
        return resultArr;
    }
    
    public final Boolean getAsBoolean(String key) {
        String value = getAttribute(key);
        if (value == null) {
            return null;
        }
        return (value.equalsIgnoreCase("true") || value.equals("1"));
    }
    
    public final boolean getAsBoolean(String key, boolean defValue) {
        String value = getAttribute(key);
        if (value == null) {
            return defValue;
        }
        return (value.equalsIgnoreCase("true") || value.equals("1"));
    }
    
    /**
     * 获取参数值，并以List形式返回，原始参数值格式必须是使用半角逗号","分隔的,
     * 如：属性值 "1,3,2", 如果属性值为null或空，则返回null.
     * @param key
     * @return 
     */
    public final List<String> getAsList(String key) {
        String[] arr = getAsArray(key);
        if (arr == null)
            return null;
        
//        return Arrays.asList(arr); // 不要再使用asList, asList使用的是内部类，这会在序列化时异常

        List<String> result = new ArrayList<String>(arr.length);
        Collections.addAll(result, arr);
        return result;
    }
    
    /**
     * 获取参数值，并以Vector3f形式返回，原始格式必须如："x"或 "x,y" 或 "x,y,z"，
     * 如果参数不对，将返回null.<br />
     * 当只有x值时，则y = z =x; <br />
     * 当只有x,y值时，z = 1; <br />
     * @param key
     * @return 
     */
    public final Vector3f getAsVector3f(String key) {
        String temp = getAttribute(key);
        if (temp == null) 
            return null;
        String[] arr = temp.split(",");
        
        float x,y,z; 
        if (arr.length == 1) {
            x = Float.parseFloat(arr[0]);
            y = x;
            z = x;
        } else if (arr.length == 2) {
            x = Float.parseFloat(arr[0]);
            y = Float.parseFloat(arr[1]);
            z = 1;
        } else {
            x = Float.parseFloat(arr[0]);
            y = Float.parseFloat(arr[1]);
            z = Float.parseFloat(arr[2]);
        }
        return new Vector3f(x,y,z);
    }
    
    public final Vector3f getAsVector3f(String key, Vector3f defValue) {
        Vector3f temp = getAsVector3f(key);
        return temp != null ? temp : defValue;
    }
    
    /**
     * 格式, "x|y|z,x|y|z,...", 如果没有指定参数则返回null.
     * @param key
     * @return 
     */
    public final Vector3f[] getAsVector3fArray(String key) {
        String temp = getAttribute(key);
        if (temp == null) 
            return null;
        String[] arrStr = temp.split(",");
        Vector3f[] result = new Vector3f[arrStr.length];
        for (int i = 0; i < arrStr.length; i++) {
            String[] xyz = arrStr[i].split("\\|");
            Vector3f vec = new Vector3f();
            vec.setX(Float.parseFloat(xyz[0]));
            vec.setY(Float.parseFloat(xyz[1]));
            vec.setZ(Float.parseFloat(xyz[2]));
            result[i] = vec;
        }
        return result;
    }
    
    public final Vector2f getAsVector2f(String key) {
        String temp = getAttribute(key);
        if (temp == null) 
            return null;
        String[] arr = temp.split(",");
        if (arr.length < 2) {
            return null;
        }
        Vector2f vec = new Vector2f();
        vec.setX(Float.parseFloat(arr[0]));
        vec.setY(Float.parseFloat(arr[1]));
        return vec;
    }
    
    public final Vector2f getAsVector2f(String key, Vector2f defValue) {
        Vector2f temp = getAsVector2f(key);
        return temp != null ? temp : defValue;
    }
    
    /**
     * 获取参数值，并以Quaternion形式返回，原始格式必须如： "x,y,z"<br>
     * x,y,z分别表示在各个轴上的旋转<STRONG>弧度</STRONG>数.如果参数长度不对，将返回null.
     * @param key
     * @return 
     */
    public final Quaternion getAsQuaternion(String key) {
        String temp = getAttribute(key);
        if (temp == null) 
            return null;
        String[] arr = temp.split(",");
        if (arr.length < 3) 
            return null;
        
        Quaternion qua = new Quaternion();
        qua.fromAngles(Float.parseFloat(arr[0]), Float.parseFloat(arr[1]), Float.parseFloat(arr[2]));
        return qua;
    }
    
    /**
     * 获取参数值，并以Quaternion形式返回，原始格式必须如： "x,y,z"<br>
     * x,y,z分别表示在各个轴上的旋转<STRONG>弧度</STRONG>数.如果参数长度不对，将返回null.
     * @param key
     * @param defValue 如果没有指定的参数值，则返回这个默认值。
     * @return 
     */
    public final Quaternion getAsQuaternion(String key, Quaternion defValue) {
        Quaternion temp = getAsQuaternion(key);
        return temp != null ? temp : defValue;
    }
    
    /**
     * 将参数值获取为颜色类型，数据类型必须是格式为："r,g,b,a"的格式。
     * @param key
     * @return 
     */
    public final ColorRGBA getAsColor(String key) {
        String temp = getAttribute(key);
        if (temp == null) 
            return null;
        String[] arr = temp.split(",");
        ColorRGBA color = new ColorRGBA();
        if (arr.length > 0) {
            color.r = Float.parseFloat(arr[0]);
        }
        if (arr.length > 1) {
            color.g = Float.parseFloat(arr[1]);
        }
        if (arr.length > 2) {
            color.b = Float.parseFloat(arr[2]);
        }
        if (arr.length > 3) {
            color.a = Float.parseFloat(arr[3]);
        }
        return color;
    }
    
    /**
     * 将参数值获取为颜色类型，数据类型必须是格式为："r,g,b,a"的格式。
     * @param key
     * @param defValue
     * @return 
     */
    public final ColorRGBA getAsColor(String key, ColorRGBA defValue) {
        ColorRGBA temp = getAsColor(key);
        return temp != null ? temp : defValue;
    }
    
    public final void clear() {
        data.clear();
    }
    
    public final boolean isEmpty() {
        return data.isEmpty();
    }
    
    @Override
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule oc = ex.getCapsule(this);
        UserData dataObject = new UserData(UserData.getObjectType(data), data);
        oc.write(dataObject, "_dataObject_", null);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        InputCapsule ic = im.getCapsule(this);
        UserData dataObject = (UserData) ic.readSavable("_dataObject_", new UserData(UserData.getObjectType(data), data));
        data = (Map) dataObject.getValue();
    }
}
