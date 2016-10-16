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
     * 当游戏初始化完成时该方法被调用。
     * @param game 
     */
    void onGameInitialized(Game game);
    
    /**
     * 当游戏退出时该方法会被调用，在这个方法被调用的时候当前游戏资源还未被清理，
     * 监听这个方法来实现在游戏退出、资源被清理释放之前对一些必要的信息进行保存。
     * @param game 
     */
    void onGameExit(Game game);
    
    /**
     * 在游戏切换场景<b>前</b>该方法会被调用
     * @param game 当前游戏
     * @param oldScene 旧场景，如果是刚开始游戏，则该参数<b>可能</b>为null.
     * @param newScene 新场景
     */
    void onGameSceneChangeBefore(Game game, Scene oldScene, Scene newScene);
    
    /**
     * 当新场景切换<b>后</b>该方法被立即调用。
     * @param game 当前游戏
     * @param oldScene 旧场景，如果是刚开始游戏，则该参数<b>可能</b>为null.
     * @param newScene 新场景
     */
    void onGameSceneChangeAfter(Game game, Scene oldScene, Scene newScene);
    
    
}
