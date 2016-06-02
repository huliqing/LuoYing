/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.actor;

import name.huliqing.fighter.data.SkinData;

/**
 * 监听角色的装备的穿戴，包含武器
 * @author huliqing
 */
public interface SkinListener {
    
    /**
     * 角色穿上装备时触发
     * @param actor
     * @param data 被穿上的装备
     */
    void onSkinAttached(Actor actor, SkinData data);
    
    /**
     * 角色脱下装备时触发
     * @param actor
     * @param data 被脱下的装备
     */
    void onSkinDetached(Actor actor, SkinData data);
}
