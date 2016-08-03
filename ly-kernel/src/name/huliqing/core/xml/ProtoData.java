/*
 * To change this template, choose Tools | Templates
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
 * 物品基类,对于运行时，所有可动态改变的参数都需要封装在Data内。
 * Proto为物品的原形定义
 * @author huliqing
 */
@Serializable
public class ProtoData extends DataAttribute {
    public final static String USER_DATA = "PROTO_USER_DATA";
    private static long idIndex = System.currentTimeMillis();
    // 原形
    private transient Proto proto;
    
    // 物品的唯一ID,当前游戏的全局唯一ID
    private long uniqueId = generateUniqueId();
    
    // 物品ID，与proto中的id一致
    private String id;
    
    // 物品数量
    private int total;
    
    public ProtoData() {}
    
    public ProtoData(String id) {
        this.id = id;
    }

    @Override
    public final String getAttribute(String key) {
        // 先从本地实例中查找
        String result = super.getAttribute(key);
        if (result != null) {
            return result;
        }
        
        // 本地实例中找不到再从Proto中查找
        return getProto().getAttribute(key);
    }
    
    /**
     * @deprecated 以后外部不再从这里获取参数,这个方法要进行private屏蔽
     * 不要再让外部直接获取proto
     * @return 
     */
    public final Proto getProto() {
        if (proto == null) {
            proto = DataFactory.getProto(id);
        }
        return proto;
    }
    
    public final String getTagName() {
        return getProto().getTagName();
    }

    public final void setId(String id) {
        this.id = id;
    }
    
    public final String getId() {
        return id;
    }

    public final long getUniqueId() {
        return uniqueId;
    }
    
    /**
     * 获取图标，如果没有设置则返回null.
     * @return 
     */
    public final String getIcon() {
        return getProto().getIcon();
    }
    
    /**
     * 物体是否是可选择的
     * @return 
     */
    public boolean isPickable() {
        return getProto().getAsBoolean("pickable", false);
    }

    public String getHandler() {
        return getProto().getAttribute("handler");
    }
    
    /**
     * 判断是否为本地物体，对于本地物体在使用的时候不需要把事件广播到客户端或服务端。
     * @return 
     */
    public boolean isLocalObject() {
        return getProto().getAsBoolean("localObject", false);
    }
    
    /**
     * 获取单件物品的价值（单件，非total）,如果没有定义商品的价值，则默认0
     * @return 
     */
    public int getCost() {
        return getProto().getAsInteger("cost", 0);
    }
    
    /**
     * 获取物品数量
     * @return 
     */
    public int getTotal() {
        return total;
    }
    
    public void setTotal(int total) {
        this.total = total;
    }
    
    /**
     * 增加物品数量(amount<0为减少).
     * @param amount 
     * @return  
     */
    public int increaseTotal(int amount) {
        total += amount;
        if (total < 0) {
            total = 0;
        }
        return total;
    }

    /**
     * 获取描述说明
     * @return 
     */
    public String getDes() {
        return getProto().getAttribute("des");
    }
    
    /**
     * 给物体产生一个唯一ID
     * @return 
     */
    private synchronized static long generateUniqueId() {
        return idIndex++;
    }
    
    @Override
    public String toString() {
        return "ProtoData{" + "uniqueId=" + uniqueId + ", id=" + id + ", total=" + total + '}';
    }
    
    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(uniqueId, "uniqueId", -1);
        oc.write(id, "id", null);
        oc.write(total, "total", 0);
    }
    
    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule ic = im.getCapsule(this);
        uniqueId = ic.readLong("uniqueId", generateUniqueId());
        id = ic.readString("id", null);
        total = ic.readInt("total", 0);
    }
    
}
