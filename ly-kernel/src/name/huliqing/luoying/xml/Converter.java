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

import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.math.Vector4f;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author huliqing
 */
class Converter {
    
    /**
     * 将目标对象转换为字符串形式
     * @param value
     * @return 
     */
    public final static String getAsString(Object value) {
         if (value instanceof String) {
            return (String) value;
        } else if (value instanceof Vector2f) {
            Vector2f v2 = (Vector2f) value;
            return v2.x + "," + v2.y;
        } else if (value instanceof Vector3f) {
            Vector3f v3 = (Vector3f) value;
            return v3.x + "," + v3.y + "," + v3.z;
        } else if (value instanceof Vector4f) {
            Vector4f v4 = (Vector4f) value;
            return v4.x + "," + v4.y + "," + v4.z + "," + v4.w;
        } else if (value instanceof Quaternion) {
            Quaternion q4 = (Quaternion) value;
            return q4.getX() + "," + q4.getY() + "," + q4.getZ() + "," + q4.getW();
        } else if (value instanceof ColorRGBA) {
            ColorRGBA c4 = (ColorRGBA) value;
            return c4.r + "," + c4.g + "," + c4.b + "," + c4.a;
        }
        return value.toString();
    }
    
    public final static String[] getAsArray(Object value) {
        if (value instanceof String) {
            String[] result = ((String)value).split(",");
            for (int i = 0; i < result.length; i++) {
                result[i] = result[i].trim();
            }
            return result;
        }
        return (String[]) value;
    }
        
    public final static  List<String> getAsStringList(Object value) {
        if (value instanceof String) {
            String[] arr = getAsArray(value);
    //        return Arrays.asList(arr); // 不要再使用asList, asList使用的是内部类，这会在序列化时异常
            List<String> result = new ArrayList<String>(arr.length);
            Collections.addAll(result, arr);
            return result;
        }
        return (List<String>) value;
    }
    
    public final static Byte getAsByte(Object value) {
        if (value instanceof String) {
            return Byte.parseByte((String)value);
        }
        return (Byte) value;
    }
    
    public final static Integer getAsInteger(Object value) {
        if (value instanceof String) {
            return Integer.parseInt((String)value);
        }
        return (Integer) value;
    }
    
    public final static int[] getAsIntegerArray(Object value) {
        if (value instanceof String) {
            String[] strArr = getAsArray(value);
            return toIntegerArray(strArr);
        }
        return (int[]) value;
    }
    
    public final static List<Integer> getAsIntegerList(Object value) {
        if (value instanceof String) {
            String[] strArr = getAsArray(value);
            return toIntegerList(strArr);
        }
        return (List<Integer>) value;
    }
    
    public final static Long getAsLong(Object value) {
        if (value instanceof String) {
            return Long.parseLong(value.toString());
        }
        return (Long) value;
    }
    
    public final static long[] getAsLongArray(Object value) {
        if (value instanceof String) {
            String[] strArr = getAsArray(value);
            return toLongArray(strArr);
        }
        return (long[]) value;
    }
    
    public final static List<Long> getAsLongList(Object value) {
        if (value instanceof String) {
            String[] strArr = getAsArray(value);
            return toLongList(strArr);
        }
        return (List<Long>) value;
    }
    
    public final static Float getAsFloat(Object value) {
        if (value instanceof String) {
            return Float.parseFloat(value.toString());
        }
        return (Float) value;
    }
    
    public final static float[] getAsFloatArray(Object value) {
        if (value instanceof String) {
            String[] tempArr = value.toString().split(",");
            float[] resultArr = new float[tempArr.length];
            for (int i = 0; i < resultArr.length; i++) {
                resultArr[i] = Float.parseFloat(tempArr[i]);
            }
            return resultArr;
        }
        return (float[]) value;
    }
    
    public final static Boolean getAsBoolean(Object value) {
        if (value instanceof String) {
            return (value.equals("1") || value.toString().equalsIgnoreCase("true"));
        }
        return (Boolean) value;
    }
    
    public final static Vector2f getAsVector2f(Object value) {
        if (value instanceof String) {
            String[] arr = value.toString().split(",");
            return new Vector2f(Float.parseFloat(arr[0])
                    , Float.parseFloat(arr[1]));
        }
        return (Vector2f) value;
    }
    
    public final static Vector3f getAsVector3f(Object value) {
        if (value instanceof String) {
            String[] arr = value.toString().split(",");
            return new Vector3f(Float.parseFloat(arr[0])
                    , Float.parseFloat(arr[1])
                    , Float.parseFloat(arr[2]));
        }
        return (Vector3f) value;
    }
    
    /**
     * 格式, "x|y|z,x|y|z,...", 如果没有指定参数则返回null.
     * @param key
     * @return 
     */
    public final static Vector3f[] getAsVector3fArray(Object value) {
        if (value instanceof String) {
            String[] arrStr = value.toString().split(",");
            Vector3f[] result = new Vector3f[arrStr.length];
            for (int i = 0; i < arrStr.length; i++) {
                String[] xyz = arrStr[i].split("\\|");
                result[i] = new Vector3f(Float.parseFloat(xyz[0])
                        , Float.parseFloat(xyz[1])
                        , Float.parseFloat(xyz[2]));
            }
            return result;
        }
        return (Vector3f[]) value;
    }
    
    /**
     * 获取参数值，并以Vector4f形式返回，如果不存在指定参数则返回defValue
     * @param key
     * @param defValue
     * @return 
     */
    public final static Vector4f getAsVector4f(Object value) {
        if (value instanceof String) {
            String[] arr = value.toString().split(",");
            return new Vector4f(Float.parseFloat(arr[0])
                    , Float.parseFloat(arr[1])
                    , Float.parseFloat(arr[2])
                    , Float.parseFloat(arr[3]));
        }
        return (Vector4f) value;
    }
    
    /**
     * 获取参数值，并以Quaternion形式返回，原始格式必须如： "x,y,z"<br>
     * x,y,z分别表示在各个轴上的旋转<STRONG>弧度</STRONG>数.如果参数长度不对，将返回null.
     * @param key
     * @return 
     */
    public final static Quaternion getAsQuaternion(Object value) {
        if (value instanceof String) {
            String[] arr = value.toString().split(",");
            if (arr.length < 3) 
                return null;

            Quaternion qua = new Quaternion();
            qua.fromAngles(Float.parseFloat(arr[0]), Float.parseFloat(arr[1]), Float.parseFloat(arr[2]));
            return qua;
        }
        return (Quaternion) value;
    }
    
    public final static ColorRGBA getAsColor(Object value) {
        if (value instanceof String) {
            String[] arr = value.toString().split(",");
            return new ColorRGBA(Float.parseFloat(arr[0])
                    , Float.parseFloat(arr[1])
                    , Float.parseFloat(arr[2])
                    , Float.parseFloat(arr[3]));
        }
        return (ColorRGBA) value;
    }
    
    // -----------------------------------------------------------------------------
    
    private static int[] toIntegerArray(String[] strArr) {
        int[] result = new int[strArr.length];
        for (int i = 0; i < strArr.length; i++) {
            result[i] = Integer.parseInt(strArr[i]);
        }
        return result;
    }
    
    private static List<Integer> toIntegerList(String[] strArr) {
        List<Integer> list = new ArrayList<Integer>(strArr.length);
        for (String strArr1 : strArr) {
            list.add(Integer.parseInt(strArr1));
        }
        return list;
    }
    
    private static long[] toLongArray(String[] strArr) {
        long[] result = new long[strArr.length];
        for (int i = 0; i < strArr.length; i++) {
            result[i] = Long.parseLong(strArr[i]);
        }
        return result;
    }
    
    private static List<Long> toLongList(String[] strArr) {
        List<Long> result = new ArrayList<Long>(strArr.length);
        for (String s : strArr) {
            result.add(Long.parseLong(s));
        }
        return result;
    }
}
