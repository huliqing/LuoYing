/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.layer.network;

import com.jme3.math.Vector3f;
import name.huliqing.ly.Inject;
import name.huliqing.ly.data.ObjectData;
import name.huliqing.ly.object.actor.Actor;
import name.huliqing.ly.object.task.Task;

/**
 * 这个类主要是用来专门处理用户命令事件的。所有用户命令统一经过这个接口，
 * >>>>这个接口必须遵循以下逻辑：
 * 1.如果是client模式,则直接将用户命令发送到服务器，不处理命令。
 * 2.如果是server或single模式，则处理该命令，如果处理成功，则将该命令广播到所有客户端。
 * 否则不广播事件。
 * 注意：只有UserCommandNetwork会处理第1点，其它所有**Network类都不应该处理
 * 第1点。
 * 
 * >>>> 原则：
 * 1.注意层级关系： UserCommandNetwork -> **OtherNetwork -> **Service -> **Loader -> **Dao
 * 2.Network层只用来处理网络事件.除UserCommandNetwork允许处理游戏逻辑之外，
 * 其它**Network对游戏逻辑的处理只通过委托执行。
 * @author huliqing
 */
public interface UserCommandNetwork extends Inject {
    
    /**
     * 选择一个角色作为玩家角色
     * @param actorId 
     * @param actorName 玩家角色名称
     * @deprecated 不再使用
     */
    void selectPlayer(String actorId, String actorName);
    
    /**
     * 添加普通的玩家类角色。
     * @param actor 
     */
    void addSimplePlayer(Actor actor);
    
    /**
     * 让角色走到目标位置
     * @param actor
     * @param worldPos 
     */
    void playRunToPos(Actor actor, Vector3f worldPos);
    
    /**
     * 攻击命令
     * @param actor 攻击者
     * @param target 被攻击目标，如果为null,则让角色转入自动攻击状态 
     */
    void attack(Actor actor, Actor target);
    
    /**
     * @deprecated 
     * 使用物品
     * @param actor
     * @param data 
     */
    void useObject(Actor actor, ObjectData data);
    
    /**
     * 移除物品
     * @param actor
     * @param objectId
     * @param amount 
     */
    void removeObject(Actor actor, String objectId, int amount);
    
    /**
     * 让actor跟随目标target角色
     * @param actor
     * @param targetId 目标ID,如果为小于或等于0的值则表示清除跟随
     */
    void follow(Actor actor, long targetId);
    
    /**
     * 增加角色某个天赋的点数,注：角色必须拥有足够的可用天赋点数才能增加。
     * 否则该方法将什么也不处理，当天赋的点数增加后，角色的可用天赋将会减少。
     * @param actor 指定的角色
     * @param talentId 天赋ID
     * @param points 增加的点数，必须是正数
     */
    void addTalentPoints(Actor actor, String talentId, int points);
    
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
    
    /**
     * 接受任务
     * @param actor 
     * @param task 
     */
    void chatTaskAdd(Actor actor, Task task);
    
    /**
     * 提交已经完成的任务
     * @param actor
     * @param task 
     */
    void chatTaskComplete(Actor actor, Task task);
    
    /**
     * 切换游戏
     * @param gameId 
     */
    void changeGameState(String gameId);
}
