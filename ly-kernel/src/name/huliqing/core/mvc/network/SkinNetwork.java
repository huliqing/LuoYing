/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.mvc.network;

import name.huliqing.core.Inject;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.skin.Skin;

/**
 * @author huliqing
 */
public interface SkinNetwork extends Inject {
    
    /**
     * 给角色添加skin装备
     * @param actor
     * @param skinId
     * @param amount 
     */
    void addSkin(Actor actor, String skinId, int amount);
    
    /**
     * 移除角色身上的skin装备。
     * @param actor
     * @param skinId
     * @param amount
     */
    void removeSkin(Actor actor, String skinId, int amount);
    
    /**
     * 给角色换上装备,注：换装备的时候需要考虑冲突的装备，并把冲突的装备换
     * 下来
     * @param actor
     * @param skin
     */
    void attachSkin(Actor actor, Skin skin);
    
    /**
     * 脱下角色的装备，注：脱下装备时需要判断及补上缺失的装备
     * @param actor
     * @param skin
     */
    void detachSkin(Actor actor, Skin skin);
    
    /**
     * 让角色取出武器。
     * @param actor 
     */
    void takeOnWeapon(Actor actor);
    
    /**
     * 让角色收起武器
     * @param actor 
     */
    void takeOffWeapon(Actor actor);
    
}
