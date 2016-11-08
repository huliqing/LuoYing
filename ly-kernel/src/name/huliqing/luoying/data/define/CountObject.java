/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.data.define;

/**
 * 可量化物体接口，这类物体可以计算数量，可以获取数量及设置数量。
 * 游戏中所有可进行量化的物体都应该实体这个接口，以便获得公共支持，如：普通杂物、装备等。只有实现这个接口，物品
 * 才可能在角色之间交换、发送、买卖等。
 * @author huliqing
 */
public interface CountObject {
    
    /**
     * 获取物体的总数
     * @return 
     */
    int getTotal();
    
    /**
     * 设置物体的总数
     * @param total 
     */
    void setTotal(int total);
    
}
