/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.data;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.network.serializing.Serializable;
import java.io.IOException;
import name.huliqing.core.xml.ProtoData;

/**
 * 物品基类,对于运行时，所有可动态改变的参数都需要封装在Data内。
 * Proto为物品的原形定义
 * @author huliqing
 */
@Serializable
public class ObjectData extends ProtoData {
    public final static String USER_DATA = "PROTO_USER_DATA";
    
    // 物品数量
    private int total;
    
    public ObjectData() {}
    
    /**
     * 获取图标，如果没有设置则返回null.
     * @return 
     */
    public final String getIcon() {
        return proto.getAttribute("icon");
    }
    
    // remove20160805
//    /**
//     * 物体是否是可选择的
//     * @return 
//     */
//    public boolean isPickable() {
//        return getAsBoolean("pickable", false);
//    }
    
    /**
     * 判断是否为本地物体，对于本地物体在使用的时候不需要把事件广播到客户端或服务端。
     * @return 
     */
    public boolean isLocalObject() {
        return getProto().getAsBoolean("localObject", false);
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
     * 获取描述说明
     * @return 
     */
    public String getDes() {
        return proto.getAttribute("des");
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
    
    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(total, "total", 0);
        // 不要保存proto
    }
    
    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule ic = im.getCapsule(this);
        total = ic.readInt("total", 0);
    }

    @Override
    public String toString() {
        return "ObjectData{" + "total=" + total + '}';
    }

}
