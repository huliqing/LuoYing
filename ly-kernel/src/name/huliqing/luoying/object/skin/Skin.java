/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.skin;

import com.jme3.scene.Spatial;
import name.huliqing.luoying.data.SkinData;
import name.huliqing.luoying.xml.DataProcessor;
import name.huliqing.luoying.object.entity.Entity;

/**
 * 皮肤、装备，可以是普通皮肤或者是武器。
 * @author huliqing
 */
public interface Skin extends DataProcessor<SkinData> {
    
    /**
     * 判断指定角色是否可以使用当前这件装备.
     * @param actor
     * @return 
     */
    boolean canUse(Entity actor);
    
    /**
     * 把skin添加到角色身上
     * @param actor 
     */
    void attach(Entity actor);
    
    /**
     * 把当前skin从角色身上剥离
     * @param actor 
     */
    void detach(Entity actor);
    
    /**
     * 获取skin的部位类型，注：这里返回的整数使用的是二进制位来表示skin的部分，每一个位表示一个skin类型
     * @return 
     */
    public long getParts();
    
    /**
     * 获取与当前皮肤“排斥”的其它皮肤类型，返回的二进制值中每个位(1)表示一个皮肤类型。
     * “排斥”表示如果当前这件装备穿上角色身上，则这些“冲突”的装备必须从角色身上脱下。
     * @return 
     */
    long getConflictParts();
    
    /**
     * 获取皮肤的模型节点,如果没有则返回null.
     * @return 
     */
    Spatial getSpatial();

    /**
     * 强制或提前结束skinning过程，对于一些比较特殊的装备，
     * 如果穿、脱、取、收...等装配过程是一个<b>持续动画</b>过程，<br>
     * 那么当这个过程在进行时，如果调用了{@link #forceEndSkinning() }这个方法，则应该立即停止或提前结束这个过程。
     * 装备是否正处于装配过程可以调用{@link #isSkinning() }来判断。
     */
    void forceEndSkinning();
    
    /**
     * 判断当前装备是否正在执行装配过程, 穿装备或脱装备可以是一个"过程", 
     * 也就是可以是一个有update()过程的逻辑，当装备在这个过程时这个方法应该始终返回true, 
     * 比如：一个穿装备、脱装备的动画过程; 一个取武器、收武器的动画过程。
     * @return 
     */
    boolean isSkinning();
    
//    /**
//     * 判断是否为“基本皮肤”
//     * @return 
//     */
//    boolean isBaseSkin();
//    
//    /**
//     * 判断当前Skin是否已经attach到角色身上.
//     * @return 
//     */
//    boolean isAttached();

}
