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
package name.huliqing.ly.object.game.story;

import com.jme3.math.Vector3f;
import name.huliqing.luoying.Config;
import name.huliqing.luoying.utils.MathUtils;
import name.huliqing.ly.object.game.SceneHelper;
import name.huliqing.ly.object.game.StoryServerNetworkRpgGame;

/**
 *
 * @author huliqing
 */
public class StoryTreasureGame extends StoryServerNetworkRpgGame {
    
    public Vector3f treasurePos = new Vector3f(0, 0, 0);
    public int treasureLevel = 5;
    public int groupPlayer = 1;
    public int groupEnemy = 2;
    
    private SceneHelper sceneHelper;
     
    @Override
    protected void doStoryInitialize() {
        
        sceneHelper = new SceneHelper(scene);
        
        do {
            MathUtils.getRandomPosition(75, 75, new Vector3f(), 50, treasurePos);
        } while (!sceneHelper.isInEmptyZone(treasurePos, 5, Float.MAX_VALUE, 5));
        
        addTask(new StoryTreasureTask1(this));
        addTask(new StoryTreasureTask2(this)); 
        
        if (Config.debug) {
            treasurePos = new Vector3f(-14,0.5f,62);
        }
    }

}
