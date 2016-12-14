/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.layer.service;

import name.huliqing.luoying.Inject;
import name.huliqing.luoying.object.entity.Entity;

/**
 *
 * @author huliqing
 */
public interface ResistService extends Inject {
    
    /**
     * 获取Entity对于指定状态的抗性值。
     * @param entity
     * @param state
     * @return 
     */
    float getResist(Entity entity, String state);
}
