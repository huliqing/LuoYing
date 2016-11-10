/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.layer.service;

import name.huliqing.luoying.xml.ObjectData;
import name.huliqing.luoying.layer.network.ObjectNetwork;

/**
 *
 * @author huliqing
 */
public interface ObjectService extends ObjectNetwork {
    
    /**
     * 创建一个物体
     * @param id
     * @return 
     */
    ObjectData createData(String id);
   
}
