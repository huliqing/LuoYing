/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.view.transfer;

import name.huliqing.luoying.xml.DataProcessor;
import name.huliqing.luoying.xml.ObjectData;

/**
 * 在Transfer中交换的封装数据。
 * @author huliqing
 */
public class TransferData {
    
    private DataProcessor<ObjectData> object;
    private int count;
    
    /**
     * 获取交换的物体
     * @return 
     */
    public DataProcessor<ObjectData> getObject() {
        return object;
    }

    /**
     * 设置交换的物体
     * @param object 
     */
    public void setObject(DataProcessor<ObjectData> object) {
        this.object = object;
    }

    /**
     * 获取物体的数量
     * @return 
     */
    public int getCount() {
        return count;
    }

    /**
     * 设置物体数量
     * @param count 
     */
    public void setCount(int count) {
        this.count = count;
    }
    
}
