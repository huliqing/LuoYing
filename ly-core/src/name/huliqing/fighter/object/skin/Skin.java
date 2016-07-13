/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.skin;

import name.huliqing.fighter.data.SkinData;
import name.huliqing.fighter.object.DataProcessor;
import name.huliqing.fighter.object.actor.Actor;

/**
 *
 * @author huliqing
 * @param <T>
 */
public interface Skin<T extends SkinData> extends DataProcessor<T>{
    
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
