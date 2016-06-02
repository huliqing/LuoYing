/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object;

import name.huliqing.fighter.data.ProtoData;

/**
 * 基本物体
 * @author huliqing
 */
public interface ProtoObject<T extends ProtoData> {
    
    /**
     * 更新实时状态下的数据
     */
    T getUpdateData();
    
    /**
     * 获取数据
     * @return 
     */
    T getData();
    
}
