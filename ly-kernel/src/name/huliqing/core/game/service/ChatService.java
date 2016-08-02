/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.game.service;

import name.huliqing.core.Inject;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.chat.Chat;

/**
 *
 * @author huliqing
 */
public interface ChatService extends Inject {
    
    Chat loadChat(String chatId);
    
    /**
     * 获取角色的对话面板,如果没有设置，则返回null.
     * @param actor
     * @return 
     */
    Chat getChat(Actor actor);
    
    /**
     * 购买物品
     * @param seller 商品出售者
     * @param buyer 购买者
     * @param itemId 购买的物品ID
     * @param count 购买的物品数量
     * @param discount 单件商品的折扣，比如: discount=0.5，则每件商品以50%的价钱计算。
     */
    void chatShop(Actor seller, Actor buyer, String itemId, int count, float discount);
    
    /**
     * 出售物品
     * @param seller
     * @param buyer
     * @param items
     * @param counts 
     * @param discount 物品折扣
     */
    void chatSell(Actor seller, Actor buyer, String[] items, int[] counts, float discount);
    
    /**
     * 发送东西给指定目标
     * @param sender 发送者
     * @param receiver 接收者
     * @param items 发送的物品ID列表
     * @param counts 发送的数量，与items一一对应
     */
    void chatSend(Actor sender, Actor receiver, String[] items, int[] counts);
}
