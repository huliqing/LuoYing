/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.module;

import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.skin.Skin;

/**
 * 监听角色的装备的穿戴
 * @author huliqing
 */
public interface SkinListener {
    
    /**
     * 当角色添加了装备之后该方法被调用。
     * @param actor
     * @param skinAdded 刚添加的装备
     */
    void onSkinAdded(Actor actor, Skin skinAdded);
    
    /**
     * 当角色装备被移除之后该方法被调用.
     * @param actor
     * @param skinRemoved 刚移除的装备
     */
    void onSkinRemoved(Actor actor, Skin skinRemoved);
    
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
