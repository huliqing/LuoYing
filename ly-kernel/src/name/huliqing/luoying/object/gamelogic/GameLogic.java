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
package name.huliqing.luoying.object.gamelogic;

import name.huliqing.luoying.data.GameLogicData;
import name.huliqing.luoying.xml.DataProcessor;
import name.huliqing.luoying.object.game.Game;

/**
 * 游戏逻辑
 * @author huliqing
 * @param <T>
 */
public interface GameLogic<T extends GameLogicData> extends DataProcessor<T> {

    @Override
    void setData(T data);

    @Override
    T getData();
    
    /**
     * 初始化物体
     * @param game
     */
    public void initialize(Game game);

    /**
     * 判断是否已经初始化
     * @return
     */
    public boolean isInitialized();

    /**
     * 更新Object的logic
     *
     * @param tpf
     */
    void update(float tpf);

    /**
     * 清理Logic产生的资源，当该方法被调用的时候Object已经被移出更新队列。
     */
    void cleanup();

    /**
     * 设置是否打开逻辑
     * @param enabled
     */
    void setEnabled(boolean enabled);

    /**
     * 判断逻辑是否打开
     * @return
     */
    boolean isEnabled();

}
