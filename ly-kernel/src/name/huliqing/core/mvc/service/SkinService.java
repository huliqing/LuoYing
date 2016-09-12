/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.mvc.service;

import java.util.List;
import name.huliqing.core.Inject;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.module.SkinListener;
import name.huliqing.core.object.skin.Skin;

/**
 *
 * @author huliqing
 */
public interface SkinService extends Inject {
    
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
     * @return 
     */
    boolean removeSkin(Actor actor, String skinId, int amount);
    
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
     * 判断目标角色是否可以取出武器
     * @param actor
     * @return 
     */
    boolean isCanTakeOnWeapon(Actor actor);
    
    /**
     * 判断目标角色是否可以取下武器
     * @param actor
     * @return 
     */
    boolean isCanTakeOffWeapon(Actor actor);
    
    /**
     * 判断目标角色的武器是否拿在手上。
     * @param actor
     * @return 
     */
    boolean isWeaponTakeOn(Actor actor);
    
    /**
     * 让角色取出武器。部分情况下是不能取出武器的，除非force=true,要判断在当
     * 前情况下是否可以取出武器，use {@link #isCanTakeOnWeapon(name.huliqing.fighter.object.actor.Actor) }
     * @param actor 
     * @param force 是否强制取出武器。
     */
    void takeOnWeapon(Actor actor, boolean force);
    
    /**
     * 让角色取下武器，部分情况下是不能取下武器的，除非force=true,要判断在当
     * 前情况下是否可以取下武器，use {@link #isCanTakeOffWeapon(name.huliqing.fighter.object.actor.Actor) }
     * @param actor 
     * @param force 强制解除武器
     */
    void takeOffWeapon(Actor actor, boolean force);
    
    /**
     * 获取角色所有的皮肤
     * @param actor
     * @return 
     */
    List<Skin> getSkins(Actor actor);
    
    /**
     * 获取角色当前正在使用的装备
     * @param actor
     * @return 
     */
    List<Skin> getUsingSkins(Actor actor);
    
    /**
     * 获取角色当前正在使用的武器类型状态
     * @param actor
     * @return 
     */
    int getWeaponState(Actor actor);
    
    /**
     * 添加皮肤侦听器
     * @param actor
     * @param skinListener 
     */
    void addSkinListener(Actor actor, SkinListener skinListener);
    
    /**
     * 移除皮肤侦听器
     * @param actor
     * @param skinListener
     * @return 
     */
    boolean removeSkinListener(Actor actor, SkinListener skinListener);
}
