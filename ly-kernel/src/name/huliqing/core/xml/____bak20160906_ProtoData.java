/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.xml;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.network.serializing.Serializable;
import java.io.IOException;

/**
 *
 * @author huliqing
 */
@Serializable
public class ____bak20160906_ProtoData extends Data {
    
    private static long idIndex = System.currentTimeMillis();
    
    protected transient Proto proto;
    protected String id;
    // 物品的唯一ID,当前游戏的全局唯一ID
    protected long uniqueId = generateUniqueId();

    public Proto getProto() {
        return proto;
    }

    public void setProto(Proto proto) {
        this.proto = proto;
        this.id = proto.getAsString("id");
    }

    public String getTagName() {
        return proto.getTagName();
    }
    
    public String getId() {
        return id;
    }
    
    public final long getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(long uniqueId) {
        this.uniqueId = uniqueId;
    }
    
    /**
     * 从Data中获取数据,如果不存在指定键值的数据，则返回null.
     * @param key
     * @return 
     */
    @Override
    public final Object getAttribute(String key) {
        //所有数据的查找都是优先从本地实例查找，如果本地找不到再向上从原形proto中查找，如果再找不到则返回null.
        Object result = super.getAttribute(key);
        if (result != null) {
            return result;
        }
        
        // 本地实例中找不到再从Proto中查找
        return proto.getAttribute(key);
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
        super.write(ex);
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(id, "id", null);
        oc.write(uniqueId, "uniqueId", 0);
        // 不要保存proto
    }
    
    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule ic = im.getCapsule(this);
        id = ic.readString("id", null);
        uniqueId = ic.readLong("uniqueId", generateUniqueId());
        // 重新从系统读取proto
        proto = DataFactory.getProto(id);
    }

    @Override
    public String toString() {
        return "ProtoData{" + "proto=" + proto + ", id=" + id + ", uniqueId=" + uniqueId + '}';
    }
    
}
