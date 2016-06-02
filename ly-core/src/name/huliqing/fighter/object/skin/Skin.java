/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.skin;

import name.huliqing.fighter.data.SkinData;
import name.huliqing.fighter.object.actor.Actor;

/**
 *
 * @author huliqing
 */
public interface Skin {
    
    /**
     * 获取绑定的数据
     * @return 
     */
    SkinData getData();
    
    /**
     * 把skin添加到角色身上
     * @param actor 
     */
    void attach(Actor actor);
    
    /**
     * 把当前skin从角色身上剥离
     * @param actor 
     */
    void detach(Actor actor);
    
}
