/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.data;

import com.jme3.network.serializing.Serializable;
import name.huliqing.ly.xml.ProtoData;

/**
 * 物品基类,对于运行时，所有可动态改变的参数都需要封装在Data内。
 * Proto为物品的原形定义
 * @author huliqing
 */
@Serializable
public class ObjectData extends ProtoData {
    public final static String USER_DATA = "PROTO_USER_DATA";
    
    /**
     * 获取图标，如果没有设置则返回null.
     * @return 
     */
    public String getIcon() {
        return getAsString("icon");
    }
    
    /**
     * 获取物品数量
     * @return 
     */
    public int getTotal() {
        return getAsInteger("total", 0);
    }
    
    /**
     * 设置物品数量
     * @param total 
     */
    public void setTotal(int total) {
        setAttribute("total", total);
    }
    
}
