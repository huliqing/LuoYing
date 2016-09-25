/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.skin;

import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.slot.Slot;

/**
 * 武器装备
 * @author huliqing
 */
public interface Weapon extends Skin {
    
    /**
     * 获取武器类型
     * @return 
     */
    String getWeaponType();
    
    /**
     * 获取当前武器占用的槽位,如果武器不支持任何槽位或者当前武器不占用任何槽位，则返回null.
     * @return 
     */
    Slot getUsingSlot();
    
    /**
     * 抽出武器
     * @param actor 
     */
    void takeOn(Actor actor);
    
    /**
     * 收起武器,
     * @param actor 
     */
    void takeOff(Actor actor);
    
}
