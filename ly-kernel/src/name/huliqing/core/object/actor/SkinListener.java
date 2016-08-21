/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.actor;

import name.huliqing.core.data.SkinData;
import name.huliqing.core.object.skin.Skin;

/**
 * 监听角色的装备的穿戴，包含武器
 * @author huliqing
 */
public interface SkinListener {
    
    /**
     * 当角色添加了装备之后该方法被调用。
     * @param actor
     * @param data 添加后的装备
     */
    void onSkinAdded(Actor actor, SkinData data);
    
    /**
     * 当角色装备被移除之后该方法被调用.
     * @param actor
     * @param data 移除后的装备，如果该装备的数量小于或等于0，则该装备可能已经被完全移除出角色身上。
     */
    void onSkinRemoved(Actor actor, SkinData data);
    
    /**
     * 角色穿上装备后触发
     * @param actor
     * @param skin 被穿上的装备
     */
    void onSkinAttached(Actor actor, Skin skin);
    
    /**
     * 角色脱下装备后触发
     * @param actor
     * @param skin 被脱下的装备
     */
    void onSkinDetached(Actor actor, Skin skin);
}
