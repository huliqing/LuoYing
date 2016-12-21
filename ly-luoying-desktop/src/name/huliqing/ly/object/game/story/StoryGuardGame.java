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
import name.huliqing.luoying.data.GameData;
import name.huliqing.ly.object.game.StoryServerNetworkRpgGame;

/**
 *
 * @author huliqing
 */
public class StoryGuardGame extends StoryServerNetworkRpgGame {
    
//    public final static int GROUP_PLAYER = 1;
    /** 敌军分组 */
    public final static int GROUP_ENEMY = 2;    // 敌军
    /** 妖精分组 */
    public final static int GROUP_FAIRY = 3;    // 妖精
    
    // 古柏的位置
    private Vector3f selfPosition;
    // 盗贼据点 
    private Vector3f enemyPosition;
    // 妖精位置
    private Vector3f fairyPosition;
    
    public int fairyLevel = 25;
    public int servantLevel = 20;
    
     @Override
    public void setData(GameData data) {
        super.setData(data);
        selfPosition = data.getAsVector3f("selfPosition");
        enemyPosition = data.getAsVector3f("enemyPosition");
        fairyPosition = data.getAsVector3f("fairyPosition");
        fairyLevel = data.getAsInteger("fairyLevel", fairyLevel);
        servantLevel = data.getAsInteger("servantLevel", servantLevel);
        
        if (selfPosition == null || enemyPosition == null || fairyPosition == null) 
            throw new NullPointerException();
    }

    @Override
    protected void doStoryInitialize() {
        addTask(new StoryGuardTask1(this));
        addTask(new StoryGuardTask2(this));
    }

    public Vector3f getSelfPosition() {
        return selfPosition;
    }

    public Vector3f getEnemyPosition() {
        return enemyPosition;
    }
    
    public Vector3f getFairyPosition() {
        return fairyPosition;
    }

    public int getFairyLevel() {
        return fairyLevel;
    }

    public int getServantLevel() {
        return servantLevel;
    }

}
