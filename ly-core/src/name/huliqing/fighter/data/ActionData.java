/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.data;

import com.jme3.network.serializing.Serializable;

/**
 * 角色行为逻辑数据
 * @author huliqing
 */
@Serializable
public class ActionData extends ProtoData {
    
    public ActionData(){
        super();
    }
    
    public ActionData(String id) {
        super(id);
    }

}
