///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.fighter.data;
//
//import com.jme3.export.InputCapsule;
//import com.jme3.export.JmeExporter;
//import com.jme3.export.JmeImporter;
//import com.jme3.export.OutputCapsule;
//import com.jme3.export.Savable;
//import com.jme3.network.serializing.Serializable;
//import java.io.IOException;
//import java.util.Map;
//
///**
// * 物品基类,对于运行时，所有可动态改变的参数都需要封装在Data内。
// * Proto为物品的原形定义
// * @author huliqing
// */
//@Serializable
//public class ProtoData implements Savable {
//    
//    public final static String USER_DATA = "PROTO_USER_DATA";
//    
//    private static long idIndex = System.currentTimeMillis();
//    
//    private transient Proto proto;
//    
//    // 物品的唯一ID,当前游戏的全局唯一ID
//    private long uniqueId = generateUniqueId();
//    
//    // 物品ID，与proto中的id一致
//    private String id;
//    
//    // 物品数量
//    private int total;
//    
//    @Override
//    public void write(JmeExporter ex) throws IOException {
//        OutputCapsule oc = ex.getCapsule(this);
//        oc.write(uniqueId, "uniqueId", -1);
//        oc.write(id, "id", null);
//        oc.write(total, "total", 0);
//    }
//
//    @Override
//    public void read(JmeImporter im) throws IOException {
//        InputCapsule ic = im.getCapsule(this);
//        uniqueId = ic.readLong("uniqueId", generateUniqueId());
//        id = ic.readString("id", null);
//        total = ic.readInt("total", 0);
//    }
//    
//    public ProtoData() {}
//    
//    public ProtoData(String id) {
//        this.id = id;
//    }
//    
//    public Proto getProto() {
//        if (proto == null) {
//            proto = DataLoaderFactory.getProto(id);
//        }
//        return proto;
//    }
//    
//    public String getId() {
//        return id;
//    }
//
//    public long getUniqueId() {
//        return uniqueId;
//    }
//    
//    /**
//     * 物体是否是可选择的
//     * @return 
//     */
//    public boolean isPickable() {
//        return getProto().getAsBoolean("pickable", false);
//    }
//    
//    /**
//     * 获取物品数量
//     * @return 
//     */
//    public int getTotal() {
//        return total;
//    }
//    
//    public void setTotal(int total) {
//        this.total = total;
//    }
//    
//    /**
//     * 增加物品数量(amount<0为减少).
//     * @param amount 
//     */
//    public int increaseTotal(int amount) {
//        total += amount;
//        if (total < 0) {
//            total = 0;
//        }
//        return total;
//    }
//    
//    public String getHandler() {
//        return getProto().getAttribute("handler");
//    }
//    
//    /**
//     * 给物体产生一个唯一ID
//     * @return 
//     */
//    private synchronized static long generateUniqueId() {
//        return idIndex++;
//    }
//
//    @Override
//    public String toString() {
//        return "ProtoData{" + "uniqueId=" + uniqueId + ", id=" + id + ", total=" + total + '}';
//    }
//    
//}
