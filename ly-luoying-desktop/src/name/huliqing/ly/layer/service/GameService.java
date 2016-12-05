/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.layer.service;

import name.huliqing.ly.layer.network.GameNetwork;
import name.huliqing.luoying.xml.ObjectData;
import name.huliqing.luoying.object.entity.Entity;

/**
 *
 * @author huliqing
 */
public interface GameService extends GameNetwork {
    
    /**
     * 获取当前游戏主角
     * @return 
     */
    Entity getPlayer();
    
    /**
     * 把指定entity设置为当前游戏主玩家。
     * @param entity 
     */
    void setPlayer(Entity entity);
    
    /**
     * 获取当前游戏的主目标,玩家界面上的主目标
     * @return 
     */
    Entity getTarget();
    
    /**
     * 设置当前主界面的目标
     * @param target 
     */
    void setTarget(Entity target);
    
    /**
     * 退出游戏
     */
    void exitGame();
    
    /**
     * 保存该关卡为完成状态,调用该方法则保存并标记当前关卡为“完成”
     * @deprecated 
     * @param storyNum
     */
    void saveCompleteStage(int storyNum);   
    
    /**
     * 添加快捷方式
     * @param actor
     * @param data 
     */
    void addShortcut(Entity actor, ObjectData data);
    
    /**
     * 判断角色是否死亡
     * @param entity
     * @return 
     */
    boolean isDead(Entity entity);
    
    /**
     * 判断目标是否是可“转向的"
     * @param entity
     * @return 
     */
    boolean isRotatable(Entity entity);
    
    /**
     * 判断目标是否是生物
     * @param entity
     * @return 
     */
    boolean isBiology(Entity entity);
    
    /**
     * 获取目标的”所有者“，即主人。
     * @param entity
     * @return 
     */
    long getOwner(Entity entity);
    
    /**
     * 获取目标的分组,不同的分组通常意味着战斗
     * @param entity
     * @return 
     */
    int getGroup(Entity entity);
    
    /**
     * 获取目标所在的队伍
     * @param entity
     * @return 
     */
    int getTeam(Entity entity);
    
    /**
     * 获取目标的等级
     * @param entity
     * @return 
     */
    int getLevel(Entity entity);
    
    /**
     * 获取entity的当前目标对象
     * @param entity
     * @return 
     */
    long getTarget(Entity entity);
    
    /**
     * 获取角色名字
     * @param entity
     * @return 
     */
    String getName(Entity entity);
    
    /**
     * 判断一个目标target是否是entity的敌人
     * @param entity
     * @param target
     * @return 
     */
    boolean isEnemy(Entity entity, Entity target);
    
    /**
     * 获取目标的视角范围
     * @param entity
     * @return 
     */
    int getViewDistance(Entity entity);
    
    /**
     * 寻找角色周围指定范围内最近的敌人,该敌人必须是活着的，如果没有敌人，则返回null.
     * @param actor
     * @param maxDistance 
     * @return 
     */
    Entity findNearestEnemies(Entity actor, float maxDistance);
}
