/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.data;

import com.jme3.network.serializing.Serializable;

/**
 * 角色动画数据
 * @author huliqing
 */
@Serializable
public class ActorAnimData extends AnimData {
    
    public ActorAnimData() {}
    
    public ActorAnimData(String id) {
        super(id);
    }
    
}
