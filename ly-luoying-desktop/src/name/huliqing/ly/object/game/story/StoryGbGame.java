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
public class StoryGbGame extends StoryServerNetworkRpgGame {
    // 古柏的位置
    private Vector3f gbPosition;
    // 盗贼据点
    private Vector3f enemyPosition;
    
    // 敌人角色列表
    public String[] enemyActors;
    
    // ---- inner
    public int groupEnemy = 2;

    @Override
    public void setData(GameData data) {
        super.setData(data); //To change body of generated methods, choose Tools | Templates.
        gbPosition = data.getAsVector3f("gbPosition");
        enemyPosition = data.getAsVector3f("enemyPosition");
        if (gbPosition == null || enemyPosition == null) 
            throw new NullPointerException();
        enemyActors = data.getAsArray("enemyActors");
    }
    
    @Override
    protected void doStoryInitialize() {
        addTask(new StoryGbTask1(this));
        addTask(new StoryGbTask2(this));
    }
    
    public Vector3f getGbPosition() {
        return gbPosition;
    }

    public Vector3f getEnemyPosition() {
        return enemyPosition;
    }

    public String[] getEnemyActors() {
        return enemyActors;
    } 
    
}
