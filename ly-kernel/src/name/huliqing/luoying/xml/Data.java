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
package name.huliqing.luoying.xml;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.math.Vector4f;
import com.jme3.network.serializing.Serializable;
import com.jme3.scene.UserData;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author huliqing
 */
@Serializable
public class Data implements Savable {

    // 扩展参数
    protected Map<String, Object> data;
    
    public Data() {}
    
    public Data(Map data) {
        if (data == null) {
            throw new NullPointerException("Data could not be null!");
        }
        this.data = data;
    }
    
    /**
     * 清理所有数据
     */
    public final void clear() {
        if (data != null) {
            data.clear();
        }
    }
    
    /**
     * 检查data是否为空
     * @return 
     */
    public final boolean isEmpty() {
        return data == null || data.isEmpty();
    }
    
    /**
     * 设置一个参数值，如果value为null则清除该值。
     * @param key
     * @param value 
     */
    public void setAttribute(String key, Object value) {
        if (value == null) {
            if (data != null) {
                data.remove(key);
            }
            return;
        } 
        if (data == null) {
            data = new HashMap<String, Object>();
        }
        data.put(key, value);
    }
    
    /**
     * 直接获得属性值，这个方法不会返回“”(空值），所有“”值的参数都将返回null.
     * @param key
     * @return 
     */
    public Object getAttribute(String key) {
        if (data == null) {
            return null;
        }
        Object obj = data.get(key);
        // 注：这里要防止返回“空”值，这样其它依赖于这个方法的方法就不需要再判断空值，并在转换类型的时候比较简单.
        // 比如在：Integer.parseInt的时候就不需要再判断空值。
        if (obj == null || obj.equals("")) {
            return null;
        }
        return obj;
    }
    
    public final String getAsString(String key, String defValue) {
        Object value = getAttribute(key);
        if (value != null) {
            return value.toString();
        }
        return defValue;
    }
    
    /**
     * 获取参数，如果不存在则返回null, 如果为空值也返回null
     * @param key
     * @return 
     */
    public final String getAsString(String key) {
        return getAsString(key, null);
    }
    
    /**
     * 获取参数值，并以数组形式返回，原始参数值格式必须是使用半角逗号","分隔的,
     * 如：属性值 "1,3,2" 将返回为数组 {"1","3","2"}.
     * 如果属性值为null或空，则返回null.
     * @param key
     * @return 
     */
    public final String[] getAsArray(String key) {
        String temp = getAsString(key);
        if (temp == null) 
            return null;
        return Converter.getAsArray(temp);
    }

    public final Byte getAsByte(String key) {
        Object value = getAttribute(key);
        if (value != null) {
            return Converter.getAsByte(value);
        }
        return null;
    }
    
    public final byte getAsByte(String key, byte defValue) {
        Byte value = getAsByte(key);
        if (value != null) {
            return value;
        }
        return defValue;
    }
    
    public final Integer getAsInteger(String key) {
        Object value = getAttribute(key);
        if (value != null) {
            return Converter.getAsInteger(value);
        }
        return null;
    }
    
    public final int getAsInteger(String key, int defValue) {
        Integer value = getAsInteger(key);
        if (value != null) {
            return value;
        }
        return defValue;
    }
    
    /**
     * 把参数获取为整形数组，如果没有设置该参数则返回null.
     * @param key
     * @return 
     */
    public final int[] getAsIntegerArray(String key) {
        Object value = getAttribute(key);
        if (value != null) {
            return Converter.getAsIntegerArray(value);
        }
        return null;
    }
    
    /**
     * 把参数获取为整形集合，如果没有设置该参数则返回null.
     * @param key
     * @return 
     */
    public final List<Integer> getAsIntegerList(String key) {
        Object value = getAttribute(key);
        if (value != null) {
            return Converter.getAsIntegerList(value);
        }
        return null;
    }
    
    public final Long getAsLong(String key) {
        Object value = getAttribute(key);
        if (value != null) {
            return Converter.getAsLong(value);
        }
        return null;
    }
    
    public final long getAsLong(String key, long defValue) {
        Long value = getAsLong(key);
        if (value != null) {
            return value;
        }
        return defValue;
    }
    
    /**
     * 把参数获取为Long数组，如果没有设置该参数则返回null.
     * @param key
     * @return 
     */
    public final long[] getAsLongArray(String key) {
        Object value = getAttribute(key);
        if (value != null) {
            return Converter.getAsLongArray(value);
        }
        return null;
    }
    
    /**
     * 把参数获取为Long列表，如果没有设置该参数则返回null.
     * @param key
     * @return 
     */
    public final List<Long> getAsLongList(String key) {
        Object value = getAttribute(key);
        if (value != null) {
            return Converter.getAsLongList(value);
        }
        return null;
    }
    
    public final Float getAsFloat(String key) {
        Object value = getAttribute(key);
        if (value != null) {
            return Converter.getAsFloat(value);
        }
        return null;
    }
    
    public final float getAsFloat(String key, float defValue) {
        Float value = getAsFloat(key);
        if (value != null) {
            return value;
        }
        return defValue;
    }
    
    public final float[] getAsFloatArray(String key) {
        Object value = getAttribute(key);
        if (value != null) {
            return Converter.getAsFloatArray(value);
        }
        return null;
    }
    
    public final Boolean getAsBoolean(String key) {
        Object value = getAttribute(key);
        if (value != null) {
            return Converter.getAsBoolean(value);
        }
        return null;
    }
    
    public final boolean getAsBoolean(String key, boolean defValue) {
        Boolean value = getAsBoolean(key);
        if (value != null) {
            return value;
        }
        return defValue;
    }
    
    /**
     * 获取参数值，并以List形式返回，原始参数值格式必须是使用半角逗号","分隔的,
     * 如：属性值 "1,3,2", 如果属性值为null或空，则返回null.
     * @param key
     * @return 
     */
    public final List<String> getAsStringList(String key) {
        Object value = getAttribute(key);
        if (value != null) {
            return Converter.getAsStringList(value);
        }
        return null;
    }
    
    /**
     * 获取参数值，并以Vector2f形式返回，格式："x,y"
     * @param key
     * @return 
     */
    public final Vector2f getAsVector2f(String key) {
        Object value = getAttribute(key);
        if (value != null) {
            return Converter.getAsVector2f(value);
        }
        return null;
    }
    
    /**
     * 获取参数值，并以Vector2f形式返回，如果不存在指定的参数则以defValue返回
     * @param key
     * @param defValue
     * @return 
     */
    public final Vector2f getAsVector2f(String key, Vector2f defValue) {
        Vector2f value = getAsVector2f(key);
        if (value != null) {
            return value;
        }
        return defValue;
    }
    
    /**
     * 获取参数值，并以Vector3f形式返回，格式："x,y,z"
     * @param key
     * @return 
     */
    public final Vector3f getAsVector3f(String key) {
        Object value = getAttribute(key);
        if (value != null) {
            return Converter.getAsVector3f(value);
        }
        return null;
    }
    
    /**
     * 获取参数值，如果不存在指定的参数则返回defValue
     * @param key
     * @param defValue
     * @return 
     */
    public final Vector3f getAsVector3f(String key, Vector3f defValue) {
        Vector3f value = getAsVector3f(key);
        if (value != null) {
            return value;
        }
        return defValue;
    }
    
    /**
     * 格式, "x|y|z,x|y|z,...", 如果没有指定参数则返回null.
     * @param key
     * @return 
     */
    public final Vector3f[] getAsVector3fArray(String key) {
        Object value = getAttribute(key);
        if (value != null) {
            return Converter.getAsVector3fArray(value);
        }
        return null;
    }
    
    /**
     * 获取参数值，并以Vector4f形式返回，格式："x,y,z,w"
     * @param key
     * @return 
     */
    public final Vector4f getAsVector4f(String key) {
        Object value = getAttribute(key);
        if (value != null) {
            return Converter.getAsVector4f(value);
        }
        return null;
    }
    
    /**
     * 获取参数值，并以Vector4f形式返回，如果不存在指定参数则返回defValue
     * @param key
     * @param defValue
     * @return 
     */
    public final Vector4f getAsVector4f(String key, Vector4f defValue) {
        Vector4f value = getAsVector4f(key);
        if (value != null) {
            return value;
        }
        return defValue;
    }
    
    /**
     * 获取参数值，并以Quaternion形式返回，原始格式必须如： "x,y,z"<br>
     * x,y,z分别表示在各个轴上的旋转<STRONG>弧度</STRONG>数.如果参数长度不对，将返回null.
     * @param key
     * @return 
     */
    public final Quaternion getAsQuaternion(String key) {
        Object value = getAttribute(key);
        if (value != null) {
            return Converter.getAsQuaternion(value);
        }
        return null;
    }
    
    /**
     * 获取参数值，并以Quaternion形式返回，原始格式必须如： "x,y,z"<br>
     * x,y,z分别表示在各个轴上的旋转<STRONG>弧度</STRONG>数.如果参数长度不对，将返回null.
     * @param key
     * @param defValue 如果没有指定的参数值，则返回这个默认值。
     * @return 
     */
    public final Quaternion getAsQuaternion(String key, Quaternion defValue) {
        Quaternion value = getAsQuaternion(key);
        if (value != null) {
            return value;
        }
        return defValue;
    }
    
    /**
     * 将参数值获取为颜色类型，数据类型必须是格式为："r,g,b,a"的格式。
     * @param key
     * @return 
     */
    public final ColorRGBA getAsColor(String key) {
        Object value = getAttribute(key);
        if (value != null) {
            return Converter.getAsColor(value);
        }
        return null;
    }
    
    /**
     * 将参数值获取为颜色类型，数据类型必须是格式为："r,g,b,a"的格式。
     * @param key
     * @param defValue
     * @return 
     */
    public final ColorRGBA getAsColor(String key, ColorRGBA defValue) {
        ColorRGBA value = getAsColor(key);
        if (value != null) {
            return value;
        }
        return defValue;
    }
    
    /**
     * 获取参数并以Savable对象返回，获取前你必须确定你要的类型是匹配的，否则会报错。
     * @param <T>
     * @param key
     * @return 
     */
    public final <T extends Savable> T getAsSavable(String key) {
        Object value = data.get(key);
        return (T) value;
    }
    
    @Override
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule oc = ex.getCapsule(this);
        if (data != null) {
            UserData dataObject = new UserData(UserData.getObjectType(data), data);
            oc.write(dataObject, "_dataObject_", null);
        }
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        InputCapsule ic = im.getCapsule(this);
        UserData dataObject = (UserData) ic.readSavable("_dataObject_", null);
        if (dataObject != null) {
            data = (Map) dataObject.getValue();
        }
    }

    @Override
    public String toString() {
        return "Data{" + "data=" + data + '}';
    }
    
}
