/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.entity;

import com.jme3.light.Light;

/**
 * 灯光，光源类实体
 * @author huliqing
 */
public interface LightEntity extends Entity {
    
    /**
     * 获取光源
     * @return 
     */
    Light getLight();
    
}
