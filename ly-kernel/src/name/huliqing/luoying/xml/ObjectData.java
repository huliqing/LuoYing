/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import name.huliqing.luoying.data.CustomUserData;

/**
 * 物品基类,对于运行时，所有可动态改变的参数都需要封装在Data内。
 * Proto为物品的原形定义
 * @author huliqing
 */
@Serializable
public class ObjectData implements Savable, Cloneable {
    
    private static long idIndex = System.currentTimeMillis();
    
    // 类型ID
    protected String id;
    
    // 全局唯一ID,当前游戏的全局唯一ID
    protected long uniqueId;
    
    // 本地数据,读取或设置数据时优先从本地localData中获取.
    protected HashMap<String, Savable> localData;
    
    // 原形数据,不在Network过程中进行序列化传输。
    // 因为proto可以直接从本地获取，不需要进行network传输，减少网络流量
    private transient Proto proto;
    
    public ObjectData() {
        uniqueId = generateUniqueId();
    }
    
    public Proto getProto() {
        // Proto不会在网络中传输，当在网络情况下, 需要在客户端重新获取。
        if (proto == null && id != null) {
            proto = DataFactory.getProto(id);
        }
        return proto;
    }

    /**
     * 设置原形数据，这个方法会在DataFactory中调用，不要在外部直接调用。
     * @param proto 
     */
    public void setProto(Proto proto) {
        this.proto = proto;
        this.id = proto.getAsString("id");
    }

    public String getTagName() {
        return getProto().getTagName();
    }
    
    public String getId() {
        return id;
    }
    
    /**
     * 获取物体的唯一id
     * @return 
     */
    public final long getUniqueId() {
        return uniqueId;
    }

    /**
     * 设置唯一id.
     * @param uniqueId 
     */
    public void setUniqueId(long uniqueId) {
        this.uniqueId = uniqueId;
    }
    
    /**
     * 给物体产生一个唯一ID
     * @return 
     */
    private synchronized static long generateUniqueId() {
        return idIndex++;
    }
   
    @Override
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(id, "id", null);
        oc.write(uniqueId, "uniqueId", 0);
        oc.writeStringSavableMap(localData, "localData", null);
        // 不需要要保存proto
    }
    
    @Override
    public void read(JmeImporter im) throws IOException {
        InputCapsule ic = im.getCapsule(this);
        id = ic.readString("id", null);
        uniqueId = ic.readLong("uniqueId", generateUniqueId());
        localData = (HashMap<String, Savable>) ic.readStringSavableMap("localData", null);
    }

    @Override
    public String toString() {
        return "ObjectData{" + "id=" + id + ", uniqueId=" + uniqueId + ", proto=" + proto + ", userData=" + localData + '}';
    }
    
    /**
     * 从Data中获取数据,如果不存在指定键值的数据，则返回null.
     * @param key
     * @return 
     */
    private Object getAttributeFromLocal(String key) {
        if (localData == null) {
            return null;
        }
        Savable s = localData.get(key);
        if (s instanceof CustomUserData) {
            return ((CustomUserData) s).getValue();
        } else {
            return s;
        }
    }
    
    /**
     * 设置参数值，如果参数已经存在，则该参数将被替换
     * @param key
     * @param value 
     */
    public void setAttribute(String key, Object value) {
        if (value == null) {
            if (localData != null) {
                localData.remove(key);
                if (localData.isEmpty()) {
                    localData = null;
                }
            }
        } else {
            if (localData == null) {
                localData = new HashMap<String, Savable>();
            }
            if (value instanceof Savable) {
                localData.put(key, (Savable) value);
            } else {
                localData.put(key, new CustomUserData(CustomUserData.getObjectType(value), value));
            }
        }
    }
    
    /**
     * 从本地数据中获取指定参数的值，如果本地数据不存在，则从原形中获取。
     * @param key
     * @return 
     */
    public Object getAttribute(String key) {
        Object value = getAttributeFromLocal(key);
        if (value != null) {
            return value;
        }
        return getProto().getAttribute(key);
    }

    public final String getAsString(String key, String defValue) {
        Object value = getAttributeFromLocal(key);
        if (value != null) {
            return value.toString();
        }
        return getProto().getAsString(key, defValue);
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
        Object value = getAttributeFromLocal(key);
        if (value != null) {
            return (String[]) value;
        }
        return getProto().getAsArray(key);
    }
    
    public final Integer getAsInteger(String key) {
        Object value = getAttributeFromLocal(key);
        if (value != null) {
            return (Integer) value;
        }
        return getProto().getAsInteger(key);
    }
    
    public final int getAsInteger(String key, int defValue) {
        Object value = getAttributeFromLocal(key);
        if (value != null) {
            return (Integer) value;
        }
        return getProto().getAsInteger(key, defValue);
    }
    
    /**
     * 把参数获取为整形数组，如果没有设置该参数则返回null.
     * @param key
     * @return 
     */
    public final int[] getAsIntegerArray(String key) {
        Object value = getAttributeFromLocal(key);
        if (value != null) {
            return (int[]) value;
        }
        return getProto().getAsIntegerArray(key);
    }
    
    /**
     * 把参数获取为整形集合，如果没有设置该参数则返回null.
     * @param key
     * @return 
     */
    public final List<Integer> getAsIntegerList(String key) {
        Object value = getAttributeFromLocal(key);
        if (value != null) {
            return (List<Integer>) value;
        }
        return getProto().getAsIntegerList(key);
    }
    
    public final Long getAsLong(String key) {
        Object value = getAttributeFromLocal(key);
        if (value != null) {
            return (Long) value;
        }
        return getProto().getAsLong(key);
    }
    
    public final Long getAsLong(String key, long defValue) {
        Object value = getAttributeFromLocal(key);
        if (value != null) {
            return (Long) value;
        }
        return getProto().getAsLong(key, defValue);
    }
    
    public final Float getAsFloat(String key) {
        Object value = getAttributeFromLocal(key);
        if (value != null) {
            return (Float) value;
        }
        return getProto().getAsFloat(key);
    }
    
    public final float getAsFloat(String key, float defValue) {
        Object value = getAttributeFromLocal(key);
        if (value != null) {
            return (Float) value;
        }
        return getProto().getAsFloat(key, defValue);
    }
    
    public final float[] getAsFloatArray(String key) {
        Object value = getAttributeFromLocal(key);
        if (value != null) {
            return (float[]) value;
        }
        return getProto().getAsFloatArray(key);
    }
    
    public final Boolean getAsBoolean(String key) {
        Object value = getAttributeFromLocal(key);
        if (value != null) {
            return (Boolean) value;
        }
        return getProto().getAsBoolean(key);
    }
    
    public final boolean getAsBoolean(String key, boolean defValue) {
        Object value = getAttributeFromLocal(key);
        if (value != null) {
            return (Boolean) value;
        }
        return getProto().getAsBoolean(key, defValue);
    }
    
    /**
     * 获取参数值，并以List形式返回，原始参数值格式必须是使用半角逗号","分隔的,
     * 如：属性值 "1,3,2", 如果属性值为null或空，则返回null.
     * @param key
     * @return 
     */
    public final List<String> getAsStringList(String key) {
        Object value = getAttributeFromLocal(key);
        if (value != null) {
            return (List<String>) value;
        }
        return getProto().getAsStringList(key);
    }
    
    public final Vector2f getAsVector2f(String key) {
        Object value = getAttributeFromLocal(key);
        if (value != null) {
            return (Vector2f) value;
        }
        return getProto().getAsVector2f(key);
    }
    
    public final Vector2f getAsVector2f(String key, Vector2f defValue) {
        Object value = getAttributeFromLocal(key);
        if (value != null) {
            return (Vector2f) value;
        }
        return getProto().getAsVector2f(key, defValue);
    }
    
    /**
     * 获取参数值，并以Vector3f形式返回，原始格式必须如："x"或 "x,y" 或 "x,y,z"，
     * 如果参数不对，将返回null.<br>
     * 当只有x值时，则y = z =x; <br>
     * 当只有x,y值时，z = 1; <br>
     * @param key
     * @return 
     */
    public final Vector3f getAsVector3f(String key) {
        Object value = getAttributeFromLocal(key);
        if (value != null) {
            return (Vector3f) value;
        }
        return getProto().getAsVector3f(key);
    }
    
    public final Vector3f getAsVector3f(String key, Vector3f defValue) {
        Object value = getAttributeFromLocal(key);
        if (value != null) {
            return (Vector3f) value;
        }
        return getProto().getAsVector3f(key, defValue);
    }
    
    /**
     * 格式, "x|y|z,x|y|z,...", 如果没有指定参数则返回null.
     * @param key
     * @return 
     */
    public final Vector3f[] getAsVector3fArray(String key) {
        Object value = getAttributeFromLocal(key);
        if (value != null) {
            return (Vector3f[]) value;
        }
        return getProto().getAsVector3fArray(key);
    }
    
    /**
     * 获取参数值，并以Vector4f形式返回，格式："x,y,z,w"
     * @param key
     * @return 
     */
    public final Vector4f getAsVector4f(String key) {
        Object value = getAttributeFromLocal(key);
        if (value != null) {
            return (Vector4f) value;
        }
        return getProto().getAsVector4f(key);
    }
    
    /**
     * 获取参数值，并以Vector4f形式返回，如果不存在指定参数则返回defValue
     * @param key
     * @param defValue
     * @return 
     */
    public final Vector4f getAsVector4f(String key, Vector4f defValue) {
        Object value = getAttributeFromLocal(key);
        if (value != null) {
            return (Vector4f) value;
        }
        return getProto().getAsVector4f(key, defValue);
    }
    
    /**
     * 获取参数值，并以Quaternion形式返回，原始格式必须如： "x,y,z"<br>
     * x,y,z分别表示在各个轴上的旋转<STRONG>弧度</STRONG>数.如果参数长度不对，将返回null.
     * @param key
     * @return 
     */
    public final Quaternion getAsQuaternion(String key) {
        Object value = getAttributeFromLocal(key);
        if (value != null) {
            return (Quaternion) value;
        }
        return getProto().getAsQuaternion(key);
    }
    
    /**
     * 获取参数值，并以Quaternion形式返回，原始格式必须如： "x,y,z"<br>
     * x,y,z分别表示在各个轴上的旋转<STRONG>弧度</STRONG>数.如果参数长度不对，将返回null.
     * @param key
     * @param defValue 如果没有指定的参数值，则返回这个默认值。
     * @return 
     */
    public final Quaternion getAsQuaternion(String key, Quaternion defValue) {
        Object value = getAttributeFromLocal(key);
        if (value != null) {
            return (Quaternion) value;
        }
        return getProto().getAsQuaternion(key, defValue);
    }
    
    /**
     * 将参数值获取为颜色类型，数据类型必须是格式为："r,g,b,a"的格式。
     * @param key
     * @return 
     */
    public final ColorRGBA getAsColor(String key) {
        Object value = getAttributeFromLocal(key);
        if (value != null) {
            return (ColorRGBA) value;
        }
        return getProto().getAsColor(key);
    }
    
    /**
     * 将参数值获取为颜色类型，数据类型必须是格式为："r,g,b,a"的格式。
     * @param key
     * @param defValue
     * @return 
     */
    public final ColorRGBA getAsColor(String key, ColorRGBA defValue) {
        Object value = getAttributeFromLocal(key);
        if (value != null) {
            return (ColorRGBA) value;
        }
        return getProto().getAsColor(key, defValue);
    }
    
    /**
     * 克隆ObjectData。
     * 注意：除物体的uniqueId之外，其它字段都尽量进行深度克隆。
     * @return 
     */
    @Override
    public ObjectData clone() {
        try {
            ObjectData newObject = (ObjectData) super.clone();
            
            // 唯一id必须变化
            newObject.uniqueId = generateUniqueId();
            
            // 本地数据localData的深度克隆
            if (localData != null && !localData.isEmpty()) {
                newObject.localData = new HashMap<String, Savable>(localData.size());
                for (Entry<String, Savable> e : localData.entrySet()) {
                    if (e.getValue() instanceof Cloneable) {
                        localData.put(e.getKey(), cloneObject(e.getValue()));
                    } else {
                        localData.put(e.getKey(), e.getValue());
                    }
                }
            }
            return newObject;
        } catch( CloneNotSupportedException e ) {
            throw new RuntimeException( "Can't clone control for spatial", e );
        }
    }
    
    private <T> T cloneObject(T object) {
        if(object == null ) {
            return null;
        }
        try {
            Method m = object.getClass().getMethod("clone");
            return (T) m.invoke(object);
        } catch(Exception e) {
            throw new RuntimeException("Could not cloneObject for Object=" + object);
        }
    }
}
