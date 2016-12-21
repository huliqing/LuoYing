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
package name.huliqing.ly.object.game.lan;

import name.huliqing.luoying.Factory;
import name.huliqing.ly.constants.ResConstants;
import name.huliqing.ly.enums.MessageType;
import name.huliqing.luoying.layer.network.PlayNetwork;
import name.huliqing.ly.manager.ResourceManager;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.game.Game;
import name.huliqing.luoying.object.gamelogic.AbstractGameLogic;
import name.huliqing.ly.constants.IdConstants;
import name.huliqing.ly.object.view.TextView;
import name.huliqing.ly.layer.network.GameNetwork;

/**
 * 用于按时间计算等级
 * @author huliqing
 */
public class SurvivalLevelLogic extends AbstractGameLogic {
    private final PlayNetwork playNetwork = Factory.get(PlayNetwork.class);
    private final GameNetwork gameNetwork = Factory.get(GameNetwork.class);

    // 当前等级
    private int level = 1;
    private final int maxLevel;
    // 当前的运行时间,单位秒
    private float timeIntervalUsed;
    // 每隔多长时间提升敌军等级,单位秒
    private final float levelUpBySec;
    
    public SurvivalLevelLogic(float levelUpBySec, int maxLevel) {
        this.levelUpBySec = levelUpBySec;
        this.maxLevel = maxLevel;
    }

    /**
     * 获取当前的运行时间
     * @return 
     */
    public int getLevel() {
        return level;
    }
    
    public int getMaxLevel() {
        return maxLevel;
    }

    @Override
    protected void logicInit(Game game) {}
    
    @Override
    protected void logicUpdate(float tpf) {
        if (level >= maxLevel) {
            return;
        }
        
        timeIntervalUsed += tpf;
        if (timeIntervalUsed >= levelUpBySec) {
            level++;
            timeIntervalUsed = 0;
            String message = ResourceManager.get(ResConstants.COMMON_LEVEL) + " " + level;
            gameNetwork.addMessage(message, MessageType.notice);
            TextView levelView = (TextView) Loader.load(IdConstants.VIEW_TEXT_SUCCESS);
            levelView.setText(message);
            playNetwork.addEntity(levelView);
        }
    }

    @Override
    public void cleanup() {
        level = 0;
        timeIntervalUsed = 0;
        super.cleanup(); 
    }
    
}
