/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.layer.network;

import name.huliqing.luoying.Inject;
import name.huliqing.luoying.object.entity.Entity;

/**
 * @author huliqing
 */
public interface ChatNetwork extends Inject {

    /**
     * 出售或购买物品,主要用于角色从商店购买物品或者从另一角色购买物品。
     * @param seller 商品出售者
     * @param buyer 购买者
     * @param objectId 购买的物品ID
     * @param amount 购买的物品数量
     * @param discount 单件商品的折扣，比如: discount=0.5，则每件商品以50%的价钱计算。
     */
    void chatShop(Entity seller, Entity buyer, long objectId, int amount, float discount);
    
    /**
     * 发送东西给指定目标
     * @param sender 发送者
     * @param receiver 接收者
     * @param objectId 发送的物品
     * @param amount 发送的数量
     */
    void chatSend(Entity sender, Entity receiver, long objectId, int amount);
    
}
