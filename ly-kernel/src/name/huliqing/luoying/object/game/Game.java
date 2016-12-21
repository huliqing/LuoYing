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

import com.jme3.app.Application;
import java.util.List;
import name.huliqing.luoying.data.GameData;
import name.huliqing.luoying.xml.DataProcessor;
import name.huliqing.luoying.object.gamelogic.GameLogic;
import name.huliqing.luoying.object.scene.Scene;

/**
 * 游戏接口
 * @author huliqing
 * @param <T>
 */
public interface Game<T extends GameData> extends DataProcessor<T> {

    @Override
    public void setData(T data);

    @Override
    public T getData();
    
    /**
     * 初始化游戏
     * @param app
     */
    void initialize(Application app);
    
    /**
     * 判断游戏是否已经初始化
     * @return 
     */
    boolean isInitialized();

    /**
     * 更新游戏逻辑
     * @param tpf 
     */
    void update(float tpf);
    
    /**
     * 清理并释放资源
     */
    void cleanup();
    
    /**
     * 打开或关闭游戏逻辑
     * @param enabled 
     */
    void setEnabled(boolean enabled);
    
    /**
     * 判断游戏逻辑是否打开或关闭
     * @return 
     */
    boolean isEnabled();
    
    /**
     * 设置游戏场景,
     * @param scene 
     */
    void setScene(Scene scene);

    /**
     * 获取当前游戏场景
     * @return 
     */
    Scene getScene();
    
    /**
     * 设置GUI场景
     * @param scene 
     */
    void setGuiScene(Scene scene);
    
    /**
     * 获取GUI场景
     * @return 
     */
    Scene getGuiScene();
    
    /**
     * 添加一个游戏逻辑
     * @param logic 
     */
    void addLogic(GameLogic logic);
    
    /**
     * 移除一个游戏逻辑
     * @param logic 
     * @return  
     */
    boolean removeLogic(GameLogic logic);
    
    /**
     * 获取当前游戏中运行的所有逻辑,返回的列表不能直接修改。增加或删除逻辑需要使用 <br>
     * {@link #addLogic(name.huliqing.ly.object.gamelogic.GameLogic) }<br>
     * {@link #removeLogic(name.huliqing.ly.object.gamelogic.GameLogic) }<br>
     * @return 
     */
    List<GameLogic> getLogics();
    
    /**
     * 添加游戏侦听器
     * @param listener 
     */
    void addListener(GameListener listener);
    
    /**
     * 移除指定的游戏侦听器，如果不存在则返回false.
     * @param listener
     * @return 
     */
    boolean removeListener(GameListener listener);

}
