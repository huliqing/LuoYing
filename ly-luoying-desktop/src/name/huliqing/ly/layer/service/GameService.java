/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.layer.service;

import name.huliqing.ly.layer.network.GameNetwork;
import name.huliqing.luoying.data.ObjectData;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.ly.object.NetworkObject;

/**
 *
 * @author huliqing
 */
public interface GameService extends GameNetwork {
    
    /**
     * 从场景中获取同步物体
     * @param objectId
     * @return 
     */
    NetworkObject findSyncObject(long objectId);
    
    /**
     * 获取当前游戏主角
     * @return 
     */
    Entity getPlayer();
    
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
}
