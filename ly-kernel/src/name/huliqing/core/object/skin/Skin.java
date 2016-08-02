/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.skin;

import name.huliqing.core.data.SkinData;
import name.huliqing.core.object.DataProcessor;
import name.huliqing.core.object.actor.Actor;

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
