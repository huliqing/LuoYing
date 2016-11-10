/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.transfer;

import name.huliqing.luoying.xml.ObjectData;

/**
 * 在Transfer中交换的封装数据。
 * @author huliqing
 */
public class TransferData {
    
    private ObjectData objectData;
    private int amount;
    
    /**
     * 获取交换的物体
     * @return 
     */
    public ObjectData getObjectData() {
        return objectData;
    }

    /**
     * 设置交换的物体
     * @param object 
     */
    public void setObjectData(ObjectData object) {
        this.objectData = object;
    }

    /**
     * 获取物体的数量
     * @return 
     */
    public int getAmount() {
        return amount;
    }

    /**
     * 设置物体数量
     * @param amount 
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }
    
}
