/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.game;

import name.huliqing.luoying.object.scene.Scene;

/**
 * 游戏侦听器
 * @author huliqing
 */
public interface GameListener {
    
    /**
     * 当游戏场景载入完毕时该方法会被调用，当游戏初始载入场景完毕时该方法会被调用，
     * 当游戏过程中切换场景时该方法也会被调用，也就是该方法可能不只调用一次，除非游戏过程中不切换场景。
     * @param game 
     */
    void onGameSceneLoaded(Game game);

    /**
     * 在游戏场景<b>切换前</b>该方法会被调用。
     * @param game 当前游戏
     * @param oldScene 旧场景
     * @param newScene 新场景
     */
    void onGameSceneChange(Game game, Scene oldScene, Scene newScene);
        
    /**
     * 当游戏退出时该方法会被调用，在这个方法被调用的时候当前游戏资源还未被清理，
     * 监听这个方法来实现在游戏退出、资源被清理释放之前对一些必要的信息进行保存。
     * @param game 
     */
    void onGameExit(Game game);
    
}
