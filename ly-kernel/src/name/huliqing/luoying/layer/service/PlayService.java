/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.layer.service;

import java.util.List;
import name.huliqing.luoying.layer.network.PlayNetwork;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.game.Game;

/**
 * @author huliqing
 */
public interface PlayService extends PlayNetwork {
    
    /**
     * 注册当前游戏
     * @param game 
     */
    void registerGame(Game game);
    
    /**
     * 获取当前的游戏实例
     * @return 
     */
    Game getGame();
    
    /**
     * 从当前游戏中查找指定的实体
     * @param entityId
     * @return 
     */
    Entity getEntity(long entityId);
    
    /**
     * @param <T>
     * @param type
     * @param store
     * @return 
     */
    <T extends Entity> List<T> getEntities(Class<T> type, List<T> store);
    
    /**
     * 获取当前游戏主屏幕宽度
     * @return 
     */
    float getScreenWidth();
    
    /**
     * 获取当前游戏主屏幕高度
     * @return 
     */
    float getScreenHeight();
    
//    /**
//     * 获取场景指定位置处的高度，如果指定位置超出地面范围，则返回null.
//     * @param scene
//     * @param x
//     * @param z
//     * @return 
//     */
//    Vector3f getTerrainHeight(Scene scene, float x, float z);
    
}
