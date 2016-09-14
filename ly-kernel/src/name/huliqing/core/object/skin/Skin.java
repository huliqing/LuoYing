/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.skin;

import com.jme3.scene.Spatial;
import name.huliqing.core.data.SkinData;
import name.huliqing.core.xml.DataProcessor;
import name.huliqing.core.object.actor.Actor;

/**
 *
 * @author huliqing
 */
public interface Skin extends DataProcessor<SkinData>{
    
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
    
    /**
     * 获取skin的类型，注：这里返回的整数使用的是二进制位来表示skin的类型，每一个位表示一个skin类型。<br>
     *  注：一件skin可属于多个type,如上下连身的套装，如法袍可属于 "7,8", 二进制表示为"11000000"
     * @return 
     */
    public int getType();
    
    /**
     * 获取与当前皮肤“排斥”的其它皮肤类型，返回的二进制值中每个位(1)表示一个皮肤类型。
     * “排斥”表示如果当前这件装备穿上角色身上，则这些“冲突”的装备必须从角色身上脱下。
     * @return 
     */
    int getConflicts();
    
    /**
     * 获取皮肤的模型节点,如果没有则返回null.
     * @return 
     */
    Spatial getSpatial();
    
    /**
     * 获取武器类型,Skin必须是武器时才有意义
     * @return 
     */
    int getWeaponType();
    
    /**
     * 判断皮肤是否是一件武器
     * @return 
     */
    boolean isWeapon();
    
    /**
     * 判断是否为“基本皮肤”
     * @return 
     */
    boolean isBaseSkin();
    
    /**
     * 判断当前Skin是否已经attach到角色身上.
     * @return 
     */
    boolean isAttached();
    
    /**
     * 判断指定角色是否可以使用当前这件装备.
     * @param actor
     * @return 
     */
    boolean canUse(Actor actor);
}
