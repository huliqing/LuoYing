/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.skin;

import name.huliqing.core.data.SkinData;
import name.huliqing.core.xml.DataProcessor;
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
     * @param isWeaponTakedOn 如果这个参数为true,则说明当前角色的武器为"取出"状态,否则为未取出或挂起状态.
     */
    void attach(Actor actor, boolean isWeaponTakedOn);
    
    /**
     * 把当前skin从角色身上剥离
     * @param actor 
     */
    void detach(Actor actor);
    
}
