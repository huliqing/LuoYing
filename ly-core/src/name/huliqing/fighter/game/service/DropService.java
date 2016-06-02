/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.service;

import java.util.List;
import name.huliqing.fighter.Inject;
import name.huliqing.fighter.data.DropData;
import name.huliqing.fighter.data.ProtoData;
import name.huliqing.fighter.object.actor.Actor;

/**
 * 角色物品掉落处理
 * @author huliqing
 */
public interface DropService extends Inject {
    
    /**
     * 创建一个dropData
     * @param id
     * @return 
     */
    DropData createDrop(String objectId);
    
    /**
     * 获取基本掉落的物品列表，这些物品被定义为必然掉落，即不受任何机率的影响.
     * 如果没有设置“必掉”的物品，则该方法可返回null.
     * @param store 存放结果,如果为null,则创建一个
     * @return 
     */
    List<ProtoData> getBaseDrop(Actor actor, List<ProtoData> store);
    
    /**
     * 获取随机掉落的物品，返回的物品列表将是通过掉落机率计算后的结果。掉落
     * 机率的计算： result = 物品自身掉落率 * dropFactor. 示例,如果物品的
     * 掉落率为0.3, dropFactor=0.5, 则该物品的掉落机率为: 0.15。
     * 该方法可能返回null,即通过计算机率后没有任何可掉落的物品。
     * @param dropFactor 随机掉落机率,取值[0.0~1.0]
     * @param store 存放结果
     * @return 返回通过计算后可掉落的物品。
     */
    List<ProtoData> getRandomDrop(Actor actor, float dropFactor, List<ProtoData> store);
    
    /**
     * 从角色身上获取可用于掉落的物品，包含“必掉”和“随机掉”物品
     * @param actor
     * @param store
     * @return 
     */
    List<ProtoData> getRandomDropFull(Actor actor, List<ProtoData> store);
}
