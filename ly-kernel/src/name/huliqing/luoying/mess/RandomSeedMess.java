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
package name.huliqing.luoying.mess;

import com.jme3.network.serializing.Serializable;
import name.huliqing.luoying.manager.RandomManager;
import name.huliqing.luoying.network.GameClient;

/**
 * 用于服务端和客户端之间同步随机种子。
 * @author huliqing
 */
@Serializable
public class RandomSeedMess extends GameMess {

    private int randomSeed;

    public int getRandomSeed() {
        return randomSeed;
    }

    /**
     * 设置在客户端要随机更新的种子
     * @param randomSeed 
     */
    public void setRandomSeed(int randomSeed) {
        this.randomSeed = randomSeed;
    }
    
    @Override
    public void applyOnClient(GameClient gameClient) {
        super.applyOnClient(gameClient);
        RandomManager.setRandomSeed(randomSeed);
    }
    
}
