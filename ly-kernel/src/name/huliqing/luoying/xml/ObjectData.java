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
import com.jme3.scene.UserData;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import name.huliqing.luoying.LuoYingException;
import name.huliqing.luoying.data.SavableArray;
import name.huliqing.luoying.data.SavableString;

/**
 * 物品基类,对于运行时，所有可动态改变的参数都需要封装在Data内。
 * Proto为物品的原形定义
 * @author huliqing
 */
@Serializable
public class ObjectData implements Savable, Cloneable {
//    private static final Logger LOG = Logger.getLogger(ObjectData.class.getName());
    
    /** 类型ID */
    protected String id;
    
    /** 全局唯一ID,当前游戏的全局唯一ID */
    protected long uniqueId;
    
    /** 本地数据,读取或设置数据时优先从本地localData中获取*/
    protected HashMap<String, Savable> localData;
    
    /** 
     * 原形数据,不在Network过程中进行序列化传输。
     *因为proto可以直接从本地获取，不需要进行network传输，也不需要档存。
     */
    private transient Proto proto;
    
    public ObjectData() {}
    
    public Proto getProto() {
        // Proto不会在网络中传输，当在网络情况下, 需要在客户端重新获取。
        if (proto == null) {
            if (id != null) {
                proto = DataFactory.getProto(id);
            }
            if (proto == null) {
                throw new LuoYingException("Could not find proto, objectData=" + this + ", id=" + id);
            }
        }
        return proto;
    }

    /**
     * 设置原形数据，这个方法会在DataFactory中调用，不要在外部直接调用。
     * @param proto 
     */
    public void setProto(Proto proto) {
        this.proto = proto;
        this.id = proto.getId();
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
     * 设置唯一id, 这个ID是物体在创建的时候自动生成的，不要直接在运行时改变。
     * @param uniqueId 
     */
    public void setUniqueId(long uniqueId) {
        this.uniqueId = uniqueId;
    }

    /**
     * 获取本地变量
     * @return 
     */
    public HashMap<String, Savable> getLocalData() {
        return localData;
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
        
        if (s instanceof UserData) {
            return ((UserData) s).getValue();
            
        } else if (s instanceof SavableString) {
            return ((SavableString)s).getValue();
            
        } else if (s instanceof SavableArray) {
            return ((SavableArray)s).getValue();
            
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
            
            if (value instanceof String) {
                localData.put(key, new SavableString((String) value));
                
            } else if (value.getClass().isArray()) {
                localData.put(key, new SavableArray(value));
                
            } else if (value instanceof Savable) {
                localData.put(key, (Savable) value);
                
            } else {
                localData.put(key, new UserData(UserData.getObjectType(value), value));
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
    
    public final Byte getAsByte(String key) {
        Object value = getAttributeFromLocal(key);
        if (value != null) {
            return (Byte) value;
        }
        return getProto().getAsByte(key);
    }
    
    public final byte getAsByte(String key, byte defValue) {
        Object value = getAttributeFromLocal(key);
        if (value != null) {
            return (Byte) value;
        }
        return getProto().getAsByte(key, defValue);
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
    
    public final long getAsLong(String key, long defValue) {
        Object value = getAttributeFromLocal(key);
        if (value != null) {
            return (Long) value;
        }
        return getProto().getAsLong(key, defValue);
    }
    
    public final long[] getAsLongArray(String key) {
        Object value = getAttributeFromLocal(key);
        if (value != null) { 
            return (long[]) value;
        }
        return getProto().getAsLongArray(key); 
    }
    
    public final List<Long> getAsLongList(String key) {
        Object value = getAttributeFromLocal(key);
        if (value != null) {
            return (List<Long>) value;
        }
        return getProto().getAsLongList(key);
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
    
    public final <T extends Savable> T getAsSavable(String key) {
        Object value = getAttributeFromLocal(key);
        if (value != null) {
            return (T) value;
        }
        return getProto().getAsSavable(key);
    }
    
    /**
     * 克隆ObjectData。
     * 注意：除物体的uniqueId之外，其它字段都尽量进行深度克隆。
     * @return 
     */
    @Override
    public ObjectData clone() {
        try {
            ObjectData clone = (ObjectData) super.clone();
            
            // 这里要把localData为null,以便重新创建本地变量(localData)实例。
            // 非常重要: 这引起过一个BUG，买卖物品时,买家和卖家引用了同一个"物品数量"
            clone.localData = null; 
            
            if (localData != null && !localData.isEmpty()) {
                Set<String> keys = localData.keySet();
                for (String key : keys) {
                    // 注：不要直接从localData中获取，因为localData中的数据是进行了包装过的。
                    // 调用getAttributeFromLocal可以进行自动解包,并通过setAttribute来自动再打包。
                    Object value = getAttributeFromLocal(key);
                    if (value instanceof Cloneable) {
                        clone.setAttribute(key, SimpleCloner.deepClone(value));
//                        LOG.log(Level.INFO, "----=={0}, type={1}", new Object[] {value, value.getClass().getSimpleName()});
                    } else {
                        clone.setAttribute(key, value);
//                        LOG.log(Level.INFO, "xxxx=={0}, type={1}", new Object[] {value, value.getClass().getSimpleName()});
                    }
                }
            }
            return clone;
            
        } catch(CloneNotSupportedException e ) {
            throw new LuoYingException(e);
        }
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
        uniqueId = ic.readLong("uniqueId", 0);
        localData = (HashMap<String, Savable>) ic.readStringSavableMap("localData", null);
    }
    
    @Override
    public String toString() {
        return "ObjectData{" + "id=" + id + ", uniqueId=" + uniqueId + ", proto=" + proto + ", userData=" + localData + '}';
    }
}
