/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.game.service;

import java.util.List;
import name.huliqing.core.Inject;
import name.huliqing.core.data.SkinData;
import name.huliqing.core.object.actor.Actor;

/**
 *
 * @author huliqing
 */
public interface SkinService extends Inject {
    
    /**
     * 给角色换上装备,注：换装备的时候需要考虑冲突的装备，并把冲突的装备换
     * 下来
     * @param actor
     * @param skinData 
     */
    void attachSkin(Actor actor, SkinData skinData);
    
    /**
     * 脱下角色的装备，注：脱下装备时需要判断及补上缺失的装备
     * @param actor
     * @param skinData 
     */
    void detachSkin(Actor actor, SkinData skinData);
    
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
     * 让角色取出武器。部分情况下是不能取出武器的，除非force=true,要判断在当
     * 前情况下是否可以取出武器，use {@link #isCanTakeOnWeapon(name.huliqing.fighter.object.actor.Actor) }
     * @param actor 
     * @param force 是否强制取出武器。
     * @return 取出武器成功true，否则false
     */
    boolean takeOnWeapon(Actor actor, boolean force);
    
    /**
     * 让角色取下武器，部分情况下是不能取下武器的，除非force=true,要判断在当
     * 前情况下是否可以取下武器，use {@link #isCanTakeOffWeapon(name.huliqing.fighter.object.actor.Actor) }
     * @param actor 
     * @param force 强制解除武器
     * @return 取下武器成功true，否则false
     */
    boolean takeOffWeapon(Actor actor, boolean force);
    
    /**
     * 获取角色包裹中所有的装备(不包含武器，也不包含基本皮肤中的装备)
     * @param actor
     * @param store 存放结果
     * @return 
     */
    List<SkinData> getArmorSkins(Actor actor, List<SkinData> store);
    
    /**
     * 获取角色包裹中所有的武器(不包含基本皮肤中的武器）
     * @param actor
     * @param store
     * @return 
     */
    List<SkinData> getWeaponSkins(Actor actor, List<SkinData> store);
    
    /**
     * 获取角色当前正在使用的武器,
     * 角色可能同时正在使用多把武器，如果没有使用任何武器则返回empty.
     * 禁止手动删除该列表中的数据。
     * @param actor
     * @return 
     */
    List<SkinData> getCurrentWeaponSkin(Actor actor);
    
    /**
     * 获取角色当前正在使用的武器类型
     * @param actor
     * @return 
     */
    int getWeaponState(Actor actor);
    
    /**
     * 判断目标角色的武器是否拿在手上。
     * @param actor
     * @return 
     */
    boolean isWeaponTakeOn(Actor actor);
    
    /**
     * 判断目标Skin是否为一把武器
     * @param skinData
     * @return 
     */
    boolean isWeapon(SkinData skinData);
    
}
