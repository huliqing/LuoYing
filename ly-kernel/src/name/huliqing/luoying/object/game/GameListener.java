/*
 * LuoYing is a program used to make 3D RPG game.
 * Copyright (c) 2014-2016 Huliqing <31703299@qq.com>
 * 
 * This file is part of LuoYing.
 *
 * LuoYing is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LuoYing is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with LuoYing.  If not, see <http://www.gnu.org/licenses/>.
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
